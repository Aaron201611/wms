package com.yunkouan.wms.modules.sys.service;

import java.util.*;
import com.yunkouan.wms.modules.sys.entity.SysInterfaces;

/**
 * 系统接口服务接口
 */
public interface InterfacesService {

    /**
     * 系统接口列表数据查询
     * @param interf 
     * @param page 
     * @return
     */
    public Set<SysInterfaces> list(SysInterfaces interf);

    /**
     * 查看接口详情
     * @param id 
     * @return
     */
    public SysInterfaces view(String id);

    /**
     * 取消报文发送
     * @param id 
     * @return
     */
    public boolean cancel(String id);

    /**
     * 对失败报文重新进行解析
     * @param interf 
     * @return
     */
    public boolean reanalyze(SysInterfaces interf);

    /**
     * 对失败报文重新进行发送
     * @param interf 
     * @return
     */
    public boolean resend(SysInterfaces interf);

}