package com.yunkouan.wms.modules.send.vo;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yunkouan.base.BaseVO;
import com.yunkouan.wms.modules.send.entity.SysExpressNoPool;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/** 
* @Description: 快递单号池vo
* @author tphe06
* @date 2018年4月18日 下午2:44:47  
*/
public class SysExpressNoPoolVo extends BaseVO {
	private static final long serialVersionUID = 1017562187661108161L;

	@Valid
	private SysExpressNoPool entity = new SysExpressNoPool();

	public SysExpressNoPoolVo(){}
	public SysExpressNoPoolVo(SysExpressNoPool entity) {
		this.entity = entity;
	}

	@Override
	@JsonIgnore
	public Example getExample() {
		Example example = new Example(SysExpressNoPool.class);
		Criteria c = example.createCriteria();
		example.setOrderByClause("create_time asc ");
		if(entity == null) return example;
		if(StringUtils.isNoneBlank(entity.getExpressBillNo())) {
			c.andEqualTo("expressBillNo", entity.getExpressBillNo());
		}
		if(StringUtils.isNoneBlank(entity.getExpressServiceCode())) {
			c.andEqualTo("expressServiceCode", entity.getExpressServiceCode());
		}
		if(StringUtils.isNoneBlank(entity.getOrgId())) {
			c.andEqualTo("orgId", entity.getOrgId());
		}
		if(entity.getIsUsed() != null) {
			c.andEqualTo("isUsed", entity.getIsUsed());
		}
		return example;
	}

	public SysExpressNoPool getEntity() {
		return entity;
	}

	public void setEntity(SysExpressNoPool entity) {
		this.entity = entity;
	}
}