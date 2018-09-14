/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * 创建日期:<br/> 2017年2月8日 下午4:15:24<br/>
 * @author 王通<br/>
 */
package com.yunkouan.wms.modules.inv.vo;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.yunkouan.base.BaseVO;
import com.yunkouan.valid.ValidSave;
import com.yunkouan.valid.ValidUpdate;
import com.yunkouan.wms.modules.inv.entity.InvAdjust;
import com.yunkouan.wms.modules.sys.entity.SysAccount;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 
  * 盈亏调整单VO对象
  * @author 王通
  * @date 2017年2月13日 下午3:09:27
  *
 */
public class InvAdjustVO extends BaseVO {
	
	private static final long serialVersionUID = 7539617122998544603L;
	
	/**
	 * 
	  * 创建一个新的实例 AdjustVO. 
	  * <p>Title: </p>
	  * <p>Description: </p>
	  * @author 王通
	  * @date 2017年2月13日 下午3:25:28
	 */
	
	public InvAdjustVO(){}
	/**
	  * 创建一个新的实例 InvAdjustVO. 
	  * <p>Title: </p>
	  * <p>Description: </p>
	  * @author 王通
	  * @date 2017年2月13日 下午3:28:08
	  * @param adj 盈亏调整单
	 */
	public InvAdjustVO( InvAdjust invAdjust ){
		this.setInvAdjust(invAdjust);
	}
	
	/**
	 * 盈亏调整单对象
	 */
	@NotNull(message="{valid_adjust_is_null}",groups={ValidSave.class,ValidUpdate.class})
	private InvAdjust invAdjust;

    @NotNull(message="{valid_adjust_detail_is_null}",groups={ValidSave.class,ValidUpdate.class})
	private List<InvAdjustDetailVO> adjDetailVoList;
	
	private String orgName;
	
	private String ownerName;
	private String merchantShortName;
	
	private String dataFromName;
	
	private String createUserName;
	
	private String updateUserName;
	
	private String statusName;
	
	private String countNo;
	
	/* getset *************************************************/
	
	/**
	 * 属性 adjDetailVoList getter方法
	 * @return 属性adjDetailVoList
	 * @author 王通<br/>
	 */
	public List<InvAdjustDetailVO> getAdjDetailVoList() {
		return adjDetailVoList;
	}
	/**
	 * 属性 adjDetailVoList setter方法
	 * @param adjDetailVoList 设置属性adjDetailVoList的值
	 * @author 王通<br/>
	 */
	public void setAdjDetailVoList(List<InvAdjustDetailVO> adjDetailVoList) {
		this.adjDetailVoList = adjDetailVoList;
	}
	/**
	 * 属性 countNo getter方法
	 * @return 属性countNo
	 * @author 王通<br/>
	 */
	public String getCountNo() {
		return countNo;
	}
	/**
	 * 属性 countNo setter方法
	 * @param countNo 设置属性countNo的值
	 * @author 王通<br/>
	 */
	public void setCountNo(String countNo) {
		this.countNo = countNo;
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
	 * 属性 dataFromName getter方法
	 * @return 属性dataFromName
	 * @author 王通<br/>
	 */
	public String getDataFromName() {
		return dataFromName;
	}
	/**
	 * 属性 dataFromName setter方法
	 * @param dataFromName 设置属性dataFromName的值
	 * @author 王通<br/>
	 */
	public void setDataFromName(String dataFromName) {
		this.dataFromName = dataFromName;
	}
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

	/* method ********************************************/
	
	/**
	 * 
	  * 查询缓存
	  * @Description: TODO
	  * @param @return    设定文件
	  * @return InvAdjustVO    返回类型
	  * @throws Exception
	  * @anthor 王通
	  * @date 2017年2月13日 下午4:05:29
	 */
	public InvAdjustVO searchCache () {
		return this;
	}
	
	/**
	 * 设置当前登录人信息
	  * @Description: TODO
	  * @param @param loginUser    设定文件
	  * @return void    返回类型
	  * @throws Exception
	  * @anthor 王通
	  * @date 2017年2月13日 下午4:06:27
	 */
	public void setLoginUser( SysAccount loginUser ) {
	}
	/*
	  * <p>Title: getExample</p>
	  * <p>Description: </p>
	  * @return
	  * @see com.yunkouan.base.BaseVO#getExample()
	  */
	
	
	@Override
	public Example getExample() {
		Example example = new Example(InvAdjust.class);
		Criteria c = example.createCriteria();
		c.andEqualTo("warehouseId", this.invAdjust.getWarehouseId());
		c.andEqualTo("orgId", this.invAdjust.getOrgId());
		if(this.invAdjust.getAdjustStatus() != null) {
			c.andEqualTo("adjustStatus", this.invAdjust.getAdjustStatus());
		}
		return example;
	}
	/**
	 * 属性 invAdjust getter方法
	 * @return 属性invAdjust
	 * @author 王通<br/>
	 */
	public InvAdjust getInvAdjust() {
		return invAdjust;
	}
	/**
	 * 属性 invAdjust setter方法
	 * @param invAdjust 设置属性invAdjust的值
	 * @author 王通<br/>
	 */
	public void setInvAdjust(InvAdjust invAdjust) {
		this.invAdjust = invAdjust;
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
	
	
}
