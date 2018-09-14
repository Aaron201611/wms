package com.yunkouan.wms.modules.meta.service;

import java.util.List;

import com.yunkouan.entity.ResultModel;
import com.yunkouan.exception.DaoException;
import com.yunkouan.exception.ServiceException;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.modules.meta.entity.MetaSkuMaterialBom;
import com.yunkouan.wms.modules.meta.vo.SkuMaterialBomVo;

/**
 * 货品辅材BOM服务接口
 * @author tphe06 2017年2月14日
 */
public interface ISkuMaterialBomService {
    /**
     * 货品辅材BOM列表数据查询（分页）
     * @param vo 
     * @param page 
     * @return
     */
    public ResultModel list(SkuMaterialBomVo vo, Principal p) throws DaoException, ServiceException;

    /**
     * 查询货品辅材BOM详情
     * @param id 
     * @return
     */
    public ResultModel view(String id, Principal p) throws DaoException, ServiceException;
    /**
     * 查询货品辅材BOM详情
     * @param id 
     * @return
     */
    public MetaSkuMaterialBom get(String id);
    /**
     * 根据货品辅材BOM属性查询单个货品辅材BOM信息
     */
    public MetaSkuMaterialBom query(MetaSkuMaterialBom entity);

    /**
     * 添加货品辅材BOM
     * @param vo 
     * @return
     */
    public ResultModel add(SkuMaterialBomVo vo, Principal p) throws DaoException, ServiceException;

    /**
     * 修改货品辅材BOM
     * @param vo 
     * @return
     */
    public ResultModel update(SkuMaterialBomVo vo, Principal p) throws DaoException, ServiceException;

    /**
     * 生效货品辅材BOM
     * @param idList 
     * @return
     */
    public ResultModel enable(List<String> idList) throws DaoException, ServiceException;

    /**
     * 失效货品辅材BOM
     * @param id 
     * @return
     * @throws Exception 
     */
    public ResultModel disable(List<String> idList) throws DaoException, ServiceException, Exception;
	/**
	 * @param vo
	 * @param p
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年6月19日 下午2:55:57<br/>
	 * @author 王通<br/>
	 */
	public ResultModel saveAndEnable(SkuMaterialBomVo vo) throws DaoException, ServiceException;

	public SkuMaterialBomVo selectOne(SkuMaterialBomVo bomVo, Principal loginUser) throws ServiceException, DaoException;


}