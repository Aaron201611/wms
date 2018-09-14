package com.yunkouan.wms.modules.meta.service;

import java.util.List;

import com.yunkouan.entity.ResultModel;
import com.yunkouan.exception.DaoException;
import com.yunkouan.exception.ServiceException;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.modules.meta.entity.MetaPack;
import com.yunkouan.wms.modules.meta.vo.PackVo;

/**
 * 包装服务接口
 * @author tphe06 2017年2月14日
 */
public interface IPackService {
    /**
     * 包装列表数据查询
     * @param vo 
     * @param page 
     * @return
     */
    public ResultModel list(PackVo vo, Principal p) throws DaoException, ServiceException;

    /**
     * 查询包装详情
     * @param id 
     * @return
     */
    public ResultModel view(String id, Principal p) throws DaoException, ServiceException;
    /**
     * 查询包装详情
     * @param id 
     * @return
     */
    public MetaPack get(String id);

    /**
     * 添加包装
     * @param vo 
     * @return
     */
    public ResultModel add(PackVo vo, Principal p) throws DaoException, ServiceException;

    /**
     * 修改包装
     * @param vo 
     * @return
     */
    public ResultModel update(PackVo vo, Principal p) throws DaoException, ServiceException;

    /**
     * 生效包装
     * @param id 
     * @return
     */
    public ResultModel enable(List<String> idList) throws DaoException, ServiceException;

    /**
     * 失效包装
     * @param id 
     * @return
     */
    public ResultModel disable(List<String> idList) throws DaoException, ServiceException;

    /**
     * 取消包装
     * @param idList 
     * @return
     */
    public ResultModel cancel(List<String> idList) throws DaoException, ServiceException;

	/**
	 * @param vo
	 * @return
	 * @throws DaoException
	 * @throws ServiceException
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年6月19日 下午3:10:41<br/>
	 * @author 王通<br/>
	 */
    public ResultModel saveAndEnable(PackVo vo) throws DaoException, ServiceException;
}