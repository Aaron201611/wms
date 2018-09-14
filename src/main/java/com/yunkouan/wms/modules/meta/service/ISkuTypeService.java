package com.yunkouan.wms.modules.meta.service;

import java.util.List;

import com.yunkouan.entity.ResultModel;
import com.yunkouan.exception.BizException;
import com.yunkouan.exception.DaoException;
import com.yunkouan.exception.ServiceException;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.modules.meta.entity.MetaSkuType;
import com.yunkouan.wms.modules.meta.vo.SkuTypeVo;

/**
 * 货品类型服务接口
 * @author tphe06 2017年2月14日
 */
public interface ISkuTypeService {
    /**
     * 货品类型列表数据查询
     * @param vo 
     * @param page 
     * @return
     */
    public ResultModel list(SkuTypeVo vo, Principal p) throws ServiceException, DaoException;
    /**
     * 顶级货品类型列表数据查询（用于货品类型下拉选择框）
     * @param vo 
     * @param page 
     * @return
     */
    public ResultModel toplist(Principal p) throws ServiceException, DaoException;
    /**
     * 货品类型列表数据查询（用于货品类型下拉选择框）
     * @param vo 
     * @param page 
     * @return
     */
    public ResultModel selectList(SkuTypeVo vo, Principal p) throws ServiceException, DaoException;

    /**
     * 查询货品类型详情
     * @param id 
     * @return
     */
    public ResultModel view(String id, Principal p) throws ServiceException, DaoException;
    /**
    * @Description: 根据货品类型主键查询货品类型详情
    * @param id
    * @return
    * @throws ServiceException
    * @throws DaoException
    */
    public MetaSkuType get(String id) throws ServiceException, DaoException;
    /**
    * @Description: 根据货品类型信息查询货品类型详情
    * @param id
    * @return
    * @throws ServiceException
    * @throws DaoException
    */
    public MetaSkuType get(MetaSkuType id) throws ServiceException, DaoException;

    /**
     * 添加货品类型
     * @param vo 
     * @return
     */
    public ResultModel add(SkuTypeVo vo, Principal p) throws ServiceException, DaoException;

    /**
     * 修改货品类型
     * @param vo 
     * @return
     */
    public ResultModel update(SkuTypeVo vo, Principal p) throws ServiceException, DaoException;

    /**
     * 生效货品类型
     * @param idList 
     * @return
     */
    public ResultModel enable(List<String> idList) throws ServiceException, DaoException;

    /**
     * 失效货品类型
     * @param id 
     * @return
     */
    public ResultModel disable(List<String> idList) throws ServiceException, DaoException;

    /**
     * 取消货品类型
     * @param idList 
     * @return
     */
    public ResultModel cancel(List<String> idList) throws ServiceException, DaoException;

	/**
	 * @param vo
	 * @return
	 * @throws DaoException
	 * @throws ServiceException
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年6月19日 下午3:06:32<br/>
	 * @author 王通<br/>
	 */
	public ResultModel saveAndEnable(SkuTypeVo vo) throws DaoException, ServiceException;
	/**
	 * @param skuTypeName$
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年9月16日 上午11:12:27<br/>
	 * @author 王通<br/>
	 */
	public MetaSkuType selectChild(String skuTypeName$,Integer skuTypeStatus) throws BizException;
}