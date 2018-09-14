package com.yunkouan.wms.modules.sys.vo;

import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;

import com.yunkouan.base.BaseVO;
import com.yunkouan.saas.modules.sys.entity.MetaWarehouse;
import com.yunkouan.saas.modules.sys.entity.SysOrg;
import com.yunkouan.saas.modules.sys.entity.SysUser;
import com.yunkouan.saas.modules.sys.service.ISysParamService;
import com.yunkouan.util.SpringContextHolder;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.common.constant.CacheName;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.modules.sys.entity.SysAccount;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 企业帐号数据传输对象
 * @author tphe06 2017年2月14日
 */
public final class AccountVo extends BaseVO {
	private static final long serialVersionUID = -5156104275136837146L;

	/**帐号实体类**/
	@Valid
	private SysAccount entity;
	/**@Fields 排序字段 */
	private String orderBy = "account_id2 desc";
	/**@Fields 帐号编号模糊查询字段 */
	private String accountNoLike;
	/**@Fields 帐号名称模糊查询字段 */
	private String accountNameLike;
	/**
	 * 手机号模糊查询字段 
	 * @version 2017年5月17日 下午4:10:23<br/>
	 * @author 王通<br/>
	 */
	private String phoneLike;
	/**
	 * 是否返回当前登录人
	 * @version 2017年5月17日 下午4:10:23<br/>
	 * @author 王通<br/>
	 */
	private Boolean isLoginUser;
	/**@Fields 不查询的帐号状态 */
	private Integer accountStatusNo;

	/**修改密码时的旧密码**/
	private String oldPwd;
	/**帐号对应的企业实体类**/
	private SysOrg org;
	/**帐号绑定的用户实体类**/
	private SysUser user;
	/**帐号授权角色列表信息**/
	private List<RoleWarehouse> list;
	private String statusName;

	/**@Fields 当前登录帐号所选定的仓库 */
	private MetaWarehouse warehouse;
	/**@Fields 仓库id */
	private String wareHouseId;
	/**@Fields 权限级别 */
	private int authLevel;
	/**@Fields 当前登录用户 */
	private Principal loginUser;

	public AccountVo(){}
	public AccountVo(SysAccount entity) {
		this.entity = entity;
		if(this.entity == null) return;
		ISysParamService paramService = SpringContextHolder.getBean(ISysParamService.class);
		this.statusName = paramService.getValue(CacheName.COMMON_STATUS, this.entity.getAccountStatus());
//		this.statusName = Constant.getStatus(this.entity.getAccountStatus());
	}

	@Override
	public Example getExample() {
		Example example = new Example(SysAccount.class);
		example.setOrderByClause(orderBy);
		if(entity == null) return example;
		Criteria c = example.createCriteria();
//		if (isLoginUser) {
//			// 获取当前用户信息
//			Principal loginUser = LoginUtil.getLoginUser();
//			if (loginUser == null) {
//				throw new BizException("valid_common_user_no_login");
//			}
//			c.andEqualTo("userId", loginUser.getUserId());
//		}
		if(StringUtils.isNoneBlank(accountNoLike)) {
			c.andLike("accountNo", StringUtil.likeEscapeH(accountNoLike));
		}
		if(StringUtils.isNoneBlank(accountNameLike)) {
			c.andLike("accountName", StringUtil.likeEscapeH(accountNameLike));
		}
		if(StringUtils.isNoneBlank(phoneLike)) {
			c.andLike("phone", StringUtil.likeEscapeH(phoneLike));
		}
		if(StringUtils.isNoneBlank(entity.getAccountNo())) {
			c.andEqualTo("accountNo", entity.getAccountNo());
		}
		if(StringUtils.isNoneBlank(entity.getAccountName())) {
			c.andEqualTo("accountName", entity.getAccountName());
		}
		if(StringUtils.isNoneBlank(entity.getUserId())) {
			c.andEqualTo("userId", entity.getUserId());
		}
		if(entity.getAccountStatus() != null) {
			c.andEqualTo("accountStatus", entity.getAccountStatus());
		}
		if(accountStatusNo != null) {
			c.andNotEqualTo("accountStatus", accountStatusNo);
		}
		c.andEqualTo("orgId", entity.getOrgId());
		return example;
	}

	public SysAccount getEntity() {
		return entity;
	}
	public AccountVo orderByUpdateTimeDesc() {
		this.orderBy = "update_time desc ";
		return this;
	}

	public List<RoleWarehouse> getList() {
		return list;
	}

	public AccountVo setList(List<RoleWarehouse> list) {
		this.list = list;
		return this;
	}

	public SysUser getUser() {
		return user;
	}

	public AccountVo setUser(SysUser user) {
		this.user = user;
		return this;
	}

	public SysOrg getOrg() {
		return org;
	}

	public AccountVo setOrg(SysOrg org) {
		this.org = org;
		return this;
	}

	public String getOldPwd() {
		return oldPwd;
	}

	public AccountVo setOldPwd(String oldPwd) {
		this.oldPwd = oldPwd;
		return this;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setAccountNoLike(String accountNoLike) {
		this.accountNoLike = accountNoLike;
	}
	public void setAccountNameLike(String accountNameLike) {
		this.accountNameLike = accountNameLike;
	}

	/**
	 * 属性 phoneLike getter方法
	 * @return 属性phoneLike
	 * @author 王通<br/>
	 */
	public String getPhoneLike() {
		return phoneLike;
	}
	/**
	 * 属性 phoneLike setter方法
	 * @param phoneLike 设置属性phoneLike的值
	 * @author 王通<br/>
	 */
	public void setPhoneLike(String phoneLike) {
		this.phoneLike = phoneLike;
	}
	/**
	 * 属性 isLoginUser getter方法
	 * @return 属性isLoginUser
	 * @author 王通<br/>
	 */
	public Boolean getIsLoginUser() {
		return isLoginUser;
	}
	/**
	 * 属性 isLoginUser setter方法
	 * @param isLoginUser 设置属性isLoginUser的值
	 * @author 王通<br/>
	 */
	public void setIsLoginUser(Boolean isLoginUser) {
		this.isLoginUser = isLoginUser;
	}
	public String getWareHouseId() {
		return wareHouseId;
	}
	public AccountVo setWareHouseId(String wareHouseId) {
		this.wareHouseId = wareHouseId;
		return this;
	}
	public String getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	public MetaWarehouse getWarehouse() {
		return warehouse;
	}
	public AccountVo setWarehouse(MetaWarehouse warehouse) {
		this.warehouse = warehouse;
		return this;
	}
	public Principal getLoginUser() {
		return loginUser;
	}
	public AccountVo setLoginUser(Principal loginUser) {
		this.loginUser = loginUser;
		return this;
	}
	public int getAuthLevel() {
		return authLevel;
	}
	public AccountVo setAuthLevel(int authLevel) {
		this.authLevel = authLevel;
		return this;
	}
	public Integer getAccountStatusNo() {
		return accountStatusNo;
	}
	public AccountVo setAccountStatusNo(Integer accountStatusNo) {
		this.accountStatusNo = accountStatusNo;
		return this;
	}
}