package com.yunkouan.wms.modules.intf.controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunkouan.base.BaseController;
import com.yunkouan.entity.ResultModel;
import com.yunkouan.exception.BizException;
import com.yunkouan.exception.ServiceException;
import com.yunkouan.saas.modules.sys.service.ISysParamService;
import com.yunkouan.util.JsonUtil;
import com.yunkouan.util.MD5Util;
import com.yunkouan.util.StringUtil;
import com.yunkouan.valid.ValidSearch;
import com.yunkouan.wms.common.constant.BizStatus;
import com.yunkouan.wms.common.constant.CacheName;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.common.constant.ExtInterf;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.intf.vo.Message;
import com.yunkouan.wms.modules.intf.vo.StockQueryVo;
import com.yunkouan.wms.modules.inv.service.IShiftService;
import com.yunkouan.wms.modules.inv.service.IStockService;
import com.yunkouan.wms.modules.inv.vo.InvSkuStockVO;
import com.yunkouan.wms.modules.inv.vo.InvStockVO;
import com.yunkouan.wms.modules.message.service.IMsmqMessageService;
import com.yunkouan.wms.modules.meta.service.ISkuService;
import com.yunkouan.wms.modules.meta.vo.SkuVo;
import com.yunkouan.wms.modules.rec.service.IASNService;
import com.yunkouan.wms.modules.rec.vo.RecAsnVO;
import com.yunkouan.wms.modules.send.service.IDeliveryService;
import com.yunkouan.wms.modules.send.util.InterfaceLogUtil;
import com.yunkouan.wms.modules.send.vo.SendDelivery2ExternalVo;
import com.yunkouan.wms.modules.sys.vo.InterfaceLogVo;

@Controller
@CrossOrigin
@RequestMapping("${adminPath}/external")
public class ExternalController extends BaseController {
	private static Log log = LogFactory.getLog(ExternalController.class);

	/**skuService:货品服务接口**/
	@Autowired
	private ISkuService skuService;
	
	/** 发货服务 **/
	@Autowired
	private IDeliveryService deliveryService;
	/** 库存服务 **/
	@Autowired
	private IStockService stockService;
	/** 移位服务 **/
	@Autowired
	private IShiftService shiftService;
	
	@Autowired
	private IASNService asnService;
	
	@Autowired
	private IMsmqMessageService msmqMessageService;
	
	@Autowired
	private ISysParamService paramService;

//	@OpLog(model = OpLog.MODEL_EXT_INTERFACE, type = OpLog.OP_TYPE_QUERY, pos=0)
	@RequestMapping(value = "/interface", method = RequestMethod.POST)
//	@ResponseBody
	public void interf(@Validated(value = { ValidSearch.class }) @RequestBody Message vo, BindingResult br, HttpServletRequest req, HttpServletResponse rsp) throws IOException {
		if(log.isDebugEnabled()) log.debug("SessionId:["+req.getSession().getId()+"]，URL:"+req.getRequestURL());
		ResultModel rm = new ResultModel();
		InterfaceLogVo logVo = null;
		
			// 参数校验
			if(br.hasErrors()) {
				if(log.isDebugEnabled()) log.debug("请求参数有误");
				rm =  super.handleValid(br);
				LoginUtil.printAjax(rsp, JsonUtil.toJson(rm));
				return;
			}
			// 接口类型校验
			if(!contains(vo.getNotify_type())) throw new BizException("valid_extintf_notify_type_no_exists");
			if(log.isInfoEnabled()) log.info("请求完整数据："+JsonUtil.toJson(vo));
			if(log.isInfoEnabled()) log.info("请求body数据："+StringUtils.trimToEmpty(vo.getData()));
			// 获取当前用户信息
			Principal loginUser = LoginUtil.getLoginUser();
			// 验证用户是否登录
	    	if (loginUser == null || StringUtil.isTrimEmpty(loginUser.getAccountId())) {
	    		throw new BizException("valid_common_user_no_login");
	    	}

			// 签名校验
			if(StringUtils.isNoneBlank(vo.getData())) {
				String key = paramService.getKey(CacheName.PARAM_EXT_KEY);
				String md5 = md5(vo.getData(), key);
//				String md5 = md5(URLEncoder.encode(vo.getData(), "UTF-8"), key);
//				if(log.isInfoEnabled()) log.info("data-encode："+URLEncoder.encode(vo.getData(), "UTF-8"));
//				if(log.isInfoEnabled()) log.info("data："+vo.getData());
				if(log.isInfoEnabled()) log.info("wms sign："+md5);
				if(log.isInfoEnabled()) log.info("erp sign："+vo.getSign());
				if(!md5.equalsIgnoreCase(vo.getSign())) throw new BizException("valid_extintf_sign_no_right");
			}
			if(log.isInfoEnabled()) log.info("签名校验成功");
			if(log.isInfoEnabled()) log.info("data-decode："+URLDecoder.decode(vo.getData(),"UTF-8"));
			//data数据按UTF-8解码
			String data = URLDecoder.decode(vo.getData(),"UTF-8");
			vo.setData(data);
			//记录接口日志
			logVo = InterfaceLogUtil.getInstance().addLog(Constant.SYS_ERP, 
					null, new Date(), Constant.SYS_WMS, data, null, null, null, vo.getNotify_type());
			try {
			// 业务处理
			if(ExtInterf.INTERFACE_QUERY_SKU.getValue().equals(vo.getNotify_type())) {
				SkuVo vo1 = JsonUtil.fromJson(vo.getData(), SkuVo.class);
				if(vo1.getCurrentPage() == null 
						|| vo1.getCurrentPage() < 1
						|| vo1.getPageSize() == null
						|| vo1.getPageSize() > 10000) throw new ServiceException("err_common_current_page");
					rm =  skuService.list4Page(vo1);
//				try {
//					if(vo1.getCurrentPage() == null 
//						|| vo1.getCurrentPage() < 1
//						|| vo1.getPageSize() == null
//						|| vo1.getPageSize() > 10000) throw new ServiceException("err_common_current_page");
////					vo1.setCurrentPage(vo1.getCurrentPage()-1);
//					rm =  skuService.list4Page(vo1);
//				} catch (DaoException | ServiceException e) {
//					e.printStackTrace();
//					if(log.isErrorEnabled()) log.error(e.getMessage(),e);
//					throw new BizException(e.getMessage(),e);
//				} catch(Exception e) {
//					e.printStackTrace();
//					if(log.isErrorEnabled()) log.error(e.getMessage(),e);
//					throw new BizException(ErrorCode.UNKNOW_ERROR);
//				}
			}
			//原始订单推送接口
			else if(ExtInterf.INTERFACE_SEND_ORDER.getValue().equals(vo.getNotify_type())){
				SendDelivery2ExternalVo send2InterfaceVo = JsonUtil.fromJson(vo.getData(), SendDelivery2ExternalVo.class);
				if(send2InterfaceVo == null)
					throw new BizException(BizStatus.DATA_IS_NULL.getReasonPhrase());
				deliveryService.importDeliveryToInterface(send2InterfaceVo);
//				try {
//					deliveryService.importDeliveryToInterface(send2InterfaceVo);
//	//				ResultModel rm = new ResultModel();
//	//				return rm;
//				} catch (BizException e) {
//					throw e;
//				} catch (Exception e) {
//					e.printStackTrace();
//					if(log.isErrorEnabled()) log.error(e.getMessage(),e);
//					throw new BizException(ErrorCode.UNKNOW_ERROR);
//				}
			}
			//查询订单状态接口
			else if(ExtInterf.INTERFACE_QUERY_ORDER_STATUS.getValue().equals(vo.getNotify_type())){
				SendDelivery2ExternalVo sendDeliveryVo = JsonUtil.fromJson(vo.getData(), SendDelivery2ExternalVo.class);
				Map<String,String> statusMap = deliveryService.qryStatus(sendDeliveryVo.getSendDelivery().getSrcNo());
				rm.setObj(statusMap);
//				try {
//					Map<String,String> statusMap = deliveryService.qryStatus(sendDeliveryVo.getSendDelivery().getSrcNo());
//					rm.setObj(statusMap);
//				} catch (BizException e) {
//					throw e;
//				} catch (Exception e) {
//					e.printStackTrace();
//					if(log.isErrorEnabled()) log.error(e.getMessage(),e);
//					throw new BizException(ErrorCode.UNKNOW_ERROR);
//				}
			}
			//订单拦截/取消拦截
			else if(ExtInterf.INTERFACE_CANCEL_ORDER.getValue().equals(vo.getNotify_type())){
				Map<String,String> map = JsonUtil.fromJson(vo.getData(), Map.class);
				Map<String,String> result = deliveryService.cancelToInterface(map.get("srcNo"),map.get("oper"));
				rm.setObj(result);
//				try {
//					deliveryService.cancelToInterface(sendDeliveryVo.getSendDelivery().getSrcNo());
//				} catch (BizException e) {
//					throw e;
//				} catch (Exception e) {
//					e.printStackTrace();
//					if(log.isErrorEnabled()) log.error(e.getMessage(),e);
//					throw new BizException(ErrorCode.UNKNOW_ERROR);
//				}
			}
			//查询库存接口
			else if(ExtInterf.INTERFACE_QUERY_STOCK.getValue().equals(vo.getNotify_type())){
				StockQueryVo svo = JsonUtil.fromJson(vo.getData(), StockQueryVo.class);
				List<String> skuList = svo.getSkuNoList();
				//获取企业id与仓库id
				List<InvSkuStockVO> list = stockService.stockSkuCount(skuList);
				rm.setList(list);
//				try {
//					//获取企业id与仓库id
//					List<InvSkuStockVO> list = stockService.stockSkuCount(skuList);
//					rm.setList(list);
//				} catch (BizException e) {
//					throw e;
//				} catch (Exception e) {
//					e.printStackTrace();
//					if(log.isErrorEnabled()) log.error(e.getMessage(),e);
//					throw new BizException(ErrorCode.UNKNOW_ERROR);
//				}
			}
			//入库单
			else if(ExtInterf.INTERFACE_IMPORT_ASN.getValue().equals(vo.getNotify_type())){
				RecAsnVO raVo = JsonUtil.fromJson(vo.getData(), RecAsnVO.class);
				RecAsnVO recAsnVO=asnService.importAsn(raVo);
				//TODO
				if(recAsnVO==null){
					rm.setError();
					rm.addMessage("入库清单中有非生效状态的货品，请同步货品信息查看");
					String json = JsonUtil.toJson(rm);
					if(log.isDebugEnabled()) log.debug(json);
					LoginUtil.printAjax(rsp, json);
					return;
				}
//				try {
//					RecAsnVO raVo = JsonUtil.fromJson(vo.getData(), RecAsnVO.class);
//					RecAsnVO recAsnVO=asnService.importAsn(raVo);
//					//TODO
//					if(recAsnVO==null){
//						rm.setError();
//						rm.addMessage("入库清单中有非生效状态的货品，请同步货品信息查看");
//						String json = JsonUtil.toJson(rm);
//						if(log.isDebugEnabled()) log.debug(json);
//						LoginUtil.printAjax(rsp, json);
//						return;
//					}
//				} catch (BizException e) {
//					throw e;
//				}catch(Exception e) {
//					e.printStackTrace();
//					if(log.isErrorEnabled()) log.error(e.getMessage(),e);
//					throw new BizException(ErrorCode.UNKNOW_ERROR);
//				}
			}
			//导入货品信息
			else if(ExtInterf.INTERFACE_IMPORT_SKU.getValue().equals(vo.getNotify_type())){
				List<SkuVo> skuVoList= JsonUtil.listFromJson(vo.getData(), SkuVo.class);
				List<SkuVo> list = skuService.importSkuForExtInface(skuVoList);
				rm.setList(list);
//				try {
//					List<SkuVo> list = skuService.importSkuForExtInface(skuVoList);
//					rm.setList(list);
//				} catch (BizException e) {
//					throw e;
//				} catch (Exception e) {
//					e.printStackTrace();
//					if(log.isErrorEnabled()) log.error(e.getMessage(),e);
//					throw new BizException(ErrorCode.UNKNOW_ERROR);
//				}
			}
			//查询货品库存接口
			else if(ExtInterf.INTERFACE_QUERY_STOCK_NEW.getValue().equals(vo.getNotify_type())){
				InvStockVO invStockVO=JsonUtil.fromJson(vo.getData(), InvStockVO.class);
				Map<String,Object>map=stockService.stockList2ERP(invStockVO, loginUser);
				rm.setObj(map);
//				String json = JsonUtil.toJson(map);
//				if(log.isDebugEnabled()) log.debug(json);
//				LoginUtil.printAjax(rsp, json);
//				try {
//					InvStockVO invStockVO=JsonUtil.fromJson(vo.getData(), InvStockVO.class);
//					Map<String,Object>map=stockService.stockList2ERP(invStockVO, loginUser);
//					String json = JsonUtil.toJson(map);
//					if(log.isDebugEnabled()) log.debug(json);
//					LoginUtil.printAjax(rsp, json);
//				} catch (Exception e) {
//					e.printStackTrace();
//					if(log.isErrorEnabled()) log.error(e.getMessage(),e);
//					throw new BizException(ErrorCode.UNKNOW_ERROR);
//				}
				
			}
			//创建补货任务
			else if(ExtInterf.INTERFACE_ADD_SHIFT.getValue().equals(vo.getNotify_type())){
				InvStockVO paramVo = JsonUtil.fromJson(vo.getData(), InvStockVO.class);
				stockService.repSkuInterface(paramVo.getSkuNo(), paramVo.getFindNum());
//				try {
//					InvStockVO paramVo = JsonUtil.fromJson(vo.getData(), InvStockVO.class);
//					stockService.repSkuInterface(paramVo.getSkuNo(), paramVo.getFindNum());
//				} catch (BizException e) {
//					throw e;
//				}catch(Exception e) {
//					e.printStackTrace();
//					if(log.isErrorEnabled()) log.error(e.getMessage(),e);
//					throw new BizException(ErrorCode.UNKNOW_ERROR);
//				}
			}
			//取消收货单
			else if(ExtInterf.INTERFACE_CANCEL_ASN.getValue().equals(vo.getNotify_type())){
				RecAsnVO asnVo = JsonUtil.fromJson(vo.getData(), RecAsnVO.class);
				asnService.cancelByNo(asnVo);
//				try {
//					RecAsnVO asnVo = JsonUtil.fromJson(vo.getData(), RecAsnVO.class);
//					asnService.cancelByNo(asnVo);
//				} catch (BizException e) {
//					throw e;
//				}catch (Exception e) {
//					e.printStackTrace();
//					if(log.isErrorEnabled()) log.error(e.getMessage(),e);
//					throw new BizException(ErrorCode.UNKNOW_ERROR);
//				}
				
			}
		}
		catch(BizException e) {
			rm.setError();
			String msg = handleBizException(req, e);
			rm.addMessage(msg);
			//更新接口日志结果
			logVo.getEntity().setResult(msg);
			InterfaceLogUtil.getInstance().updateResult(logVo);
			log.error(e.getMessage(),e);
		} catch(Throwable e) {
			if(log.isErrorEnabled()) log.error(e.getMessage(),e);
			rm.setError();
			rm.addMessage("未知错误");
			//更新接口日志结果
			logVo.getEntity().setResult(e.getMessage());
			InterfaceLogUtil.getInstance().updateResult(logVo);
			log.error(e.getMessage(),e);
		}
		try {
			String json = JsonUtil.toJson(rm);
			if(log.isDebugEnabled()) log.debug(json);
			LoginUtil.printAjax(rsp, json);
			//更新接口日志结果
			logVo.getEntity().setSendMessage(json);
			InterfaceLogUtil.getInstance().updateResult(logVo);
		} catch (Exception e) {
			if(log.isErrorEnabled()) log.error(e.getMessage(),e);
		}
		
	}

	/**
	 * 判断接口类型是否存在
	 * @param key
	 * @return
	 */
	private static boolean contains(String key) {
		ExtInterf[] v = ExtInterf.values();
		for(int i=0; i<v.length; ++i) {
			if(v[i].getValue().equals(key)) return true;
		}
		return false;
	}
	
    /**
     * 接收回执
     * @param putawayId
     * @return
     * @throws Exception
     * @required 
     * @optional  
     * @Description 
     * @version 2017年3月7日 下午5:28:38<br/>
     * @author hgx<br/>
     */
    @RequestMapping(value = "/receiveResult", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel receiveResult(@RequestBody String xmlStr) throws Exception {
    	if(log.isDebugEnabled()) log.debug("手工接收Queue");
    	ResultModel rm = new ResultModel();
    	String xml = new String(xmlStr.getBytes("gbk"),"UTF-8");
    	msmqMessageService.testReceiveMessage(xml); 
    	if(log.isDebugEnabled()) log.debug("手工接收Queue--end");
    	return rm;
    }

	/**
	 * 生成md5签名数据
	 * @param data 数据
	 * @param key 密钥
	 * @return
	 */
	private static String md5(String data, String key) {
	   String sign = new StringBuilder(data.trim()).append(key).toString();
	    return MD5Util.md5(sign);
	}
	
}