package com.yunkouan.wms.modules.intf.timer;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.github.pagehelper.Page;
import com.yunkouan.saas.modules.sys.service.ISysParamService;
import com.yunkouan.util.SpringContextHolder;
import com.yunkouan.util.XmlUtil;
import com.yunkouan.wms.common.constant.CacheName;
import com.yunkouan.wms.common.util.IdUtil;
import com.yunkouan.wms.common.util.MSMQUtil;
import com.yunkouan.wms.modules.intf.vo.Body;
import com.yunkouan.wms.modules.intf.vo.Head;
import com.yunkouan.wms.modules.intf.vo.MSMQ;
import com.yunkouan.wms.modules.inv.service.IStockService;
import com.yunkouan.wms.modules.inv.vo.InvStockVO;

/**
 * 库存查询任务的执行
 * @author tphe06
 */
@DisallowConcurrentExecution
public class QueryStockTask extends QuartzJobBean {
	private static Log log = LogFactory.getLog(QueryStockTask.class);

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		if(log.isDebugEnabled()) log.debug("运行【库存查询】定时任务...");
		try {
			//数据查询
			IStockService service = SpringContextHolder.getBean(IStockService.class);
			ISysParamService paramService = SpringContextHolder.getBean(ISysParamService.class);
			String emsNo = paramService.getKey(CacheName.PARAM_EMS_NO);
			String revId = paramService.getKey(CacheName.PARAM_RECEIVER_ID);
			String sendId = paramService.getKey(CacheName.PARAM_SENDER_ID);
			String fullName = paramService.getKey(CacheName.PARAM_FULL_NAME);
			String label = paramService.getKey(CacheName.PARAM_MESSAGE_LABEL);
			if(log.isDebugEnabled()) log.debug("fullName:"+fullName);
			if(log.isDebugEnabled()) log.debug("label:"+label);
			int currentPage = 0;
			int pageSize = 2000;//2000
			String id = IdUtil.getUUID();
			do {
				InvStockVO vo = new InvStockVO();
				vo.setCurrentPage(currentPage);
				vo.setPageSize(pageSize);
				Page<InvStockVO> page = service.listByPageNoLogin(vo);
				List<InvStockVO> list = page.getResult();
				if(list == null || list.size() == 0) {
					if(log.isDebugEnabled()) log.debug("无库存记录");
					break;
				}
				//封装报文
				MSMQ mq = new MSMQ();
				Head head = new Head();
				head.setMESSAGE_ID(id);
				head.setMESSAGE_TYPE("WHKJAMOUNT");
				head.setEMS_NO(StringUtils.defaultString(emsNo));
				Date today = new Date();
//				String datetime = DateFormatUtils.format(today, "yyyy-MM-dd HH:mm:ss");
				String date = DateFormatUtils.format(today, "yyyy-MM-dd");
				head.setMESSAGE_DATE(date);//YYYY-MM-DD
				head.setRECEIVER_ID(StringUtils.defaultString(revId));
				head.setSENDER_ID(StringUtils.defaultString(sendId));
				mq.setHead(head);
				for(InvStockVO v : list) {
					Body d = new Body();
					d.setMESSAGE_ID(id);
					d.setEMS_NO(StringUtils.defaultString(emsNo));
					d.setCOP_G_NO(StringUtils.defaultString(v.getSkuNo()));//货品代码
					d.setG_NO(v.getSku().getgNo());//项号，海关归类税号
					d.setQTY(String.valueOf(v.getInvStock().getStockQty()));//数量
					d.setUNIT(StringUtils.defaultString(v.getMeasureUnitCode()));//计量单位
					d.setSTOCK_DATE(date);//实盘日期（YYYY-MM-DD）
					d.setGOODS_NATURE("1");
					d.setWHS_CODE(StringUtils.defaultString(v.getAreaNo()));//库区
					d.setLOCATION_CODE(StringUtils.defaultString(v.getLocationNo()));//库位
					mq.addBody(d);
				}
				//发送消息
				boolean result = MSMQUtil.send(fullName, label, toXML(mq), id.getBytes());
				if(log.isInfoEnabled()) log.info("MSMQ接口运行结果：["+result+"]");
				currentPage += 1;
				if(currentPage >= page.getPages()) {
					if(log.isDebugEnabled()) log.debug("库存数据传输完毕");
					break;
				}
			} while(true);
		} catch(Throwable e) {
			if(log.isErrorEnabled()) log.error(e.getMessage(), e);
		}
		if(log.isDebugEnabled()) log.debug("【库存查询】定时任务完成.");
	}

	private static String toXML(MSMQ data) {
		Document d = XmlUtil.createDocument();
		Element root = XmlUtil.createElement(d, "ITF_WHKJ_TRANSMIT");
		XmlUtil.createElement(root, "MESSAGE_ID", data.getHead().getMESSAGE_ID());
		XmlUtil.createElement(root, "MESSAGE_TYPE", data.getHead().getMESSAGE_TYPE());
		XmlUtil.createElement(root, "EMS_NO", data.getHead().getEMS_NO());
		XmlUtil.createElement(root, "ORDER_NO", data.getHead().getORDER_NO());
		XmlUtil.createElement(root, "FUNCTION_CODE", data.getHead().getFUNCTION_CODE());
		XmlUtil.createElement(root, "CHK_RESULT", data.getHead().getCHK_RESULT());
		XmlUtil.createElement(root, "MESSAGE_DATE", data.getHead().getMESSAGE_DATE());

		XmlUtil.createElement(root, "SENDER_ID", data.getHead().getSENDER_ID());
		XmlUtil.createElement(root, "SEND_ADDRESS", data.getHead().getSEND_ADDRESS());
		XmlUtil.createElement(root, "RECEIVER_ID", data.getHead().getRECEIVER_ID());
		XmlUtil.createElement(root, "RECEIVER_ADDRESS", data.getHead().getRECEIVER_ADDRESS());
		XmlUtil.createElement(root, "MESSAGE_SIGN", data.getHead().getMESSAGE_SIGN());
		XmlUtil.createElement(root, "SEND_TYPE", data.getHead().getSEND_TYPE());

		data.getBodys().forEach(t->{
			Element c = XmlUtil.createElement(root, "ITF_WHKJ_STORE_AMOUNT");
			XmlUtil.createElement(c, "MESSAGE_ID", data.getHead().getMESSAGE_ID());
			XmlUtil.createElement(c, "EMS_NO", t.getEMS_NO());
			XmlUtil.createElement(c, "COP_G_NO", t.getCOP_G_NO());
			XmlUtil.createElement(c, "G_NO", t.getG_NO());
			XmlUtil.createElement(c, "QTY", t.getQTY());
			XmlUtil.createElement(c, "UNIT", t.getUNIT());
			XmlUtil.createElement(c, "STOCK_DATE", t.getSTOCK_DATE());
			XmlUtil.createElement(c, "GOODS_NATURE", t.getGOODS_NATURE());
			XmlUtil.createElement(c, "BELONG", t.getBELONG());
			XmlUtil.createElement(c, "GOODS_FORM", t.getGOODS_FORM());
			XmlUtil.createElement(c, "BOM_VERSION", t.getBOM_VERSION());
			XmlUtil.createElement(c, "DATA_TYPE", t.getDATA_TYPE());
			XmlUtil.createElement(c, "WHS_CODE", t.getWHS_CODE());
			XmlUtil.createElement(c, "LOCATION_CODE", t.getLOCATION_CODE());
			XmlUtil.createElement(c, "NOTE", t.getNOTE());
		});

		String xml = XmlUtil.toXML(d);
		if(log.isDebugEnabled()) log.debug(xml);
		return xml;
	}
}