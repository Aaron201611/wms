package com.yunkouan.wms.modules.meta.vo;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;

import com.yunkouan.base.BaseVO;
import com.yunkouan.saas.modules.sys.service.ISysParamService;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.util.SpringContextHolder;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.common.constant.CacheName;
import com.yunkouan.wms.modules.meta.entity.MetaMerchant;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 客商数据传输类
 * @author tphe06 2017年2月14日
 */
public class MerchantVo extends BaseVO {
	private static final long serialVersionUID = -731292610571874760L;

	/**客商实体类*/
	@Valid
	private MetaMerchant entity;
	/**@Fields 排序字段 */
	private String orderBy = "merchant_id2 desc";
	/**@Fields 客商编号模糊查询字段 */
	private String merchantNoLike;
	/**@Fields 客商名称模糊查询字段 */
	private String merchantNameLike;
	/**联系人模糊查询*/
	private String contactPersonLike;

	/**@Fields 客商状态名称 */
	private String merchantStatus;
    /**@Fields 是否货主 */
    private String isOwner;
    /**@Fields 是否客商 */
    private String isCustomer;
    /**@Fields 是否承运商 */
    private String isCarrier;
    /**@Fields 是否供应商 */
    private String isProvider;
    /**@Fields 是否发货方 */
    private String isSender;
    /**@Fields 是否收货方 */
    private String isReceiver;
    /**isConsignor:是否寄件人**/
    private String isConsignor;

	public MerchantVo(){}
	public MerchantVo(MetaMerchant entity) {
		this.entity = entity;
		if(this.entity == null) return;
		ISysParamService paramService = SpringContextHolder.getBean(ISysParamService.class);
		this.merchantStatus = paramService.getValue(CacheName.COMMON_STATUS, this.entity.getMerchantStatus());
		this.isOwner = paramService.getValue(CacheName.COMMON_YES_NO, this.entity.getIsOwner());
		this.isCustomer = paramService.getValue(CacheName.COMMON_YES_NO, this.entity.getIsCustomer());
		this.isCarrier = paramService.getValue(CacheName.COMMON_YES_NO, this.entity.getIsCarrier());
		this.isProvider = paramService.getValue(CacheName.COMMON_YES_NO, this.entity.getIsProvider());
		this.isSender = paramService.getValue(CacheName.COMMON_YES_NO, this.entity.getIsSender());
		this.isReceiver = paramService.getValue(CacheName.COMMON_YES_NO, this.entity.getIsReceiver());
		this.isConsignor = paramService.getValue(CacheName.COMMON_YES_NO, this.entity.getIsConsignor());
	}

	@Override
	public Example getExample() {
		Example example = new Example(MetaMerchant.class);
		example.setOrderByClause(orderBy);
		Criteria c = example.createCriteria();
		if(StringUtils.isNoneBlank(merchantNoLike)) {
			c.andLike("merchantNo", StringUtil.likeEscapeH(merchantNoLike));
		}
		if(StringUtils.isNoneBlank(merchantNameLike)) {
			c.andLike("merchantName", StringUtil.likeEscapeH(merchantNameLike));
		}
		if(StringUtils.isNoneBlank(contactPersonLike)) {
			c.andLike("contactPerson", StringUtil.likeEscapeH(contactPersonLike));
		}
		//以上为非实体属性
		if(entity == null) return example;
		//以下为实体属性
		if(StringUtils.isNoneBlank(entity.getMerchantNo())) {
			c.andEqualTo("merchantNo", entity.getMerchantNo());
		}
		if(StringUtils.isNoneBlank(entity.getMerchantName())) {
			c.andEqualTo("merchantName", entity.getMerchantName());
		}
		if(StringUtils.isNoneBlank(entity.getContactPerson())) {
			c.andEqualTo("contactPerson", entity.getContactPerson());
		}
		if(entity.getIsOwner() != null) {
			c.andEqualTo("isOwner", entity.getIsOwner());
		}
		if(entity.getIsCustomer() != null) {
			c.andEqualTo("isCustomer", entity.getIsCustomer());
		}
		if(entity.getIsCarrier() != null) {
			c.andEqualTo("isCarrier", entity.getIsCarrier());
		}
		if(entity.getIsProvider() != null) {
			c.andEqualTo("isProvider", entity.getIsProvider());
		}
		if(entity.getIsSender() != null) {
			c.andEqualTo("isSender", entity.getIsSender());
		}
		if(entity.getIsReceiver() != null) {
			c.andEqualTo("isReceiver", entity.getIsReceiver());
		}
		if(entity.getIsConsignor() != null) {
			c.andEqualTo("isConsignor", entity.getIsConsignor());
		}
		if(entity.getMerchantStatus() != null) {
			c.andEqualTo("merchantStatus", entity.getMerchantStatus());
		} else {
			c.andNotEqualTo("merchantStatus", Constant.STATUS_CANCEL);
		}
		return example;
	}

	public MerchantVo orderByUpdateTimeDesc() {
		this.orderBy = "update_time desc ";
		return this;
	}
	public MetaMerchant getEntity() {
		return entity;
	}
	public String getMerchantStatus() {
		return merchantStatus;
	}
	public String getIsOwner() {
		return isOwner;
	}
	public String getIsCustomer() {
		return isCustomer;
	}
	public String getIsCarrier() {
		return isCarrier;
	}
	public String getIsProvider() {
		return isProvider;
	}
	public String getIsSender() {
		return isSender;
	}
	public String getIsReceiver() {
		return isReceiver;
	}
	public void setMerchantNoLike(String merchantNoLike) {
		this.merchantNoLike = merchantNoLike;
	}
	public void setMerchantNameLike(String merchantNameLike) {
		this.merchantNameLike = merchantNameLike;
	}
	/**
	 * 属性 contactPersonLike getter方法
	 * @return 属性contactPersonLike
	 * @author 王通<br/>
	 */
	public String getContactPersonLike() {
		return contactPersonLike;
	}
	/**
	 * 属性 contactPersonLike setter方法
	 * @param contactPersonLike 设置属性contactPersonLike的值
	 * @author 王通<br/>
	 */
	public void setContactPersonLike(String contactPersonLike) {
		this.contactPersonLike = contactPersonLike;
	}
	public String getIsConsignor() {
		return isConsignor;
	}
}