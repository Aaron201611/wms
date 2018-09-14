package com.yunkouan.wms.modules.send.util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.BeanUtils;

import com.yunkouan.exception.BizException;
import com.yunkouan.util.NumberUtil;
import com.yunkouan.wms.common.constant.BizStatus;
import com.yunkouan.wms.common.util.IdUtil;
import com.yunkouan.wms.modules.send.entity.SendDelivery;
import com.yunkouan.wms.modules.send.entity.SendDeliveryDetail;
import com.yunkouan.wms.modules.send.service.impl.SplitContext;
import com.yunkouan.wms.modules.send.vo.DeliveryDetailVo;
import com.yunkouan.wms.modules.send.vo.SendDeliveryVo;

public class DeliverySplitUtil {

	private SendDeliveryVo deliveryVo_1;
	
	private SendDeliveryVo deliveryVo_2;
	
	private SendDeliveryVo parentVo;
	
	private SplitContext splitContext = null; //拆分规则
	
	
	public void split(SendDeliveryVo deliveryVo,String operator) throws Exception{
		
		if(deliveryVo == null)
			throw new BizException(BizStatus.DATA_IS_NULL.getReasonPhrase());			
		//创建子拣货单vo
		deliveryVo_1 = newSubDelivery(parentVo,1,operator);
		deliveryVo_2 = newSubDelivery(parentVo,2,operator);
		//拆分拣货单明细
		splitDetails(deliveryVo,operator);	
	}
	
	public void splitDetails(SendDeliveryVo deliveryVo,String operator) throws Exception{
		double rate = 0.0;
		double zero = 0.0;
		//map<detailid,DeliveryDetailVo>
		Map<String,DeliveryDetailVo> map = deliveryVo.listToMap();
		for (DeliveryDetailVo detailVo : parentVo.getDeliveryDetailVoList()) {
			//原发货单明细
			SendDeliveryDetail sourceDetail = detailVo.getDeliveryDetail();
			//子发货单明细
			DeliveryDetailVo subVo = map.get(sourceDetail.getDeliveryDetailId());
			if(subVo == null){
				splitDetail(sourceDetail,sourceDetail.getOrderQty(),sourceDetail.getOrderWeight(),sourceDetail.getOrderVolume(),operator,deliveryVo_1);
				continue;
			}
			SendDeliveryDetail detail = subVo.getDeliveryDetail();
			//数据校验
			check(sourceDetail,detail);
			//按规则拆分
			//若重量或体积不为空，则按实际填写的重量体积拆分
			//若若重量或体积为空，则按数量比例拆分重量和体积
			double qty_1 = detail.getOrderQty();
			double weight_1 = 0.0;
			double volume_1 = 0.0;
			if((detail.getOrderWeight() != null && detail.getOrderWeight().compareTo(zero) != 0)
					|| (detail.getOrderVolume() != null && detail.getOrderVolume().compareTo(zero) !=0)){
			
				this.splitContext = SplitContext.createInstance(SplitContext.STRATEGE_QUAN);
				weight_1 = detail.getOrderWeight();
				volume_1 = detail.getOrderVolume();
			}else{
				this.splitContext = SplitContext.createInstance(SplitContext.STRATEGE_RATE);
				rate = (double)detail.getOrderQty()/sourceDetail.getOrderQty();		
				weight_1 = splitContext.quoteWeigh(sourceDetail.getOrderWeight(), detail.getOrderWeight(), rate);
				volume_1 = splitContext.quoteVolume(sourceDetail.getOrderVolume(), detail.getOrderVolume(), rate);
			}
			if(qty_1 > 0){
				splitDetail(sourceDetail,qty_1,weight_1,volume_1,operator,deliveryVo_1);
			}
			//创建子拣货单02,保存拆分数量大于0的明细
			double qty_2 = splitContext.quoteQty(sourceDetail.getOrderQty(), detail.getOrderQty());
			if(qty_2 > 0){	
				double weigh2 = NumberUtil.sub(sourceDetail.getOrderWeight(), weight_1);
				double volume2 = NumberUtil.sub(sourceDetail.getOrderVolume(),volume_1);
				splitDetail(sourceDetail,qty_2,weigh2,volume2,operator,deliveryVo_2);
			}
		}
	}
	
	/**
	 * 校验是否拆分数量是否超出订单数量
	 * @param org
	 * @param sub
	 * @version 2017年2月28日 下午2:19:47<br/>
	 * @author Aaron He<br/>
	 */
	public void check(SendDeliveryDetail org,SendDeliveryDetail sub){
		//检查数量，重量，体积是否为空
		qtyIsNull(org);
		qtyIsNull(sub);
		//校验是否拆分数量是否超出订单数量
		if((sub.getOrderQty().doubleValue() >  org.getOrderQty().doubleValue()) 
				|| (sub.getOrderWeight().compareTo( org.getOrderWeight()) > 0)
				|| (sub.getOrderVolume().compareTo( org.getOrderVolume()) > 0)){
			
			throw new BizException(BizStatus.SEND_PICK_SPLIT_ERROR.getReasonPhrase());
		}
	}
	
	/**
	 * 检查数量，重量，体积是否为空
	 * @param detail
	 * @version 2017年2月28日 下午2:15:51<br/>
	 * @author Aaron He<br/>
	 */
	public void qtyIsNull(SendDeliveryDetail detail){
		if(detail.getOrderQty() == null || detail.getOrderWeight() == null || detail.getOrderVolume() == null)
			throw new BizException(BizStatus.DATA_IS_NULL.getReasonPhrase());
	}
	
	/**
	 * 创建子发货单
	 * @param parentVo
	 * @param no
	 * @return
	 * @throws Exception
	 * @version 2017年2月16日 下午6:12:32<br/>
	 * @author Aaron He<br/>
	 */
	public SendDeliveryVo newSubDelivery(SendDeliveryVo parentVo,int no,String operator)throws Exception{
		SendDeliveryVo subDeliveryVo = new SendDeliveryVo();
		//根据系统规则生成子发货单号
		String deliveryNo = newSubDeliveryNo(parentVo.getSendDelivery().getDeliveryNo(),no);		
		//创建子发货单		
		SendDelivery subDelivery = new SendDelivery();
		subDelivery = (SendDelivery)BeanUtils.cloneBean(parentVo.getSendDelivery());
		subDelivery.setDeliveryId(IdUtil.getUUID());
		subDelivery.setDeliveryNo(deliveryNo);
		subDelivery.setParentId(parentVo.getSendDelivery().getDeliveryId());	
		subDelivery.setCreateTime(new Date());
		subDelivery.setCreatePerson(operator);
		subDelivery.setUpdatePerson(operator);
		subDelivery.setUpdateTime(new Date());
		subDeliveryVo.setSendDelivery(subDelivery);
		
		return subDeliveryVo;
	}
	
	/**
	 * 创建新发货子单号
	 * @param oriDeliveryId
	 * @return
	 * @version 2017年2月15日 下午2:04:47<br/>
	 * @author Aaron He<br/>
	 */
	public String newSubDeliveryNo(String oriDeliveryNo,int no){
		String subDeliveryId = "";
		if(no == 1) subDeliveryId = oriDeliveryNo + "-01";
		else if (no == 2) subDeliveryId = oriDeliveryNo + "-02";
		return subDeliveryId;
	}
	
	/**
	 * 创建子发货单明细
	 * @param orlRecord
	 * @param qty
	 * @param weigh
	 * @param volume
	 * @return
	 * @throws Exception
	 * @version 2017年2月16日 下午6:12:52<br/>
	 * @author Aaron He<br/>
	 */
	public void splitDetail(SendDeliveryDetail orlRecord,double qty,double weigh,double volume,String operator,SendDeliveryVo subVo)throws Exception{
		SendDeliveryDetail newDetail = (SendDeliveryDetail)BeanUtils.cloneBean(orlRecord);
		newDetail.setDeliveryId(subVo.getSendDelivery().getDeliveryId());
		newDetail.setOrderQty(qty);
		newDetail.setOrderWeight(weigh);
		newDetail.setOrderVolume(volume);
		newDetail.setCreateTime(new Date());
		newDetail.setCreatePerson(operator);
		newDetail.setUpdateTime(new Date());
		newDetail.setUpdatePerson(operator);
		newDetail.defaultValue();
		
		DeliveryDetailVo vo = new DeliveryDetailVo();
		vo.setDeliveryDetail(newDetail);
		subVo.getDeliveryDetailVoList().add(vo);		
	}

	public SendDeliveryVo getDeliveryVo_1() {
		return deliveryVo_1;
	}

	public void setDeliveryVo_1(SendDeliveryVo deliveryVo_1) {
		this.deliveryVo_1 = deliveryVo_1;
	}

	public SendDeliveryVo getDeliveryVo_2() {
		return deliveryVo_2;
	}

	public void setDeliveryVo_2(SendDeliveryVo deliveryVo_2) {
		this.deliveryVo_2 = deliveryVo_2;
	}
	
	public SplitContext getSplitContext() {
		return splitContext;
	}

	public void setSplitContext(SplitContext splitContext) {
		this.splitContext = splitContext;
	}
	
	
	public SendDeliveryVo getParentVo() {
		return parentVo;
	}

	public void setParentVo(SendDeliveryVo parentVo) {
		this.parentVo = parentVo;
	}

	public static void main(String[] args) {
		Integer a = 12;
		Integer b = 13;
		Double r = (double)a/b;
		DecimalFormat d = new DecimalFormat("#0.00");
		System.out.println(r);
		System.out.println(d.format(r));
	}
	
	
}
