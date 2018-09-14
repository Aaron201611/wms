/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * @version 2017年2月23日 下午2:17:57<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.meta.vo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import com.yunkouan.base.BaseVO;
import com.yunkouan.saas.modules.sys.entity.MetaLocation;
import com.yunkouan.saas.modules.sys.service.ISysParamService;
import com.yunkouan.util.PubUtil;
import com.yunkouan.util.SpringContextHolder;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.common.constant.CacheName;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.meta.entity.MetaMerchant;
import com.yunkouan.wms.modules.rec.entity.RecPutawayDetail;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 库位VO<br/><br/>
 * @version 2017年2月23日 下午2:17:57<br/>
 * @author andy wang<br/>
 */
public class MetaLocationVO extends BaseVO {
	private static final long serialVersionUID = 6076022690790953289L;

	/**
	 * 构造方法
	 * @version 2017年2月23日 下午2:51:05<br/>
	 * @author andy wang<br/>
	 */
	public MetaLocationVO() {
		this(new MetaLocation());
	}
	/**
	 * 构造方法
	 * @param location
	 * @version 2017年2月23日 下午2:51:02<br/>
	 * @author andy wang<br/>
	 */
	public MetaLocationVO(MetaLocation location) {
		super();
		this.location = location;
	}

	/**
	 * 库位
	 * @version 2017年2月23日下午2:27:55<br/>
	 * @author andy wang<br/>
	 */
	@Valid
	private MetaLocation location;
	/**
	 * 库容量
	 * @version 2017年2月26日下午5:03:17<br/>
	 * @author andy wang<br/>
	 */
	private BigDecimal capacity;
	
	/**
	 * 上架明细
	 * @version 2017年2月27日下午2:37:03<br/>
	 * @author andy wang<br/>
	 */
	private RecPutawayDetail putawayDetail;
	
	/**
	 * 库位id集合
	 * @version 2017年3月6日上午11:47:23<br/>
	 * @author andy wang<br/>
	 */
	private List<String> listLocationId;
	
	private List<String> noListLocationId;

	/*
	 * 按库容排序（最大库容-已使用库容-预分配库容）
	 * @version 2017年3月6日下午2:07:39<br/>
	 * @author andy wang<br/>
	 */
	private Boolean orderByCapacityDesc = false;
	
	/**
	 * 与某一库位之间的距离
	 * @version 2017年3月7日下午8:14:04<br/>
	 * @author andy wang<br/>
	 */
	private Double distance;
	
	
	/**
	 * 库位状态中文描述
	 * @version 2017年3月12日下午9:50:00<br/>
	 * @author andy wang<br/>
	 */
	private String locationStatusComment;
	
	/**
	 * 库区中文描述
	 * @version 2017年3月12日下午9:50:53<br/>
	 * @author andy wang<br/>
	 */
	private String areaComment;
	
	/**
	 * 是否冻结
	 * @version 2017年3月12日下午9:51:34<br/>
	 * @author andy wang<br/>
	 */
	private String blockComment;
	
	/**
	 * 动碰次数
	 * @version 2017年3月12日下午9:52:56<br/>
	 * @author andy wang<br/>
	 */
	private Integer touchTimes;
	
	/**
	 * 货主id集合
	 * @version 2017年3月14日下午4:45:40<br/>
	 * @author andy wang<br/>
	 */
	private List<String> listOwnerId;
	
	
	/**
	 * 模糊查询库区名称
	 * @version 2017年3月30日上午9:44:57<br/>
	 * @author andy wang<br/>
	 */
	private String likeAreaName;

	/**
	 * 模糊查询库位名称
	 * @version 2017年3月30日上午9:44:57<br/>
	 * @author andy wang<br/>
	 */
	private String likeLocName;

	/**
	 * 模糊查询库位代码
	 * @version 2017年3月30日上午9:44:57<br/>
	 * @author andy wang<br/>
	 */
	private String likeLocNo;
	private String skuId;
	private String skuNoLike;
	private String skuTypeId;
	
	private Boolean batchNoIsNull;
	private Boolean skuNotNull;
	private Boolean skuIsNull;
	private Boolean isEmpty;
	/**
	 * 库区id集合
	 * @version 2017年3月30日上午10:55:21<br/>
	 * @author andy wang<br/>
	 */
	private List<String> listAreaId;

	/**listTypes:查询条件-限定库区类型**/
	private List<Integer> listTypes;
	private List<String> listSkuId;

	/**
	 * 包装中文描述
	 * @version 2017年4月4日下午3:25:05<br/>
	 * @author andy wang<br/>
	 */
	private String packComment;
	
	private MetaMerchant owner;
	
	private SkuVo skuVo;
	/**
	 * 货主中文描述
	 * @version 2017年4月4日下午4:06:39<br/>
	 * @author andy wang<br/>
	 */
	private String ownerComment;
	
	/**
	 * 库位规格中文描述
	 * @version 2017年4月4日下午4:57:41<br/>
	 * @author andy wang<br/>
	 */
	private String specComment;
	
	/**
	 * 是否查询没有货主的库位
	 * @version 2017年4月7日下午5:45:21<br/>
	 * @author andy wang<br/>
	 */
	private Integer nullOwner;
	
	/**
	 * 根据库位id2进行排序
	 * true - 进行排序
	 * false - 不进行排序
	 * @version 2017年4月24日下午1:57:49<br/>
	 * @author andy wang<br/>
	 */
	private Boolean orderByLocationId;
	private Boolean orderByLocationNoAsc;

	/**
	 * 库区类型
	 * @version 2017年5月12日下午5:24:56<br/>
	 * @author andy wang<br/>
	 */
	private String areaTypeComment;
	
	private List<String> listNotId;
	
	/**
	 * 状态集合
	 * @version 2017年6月21日下午6:55:09<br/>
	 * @author andy wang<br/>
	 */
	private List<Integer> listStatus;
	
	/**
	 * 非类型集合
	 * @version 2017年6月21日下午6:55:09<br/>
	 * @author andy wang<br/>
	 */
	private List<Integer> listNotType;
	
	/* getset ************************************************/
	/**
	 * 属性 location getter方法
	 * @return 属性location
	 * @author andy wang<br/>
	 */
	public MetaLocation getLocation() {
		return location;
	}

	/**
	 * 属性 location setter方法
	 * @param location 设置属性location的值
	 * @author andy wang<br/>
	 */
	public void setLocation(MetaLocation location) {
		this.location = location;
	}
	
	/**
	 * 属性 listStatus getter方法
	 * @return 属性listStatus
	 * @author andy wang<br/>
	 */
	public List<Integer> getListStatus() {
		return listStatus;
	}
	/**
	 * 属性 listNotType getter方法
	 * @return 属性listNotType
	 * @author andy wang<br/>
	 */
	public List<Integer> getListNotType() {
		return listNotType;
	}
	/**
	 * 属性 listNotType setter方法
	 * @param listNotType 设置属性listNotType的值
	 * @author andy wang<br/>
	 */
	public void setListNotType(List<Integer> listNotType) {
		this.listNotType = listNotType;
	}
	/**
	 * 属性 listStatus setter方法
	 * @param listStatus 设置属性listStatus的值
	 * @author andy wang<br/>
	 */
	public void setListStatus(List<Integer> listStatus) {
		this.listStatus = listStatus;
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
	/**
	 * 属性 orderByLocationId getter方法
	 * @return 属性orderByLocationId
	 * @author andy wang<br/>
	 */
	public Boolean getOrderByLocationId() {
		if ( this.orderByLocationId == null ) {
			return false;
		}
		return orderByLocationId;
	}
	/**
	 * 属性 orderByLocationId setter方法
	 * @param orderByLocationId 设置属性orderByLocationId的值
	 * @author andy wang<br/>
	 */
	public void setOrderByLocationId(Boolean orderByLocationId) {
		this.orderByLocationId = orderByLocationId;
	}
	/**
	 * 属性 nullOwner getter方法
	 * @return 属性nullOwner
	 * @author andy wang<br/>
	 */
	public Integer getNullOwner() {
		return nullOwner;
	}
	/**
	 * 属性 nullOwner setter方法
	 * @param nullOwner 设置属性nullOwner的值
	 * @author andy wang<br/>
	 */
	public void setNullOwner(Integer nullOwner) {
		this.nullOwner = nullOwner;
	}
	/**
	 * 属性 specComment getter方法
	 * @return 属性specComment
	 * @author andy wang<br/>
	 */
	public String getSpecComment() {
		return specComment;
	}
	/**
	 * 属性 specComment setter方法
	 * @param specComment 设置属性specComment的值
	 * @author andy wang<br/>
	 */
	public void setSpecComment(String specComment) {
		this.specComment = specComment;
	}
	/**
	 * 属性 ownerComment getter方法
	 * @return 属性ownerComment
	 * @author andy wang<br/>
	 */
	public String getOwnerComment() {
		return ownerComment;
	}
	/**
	 * 属性 ownerComment setter方法
	 * @param ownerComment 设置属性ownerComment的值
	 * @author andy wang<br/>
	 */
	public void setOwnerComment(String ownerComment) {
		this.ownerComment = ownerComment;
	}
	/**
	 * 属性 packComment getter方法
	 * @return 属性packComment
	 * @author andy wang<br/>
	 */
	public String getPackComment() {
		return packComment;
	}
	/**
	 * 属性 packComment setter方法
	 * @param packComment 设置属性packComment的值
	 * @author andy wang<br/>
	 */
	public void setPackComment(String packComment) {
		this.packComment = packComment;
	}
	/**
	 * 属性 listAreaId getter方法
	 * @return 属性listAreaId
	 * @author andy wang<br/>
	 */
	public List<String> getListAreaId() {
		return listAreaId;
	}
	/**
	 * 属性 listAreaId setter方法
	 * @param listAreaId 设置属性listAreaId的值
	 * @author andy wang<br/>
	 */
	public void setListAreaId(List<String> listAreaId) {
		this.listAreaId = listAreaId;
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
	 * 属性 likeLocName getter方法
	 * @return 属性likeLocName
	 * @author andy wang<br/>
	 */
	public String getLikeLocName() {
		return likeLocName;
	}
	/**
	 * 属性 likeLocName setter方法
	 * @param likeLocName 设置属性likeLocName的值
	 * @author andy wang<br/>
	 */
	public void setLikeLocName(String likeLocName) {
		this.likeLocName = likeLocName;
	}
	/**
	 * 属性 likeLocNo getter方法
	 * @return 属性likeLocNo
	 * @author andy wang<br/>
	 */
	public String getLikeLocNo() {
		return likeLocNo;
	}
	/**
	 * 属性 likeLocNo setter方法
	 * @param likeLocNo 设置属性likeLocNo的值
	 * @author andy wang<br/>
	 */
	public void setLikeLocNo(String likeLocNo) {
		this.likeLocNo = likeLocNo;
	}
	/**
	 * 属性 areaComment getter方法
	 * @return 属性areaComment
	 * @author andy wang<br/>
	 */
	public String getAreaComment() {
		return areaComment;
	}
	/**
	 * 属性 areaComment setter方法
	 * @param areaComment 设置属性areaComment的值
	 * @author andy wang<br/>
	 */
	public void setAreaComment(String areaComment) {
		this.areaComment = areaComment;
	}
	/**
	 * 属性 blockComment getter方法
	 * @return 属性blockComment
	 * @author andy wang<br/>
	 */
	public String getBlockComment() {
		return blockComment;
	}
	/**
	 * 属性 blockComment setter方法
	 * @param blockComment 设置属性blockComment的值
	 * @author andy wang<br/>
	 */
	public void setBlockComment(String blockComment) {
		this.blockComment = blockComment;
	}
	/**
	 * 属性 touchTimes getter方法
	 * @return 属性touchTimes
	 * @author andy wang<br/>
	 */
	public Integer getTouchTimes() {
		return touchTimes;
	}
	/**
	 * 属性 touchTimes setter方法
	 * @param touchTimes 设置属性touchTimes的值
	 * @author andy wang<br/>
	 */
	public void setTouchTimes(Integer touchTimes) {
		this.touchTimes = touchTimes;
	}
	/**
	 * 属性 locationStatusComment getter方法
	 * @return 属性locationStatusComment
	 * @author andy wang<br/>
	 */
	public String getLocationStatusComment() {
		return locationStatusComment;
	}
	/**
	 * 属性 locationStatusComment setter方法
	 * @param locationStatusComment 设置属性locationStatusComment的值
	 * @author andy wang<br/>
	 */
	public void setLocationStatusComment(String locationStatusComment) {
		this.locationStatusComment = locationStatusComment;
	}
	/**
	 * 属性 distance getter方法
	 * @return 属性distance
	 * @author andy wang<br/>
	 */
	public Double getDistance() {
		return distance;
	}
	/**
	 * 属性 distance setter方法
	 * @param distance 设置属性distance的值
	 * @author andy wang<br/>
	 */
	public void setDistance(Double distance) {
		this.distance = distance;
	}
	/**
	 * 属性 orderByCapacityDesc getter方法
	 * @return 属性orderByCapacityDesc
	 * @author andy wang<br/>
	 */
	public Boolean getOrderByCapacityDesc() {
		return orderByCapacityDesc;
	}
	/**
	 * 属性 orderByCapacityDesc setter方法
	 * @param orderByCapacityDesc 设置属性orderByCapacityDesc的值
	 * @author andy wang<br/>
	 */
	public void setOrderByCapacityDesc(Boolean orderByCapacityDesc) {
		this.orderByCapacityDesc = orderByCapacityDesc;
	}
	/**
	 * 属性 listLocationId getter方法
	 * @return 属性listLocationId
	 * @author andy wang<br/>
	 */
	public List<String> getListLocationId() {
		return listLocationId;
	}
	/**
	 * 属性 listLocationId setter方法
	 * @param listLocationId 设置属性listLocationId的值
	 * @author andy wang<br/>
	 */
	public void setListLocationId(List<String> listLocationId) {
		this.listLocationId = listLocationId;
	}
	/**
	 * 属性 capacity getter方法
	 * @return 属性capacity
	 * @author andy wang<br/>
	 */
	public BigDecimal getCapacity() {
		if ( this.capacity == null ) {
			return BigDecimal.valueOf(0);
		}
		return capacity;
	}
	/**
	 * 属性 capacity setter方法
	 * @param capacity 设置属性capacity的值
	 * @author andy wang<br/>
	 */
	public void setCapacity(BigDecimal capacity) {
		this.capacity = capacity;
	}

	public List<Integer> getListTypes() {
		return listTypes;
	}
	public void setListTypes(List<Integer> listTypes) {
		this.listTypes = listTypes;
	}
	public void addListTypes(Integer listType) {
		if(this.listTypes == null) this.listTypes = new ArrayList<Integer>();
		this.listTypes.add(listType);
	}

	public Boolean getOrderByLocationNoAsc() {
		if(orderByLocationNoAsc == null) return false;
		return orderByLocationNoAsc;
	}
	public void setOrderByLocationNoAsc(Boolean orderByLocationNoAsc) {
		this.orderByLocationNoAsc = orderByLocationNoAsc;
	}
	/**
	 * 属性 putawayDetail getter方法
	 * @return 属性putawayDetail
	 * @author andy wang<br/>
	 */
	public RecPutawayDetail getPutawayDetail() {
		return putawayDetail;
	}
	/**
	 * 属性 putawayDetail setter方法
	 * @param putawayDetail 设置属性putawayDetail的值
	 * @author andy wang<br/>
	 */
	public void setPutawayDetail(RecPutawayDetail putawayDetail) {
		this.putawayDetail = putawayDetail;
	}
	
	
	
	/* method ************************************************/
	
	public MetaMerchant getOwner() {
		return owner;
	}
	public void setOwner(MetaMerchant owner) {
		this.owner = owner;
	}
	/**
	 * 
	 * @version 2017年3月12日 下午9:20:03<br/>
	 * @author andy wang<br/>
	 */
	@Override
	public Example getExample() {
		Example example = new Example(MetaLocation.class);
		Criteria criteria = example.createCriteria();
		// 设置排序
		if ( this.getOrderByCapacityDesc() ) {
			example.setOrderByClause("max_capacity-used_capacity-preused_capacity DESC");
		} else if ( this.getOrderByLocationId() ) {
			example.setOrderByClause("location_id2 ASC");
		} else if( this.getOrderByLocationNoAsc()) {
			example.setOrderByClause("location_no asc ");
		} else {
			example.setOrderByClause("location_no desc ");
		}
		if ( !PubUtil.isEmpty(this.listLocationId)) {
			criteria.andIn("locationId",this.listLocationId);
		}
		if ( !PubUtil.isEmpty(this.listLocationId)) {
			criteria.andIn("locationId",this.listLocationId);
		}
		if ( !PubUtil.isEmpty(this.noListLocationId)) {
			criteria.andNotIn("locationId",this.noListLocationId);
		}
		if ( !PubUtil.isEmpty(this.getListOwnerId()) ) {
			criteria.andIn("owner",this.getListOwnerId());
		}
		if ( !PubUtil.isEmpty(this.getListSkuId()) ) {
			criteria.andIn("skuId",this.getListSkuId());
		}
		if ( !PubUtil.isEmpty(this.getListAreaId()) ) {
			criteria.andIn("areaId",this.getListAreaId());
		}
		if ( !StringUtil.isTrimEmpty(this.getLikeLocName()) ) {
			criteria.andLike("locationName",StringUtil.likeEscapeH(this.getLikeLocName()));
		}
		if ( !StringUtil.isTrimEmpty(this.getLikeLocNo()) ) {
			criteria.andLike("locationNo",StringUtil.likeEscapeH(this.getLikeLocNo()));
		}
		if ( !PubUtil.isEmpty(this.getListNotId()) ) {
			criteria.andNotIn("locationId", this.getListNotId());
		}
		if ( !PubUtil.isEmpty(this.getListStatus())) {
			criteria.andIn("status", this.getListStatus());
		}
		if ( !PubUtil.isEmpty(this.getListNotType())) {
			criteria.andNotIn("locationType", this.getListNotType());
		}
		if ( !PubUtil.isEmpty(this.getListTypes())) {
			criteria.andIn("locationType", this.getListTypes());
		}
		if (this.batchNoIsNull != null && batchNoIsNull) {
			criteria.andIsNull("batchNo");
		}
		if (this.skuNotNull != null && skuNotNull) {
			criteria.andIsNotNull("skuId");
		}
		if (this.skuIsNull != null && skuIsNull) {
			criteria.andIsNull("skuId");
		}
		if (this.isEmpty != null && !isEmpty) {
			criteria.andGreaterThan("usedCapacity", 0d);
		}
		
		// 以上属性为非实体类属性
		if(this.location == null) return example;
		// 以下为实体类属性
		if ( !StringUtil.isTrimEmpty(this.location.getLocationId()) ) {
			criteria.andEqualTo("locationId",this.location.getLocationId());
		}
		if ( !StringUtil.isTrimEmpty(this.location.getLocationName()) ) {
			criteria.andEqualTo("locationName",this.location.getLocationName());
		}
		if ( !StringUtil.isTrimEmpty(this.location.getLocationNo()) ) {
			criteria.andEqualTo("locationNo",this.location.getLocationNo());
		}
		if ( !StringUtil.isTrimEmpty(this.location.getPackId()) ) {
			criteria.andEqualTo("packId",this.location.getPackId());
		}
		if ( !StringUtil.isTrimEmpty(this.location.getAreaId()) ) {
			criteria.andEqualTo("areaId",this.location.getAreaId());
		}
		if ( !StringUtil.isTrimEmpty(this.location.getSpecId()) ) {
			criteria.andEqualTo("specId",this.location.getSpecId());
		}
		if ( !StringUtil.isTrimEmpty(this.location.getSkuId()) ) {
			criteria.andEqualTo("skuId",this.location.getSkuId());
		}
		if ( !StringUtil.isTrimEmpty(this.location.getBatchNo()) ) {
			criteria.andEqualTo("batchNo",this.location.getBatchNo());
		}
		if ( this.location.getUsedCapacity() != null ) {
			criteria.andEqualTo("usedCapacity",this.location.getUsedCapacity());
		}
		if ( this.location.getPreusedCapacity() != null ) {
			criteria.andEqualTo("preusedCapacity",this.location.getPreusedCapacity());
		}
		if ( !StringUtil.isTrimEmpty(this.location.getOrgId()) ) {
			criteria.andEqualTo("orgId",this.location.getOrgId());
		}
		if ( !StringUtil.isTrimEmpty(this.location.getWarehouseId()) ) {
			criteria.andEqualTo("warehouseId",this.location.getWarehouseId());
		}
		if ( this.location.getIsDefault() != null) {
			criteria.andEqualTo("isDefault",this.location.getIsDefault());
		}
		if ( this.location.getLocationType() != null) {
			criteria.andEqualTo("locationType",this.location.getLocationType());
		}
		if ( this.location.getLocationStatus() != null) {
			criteria.andEqualTo("locationStatus",this.location.getLocationStatus());
		}
		if ( this.location.getIsBlock() != null ) {
			criteria.andEqualTo("isBlock",this.location.getIsBlock());
		}
		if ( !StringUtil.isTrimEmpty(this.location.getOwner()) ) {
//			if ( this.nullOwner != null && this.nullOwner == 1 ) {
				criteria.andCondition(String.format("( owner = '%s' or owner IS NULL or owner = '') " , this.location.getOwner()));
//				criteria.or
//				Criteria or = example.or();
//				or.andEqualTo("owner",this.location.getOwner());
//				or.andIsNull("owner");
//			} else {
//				criteria.andEqualTo("owner",this.location.getOwner());
//			}
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
	 * 属性 listOwnerId getter方法
	 * @return 属性listOwnerId
	 * @author andy wang<br/>
	 */
	public List<String> getListOwnerId() {
		return listOwnerId;
	}
	/**
	 * 属性 listOwnerId setter方法
	 * @param listOwnerId 设置属性listOwnerId的值
	 * @author andy wang<br/>
	 */
	public void setListOwnerId(List<String> listOwnerId) {
		this.listOwnerId = listOwnerId;
	}
	/**
	 * 查询缓存参数信息
	 * @return 当前的库位VO
	 * @version 2017年3月12日 下午9:48:37<br/>
	 * @author andy wang<br/>
	 */
	public MetaLocationVO searchCache () {
		if ( this.location == null ) {
			return this;
		}
		ISysParamService paramService = SpringContextHolder.getBean(ISysParamService.class);
		if ( this.location.getLocationStatus() != null ) {
			this.locationStatusComment = paramService.getValue(CacheName.LOCATION_STATUS, this.location.getLocationStatus());
		}
		if ( this.location.getIsBlock() != null ) {
			this.blockComment = paramService.getValue(CacheName.LOCATION_BLOCK, this.location.getIsBlock());
		}
		return this;
	}
	
	/**
	 * 设置登录信息
	 * @version 2017年3月20日 下午7:30:51<br/>
	 * @author andy wang<br/>
	 */
	public void loginInfo () {
		if ( this.location == null ) {
			this.location = new MetaLocation();
		}
		Principal loginUser = LoginUtil.getLoginUser();
		this.location.setOrgId(loginUser.getOrgId());
		this.location.setWarehouseId(LoginUtil.getWareHouseId());
	}
	public List<String> getNoListLocationId() {
		return noListLocationId;
	}
	public void setNoListLocationId(List<String> noListLocationId) {
		this.noListLocationId = noListLocationId;
	}
	public List<String> getListSkuId() {
		return listSkuId;
	}
	public void setListSkuId(List<String> listSkuId) {
		this.listSkuId = listSkuId;
	}
	public String getSkuId() {
		return skuId;
	}
	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}
	public Boolean getBatchNoIsNull() {
		return batchNoIsNull;
	}
	
	public void setBatchNoIsNull(Boolean batchNoIsNull) {
		this.batchNoIsNull = batchNoIsNull;
	}
	public SkuVo getSkuVo() {
		return skuVo;
	}
	public void setSkuVo(SkuVo skuVo) {
		this.skuVo = skuVo;
	}
	public String getSkuTypeId() {
		return skuTypeId;
	}
	public void setSkuTypeId(String skuTypeId) {
		this.skuTypeId = skuTypeId;
	}
	public Boolean getSkuNotNull() {
		return skuNotNull;
	}
	public void setSkuNotNull(Boolean skuNotNull) {
		this.skuNotNull = skuNotNull;
	}
	public String getSkuNoLike() {
		return skuNoLike;
	}
	public void setSkuNoLike(String skuNoLike) {
		this.skuNoLike = skuNoLike;
	}
	public Boolean getSkuIsNull() {
		return skuIsNull;
	}
	public void setSkuIsNull(Boolean skuIsNull) {
		this.skuIsNull = skuIsNull;
	}
	public Boolean getIsEmpty() {
		return isEmpty;
	}
	public void setIsEmpty(Boolean isEmpty) {
		this.isEmpty = isEmpty;
	}
	
	
}
