package com.yunkouan.wms.modules.assistance.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XmlFriendlyReplacer;
import com.thoughtworks.xstream.io.xml.XppDriver;
import com.yunkouan.exception.BizException;
import com.yunkouan.saas.modules.sys.service.ISysParamService;
import com.yunkouan.util.FileUtil;
import com.yunkouan.util.NumberUtil;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.common.constant.BillPrefix;
import com.yunkouan.wms.common.constant.CacheName;
import com.yunkouan.wms.common.dao.INoDao;
import com.yunkouan.wms.common.entity.Sequence;
import com.yunkouan.wms.common.util.IdUtil;
import com.yunkouan.wms.modules.assistance.dao.InvAssisLogDao;
import com.yunkouan.wms.modules.assistance.entity.InvAssisLog;
import com.yunkouan.wms.modules.assistance.vo.MessageBody;
import com.yunkouan.wms.modules.assistance.vo.MessageData;
import com.yunkouan.wms.modules.assistance.vo.MessageHead;
import com.yunkouan.wms.modules.assistance.vo.RequestMessage;
import com.yunkouan.wms.modules.assistance.vo.WarehouseHead;
import com.yunkouan.wms.modules.assistance.vo.WarehouseRtnDocument;
import com.yunkouan.wms.modules.assistance.vo.WarehouseRtnHead;
import com.yunkouan.wms.modules.rec.service.IPutawayService;
import com.yunkouan.wms.modules.rec.vo.RecPutawayDetailVO;
import com.yunkouan.wms.modules.rec.vo.RecPutawayLocationVO;
import com.yunkouan.wms.modules.rec.vo.RecPutawayVO;
import com.yunkouan.wms.modules.send.service.IPickService;
import com.yunkouan.wms.modules.send.vo.SendDeliveryVo;
import com.yunkouan.wms.modules.send.vo.SendPickDetailVo;
import com.yunkouan.wms.modules.send.vo.SendPickLocationVo;
import com.yunkouan.wms.modules.send.vo.SendPickVo;

import jcifs.smb.SmbFile;

/** 
* @Description: 海关辅助系统接口（哈尔滨实现）
* @author tphe06
* @date 2018年7月26日 上午11:16:53  
*/
@Service(value="assisService")
public class AssisServiceImpl implements IAssisService {
	private static Log log = LogFactory.getLog(AssisServiceImpl.class);

	@Autowired
	private INoDao seqDao;
	@Autowired
	private InvAssisLogDao dao;

	@Autowired
	private IPickService pickService;
	@Autowired
	private IPutawayService putawayService;

	@Autowired
	private ISysParamService paramService;

	/** 
	* @Title: request 
	* @Description: 把出库单发送给海关辅助系统
	* @auth tphe06
	* @time 2018 2018年7月26日 上午10:34:02
	* @param vo
	* @throws IOException
	*/
	@Deprecated
	public void request(SendDeliveryVo vo) throws IOException {
		List<MessageData> list = new ArrayList<MessageData>();
		if(vo.getSendPickVo() != null && vo.getSendPickVo().getSendPickDetailVoList() != null) for(SendPickDetailVo detail : vo.getSendPickVo().getSendPickDetailVoList()) {
			if(detail.getRealPickLocations() != null) for(SendPickLocationVo real : detail.getRealPickLocations()) {
				MessageData data = new MessageData();
				data.setLocationComment(real.getLocationComment());
				data.setLocationNo(real.getLocationNo());
				data.setMeasureUnit(real.getSendPickLocation().getMeasureUnit());
				data.setQty(NumberUtil.rounded(real.getSendPickLocation().getPickQty(), 0));
				data.setSkuNo(detail.getSkuNo());
				list.add(data);
			}
		}
		request(vo.getSendDelivery().getOrgId(), BILL_TYPE_PUTAWAY, vo.getSendDelivery().getDeliveryId(), true, list);
	}

	/** 
	* @Title: response 
	* @Description: 出入库回执接口
	* @auth tphe06
	* @time 2018 2018年7月26日 下午1:59:12
	* @throws IOException
	*/
	public void response() throws IOException {
		//文件夹路径
		String docPath = paramService.getKey(CacheName.ASSIS_DOC_PATH_RSP);
		String backPath = paramService.getKey(CacheName.ASSIS_DOC_PATH_BACK);
//		String backRemotePath = paramService.getKey(CacheName.ASSIS_DOC_PATH_REMOTE_BACK);
//		File doc = new File(docPath);
//		if(!doc.exists()) doc.mkdirs();
		File doc1 = new File(backPath);
		if(!doc1.exists()) doc1.mkdirs();
		//读取文件
		SmbFile[] files = FileUtil.listFiles(docPath, "^WMSR_[\\d]+_WMS_[\\d]+_[\\d]{4}.xml$");
		if(files == null) return;
		for(SmbFile file : files) {
			//解析文件
			String xml = FileUtil.smbRead(file);
			RequestMessage message = fromXML(xml);
			if(message == null || message.getMessageBody() == null || message.getMessageHead() == null) continue;
			if(message.getMessageBody().getWarehouseRtnDocument() == null) continue;
			if(message.getMessageBody().getWarehouseRtnDocument().getWarehouseRtnHead() == null) continue;
			// 根据消息id查找上架单、拣货单信息，然后更新处理结果
			String msgId = message.getMessageHead().getMESSAGE_ID();
			String retCode = message.getMessageBody().getWarehouseRtnDocument().getWarehouseRtnHead().getRESULT_CODE();
			String retMsg = message.getMessageBody().getWarehouseRtnDocument().getWarehouseRtnHead().getRESULT_MESSAGE();
			//查询消息详情
			InvAssisLog log = new InvAssisLog();
			log.setMessageId(msgId);
			log = dao.selectOne(log);
			if(log == null || "1".equals(log.getResult())) continue;
			String billType = log.getBillType();
			String billId = log.getBillId();
			//更新上架单、拣货单发送状态
			if(BILL_TYPE_PICKUP.equals(billType)) {
				String status = "1".equals(retCode)?IPickService.ASSIS_STATUS_SUCCESS:IPickService.ASSIS_STATUS_FAILED;
				pickService.updateAssisStatus(billId, status);
			}
			if(BILL_TYPE_PUTAWAY.equals(billType)) {
				String status = "1".equals(retCode)?IPutawayService.ASSIS_STATUS_SUCCESS:IPutawayService.ASSIS_STATUS_FAILED;
				putawayService.updateAssisStatus(billId, status);
			}
			//更新日志
			log.setResult(retCode);
			log.setMessage(retMsg);
			dao.updateByPrimaryKeySelective(log);
		}
		//文件备份
		for(SmbFile file : files) {
			if(log.isInfoEnabled()) log.info(file.getPath());
			FileUtil.smbGet(file.getPath(), backPath+"/"+file.getName());
//			file.renameTo(new SmbFile(backRemotePath+"/"+file.getName()));
			file.delete();
		}
	}

	/** 
	* @Title: fromXML 
	* @Description: 把XML字符串转换成消息对象
	* @auth tphe06
	* @time 2018 2018年7月26日 下午1:38:57
	* @param xml
	* @return
	* RequestMessage
	*/
	private static RequestMessage fromXML(String xml) {
		XStream x = new XStream(new XppDriver(new XmlFriendlyReplacer("-_", "_")));
		x.alias("RequestMessage", RequestMessage.class);
		x.alias("MessageHead", MessageHead.class);
		x.alias("MessageBody", MessageBody.class);
		x.alias("WarehouseHead", WarehouseHead.class);
		x.alias("WarehouseRtnDocument", WarehouseRtnDocument.class);
		x.alias("WarehouseRtnHead", WarehouseRtnHead.class);
		return (RequestMessage)x.fromXML(xml);
	}

	/** 
	* @Title: toXML 
	* @Description: 把消息对象转化成XML字符串
	* @auth tphe06
	* @time 2018 2018年7月26日 上午10:33:07
	* @param message
	* @return
	* String
	*/
	private static String toXML(RequestMessage message) {
		XStream x = new XStream(new XppDriver(new XmlFriendlyReplacer("-_", "_")));
		x.alias("RequestMessage", RequestMessage.class);
		x.alias("MessageHead", MessageHead.class);
		x.alias("MessageBody", MessageBody.class);
		x.alias("WarehouseHead", WarehouseHead.class);
		x.alias("WarehouseRtnDocument", WarehouseRtnDocument.class);
		x.alias("WarehouseRtnHead", WarehouseRtnHead.class);
		String xml = x.toXML(message);
		return xml;
	}

	/** 
	* @Title: getSeq 
	* @Description: 生成流水，支持系统集群，利用数据库行锁
	* @auth tphe06
	* @time 2018 2018年7月26日 上午10:31:00
	* @param seqName
	* @param orgid
	* @return
	* int
	*/
	private int getSeq(String seqName, String orgid) {
		/**1查询当前数值**/
		Sequence current = seqDao.selectOne(new Sequence().setOrgId(orgid).setSeqName(seqName));
		if(current != null) {
			/**3更新数据库**/
			int result = seqDao.increaseSequence(current);
			if(result == 0) throw new BizException("err_sytem_error");
			return current.getCurrentValue()+1;
		} else {
			/**3新增一笔数据**/
			Sequence s = new Sequence();
			s.setOrgId(orgid);
			s.setSeqName(seqName);
			s.setsIncrement(1);
			s.setCurrentValue(1);
			seqDao.insertSelective(s);
			return 1;
		}
	}

	/** 
	* @Title: request 
	* @Description: 通用出入库请求接口
	* @auth tphe06
	* @time 2018 2018年7月26日 上午10:32:39
	* @param orgid
	* @param list
	* @throws IOException
	*/
	public void request(String orgid, String billType, String billId, boolean isOut, List<MessageData> list) throws IOException {
		if(list == null || list.isEmpty()) return;
		//生成文件名
		String docPath = paramService.getKey(CacheName.ASSIS_DOC_PATH_REQ);
//		File doc = new File(docPath);
//		if(!doc.exists()) doc.mkdirs();
		String messageType = paramService.getKey(CacheName.ASSIS_MESSAGE_TYPE);
		String seqName = BillPrefix.DOCUMENT_PREFIX_ASSIS_FILE_NAME.getPrefix();
		int seq = getSeq(seqName, orgid);
		String fileSeq = StringUtil.fillZero(String.valueOf(seq), 4);
		String time = String.valueOf(System.currentTimeMillis());
		String time1 = DateFormatUtils.format(new Date(), "yyyyMMddHHmmss");
		StringBuilder fileName = new StringBuilder();
		fileName.append(docPath).append("/").append(messageType).append("_").append(time).append("_").append(fileSeq).append(".xml");
		//生成消息id
		String stationCode = StringUtil.fillZero(paramService.getKey(CacheName.ASSIS_STATION_CODE), 6);
		String tradeCode = StringUtil.fillZero(paramService.getKey(CacheName.ASSIS_TRADE_CODE), 10);
		String seqName1 = BillPrefix.DOCUMENT_PREFIX_ASSIS_MESSAGE_ID.getPrefix();
		int seq1 = getSeq(seqName1, orgid);
		String msgSeq = StringUtil.fillZero(String.valueOf(seq1), 15);
		String messageId = new StringBuilder().append("W").append(stationCode).append(tradeCode).append(msgSeq).toString();
		//发送方代码
		StringBuilder source = new StringBuilder();
		source.append(CacheName.ASSIC_SOURCE_CODE).append(stationCode).append(tradeCode);
		//接收方代码
		StringBuilder dest = new StringBuilder();
		dest.append(CacheName.ASSIC_DEST_CODE).append(stationCode);
		//帐册编号
//		String bookNo = paramService.getKey(CacheName.ASSIC_BOOK_NO);
		//XML报文
		RequestMessage msg = new RequestMessage();
		msg.getMessageHead().setMESSAGE_ID(messageId);
		msg.getMessageHead().setMESSAGE_TYPE(CacheName.ASSIC_SOURCE_CODE);
		msg.getMessageHead().setMESSAGE_TIME(time1);
		msg.getMessageHead().setMESSAGE_SOURCE(source.toString());
		msg.getMessageHead().setMESSAGE_DEST(dest.toString());
		for(MessageData data : list) {
			WarehouseHead body = new WarehouseHead();
			body.setWAREHOUSE_NO(data.getLocationNo());//20位库位编号
			body.setCFS_NO(stationCode);//6位场站编号
			body.setTRADE_COM(tradeCode);//10位企业编号
			body.setWAREHOUSR_INFO(StringUtils.trimToEmpty(data.getLocationComment()));//库位信息
			body.setBOOK_NO(StringUtils.trimToEmpty(data.getBookNo()));//12位帐册编号
			body.setMATERIAL_CODE(data.getSkuNo());//20位料号、海关备案货品编号
			body.setMATERIAL_TYPE(data.getMaterialType());//L料件；C成品
			body.setREPORT_COUNT(data.getQty());//数量；最大18位数字，保留5位小数
			body.setREPORT_UNIT(StringUtils.trimToEmpty(data.getMeasureUnit()));//中文单位
			body.setIN_OR_EXIT(isOut?"E":"I");//I入库；E出库
			body.setGOODS_TYPE(data.getGoodType());//001保税货物；002非保税货物；003口岸货物；004跨境货物
			msg.getMessageBody().getWarehouseDocument().add(body);
		}
		msg.getMessageBody().setWarehouseRtnDocument(null);
		String xml = toXML(msg);
		//写日志
		InvAssisLog log = new InvAssisLog();
		log.setBillType(billType);
		log.setBillId(billId);
		log.setEnterOrExit(isOut);
		log.setId(IdUtil.getUUID());
		log.setMessageId(messageId);
		dao.insert(log);
		//写文件
		FileUtil.smbWrite(fileName.toString(), xml);
	}

	/** 
	* @Title: request 
	* @Description: 发送入库单（收货单+上架单）
	* @auth tphe06
	* @time 2018 2018年7月26日 上午10:59:41
	* @param vo
	* @throws IOException
	*/
	public void request(RecPutawayVO vo) throws IOException {
		List<MessageData> list = new ArrayList<MessageData>();
		if(vo.getListPutawayDetailVO() != null) for(RecPutawayDetailVO detail : vo.getListPutawayDetailVO()) {
			if(detail.getListRealLocationVO() != null) for(RecPutawayLocationVO real : detail.getListRealLocationVO()) {
				MessageData data = new MessageData();
				data.setLocationComment(real.getLocationComment());
				data.setLocationNo(real.getRealLocationNo());
				data.setMeasureUnit(real.getPutawayLocation().getMeasureUnit());
				data.setQty(NumberUtil.rounded(real.getPutawayLocation().getPutawayQty(), 0));
				data.setSkuNo(detail.getSkuNo());
				list.add(data);
			}
		}
		request(vo.getPutaway().getOrgId(), BILL_TYPE_PUTAWAY, vo.getPutaway().getPutawayId(), false, list);
	}

	/** 
	* @Title: request 
	* @Description: 发送出库单（发货单+拣货单）
	* @auth tphe06
	* @time 2018 2018年7月26日 上午10:59:24
	* @param vo
	* @throws IOException
	*/
	public void request(SendPickVo vo) throws IOException {
		List<MessageData> list = new ArrayList<MessageData>();
		if(vo.getSendPickDetailVoList() != null) for(SendPickDetailVo detail : vo.getSendPickDetailVoList()) {
			if(detail.getRealPickLocations() != null) for(SendPickLocationVo real : detail.getRealPickLocations()) {
				MessageData data = new MessageData();
				data.setLocationComment(real.getLocationComment());
				data.setLocationNo(real.getLocationNo());
				data.setMeasureUnit(detail.getMeasureUnit());
				data.setQty(NumberUtil.rounded(real.getSendPickLocation().getPickQty(), 0));
				data.setSkuNo(detail.getSkuNo());
				data.setBookNo(detail.getBookNo());
				list.add(data);
			}
		}
		request(vo.getSendPick().getOrgId(), BILL_TYPE_PICKUP, vo.getSendPick().getPickId(), true, list);
	}

	public void request(String orgid, String billType, String billId, boolean isOut, MessageData data) throws IOException {
		List<MessageData> list = new ArrayList<MessageData>();
		list.add(data);
		this.request(orgid, billType, billId, isOut, list);
	}
}