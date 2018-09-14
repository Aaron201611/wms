package com.yunkouan.wms.modules.sys.service;

import java.util.List;

import com.yunkouan.wms.modules.sys.vo.EventVO;

/**
 * 主页业务接口
 */
public interface IHomepageService {


    /**
     * 列出待办事项列表
     * @param shiftVO 
     * @param page 
     * @return
     */
    public EventVO list() throws Exception ;
    /**
     * 列出所有仓库的代办事项
     * @return
     * @throws Exception
     */
    public List<EventVO> warehouseTasks() throws Exception;
}