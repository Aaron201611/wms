package com.yunkouan.wms.modules.inv.service;

import java.util.List;

import com.yunkouan.wms.modules.inv.vo.InvRejectsDestoryDetailVO;

/**
 * 移位业务接口
 */
public interface IRejectsDestoryDetailService {

	List<InvRejectsDestoryDetailVO> listByParentId(String id) throws Exception;

}