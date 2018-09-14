package com.yunkouan.wms.modules.meta.service;

import com.yunkouan.entity.ResultModel;
import com.yunkouan.exception.DaoException;
import com.yunkouan.exception.ServiceException;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.modules.meta.vo.MachineVo;

/**
 * 设备服务接口
 */
public interface IMachineService {
    /**
     * 设备列表数据查询
     */
    public ResultModel list(MachineVo vo, Principal p) throws DaoException, ServiceException;

    /**
     * 查看设备详情
     */
    public ResultModel view(String id, Principal p) throws DaoException, ServiceException;

    /**
     * 添加设备
     */
    public ResultModel add(MachineVo vo, Principal p) throws DaoException, ServiceException;

    /**
     * 修改设备
     */
    public ResultModel update(MachineVo vo, Principal p) throws DaoException, ServiceException;

    /**
     * 生效设备
     */
    public ResultModel enable(String id) throws DaoException, ServiceException;

    /**
     * 失效设备
     */
    public ResultModel disable(String id) throws DaoException, ServiceException;

    /**
     * 取消设备
     */
    public ResultModel cancel(String id) throws DaoException, ServiceException;
}