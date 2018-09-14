package com.yunkouan.wms.modules.send.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.StringUtil;
import com.yunkouan.exception.BizException;
import com.yunkouan.util.DateUtil;
import com.yunkouan.util.HttpUtil;
import com.yunkouan.util.JsonUtil;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.common.util.FqDataUtils;
import com.yunkouan.wms.modules.meta.entity.MetaMerchant;
import com.yunkouan.wms.modules.meta.entity.MetaSku;
import com.yunkouan.wms.modules.meta.service.IMerchantService;
import com.yunkouan.wms.modules.meta.service.ISkuService;
import com.yunkouan.wms.modules.send.service.AbstractExpressService;
import com.yunkouan.wms.modules.send.util.InterfaceLogUtil;
import com.yunkouan.wms.modules.send.vo.DeliveryDetailVo;
import com.yunkouan.wms.modules.send.vo.SendDeliveryVo;
import com.yunkouan.wms.modules.send.vo.Vip007Data;
import com.yunkouan.wms.modules.send.vo.Vip007Result;
import com.yunkouan.wms.modules.send.vo.Vip009Result;
import com.yunkouan.wms.modules.sys.vo.InterfaceLogVo;

/** 
* @Description: 申通快递接口
* @author tphe06
* @date 2018年4月18日 下午5:09:58  
*/
@Service
@Scope("prototype")//此注解不得删除!!
@Transactional(readOnly=false, rollbackFor=Exception.class)
public class STServiceImpl extends AbstractExpressService {
	private static Log log = LogFactory.getLog(STServiceImpl.class);

	@Value("${st_url}")
	private String url;
	@Value("${st_sign}")
	private String sign;
	@Value("${st_pwd}")
	private String pwd;
	@Value("${st_cusname}")
	private String cusname;
	@Value("${st_cussite}")
	private String cussite;
	
	@Autowired 
	private ISkuService skuService;//货品接口
	
	@Autowired 
	private IMerchantService merchantService;//客商接口

	/** 
	* @Title: get 
	* @Description: 从物流公司获取N个运单号
	* @auth tphe06
	* @time 2018 2018年4月18日 下午6:22:14
	* @param sum
	* @return
	* @throws IOException
	*/
	@Override
	protected List<String> get(int sum) throws IOException {
		Map<String, String> map = new HashMap<String, String>();
		map.put("code", "vip0009");//请求的方法名
		map.put("data_digest", sign);//方法签名
		map.put("cuspwd", pwd);//客户密码
		map.put("cusname", cusname);//客户名称
		map.put("cusite", cussite);//网点名称
		map.put("len", String.valueOf(sum));//个数
		String ret = HttpUtil.post(url, map);
		if(log.isInfoEnabled()) log.info("vip0009:"+ret);
		Vip009Result result = JsonUtil.fromJson(ret, Vip009Result.class);
		if(!result.getSuccess() || !"103".equals(result.getMessage())) throw new BizException("st_"+result.getMessage());
		return Arrays.asList(result.getData().split(","));
	}

	/** 
	* @Title: push 
	* @Description: 往快递公司推送订单
	* @auth tphe06
	* @time 2018 2018年4月18日 下午6:22:18
	* @param vo
	* @return
	* @throws IOException
	*/
	@Override
	public SendDeliveryVo send(SendDeliveryVo vo) throws Exception {
		//封装参数
		Vip007Data data = new Vip007Data();
		data.setBillno(vo.getSendDelivery().getExpressBillNo());
		data.setSenddate(DateUtil.formatDate(new Date()));
		data.setSendsite(cussite);
		data.setSendcus(cusname);
		data.setSendperson(vo.getSendDelivery().getConsignor());
		data.setSendtel(vo.getSendDelivery().getConsignorPhone());
//		data.setReceivecus("深圳天翼德");
		data.setReceiveperson(vo.getSendDelivery().getContactPerson());
		data.setReceivetel(vo.getSendDelivery().getContactPhone());
		data.setGoodsname(getGoodsName(vo.getDeliveryDetailVoList()));
		data.setInputdate(data.getSenddate());
		data.setInputperson(cusname);
		data.setInputsite(cussite);
//		data.setLasteditdate("");
//		data.setLasteditperson("");
//		data.setLasteditsite("上海陈行公司");
//		data.setRemark("测试备注");
		data.setReceiveprovince(vo.getSendDelivery().getProvince());
		data.setReceivecity(vo.getSendDelivery().getCity());
		data.setReceivearea(vo.getSendDelivery().getCounty());
		data.setReceiveaddress(vo.getSendDelivery().getContactAddress());
		if(StringUtil.isNotEmpty(vo.getSendDelivery().getConsignor())) {
			data.setSendprovince(vo.getSendDelivery().getSendProvince());
			data.setSendcity(vo.getSendDelivery().getSendCity());
			data.setSendarea(vo.getSendDelivery().getSendCounty());
			data.setSendaddress(vo.getSendDelivery().getConsignorAddress());
		} else {
			throw new BizException("st_send_user_not_empty");
		}
//		data.setWeight("12");
//		data.setProductcode("");
		data.setOrderno(vo.getSendDelivery().getSrcNo());
		//向申通发送数据
		List<Vip007Data> list = new ArrayList<Vip007Data>();
		list.add(data);
		String json = JsonUtil.toJson(list);
		if(log.isInfoEnabled()) log.info(json);
		Map<String, String> map = new HashMap<String, String>();
		map.put("code", "vip0007");//请求的方法名
		map.put("data_digest", sign);//方法签名
		map.put("cuspwd", pwd);//客户密码
		map.put("data", json);
		try {
			if(log.isInfoEnabled()) log.info(url);
			//记录接口日志
			InterfaceLogVo logVo = InterfaceLogUtil.getInstance().addLog(Constant.SYS_WMS, 
																			map.toString(), 
																			new Date(), 
																			Constant.SYS_ST, 
																			null, 
																			null, 
																			url, 
																			null, 
																			null);
			String ret = HttpUtil.post(url, map);
			
			//更新接口日志
			logVo.getEntity().setReceiveTime(new Date());
			logVo.getEntity().setReceiveMessage(ret);
			logVo.getEntity().setResult(ret);
			InterfaceLogUtil.getInstance().updateResult(logVo);
			
			if(ret == null) return null;
			if(log.isInfoEnabled()) log.info("vip0007:"+ret);
			Vip007Result result = JsonUtil.fromJson(ret, Vip007Result.class);
			if(result.getSuccess() != null && result.getSuccess()) {
				if(result.getData()!=null && result.getData().size()>0) {
					vo.getSendDelivery().setExpressBigchar(result.getData().get(0).getBigchar());
					vo.getSendDelivery().setExpressGatheringPlace(result.getData().get(0).getPackageName());
					if(log.isInfoEnabled()) log.info("Bigchar====="+vo.getSendDelivery().getExpressBigchar());
					if(log.isInfoEnabled()) log.info("PackageName====="+vo.getSendDelivery().getExpressGatheringPlace());
				}
				return vo;
			}
		} catch(Exception e) {
			throw new BizException("st_netexception");
		}
		return null;
	}

	/** 
	* @Title: getGoodsName 
	* @Description: 拼接货品名称，用逗号隔开，最大长度500
	* @auth tphe06
	* @time 2018 2018年4月19日 上午11:11:07
	* @param list
	* @return
	* String
	*/
	private  String getGoodsName(List<DeliveryDetailVo> list) throws Exception{
		StringBuffer goods = new StringBuffer();
		Map<String, String> skus = new HashMap<String, String>();
		FqDataUtils fdu = new FqDataUtils();
		if(list != null) for(DeliveryDetailVo vo : list) {
			MetaSku sku = fdu.getSkuById(skuService, vo.getDeliveryDetail().getSkuId());
			vo.setSkuName(sku.getSkuName());
			skus.put(vo.getDeliveryDetail().getSkuId(), vo.getSkuName());
		}
		for(Iterator<String> it = skus.keySet().iterator(); it.hasNext();) {
			String name = skus.get(it.next());
			if(goods.length() + name.length() > 500) break;
			goods.append(name).append(",");
		}
		return goods.toString();
	}

	/** 
	* @Title: isBathPush 
	* @Description: 是否支持批量推送订单
	* @auth tphe06
	* @time 2018 2018年4月20日 上午11:29:11
	* @return
	*/
	@Override
	protected boolean isBathPush() {
		return false;
	}

	/** 
	* @Title: send 
	* @Description: 批量推送订单接口，返回推送成功列表，如果不支持请在isBathPush方法返回false
	* @auth tphe06
	* @time 2018 2018年4月20日 上午11:29:32
	* @param vo
	* @return
	* @throws IOException
	*/
	@Override
	protected List<SendDeliveryVo> send(List<SendDeliveryVo> vo) throws IOException {
		return null;
	}
}