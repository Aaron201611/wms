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
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.common.constant.ErrorCode;
import com.yunkouan.wms.common.service.BaseService;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.IdUtil;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.meta.dao.ISkuBomDao;
import com.yunkouan.wms.modules.meta.entity.MetaSkuBom;
import com.yunkouan.wms.modules.meta.service.ISkuBomService;
import com.yunkouan.wms.modules.meta.service.ISkuService;
import com.yunkouan.wms.modules.meta.vo.SkuBomVo;
import com.yunkouan.wms.modules.meta.vo.SkuVo;

/**
 * 货品BOM服务实现类
 * @author tphe06 2017年2月14日
 */
@Service
@Transactional(readOnly=true)
public class SkuBomServiceImpl extends BaseService implements ISkuBomService {
	private static Log log = LogFactory.getLog(SkuBomServiceImpl.class);

	/**货品BOM数据层接口*/
	@Autowired
    private ISkuBomDao dao;
	@Autowired
    private ISkuService skuService;


    /**
     * 货品BOM列表数据查询
     * @param vo 
     * @param page 
     * @return
     */
    public ResultModel list(SkuBomVo vo, Principal p) throws DaoException, ServiceException {
    	vo.getEntity().setOrgId(p.getOrgId());
    	vo.getEntity().setWarehouseId(LoginUtil.getWareHouseId());
    	Page<MetaSkuBom> page = PageHelper.startPage(vo.getCurrentPage()+1, vo.getPageSize());
		// 返回查询结果
		List<MetaSkuBom> list = dao.selectByExample(vo.getExample());
		return new ResultModel().setPage(page).setList(fill(list));
    }
    /**
     * 货品BOM列表数据查询
     * @param vo 
     * @param page 
     * @return
     */
    public List<SkuBomVo> listNoPage(SkuBomVo vo, Principal p) throws DaoException, ServiceException {
    	vo.getEntity().setOrgId(p.getOrgId());
    	vo.getEntity().setWarehouseId(LoginUtil.getWareHouseId());
		// 返回查询结果
		List<MetaSkuBom> list = dao.selectByExample(vo.getExample());
		return fill(list);
    }
    /**
     * 查询货品BOM详情
     * @param id 
     * @return
     */
	public SkuBomVo view(String id, Principal p) throws DaoException, ServiceException {
    	/**查询货品BOM信息**/
    	MetaSkuBom entity =  dao.selectByPrimaryKey(id);
        return fill(entity);
    }

    /**
     * 添加货品BOM
     * @param vo 
     * @return
     */
    @Transactional(readOnly=false, rollbackFor=Exception.class)
    public SkuBomVo add(SkuBomVo vo, Principal p) throws DaoException, ServiceException {
    	/**添加货品BOM信息**/
    	MetaSkuBom entity = vo.getEntity();
    	if(entity == null) throw new ServiceException(ErrorCode.DATA_EMPTY);
    	entity.clear();
    	String id = IdUtil.getUUID();
    	entity.setSkuBomId(id);
    	entity.setOrgId(p.getOrgId());
    	entity.setWarehouseId(LoginUtil.getWareHouseId());
    	entity.setCreatePerson(p.getUserId());
        dao.insertSelective(entity);
        /**返回货品BOM信息**/
        return fill(entity);
    }

    /**
     * 修改货品BOM
     * @param vo 
     * @return
     */
    @Transactional(readOnly=false, rollbackFor=Exception.class)
    public SkuBomVo update(SkuBomVo vo, Principal p) throws DaoException, ServiceException {
    	/**修改货品BOM信息**/
    	MetaSkuBom entity = vo.getEntity();
    	if(entity == null) throw new ServiceException(ErrorCode.DATA_EMPTY);
    	entity.clear();
    	entity.setUpdatePerson(p.getUserId());
    	entity.setUpdateTime(new Date());
    	int r = dao.updateByPrimaryKeySelective(entity);
    	if(r == 0) throw new DaoException(ErrorCode.DB_EXCEPTION);
    	/**批量修改货品BOM包装信息，先删除后添加**/
    	/**返回货品BOM信息**/
        return fill(entity);
    }

    
    
    
    @Override
	public MetaSkuBom get(String id) {
		MetaSkuBom obj =  dao.selectByPrimaryKey(id);
		return obj;
	}

    @Override
	public MetaSkuBom query(MetaSkuBom entity) {
		return dao.selectOne(entity);
	}

    /**
	 * @param vo
	 * @return
	 * @throws DaoException
	 * @throws ServiceException
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年6月19日 下午2:57:02<br/>
	 * @author 王通<br/>
	 */
	@Override
    @Transactional(readOnly=false, rollbackFor=Exception.class)
	public SkuBomVo saveAndEnable(SkuBomVo vo) throws DaoException, ServiceException {
    	Principal p = LoginUtil.getLoginUser();
    	SkuBomVo r = null;
		if (StringUtil.isTrimEmpty(vo.getEntity().getSkuBomId())) {
			r = this.add(vo, p);
		} else {
			r = this.update(vo, p);
		}
		return r;
	}

	/**
	 * 批量把货品BOM实体类转化成VO对象
	 * @param list
	 * @return
	 * @throws ServiceException
	 * @throws DaoException
	 */
	private List<SkuBomVo> fill(List<MetaSkuBom> list) throws ServiceException, DaoException {
    	List<SkuBomVo> r = new ArrayList<SkuBomVo>();
    	if(list != null) for(MetaSkuBom entity : list) {
    		r.add(fill(entity));
    	}
    	return r;
	}
	/**
	 * 把货品BOM实体类转化成VO对象，同时补充一些扩展属性
	 * @param entity
	 * @return
	 * @throws DaoException 
	 * @throws ServiceException 
	 * @throws Exception 
	 */
	private SkuBomVo fill(MetaSkuBom entity) throws ServiceException, DaoException {
		SkuBomVo vo = new SkuBomVo(entity);
		vo.setSkuVo((SkuVo)skuService.view(entity.getSkuId(), LoginUtil.getLoginUser()).getObj());
    	return vo;
	}
	@Override
	public void deleteByRecord(MetaSkuBom paramSkuBom) {
		this.dao.delete(paramSkuBom);
	}
}