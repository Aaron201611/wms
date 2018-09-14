package com.yunkouan.wms.modules.meta.vo;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;

import com.yunkouan.base.BaseVO;
import com.yunkouan.exception.BizException;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.meta.entity.MetaSalesman;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 业务员
 */
public class MetaSalesmanVo extends BaseVO {
	
	private static final long serialVersionUID = -3744078952059952264L;
	/**部门类型实体类*/
	@Valid
	private MetaSalesman entity;
	/**@Fields 包装类型名称模糊查询字段 */
	private String salesmanNameLike;
	/**@Fields 包装类型名称模糊查询字段 */
	private String salesmanNoLike;
	/**@Fields 状态名称 */
	private String salesmanStatus;
	/**部门id*/
	private String deparmentId;
	public MetaSalesmanVo likeVo() {
		// 验证用户是否登录
		Principal loginUser = LoginUtil.getLoginUser();
		entity.setUpdatePerson(loginUser.getOrgId());
		if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
			throw new BizException("valid_common_user_no_login");
		}
		if (!StringUtil.isTrimEmpty(salesmanNameLike)) {
			salesmanNameLike = StringUtil.likeEscapeH(salesmanNameLike);
		}
		return this;
	}

	/**
	 * 获得所有生效状态的业务员
	 */
	public Example getExample(String key,String value) {
		Example example = new Example(MetaSalesman.class);
		Criteria c = example.createCriteria();
		c.andEqualTo(key,value);
		return example;
	}
	/**
	 * 获得符合查询条件的业务员/默认为生效状态的
	 */
	public Example getExampleLike() {
		Example example = new Example(MetaSalesman.class);
		Criteria c = example.createCriteria();
		boolean boo=true;
		if(!StringUtils.isBlank(deparmentId)){
			c.andEqualTo("deparmentId", deparmentId);
		}
		if(!StringUtils.isBlank(salesmanStatus)){
			c.andEqualTo("salesmanStatus", salesmanStatus);
			boo=false;
		}
		if(!StringUtils.isBlank(salesmanNameLike)){
			c.andLike("salesmanNameLike", StringUtil.likeEscapeH(salesmanNameLike));
			boo=false;
		}
		if(!StringUtils.isBlank(salesmanNoLike)){
			c.andLike("salesmanNoLike", StringUtil.likeEscapeH(salesmanNoLike));
			boo=false;
		}
		if(boo){
			//默认不显示取消状态的
			c.andNotEqualTo("salesmanStatus",Constant.STATUS_CANCEL);
		}
		return example;
	}

	/**
	获取部门类型实体类  
	 * @return the entity
	 */
	public MetaSalesman getEntity() {
		return entity;
	}
	

	/**
	 * @param 部门类型实体类 the entity to set
	 */
	public void setEntity(MetaSalesman entity) {
		this.entity = entity;
	}
	

	/**
	获取@Fields包装类型名称模糊查询字段  
	 * @return the salesmanNameLike
	 */
	public String getSalesmanNameLike() {
		return salesmanNameLike;
	}
	

	/**
	 * @param @Fields包装类型名称模糊查询字段 the salesmanNameLike to set
	 */
	public void setSalesmanNameLike(String salesmanNameLike) {
		this.salesmanNameLike = salesmanNameLike;
	}
	

	/**
	获取@Fields状态名称  
	 * @return the salesmanStatus
	 */
	public String getSalesmanStatus() {
		return salesmanStatus;
	}
	

	/**
	 * @param @Fields状态名称 the salesmanStatus to set
	 */
	public void setSalesmanStatus(String salesmanStatus) {
		this.salesmanStatus = salesmanStatus;
	}

	/**
	获取deparmentId  
	 * @return the deparmentId
	 */
	public String getDeparmentId() {
		return deparmentId;
	}
	

	/**
	 * @param deparmentId the deparmentId to set
	 */
	public void setDeparmentId(String deparmentId) {
		this.deparmentId = deparmentId;
	}

	/**
	获取@Fields包装类型名称模糊查询字段  
	 * @return the salesmanNoLike
	 */
	public String getSalesmanNoLike() {
		return salesmanNoLike;
	}
	

	/**
	 * @param @Fields包装类型名称模糊查询字段 the salesmanNoLike to set
	 */
	public void setSalesmanNoLike(String salesmanNoLike) {
		this.salesmanNoLike = salesmanNoLike;
	}
}