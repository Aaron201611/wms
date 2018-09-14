/**
 * Created on 2017年2月16日
 * InvRejectsDestoryDetailVO.java V1.0
 *
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2016 =======================================================
 */
package com.yunkouan.wms.modules.inv.vo;

import org.apache.commons.lang3.StringUtils;

import com.yunkouan.base.BaseVO;
import com.yunkouan.saas.modules.sys.entity.MetaLocation;
import com.yunkouan.wms.modules.inv.entity.InvRejectsDestoryDetail;
import com.yunkouan.wms.modules.inv.entity.InvStock;
import com.yunkouan.wms.modules.meta.entity.MetaSku;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 不良品销毁单VO对象<br/><br/>
 * @Description 
 * @version 2017年2月16日 上午9:52:01<br/>
 * @author 王通<br/>
 */
public class InvRejectsDestoryDetailVO extends BaseVO {

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
	public InvRejectsDestoryDetailVO(){this.invRejectsDestoryDetail = new InvRejectsDestoryDetail();}
	
	/**
	 * 
	 * 构造方法
	 * @param shift
	 * @version 2017年2月16日 上午10:07:21<br/>
	 * @author 王通<br/>
	 */
	public InvRejectsDestoryDetailVO(InvRejectsDestoryDetail invRejectsDestoryDetail){
		this.invRejectsDestoryDetail = invRejectsDestoryDetail;
	}

	/**
	 * 不良品销毁单详情对象
	 * @version 2017年2月16日 上午10:07:48<br/>
	 * @author 王通<br/>
	 */
	private InvRejectsDestoryDetail invRejectsDestoryDetail;
	
	/**
	 * 库位对象
	 * @version 2017年2月16日 上午11:39:36<br/>
	 * @author 王通<br/>
	 */
	private MetaLocation location;
	
	/**
	 * 货品对象状态
	 * @version 2017年2月16日 上午10:09:12<br/>
	 * @author 王通<br/>
	 */
	private MetaSku sku;
	/**
	 * 库存对象
	 * @version 2017年2月16日 上午10:09:12<br/>
	 * @author 王通<br/>
	 */
	private InvStock stock;
	/**
	 * 货主
	 * @version 2017年2月16日 上午10:09:12<br/>
	 * @author 王通<br/>
	 */
	private String owner;
	/**
	 * 库区名称
	 * @version 2017年2月16日 上午10:09:12<br/>
	 * @author 王通<br/>
	 */
	private String areaName;

	public InvRejectsDestoryDetail getInvRejectsDestoryDetail() {
		return invRejectsDestoryDetail;
	}
	

	public void setInvRejectsDestoryDetail(InvRejectsDestoryDetail invRejectsDestoryDetail) {
		this.invRejectsDestoryDetail = invRejectsDestoryDetail;
	}

	public InvStock getStock() {
		return stock;
	}
	

	public void setStock(InvStock stock) {
		this.stock = stock;
	}
	
	public Example getExample() {
		
		Example example = new Example(InvRejectsDestoryDetail.class);
		example.setOrderByClause("inv_rejects_destory_detail_id2 asc");
		Criteria c = example.createCriteria();
		if (StringUtils.isNoneBlank(this.invRejectsDestoryDetail.getRejectsDestoryId())) {
			c.andEqualTo("rejectsDestoryId", this.invRejectsDestoryDetail.getRejectsDestoryId());
		}
		return example;
	}

	public MetaLocation getLocation() {
		return location;
	}

	public void setLocation(MetaLocation location) {
		this.location = location;
	}

	public MetaSku getSku() {
		return sku;
	}

	public void setSku(MetaSku sku) {
		this.sku = sku;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	
	
}
