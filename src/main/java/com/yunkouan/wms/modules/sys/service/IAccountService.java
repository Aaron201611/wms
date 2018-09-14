package com.yunkouan.wms.modules.sys.service;

import java.util.List;

import com.yunkouan.entity.ResultModel;
import com.yunkouan.exception.DaoException;
import com.yunkouan.exception.ServiceException;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.saas.modules.sys.entity.SysAuth;
import com.yunkouan.wms.modules.sys.entity.SysAccount;
import com.yunkouan.wms.modules.sys.vo.AccountVo;

/**
 * 企业帐户服务接口
 * @author tphe06 2017年2月14日
 */
public interface IAccountService {
    /**
     * 帐号列表数据查询
     * @param vo 
     * @param page 
     * @return
     */
    public ResultModel list(AccountVo vo, Principal p) throws DaoException, ServiceException;

    /**
     * 查询帐号详情
     * @param id 
     * @return
     * @throws Exception 
     */
    public ResultModel view(String id, Principal p) throws DaoException, ServiceException, Exception;
    /**
     * 查询帐号详情
     * @param id 
     * @return
     */
    public SysAccount get(String id);
    /**
     * 根据帐户编号和组织id查询帐户和用户信息
     * @param vo
     * @return
     */
    public AccountVo query(SysAccount vo);
    /**
     * 根据帐号id查询该帐号的所有授权信息
     */
    public List<SysAuth> query(String id, String warehouseId);
    public List<SysAuth> query4park(String id);
    public AccountVo load(String id) throws DaoException, ServiceException;

    /**
     * 添加帐号
     * @param vo 
     * @return
     * @throws Exception 
     */
    public ResultModel add(AccountVo vo, Principal p) throws DaoException, ServiceException, Exception;

    /**
     * 修改帐号
     * @param vo 
     * @return
     * @throws Exception 
     */
    public ResultModel update(AccountVo vo, Principal p) throws DaoException, ServiceException, Exception;
    /**
     * 修改帐号登录密码
     * @param vo
     * @return
     */
    public void updatePwd(AccountVo vo) throws DaoException, ServiceException;

    /**
     * 生效帐号
     * @param id 
     * @return
     */
    public ResultModel enable(String id) throws DaoException, ServiceException;

    /**
     * 失效帐号
     * @param id 
     * @return
     */
    public ResultModel disable(String id) throws DaoException, ServiceException;

    /**
     * 取消帐号
     * @param id 
     * @return
     */
    public ResultModel cancel(String id) throws DaoException, ServiceException;

	/**
	 * @param vo
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年6月21日 上午10:36:42<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
	public ResultModel saveAndEnable(AccountVo vo) throws DaoException, ServiceException, Exception;
}