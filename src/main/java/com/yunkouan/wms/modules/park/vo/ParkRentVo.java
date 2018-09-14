package com.yunkouan.wms.modules.park.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.yunkouan.base.BaseVO;
import com.yunkouan.saas.modules.sys.service.ISysParamService;
import com.yunkouan.util.SpringContextHolder;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.common.constant.CacheName;
import com.yunkouan.wms.modules.park.entity.ParkRent;
import com.yunkouan.wms.modules.send.vo.SendDeliveryVo;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

public class ParkRentVo extends BaseVO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5404053768909759303L;
	
	private ParkRent parkRent;
	
	private String orgName;
	
	private String warehouseName;
	
	private String merchantName;
	
	private Example example;

	private List<String> orgIdList;
	
	private List<String> merchantIdList;
	
	private List<String> warehouseIdList;
	
	private String rentStatusComment;
	
	private String rentStyleComment;
	
	private String feeStyleComment;
	
	private String settleCycleComment;
	
	private String warnFrequencyComment;
	
	private String warnStyleComment;
	
	private Date orderTime;
	
	private List<String> notInRentIds = new ArrayList<String>();

	public ParkRentVo() {
		super();
		parkRent = new ParkRent();
		example = new Example(ParkRent.class);
		example.setOrderByClause("update_time desc");
		example.createCriteria();
	}

	public Example getExample() {
		return example;
	}

	public void setExample(Example example) {
		this.example = example;
	}

	public ParkRent getParkRent() {
		return parkRent;
	}

	public void setParkRent(ParkRent parkRent) {
		this.parkRent = parkRent;
		ISysParamService paramService = SpringContextHolder.getBean(ISysParamService.class);
		if(parkRent.getRentStatus() != null){
			this.rentStatusComment = paramService.getValue(CacheName.PARK_RENT_STATUS, parkRent.getRentStatus());
		}
		
		if(parkRent.getRentStyle() != null){
			this.rentStyleComment = paramService.getValue(CacheName.PARK_RENT_STYLE, parkRent.getRentStyle());
		}
		
		if(parkRent.getFeeStyle() != null){
			this.feeStyleComment = paramService.getValue(CacheName.PARK_FEE_STYLE, parkRent.getFeeStyle());
		}
		
		if(parkRent.getSettleCycle() != null){
			this.settleCycleComment = paramService.getValue(CacheName.PARK_SETTLE_CYCLE, parkRent.getSettleCycle());
		}
		if(parkRent.getWarnFrequency() != null){
			this.warnFrequencyComment = paramService.getValue(CacheName.WARN_FREQUENCY, parkRent.getWarnFrequency());
		}
		if(parkRent.getWarnStyle() != null){
			this.warnStyleComment = paramService.getValue(CacheName.WARN_STYLE, parkRent.getWarnStyle());
		}
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	
	
    public String getRentStatusComment() {
		return rentStatusComment;
	}

	public void setRentStatusComment(String rentStatusComment) {
		this.rentStatusComment = rentStatusComment;
	}

	public String getRentStyleComment() {
		return rentStyleComment;
	}

	public void setRentStyleComment(String rentStyleComment) {
		this.rentStyleComment = rentStyleComment;
	}

	public String getFeeStyleComment() {
		return feeStyleComment;
	}

	public void setFeeStyleComment(String feeStyleComment) {
		this.feeStyleComment = feeStyleComment;
	}

	public String getSettleCycleComment() {
		return settleCycleComment;
	}

	public void setSettleCycleComment(String settleCycleComment) {
		this.settleCycleComment = settleCycleComment;
	}

	public String getWarnFrequencyComment() {
		return warnFrequencyComment;
	}

	public void setWarnFrequencyComment(String warnFrequencyComment) {
		this.warnFrequencyComment = warnFrequencyComment;
	}

	public String getWarnStyleComment() {
		return warnStyleComment;
	}

	public void setWarnStyleComment(String warnStyleComment) {
		this.warnStyleComment = warnStyleComment;
	}

	public List<String> getOrgIdList() {
		return orgIdList;
	}

	public void setOrgIdList(List<String> orgIdList) {
		this.orgIdList = orgIdList;
	}

	public List<String> getMerchantIdList() {
		return merchantIdList;
	}

	public void setMerchantIdList(List<String> merchantIdList) {
		this.merchantIdList = merchantIdList;
	}

	public List<String> getWarehouseIdList() {
		return warehouseIdList;
	}

	public List<String> getNotInRentIds() {
		return notInRentIds;
	}

	public void setNotInRentIds(List<String> notInRentIds) {
		this.notInRentIds = notInRentIds;
	}

	public void setWarehouseIdList(List<String> warehouseIdList) {
		this.warehouseIdList = warehouseIdList;
	}
	
	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}

	public Example getConditionExample(){
		Criteria c = example.getOredCriteria().get(0);
		if(orgIdList != null && !orgIdList.isEmpty()){
			c.andIn("orgId", orgIdList);
		}
		if(merchantIdList != null && !merchantIdList.isEmpty()){
			c.andIn("merchantId", merchantIdList);
		}
		if(warehouseIdList != null && !warehouseIdList.isEmpty()){
			c.andIn("warehouseId", warehouseIdList);
		}
		if(parkRent.getRentStatus() != null){
			c.andEqualTo("rentStatus",parkRent.getRentStatus());
		}
		if(notInRentIds != null && !notInRentIds.isEmpty()){
			c.andNotIn("rentId", notInRentIds);
		}
		if(StringUtil.isTrimEmpty(parkRent.getOrgId())){
			c.andEqualTo("orgId",parkRent.getOrgId());
		}
		if(StringUtil.isTrimEmpty(parkRent.getMerchantId())){
			c.andEqualTo("merchantId",parkRent.getMerchantId());
		}
		if(StringUtil.isTrimEmpty(parkRent.getWarehouseId())){
			c.andEqualTo("warehouseId",parkRent.getWarehouseId());
		}
		if(orderTime != null){
			c.andLessThanOrEqualTo("startTime", orderTime);
			c.andGreaterThanOrEqualTo("endTime",orderTime);
		}
		return example;
	}
	
	public ParkRentVo addCondition(String condition){
		Criteria c = example.getOredCriteria().get(0);
		if(condition == null  || StringUtils.isEmpty(condition)) return this;
		
		c.andCondition(condition);
		return this;
		
	}
	

	public static Map<String,ParkRentVo> parseListToMap(List<ParkRentVo> rentList){
    	if(rentList == null || rentList.isEmpty()) return null;
    	
    	Map<String,ParkRentVo> map = rentList.stream().collect(Collectors.toMap((p)->p.getParkRent().getRentId(), p->p));
    	return map;
    }
    
    public static List<String> idToList(List<ParkRentVo> rentVoList){
    	if(rentVoList == null || rentVoList.isEmpty()) return null;
    	List<String> list = rentVoList.stream().map(p->p.getParkRent().getRentId()).collect(Collectors.toList());
    	return list;
    }

}
