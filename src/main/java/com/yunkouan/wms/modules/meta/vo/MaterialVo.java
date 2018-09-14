package com.yunkouan.wms.modules.meta.vo;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;

import com.yunkouan.base.BaseVO;
import com.yunkouan.exception.BizException;
import com.yunkouan.saas.modules.sys.service.ISysParamService;
import com.yunkouan.util.SpringContextHolder;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.common.constant.CacheName;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.meta.entity.MetaMaterial;
import com.yunkouan.wms.modules.meta.entity.MetaMaterialLog;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 辅材数据传输类
 * @author tphe06 2017年2月14日
 */
public class MaterialVo extends BaseVO {
	private static final long serialVersionUID = 8254229011084473157L;

	/**辅材视图类*/
	@Valid
	private MetaMaterial entity;
	/**@Fields 排序字段 */
	private String orderBy = "create_time desc";
	/**@Fields 辅材代码模糊查询字段 */
	private String materialNoLike;
	/**@Fields 辅材名称模糊查询字段 */
	private String materialNameLike;
	/**@Fields 辅材名称模糊查询字段 */
	private String materialBarCodeLike;

	/**@Fields 状态名称 */
	private String materialStatusName;
	/**@Fields 辅材类型名称 */
	private String materialTypeName;
	/**辅材名称*/
	private String materialName;
	/**@Fields 相关单号 */
	private String involveBill;
	/**@Fields 调整后数量 */
	private Integer resultQty;
	/**@Fields 调整数量 */
	private Integer changeQty;
	/**@Fields 辅材日志 */
	private MetaMaterialLog materialLog;

	public MaterialVo() {}
	public MaterialVo(MetaMaterial entity) {
		this.entity = entity;
		if(this.entity == null) return;
		ISysParamService paramService = SpringContextHolder.getBean(ISysParamService.class);
		this.materialStatusName = paramService.getValue(CacheName.COMMON_STATUS, this.entity.getMaterialStatus());
	}

	@Override
	public Example getExample() {
		Example example = new Example(MetaMaterial.class);
		example.setOrderByClause(orderBy);
		if(entity == null) return example;
		Criteria c = example.createCriteria();
		Principal loginUser = LoginUtil.getLoginUser();
    	// 验证用户是否登录
    	if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
    		throw new BizException("valid_common_user_no_login");
    	}
    	c.andEqualTo("orgId", loginUser.getOrgId());
		if(StringUtils.isNoneBlank(materialNoLike)) {
			c.andLike("materialNo", StringUtil.likeEscapeH(materialNoLike));
		}
		if(StringUtils.isNoneBlank(materialNameLike)) {
			c.andLike("materialName", StringUtil.likeEscapeH(materialNameLike));
		}
		if(StringUtils.isNoneBlank(materialBarCodeLike)) {
			c.andLike("materialBarCode", StringUtil.likeEscapeH(materialBarCodeLike));
		}
		if(StringUtils.isNoneBlank(entity.getMaterialNo())) {
			c.andEqualTo("materialNo", entity.getMaterialNo());
		}
		if(StringUtils.isNoneBlank(entity.getMaterialName())) {
			c.andEqualTo("materialName", entity.getMaterialName());
		}
    	if(StringUtils.isNoneBlank(entity.getMaterialBarCode())) {
			c.andEqualTo("materialBarCode", entity.getMaterialBarCode());
		}
		if(entity.getMaterialStatus() != null) {
			c.andEqualTo("materialStatus", entity.getMaterialStatus());
		}
		return example;
	}

	public MetaMaterial getEntity() {
		return entity;
	}

	public MaterialVo orderByUpdateTimeDesc() {
		this.orderBy = "update_time desc ";
		return this;
	}

	public MaterialVo setEntity(MetaMaterial entity) {
		this.entity = entity;
		return this;
	}

	public MaterialVo setMaterialStatus(String materialStatusName) {
		this.materialStatusName = materialStatusName;
		return this;
	}
	public void setMaterialNoLike(String materialNoLike) {
		this.materialNoLike = materialNoLike;
	}
	public void setMaterialNameLike(String materialNameLike) {
		this.materialNameLike = materialNameLike;
	}
	/**
	 * 属性 materialNoLike getter方法
	 * @return 属性materialNoLike
	 * @author 王通<br/>
	 */
	public String getMaterialNoLike() {
		return materialNoLike;
	}
	/**
	 * 属性 materialNameLike getter方法
	 * @return 属性materialNameLike
	 * @author 王通<br/>
	 */
	public String getMaterialNameLike() {
		return materialNameLike;
	}
	public String getMaterialTypeName() {
		return materialTypeName;
	}
	public void setMaterialTypeName(String materialTypeName) {
		this.materialTypeName = materialTypeName;
	}
	/**
	 * 属性 materialStatusName getter方法
	 * @return 属性materialStatusName
	 * @author 王通<br/>
	 */
	public String getMaterialStatusName() {
		return materialStatusName;
	}
	/**
	 * 属性 materialStatusName setter方法
	 * @param materialStatusName 设置属性materialStatusName的值
	 * @author 王通<br/>
	 */
	public void setMaterialStatusName(String materialStatusName) {
		this.materialStatusName = materialStatusName;
	}
	/**
	 * 属性 involveBill getter方法
	 * @return 属性involveBill
	 * @author 王通<br/>
	 */
	public String getInvolveBill() {
		return involveBill;
	}
	/**
	 * 属性 involveBill setter方法
	 * @param involveBill 设置属性involveBill的值
	 * @author 王通<br/>
	 */
	public void setInvolveBill(String involveBill) {
		this.involveBill = involveBill;
	}
	/**
	 * 属性 materialLog getter方法
	 * @return 属性materialLog
	 * @author 王通<br/>
	 */
	public MetaMaterialLog getMaterialLog() {
		return materialLog;
	}
	/**
	 * 属性 materialLog setter方法
	 * @param materialLog 设置属性materialLog的值
	 * @author 王通<br/>
	 */
	public void setMaterialLog(MetaMaterialLog materialLog) {
		this.materialLog = materialLog;
	}
	/**
	 * 属性 resultQty getter方法
	 * @return 属性resultQty
	 * @author 王通<br/>
	 */
	public Integer getResultQty() {
		return resultQty;
	}
	/**
	 * 属性 resultQty setter方法
	 * @param resultQty 设置属性resultQty的值
	 * @author 王通<br/>
	 */
	public void setResultQty(Integer resultQty) {
		this.resultQty = resultQty;
	}
	/**
	 * 属性 changeQty getter方法
	 * @return 属性changeQty
	 * @author 王通<br/>
	 */
	public Integer getChangeQty() {
		return changeQty;
	}
	/**
	 * 属性 changeQty setter方法
	 * @param changeQty 设置属性changeQty的值
	 * @author 王通<br/>
	 */
	public void setChangeQty(Integer changeQty) {
		this.changeQty = changeQty;
	}
	/**
	 * 属性 materialBarCodeLike getter方法
	 * @return 属性materialBarCodeLike
	 * @author 王通<br/>
	 */
	public String getMaterialBarCodeLike() {
		return materialBarCodeLike;
	}
	/**
	 * 属性 materialBarCodeLike setter方法
	 * @param materialBarCodeLike 设置属性materialBarCodeLike的值
	 * @author 王通<br/>
	 */
	public void setMaterialBarCodeLike(String materialBarCodeLike) {
		this.materialBarCodeLike = materialBarCodeLike;
	}
	/**
	 * 属性 materialName getter方法
	 * @return 属性materialName
	 * @author 王通<br/>
	 */
	public String getMaterialName() {
		return materialName;
	}
	/**
	 * 属性 materialName setter方法
	 * @param materialName 设置属性materialName的值
	 * @author 王通<br/>
	 */
	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}
}