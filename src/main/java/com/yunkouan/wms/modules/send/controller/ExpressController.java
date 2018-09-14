package com.yunkouan.wms.modules.send.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunkouan.base.BaseController;
import com.yunkouan.entity.ResultModel;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.common.strategy.StrategyContext;
import com.yunkouan.wms.modules.send.entity.SendDelivery;
import com.yunkouan.wms.modules.send.service.IDeliveryService;
import com.yunkouan.wms.modules.send.service.IExpressService;
import com.yunkouan.wms.modules.send.vo.ExpressVo;
import com.yunkouan.wms.modules.send.vo.SendDeliveryVo;
import com.yunkouan.wms.modules.send.vo.ems.ErpResult;

@Controller
@RequestMapping("${adminPath}/express")
public class ExpressController extends BaseController {
	@Autowired
	private IDeliveryService deliveryService;
	@Autowired
	private IExpressService service;
	@Autowired
	protected StrategyContext context;

	/** 
	* @Title: sendToExpress 
	* @Description: 单独发送物流公司
	* @auth tphe06
	* @time 2018 2018年6月5日 上午10:52:45
	* @param deliveryIds
	* @return
	* @throws Exception
	* ResultModel
	*/
	@RequestMapping(value="/sendToExpress",method=RequestMethod.POST)
	@ResponseBody
	public ResultModel sendToExpress(@RequestBody List<String> deliveryIds) throws Exception {
		ExpressVo vo = new ExpressVo();
		// 1.查询订单详情
		List<SendDeliveryVo> list = deliveryService.qryByIds(deliveryIds);
		// 2.推送物流公司
		List<SendDeliveryVo> success = service.sendDeliveryToLogistics(filterExpress(list));
		vo.setExpressSuccessSum(success.size());
		return new ResultModel().setObj(vo);
	}

	/** 
	* @Title: sendToErp 
	* @Description: 单独发送ERP
	* @auth tphe06
	* @time 2018 2018年6月5日 上午10:59:38
	* @param deliveryIds
	* @return
	* @throws Exception
	* ResultModel
	*/
	@RequestMapping(value="/sendToErp",method=RequestMethod.POST)
	@ResponseBody
	public ResultModel sendToErp(@RequestBody List<String> deliveryIds) throws Exception {
		ExpressVo vo = new ExpressVo();
		int result = 0;
		// 1.查询订单详情
		List<SendDeliveryVo> list = deliveryService.qryByIds(deliveryIds);
		// 3.2推送无需发送快递公司的记录到ERP
		for(SendDeliveryVo deliver : filterUnExpress(list)) {
			ErpResult erpResult = context.getStrategy4Erp().sendToERP(deliver,"",Constant.CONFIRM_RETURN);
			if(erpResult != null && erpResult.getCode() ==1){
				deliveryService.updateSendStatus(deliver.getSendDelivery().getDeliveryId(), Constant.SEND_JUST_ERP_SUCCESS);
				result ++;
			}
		}
		// 3.3推送快递公司失败的本次不推送给ERP
		// 3.4推送快递公司成功推送ERP失败的重新推送给ERP
		for(SendDeliveryVo deliver : filterExpErpFaile(list)) {
			ErpResult erpResult = context.getStrategy4Erp().sendToERP(deliver,"",Constant.CONFIRM_RETURN);
			if(erpResult != null && erpResult.getCode() ==1){
				deliveryService.updateSendStatus(deliver.getSendDelivery().getDeliveryId(), Constant.SEND_WULIU_ERP_SUCCESS);
				result ++;
			}
		}
		// 4返回推送ERP成功数量
		vo.setErpSuccessSum(result);
		return new ResultModel().setObj(vo);
	}

	/**
	 * 批量将绑定运单号的订单推送给快递公司（目前只有EMS/ST需要），然后推送erp系统
	 * @param deliveryId
	 * @return
	 */
	@RequestMapping(value="/sendToLogisticsCompany",method=RequestMethod.POST)
	@ResponseBody
	public ResultModel sendToLogisticsCompany(@RequestBody List<String> deliveryIds) throws Exception {
		int result = 0;
		// 1.查询订单详情
		List<SendDeliveryVo> list = deliveryService.qryByIds(deliveryIds);
		// 2.推送物流公司
		ExpressVo vo = new ExpressVo();
		List<SendDeliveryVo> success = service.sendDeliveryToLogistics(filterExpress(list));
		vo.setExpressSuccessSum(success.size());
		// 3.1推送发送快递公司成功的记录到ERP
		for(SendDeliveryVo deliver : success) {
			ErpResult erpResult = context.getStrategy4Erp().sendToERP(deliver,"",Constant.CONFIRM_RETURN);
			if(erpResult != null && erpResult.getCode() ==1){
				deliveryService.updateSendStatus(deliver.getSendDelivery().getDeliveryId(), 3);
				result ++;
			}
		}
		// 3.2推送无需发送快递公司的记录到ERP
		for(SendDeliveryVo deliver : filterUnExpress(list)) {
			ErpResult erpResult = context.getStrategy4Erp().sendToERP(deliver,"",Constant.CONFIRM_RETURN);
			if(erpResult != null && erpResult.getCode() ==1){
				deliveryService.updateSendStatus(deliver.getSendDelivery().getDeliveryId(), 4);
				result ++;
			}
		}
		// 3.3推送快递公司失败的本次不推送给ERP
		// 3.4推送快递公司成功推送ERP失败的重新推送给ERP
		for(SendDeliveryVo deliver : filterExpErpFaile(list)) {
			ErpResult erpResult = context.getStrategy4Erp().sendToERP(deliver,"",Constant.CONFIRM_RETURN);
			if(erpResult != null && erpResult.getCode() ==1){
				deliveryService.updateSendStatus(deliver.getSendDelivery().getDeliveryId(), 3);
				result ++;
			}
		}
		// 4返回推送ERP成功数量
		vo.setErpSuccessSum(result);
		return new ResultModel().setObj(vo);
	}
	/** 
	* @Title: filterExpress 
	* @Description: 过滤需要发送快递公司的数据
	* @auth tphe06
	* @time 2018 2018年5月18日 下午4:38:56
	* @param deliverys
	* @return
	* List<SendDeliveryVo>
	*/
	public static List<SendDeliveryVo> filterExpress(List<SendDeliveryVo> deliverys) {
		List<SendDeliveryVo> list = new ArrayList<SendDeliveryVo>();
		for(SendDeliveryVo vo : deliverys) {
			if(!IExpressService.EXPRESS_CODE_EMS.equals(vo.getSendDelivery().getExpressServiceCode())
				&& !IExpressService.EXPRESS_CODE_ST.equals(vo.getSendDelivery().getExpressServiceCode())
			) continue;
			Integer status = vo.getSendDelivery().getSendStatus();
			if(status != null && status > 1) continue;
			list.add(vo);
		}
		return list;
	}
	private static List<SendDeliveryVo> filterExpErpFaile(List<SendDeliveryVo> deliverys) {
		List<SendDeliveryVo> list = new ArrayList<SendDeliveryVo>();
		for(SendDeliveryVo vo : deliverys) {
			if(!IExpressService.EXPRESS_CODE_EMS.equals(vo.getSendDelivery().getExpressServiceCode())
				&& !IExpressService.EXPRESS_CODE_ST.equals(vo.getSendDelivery().getExpressServiceCode())
			) continue;
			Integer status = vo.getSendDelivery().getSendStatus();
			if(status != null && status == 2) {
				list.add(vo);
			}
		}
		return list;
	}
	private static List<SendDeliveryVo> filterUnExpress(List<SendDeliveryVo> deliverys) {
		List<SendDeliveryVo> list = new ArrayList<SendDeliveryVo>();
		for(SendDeliveryVo vo : deliverys) {
			if(IExpressService.EXPRESS_CODE_EMS.equals(vo.getSendDelivery().getExpressServiceCode())
				|| IExpressService.EXPRESS_CODE_ST.equals(vo.getSendDelivery().getExpressServiceCode())
			) continue;
			Integer status = vo.getSendDelivery().getSendStatus();
			if(status != null && status > 3) continue;
			list.add(vo);
		}
		return list;
	}

	/**
	 * 从EMS/ST获取运单号
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/getLogisticsNo",method=RequestMethod.POST)
	@ResponseBody
	public ResultModel getLogisticsNo(@RequestBody SendDelivery entity) throws Exception{
		String billNo = service.getLogisticsNo(entity.getExpressServiceCode());
		return new ResultModel().setObj(billNo);
	}
	
	
}