package com.yunkouan.wms.modules.park.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.yunkouan.base.BaseVO;
import com.yunkouan.saas.modules.sys.service.ISysParamService;
import com.yunkouan.util.SpringContextHolder;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.common.constant.CacheName;
import com.yunkouan.wms.modules.park.entity.ParkWarn;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

public class ParkWarnVo extends BaseVO{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6741823757279602661L;
	
	private ParkRentVo parkRentVo;
	
	private ParkWarn parkWarn = new ParkWarn();
	
	private String orgName;
	
	private String warehouseName;
	
	private String merchantName;
	
	private String warnStatusComment;
	
	private int countDown;//倒计时天
	
	private Example example;
	
	private List<String> rentIdList;
		
	public static final String PARK_RENT_EXPIRE = "租金到期";

	
	public ParkWarnVo(){
		super();
		example = new Example(ParkWarn.class);
		example.setOrderByClause("update_time desc");
		example.createCriteria();
	}
	
	public ParkRentVo getParkRentVo() {
		return parkRentVo;
	}

	public void setParkRentVo(ParkRentVo parkRentVo) {
		this.parkRentVo = parkRentVo;
		 Date now = new Date();
		 if(parkRentVo.getParkRent().getEndTime()!= null){
			 long m = parkRentVo.getParkRent().getEndTime().getTime() - now.getTime();
			 this.countDown = (int)(m/(1000 * 60 * 60 * 24));
		 }	 	
	}

	public ParkWarn getParkWarn() {
		return parkWarn;
	}

	public void setParkWarn(ParkWarn parkWarn) {
		this.parkWarn = parkWarn;
		ISysParamService paramService = SpringContextHolder.getBean(ISysParamService.class);
		if(parkWarn.getWarnStatus() != null){
			this.warnStatusComment = paramService.getValue(CacheName.PARK_WARN_STATUS, parkWarn.getWarnStatus());
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
	
	

	public String getWarnStatusComment() {
		return warnStatusComment;
	}

	public void setWarnStatusComment(String warnStatusComment) {
		this.warnStatusComment = warnStatusComment;
	}

	public Example getExample() {
		return example;
	}

	public void setExample(Example example) {
		this.example = example;
	}

	public List<String> getRentIdList() {
		return rentIdList;
	}

	public void setRentIdList(List<String> rentIdList) {
		this.rentIdList = rentIdList;
	}
	
	public Example getConditionExample(){
		Criteria c = example.getOredCriteria().get(0);
		if(rentIdList != null && !rentIdList.isEmpty()){
			c.andIn("rentId", rentIdList);
		}
		if(!StringUtil.isTrimEmpty(parkWarn.getRentId())){
			c.andEqualTo("rentId",parkWarn.getRentId());
		}

		if(parkWarn.getWarnStatus() != null){
			c.andEqualTo("warnStatus",parkWarn.getWarnStatus());
		}
		return example;
	}

	public int getCountDown() {
		return countDown;
	}

	public void setCountDown(int countDown) {
		this.countDown = countDown;
	}
	
	
}
