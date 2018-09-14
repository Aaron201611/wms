package com.yunkouan.wms.modules.meta.service;

import java.util.List;

import com.yunkouan.entity.ResultModel;
import com.yunkouan.exception.DaoException;
import com.yunkouan.exception.ServiceException;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.modules.meta.entity.MetaMaterialBom;
import com.yunkouan.wms.modules.meta.vo.MaterialBomVo;

/**
 * 货品服务接口
 * @author tphe06 2017年2月14日
 */
public interface IMaterialBomService {
    /**
     * 货品列表数据查询（分页）
     * @param vo 
     * @param page 
     * @return
     */
    public ResultModel list(MaterialBomVo vo, Principal p) throws DaoException, ServiceException;
    /**
     * 查询货品详情
     * @param id 
     * @return
     */
    public MaterialBomVo view(String id, Principal p) throws DaoException, ServiceException;
    /**
     * 查询货品详情
     * @param id 
     * @return
     */
    public MetaMaterialBom get(String id);
    /**
     * 根据货品属性查询单个货品信息
     */
    public MetaMaterialBom query(MetaMaterialBom entity);

    /**
     * 添加货品
     * @param vo 
     * @return
     */
    public MaterialBomVo add(MaterialBomVo vo, Principal p) throws DaoException, ServiceException;

    /**
     * 修改货品
     * @param vo 
     * @return
     */
    public MaterialBomVo update(MaterialBomVo vo, Principal p) throws DaoException, ServiceException;
	public List<MaterialBomVo> listNoPage(MaterialBomVo materialBomVo, Principal loginUser) throws DaoException, ServiceException;
	public void deleteByRecord(MetaMaterialBom paramMaterialBom);

    


}