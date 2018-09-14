package com.yunkouan.wms.modules.meta.service;

import com.yunkouan.entity.ResultModel;
import com.yunkouan.exception.DaoException;
import com.yunkouan.exception.ServiceException;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.modules.meta.vo.WorkAreaVo;

/**
 * 工作区服务接口
 */
public interface IWorkAreaService {
    /**
     * 工作区列表数据查询
     */
    public ResultModel list(WorkAreaVo vo, Principal p) throws DaoException, ServiceException;

    /**
     * 查看工作区详情
     */
    public ResultModel view(String id, Principal p) throws DaoException, ServiceException;

    /**
     * 添加工作区
     */
    public ResultModel add(WorkAreaVo vo, Principal p) throws DaoException, ServiceException;

    /**
     * 修改工作区
     */
    public ResultModel update(WorkAreaVo vo, Principal p) throws DaoException, ServiceException;

    /**
     * 生效工作区
     */
    public ResultModel enable(String id) throws DaoException, ServiceException;

    /**
     * 失效工作区
     */
    public ResultModel disable(String id) throws DaoException, ServiceException;

    /**
     * 取消工作区
     */
    public ResultModel cancel(String id) throws DaoException, ServiceException;
}