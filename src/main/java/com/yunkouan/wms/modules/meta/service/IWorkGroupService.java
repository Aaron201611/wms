package com.yunkouan.wms.modules.meta.service;

import com.yunkouan.entity.ResultModel;
import com.yunkouan.exception.DaoException;
import com.yunkouan.exception.ServiceException;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.modules.meta.vo.WorkGroupVo;

/**
 * 班组服务接口
 */
public interface IWorkGroupService {
    /**
     * 班组列表数据查询
     */
    public ResultModel list(WorkGroupVo vo, Principal p) throws DaoException, ServiceException;

    /**
     * 查看班组详情
     */
    public ResultModel view(String id, Principal p) throws DaoException, ServiceException;

    /**
     * 添加班组
     */
    public ResultModel add(WorkGroupVo vo, Principal p) throws DaoException, ServiceException;

    /**
     * 修改班组
     */
    public ResultModel update(WorkGroupVo vo, Principal p) throws DaoException, ServiceException;

    /**
     * 生效班组
     */
    public ResultModel enable(String id) throws DaoException, ServiceException;

    /**
     * 失效班组
     */
    public ResultModel disable(String id) throws DaoException, ServiceException;

    /**
     * 取消班组
     */
    public ResultModel cancel(String id) throws DaoException, ServiceException;
}