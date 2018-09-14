package com.yunkouan.wms.modules.sys.service;

import java.util.List;

import com.yunkouan.entity.ResultModel;
import com.yunkouan.exception.DaoException;
import com.yunkouan.exception.ServiceException;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.modules.sys.entity.SysRoleAuth;
import com.yunkouan.wms.modules.sys.vo.RoleVo;

/**
 * 角色服务接口
 * @author tphe06 2017年2月14日
 */
public interface IRoleService {
    /**
     * 角色列表数据查询
     * @param role 
     * @param page 
     * @return
     */
    public ResultModel list(RoleVo role, Principal p) throws DaoException, ServiceException;
    /**
    * @Description: 角色授权列表查询
    * @param entity
    * @return
    */
    public List<SysRoleAuth> query(SysRoleAuth entity);

    /**
     * 查询角色详情
     * @param id 
     * @return
     * @throws ServiceException 
     */
    public ResultModel view(String id, Principal p) throws DaoException, ServiceException;

    /**
     * 添加角色
     * @param vo 
     * @return
     * @throws DaoException 
     * @throws ServiceException 
     */
    public ResultModel add(RoleVo vo, Principal p) throws DaoException, ServiceException;

    /**
     * 修改角色
     * @param vo 
     * @return
     * @throws ServiceException 
     */
    public ResultModel update(RoleVo vo, Principal p) throws DaoException, ServiceException;

    /**
     * 生效角色
     * @param id 
     * @return
     */
    public ResultModel enable(String id) throws DaoException, ServiceException;

    /**
     * 角色失效
     * @param id 
     * @return
     */
    public ResultModel disable(String id) throws DaoException, ServiceException;

    /**
     * 失效角色
     * @param id 
     * @return
     */
    public ResultModel cancel(String id) throws DaoException, ServiceException;

    /**
    * @Description: 按照树形结构查询权限列表（限企业普通用户级）
    * @return
    * @throws DaoException
    * @throws ServiceException
    */
    public ResultModel tree(Principal p) throws DaoException, ServiceException;
	/**
	 * @param vo
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年6月21日 上午10:22:23<br/>
	 * @author 王通<br/>
	 */
	public ResultModel saveAndEnable(RoleVo vo) throws DaoException, ServiceException;
}