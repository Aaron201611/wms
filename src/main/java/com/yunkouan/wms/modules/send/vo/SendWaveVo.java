package com.yunkouan.wms.modules.send.vo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.yunkouan.base.BaseVO;
import com.yunkouan.saas.modules.sys.service.ISysParamService;
import com.yunkouan.util.SpringContextHolder;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.common.constant.CacheName;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.send.entity.SendWave;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

public class SendWaveVo extends BaseVO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5250282688153916890L;

	private SendWave sendWave = new SendWave();
	
	private List<SendDeliveryVo> sendDeliberyVoList = new ArrayList<SendDeliveryVo>();
	
	private String statusComment;
	
	private List<Integer> waveStatusList;
	
	private Example example;
	
	private String ownerName;
	
	private String waveNoLike;
	
	private String deliveryTypeDesc;
	
	public SendWaveVo(){
		example = new Example(SendWave.class);
    	example.setOrderByClause("update_time desc");
    	example.createCriteria(); 
	}
	

	public SendWave getSendWave() {
		return sendWave;
	}

	public void setSendWave(SendWave sendWave) {
		this.sendWave = sendWave;
		if(sendWave.getWaveStatus() != null){
			ISysParamService paramService = SpringContextHolder.getBean(ISysParamService.class);
			this.statusComment = paramService.getValue(CacheName.WAVE_STATUS, sendWave.getWaveStatus());
		}
	}

	public List<SendDeliveryVo> getSendDeliberyVoList() {
		return sendDeliberyVoList;
	}

	public void setSendDeliberyVoList(List<SendDeliveryVo> sendDeliberyVoList) {
		this.sendDeliberyVoList = sendDeliberyVoList;
	}	
	
	public String getStatusComment() {
		return statusComment;
	}

	public void setStatusComment(String statusComment) {
		this.statusComment = statusComment;
	}
	
	

	public List<Integer> getWaveStatusList() {
		return waveStatusList;
	}


	public void setWaveStatusList(List<Integer> waveStatusList) {
		this.waveStatusList = waveStatusList;
	}

	public String getWaveNoLike() {
		return waveNoLike;
	}


	public void setWaveNoLike(String waveNoLike) {
		this.waveNoLike = waveNoLike;
	}
	

	public String getOwnerName() {
		return ownerName;
	}


	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}


	public Example getExample() {
		return example;
	}


	public void setExample(Example example) {
		this.example = example;
	}


	public void countOrderTotal() throws Exception{
		countOrderQty();
		countOrderWeight();
		countOrderVolume();
	}

	public void countOrderQty(){
		if(sendDeliberyVoList == null || sendDeliberyVoList.isEmpty()) return;
		double totalQty =  sendDeliberyVoList.stream().mapToDouble(p->p.getSendDelivery().getOrderQty()).sum();
		this.sendWave.setOrderQty(totalQty);
	}
	
	public void countOrderWeight() throws Exception{
		if(sendDeliberyVoList == null || sendDeliberyVoList.isEmpty()) return;
		Double totalWeight = sendDeliberyVoList.stream().mapToDouble(p->p.getSendDelivery().getOrderWeight()).sum();
		this.sendWave.setOrderWeight(totalWeight);
	}
	
	public void countOrderVolume() throws Exception{
		if(sendDeliberyVoList == null || sendDeliberyVoList.isEmpty()) return;
		Double totalVolume = sendDeliberyVoList.stream().mapToDouble(p->p.getSendDelivery().getOrderVolume()).sum();
		this.sendWave.setOrderVolume(totalVolume);
	}
	
	public void countPickTotal() throws Exception{
		countPickQty();
		countPickWeight();
		countPickVolume();
	}

	public void countPickQty(){
		if(sendDeliberyVoList == null || sendDeliberyVoList.isEmpty()) return;
		double totalQty =  sendDeliberyVoList.stream().mapToDouble(p->p.getSendDelivery().getPickQty()).sum();
		this.sendWave.setPickQty(totalQty);
	}
	
	public void countPickWeight() throws Exception{
		if(sendDeliberyVoList == null || sendDeliberyVoList.isEmpty()) return;
		Double totalWeight = sendDeliberyVoList.stream().mapToDouble(p->p.getSendDelivery().getPickWeight()).sum();
		this.sendWave.setPickWeight(totalWeight);
	}
	
	public void countPickVolume() throws Exception{
		if(sendDeliberyVoList == null || sendDeliberyVoList.isEmpty()) return;
		Double totalVolume = sendDeliberyVoList.stream().mapToDouble(p->p.getSendDelivery().getPickVolume()).sum();
		this.sendWave.setPickVolume(totalVolume);
	}
	
	
	
	/**
	 * 统计货品种类数量
	 */
	public void calSkuQty(){
		if(this.sendDeliberyVoList == null || sendDeliberyVoList.isEmpty()) {
			this.getSendWave().setOrderSkuQty(0);
			return;
		}
		int skuQty = sendDeliberyVoList.stream().filter(t->t.getSendDelivery().getOrderSkuQty() != null)
				.mapToInt(t->t.getSendDelivery().getOrderSkuQty()).sum();
		this.getSendWave().setOrderSkuQty(skuQty);
	}
	
	/**
	 * 统计拣货数量
	 * @throws Exception 
	 */
	public void calTotalPick() throws Exception{
		calPickQty();
		calPickWeight();
		calPickVolume();
	}
	
	public void calPickQty(){
		if(this.sendDeliberyVoList == null || sendDeliberyVoList.isEmpty()) return;
		double sum = sendDeliberyVoList.stream().mapToDouble(t->t.getSendDelivery().getPickQty()).sum();
		this.sendWave.setPickQty(sum);
	}
	
	public void calPickWeight() throws Exception{
		if(this.sendDeliberyVoList == null || sendDeliberyVoList.isEmpty()) return;
		double sum = sendDeliberyVoList.stream().mapToDouble(t->t.getSendDelivery().getPickWeight()).sum();
		this.sendWave.setOrderWeight(sum);
	}
	
	public void calPickVolume() throws Exception{
		if(this.sendDeliberyVoList == null || sendDeliberyVoList.isEmpty()) return;
		double sum = sendDeliberyVoList.stream().mapToDouble(t->t.getSendDelivery().getPickVolume()).sum();
		this.sendWave.setOrderVolume(sum);
	}
	
	public List<String> toDeliveryIdList(){
		if(sendDeliberyVoList == null || sendDeliberyVoList.isEmpty()) return null;
		List<String> list = sendDeliberyVoList.stream().map(p->p.getSendDelivery().getDeliveryId()).collect(Collectors.toList());
		return list;
	}
	
	public SendWaveVo defaultCondition(){
		Criteria c = example.getOredCriteria().get(0);
		if(StringUtils.isNotEmpty(sendWave.getOrgId())){
			c.andCondition("org_id=", sendWave.getOrgId());
		}
		if(StringUtils.isNotEmpty(sendWave.getWarehouseId())){
			c.andCondition("warehouse_id=", sendWave.getWarehouseId());
		}
		return this;
	}
	
	public Example getConditionExample(){
		Criteria c = example.getOredCriteria().get(0);
		if(StringUtils.isNotEmpty(sendWave.getWaveNo())){
			c.andEqualTo("waveNo", sendWave.getWaveNo());
		}
		if(sendWave.getDeliveryAmount() != null){
			c.andEqualTo("deliveryAmount",sendWave.getDeliveryAmount());
		}
		if(sendWave.getWaveStatus() != null){
			c.andEqualTo("waveStatus",sendWave.getWaveStatus());
		}
		if(StringUtils.isNotEmpty(sendWave.getOrgId())){
			c.andCondition("org_id=", sendWave.getOrgId());
		}
		if(StringUtils.isNotEmpty(sendWave.getWarehouseId())){
			c.andCondition("warehouse_id=", sendWave.getWarehouseId());
		}
		if(waveStatusList != null && !waveStatusList.isEmpty()){
			c.andIn("waveStatus", waveStatusList);
		}
		if(!StringUtil.isTrimEmpty(waveNoLike)){
			c.andLike("waveNo", '%'+waveNoLike+'%');
		}
		return example;
	}
	
	public void setOrgIdAndWareHouseId(){
		//获取登录用户
		Principal loginUser = LoginUtil.getLoginUser();
		if(loginUser == null) return;
		//获取企业id与仓库id
		String orgId = loginUser.getOrgId();
		String wareHouseId = LoginUtil.getWareHouseId();
		this.sendWave.setOrgId(orgId);
		this.sendWave.setWarehouseId(wareHouseId);
	}


	public String getDeliveryTypeDesc() {
		ISysParamService paramService = SpringContextHolder.getBean(ISysParamService.class);
		if(this.sendWave.getDeliveryType()!=null){
			this.deliveryTypeDesc = paramService.getValue(CacheName.DELIVERY_TYPE, this.sendWave.getDeliveryType());
		}
		return deliveryTypeDesc;
	}
	


	public void setDeliveryTypeDesc(String deliveryTypeDesc) {
		this.deliveryTypeDesc = deliveryTypeDesc;
	}
	
}
