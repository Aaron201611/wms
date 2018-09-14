/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * @version 2017年2月20日 下午3:56:07<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.rec.vo;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.yunkouan.base.BaseVO;
import com.yunkouan.util.PubUtil;
import com.yunkouan.util.StringUtil;
import com.yunkouan.valid.ValidSave;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.rec.entity.RecPutawayLocation;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * <br/><br/>
 * @version 2017年2月20日 下午3:56:07<br/>
 * @author andy wang<br/>
 */
public class RecPutawayLocationVO extends BaseVO {

	private static final long serialVersionUID = -2993982175292734948L;
	
	/**
	 * 构造方法
	 * @version 2017年2月20日 下午3:58:24<br/>
	 * @author andy wang<br/>
	 */
	public RecPutawayLocationVO() {
		this(new RecPutawayLocation());
	}
	
	/**
	 * 构造方法
	 * @param recPutawayLocation 上架单操作明细
	 * @version 2017年2月20日 下午3:58:38<br/>
	 * @author andy wang<br/>
	 */
	public RecPutawayLocationVO(RecPutawayLocation putawayLocation) {
		this.putawayLocation = putawayLocation;
	}

	/**
	 * 上架单操作明细
	 * @version 2017年2月20日下午4:00:18<br/>
	 * @author andy wang<br/>
	 */
	@Valid
	@NotNull(message="{valid_rec_putawayLocation_notnull}",groups={ValidSave.class})
	private RecPutawayLocation putawayLocation;

	/**
	 * 上架单操作明细id
	 * @version 2017年2月27日下午9:13:52<br/>
	 * @author andy wang<br/>
	 */
	private List<String> listPutawayLocationId;
	
	/**
	 * 库位名
	 * @version 2017年3月1日下午7:48:15<br/>
	 * @author andy wang<br/>
	 */
	private String locationComment;
	
	
	/**
	 * 上架单id
	 * @version 2017年3月3日上午8:57:49<br/>
	 * @author andy wang<br/>
	 */
	private String putawayId;
	
	/**
	 * 上架单明细id集合
	 * @version 2017年3月9日下午8:11:09<br/>
	 * @author andy wang<br/>
	 */
	private List<String> listPtwDetailId;
	
	/**
	 * 货品id
	 * @version 2017年5月15日下午4:56:39<br/>
	 * @author andy wang<br/>
	 */
	private String skuId;
	
	
	/**
	 * 货品单体
	 * @version 2017年5月16日下午6:58:42<br/>
	 * @author andy wang<br/>
	 */
	private Double perVolume;
	
	/**
	 * 货品单重
	 * @version 2017年5月16日下午6:58:45<br/>
	 * @author andy wang<br/>
	 */
	private Double perWeight;
	
	/* 新属性 20170717**************************************************/

	/** 所属上架单明细 add by andy wang */
	private RecPutawayDetailVO ptwDetailVO;
	/** 计划库位编号 add by andy wang */
	private String planLocationNo;
	/** 实际库位编号 add by andy wang */
	private String realLocationNo;
	
	/* getset **************************************************/
	
	/**
	 * 属性 putawayLocation getter方法
	 * @return 属性putawayLocation
	 * @author andy wang<br/>
	 */
	public RecPutawayLocation getPutawayLocation() {
		return putawayLocation;
	}

	/**
	 * 属性 putawayLocation setter方法
	 * @param putawayLocation 设置属性putawayLocation的值
	 * @author andy wang<br/>
	 */
	public void setPutawayLocation(RecPutawayLocation putawayLocation) {
		this.putawayLocation = putawayLocation;
	}
	
	public String getRealLocationNo() {
		return realLocationNo;
	}

	public void setRealLocationNo(String realLocationNo) {
		this.realLocationNo = realLocationNo;
	}

	public RecPutawayDetailVO getPtwDetailVO() {
		return ptwDetailVO;
	}

	public void setPtwDetailVO(RecPutawayDetailVO ptwDetailVO) {
		this.ptwDetailVO = ptwDetailVO;
	}

	public String getPlanLocationNo() {
		return planLocationNo;
	}

	public void setPlanLocationNo(String planLocationNo) {
		this.planLocationNo = planLocationNo;
	}

	/**
	 * 属性 perVolume getter方法
	 * @return 属性perVolume
	 * @author andy wang<br/>
	 */
	public Double getPerVolume() {
		return perVolume;
	}

	/**
	 * 属性 perVolume setter方法
	 * @param perVolume 设置属性perVolume的值
	 * @author andy wang<br/>
	 */
	public void setPerVolume(Double perVolume) {
		this.perVolume = perVolume;
	}

	/**
	 * 属性 perWeight getter方法
	 * @return 属性perWeight
	 * @author andy wang<br/>
	 */
	public Double getPerWeight() {
		return perWeight;
	}

	/**
	 * 属性 perWeight setter方法
	 * @param perWeight 设置属性perWeight的值
	 * @author andy wang<br/>
	 */
	public void setPerWeight(Double perWeight) {
		this.perWeight = perWeight;
	}

	/**
	 * 属性 skuId getter方法
	 * @return 属性skuId
	 * @author andy wang<br/>
	 */
	public String getSkuId() {
		return skuId;
	}

	/**
	 * 属性 skuId setter方法
	 * @param skuId 设置属性skuId的值
	 * @author andy wang<br/>
	 */
	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}

	/**
	 * 属性 listPtwDetailId getter方法
	 * @return 属性listPtwDetailId
	 * @author andy wang<br/>
	 */
	public List<String> getListPtwDetailId() {
		return listPtwDetailId;
	}

	/**
	 * 属性 listPtwDetailId setter方法
	 * @param listPtwDetailId 设置属性listPtwDetailId的值
	 * @author andy wang<br/>
	 */
	public void setListPtwDetailId(List<String> listPtwDetailId) {
		this.listPtwDetailId = listPtwDetailId;
	}

	/**
	 * 属性 putawayId getter方法
	 * @return 属性putawayId
	 * @author andy wang<br/>
	 */
	public String getPutawayId() {
		return putawayId;
	}

	/**
	 * 属性 putawayId setter方法
	 * @param putawayId 设置属性putawayId的值
	 * @author andy wang<br/>
	 */
	public void setPutawayId(String putawayId) {
		this.putawayId = putawayId;
	}

	/**
	 * 属性 locationComment getter方法
	 * @return 属性locationComment
	 * @author andy wang<br/>
	 */
	public String getLocationComment() {
		return locationComment;
	}

	/**
	 * 属性 locationComment setter方法
	 * @param locationComment 设置属性locationComment的值
	 * @author andy wang<br/>
	 */
	public void setLocationComment(String locationComment) {
		this.locationComment = locationComment;
	}

	/**
	 * 属性 listPutawayLocationId getter方法
	 * @return 属性listPutawayLocationId
	 * @author andy wang<br/>
	 */
	public List<String> getListPutawayLocationId() {
		return listPutawayLocationId;
	}

	/**
	 * 属性 listPutawayLocationId setter方法
	 * @param listPutawayLocationId 设置属性listPutawayLocationId的值
	 * @author andy wang<br/>
	 */
	public void setListPutawayLocationId(List<String> listPutawayLocationId) {
		this.listPutawayLocationId = listPutawayLocationId;
	}

	/* method *************************************************/
	

	/**
	 * 查询缓存
	 * @return
	 * 创建日期:<br/> 2017年2月9日 下午2:53:13<br/>
	 * @author andy wang<br/>
	 */
	public RecPutawayLocationVO seachCache () {
		return this;
	}
	/**
	 * 设置当前登录人信息（企业id，仓库id）
	 * @version 2017年3月25日 上午11:08:27<br/>
	 * @author andy wang<br/>
	 */
	public void loginInfo() {
		Principal loginUser = LoginUtil.getLoginUser();
		this.loginInfo(loginUser);
	}
	/**
	 * 设置当前登录人信息（企业id，仓库id）
	 * @param loginUser 当前登录人对象
	 * @version 2017年3月25日 上午11:08:27<br/>
	 * @author andy wang<br/>
	 */
	public void loginInfo(Principal loginUser) {
		if ( this.putawayLocation == null ) {
			this.putawayLocation = new RecPutawayLocation();
		}
		this.putawayLocation.setOrgId(loginUser.getOrgId());
		this.putawayLocation.setWarehouseId(LoginUtil.getWareHouseId());
	}
	
	public Example getExample () {
		if ( this.getPutawayLocation() == null ) {
			return null;
		}
		RecPutawayLocation putawayLocation = this.getPutawayLocation();
		Example example = new Example(RecPutawayLocation.class);
		Criteria criteria = example.createCriteria();
		if ( !StringUtil.isTrimEmpty(putawayLocation.getPutawayLocationId()) ) {
			criteria.andEqualTo("putawayLocationId", putawayLocation.getPutawayLocationId());
		}
		if ( !PubUtil.isEmpty(this.getListPutawayLocationId()) ) {
			criteria.andIn("putawayLocationId", this.getListPutawayLocationId());
		}
		if ( !StringUtil.isTrimEmpty(putawayLocation.getLocationId()) ) {
			criteria.andEqualTo("locationId", putawayLocation.getLocationId());
		}
		if ( !StringUtil.isTrimEmpty(putawayLocation.getPutawayDetailId()) ) {
			criteria.andEqualTo("putawayDetailId", putawayLocation.getPutawayDetailId());
		}
		if ( !PubUtil.isEmpty(this.getListPtwDetailId()) ) {
			criteria.andIn("putawayDetailId", this.getListPtwDetailId());
		}
		if ( putawayLocation.getPutawayType() != null ) {
			criteria.andEqualTo("putawayType", putawayLocation.getPutawayType());
		}
		if ( !StringUtil.isTrimEmpty(putawayLocation.getOrgId()) ) {
			criteria.andEqualTo("orgId", putawayLocation.getOrgId());
		}
		if ( !StringUtil.isTrimEmpty(putawayLocation.getWarehouseId()) ) {
			criteria.andEqualTo("warehouseId", putawayLocation.getWarehouseId());
		}
		return example;
	}

}
