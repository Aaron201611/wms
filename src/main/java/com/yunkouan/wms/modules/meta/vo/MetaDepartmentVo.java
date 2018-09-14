package com.yunkouan.wms.modules.meta.vo;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;

import com.yunkouan.base.BaseVO;
import com.yunkouan.exception.BizException;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.meta.entity.MetaDepartment;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 部门
 */
public class MetaDepartmentVo extends BaseVO {
	private static final long serialVersionUID = -5272022949961944914L;

	/**部门类型实体类*/
	@Valid
	private MetaDepartment entity;
	/**@Fields 包装类型名称模糊查询字段 */
	private String departmentNameLike;
	/**@Fields 状态名称 */
	private String departmentStatus;

	/*public DepartmentVo likeVo() {
		// 验证用户是否登录
		Principal loginUser = LoginUtil.getLoginUser();
		entity.setUpdatePerson(loginUser.getOrgId());
		if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
			throw new BizException("valid_common_user_no_login");
		}
		if (!StringUtil.isTrimEmpty(departmentNameLike)) {
			departmentNameLike = StringUtil.likeEscapeH(departmentNameLike);
		}
		return this;
	}*/
	public Example getExample(String key,String value){
		Example example = new Example(MetaDepartment.class);
		Criteria c = example.createCriteria();
		c.andEqualTo(key, value);
		return example;
	}

	/**
	 * 获得所有生效状态的企业
	 */
	public Example getExample() {
		Example example = new Example(MetaDepartment.class);
		Criteria c = example.createCriteria();
		c.andEqualTo("deparmentStatus",Constant.STATUS_ACTIVE);
		return example;
	}
	/**
	 * 获得符合条件的企业/默认为生效状态的
	 */
	public Example getExampleLike() {
		Example example = new Example(MetaDepartment.class);
		Criteria c = example.createCriteria();
		boolean boo=true;
		if(!StringUtils.isBlank(departmentStatus)){
			c.andEqualTo("departmentStatus", departmentStatus);
			boo=false;
		}
		if(!StringUtils.isBlank(departmentNameLike)){
			c.andLike("departmentName", StringUtil.likeEscapeH(departmentNameLike));
			boo=false;
		}
		if(boo){
			c.andEqualTo("departmentStatus",Constant.STATUS_ACTIVE);
		}
		return example;
	}
	/**
	获取部门类型实体类  
	 * @return the entity
	 */
	public MetaDepartment getEntity() {
		return entity;
	}


	/**
	 * @param 部门类型实体类 the entity to set
	 */
	public void setEntity(MetaDepartment entity) {
		this.entity = entity;
	}

	/**
	获取@Fields包装类型名称模糊查询字段  
	 * @return the departmentNameLike
	 */
	public String getDepartmentNameLike() {
		return departmentNameLike;
	}


	/**
	 * @param @Fields包装类型名称模糊查询字段 the departmentNameLike to set
	 */
	public void setDepartmentNameLike(String departmentNameLike) {
		this.departmentNameLike = departmentNameLike;
	}


	/**
	获取@Fields状态名称  
	 * @return the departmentStatus
	 */
	public String getDepartmentStatus() {
		return departmentStatus;
	}


	/**
	 * @param @Fields状态名称 the departmentStatus to set
	 */
	public void setDepartmentStatus(String departmentStatus) {
		this.departmentStatus = departmentStatus;
	}
	
}