package com.yunkouan.wms.modules.send.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunkouan.util.SpringContextHolder;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.modules.send.entity.SysExpressNoPool;
import com.yunkouan.wms.modules.send.service.AbstractExpressService;
import com.yunkouan.wms.modules.send.service.IDeliveryService;
import com.yunkouan.wms.modules.send.service.IExpressService;
import com.yunkouan.wms.modules.send.service.ISysExpressNoPoolService;
import com.yunkouan.wms.modules.send.vo.SendDeliveryVo;

@Service
public class ExpressServiceImpl implements IExpressService {
	@Autowired
	private ISysExpressNoPoolService expressPoolService;
	@Autowired
	private IDeliveryService deliveryService;

	/** 
	* @Title: getLogisticsNo 
	* @Description: 获取物流公司运单号
	* @auth tphe06
	* @time 2018 2018年4月19日 下午6:03:08
	* @param expressCode
	* @return
	* @throws IOException
	* @throws Exception
	*/
	@Override
	@Transactional(readOnly = false)
	public String getLogisticsNo(String expressCode) throws IOException, Exception {
		//生成运单号
		SysExpressNoPool pool = new SysExpressNoPool();
		pool.setExpressServiceCode(expressCode);
		pool.setIsUsed(false);
		String billNo = expressPoolService.selectOne(pool);
		return billNo;
	}

	/** 
	* @Title: sendDeliveryToLogistics 
	* @Description: 批量将发货单发送给物流公司，返回推送成功的记录
	* @auth tphe06
	* @time 2018 2018年4月19日 下午6:03:17
	* @param vos
	* @return
	* @throws Exception
	*/
	@Override
	@Transactional(readOnly = false)
	public List<SendDeliveryVo> sendDeliveryToLogistics(List<SendDeliveryVo> vos) throws Exception {
		// 推送物流公司
		List<SendDeliveryVo> list = new ArrayList<SendDeliveryVo>();
		Map<String, List<SendDeliveryVo>> map = hash(fill(vos));
		for(Iterator<String> it = map.keySet().iterator(); it.hasNext(); ) {
			String code = it.next();
			AbstractExpressService service = SpringContextHolder.getBean(new StringBuilder(code).append("ServiceImpl").toString());
			if(service == null) continue;
			List<SendDeliveryVo> result = service.push(map.get(code));
			if(result != null) list.addAll(result);
		}
		updateStatus(list, Constant.SEND_WULIU_SUCCESS);//推送
		return list;
	}
	/** 
	* @Title: updateStatus 
	* @Description: 更新发送EMS状态
	* @auth tphe06
	* @time 2018 2018年4月19日 下午5:28:27
	* @param list
	* void
	*/
	private void updateStatus(List<SendDeliveryVo> list, int status) {
		for(SendDeliveryVo vo : list){
			int result = deliveryService.updateSendStatus(vo.getSendDelivery().getDeliveryId(), status,vo.getSendDelivery().getExpressBigchar(),vo.getSendDelivery().getExpressGatheringPlace());
			if(result > 0) vo.getSendDelivery().setSendStatus(status);
		}
	}
	/** 
	* @Title: fill 
	* @Description: 排除状态不符合推送条件的
	* @auth tphe06
	* @time 2018 2018年4月19日 下午4:46:57
	* @param vos
	* @return
	* List<SendDeliveryVo>
	*/
	private static List<SendDeliveryVo> fill(List<SendDeliveryVo> vos) {
		List<SendDeliveryVo> list = new ArrayList<SendDeliveryVo>();
		for(SendDeliveryVo vo : vos) {
			Integer deliveryStatus = vo.getSendDelivery().getDeliveryStatus();
			Integer status = vo.getSendDelivery().getSendStatus();
			if(status==null) status=0;
			if(deliveryStatus==null) status=0;
//			//排除不符合推送条件的
			if( 10==deliveryStatus||//打开
				//20==deliveryStatus||//生效
				90==deliveryStatus||//部分拣货
				95==deliveryStatus||//查验
				97==deliveryStatus||//销毁
				98==deliveryStatus||//退货
				99==deliveryStatus//取消
//				0==status||//未获取运单号
//				2==status||//已经给快递公司发送
//				3<=status//已经给erp发送
			) { continue; }
			list.add(vo);
		}
		return list;
	}
	/** 
	* @Title: hash 
	* @Description: 按照物流公司进行分组
	* @auth tphe06
	* @time 2018 2018年4月19日 下午3:43:20
	* @param list
	* @return
	* Map<String,List<SendDeliveryVo>>
	*/
	private static Map<String, List<SendDeliveryVo>> hash(List<SendDeliveryVo> list) {
		Map<String, List<SendDeliveryVo>> map = new HashMap<String, List<SendDeliveryVo>>();
		String code;
		for(SendDeliveryVo vo : list) {
			code = vo.getSendDelivery().getExpressServiceCode();
			if(map.get(code) == null) {
				map.put(code, new ArrayList<SendDeliveryVo>());
			}
			map.get(code).add(vo);
		}
		return map;
	}
}