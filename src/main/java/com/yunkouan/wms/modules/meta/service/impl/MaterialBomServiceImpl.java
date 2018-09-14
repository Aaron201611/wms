package com.yunkouan.wms.modules.meta.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yunkouan.entity.ResultModel;
import com.yunkouan.exception.DaoException;
import com.yunkouan.exception.ServiceException;
import com.yunkouan.wms.common.constant.ErrorCode;
import com.yunkouan.wms.common.service.BaseService;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.IdUtil;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.meta.dao.IMaterialBomDao;
import com.yunkouan.wms.modules.meta.entity.MetaMaterialBom;
import com.yunkouan.wms.modules.meta.service.IMaterialBomService;
import com.yunkouan.wms.modules.meta.service.IMaterialService;
import com.yunkouan.wms.modules.meta.vo.MaterialBomVo;
import com.yunkouan.wms.modules.meta.vo.MaterialVo;

/**
 * 辅材bom服务实现类
 * @author tphe06 2017年2月14日
 */
@Service
@Transactional(readOnly=true)
public class MaterialBomServiceImpl extends BaseService implements IMaterialBomService {
	private static Log log = LogFactory.getLog(MaterialBomServiceImpl.class);

	/**
	 * 外部服务-辅材
	 */
	@Autowired
	IMaterialService materialService;
	/**数据层接口*/
	@Autowired
    private IMaterialBomDao dao;


    /**
     * 辅材bom列表数据查询
     * @param vo 
     * @param page 
     * @return
     */
    public ResultModel list(MaterialBomVo vo, Principal p) throws DaoException, ServiceException {
    	vo.getEntity().setOrgId(p.getOrgId());
    	vo.getEntity().setWarehouseId(LoginUtil.getWareHouseId());
		Page<MetaMaterialBom> page = PageHelper.startPage(vo.getCurrentPage()+1, vo.getPageSize());
		// 返回查询结果
		List<MetaMaterialBom> list = dao.selectByExample(vo.getExample());
    	return new ResultModel().setPage(page).setList(fill(list));
    }/**
     * 辅材bom列表数据查询
     * @param vo 
     * @param page 
     * @return
     */
    public List<MaterialBomVo> listNoPage(MaterialBomVo vo, Principal p) throws DaoException, ServiceException {
    	vo.getEntity().setOrgId(p.getOrgId());
    	vo.getEntity().setWarehouseId(LoginUtil.getWareHouseId());
		// 返回查询结果
		List<MetaMaterialBom> list = dao.selectByExample(vo.getExample());
    	return fill(list);
    }

    /**
     * 查询辅材bom详情
     * @param id 
     * @return
     */
	public MaterialBomVo view(String id, Principal p) throws DaoException, ServiceException {
    	/**查询辅材bom信息**/
    	MetaMaterialBom entity =  dao.selectByPrimaryKey(id);
        return fill(entity);
    }

    /**
     * 添加辅材bom
     * @param vo 
     * @return
     */
    @Transactional(readOnly=false, rollbackFor=Exception.class)
    public MaterialBomVo add(MaterialBomVo vo, Principal p) throws DaoException, ServiceException {
    	/**添加辅材bom信息**/
    	MetaMaterialBom entity = vo.getEntity();
    	if(entity == null) throw new ServiceException(ErrorCode.DATA_EMPTY);
    	entity.clear();
    	String id = IdUtil.getUUID();
    	entity.setMaterialBomId(id);
    	entity.setOrgId(p.getOrgId());
    	entity.setWarehouseId(LoginUtil.getWareHouseId());
    	entity.setCreatePerson(p.getUserId());
        dao.insertSelective(entity);
        /**返回辅材bom信息**/
        return fill(entity);
    }

    /**
     * 修改辅材bom
     * @param vo 
     * @return
     */
    @Transactional(readOnly=false, rollbackFor=Exception.class)
    public MaterialBomVo update(MaterialBomVo vo, Principal p) throws DaoException, ServiceException {
    	/**修改辅材bom信息**/
    	MetaMaterialBom entity = vo.getEntity();
    	if(entity == null) throw new ServiceException(ErrorCode.DATA_EMPTY);
    	entity.clear();
    	entity.setUpdatePerson(p.getUserId());
    	entity.setUpdateTime(new Date());
    	dao.updateByExample(entity, vo.getExample());
    	/**返回辅材bom信息**/
        return fill(entity);
    }

    
    @Override
	public MetaMaterialBom get(String id) {
		MetaMaterialBom obj =  dao.selectByPrimaryKey(id);
		return obj;
	}

    @Override
	public MetaMaterialBom query(MetaMaterialBom entity) {
		return dao.selectOne(entity);
	}

    /**
	 * 批量把辅材bom实体类转化成VO对象
	 * @param list
	 * @return
	 * @throws ServiceException
	 * @throws DaoException
	 */
	private List<MaterialBomVo> fill(List<MetaMaterialBom> list) throws ServiceException, DaoException {
    	List<MaterialBomVo> r = new ArrayList<MaterialBomVo>();
    	if(list != null) for(MetaMaterialBom entity : list) {
    		r.add(fill(entity));
    	}
    	return r;
	}
	/**
	 * 把辅材bom实体类转化成VO对象，同时补充一些扩展属性
	 * @param entity
	 * @return
	 * @throws DaoException 
	 * @throws ServiceException 
	 * @throws Exception 
	 */
	private MaterialBomVo fill(MetaMaterialBom entity) throws ServiceException, DaoException {
		MaterialBomVo vo = new MaterialBomVo(entity);
		vo.setMaterialVo((MaterialVo) materialService.view(entity.getMaterialId(), LoginUtil.getLoginUser()).getObj());
    	return vo;
	}
	@Override
	public void deleteByRecord(MetaMaterialBom paramMaterialBom) {
		this.dao.delete(paramMaterialBom);
	}
}