package com.yunkouan.wms.modules.meta.vo;

import java.util.ArrayList;
import java.util.List;

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
import com.yunkouan.wms.modules.meta.entity.MetaSkuType;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 货品类型数据传输类
 * @author tphe06 2017年2月14日
 */
public class SkuTypeVo extends BaseVO {
	private static final long serialVersionUID = -5272022949961944914L;

	/**货品类型实体类*/
	@Valid
	private MetaSkuType entity;
	private MetaSkuType parent;
	/**@Fields 排序字段 */
	private String orderBy = "sku_type_id2 desc";
	private int statusNo;
	/**@Fields 包装类型编码模糊查询字段 */
	private String skuTypeNoLike;
	/**@Fields 包装类型名称模糊查询字段 */
	private String skuTypeNameLike;
	/**@Fields 包装类型名称模糊查询字段 */
	private String merchantNameLike;
	/**@Fields 状态名称 */
	private String skuTypeStatus;
	/**@Fields 所属客户名称 */
	private String merchantId;
	/**@Fields 父级名称 */
	private String parentId;
	/**
	 * 是否有子集
	 * @version 2017年5月19日 下午6:11:58<br/>
	 * @author 王通<br/>
	 */
	private Boolean hasChild;
	/**
	 * 父类编码模糊查询
	 * @version 2017年5月19日 下午6:37:09<br/>
	 * @author 王通<br/>
	 */
	private String parentNoLike;
	/**
	 * 父类编码
	 * @version 2017年5月21日 上午11:09:20<br/>
	 * @author 王通<br/>
	 */
	private String parentNo;
	
	private Integer skuTypeStatueNotEqual;
	
	public SkuTypeVo(){}
	public SkuTypeVo(MetaSkuType entity) {
		this.entity = entity;
		if(this.entity == null) return;
		ISysParamService paramService = SpringContextHolder.getBean(ISysParamService.class);
		this.skuTypeStatus = paramService.getValue(CacheName.COMMON_STATUS, this.entity.getSkuTypeStatus());
	}
	/**
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年5月21日 下午2:05:33<br/>
	 * @author 王通<br/>
	 */
	public SkuTypeVo likeVo() {
		Principal loginUser = LoginUtil.getLoginUser();
    	// 验证用户是否登录
    	if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
    		throw new BizException("valid_common_user_no_login");
    	}
		entity.setOrgId(loginUser.getOrgId());
		if (!StringUtil.isTrimEmpty(merchantNameLike)) {
			merchantNameLike = StringUtil.likeEscapeH(merchantNameLike);
		}
		if (!StringUtil.isTrimEmpty(skuTypeNoLike)) {
			skuTypeNoLike = StringUtil.likeEscapeH(skuTypeNoLike);
		}
		if (!StringUtil.isTrimEmpty(skuTypeNameLike)) {
			skuTypeNameLike = StringUtil.likeEscapeH(skuTypeNameLike);
		}
		if (!StringUtil.isTrimEmpty(parentNoLike)) {
			parentNoLike = StringUtil.likeEscapeH(parentNoLike);
		}
		return this;
	}

	public Example getExample() {
		Example example = new Example(MetaSkuType.class);
		Criteria c = example.createCriteria();
		if(this.statusNo > 0) {
			c.andNotEqualTo("skuTypeStatus", this.statusNo);
		}
		if(entity == null) return example;
		if(StringUtils.isNoneBlank(entity.getOrgId())) {
			c.andEqualTo("orgId", entity.getOrgId());
		}
		return example;
	}

	public Example getExampleParent() {
		Example example = new Example(MetaSkuType.class);
		example.setOrderByClause(orderBy);
		if(entity == null) return example;
		Criteria c = example.createCriteria();
		
		if (StringUtils.isNoneBlank(parentNoLike)) {
			c.andLike("skuTypeId", StringUtil.likeEscapeH(parentNoLike));
		}
		return example;
	}
	
	public Example getExample(List<MetaSkuType> list) {
		Example example = new Example(MetaSkuType.class);
		example.setOrderByClause(orderBy);
		if(entity == null) return example;
		Criteria c = example.createCriteria();
		if(StringUtils.isNoneBlank(skuTypeNoLike)) {
			c.andLike("skuTypeNo", StringUtil.likeEscapeH(skuTypeNoLike));
		}
		if(StringUtils.isNoneBlank(skuTypeNameLike)) {
			c.andLike("skuTypeName", StringUtil.likeEscapeH(skuTypeNameLike));
		}
		
		if (list != null) {
			List<String> parentIdList = new ArrayList<String>();
			for (MetaSkuType sku : list) {
				parentIdList.add(sku.getSkuTypeId());
			}
			if (!parentIdList.isEmpty()) {
				c.andIn("parentId", parentIdList);
			}
		}
		if(StringUtils.isNoneBlank(entity.getSkuTypeNo())) {
			c.andEqualTo("skuTypeNo", entity.getSkuTypeNo());
		}
		if(StringUtils.isNoneBlank(entity.getSkuTypeName())) {
			c.andEqualTo("skuTypeName", entity.getSkuTypeName());
		}
		if(StringUtils.isNoneBlank(entity.getParentId())) {
			c.andEqualTo("parentId", entity.getParentId());
		}
		if(StringUtils.isNoneBlank(entity.getOwner())) {
			c.andEqualTo("owner", entity.getOwner());
		}
		if(entity.getSkuTypeStatus() != null) {
			c.andEqualTo("skuTypeStatus", entity.getSkuTypeStatus());
		}
		return example;
	}

	/**
	 * @param id
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年5月19日 下午6:09:04<br/>
	 * @author 王通<br/>
	 */
	public Example getExample(String parantSkuTypeId) {
		Example example = new Example(MetaSkuType.class);
		Criteria c = example.createCriteria();
		c.andEqualTo("parentId", parantSkuTypeId);
		if(this.statusNo > 0) {
			c.andNotEqualTo("skuTypeStatus", this.statusNo);
		}
		return example;
	}

	/**
	 * @param id
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年5月19日 下午6:09:04<br/>
	 * @author 王通<br/>
	 */
	public Example getExampleByNo(String skyTypeNo) {
		Example example = new Example(MetaSkuType.class);
		Criteria c = example.createCriteria();
		c.andEqualTo("skyTypeNo", skyTypeNo);
		return example;
	}

	public Example getExampleChild() {
		Principal loginUser = LoginUtil.getLoginUser();
    	// 验证用户是否登录
    	if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
    		throw new BizException("valid_common_user_no_login");
    	}
		Example example = new Example(MetaSkuType.class);
		Criteria c = example.createCriteria();
		if(entity == null) return example;
		c.andEqualTo("orgId", loginUser.getOrgId());
		c.andIsNotNull("parentId");
		if(StringUtils.isNoneBlank(entity.getSkuTypeName())) {
			c.andEqualTo("skuTypeName", entity.getSkuTypeName());
		}
		if(entity.getSkuTypeStatus() != null){
			c.andEqualTo("skuTypeStatus",entity.getSkuTypeStatus());
		}
		if(this.skuTypeStatueNotEqual != null){
			c.andNotEqualTo("skuTypeStatus", skuTypeStatueNotEqual);
		}
		return example;
	}
	public MetaSkuType getEntity() {
		return entity;
	}
	public SkuTypeVo orderByUpdateTimeDesc() {
		this.orderBy = "update_time desc ";
		return this;
	}
	public String getSkuTypeStatus() {
		return skuTypeStatus;
	}
	public String getMerchantId() {
		return merchantId;
	}
	public String getParentId() {
		return parentId;
	}
	public SkuTypeVo setMerchantId(String merchantId) {
		this.merchantId = merchantId;
		return this;
	}
	public SkuTypeVo setParentId(String parentId) {
		this.parentId = parentId;
		return this;
	}
	public void setSkuTypeNoLike(String skuTypeNoLike) {
		this.skuTypeNoLike = skuTypeNoLike;
	}
	public void setSkuTypeNameLike(String skuTypeNameLike) {
		this.skuTypeNameLike = skuTypeNameLike;
	}

	public int getStatusNo() {
		return statusNo;
	}

	public SkuTypeVo setStatusNo(int statusNo) {
		this.statusNo = statusNo;
		return this;
	}

	/**
	 * 属性 skuTypeNoLike getter方法
	 * @return 属性skuTypeNoLike
	 * @author 王通<br/>
	 */
	public String getSkuTypeNoLike() {
		return skuTypeNoLike;
	}
	/**
	 * 属性 hasChild getter方法
	 * @return 属性hasChild
	 * @author 王通<br/>
	 */
	public Boolean getHasChild() {
		return hasChild;
	}
	/**
	 * 属性 hasChild setter方法
	 * @param hasChild 设置属性hasChild的值
	 * @author 王通<br/>
	 */
	public void setHasChild(Boolean hasChild) {
		this.hasChild = hasChild;
	}
	/**
	 * 属性 parentNo getter方法
	 * @return 属性parentNo
	 * @author 王通<br/>
	 */
	public String getParentNo() {
		return parentNo;
	}
	/**
	 * 属性 parentNo setter方法
	 * @param parentNo 设置属性parentNo的值
	 * @author 王通<br/>
	 */
	public void setParentNo(String parentNo) {
		this.parentNo = parentNo;
	}
	/**
	 * 属性 merchantNameLike getter方法
	 * @return 属性merchantNameLike
	 * @author 王通<br/>
	 */
	public String getMerchantNameLike() {
		return merchantNameLike;
	}

	/**
	 * 属性 merchantNameLike setter方法
	 * @param merchantNameLike 设置属性merchantNameLike的值
	 * @author 王通<br/>
	 */
	public void setMerchantNameLike(String merchantNameLike) {
		this.merchantNameLike = merchantNameLike;
	}

	/**
	 * 属性 parentNoLike getter方法
	 * @return 属性parentNoLike
	 * @author 王通<br/>
	 */
	public String getParentNoLike() {
		return parentNoLike;
	}

	/**
	 * 属性 parentNoLike setter方法
	 * @param parentNoLike 设置属性parentNoLike的值
	 * @author 王通<br/>
	 */
	public void setParentNoLike(String parentNoLike) {
		this.parentNoLike = parentNoLike;
	}
	public MetaSkuType getParent() {
		return parent;
	}
	public void setParent(MetaSkuType parent) {
		this.parent = parent;
	}
	public Integer getSkuTypeStatueNotEqual() {
		return skuTypeStatueNotEqual;
	}
	public void setSkuTypeStatueNotEqual(Integer skuTypeStatueNotEqual) {
		this.skuTypeStatueNotEqual = skuTypeStatueNotEqual;
	}
	
	
}