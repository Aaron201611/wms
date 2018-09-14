package com.yunkouan.wms.modules.park.vo;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.yunkouan.base.BaseVO;
import com.yunkouan.saas.modules.sys.service.ISysParamService;
import com.yunkouan.util.SpringContextHolder;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.common.constant.CacheName;
import com.yunkouan.wms.modules.park.entity.ParkOrgBusiStas;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

public class ParkOrgBusiStasVo extends BaseVO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ParkOrgBusiStas parkOrgBusiStas = new ParkOrgBusiStas();
	
	private String orgName;
	
	private String merchantName;
	
	private String beginDate;
	
	private String endDate;
	
	private String busiTypeComment;
	
	private List<String> orgIdList;
	
	private List<String> merchantList;
	
	
	private Example example;

	public ParkOrgBusiStasVo(){
		this.example = new Example(ParkOrgBusiStas.class);
		example.setOrderByClause("update_time desc");
		example.createCriteria();
	}

	public ParkOrgBusiStas getParkOrgBusiStas() {
		return parkOrgBusiStas;
	}

	public void setParkOrgBusiStas(ParkOrgBusiStas parkOrgBusiStas) {
		this.parkOrgBusiStas = parkOrgBusiStas;
		if(parkOrgBusiStas != null){
			ISysParamService paramService = SpringContextHolder.getBean(ISysParamService.class);
			busiTypeComment = paramService.getValue(CacheName.ASN_DOCTYPE, parkOrgBusiStas.getBusiType());
			if(StringUtil.isTrimEmpty(busiTypeComment)){
				busiTypeComment = paramService.getValue(CacheName.DELIVERY_TYPE, parkOrgBusiStas.getBusiType());
			}		
		}
	}

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	
	
	public String getBusiTypeComment() {
		return busiTypeComment;
	}

	public void setBusiTypeComment(String busiTypeComment) {
		this.busiTypeComment = busiTypeComment;
	}

	public List<String> getOrgIdList() {
		return orgIdList;
	}

	public void setOrgIdList(List<String> orgIdList) {
		this.orgIdList = orgIdList;
	}

	public List<String> getMerchantList() {
		return merchantList;
	}

	public void setMerchantList(List<String> merchantList) {
		this.merchantList = merchantList;
	}

	public Example getExample() {
		return example;
	}

	public void setExample(Example example) {
		this.example = example;
	}

	public ParkOrgBusiStasVo orderBy(String orderByStr){	
		this.example.setOrderByClause(orderByStr);
		example.createCriteria();
		return this;
	}
	
	/**
	 * 设置查询条件
	 * @return
	 * @version 2017年3月10日 上午11:37:02<br/>
	 * @author Aaron He<br/>
	 */
	public Example getConditionExample(){
		Criteria c = this.example.getOredCriteria().get(0);
		if(orgIdList != null && !orgIdList.isEmpty()){
			c.andIn("orgId", orgIdList);
		}
		
		if(merchantList != null && !merchantList.isEmpty()){
			c.andIn("merchantId", merchantList);
		}
		
		if(StringUtils.isNotEmpty(beginDate) && StringUtils.isNotEmpty(endDate)){
			c.andBetween("stasDate", beginDate, endDate+" 23:59:59");
		}
		
		if(parkOrgBusiStas.getBusiType() != null && parkOrgBusiStas.getBusiType().intValue() != 0){
			c.andCondition("busi_type = ", parkOrgBusiStas.getBusiType());
		}
		return example;
	}
	
	public Criteria andCondition(String condition,String value){
		Criteria c = this.example.getOredCriteria().get(0); 
		if(StringUtil.isEmpty(value)) return c;
		c.andCondition(condition, value);
		return c;
	}

}
