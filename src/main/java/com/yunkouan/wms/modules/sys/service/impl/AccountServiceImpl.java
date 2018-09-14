package com.yunkouan.wms.modules.sys.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yunkouan.entity.ResultModel;
import com.yunkouan.exception.DaoException;
import com.yunkouan.exception.ServiceException;
import com.yunkouan.saas.modules.sys.entity.MetaWarehouse;
import com.yunkouan.saas.modules.sys.entity.SysAuth;
import com.yunkouan.saas.modules.sys.entity.SysOrg;
import com.yunkouan.saas.modules.sys.entity.SysUser;
import com.yunkouan.saas.modules.sys.service.IOrgService;
import com.yunkouan.saas.modules.sys.service.IUserService;
import com.yunkouan.saas.modules.sys.service.IWarehouseExtlService;
import com.yunkouan.saas.modules.sys.vo.OrgVo;
import com.yunkouan.saas.modules.sys.vo.UserVo;
import com.yunkouan.util.PubUtil;
import com.yunkouan.util.StringUtil;
import com.yunkouan.util.UserUtil;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.common.constant.ErrorCode;
import com.yunkouan.wms.common.service.BaseService;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.IdUtil;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.sys.dao.IAccountDao;
import com.yunkouan.wms.modules.sys.dao.IAccountRoleDao;
import com.yunkouan.wms.modules.sys.dao.IRoleDao;
import com.yunkouan.wms.modules.sys.entity.SysAccount;
import com.yunkouan.wms.modules.sys.entity.SysAccountRole;
import com.yunkouan.wms.modules.sys.entity.SysRole;
import com.yunkouan.wms.modules.sys.service.IAccountService;
import com.yunkouan.wms.modules.sys.vo.AccQueryVo;
import com.yunkouan.wms.modules.sys.vo.AccountVo;
import com.yunkouan.wms.modules.sys.vo.RoleWarehouse;

/**
 * 企业帐号服务实现类
 * @author tphe06 2017年2月14日
 */
@Service
@Transactional(readOnly=true)
public class AccountServiceImpl extends BaseService implements IAccountService {
	/**企业帐号数据层接口*/
	@Autowired
	private IAccountDao dao;
	/**企业用户数据层接口**/
	@Autowired
	private IUserService service;
	/**企业帐号角色授权数据层接口**/
	@Autowired
	private IAccountRoleDao accountRoleDao;
	/**@Fields 仓库服务接口 */
	@Autowired
	private IWarehouseExtlService whService;
	@Autowired
	private IRoleDao roleDao;
	/**企业服务接口*/
	@Autowired
    private IOrgService orgService;

    /**
     * 企业帐号列表数据查询
     * @param vo 
     * @param page 
     * @return
     */
    public ResultModel list(AccountVo vo, Principal p) throws DaoException, ServiceException {
    	if(vo.getEntity() == null || vo.getEntity().getAccountStatus() == null) vo.setAccountStatusNo(Constant.STATUS_CANCEL);
    	if(vo.getEntity() != null) vo.getEntity().setOrgId(p.getOrgId());
		Page<SysAccount> page = PageHelper.startPage(vo.getCurrentPage()+1, vo.getPageSize());
    	List<SysAccount> list = dao.selectByExample(vo.getExample());
    	List<AccountVo> r = new ArrayList<AccountVo>();
    	if(list != null) for(int i=0; i<list.size(); ++i) {
    		r.add(chg(list.get(i)));
    	}
    	return new ResultModel().setPage(page).setList(r);
    }

    /**
     * 查询企业帐号详情
     * @param id 
     * @return
     * @throws Exception 
     */
    public ResultModel view(String id, Principal p) throws Exception {
    	/**查询企业帐号详情**/
    	SysAccount entity =  dao.selectByPrimaryKey(id);
    	if(entity == null) throw new ServiceException(ErrorCode.DATA_EMPTY);
    	AccountVo vo = chg(entity);
    	/**查询企业帐号授权角色列表**/
    	List<SysAccountRole> list = accountRoleDao.select(new SysAccountRole().setAccountId(id));
    	List<RoleWarehouse> roles = new ArrayList<RoleWarehouse>();
    	if(list != null) for(int i=0; i<list.size(); ++i) {
    		SysAccountRole ac = list.get(i);
    		SysRole r = roleDao.selectByPrimaryKey(ac.getRoleId());
    		RoleWarehouse rw = new RoleWarehouse();
    		BeanUtils.copyProperties(r, rw);
    		if(StringUtils.isNoneBlank(ac.getWarehouseId())) {
    			MetaWarehouse m = whService.findWareHouseById(ac.getWarehouseId());
    			if(m != null) {
    				rw.setWarehouseId(m.getWarehouseId());
    				rw.setWarehouseNo(m.getWarehouseNo());
    				rw.setWarehouseName(m.getWarehouseName());
    			}
    		}
    		roles.add(rw);
    	}
    	vo.setList(roles);
    	vo.setOrg(orgService.query(new SysOrg().setOrgId(entity.getOrgId())));
        return new ResultModel().setObj(vo);
    }
    /**
    * @Description: 把帐号实体类转成数据传输类，同时补充用户信息
    * @param entity
    * @return
    */
    private AccountVo chg(SysAccount entity) {
    	//不得把密码传到页面
//    	entity.setAccountPwd(null);
    	AccountVo vo = new AccountVo(entity);
    	//查询用户
    	if(StringUtils.isNoneBlank(entity.getUserId())) vo.setUser(service.get(entity.getUserId()));
    	return vo;
    }

    /**
     * 添加企业帐号
     * @param entity 
     * @return
     * @throws Exception 
     */
    @Transactional(readOnly=false, rollbackFor=Exception.class)
    public ResultModel add(AccountVo vo, Principal p) throws Exception {
    	return this.addFunc(vo, p, false);
    }
    
    private ResultModel addFunc(AccountVo vo, Principal p,  boolean isEnable) throws Exception {
    	SysAccount entity = vo.getEntity();
    	if(entity == null) throw new ServiceException(ErrorCode.DATA_EMPTY);
    	entity.clear();
    	//检查编号是否唯一
    	SysAccount old = dao.selectOne(new SysAccount().setAccountNo(entity.getAccountNo()).setOrgId(p.getOrgId()));
    	if(old != null) throw new ServiceException(ErrorCode.NO_NOT_UNIQUE);
    	//新增用户信息
    	SysUser user = vo.getUser();
    	if(user == null) user = new SysUser();
    	user.setOrgId(p.getOrgId());
    	user.setUserNo(entity.getAccountNo());
    	user.setUserName(entity.getAccountName());
    	user.setUserStatus(Constant.STATUS_ACTIVE);
    	user.setIsEmployee(3);
    	com.yunkouan.saas.common.vo.Principal p1 = new com.yunkouan.saas.common.vo.Principal();
    	BeanUtils.copyProperties(p, p1);
    	ResultModel m = service.add(new UserVo(user), p1);
    	if(m == null || m.getObj() == null) throw new DaoException(ErrorCode.DB_EXCEPTION);
    	UserVo u = (UserVo)m.getObj();
    	if(u.getEntity() == null || StringUtils.isBlank(u.getEntity().getUserId())) throw new DaoException(ErrorCode.DB_EXCEPTION);
    	/**新增企业帐号**/
    	String accountid = IdUtil.getUUID();
    	entity.setAccountId(accountid);
    	entity.setUserId(u.getEntity().getUserId());
    	if (isEnable) {
        	entity.setAccountStatus(Constant.STATUS_ACTIVE);
    		user.setUserStatus(Constant.STATUS_ACTIVE);
    	} else {
        	entity.setAccountStatus(Constant.STATUS_OPEN);
        	user.setUserStatus(Constant.STATUS_OPEN);
    	}
    	/**密码为【帐号uuid+密码】明文做SHA1加密**/
    	entity.setAccountPwd(UserUtil.entryptPassword(accountid.concat(entity.getAccountPwd())));
    	entity.setOrgId(p.getOrgId());
    	entity.setCreatePerson(p.getUserId());
    	entity.setAccountId2(context.getStrategy4Id().getAccSeq());
    	int r = dao.insertSelective(entity);
    	if(r == 0) throw new DaoException(ErrorCode.DB_EXCEPTION);
    	/**批量新增帐号授权**/
    	List<RoleWarehouse> list = vo.getList();
    	if(list != null) for(int i=0; i<list.size(); ++i) {
    		RoleWarehouse role = list.get(i);
    		if(role == null || StringUtils.isBlank(role.getRoleId())) continue;
//    		if(StringUtils.isBlank(role.getWarehouseId())) continue;
    		SysAccountRole obj = new SysAccountRole();
    		obj.setAccountId(accountid);
    		obj.setAccountRoleId(IdUtil.getUUID());
    		obj.setCreatePerson(p.getUserId());
    		obj.setOrgId(p.getOrgId());
    		obj.setRoleId(role.getRoleId());
    		obj.setUpdatePerson(p.getUserId());
    		obj.setWarehouseId(role.getWarehouseId());
    		obj.setUserRoleId2(context.getStrategy4Id().getAccRoleSeq());
    		r = accountRoleDao.insertSelective(obj);
    		if(r == 0) throw new DaoException(ErrorCode.DB_EXCEPTION);
    	}
    	/**返回企业帐号信息**/
        return view(accountid, p);
    }

    /**
     * 修改企业帐号
     * @param vo 
     * @return
     * @throws Exception 
     */
	@Transactional(readOnly=false, rollbackFor=Exception.class)
    public ResultModel update(AccountVo vo, Principal p) throws Exception {
		return this.updateFunc(vo, p, false);
	}
	
    private ResultModel updateFunc(AccountVo vo, Principal p, boolean isEnable) throws Exception {
    	SysAccount entity = vo.getEntity();
    	if(entity == null) throw new ServiceException(ErrorCode.DATA_EMPTY);
    	entity.clear();
    	if("******".equals(entity.getAccountPwd())) entity.setAccountPwd(null);
    	//检查编号是否唯一
    	SysAccount old = this.dao.selectOne(new SysAccount().setAccountNo(entity.getAccountNo()).setOrgId(p.getOrgId()));
    	if(old != null && !old.getAccountId().equals(entity.getAccountId())) throw new ServiceException(ErrorCode.NO_NOT_UNIQUE);
    	//修改用户信息
    	SysUser user = vo.getUser();
    	user.clear();
    	user.setOrgId(p.getOrgId());
    	user.setUserNo(entity.getAccountNo());
    	user.setUserName(entity.getAccountName());
    	if (isEnable) {
    		entity.setAccountStatus(Constant.STATUS_ACTIVE);
    		user.setUserStatus(Constant.STATUS_ACTIVE);
    	}
    	user.setUpdatePerson(p.getUserId());
    	com.yunkouan.saas.common.vo.Principal p1 = new com.yunkouan.saas.common.vo.Principal();
    	BeanUtils.copyProperties(p, p1);
    	ResultModel m = service.update(new UserVo(user), p1);
    	if(m == null || m.getObj() == null) throw new DaoException(ErrorCode.DB_EXCEPTION);
    	//修改企业帐号信息
    	/**密码为【帐号uuid+密码】明文做SHA1加密**/
    	if(StringUtils.isNoneBlank(entity.getAccountPwd()) 
//    			&& !StringUtil.isTrimEmpty(vo.getOldPwd()) 
    		) {
//    		String oldPwd = UserUtil.entryptPassword(old.getAccountId().concat(vo.getOldPwd()));
//    		if(!oldPwd.equals(old.getAccountPwd())) throw new ServiceException(ErrorCode.PWD_NOT_EQUAL);
    		entity.setAccountPwd(UserUtil.entryptPassword(entity.getAccountId().concat(entity.getAccountPwd())));
    	}
    	entity.setUpdatePerson(p.getUserId());
    	int r = dao.updateByPrimaryKeySelective(entity);
        if(r == 0) throw new DaoException(ErrorCode.DB_EXCEPTION);
        /**批量修改企业帐号授权角色数据，先删除后新增**/
        r = accountRoleDao.delete(new SysAccountRole().setAccountId(entity.getAccountId()));
    	List<RoleWarehouse> list = vo.getList();
    	if(list != null) for(int i=0; i<list.size(); ++i) {
    		RoleWarehouse role = list.get(i);
    		if(role == null || StringUtils.isBlank(role.getRoleId())) continue;
    		SysAccountRole obj = new SysAccountRole();
    		obj.setAccountId(entity.getAccountId());
    		obj.setAccountRoleId(IdUtil.getUUID());
    		obj.setCreatePerson(p.getUserId());
    		obj.setOrgId(p.getOrgId());
    		obj.setRoleId(role.getRoleId());
    		obj.setUpdatePerson(p.getUserId());
    		obj.setWarehouseId(role.getWarehouseId());
    		obj.setUserRoleId2(context.getStrategy4Id().getAccRoleSeq());
    		r = accountRoleDao.insertSelective(obj);
    		if(r == 0) throw new DaoException(ErrorCode.DB_EXCEPTION);
    	}
    	/**返回帐号信息**/
        return view(entity.getAccountId(), p);
    }

    /**
     * 生效企业帐号
     * @param id 
     * @return
     * @throws DaoException 
     * @throws ServiceException 
     */
    @Transactional(readOnly=false, rollbackFor=Exception.class)
    public ResultModel enable(String id) throws DaoException, ServiceException {
    	return this.enableFunc(id);
    }
    
    private ResultModel enableFunc(String id) throws DaoException, ServiceException {
    	ResultModel m = new ResultModel();
    	SysAccount old =  dao.selectByPrimaryKey(id);
    	if(old == null) throw new ServiceException(ErrorCode.DATA_EMPTY);
    	if(old.getAccountStatus() != Constant.STATUS_OPEN) throw new ServiceException(ErrorCode.STATUS_NO_RIGHT);
    	SysAccount obj = new SysAccount();
    	obj.setAccountId(id);
    	obj.setAccountStatus(Constant.STATUS_ACTIVE);
        int r = dao.updateByPrimaryKeySelective(obj);
        if(r == 0) throw new DaoException(ErrorCode.DB_EXCEPTION);
    	service.updateStatus(old.getUserId(), Constant.STATUS_ACTIVE);
    	return m;
    }

    /**
     * 失效企业帐号
     * @param id 
     * @return
     * @throws DaoException 
     * @throws ServiceException 
     */
    @Transactional(readOnly=false, rollbackFor=Exception.class)
    public ResultModel disable(String id) throws DaoException, ServiceException {
    	ResultModel m = new ResultModel();
    	SysAccount old =  dao.selectByPrimaryKey(id);
    	if(old == null) throw new ServiceException(ErrorCode.DATA_EMPTY);
    	if(old.getAccountStatus() != Constant.STATUS_ACTIVE) throw new ServiceException(ErrorCode.STATUS_NO_RIGHT);
    	SysAccount obj = new SysAccount();
    	obj.setAccountId(id);
    	obj.setAccountStatus(Constant.STATUS_OPEN);
        int r = dao.updateByPrimaryKeySelective(obj);
        if(r == 0) throw new DaoException(ErrorCode.DB_EXCEPTION);
    	service.updateStatus(old.getUserId(), Constant.STATUS_OPEN);
        return m;
    }

    /**
     * 取消企业帐号
     * @throws DaoException 
     * @throws ServiceException 
     */
    @Transactional(readOnly=false, rollbackFor=Exception.class)
	public ResultModel cancel(String id) throws DaoException, ServiceException {
    	ResultModel m = new ResultModel();
    	SysAccount old =  dao.selectByPrimaryKey(id);
    	if(old == null) throw new ServiceException(ErrorCode.DATA_EMPTY);
    	if(old.getAccountStatus() != Constant.STATUS_OPEN) throw new ServiceException(ErrorCode.STATUS_NO_RIGHT);
    	SysAccount obj = new SysAccount();
    	obj.setAccountId(id);
    	obj.setAccountStatus(Constant.STATUS_CANCEL);
        int r = dao.updateByPrimaryKeySelective(obj);
        if(r == 0) throw new DaoException(ErrorCode.DB_EXCEPTION);
    	service.updateStatus(old.getUserId(), Constant.STATUS_CANCEL);
        return m;
	}

	@Override
	public SysAccount get(String id) {
		return dao.selectByPrimaryKey(id);
	}

    /**
     * 根据帐户编号和组织id查询帐户和用户信息
     * @param account
     * @return
     */
	@Override
	public AccountVo query(SysAccount account) {
		SysAccount entity = dao.selectOne(account);
		if(entity == null) return null;
		AccountVo vo = new AccountVo(entity);
		SysUser user = service.get(entity.getUserId());
		if(user == null) return null;
    	/**查询企业帐号授权角色列表**/
    	List<SysAccountRole> list = accountRoleDao.select(new SysAccountRole().setAccountId(entity.getAccountId()));
    	List<RoleWarehouse> roles = new ArrayList<RoleWarehouse>();
    	if(list != null) for(int i=0; i<list.size(); ++i) {
    		SysAccountRole ac = list.get(i);
    		SysRole r = roleDao.selectOne(new SysRole().setRoleId(ac.getRoleId()).setRoleStatus(Constant.STATUS_ACTIVE));
    		if(r != null) {
	    		RoleWarehouse rw = new RoleWarehouse();
	    		BeanUtils.copyProperties(r, rw);
	    		roles.add(rw);
    		}
    	}
    	vo.setList(roles);
		vo.setUser(user);
		return vo;
	}

    /**
     * 根据帐号id查询该帐号的所有授权信息
     */
	@Override
	public List<SysAuth> query(String accountId, String warehouseId) {
		AccQueryVo vo = new AccQueryVo().setAccountId(accountId).setWarehouseId(warehouseId);
		return dao.query(vo);
	}
    /**
     * 根据帐号id查询该帐号的所有授权信息
     */
	@Override
	public List<SysAuth> query4park(String accountId) {
		AccQueryVo vo = new AccQueryVo().setAccountId(accountId);
		return dao.query4park(vo);
	}

	@SuppressWarnings("unchecked")
	@Override
	public AccountVo load(String id) throws DaoException, ServiceException {
		SysAccount entity = dao.selectByPrimaryKey(id);
//		entity.setAccountPwd("");
		AccountVo vo = new AccountVo(entity);
		SysUser user = service.get(entity.getUserId());
		vo.setUser(user);
		OrgVo p_org = new OrgVo(new SysOrg());
		p_org.getEntity().setOrgId(entity.getOrgId());
		ResultModel rmOrg = this.orgService.list(p_org);
		List<OrgVo> listOrg = rmOrg.getList();
		if ( !PubUtil.isEmpty(listOrg) ) {
			vo.setOrg(listOrg.get(0).getEntity());
		}
		List<SysRole> list = accountRoleDao.list(new SysAccountRole().setAccountId(id));
		vo.setList(chg(list));
		return vo;
	}
	private static List<RoleWarehouse> chg(List<SysRole> roles) {
		if(roles == null || roles.size() == 0) return null;
		List<RoleWarehouse> list = new ArrayList<RoleWarehouse>();
		for(int i=0; i<roles.size(); ++i) {
			SysRole r = roles.get(i);
			RoleWarehouse rw = new RoleWarehouse();
			BeanUtils.copyProperties(r, rw);
			list.add(rw);
		}
		return list;
	}

	@Override
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public void updatePwd(AccountVo vo) throws DaoException, ServiceException {
		/**查询当前登录帐号信息**/
    	Principal p = LoginUtil.getLoginUser();
    	SysAccount old = dao.selectByPrimaryKey(p.getAccountId());
    	if(old == null) throw new ServiceException(ErrorCode.DATA_NO_EXISTS);
    	/**修改企业帐号信息**/
    	SysAccount entity = new SysAccount();
    	entity.setAccountId(old.getAccountId());
    	/**如果新旧密码都不为空则修改密码**/
    	if(vo.getEntity() != null 
    		&& !StringUtils.isBlank(vo.getOldPwd()) 
    		&& !StringUtils.isBlank(vo.getEntity().getAccountPwd())) {
        	/**校验旧密码是否正确，密码为【帐号uuid+密码】明文做SHA1加密**/
        	String oldPwd = UserUtil.entryptPassword(old.getAccountId().concat(vo.getOldPwd()));
        	if(!oldPwd.equals(old.getAccountPwd())) throw new ServiceException(ErrorCode.PWD_NOT_EQUAL);
    		entity.setAccountPwd(UserUtil.entryptPassword(old.getAccountId().concat(vo.getEntity().getAccountPwd())));
    	}
    	entity.setUpdatePerson(p.getUserId());
    	entity.setUpdateTime(new Date());
    	entity.setOrgId(null);
    	if(vo.getEntity() != null) entity.setNote(vo.getEntity().getNote());
    	int r = dao.updateByPrimaryKeySelective(entity);
        if(r == 0) throw new DaoException(ErrorCode.DB_EXCEPTION);
        /**修改企业用户信息**/
        if(vo.getUser() != null && !StringUtils.isBlank(vo.getUser().getEmail())) {
	        SysUser user = new SysUser();
	        user.setUserId(old.getUserId());
	        user.setEmail(vo.getUser().getEmail());
	        service.update(user);
        }
    }

	/**
	 * @param vo
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年6月21日 上午10:37:04<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
	@Override
    @Transactional(readOnly=false, rollbackFor=Exception.class)
	public ResultModel saveAndEnable(AccountVo vo) throws Exception {
    	Principal p = LoginUtil.getLoginUser();
    	ResultModel r = null;
		if (StringUtil.isTrimEmpty(vo.getEntity().getAccountId())) {
			r = this.addFunc(vo, p, true);
		} else {
			r = this.updateFunc(vo, p, true);
		}
		return r;
	}
}