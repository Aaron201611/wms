package com.yunkouan.wms.modules.meta.service;

import java.util.List;

import com.yunkouan.entity.ResultModel;
import com.yunkouan.exception.DaoException;
import com.yunkouan.exception.ServiceException;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.modules.meta.entity.MetaMerchant;
import com.yunkouan.wms.modules.meta.vo.MerchantVo;

/**
 * 客商服务接口
 * @author tphe06 2017年2月14日
 */
public interface IMerchantService {
    /**
     * 客商列表数据查询
     * @param vo 
     * @param page 
     * @return
     */
    public ResultModel list(MerchantVo vo, Principal p) throws DaoException, ServiceException;
    
    /**
     * 客商列表数据查询
     * @param vo 
     * @param page 
     * @return
     */
    public List<MetaMerchant> listByParam(MerchantVo vo, Principal p) throws DaoException, ServiceException;
    /**
     * 根据客商名称模糊查询所有id
     */
    public List<String> list(String name);

    /**
     * 查询客商详情
     * @param id 
     * @return
     */
    public ResultModel view(String id, Principal p) throws DaoException, ServiceException;
    /**
     * 查询客商详情
     * @param id 
     * @return
     */
    public MetaMerchant get(String id);

    /**
     * 添加客商
     * @param vo 
     * @return
     */
    public ResultModel add(MerchantVo vo, Principal p) throws DaoException, ServiceException;
    /**
    * @Description: 添加客商并激活
    * @param vo
    * @return
    * @throws DaoException
    * @throws ServiceException
    */
    public ResultModel addAndEnable(MerchantVo vo, Principal p) throws DaoException, ServiceException;

    /**
     * 修改客商
     * @param vo 
     * @return
     */
    public ResultModel update(MerchantVo vo, Principal p) throws DaoException, ServiceException;
    /**
     * 修改并激活客商
     * @param vo 
     * @return
     */
    public ResultModel updateAndEnable(MerchantVo vo, Principal p) throws DaoException, ServiceException;

    /**
     * 生效客商
     * @param idList 
     * @return
     */
    public ResultModel enable(List<String> idList) throws DaoException, ServiceException;

    /**
     * 失效客商
     * @param idList 
     * @return
     */
    public ResultModel disable(List<String> idList) throws DaoException, ServiceException;

    /**
     * 取消客商
     * @param idList 
     * @return
     */
    public ResultModel cancel(List<String> idList) throws DaoException, ServiceException;
	/**
	 * @param merchantIdList
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年9月12日 上午10:06:15<br/>
	 * @author 王通<br/>
	 */
	public String concatMerchantShortName(List<String> merchantIdList);
	/**
	 * @param merchant
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年9月16日 上午10:17:20<br/>
	 * @author 王通<br/>
	 */
	public MetaMerchant view(MetaMerchant merchant);
	/**
	 * @param merchantName$
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年9月16日 上午10:25:54<br/>
	 * @author 王通<br/>
	 */
	public MetaMerchant selectOne(String merchantName$);
}