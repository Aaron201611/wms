package com.yunkouan.wms.modules.meta.vo;

import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;

import com.yunkouan.base.BaseVO;
import com.yunkouan.saas.modules.sys.entity.MetaWarehouse;
import com.yunkouan.saas.modules.sys.entity.SysOrg;
import com.yunkouan.saas.modules.sys.service.ISysParamService;
import com.yunkouan.util.SpringContextHolder;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.common.constant.CacheName;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.meta.entity.MetaMerchant;
import com.yunkouan.wms.modules.meta.entity.MetaPack;
import com.yunkouan.wms.modules.meta.entity.MetaSku;
import com.yunkouan.wms.modules.meta.entity.MetaSkuType;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 货品数据传输类
 * @author tphe06 2017年2月14日
 */
public class SkuVo extends BaseVO {
	private static final long serialVersionUID = 8254229011084473157L;

	/**货品实体类*/
	@Valid
	private MetaSku entity;
	/**@Fields 排序字段 */
	private String orderBy = "sku_id2 desc";
	/**@Fields 货品代码模糊查询字段 */
	private String skuNoLike;
	/**@Fields 货品名称模糊查询字段 */
	private String skuNameLike;
	/**@Fields 货品名称模糊查询字段 */
	private String skuBarCodeLike;
	/**
	 * 计量单位模糊查询字段
	 * @version 2017年5月16日 下午7:53:25<br/>
	 * @author 王通<br/>
	 */
	
	private String measureUnitLike;
	/**@Fields 状态名称 */
	private String skuStatus;
	/**@Fields 货品类型ID */
	private String skuTypeId;
	/**@Fields 货品类型名称 */
	private String skuTypeName;
	/**@Fields 父类货品类型ID */
	private String parentTypeId;
	/**parent:上级货品类型**/
	private MetaSkuType parent;
	/**typeList:所有二级货品列表，用于列表显示**/
	private List<SkuTypeVo> typeList;
	/**merchantIdList:客商id列表**/
	private List<String> merchantIdList;
	/**@Fields 货主 */
	private MetaMerchant merchant;
	/**warehouse:仓库**/
	private MetaWarehouse warehouse;
	/**@Fields 包装列表信息 */
	private List<MetaPack> list;
	/**org:企业**/
	private SysOrg org;
	
	private String currComment;
	
	private String goodsNatureComment;
	
	private String originCountryName;
	private List<String >ids;

	public SkuVo() {this.entity=new MetaSku();}
	public SkuVo(MetaSku entity) {
		this.entity = entity;
		if(this.entity == null) return;
		ISysParamService paramService = SpringContextHolder.getBean(ISysParamService.class);
		this.skuStatus = paramService.getValue(CacheName.COMMON_STATUS, this.entity.getSkuStatus());
		
	}

	@Override
	public Example getExample() {
		Example example = new Example(MetaSku.class);
		example.setOrderByClause(orderBy);
		Criteria c = example.createCriteria();
		// 以下为非实体属性
		Principal loginUser = LoginUtil.getLoginUser();
		if(loginUser != null) {
	    	c.andEqualTo("orgId", loginUser.getOrgId());
	    	c.andEqualTo("warehouseId", LoginUtil.getWareHouseId());
		}
		if(StringUtils.isNoneBlank(skuNoLike)) {
			c.andLike("skuNo", StringUtil.likeEscapeH(skuNoLike));
		}
		if(StringUtils.isNoneBlank(skuNameLike)) {
			c.andLike("skuName", StringUtil.likeEscapeH(skuNameLike));
		}
		if(StringUtils.isNoneBlank(skuBarCodeLike)) {
			c.andLike("skuBarCode", StringUtil.likeEscapeH(skuBarCodeLike));
		}
		if(StringUtils.isNoneBlank(measureUnitLike)) {
			c.andLike("measureUnit", StringUtil.likeEscapeH(measureUnitLike));
		}
		if(merchantIdList != null && !merchantIdList.isEmpty()) {
			c.andIn("owner", merchantIdList);
		}
		// 以下为实体属性
		if(entity == null) return example;
		if(StringUtils.isNoneBlank(entity.getSkuNo())) {
			c.andEqualTo("skuNo", entity.getSkuNo());
		}
		if(StringUtils.isNoneBlank(entity.getSkuBarCode())) {
			c.andEqualTo("skuBarCode", entity.getSkuBarCode());
		}
		if(StringUtils.isNoneBlank(entity.getSkuName())) {
			c.andEqualTo("skuName", entity.getSkuName());
		}
		if(StringUtils.isNoneBlank(entity.getOwner())) {
			c.andEqualTo("owner", entity.getOwner());
		}
		if(entity.getSkuStatus() != null) {
			c.andEqualTo("skuStatus", entity.getSkuStatus());
		}
		if(ids!=null&&ids.size()>0){
			c.andIn("skuId", ids);
		}
		return example;
	}
	
	public MetaSku getEntity() {
		return entity;
	}

	public SkuVo orderByUpdateTimeDesc() {
		this.orderBy = "update_time desc ";
		return this;
	}

	public SkuVo setEntity(MetaSku entity) {
		this.entity = entity;
		ISysParamService paramService = SpringContextHolder.getBean(ISysParamService.class);
		this.currComment = paramService.getValue(CacheName.CURR_CODE, this.entity.getCurr());
		this.goodsNatureComment = paramService.getValue(CacheName.GOODS_NATURE, this.entity.getGoodsNature());
		this.originCountryName = paramService.getValue(CacheName.COUNTORY_CODE, this.entity.getOriginCountry());
		return this;
	}

	public List<MetaPack> getList() {
		return list;
	}

	public SkuVo setList(List<MetaPack> list) {
		this.list = list;
		return this;
	}

	public MetaMerchant getMerchant() {
		return merchant;
	}

	public SkuVo setMerchant(MetaMerchant merchant) {
		this.merchant = merchant;
		return this;
	}
	public String getSkuStatus() {
		return skuStatus;
	}
	public String getSkuTypeId() {
		return skuTypeId;
	}
	public SkuVo setSkuStatus(String skuStatus) {
		this.skuStatus = skuStatus;
		return this;
	}
	public SkuVo setSkuTypeId(String skuTypeId) {
		this.skuTypeId = skuTypeId;
		return this;
	}
	public SkuVo setSkuNoLike(String skuNoLike) {
		this.skuNoLike = skuNoLike;
		return this;
	}
	public void setSkuNameLike(String skuNameLike) {
		this.skuNameLike = skuNameLike;
	}
	/**
	 * 属性 measureUnitLike getter方法
	 * @return 属性measureUnitLike
	 * @author 王通<br/>
	 */
	public String getMeasureUnitLike() {
		return measureUnitLike;
	}
	/**
	 * 属性 measureUnitLike setter方法
	 * @param measureUnitLike 设置属性measureUnitLike的值
	 * @author 王通<br/>
	 */
	public void setMeasureUnitLike(String measureUnitLike) {
		this.measureUnitLike = measureUnitLike;
	}
	/**
	 * 属性 skuNoLike getter方法
	 * @return 属性skuNoLike
	 * @author 王通<br/>
	 */
	public String getSkuNoLike() {
		return skuNoLike;
	}
	/**
	 * 属性 skuNameLike getter方法
	 * @return 属性skuNameLike
	 * @author 王通<br/>
	 */
	public String getSkuNameLike() {
		return skuNameLike;
	}
	public String getSkuTypeName() {
		return skuTypeName;
	}
	public void setSkuTypeName(String skuTypeName) {
		this.skuTypeName = skuTypeName;
	}
	public MetaSkuType getParent() {
		return parent;
	}
	public SkuVo setParent(MetaSkuType parent) {
		this.parent = parent;
		return this;
	}
	public List<SkuTypeVo> getTypeList() {
		return typeList;
	}
	public SkuVo setTypeList(List<SkuTypeVo> typeList) {
		this.typeList = typeList;
		return this;
	}
	/**
	 * 属性 merchantIdList getter方法
	 * @return 属性merchantIdList
	 * @author 王通<br/>
	 */
	public List<String> getMerchantIdList() {
		return merchantIdList;
	}
	/**
	 * 属性 merchantIdList setter方法
	 * @param merchantIdList 设置属性merchantIdList的值
	 * @author 王通<br/>
	 */
	public void setMerchantIdList(List<String> merchantIdList) {
		this.merchantIdList = merchantIdList;
	}
	public SysOrg getOrg() {
		return org;
	}
	public void setOrg(SysOrg org) {
		this.org = org;
	}
	public MetaWarehouse getWarehouse() {
		return warehouse;
	}
	public void setWarehouse(MetaWarehouse warehouse) {
		this.warehouse = warehouse;
	}
	public String getCurrComment() {
		return currComment;
	}
	public void setCurrComment(String currComment) {
		this.currComment = currComment;
	}
	public String getGoodsNatureComment() {
		return goodsNatureComment;
	}
	public void setGoodsNatureComment(String goodsNatureComment) {
		this.goodsNatureComment = goodsNatureComment;
	}
	public String getOriginCountryName() {
		return originCountryName;
	}
	public void setOriginCountryName(String originCountryName) {
		this.originCountryName = originCountryName;
	}
	
	public String getSkuBarCodeLike() {
		return skuBarCodeLike;
	}
	
	public void setSkuBarCodeLike(String skuBarCodeLike) {
		this.skuBarCodeLike = skuBarCodeLike;
	}
	public String getParentTypeId() {
		return parentTypeId;
	}
	public void setParentTypeId(String parentTypeId) {
		this.parentTypeId = parentTypeId;
	}
	public List<String> getIds() {
		return ids;
	}
	
	public void setIds(List<String> ids) {
		this.ids = ids;
	}
	
	
	
	
}