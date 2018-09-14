package com.yunkouan.wms.modules.sys.service.impl;

import java.util.*;
import com.yunkouan.wms.modules.sys.dao.InterfacesDao;
import com.yunkouan.wms.modules.sys.entity.SysInterfaces;
import com.yunkouan.wms.modules.sys.service.InterfacesService;

/**
 * 系统接口服务实现类
 */
public class InterfacesServiceImpl implements InterfacesService {

    /**
     * Default constructor
     */
    public InterfacesServiceImpl() {
    }

    /**
     * 
     */
    private InterfacesDao dao;

    /**
     * 系统接口列表数据查询
     * @param interf 
     * @param page 
     * @return
     */
    public Set<SysInterfaces> list(SysInterfaces interf) {
        // TODO implement here
        return null;
    }

    /**
     * 查看接口详情
     * @param id 
     * @return
     */
    public SysInterfaces view(String id) {
        // TODO implement here
        return null;
    }

    /**
     * 取消报文发送
     * @param id 
     * @return
     */
    public boolean cancel(String id) {
        // TODO implement here
        return false;
    }

    /**
     * 对失败报文重新进行解析
     * @param interf 
     * @return
     */
    public boolean reanalyze(SysInterfaces interf) {
        // TODO implement here
        return false;
    }

    /**
     * 对失败报文重新进行发送
     * @param interf 
     * @return
     */
    public boolean resend(SysInterfaces interf) {
        // TODO implement here
        return false;
    }

}