package com.yunkouan.wms.modules.meta.service;

import java.util.List;

import com.github.pagehelper.Page;
import com.yunkouan.entity.ResultModel;
import com.yunkouan.exception.DaoException;
import com.yunkouan.exception.ServiceException;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.modules.meta.entity.MetaSkuBom;
import com.yunkouan.wms.modules.meta.vo.SkuBomVo;

/**
 * 货品bom服务接口
 * @author tphe06 2017年2月14日
 */
public interface ISkuBomService {
    /**
     * 货品bom列表数据查询（分页）
     * @param vo 
     * @param page 
     * @return
     */
    public ResultModel list(SkuBomVo vo, Principal p) throws DaoException, ServiceException;
    /**
     * 货品bom列表数据查询（分页）
     * @param vo 
     * @param page 
     * @return
     */
    public List<SkuBomVo> listNoPage(SkuBomVo vo, Principal p) throws DaoException, ServiceException;

    /**
     * 查询货品bom详情
     * @param id 
     * @return
     */
    public SkuBomVo view(String id, Principal p) throws DaoException, ServiceException;
    /**
     * 查询货品bom详情
     * @param id 
     * @return
     */
    public MetaSkuBom get(String id);
    /**
     * 根据货品bom属性查询单个货品bom信息
     */
    public MetaSkuBom query(MetaSkuBom entity);

    /**
     * 添加货品bom
     * @param vo 
     * @return
     */
    public SkuBomVo add(SkuBomVo vo, Principal p) throws DaoException, ServiceException;

    /**
     * 修改货品bom
     * @param vo 
     * @return
     */
    public SkuBomVo update(SkuBomVo vo, Principal p) throws DaoException, ServiceException;

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
	public SkuBomVo saveAndEnable(SkuBomVo vo) throws DaoException, ServiceException;
	public void deleteByRecord(MetaSkuBom paramSkuBom);

}