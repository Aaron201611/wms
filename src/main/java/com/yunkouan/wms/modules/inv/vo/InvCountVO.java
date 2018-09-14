/**
 * Created on 2017年2月16日
 * InvCountVO.java V1.0
 *
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2016 =======================================================
 */
package com.yunkouan.wms.modules.inv.vo;

import java.util.List;

import com.yunkouan.base.BaseVO;
import com.yunkouan.saas.modules.sys.service.ISysParamService;
import com.yunkouan.util.SpringContextHolder;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.common.constant.CacheName;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.inv.entity.InvCount;
import com.yunkouan.wms.modules.ts.vo.TsTaskVo;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 盘点单VO对象<br/><br/>
 * @Description 
 * @version 2017年2月16日 上午9:52:01<br/>
 * @author 王通<br/>
 */
public class InvCountVO extends BaseVO {

	/**
	 * 
	 * @version 2017年2月16日 上午10:06:30<br/>
	 * @author 王通<br/>
	 */
	private static final long serialVersionUID = 7066852346579800875L;

	/**
	 * 
	 * 构造方法
	 * @version 2017年2月16日 上午10:06:17<br/>
	 * @author 王通<br/>
	 */
	public InvCountVO(){}
	
	/**
	 * 
	 * 构造方法
	 * @param count
	 * @version 2017年2月16日 上午10:07:21<br/>
	 * @author 王通<br/>
	 */
	public InvCountVO(InvCount invCount){
		this.invCount = invCount;
	}

	/**
	 * 盘点单对象
	 * @version 2017年2月16日 上午10:07:48<br/>
	 * @author 王通<br/>
	 */
	private InvCount invCount;

	/**
	 * 仓库名
	 * @version 2017年2月16日 上午11:39:36<br/>
	 * @author 王通<br/>
	 */
	private String warehouseName;
	
	private String applyNo;
	/**
	 * 货主名称
	 * @version 2017年2月16日 上午11:39:36<br/>
	 * @author 王通<br/>
	 */
	private String ownerName;
	private String merchantShortName;

	/**
	 * 企业名
	 * @version 2017年2月16日 上午11:39:36<br/>
	 * @author 王通<br/>
	 */
	private String orgName;
	/**
	 * 状态名
	 * @version 2017年2月16日 上午11:39:36<br/>
	 * @author 王通<br/>
	 */
	private String statusName;
	/**
	 * 盘点类型名
	 * @version 2017年2月16日 上午11:39:36<br/>
	 * @author 王通<br/>
	 */
	private String countTypeName;
	/**
	 * 是否冻结名称
	 * @version 2017年2月16日 上午11:39:36<br/>
	 * @author 王通<br/>
	 */
	private String isFreezeName;

	/**
	 * 库位数量
	 * @version 2017年2月16日 上午11:39:36<br/>
	 * @author 王通<br/>
	 */
	private Integer locationQty;
	/**
	 * 库位名称
	 * @version 2017年2月16日 上午11:39:36<br/>
	 * @author 王通<br/>
	 */
	private String locationName;
	/**
	 * 货品名称
	 * @version 2017年2月16日 上午11:39:36<br/>
	 * @author 王通<br/>
	 */
	private String skuName;
	/**
	 * 规格型号
	 * @version 2017年2月16日 上午11:39:36<br/>
	 * @author 王通<br/>
	 */
	private String specModel;
	/**
	 * 计量单位
	 * @version 2017年2月16日 上午11:39:36<br/>
	 * @author 王通<br/>
	 */
	private String measureUnit;
	/**
	 * 货品编号
	 * @version 2017年2月16日 上午11:39:36<br/>
	 * @author 王通<br/>
	 */
	private String skuNo;
	/**
	 * 创建人姓名
	 * @version 2017年2月16日 上午11:39:36<br/>
	 * @author 王通<br/>
	 */
	private String createUserName;
	/**
	 * 修改人姓名
	 * @version 2017年2月16日 上午11:39:36<br/>
	 * @author 王通<br/>
	 */
	private String updateUserName;
	
	/**
	 * 作业人员
	 * @version 2017年3月8日 上午10:59:02<br/>
	 * @author 王通<br/>
	 */
	private String opPersonName;
	/**
	 * 盘点结果名称
	 * @version 2017年3月8日 上午10:59:02<br/>
	 * @author 王通<br/>
	 */
	private String ResultName;
	
    private String transStatusComment;
	
	
	
	/**
	 * 页面显示盘点单明细VO集合
	 * @version 2017年2月16日10:11:40<br/>
	 * @author 王通<br/>
	 */
	private List<InvCountDetailVO> listInvCountDetailVO;
	/**
	 * 批量操作（如生效失效）时的id集合
	 * @version 2017年7月20日 上午10:14:24<br/>
	 * @author 王通<br/>
	 */
	private List<String> countIdList;
	
	/**
	 * 任务列表VO
	 * @version 2017年7月20日 上午9:58:52<br/>
	 * @author 王通<br/>
	 */
	private TsTaskVo tsTaskVo = new TsTaskVo();
	
	private String beginTime;
	
	private String endTime;
	/* getset *************************************************/
	
	/**
	 * 属性 createUserName getter方法
	 * @return 属性createUserName
	 * @author 王通<br/>
	 */
	public String getCreateUserName() {
		return createUserName;
	}

	/**
	 * 属性 createUserName setter方法
	 * @param createUserName 设置属性createUserName的值
	 * @author 王通<br/>
	 */
	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	/**
	 * 属性 updateUserName getter方法
	 * @return 属性updateUserName
	 * @author 王通<br/>
	 */
	public String getUpdateUserName() {
		return updateUserName;
	}

	/**
	 * 属性 updateUserName setter方法
	 * @param updateUserName 设置属性updateUserName的值
	 * @author 王通<br/>
	 */
	public void setUpdateUserName(String updateUserName) {
		this.updateUserName = updateUserName;
	}
	/**
	 * 属性 listInvCountDetailVO getter方法
	 * @return 属性listInvCountDetailVO
	 * @author 王通<br/>
	 */
	public List<InvCountDetailVO> getListInvCountDetailVO() {
		return listInvCountDetailVO;
	}
	/**
	 * 属性 listInvCountDetailVO setter方法
	 * @param listInvCountDetailVO 设置属性listInvCountDetailVO的值
	 * @author 王通<br/>
	 */
	public void setListInvCountDetailVO(List<InvCountDetailVO> listInvCountDetailVO) {
		this.listInvCountDetailVO = listInvCountDetailVO;
	}

	/**
	 * 属性 warehouseName getter方法
	 * @return 属性warehouseName
	 * @author 王通<br/>
	 */
	public String getWarehouseName() {
		return warehouseName;
	}

	/**
	 * 属性 warehouseName setter方法
	 * @param warehouseName 设置属性warehouseName的值
	 * @author 王通<br/>
	 */
	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	/**
	 * 属性 orgName getter方法
	 * @return 属性orgName
	 * @author 王通<br/>
	 */
	public String getOrgName() {
		return orgName;
	}

	/**
	 * 属性 orgName setter方法
	 * @param orgName 设置属性orgName的值
	 * @author 王通<br/>
	 */
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	/* method ********************************************/
	
	/**
	 * 查询缓存
	 * @return
	 * 创建日期:<br/> 2017年2月16日10:19:42<br/>
	 * @author 王通<br/>
	 */
	public InvCountVO searchCache () {
		if ( StringUtil.isNotBlank(this.invCount.getTransStatus())) {
			ISysParamService paramService = SpringContextHolder.getBean(ISysParamService.class);
			this.transStatusComment = paramService.getValue(CacheName.MSMQ_CHK_RESULT, this.invCount.getTransStatus());
		}
		return this;
	}
	
	/**
	 * 设置当前登录人信息（企业id，仓库id）
	 * @param loginUser 当前登录人对象
	 * @version 2017年2月16日10:20:03<br/>
	 * @author 王通<br/>
	 */
	public void setLoginUser( Principal loginUser ) {
		if ( loginUser == null || this.invCount == null ) {
			return;
		}
		this.invCount.setOrgId(loginUser.getOrgId());
		this.invCount.setWarehouseId(LoginUtil.getWareHouseId());
	}

	/**
	 * @param invCount
	 * @return
	 * @Description 
	 * @version 2017年2月16日 上午10:47:07<br/>
	 * @author 王通<br/>
	 */
	public InvCountVO setEntity(InvCount invCount) {
		this.invCount = invCount;
		return this;
	}
	/*
	  * <p>Title: getExample</p>
	  * <p>Description: </p>
	  * @return
	  * @see com.yunkouan.base.BaseVO#getExample()
	  */
	
	
	@Override
	public Example getExample() {
		Example example = new Example(InvCount.class);
//		example.setOrderByClause("count_id2 desc");
		Criteria c = example.createCriteria();
		c.andEqualTo("warehouseId", this.invCount.getWarehouseId());
		c.andEqualTo("orgId", this.invCount.getOrgId());
		if(!StringUtil.isTrimEmpty(this.invCount.getOwner())) {
			c.andLike("owner", StringUtil.likeEscapeH(this.invCount.getOwner()));
		}
		if(this.invCount.getCountType() != null) {
			c.andEqualTo("countType", this.invCount.getCountType());
		}
		if(this.invCount.getCountStatus() != null) {
			c.andEqualTo("countStatus", this.invCount.getCountStatus());
		}
		if(StringUtil.isNotEmpty(beginTime)){
			c.andGreaterThanOrEqualTo("createTime", beginTime+" 00:00:00");
		}
		if(StringUtil.isNotEmpty(endTime)){
			c.andLessThanOrEqualTo("createTime", endTime+" 23:59:59");
		}
		return example;
	}

	/**
	 * 属性 statusName getter方法
	 * @return 属性statusName
	 * @author 王通<br/>
	 */
	public String getStatusName() {
		return statusName;
	}

	/**
	 * 属性 statusName setter方法
	 * @param statusName 设置属性statusName的值
	 * @author 王通<br/>
	 */
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	/**
	 * 属性 countTypeName getter方法
	 * @return 属性countTypeName
	 * @author 王通<br/>
	 */
	public String getCountTypeName() {
		return countTypeName;
	}

	/**
	 * 属性 countTypeName setter方法
	 * @param countTypeName 设置属性countTypeName的值
	 * @author 王通<br/>
	 */
	public void setCountTypeName(String countTypeName) {
		this.countTypeName = countTypeName;
	}

	/**
	 * 属性 isFreezeName getter方法
	 * @return 属性isFreezeName
	 * @author 王通<br/>
	 */
	public String getIsFreezeName() {
		return isFreezeName;
	}

	/**
	 * 属性 isFreezeName setter方法
	 * @param isFreezeName 设置属性isFreezeName的值
	 * @author 王通<br/>
	 */
	public void setIsFreezeName(String isFreezeName) {
		this.isFreezeName = isFreezeName;
	}

	/**
	 * 属性 locationQty getter方法
	 * @return 属性locationQty
	 * @author 王通<br/>
	 */
	public Integer getLocationQty() {
		return locationQty;
	}

	/**
	 * 属性 locationQty setter方法
	 * @param locationQty 设置属性locationQty的值
	 * @author 王通<br/>
	 */
	public void setLocationQty(Integer locationQty) {
		this.locationQty = locationQty;
	}

	/**
	 * 属性 opPersonName getter方法
	 * @return 属性opPersonName
	 * @author 王通<br/>
	 */
	public String getOpPersonName() {
		return opPersonName;
	}

	/**
	 * 属性 opPersonName setter方法
	 * @param opPersonName 设置属性opPersonName的值
	 * @author 王通<br/>
	 */
	public void setOpPersonName(String opPersonName) {
		this.opPersonName = opPersonName;
	}

	/**
	 * 属性 ownerName getter方法
	 * @return 属性ownerName
	 * @author 王通<br/>
	 */
	public String getOwnerName() {
		return ownerName;
	}

	/**
	 * 属性 ownerName setter方法
	 * @param ownerName 设置属性ownerName的值
	 * @author 王通<br/>
	 */
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	/**
	 * 属性 locationName getter方法
	 * @return 属性locationName
	 * @author 王通<br/>
	 */
	public String getLocationName() {
		return locationName;
	}

	/**
	 * 属性 locationName setter方法
	 * @param locationName 设置属性locationName的值
	 * @author 王通<br/>
	 */
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	/**
	 * 属性 skuName getter方法
	 * @return 属性skuName
	 * @author 王通<br/>
	 */
	public String getSkuName() {
		return skuName;
	}

	/**
	 * 属性 skuName setter方法
	 * @param skuName 设置属性skuName的值
	 * @author 王通<br/>
	 */
	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}

	/**
	 * 属性 resultName getter方法
	 * @return 属性resultName
	 * @author 王通<br/>
	 */
	public String getResultName() {
		return ResultName;
	}

	/**
	 * 属性 resultName setter方法
	 * @param resultName 设置属性resultName的值
	 * @author 王通<br/>
	 */
	public void setResultName(String resultName) {
		ResultName = resultName;
	}

	/**
	 * 属性 invCount getter方法
	 * @return 属性invCount
	 * @author 王通<br/>
	 */
	public InvCount getInvCount() {
		return invCount;
	}

	/**
	 * 属性 invCount setter方法
	 * @param invCount 设置属性invCount的值
	 * @author 王通<br/>
	 */
	public void setInvCount(InvCount invCount) {
		this.invCount = invCount;
		if ( StringUtil.isNotBlank(this.invCount.getTransStatus())) {
			ISysParamService paramService = SpringContextHolder.getBean(ISysParamService.class);
			this.transStatusComment = paramService.getValue(CacheName.MSMQ_CHK_RESULT, this.invCount.getTransStatus());
		}
	}

	/**
	 * 属性 tsTaskVo getter方法
	 * @return 属性tsTaskVo
	 * @author 王通<br/>
	 */
	public TsTaskVo getTsTaskVo() {
		return tsTaskVo;
	}

	/**
	 * 属性 tsTaskVo setter方法
	 * @param tsTaskVo 设置属性tsTaskVo的值
	 * @author 王通<br/>
	 */
	public void setTsTaskVo(TsTaskVo tsTaskVo) {
		this.tsTaskVo = tsTaskVo;
	}

	/**
	 * 属性 countIdList getter方法
	 * @return 属性countIdList
	 * @author 王通<br/>
	 */
	public List<String> getCountIdList() {
		return countIdList;
	}

	/**
	 * 属性 countIdList setter方法
	 * @param countIdList 设置属性countIdList的值
	 * @author 王通<br/>
	 */
	public void setCountIdList(List<String> countIdList) {
		this.countIdList = countIdList;
	}

	/**
	 * 属性 merchantShortName getter方法
	 * @return 属性merchantShortName
	 * @author 王通<br/>
	 */
	public String getMerchantShortName() {
		return merchantShortName;
	}

	/**
	 * 属性 merchantShortName setter方法
	 * @param merchantShortName 设置属性merchantShortName的值
	 * @author 王通<br/>
	 */
	public void setMerchantShortName(String merchantShortName) {
		this.merchantShortName = merchantShortName;
	}

	public String getTransStatusComment() {
		return transStatusComment;
	}

	public void setTransStatusComment(String transStatusComment) {
		this.transStatusComment = transStatusComment;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}


	public String getSpecModel() {
		return specModel;
	}

	public void setSpecModel(String specModel) {
		this.specModel = specModel;
	}

	public String getMeasureUnit() {
		return measureUnit;
	}

	public void setMeasureUnit(String measureUnit) {
		this.measureUnit = measureUnit;
	}

	public String getSkuNo() {
		return skuNo;
	}

	public void setSkuNo(String skuNo) {
		this.skuNo = skuNo;
	}

	public String getApplyNo() {
		return applyNo;
	}

	public void setApplyNo(String applyNo) {
		this.applyNo = applyNo;
	}
	
}
