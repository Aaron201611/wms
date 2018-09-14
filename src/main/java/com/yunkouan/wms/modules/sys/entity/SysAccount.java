package com.yunkouan.wms.modules.sys.entity;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.yunkouan.base.BaseEntity;
import com.yunkouan.valid.ValidSave;
import com.yunkouan.valid.ValidSearch;
import com.yunkouan.valid.ValidUpdate;

/**
 * 【企业帐号】实体类
 * @author tphe06 2017年2月14日
 */
public class SysAccount extends BaseEntity {
	private static final long serialVersionUID = 3039334686716699547L;

	/**帐号id*/
	@Id
	private String accountId;

	/**登录帐号*/
	@Length(max=32,message="{valid_account_accountNo_length}",groups={ValidSave.class,ValidUpdate.class,ValidSearch.class})
	@NotNull(message="{valid_account_accountNo_notnull}",groups={ValidUpdate.class})
    private String accountNo;

	/**帐号名称*/
	@Length(max=32,message="{valid_account_accountName_length}",groups={ValidSave.class,ValidUpdate.class,ValidSearch.class})
	@NotNull(message="{valid_account_accountName_notnull}",groups={ValidSave.class,ValidUpdate.class})
    private String accountName;

	/**组织id*/
	@Length(max=64,message="{valid_account_orgId_length}",groups={ValidSave.class,ValidUpdate.class,ValidSearch.class})
    private String orgId;

	/**登录密码*/
	@Length(max=64,message="{valid_account_accountPwd_length}",groups={ValidSave.class,ValidUpdate.class,ValidSearch.class})
	@NotNull(message="{valid_account_accountPwd_notnull}",groups={ValidSave.class})
    private String accountPwd;

	/**帐号状态*/
//	@NotNull(message="{valid_account_accountStatus_notnull}",groups={ValidUpdate.class})
    private Integer accountStatus;

	/**用户id*/
	@Length(max=64,message="{valid_account_userId_length}",groups={ValidSave.class,ValidUpdate.class,ValidSearch.class})
//	@NotNull(message="{valid_account_userId_notnull}",groups={ValidSave.class,ValidUpdate.class})
    private String userId;

    private Integer accountId2;

    @Length(max=256,message="{valid_account_note_length}",groups={ValidSave.class,ValidUpdate.class,ValidSearch.class})
    private String note;

	@Override
	public void clear() {
    	/**不能由前端修改，添加修改时候都赋空值**/
    	this.accountId2 = null;
    	/**添加时候由程序赋值，修改时候置空不更新数据库的值，生效/激活/取消时候才更新数据库的值**/
    	this.accountStatus = null;
    	/**不能由前端修改，添加时候取当前登录人的，修改时候置空不更新数据库的值**/
    	this.orgId = null;
		super.clear();
	}

    public String getAccountId() {
        return accountId;
    }

    public SysAccount setAccountId(String accountId) {
        this.accountId = accountId == null ? null : accountId.trim();
        return this;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public SysAccount setAccountNo(String accountNo) {
        this.accountNo = accountNo == null ? null : accountNo.trim();
        return this;
    }

    public String getOrgId() {
        return orgId;
    }

    public SysAccount setOrgId(String orgId) {
        this.orgId = orgId == null ? null : orgId.trim();
        return this;
    }

    public String getAccountPwd() {
        return accountPwd;
    }

    public SysAccount setAccountPwd(String accountPwd) {
        this.accountPwd = accountPwd == null ? null : accountPwd.trim();
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public SysAccount setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
        return this;
    }

    public Integer getAccountId2() {
        return accountId2;
    }

    public SysAccount setAccountId2(Integer accountId2) {
        this.accountId2 = accountId2;
        return this;
    }

    public String getAccountName() {
        return accountName;
    }

    public SysAccount setAccountName(String accountName) {
        this.accountName = accountName == null ? null : accountName.trim();
        return this;
    }

	public String getNote() {
		return note;
	}

	public Integer getAccountStatus() {
		return accountStatus;
	}

	public SysAccount setNote(String note) {
		this.note = note;
		return this;
	}

	public SysAccount setAccountStatus(Integer accountStatus) {
		this.accountStatus = accountStatus;
		return this;
	}
}