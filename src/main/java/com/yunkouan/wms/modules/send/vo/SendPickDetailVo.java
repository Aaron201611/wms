package com.yunkouan.wms.modules.send.vo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.yunkouan.base.BaseVO;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.common.util.IdUtil;
import com.yunkouan.wms.modules.send.entity.SendPickDetail;
import com.yunkouan.wms.modules.send.entity.SendPickLocation;
import com.yunkouan.wms.modules.send.util.ServiceConstant;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

public class SendPickDetailVo extends BaseVO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4638610500868707804L;
	
	private SendPickDetail sendPickDetail = new SendPickDetail();
	
	private String skuNo;//货品代码
	
	private String skuName;//商品名称
	
	private String specModel;//规格型号
	
	//中文 
	private String measureUnit;//计量单位
	
	private String packUnit;//包装单位
	
	private String skuBarCode;//货品条码
	
	private String bookNo;//海关备案企业帐册号

	private Example example;
	
	private List<String> detailIdList = new ArrayList<String>();
	
	/**
	 * 库位计划拣货
	 */
	private List<SendPickLocationVo> planPickLocations = new ArrayList<SendPickLocationVo>();
	
	/**
	 * 库位实际拣货
	 */
	private List<SendPickLocationVo> realPickLocations = new ArrayList<SendPickLocationVo>();
	
	public SendPickDetailVo(){
		this.example = new Example(SendPickDetail.class);
		example.setOrderByClause("update_time ASC,pick_detail_id2 ASC");
		example.createCriteria();
	}
	
	
	public String getSkuName() {
		return skuName;
	}

	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}

	public String getPackUnit() {
		return packUnit;
	}
	

	public void setPackUnit(String packUnit) {
		this.packUnit = packUnit;
	}

	public SendPickDetail getSendPickDetail() {
		return sendPickDetail;
	}
 
	public void setSendPickDetail(SendPickDetail sendPickDetail) {
		this.sendPickDetail = sendPickDetail;
	}
	

	public String getSkuNo() {
		return skuNo;
	}


	public void setSkuNo(String skuNo) {
		this.skuNo = skuNo;
	}


	public String getSpecModel() {
		return specModel;
	}


	public void setSpecModel(String specModel) {
		this.specModel = specModel;
	}


	public String getSkuBarCode() {
		return skuBarCode;
	}


	public void setSkuBarCode(String skuBarCode) {
		this.skuBarCode = skuBarCode;
	}


	public String getMeasureUnit() {
		return measureUnit;
	}


	public void setMeasureUnit(String measureUnit) {
		this.measureUnit = measureUnit;
	}

	public List<SendPickLocationVo> getPlanPickLocations() {
		return planPickLocations;
	}

	public void setPlanPickLocations(List<SendPickLocationVo> planPickLocations) {
		this.planPickLocations = planPickLocations;
	}

	public List<SendPickLocationVo> getRealPickLocations() {
		return realPickLocations;
	}

	public void setRealPickLocations(List<SendPickLocationVo> realPickLocations) {
		this.realPickLocations = realPickLocations;
	}
	
	public List<String> getDetailIdList() {
		return detailIdList;
	}

	public void setDetailIdList(List<String> detailIdList) {
		this.detailIdList = detailIdList;
	}

	public String getBookNo() {
		return bookNo;
	}


	public void setBookNo(String bookNo) {
		this.bookNo = bookNo;
	}


	public Example getExample() {
		return example;
	}

	public void setExample(Example example) {
		this.example = example;
	}
	
	public Example getConditionExample(){
		Criteria c = example.getOredCriteria().get(0);
		if(StringUtils.isNotEmpty(sendPickDetail.getSkuId())){
			c.andEqualTo("skuId",sendPickDetail.getSkuId());			
		}
		if(StringUtils.isNotEmpty(sendPickDetail.getBatchNo())){
			c.andEqualTo("batchNo",sendPickDetail.getBatchNo());
		}
		if(StringUtils.isNotEmpty(sendPickDetail.getDeliveryId())){
			c.andEqualTo("deliveryId",sendPickDetail.getDeliveryId());
		}
		if(StringUtils.isNotEmpty(sendPickDetail.getDeliveryDetailId())){
			c.andEqualTo("deliveryDetailId",sendPickDetail.getDeliveryDetailId());
		}
		if(detailIdList != null && !detailIdList.isEmpty()){
			c.andIn("pickDetailId", detailIdList);
		}
		if(StringUtils.isNotEmpty(sendPickDetail.getPickId())){
			c.andEqualTo("pickId",sendPickDetail.getPickId());
		}
		if(StringUtils.isNotEmpty(sendPickDetail.getOrgId())){
			c.andEqualTo("orgId",sendPickDetail.getOrgId());
		}
		if(StringUtils.isNotEmpty(sendPickDetail.getWarehouseId())){
			c.andEqualTo("warehouseId",sendPickDetail.getWarehouseId());
		}		
		return example;
	}
	
	public SendPickDetailVo defaultCondition(){
		Criteria c = example.getOredCriteria().get(0);
		if(StringUtils.isNotEmpty(sendPickDetail.getOrgId())){
			c.andEqualTo("orgId",sendPickDetail.getOrgId());
		}
		if(StringUtils.isNotEmpty(sendPickDetail.getWarehouseId())){
			c.andEqualTo("warehouseId",sendPickDetail.getWarehouseId());
		}
		return this;
	}

	/**
	 * 根据分配库位创建拣货库位计划
	 * @param locations
	 * @param operator
	 * @version 2017年3月3日 下午4:54:41<br/>
	 * @author Aaron He<br/>
	 */
	public void createPlanPickLocations(List<SendPickLocationVo> locationVoList,String operator){		
		if(locationVoList != null && !locationVoList.isEmpty()) {
			//创建计划拣货库位明细
			locationVoList.stream().forEach(t->{
				t.getSendPickLocation().setPickLocationId(IdUtil.getUUID());
				t.getSendPickLocation().setPickDetailId(sendPickDetail.getPickDetailId());	
				t.getSendPickLocation().setPickType(Constant.PICK_TYPE_PLAN);
				t.getSendPickLocation().setPackId(sendPickDetail.getPackId());
				t.getSendPickLocation().setMeasureUnit(sendPickDetail.getMeasureUnit());
				t.getSendPickLocation().setCreatePerson(operator);
				t.getSendPickLocation().setUpdatePerson(operator);
				t.getSendPickLocation().defaultValue();
			});
		}		
	}
	
	/**
	 * 更新计划库位列表
	 * @param operator
	 * @version 2017年3月7日 下午5:33:27<br/>
	 * @author Aaron He<br/>
	 */
	public void updatePlanPickLocations(String operator){
		this.planPickLocations.stream().forEach(p->{
			p.getSendPickLocation().setPickDetailId(IdUtil.getUUID());
			p.getSendPickLocation().setPickType(Constant.PICK_TYPE_PLAN);
			p.getSendPickLocation().setCreatePerson(operator);
			p.getSendPickLocation().setUpdatePerson(operator);
		});
	}
	
	/**
	 * 计划与实际库位数量是否不一致
	 * @return
	 * @version 2017年3月7日 下午2:36:13<br/>
	 * @author Aaron He<br/>
	 */
	public String getSkuDifferMessage(){
		if(this.realPickLocations.isEmpty() || this.planPickLocations.isEmpty()) return null;

		String msg = "";
		double planQty = countQty(this.planPickLocations);
		double realQty = countQty(this.realPickLocations);
		if(planQty != realQty){
			msg = String.format(ServiceConstant.PICK_NUM_NOT_CONFORM_MESSAGE,realQty,planQty);
			return msg;
		}
		return null;
	}
	
	/**
	 * 统计实际拣货总数量重量，体积
	 * 
	 * @version 2017年3月16日 下午2:52:05<br/>
	 * @author Aaron He<br/>
	 */
	public void statisAndSetRealPickQty(){
		double realPickQty = countQty(this.realPickLocations);
		double realPickWeight = countWeight(realPickLocations);
		double realPickVolume = countVolume(realPickLocations);
		sendPickDetail.setPickQty(realPickQty);
		sendPickDetail.setPickWeight(realPickWeight);
		sendPickDetail.setPickVolume(realPickVolume);
	}
	
	/**
	 * 统计计划拣货总数量，总重量，总体积
	 * 
	 * @version 2017年3月16日 下午2:52:33<br/>
	 * @author Aaron He<br/>
	 */
	public void statisAndSetPlanPickQty(){
		double planPickQty = countQty(this.planPickLocations);
		double planPickWeight = countWeight(planPickLocations);
		double planPickVolume = countVolume(planPickLocations);
		sendPickDetail.setPickQty(planPickQty);
		sendPickDetail.setPickWeight(planPickWeight);
		sendPickDetail.setPickVolume(planPickVolume);
	}
	/**
	 * 统计数量
	 * @param pickLocationVos
	 * @return
	 * @version 2017年3月7日 下午3:32:46<br/>
	 * @author Aaron He<br/>
	 */
	public double countQty(List<SendPickLocationVo> pickLocationVos){
		boolean flag = pickLocationVos.stream().anyMatch(t->t.getSendPickLocation().getPickLocationId() == null);
		if(flag) return 0;
		double qty = pickLocationVos.stream().mapToDouble(p->p.getSendPickLocation().getPickQty()).sum();
		return qty;
	}
	
	/**
	 * 统计重量
	 * @param pickLocationVos
	 * @return
	 * @version 2017年3月16日 下午2:47:24<br/>
	 * @author Aaron He<br/>
	 */
	public double countWeight(List<SendPickLocationVo> pickLocationVos){
		boolean flag = pickLocationVos.stream().anyMatch(t->t.getSendPickLocation().getPickLocationId() == null);
		if(flag) return 0;
		double weight = pickLocationVos.stream().mapToDouble(p->p.getSendPickLocation().getPickWeight()).sum();
		return weight;
	}
	/**
	 * 统计体积
	 * @param pickLocationVos
	 * @return
	 * @version 2017年3月16日 下午2:47:42<br/>
	 * @author Aaron He<br/>
	 */
	public double countVolume(List<SendPickLocationVo> pickLocationVos){
		boolean flag = pickLocationVos.stream().anyMatch(t->t.getSendPickLocation().getPickLocationId() == null);
		if(flag) return 0;
		double volume = pickLocationVos.stream().mapToDouble(p->p.getSendPickLocation().getPickVolume()).sum();
		return volume;
	}
	
	
	/**
	 * 转换成entity的list
	 * @param pickLocationVos
	 * @return
	 * @version 2017年3月7日 下午5:01:32<br/>
	 * @author Aaron He<br/>
	 */
	public static List<SendPickLocation> parseToEntitys(List<SendPickLocationVo> pickLocationVos){
		if(pickLocationVos == null ||pickLocationVos.isEmpty() ) return null;
		List<SendPickLocation> list = pickLocationVos.stream().map(p->p.getSendPickLocation()).collect(Collectors.toList());
		return list;
	}
	
	/**
	 * entityList转VoList
	 * @param entitys
	 * @return
	 * @version 2017年3月7日 下午5:08:34<br/>
	 * @author Aaron He<br/>
	 */
	public static List<SendPickLocationVo> parseToVo(List<SendPickLocation> entitys){
		List<SendPickLocationVo> list = new ArrayList<SendPickLocationVo>();
		for (SendPickLocation entity : entitys) {
			SendPickLocationVo vo = new SendPickLocationVo();
			vo.setSendPickLocation(entity);
			list.add(vo);
		}
		return list;
	}
	
	
	public SendPickDetailVo qryDetailIdsByLocations(List<SendPickLocationVo> locations){
		if(locations.isEmpty()) return this;
		List<String> list = locations.stream().map(t->t.getSendPickLocation().getPickDetailId()).collect(Collectors.toList());
		this.detailIdList = list;
		return this;
	}
	
}
