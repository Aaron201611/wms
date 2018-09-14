package com.yunkouan.wms.modules.meta.vo;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;

import com.yunkouan.base.BaseVO;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.meta.entity.MetaSku;
import com.yunkouan.wms.modules.meta.entity.MetaSkuBom;
import com.yunkouan.wms.modules.send.vo.DeliveryDetailVo;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 货品bom类
 * @author tphe06 2017年2月14日
 */
public class SkuBomVo extends BaseVO  {
	private static final long serialVersionUID = 8254229011084473157L;

	/**货品bom实体类*/
	@Valid
	private MetaSkuBom entity;
	/**@Fields 排序字段 */
	private String orderBy = "create_time desc";
	
	private SkuVo skuVo;

	public SkuBomVo() {this.entity = new MetaSkuBom();}
	public SkuBomVo(MetaSkuBom entity) {
		this.entity = entity;
		if(this.entity == null) return;
	}

	@Override
	public Example getExample() {
		Example example = new Example(MetaSkuBom.class);
		example.setOrderByClause(orderBy);
		Criteria c = example.createCriteria();
		// 以下为非实体属性
		Principal loginUser = LoginUtil.getLoginUser();
		if(loginUser != null) {
	    	c.andEqualTo("orgId", loginUser.getOrgId());
	    	c.andEqualTo("warehouseId", LoginUtil.getWareHouseId());
		}
		// 以下为实体属性
		if(entity == null) return example;
		if(StringUtils.isNoneBlank(entity.getSkuId())) {
			c.andEqualTo("skuId", entity.getSkuId());
		}
		if(StringUtils.isNoneBlank(entity.getBomId())) {
			c.andEqualTo("bomId", entity.getBomId());
		}
		return example;
	}
	
	public MetaSkuBom getEntity() {
		return entity;
	}

	public SkuBomVo orderByUpdateTimeDesc() {
		this.orderBy = "update_time desc ";
		return this;
	}

	public SkuBomVo setEntity(MetaSkuBom entity) {
		this.entity = entity;
		return this;
	}

	public SkuVo getSkuVo() {
		return skuVo;
	}
	public void setSkuVo(SkuVo skuVo) {
		this.skuVo = skuVo;
	}
	

	@Override
	public SkuBomVo clone() {
		SkuBomVo vo = null;
		try {
			vo = (SkuBomVo) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return vo;
	}
	
}