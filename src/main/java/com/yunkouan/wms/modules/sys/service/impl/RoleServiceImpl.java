package com.yunkouan.wms.modules.sys.service.impl;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yunkouan.entity.ResultModel;
import com.yunkouan.exception.DaoException;
import com.yunkouan.exception.ServiceException;
import com.yunkouan.saas.modules.sys.entity.SysOrg;
import com.yunkouan.saas.modules.sys.service.IAuthService;
import com.yunkouan.saas.modules.sys.service.IOrgService;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.common.constant.ErrorCode;
import com.yunkouan.wms.common.service.BaseService;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.IdUtil;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.sys.dao.IRoleAuthDao;
import com.yunkouan.wms.modules.sys.dao.IRoleDao;
import com.yunkouan.wms.modules.sys.entity.SysRole;
import com.yunkouan.wms.modules.sys.entity.SysRoleAuth;
import com.yunkouan.wms.modules.sys.service.IRoleService;
import com.yunkouan.wms.modules.sys.vo.RoleAuthVo;
import com.yunkouan.wms.modules.sys.vo.RoleVo;

/**
 * 角色服务实现类
 * @author tphe06 2017年2月14日
 */
@Service
@Transactional(readOnly=true)
public class RoleServiceImpl extends BaseService implements IRoleService {
    /**角色服务数据层接口*/
	@Autowired
    private IRoleDao dao;
    /**角色授权数据层接口*/
	@Autowired
    private IRoleAuthDao roleAuthDao;
	/**企业服务接口*/
	@Autowired
    private IOrgService orgService;

    /**
     * 角色列表数据查询
     * @param vo 
     * @param page 
     * @return
     */
    public ResultModel list(RoleVo vo, Principal p) throws DaoException, ServiceException {
    	if(vo.getEntity() == null || vo.getEntity().getRoleStatus() == null) vo.setRoleStatusNo(Constant.STATUS_CANCEL);
    	if(vo.getEntity() != null) vo.getEntity().setOrgId(p.getOrgId());
		Page<SysRole> page = PageHelper.startPage(vo.getCurrentPage()+1, vo.getPageSize());
    	List<SysRole> list = dao.selectByExample(vo.getExample());
    	List<RoleVo> r = new ArrayList<RoleVo>();
    	if(list != null) for(int i=0; i<list.size(); ++i) {
    		r.add(new RoleVo(list.get(i)));
    	}
    	return new ResultModel().setPage(page).setList(r);
    }

    /**
     * 查询角色详情
     * @param id 
     * @return
     * @throws ServiceException 
     */
    public ResultModel view(String id, Principal p) throws DaoException, ServiceException {
    	/**查询角色详情**/
    	SysRole entity =  dao.selectByPrimaryKey(id);
    	if(entity == null) throw new ServiceException(ErrorCode.DATA_EMPTY);
    	RoleVo vo = new RoleVo(entity);
    	vo.setOrg(orgService.query(new SysOrg().setOrgId(entity.getOrgId())));
    	//查询角色授权信息
    	vo.setList(queryChilds(id, p));
        return new ResultModel().setObj(vo);
    }
	/**
	* @Description: 查询【该企业】【该角色】授权权限数据
	* @param a
	* @return
	* @throws DaoException
	* @throws ServiceException
	 */
	private List<RoleAuthVo> queryChilds(String roleid, Principal p) throws DaoException, ServiceException {
		List<RoleAuthVo> list = all(p);
		List<SysRoleAuth> selects = query(new SysRoleAuth().setRoleId(roleid).setOrgId(p.getOrgId()));
		//遍历树形结构的权限数据，把有权限的打上标识
		exists(list, selects);
		return list;
	}
	/**
	* @Description: 遍历树形结构的权限数据，把有权限的打上标识
	* @param list
	* @param selects
	*/
	private static void exists(List<RoleAuthVo> list, List<SysRoleAuth> selects) {
		if(list == null || selects == null) return;
		if(list.size() == 0 || selects.size() == 0) return;
		for(int i=0; i<list.size(); ++i) {
			RoleAuthVo vo = list.get(i);
			if(exists(selects, vo)) vo.setSelectStatus(true);
			exists(vo.getList(), selects);
		}
	}
	/**
	* @Description: 判断列表中是否存在该节点
	* @param list
	* @param vo
	* @return
	*/
	private static boolean exists(List<SysRoleAuth> list, RoleAuthVo vo) {
		if(list == null || vo == null || vo.getEntity() == null) return false;
		for(int i=0; i<list.size(); ++i) {
			if(list.get(i).getAuthId().equals(vo.getEntity().getAuthId())) return true;
		}
		return false;
	}
	/**
	* @Description: 按照树形结构查询权限列表（限企业普通用户级）
	* @param vo
	* @return
	* @throws Exception
	*/
	private List<RoleAuthVo> all(Principal p) throws DaoException, ServiceException {
		//查询根节点（根节点无需限制权限级别）
		SysRoleAuth entity = new SysRoleAuth().setOrgId(p.getOrgId())
			.setParentId(IAuthService.ROOT).setAuthStatus(Constant.STATUS_ACTIVE)
			.setAuthLevelMin(p.getAuthLevel());
		List<SysRoleAuth> list = roleAuthDao.query(entity);
		RoleAuthVo vo = new RoleAuthVo();
		vo.setList(chg(list));
		tree1(vo, p);
		return vo.getList();
	}
	/**
	* @Description: 查找所有树形结构的权限数据
	* @param vo
	*/
	private void tree1(RoleAuthVo vo, Principal p) {
		if(vo.getList() == null || vo.getList().size() == 0) return;
		for(int i=0; i<vo.getList().size(); ++i) {
			RoleAuthVo c = vo.getList().get(i);
			//非根节点需要限制权限级别
			SysRoleAuth entity = new SysRoleAuth().setOrgId(p.getOrgId())
				.setParentId(c.getEntity().getAuthId()).setAuthStatus(Constant.STATUS_ACTIVE)
				.setAuthLevelMin(p.getAuthLevel());
			List<SysRoleAuth> list = roleAuthDao.query(entity);
			c.setList(chg(list));
			tree1(c, p);
		}
	}
	/**
	* @Description: 把权限实体列表转化成权限数据传输对象列表
	* @param list
	* @return
	*/
	private static List<RoleAuthVo> chg(List<SysRoleAuth> list) {
		if(list == null) return null;
		List<RoleAuthVo> r = new ArrayList<RoleAuthVo>();
		for(int i=0; i<list.size(); ++i) {
			r.add(new RoleAuthVo(list.get(i)));
		}
		return r;
	}


    /**
     * 添加角色
     * @param vo 
     * @return
     * @throws ServiceException 
     * @throws Exception 
     */
    @Transactional(readOnly=false, rollbackFor=Exception.class)
    public ResultModel add(RoleVo vo, Principal p) throws DaoException, ServiceException {
    	/**新增角色数据**/
    	SysRole entity = vo.getEntity();
    	if(entity == null) throw new ServiceException(ErrorCode.DATA_EMPTY);
    	SysRole old = dao.selectOne(new SysRole().setRoleNo(entity.getRoleNo()).setOrgId(p.getOrgId()));
    	if(old != null) throw new ServiceException(ErrorCode.NO_NOT_UNIQUE);
    	entity.clear();
    	entity.setRoleStatus(Constant.STATUS_OPEN);
    	String roleid = IdUtil.getUUID();
    	entity.setRoleId(roleid);
    	entity.setOrgId(p.getOrgId());
    	entity.setCreatePerson(p.getUserId());
    	entity.setRoleId2(context.getStrategy4Id().getRoleSeq());
    	int r = dao.insertSelective(entity);
    	if(r == 0) throw new DaoException(ErrorCode.DB_EXCEPTION);
        /**批量新增角色授权数据**/
    	List<RoleAuthVo> list = vo.getList();
    	tree(list, roleid, p);
        /**返回角色数据**/
        return view(roleid, p);
    }
    /**
    * @Description: 批量添加树形结构的授权权限
    * @param list
    * @param orgid
    * @param p
    * @throws DaoException
    */
    private void tree(List<RoleAuthVo> list, String roleid, Principal p) throws DaoException {
    	if(list == null || list.size() == 0) return;
    	for(int i=0; i<list.size(); ++i) {
    		RoleAuthVo v = list.get(i);
    		if(v.getSelectStatus() == null || !v.getSelectStatus() 
    			|| v.getEntity() == null || StringUtil.isTrimEmpty(v.getEntity().getOrgAuthId())) continue;
        	SysRoleAuth obj = new SysRoleAuth();
        	obj.setRoleAuthId(IdUtil.getUUID());
        	obj.setOrgAuthId(v.getEntity().getOrgAuthId());
        	obj.setOrgId(p.getOrgId());
        	obj.setRoleId(roleid);
        	obj.setCreatePerson(p.getUserId());
        	obj.setRoleAuthId2(context.getStrategy4Id().getRoleAuthSeq());
        	int r = roleAuthDao.insertSelective(obj);
        	if(r == 0) throw new DaoException(ErrorCode.DB_EXCEPTION);
    		tree(v.getList(), roleid, p);
    	}
    }


    /**
     * 修改角色
     * @param vo 
     * @return
     * @throws ServiceException 
     */
    @Transactional(readOnly=false, rollbackFor=Exception.class)
    public ResultModel update(RoleVo vo, Principal p) throws DaoException, ServiceException {
    	/**修改角色**/
    	SysRole entity = vo.getEntity();
    	if(entity == null) throw new ServiceException(ErrorCode.DATA_EMPTY);
    	entity.clear();
    	SysRole old = dao.selectOne(new SysRole().setRoleNo(entity.getRoleNo()).setOrgId(p.getOrgId()));
    	if(old != null && !old.getRoleId().equals(entity.getRoleId())) throw new ServiceException(ErrorCode.NO_NOT_UNIQUE);
    	entity.setUpdatePerson(p.getUserId());
        int r = dao.updateByPrimaryKeySelective(entity);
        if(r == 0) throw new DaoException(ErrorCode.DB_EXCEPTION);
        /**修改角色授权，先删除然后新增**/
        roleAuthDao.deleteByExample(new RoleAuthVo(new SysRoleAuth().setRoleId(entity.getRoleId())).getExample());
    	List<RoleAuthVo> list = vo.getList();
    	tree(list, entity.getRoleId(), p);
        /**查询角色**/
        return view(entity.getRoleId(), p);
    }

    /**
     * 生效角色
     * @param id 
     * @return
     */
    @Transactional(readOnly=false, rollbackFor=Exception.class)
    public ResultModel enable(String id) throws DaoException, ServiceException {
    	ResultModel m = new ResultModel();
    	SysRole old =  dao.selectByPrimaryKey(id);
    	if(old == null) throw new ServiceException(ErrorCode.DATA_EMPTY);
    	if(old.getRoleStatus() != Constant.STATUS_OPEN) throw new ServiceException(ErrorCode.STATUS_NO_RIGHT);
    	SysRole obj = new SysRole();
    	obj.setRoleId(id);
    	obj.setRoleStatus(Constant.STATUS_ACTIVE);
        int r = dao.updateByPrimaryKeySelective(obj);
        if(r == 0) throw new DaoException(ErrorCode.DB_EXCEPTION);
        return m;
    }

    /**
     * 失效角色
     * @param id 
     * @return
     */
    @Transactional(readOnly=false, rollbackFor=Exception.class)
    public ResultModel disable(String id) throws DaoException, ServiceException {
    	ResultModel m = new ResultModel();
    	SysRole old =  dao.selectByPrimaryKey(id);
    	if(old == null) throw new ServiceException(ErrorCode.DATA_EMPTY);
    	if(old.getRoleStatus() != Constant.STATUS_ACTIVE) throw new ServiceException(ErrorCode.STATUS_NO_RIGHT);
    	SysRole obj = new SysRole();
    	obj.setRoleId(id);
    	obj.setRoleStatus(Constant.STATUS_OPEN);
        int r = dao.updateByPrimaryKeySelective(obj);
        if(r == 0) throw new DaoException(ErrorCode.DB_EXCEPTION);
        return m;
    }

    /**
     * 取消角色
     */
    @Transactional(readOnly=false, rollbackFor=Exception.class)
	public ResultModel cancel(String id) throws DaoException, ServiceException {
    	ResultModel m = new ResultModel();
    	SysRole old =  dao.selectByPrimaryKey(id);
    	if(old == null) throw new ServiceException(ErrorCode.DATA_EMPTY);
    	if(old.getRoleStatus() != Constant.STATUS_OPEN) throw new ServiceException(ErrorCode.STATUS_NO_RIGHT);
    	SysRole obj = new SysRole();
    	obj.setRoleId(id);
    	obj.setRoleStatus(Constant.STATUS_CANCEL);
        int r = dao.updateByPrimaryKeySelective(obj);
        if(r == 0) throw new DaoException(ErrorCode.DB_EXCEPTION);
        return m;
	}

	/**
	* @Description: 按照树形结构查询所有权限列表
	* @return
	* @throws DaoException
	* @throws ServiceException
	 */
	@Override
	public ResultModel tree(Principal p) throws DaoException, ServiceException {
		List<RoleAuthVo> list = all(p);
		return new ResultModel().setList(list);
	}

	/**
	* @Description: 角色授权列表查询
	* @param entity
	* @return
	 */
	@Override
	public List<SysRoleAuth> query(SysRoleAuth entity) {
		return roleAuthDao.list(entity);
	}

	/**
	 * @param vo
	 * @return
	 * @throws DaoException
	 * @throws ServiceException
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年6月19日 下午2:57:02<br/>
	 * @author 王通<br/>
	 */
	@Override
    @Transactional(readOnly=false, rollbackFor=Exception.class)
	public ResultModel saveAndEnable(RoleVo vo) throws DaoException, ServiceException {
    	Principal p = LoginUtil.getLoginUser();
    	ResultModel r = null;
		if (StringUtil.isTrimEmpty(vo.getEntity().getRoleId())) {
			r = this.add(vo, p);
			String skuId = vo.getEntity().getRoleId();
			this.enable(skuId);
		} else {
			r = this.update(vo, p);
			String skuId = vo.getEntity().getRoleId();
			this.enable(skuId);
		}
		return r;
	}
}