package com.yunkouan.wms.modules.meta.vo;

import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;

import com.yunkouan.base.BaseVO;
import com.yunkouan.saas.modules.sys.entity.SysOrg;
import com.yunkouan.saas.modules.sys.service.ISysParamService;
import com.yunkouan.util.SpringContextHolder;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.common.constant.CacheName;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.meta.entity.MetaMerchant;
import com.yunkouan.wms.modules.meta.entity.MetaPack;
import com.yunkouan.wms.modules.meta.entity.MetaSkuMaterialBom;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 货品辅材BOM数据传输类
 * @author tphe06 2017年2月14日
 */
public class SkuMaterialBomVo extends BaseVO {
	private static final long serialVersionUID = 8254229011084473157L;

	/**货品辅材BOM实体类*/
	@Valid
	private MetaSkuMaterialBom entity;
	/**@Fields 排序字段 */
	private String orderBy = "create_time desc";
	/**@Fields 识别码模糊查询字段 */
	private String identificationCodeLike;
	/**@Fields 货品代码摘要模糊查询字段 */
	private String skuNoCommentLike;
	/**@Fields 货品信息摘要模糊查询字段 */
	private String skuInfoCommentLike;
	/**@Fields 状态名称 */
	private String statusName;
	/**@Fields 创建人名称 */
	private String createPersonName;
	/**@Fields 修改人名称 */
	private String updatePersonName;
	/**@Fields 货品bom明细 */
	private List<SkuBomVo> skuBomList;
	public String getOrderBy() {
		return orderBy;
	}
	
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	
	public String getIdentificationCodeLike() {
		return identificationCodeLike;
	}
	
	public void setIdentificationCodeLike(String identificationCodeLike) {
		this.identificationCodeLike = identificationCodeLike;
	}
	
	public String getSkuNoCommentLike() {
		return skuNoCommentLike;
	}
	
	public void setSkuNoCommentLike(String skuNoCommentLike) {
		this.skuNoCommentLike = skuNoCommentLike;
	}
	
	public String getSkuInfoCommentLike() {
		return skuInfoCommentLike;
	}
	
	public void setSkuInfoCommentLike(String skuInfoCommentLike) {
		this.skuInfoCommentLike = skuInfoCommentLike;
	}
	
	public List<SkuBomVo> getSkuBomList() {
		return skuBomList;
	}
	
	public void setSkuBomList(List<SkuBomVo> skuBomList) {
		this.skuBomList = skuBomList;
	}
	
	public List<MaterialBomVo> getMaterialBomList() {
		return materialBomList;
	}
	
	public void setMaterialBomList(List<MaterialBomVo> materialBomList) {
		this.materialBomList = materialBomList;
	}
	
	/**@Fields 辅材bom明细 */
	private List<MaterialBomVo> materialBomList;
	
	public SkuMaterialBomVo() {this.entity = new MetaSkuMaterialBom();}
	public SkuMaterialBomVo(MetaSkuMaterialBom entity) {
		this.entity = entity;
		if(this.entity == null) return;
		ISysParamService paramService = SpringContextHolder.getBean(ISysParamService.class);
		this.statusName = paramService.getValue(CacheName.COMMON_STATUS, this.entity.getStatus());
		
	}

	@Override
	public Example getExample() {
		Example example = new Example(MetaSkuMaterialBom.class);
		example.setOrderByClause(orderBy);
		Criteria c = example.createCriteria();
		// 以下为非实体属性
		Principal loginUser = LoginUtil.getLoginUser();
		if(loginUser != null) {
	    	c.andEqualTo("orgId", loginUser.getOrgId());
	    	c.andEqualTo("warehouseId", LoginUtil.getWareHouseId());
		}
		if(StringUtils.isNoneBlank(skuNoCommentLike)) {
			c.andLike("skuNoComment", StringUtil.likeEscapeH(skuNoCommentLike));
		}
		if(StringUtils.isNoneBlank(skuInfoCommentLike)) {
			c.andLike("skuInfoComment", StringUtil.likeEscapeH(skuInfoCommentLike));
		}
		if(StringUtils.isNoneBlank(identificationCodeLike)) {
			c.andLike("identificationCode", StringUtil.likeEscapeH(identificationCodeLike));
		}
		// 以下为实体属性
		if(entity == null) return example;
		if(entity.getStatus() != null) {
			c.andEqualTo("status", entity.getStatus());
		}
		if(StringUtils.isNoneBlank(entity.getIdentificationCode())) {
			c.andEqualTo("identificationCode", entity.getIdentificationCode());
		}
		return example;
	}
	
	public MetaSkuMaterialBom getEntity() {
		return entity;
	}

	public SkuMaterialBomVo setEntity(MetaSkuMaterialBom entity) {
		this.entity = entity;
		return this;
	}
	public String getStatusName() {
		return statusName;
	}
	public SkuMaterialBomVo setStatusName(String statusName) {
		this.statusName = statusName;
		return this;
	}
	public String getCreatePersonName() {
		return createPersonName;
	}
	public void setCreatePersonName(String createPersonName) {
		this.createPersonName = createPersonName;
	}
	public String getUpdatePersonName() {
		return updatePersonName;
	}
	public void setUpdatePersonName(String updatePersonName) {
		this.updatePersonName = updatePersonName;
	}
}