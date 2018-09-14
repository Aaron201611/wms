package com.yunkouan.wms.modules.application.vo;

import java.util.ArrayList;
import java.util.List;

import com.yunkouan.base.BaseVO;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.modules.application.entity.DeliverGoodsExamine;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

public class DeliverGoodsExamineVo extends BaseVO{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6723577016390389644L;
	
	private DeliverGoodsExamine entity;
	
	private String statusName;
	
	private String auditStepName;
	
	private String kernelTypeName;
	
	private String kernelBizModeName;
	
	private String iEFlagName;
	
	private Integer statusLessThan;

	public String getKernelTypeName() {
		return kernelTypeName;
	}
	

	public void setKernelTypeName(String kernelTypeName) {
		this.kernelTypeName = kernelTypeName;
	}
	

	public String getKernelBizModeName() {
		return kernelBizModeName;
	}
	

	public void setKernelBizModeName(String kernelBizModeName) {
		this.kernelBizModeName = kernelBizModeName;
	}
	

	public String getKernelBizTypeName() {
		return kernelBizTypeName;
	}
	

	public void setKernelBizTypeName(String kernelBizTypeName) {
		this.kernelBizTypeName = kernelBizTypeName;
	}
	

	private String kernelBizTypeName;
	
	private List<String> statusNotIn;
	
	private String orderByStr = "update_time desc";
	
	private List<DeliverGoodsExamineApplicationVo> examineApplicationVoList = new ArrayList<DeliverGoodsExamineApplicationVo>();
	
	private List<String> delAppIdList;
	private String examineNoLike;
	private String carNoLike;
	public DeliverGoodsExamineVo(DeliverGoodsExamine entity){
		this.entity = entity;
	}
	
	public DeliverGoodsExamineVo(){
		
	}
	
	public Example getExample(){
		Example example = new Example(DeliverGoodsExamine.class);
		example.setOrderByClause(orderByStr);
		Criteria c = example.createCriteria();
		
		if(StringUtil.isNotBlank(entity.getExamineNo())){
			c.andEqualTo("examineNo",entity.getExamineNo());
		}
		if(StringUtil.isNotBlank(entity.getIEFlag())){
			c.andEqualTo("iEFlag",entity.getIEFlag());
		}
		if(statusNotIn != null){
			c.andNotIn("status", statusNotIn);
		}
		if(StringUtil.isNotBlank(entity.getCarNo())){
			c.andEqualTo("carId",entity.getCarNo());
		}
		if(entity.getStatus() != null){
			c.andEqualTo("status",entity.getStatus());
		}
		if(statusLessThan != null){
			c.andLessThan("status", statusLessThan);
		}
		if(StringUtil.isNotBlank(entity.getOrgId())){
			c.andEqualTo("orgId",entity.getOrgId());
		}
		if(StringUtil.isNotBlank(entity.getWarehouseId())){
			c.andEqualTo("warehouseId",entity.getWarehouseId());
		}
		if(StringUtil.isNotBlank(examineNoLike)){
			c.andLike("examineNo","%"+examineNoLike+"%");
		}
		if(StringUtil.isNotBlank(carNoLike)){
			c.andLike("carNo","%"+carNoLike+"%");
		}
		
		return example;
	}

	public DeliverGoodsExamine getEntity() {
		return entity;
	}

	public void setEntity(DeliverGoodsExamine entity) {
		this.entity = entity;
	}

	public String getOrderByStr() {
		return orderByStr;
	}

	public void setOrderByStr(String orderByStr) {
		this.orderByStr = orderByStr;
	}

	public List<DeliverGoodsExamineApplicationVo> getExamineApplicationVoList() {
		return examineApplicationVoList;
	}

	public void setExamineApplicationVoList(List<DeliverGoodsExamineApplicationVo> examineApplicationVoList) {
		this.examineApplicationVoList = examineApplicationVoList;
	}

	public List<String> getDelAppIdList() {
		return delAppIdList;
	}

	public void setDelAppIdList(List<String> delAppIdList) {
		this.delAppIdList = delAppIdList;
	}

	public List<String> getStatusNotIn() {
		return statusNotIn;
	}

	public void setStatusNotIn(List<String> statusNotIn) {
		this.statusNotIn = statusNotIn;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getAuditStepName() {
		return auditStepName;
	}

	public void setAuditStepName(String auditStepName) {
		this.auditStepName = auditStepName;
	}


	public String getiEFlagName() {
		return iEFlagName;
	}


	public void setiEFlagName(String iEFlagName) {
		this.iEFlagName = iEFlagName;
	}


	public Integer getStatusLessThan() {
		return statusLessThan;
	}


	public void setStatusLessThan(Integer statusLessThan) {
		this.statusLessThan = statusLessThan;
	}


	/**
	获取examineNoLike  
	 * @return the examineNoLike
	 */
	public String getExamineNoLike() {
		return examineNoLike;
	}
	


	/**
	 * @param examineNoLike the examineNoLike to set
	 */
	public void setExamineNoLike(String examineNoLike) {
		this.examineNoLike = examineNoLike;
	}
	


	/**
	获取carNoLike  
	 * @return the carNoLike
	 */
	public String getCarNoLike() {
		return carNoLike;
	}
	


	/**
	 * @param carNoLike the carNoLike to set
	 */
	public void setCarNoLike(String carNoLike) {
		this.carNoLike = carNoLike;
	}
	

}
