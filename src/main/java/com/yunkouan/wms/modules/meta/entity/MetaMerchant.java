package com.yunkouan.wms.modules.meta.entity;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.yunkouan.base.BaseEntity;
import com.yunkouan.valid.ValidSave;
import com.yunkouan.valid.ValidSearch;
import com.yunkouan.valid.ValidUpdate;

/**
 * 客商实体类
 * @author tphe06 2017年2月14日
 */
public class MetaMerchant extends BaseEntity {
	private static final long serialVersionUID = 1069716737390130924L;

	/**客商id*/
	@Id
	private String merchantId;
	/**代码*/
	@Length(max=32,message="{valid_merchant_merchantNo_length}",groups={ValidSave.class,ValidUpdate.class,ValidSearch.class})
	@NotNull(message="{valid_merchant_merchantNo_notnull}",groups={ValidUpdate.class})
    private String merchantNo;
	/**状态*/
	@NotNull(message="{valid_merchant_merchantStatus_notnull}",groups={ValidUpdate.class})
    private Integer merchantStatus;
	/**名称*/
	@Length(max=64,message="{valid_merchant_merchantName_length}",groups={ValidSave.class,ValidUpdate.class,ValidSearch.class})
	@NotNull(message="{valid_merchant_merchantName_notnull}",groups={ValidSave.class,ValidUpdate.class})
    private String merchantName;
	/**简称*/
	@Length(max=32,message="{valid_merchant_merchantShortName_length}",groups={ValidSave.class,ValidUpdate.class,ValidSearch.class})
	@NotNull(message="{valid_merchant_merchantShortName_notnull}",groups={ValidSave.class,ValidUpdate.class})
    private String merchantShortName;
    /**组织id*/
	@Length(max=32,message="{valid_sku_orgId_length}",groups={ValidSave.class,ValidUpdate.class,ValidSearch.class})
    private String orgId;
	
	/**海关客商代码*/
	private String hgMerchantNo;
	
	/**相关代码*/
	private String relatedNo;

	/**国家*/
	@Length(max=32,message="{valid_merchant_country_length}",groups={ValidSave.class,ValidUpdate.class,ValidSearch.class})
    private String country;
	/**省份*/
	@Length(max=32,message="{valid_merchant_province_length}",groups={ValidSave.class,ValidUpdate.class,ValidSearch.class})
    private String province;
	/**城市*/
	@Length(max=32,message="{valid_merchant_city_length}",groups={ValidSave.class,ValidUpdate.class,ValidSearch.class})
    private String city;
	/**区县*/
	@Length(max=32,message="{valid_merchant_county_length}",groups={ValidSave.class,ValidUpdate.class,ValidSearch.class})
    private String county;

	/**联系人*/
	@Length(max=32,message="{valid_merchant_contactPerson_length}",groups={ValidSave.class,ValidUpdate.class,ValidSearch.class})
    private String contactPerson;
	/**联系地址*/
	@Length(max=512,message="{valid_merchant_contactAddress_length}",groups={ValidSave.class,ValidUpdate.class,ValidSearch.class})
    private String contactAddress;
	/**联系电话*/
	@Length(max=16,message="{valid_merchant_contactPhone_length}",groups={ValidSave.class,ValidUpdate.class,ValidSearch.class})
    private String contactPhone;
	/**手机号码*/
	@Length(max=16,message="{valid_merchant_mobile_length}",groups={ValidSave.class,ValidUpdate.class,ValidSearch.class})
    private String mobile;
	/**传真*/
	@Length(max=16,message="{valid_merchant_fax_length}",groups={ValidSave.class,ValidUpdate.class,ValidSearch.class})
    private String fax;
	/**邮箱*/
	@Length(max=32,message="{valid_merchant_email_length}",groups={ValidSave.class,ValidUpdate.class,ValidSearch.class})
    private String email;

	/**是否货主*/
    private Boolean isOwner;
	/**是否客户*/
    private Boolean isCustomer;
	/**是否承运商*/
    private Boolean isCarrier;
	/**是否供应商*/
    private Boolean isProvider;
	/**是否发货方*/
    private Boolean isSender;
	/**是否收货方*/
    private Boolean isReceiver;
    /**isConsignor:是否寄件人**/
    private Boolean isConsignor;

	/**note:备注**/
	@Length(max=512,message="{valid_merchant_note_length}",groups={ValidSave.class,ValidUpdate.class,ValidSearch.class})
    private String note;
    /**merchantId2:备用主键**/
    private Integer merchantId2;

    @Override
	public void clear() {
    	/**不能由前端修改，添加修改时候都赋空值**/
    	this.merchantId2 = null;
    	/**添加时候由程序赋值，修改时候置空不更新数据库的值，生效/激活/取消时候才更新数据库的值**/
    	this.merchantStatus = null;
    	/**不能由前端修改，添加时候取当前登录人的，修改时候置空不更新数据库的值**/
    	this.orgId = null;
		super.clear();
	}

	/**
	 * 属性 county getter方法
	 * @return 属性county
	 * @author 王通<br/>
	 */
	public String getCounty() {
		return county;
	}

	/**
	 * 属性 county setter方法
	 * @param county 设置属性county的值
	 * @author 王通<br/>
	 */
	public MetaMerchant setCounty(String county) {
		this.county = county;
		return this;
	}

    public String getMerchantId() {
        return merchantId;
    }

    public MetaMerchant setMerchantId(String merchantId) {
        this.merchantId = merchantId == null ? null : merchantId.trim();
        return this;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public MetaMerchant setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo == null ? null : merchantNo.trim();
        return this;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public MetaMerchant setMerchantName(String merchantName) {
        this.merchantName = merchantName == null ? null : merchantName.trim();
        return this;
    }

    public String getMerchantShortName() {
        return merchantShortName;
    }

    public MetaMerchant setMerchantShortName(String merchantShortName) {
        this.merchantShortName = merchantShortName == null ? null : merchantShortName.trim();
        return this;
    }

    public String getCountry() {
        return country;
    }

    public MetaMerchant setCountry(String country) {
        this.country = country == null ? null : country.trim();
        return this;
    }

    public String getProvince() {
        return province;
    }

    public MetaMerchant setProvince(String province) {
        this.province = province == null ? null : province.trim();
        return this;
    }

    public String getCity() {
        return city;
    }

    public MetaMerchant setCity(String city) {
        this.city = city == null ? null : city.trim();
        return this;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public MetaMerchant setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson == null ? null : contactPerson.trim();
        return this;
    }

    public String getContactAddress() {
        return contactAddress;
    }

    public MetaMerchant setContactAddress(String contactAddress) {
        this.contactAddress = contactAddress == null ? null : contactAddress.trim();
        return this;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public MetaMerchant setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone == null ? null : contactPhone.trim();
        return this;
    }

    public String getMobile() {
        return mobile;
    }

    public MetaMerchant setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
        return this;
    }

    public String getFax() {
        return fax;
    }

    public MetaMerchant setFax(String fax) {
        this.fax = fax == null ? null : fax.trim();
        return this;
    }

    public String getEmail() {
        return email;
    }

    public MetaMerchant setEmail(String email) {
        this.email = email == null ? null : email.trim();
        return this;
    }

    public Boolean getIsOwner() {
        return isOwner;
    }

    public MetaMerchant setIsOwner(Boolean isOwner) {
        this.isOwner = isOwner;
        return this;
    }

    public Boolean getIsCustomer() {
        return isCustomer;
    }

    public MetaMerchant setIsCustomer(Boolean isCustomer) {
        this.isCustomer = isCustomer;
        return this;
    }

    public Boolean getIsCarrier() {
        return isCarrier;
    }

    public MetaMerchant setIsCarrier(Boolean isCarrier) {
        this.isCarrier = isCarrier;
        return this;
    }

    public Boolean getIsProvider() {
        return isProvider;
    }

    public MetaMerchant setIsProvider(Boolean isProvider) {
        this.isProvider = isProvider;
        return this;
    }

    public String getNote() {
        return note;
    }

    public MetaMerchant setNote(String note) {
        this.note = note == null ? null : note.trim();
        return this;
    }

    public Boolean getIsSender() {
        return isSender;
    }

    public MetaMerchant setIsSender(Boolean isSender) {
        this.isSender = isSender;
        return this;
    }

    public Boolean getIsReceiver() {
        return isReceiver;
    }

    public MetaMerchant setIsReceiver(Boolean isReceiver) {
        this.isReceiver = isReceiver;
        return this;
    }

    public Integer getMerchantId2() {
        return merchantId2;
    }

    public MetaMerchant setMerchantId2(Integer merchantId2) {
        this.merchantId2 = merchantId2;
        return this;
    }

	public Integer getMerchantStatus() {
		return merchantStatus;
	}

	public MetaMerchant setMerchantStatus(Integer merchantStatus) {
		this.merchantStatus = merchantStatus;
		return this;
	}

	public String getOrgId() {
		return orgId;
	}

	public MetaMerchant setOrgId(String orgId) {
		this.orgId = orgId;
		return this;
	}

	public Boolean getIsConsignor() {
		return isConsignor;
	}

	public MetaMerchant setIsConsignor(Boolean isConsignor) {
		this.isConsignor = isConsignor;
		return this;
	}

	public String getHgMerchantNo() {
		return hgMerchantNo;
	}

	public void setHgMerchantNo(String hgMerchantNo) {
		this.hgMerchantNo = hgMerchantNo;
	}

	public String getRelatedNo() {
		return relatedNo;
	}

	public void setRelatedNo(String relatedNo) {
		this.relatedNo = relatedNo;
	}
	
	
}