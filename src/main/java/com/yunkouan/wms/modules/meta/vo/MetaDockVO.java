/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * @version 2017年3月13日上午11:26:07<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.meta.vo;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import com.yunkouan.base.BaseVO;
import com.yunkouan.saas.modules.sys.service.ISysParamService;
import com.yunkouan.util.PubUtil;
import com.yunkouan.util.SpringContextHolder;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.common.constant.CacheName;
import com.yunkouan.wms.modules.meta.entity.MetaDock;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 月台VO<br/><br/>
 * @version 2017年3月13日上午11:26:07<br/>
 * @author andy wang<br/>
 */
public class MetaDockVO extends BaseVO {

	private static final long serialVersionUID = 6023457494532134420L;

	/**
	 * 构造方法
	 * @version 2017年3月13日上午11:26:07<br/>
	 * @author andy wang<br/>
	 */
	public MetaDockVO() {
		this(new MetaDock());
	}

	/**
	 * 构造方法
	 * @param dock 月台
	 * @version 2017年3月13日上午11:26:07<br/>
	 * @author andy wang<br/>
	 */
	public MetaDockVO(MetaDock dock) {
		super();
		this.dock = dock;
	}

	/**
	 * 月台对象
	 * @version 2017年3月13日上午11:26:07<br/>
	 * @author andy wang<br/>
	 */
	@Valid
	private MetaDock dock;

	/**
	 * 月台状态中文描述
	 * @version 2017年3月13日上午11:26:07<br/>
	 * @author andy wang<br/>
	 */
	private String dockStatusComment;

	/**
	 * 库区名
	 * @version 2017年3月13日上午11:45:49<br/>
	 * @author andy wang<br/>
	 */
	private String areaComment;
	
	/**
	 * 是否收货库位中文描述
	 * @version 2017年3月13日上午11:46:40<br/>
	 * @author andy wang<br/>
	 */
	private String isReceiveComment;
	
	/**
	 * 是否发货库位中文描述
	 * @version 2017年3月13日上午11:46:52<br/>
	 * @author andy wang<br/>
	 */
	private String isSendComment;
	
	/**
	 * 模糊查询月台编号
	 * @version 2017年4月4日下午7:25:13<br/>
	 * @author andy wang<br/>
	 */
	private String likeDockNo; 
	
	/**
	 * 模糊查询月台名称
	 * @version 2017年4月4日下午7:25:26<br/>
	 * @author andy wang<br/>
	 */
	private String likeDockName; 
	
	/**
	 * 模糊查询库区名称
	 * @version 2017年4月4日下午7:25:29<br/>
	 * @author andy wang<br/>
	 */
	private String likeAreaName;
	
	
	/**
	 * 库区id集合
	 * @version 2017年4月4日下午7:26:27<br/>
	 * @author andy wang<br/>
	 */
	private List<String> listAreaId;
	
	private List<String> listNotId;
	
	/* getset *********************************************/

	/**
	 * 属性 likeDockNo getter方法
	 * @return 属性likeDockNo
	 * @author andy wang<br/>
	 */
	public String getLikeDockNo() {
		return likeDockNo;
	}

	/**
	 * 属性 likeDockNo setter方法
	 * @param likeDockNo 设置属性likeDockNo的值
	 * @author andy wang<br/>
	 */
	public void setLikeDockNo(String likeDockNo) {
		this.likeDockNo = likeDockNo;
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
	 * 属性 likeDockName getter方法
	 * @return 属性likeDockName
	 * @author andy wang<br/>
	 */
	public String getLikeDockName() {
		return likeDockName;
	}

	/**
	 * 属性 likeDockName setter方法
	 * @param likeDockName 设置属性likeDockName的值
	 * @author andy wang<br/>
	 */
	public void setLikeDockName(String likeDockName) {
		this.likeDockName = likeDockName;
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
	 * 属性 dock getter方法
	 * @return 属性dock
	 * @author andy wang<br/>
	 */
	public MetaDock getDock() {
		return dock;
	}

	/**
	 * 属性 dock setter方法
	 * @param dock 设置属性dock的值
	 * @author andy wang<br/>
	 */
	public void setDock(MetaDock dock) {
		this.dock = dock;
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
	 * 属性 isReceiveComment getter方法
	 * @return 属性isReceiveComment
	 * @author andy wang<br/>
	 */
	public String getIsReceiveComment() {
		return isReceiveComment;
	}

	/**
	 * 属性 isReceiveComment setter方法
	 * @param isReceiveComment 设置属性isReceiveComment的值
	 * @author andy wang<br/>
	 */
	public void setIsReceiveComment(String isReceiveComment) {
		this.isReceiveComment = isReceiveComment;
	}

	/**
	 * 属性 isSendComment getter方法
	 * @return 属性isSendComment
	 * @author andy wang<br/>
	 */
	public String getIsSendComment() {
		return isSendComment;
	}

	/**
	 * 属性 isSendComment setter方法
	 * @param isSendComment 设置属性isSendComment的值
	 * @author andy wang<br/>
	 */
	public void setIsSendComment(String isSendComment) {
		this.isSendComment = isSendComment;
	}

	/**
	 * 属性 dockStatusComment getter方法
	 * @return 属性dockStatusComment
	 * @author andy wang<br/>
	 */
	public String getDockStatusComment() {
		return dockStatusComment;
	}

	/**
	 * 属性 dockStatusComment setter方法
	 * @param dockStatusComment 设置属性dockStatusComment的值
	 * @author andy wang<br/>
	 */
	public void setDockStatusComment(String dockStatusComment) {
		this.dockStatusComment = dockStatusComment;
	}

	/* method *********************************************/

	/**
	 * 查询缓存参数信息
	 * @return
	 * @version 2017年3月13日上午11:26:07<br/>
	 * @author andy wang<br/>
	 */
	public MetaDockVO searchCache() {
		if (this.dock == null) {
			return this;
		}
		ISysParamService paramService = SpringContextHolder.getBean(ISysParamService.class);
		if (this.dock.getDockStatus() != null) {
			this.dockStatusComment = paramService.getValue(CacheName.DOCK_STATUS, this.dock.getDockStatus());
		}
		if (this.dock.getIsIn() != null) {
			this.isReceiveComment = paramService.getValue(CacheName.DOCK_ISREC, this.dock.getIsIn());
		}
		if (this.dock.getIsOut() != null) {
			this.isSendComment = paramService.getValue(CacheName.DOCK_ISSEND, this.dock.getIsOut());
		}
		return this;
	}

	@Override
	public Example getExample() {
		if (this.dock == null) {
			return null;
		}
		Example example = new Example(MetaDock.class);
		Criteria criteria = example.createCriteria();
		if (!StringUtil.isTrimEmpty(this.dock.getDockId())) {
			criteria.andEqualTo("dockId", this.dock.getDockId());
		}
		if (!StringUtil.isTrimEmpty(this.dock.getDockNo())) {
			criteria.andEqualTo("dockNo", this.dock.getDockNo());
		}
		if (!StringUtil.isTrimEmpty(this.dock.getDockName())) {
			criteria.andEqualTo("dockName", this.dock.getDockName());
		}
		if (!StringUtil.isTrimEmpty(this.getLikeDockName())) {
			criteria.andLike("dockName", StringUtil.likeEscapeH(this.getLikeDockName()));
		}
		if (!StringUtil.isTrimEmpty(this.getLikeDockNo())) {
			criteria.andLike("dockNo", StringUtil.likeEscapeH(this.getLikeDockNo()));
		}
		if (!PubUtil.isEmpty(this.getListAreaId())) {
			criteria.andIn("areaId", this.getListAreaId());
		}
		if ( this.dock.getDockStatus() != null ) {
			criteria.andEqualTo("dockStatus", this.dock.getDockStatus());
		}
		if ( !PubUtil.isEmpty(this.getListNotId()) ) {
			criteria.andNotIn("dockId", this.getListNotId());
		}
		if (!StringUtil.isTrimEmpty(this.dock.getAreaId())) {
			criteria.andEqualTo("areaId", this.dock.getAreaId());
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

}
