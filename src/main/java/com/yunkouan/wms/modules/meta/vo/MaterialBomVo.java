package com.yunkouan.wms.modules.meta.vo;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;

import com.yunkouan.base.BaseVO;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.meta.entity.MetaMaterial;
import com.yunkouan.wms.modules.meta.entity.MetaMaterialBom;
import com.yunkouan.wms.modules.meta.entity.MetaSku;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 辅材bom类
 * @author 王通 2017年2月14日
 */
public class MaterialBomVo extends BaseVO {
	private static final long serialVersionUID = 8254229011084473157L;

	/**辅材bom类*/
	@Valid
	private MetaMaterialBom entity;
	/**@Fields 排序字段 */
	private String orderBy = "create_time desc";
	/**辅材*/
	private MaterialVo materialVo;


	public MaterialBomVo() {this.entity = new MetaMaterialBom();}
	public MaterialBomVo(MetaMaterialBom entity) {
		this.entity = entity;
		if(this.entity == null) return;
	}

	@Override
	public Example getExample() {
		Example example = new Example(MetaMaterialBom.class);
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
		if(StringUtils.isNoneBlank(entity.getMaterialId())) {
			c.andEqualTo("materialId", entity.getMaterialId());
		}
		if(StringUtils.isNoneBlank(entity.getBomId())) {
			c.andEqualTo("bomId", entity.getBomId());
		}
		return example;
	}
	public MetaMaterialBom getEntity() {
		return entity;
	}
	
	public void setEntity(MetaMaterialBom entity) {
		this.entity = entity;
	}
	
	public String getOrderBy() {
		return orderBy;
	}
	
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	public MaterialVo getMaterialVo() {
		return materialVo;
	}
	public void setMaterialVo(MaterialVo materialVo) {
		this.materialVo = materialVo;
	}
	
	
	
}