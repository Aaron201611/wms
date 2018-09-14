/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * @version 2017年3月11日 下午3:12:25<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.meta.vo;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;

import com.yunkouan.base.BaseVO;
import com.yunkouan.saas.modules.sys.entity.MetaArea;
import com.yunkouan.saas.modules.sys.service.ISysParamService;
import com.yunkouan.util.PubUtil;
import com.yunkouan.util.SpringContextHolder;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.common.constant.CacheName;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.LoginUtil;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 库区VO<br/><br/>
 * @version 2017年3月11日 下午3:12:25<br/>
 * @author andy wang<br/>
 */
public class MetaAreaVO extends BaseVO {

	private static final long serialVersionUID = 6023457494532134420L;
	
	/**
	 * 构造方法
	 * @version 2017年3月11日 下午4:36:07<br/>
	 * @author andy wang<br/>
	 */
	public MetaAreaVO() {
		this(new MetaArea());
	}

	/**
	 * 构造方法
	 * @param area 库区
	 * @version 2017年3月11日 下午4:36:21<br/>
	 * @author andy wang<br/>
	 */
	public MetaAreaVO(MetaArea area) {
		super();
		this.area = area;
	}


	/**
	 * 库区对象
	 * @version 2017年3月11日下午3:13:22<br/>
	 * @author andy wang<br/>
	 */
	@Valid
	private MetaArea area;

	/**skuTypeName:货品类型中文名称**/
	private String skuTypeName;

	/**
	 * 仓库id集合
	 * @version 2017年3月11日下午3:20:16<br/>
	 * @author andy wang<br/>
	 */
	private List<String> listWrhId;
	
	/**
	 * 库区状态中文描述
	 * @version 2017年3月11日下午5:15:06<br/>
	 * @author andy wang<br/>
	 */
	private String areaStatusComment;
	
	/**
	 * 库区类型中文描述
	 * @version 2017年3月11日下午5:15:18<br/>
	 * @author andy wang<br/>
	 */
	private String areaTypeComment;
	
	/**
	 * 仓库名
	 * @version 2017年3月11日下午5:21:54<br/>
	 * @author andy wang<br/>
	 */
	private String warehouseComment;
	
	/**
	 * 模糊查询库区名称
	 * @version 2017年3月30日上午10:02:41<br/>
	 * @author andy wang<br/>
	 */
	private String likeAreaName;
	
	
	/**
	 * 模糊查询仓库名
	 * @version 2017年4月3日下午9:57:35<br/>
	 * @author andy wang<br/>
	 */
	private String likeWarehouseComment;
	
	/**
	 * 模糊查询仓库代码
	 * @version 2017年4月3日下午10:12:22<br/>
	 * @author andy wang<br/>
	 */
	private String likeAreaNo;
	
	/**
	 * 查询条件 - 不等于的库区id
	 * @version 2017年6月21日上午9:30:04<br/>
	 * @author andy wang<br/>
	 */
	private List<String> listNotId;

	/**listTypes:查询条件-限定库区类型**/
	private List<Integer> listTypes;

	/**
	 * 拥有库位
	 * @version 2017年6月21日上午9:30:52<br/>
	 * @author andy wang<br/>
	 */
	/**
	 * 
	 * @version 2017年6月21日 上午9:31:05<br/>
	 * @author andy wang<br/>
	 */
	private Boolean hasLocation;
	
	/* getset *********************************************/
	
	/**
	 * 属性 area getter方法
	 * @return 属性area
	 * @author andy wang<br/>
	 */
	public MetaArea getArea() {
		return area;
	}

	/**
	 * 属性 area setter方法
	 * @param area 设置属性area的值
	 * @author andy wang<br/>
	 */
	public void setArea(MetaArea area) {
		this.area = area;
	}
	
	/**
	 * 属性 hasLocation getter方法
	 * @return 属性hasLocation
	 * @author andy wang<br/>
	 */
	public Boolean getHasLocation() {
		if ( hasLocation == null ) {
			return false;
		}
		return hasLocation;
	}

	/**
	 * 属性 hasLocation setter方法
	 * @param hasLocation 设置属性hasLocation的值
	 * @author andy wang<br/>
	 */
	public void setHasLocation(Boolean hasLocation) {
		this.hasLocation = hasLocation;
	}

	/**
	 * 属性 likeAreaNo getter方法
	 * @return 属性likeAreaNo
	 * @author andy wang<br/>
	 */
	public String getLikeAreaNo() {
		return likeAreaNo;
	}

	/**
	 * 属性 listNotId getter方法
	 * @return 属性listNotId
	 * @author andy wang<br/>
	 */
	public List<String> getListNotId() {
		return listNotId;
	}

	/**
	 * 属性 listNotId setter方法
	 * @param listNotId 设置属性listNotId的值
	 * @author andy wang<br/>
	 */
	public void setListNotId(List<String> listNotId) {
		this.listNotId = listNotId;
	}

	/**
	 * 属性 likeAreaNo setter方法
	 * @param likeAreaNo 设置属性likeAreaNo的值
	 * @author andy wang<br/>
	 */
	public void setLikeAreaNo(String likeAreaNo) {
		this.likeAreaNo = likeAreaNo;
	}

	/**
	 * 属性 likeWarehouseComment getter方法
	 * @return 属性likeWarehouseComment
	 * @author andy wang<br/>
	 */
	public String getLikeWarehouseComment() {
		return likeWarehouseComment;
	}

	/**
	 * 属性 likeWarehouseComment setter方法
	 * @param likeWarehouseComment 设置属性likeWarehouseComment的值
	 * @author andy wang<br/>
	 */
	public void setLikeWarehouseComment(String likeWarehouseComment) {
		this.likeWarehouseComment = likeWarehouseComment;
	}

	/**
	 * 属性 likeAreaName getter方法
	 * @return 属性likeAreaName
	 * @author andy wang<br/>
	 */
	public String getLikeAreaName() {
		return likeAreaName;
	}

	/**
	 * 属性 likeAreaName setter方法
	 * @param likeAreaName 设置属性likeAreaName的值
	 * @author andy wang<br/>
	 */
	public void setLikeAreaName(String likeAreaName) {
		this.likeAreaName = likeAreaName;
	}

	/**
	 * 属性 warehouseComment getter方法
	 * @return 属性warehouseComment
	 * @author andy wang<br/>
	 */
	public String getWarehouseComment() {
		return warehouseComment;
	}

	/**
	 * 属性 warehouseComment setter方法
	 * @param warehouseComment 设置属性warehouseComment的值
	 * @author andy wang<br/>
	 */
	public void setWarehouseComment(String warehouseComment) {
		this.warehouseComment = warehouseComment;
	}

	/**
	 * 属性 areaStatusComment getter方法
	 * @return 属性areaStatusComment
	 * @author andy wang<br/>
	 */
	public String getAreaStatusComment() {
		return areaStatusComment;
	}

	/**
	 * 属性 areaStatusComment setter方法
	 * @param areaStatusComment 设置属性areaStatusComment的值
	 * @author andy wang<br/>
	 */
	public void setAreaStatusComment(String areaStatusComment) {
		this.areaStatusComment = areaStatusComment;
	}

	/**
	 * 属性 areaTypeComment getter方法
	 * @return 属性areaTypeComment
	 * @author andy wang<br/>
	 */
	public String getAreaTypeComment() {
		return areaTypeComment;
	}

	/**
	 * 属性 areaTypeComment setter方法
	 * @param areaTypeComment 设置属性areaTypeComment的值
	 * @author andy wang<br/>
	 */
	public void setAreaTypeComment(String areaTypeComment) {
		this.areaTypeComment = areaTypeComment;
	}

	public List<Integer> getListTypes() {
		return listTypes;
	}

	public void setListTypes(List<Integer> listTypes) {
		this.listTypes = listTypes;
	}

	/**
	 * 属性 listWrhId getter方法
	 * @return 属性listWrhId
	 * @author andy wang<br/>
	 */
	public List<String> getListWrhId() {
		return listWrhId;
	}

	/**
	 * 属性 listWrhId setter方法
	 * @param listWrhId 设置属性listWrhId的值
	 * @author andy wang<br/>
	 */
	public void setListWrhId(List<String> listWrhId) {
		this.listWrhId = listWrhId;
	}
	
	/* method *********************************************/
	
	public String getSkuTypeName() {
		return skuTypeName;
	}

	public void setSkuTypeName(String skuTypeName) {
		this.skuTypeName = skuTypeName;
	}

	/**
	 * 查询缓存参数信息
	 * @return
	 * @version 2017年3月11日 下午5:17:11<br/>
	 * @author andy wang<br/>
	 */
	public MetaAreaVO searchCache () {
		if ( this.area == null ) {
			return this;
		}
		ISysParamService paramService = SpringContextHolder.getBean(ISysParamService.class);
		if ( this.area.getAreaStatus() != null ) {
			this.areaStatusComment = paramService.getValue(CacheName.AREA_STATUS, this.area.getAreaStatus());
		}
		if ( this.area.getAreaType() != null) {
			this.areaTypeComment = paramService.getValue(CacheName.AREA_TYPE, this.area.getAreaType());
		}
		return this;
	}
	
	
	@Override
	public Example getExample () {
		Example example = new Example(MetaArea.class);
		Criteria criteria = example.createCriteria();
		if( !PubUtil.isEmpty(listTypes)) {
			criteria.andIn("areaType", listTypes);
		}
		if ( !PubUtil.isEmpty(this.getListNotId()) ) {
			criteria.andNotIn("areaId", this.getListNotId());
		}
		if ( !PubUtil.isEmpty(this.listWrhId) ) {
			criteria.andIn("warehouseId", this.listWrhId);
		}
		if ( !StringUtil.isTrimEmpty(this.getLikeAreaNo()) ) {
			criteria.andLike("areaNo", StringUtil.likeEscapeH(this.getLikeAreaNo()));
		}
		if ( !StringUtil.isTrimEmpty(this.getLikeAreaName()) ) {
			criteria.andLike("areaName", StringUtil.likeEscapeH(this.getLikeAreaName()));
		}
		// 以上为非实体属性
		if(this.area == null) return example;
		// 以下为实体属性
		if ( !StringUtil.isTrimEmpty(this.area.getAreaId()) ) {
			criteria.andEqualTo("areaId", this.area.getAreaId());
		}
		if ( !StringUtil.isTrimEmpty(this.area.getAreaNo()) ) {
			criteria.andEqualTo("areaNo", this.area.getAreaNo());
		}
		if ( !StringUtil.isTrimEmpty(this.area.getAreaName()) ) {
			criteria.andEqualTo("areaName", this.area.getAreaName());
		}
		if ( this.area.getAreaStatus() != null ) {
			criteria.andEqualTo("areaStatus", this.area.getAreaStatus());
		}
		if ( this.area.getAreaType() != null ) {
			criteria.andEqualTo("areaType", this.area.getAreaType());
		}
		if ( StringUtils.isNoneBlank(this.area.getSkuTypeId())) {
//			criteria.andEqualTo("skuTypeId", this.area.getSkuTypeId());
			criteria.andCondition(String.format("( sku_type_id = '%s' or sku_type_id is null or sku_type_id = '') ", this.area.getSkuTypeId()));
		}
		if ( !StringUtil.isTrimEmpty(this.area.getOrgId()) ) {
			criteria.andEqualTo("orgId", this.area.getOrgId());
		}
		if ( !StringUtil.isTrimEmpty(this.area.getWarehouseId()) ) {
			criteria.andEqualTo("warehouseId", this.area.getWarehouseId());
		}
		return example;
	}
	
	/**
	 * 添加不等于id条件
	 * @param notId
	 * @version 2017年6月16日 上午11:54:21<br/>
	 * @author andy wang<br/>
	 */
	public void addNotId ( String notId ) {
		if ( StringUtil.isTrimEmpty(notId) ) {
			return;
		}
		if ( PubUtil.isEmpty(this.listNotId ) ) {
			this.listNotId = new ArrayList<String>();
		}
		this.listNotId.add(notId);
	}
	

	/**
	 * 设置当前用户信息
	 * @version 2017年3月6日 下午4:20:01<br/>
	 * @author andy wang<br/>
	 */
	public void setLoginUser() {
		if ( this.area == null ) {
			return ;
		}
		Principal loginUser = LoginUtil.getLoginUser();
		this.area.setOrgId(loginUser.getOrgId());
		if ( StringUtil.isTrimEmpty(this.area.getWarehouseId()) ) {
			this.area.setWarehouseId(LoginUtil.getWareHouseId());
		}
	}
	
}
