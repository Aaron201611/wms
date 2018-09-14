package com.yunkouan.wms.modules.meta.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yunkouan.entity.ResultModel;
import com.yunkouan.exception.BizException;
import com.yunkouan.exception.DaoException;
import com.yunkouan.exception.ServiceException;
import com.yunkouan.saas.modules.sys.entity.SysUser;
import com.yunkouan.saas.modules.sys.service.ISysParamService;
import com.yunkouan.saas.modules.sys.service.IUserService;
import com.yunkouan.util.MD5Util;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.common.constant.CacheName;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.common.constant.ErrorCode;
import com.yunkouan.wms.common.service.BaseService;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.IdUtil;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.inv.service.IStockService;
import com.yunkouan.wms.modules.meta.dao.ISkuMaterialBomDao;
import com.yunkouan.wms.modules.meta.entity.MetaMaterialBom;
import com.yunkouan.wms.modules.meta.entity.MetaSkuBom;
import com.yunkouan.wms.modules.meta.entity.MetaSkuMaterialBom;
import com.yunkouan.wms.modules.meta.service.IMaterialBomService;
import com.yunkouan.wms.modules.meta.service.ISkuBomService;
import com.yunkouan.wms.modules.meta.service.ISkuMaterialBomService;
import com.yunkouan.wms.modules.meta.vo.MaterialBomVo;
import com.yunkouan.wms.modules.meta.vo.SkuBomVo;
import com.yunkouan.wms.modules.meta.vo.SkuMaterialBomVo;

/**
 * 货品辅材BOM服务实现类
 * @author tphe06 2017年2月14日
 */
@Service
@Transactional(readOnly=true)
public class SkuMaterialBomServiceImpl extends BaseService implements ISkuMaterialBomService {
	private static Log log = LogFactory.getLog(SkuMaterialBomServiceImpl.class);

	@Autowired
	private ISysParamService paramService;

	/**
	 * 外部服务-库存
	 */
	@Autowired
	IStockService stockService;
	/**货品辅材BOM数据层接口*/
	@Autowired
    private ISkuMaterialBomDao dao;
	@Autowired
	private ISkuBomService skuBomService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IMaterialBomService materialBomService;


    /**
     * 货品辅材BOM列表数据查询
     * @param vo 
     * @param page 
     * @return
     */
    public ResultModel list(SkuMaterialBomVo vo, Principal p) throws DaoException, ServiceException {
    	if(log.isInfoEnabled()) log.info("list");
    	vo.getEntity().setOrgId(p.getOrgId());
    	vo.getEntity().setWarehouseId(LoginUtil.getWareHouseId());
		Page<MetaSkuMaterialBom> page = PageHelper.startPage(vo.getCurrentPage()+1, vo.getPageSize());
		// 返回查询结果
		List<MetaSkuMaterialBom> list = dao.selectByExample(vo.getExample());
    	return new ResultModel().setPage(page).setList(chg(list));
    }

    /**
     * 查询货品辅材BOM详情
     * @param id 
     * @return
     */
	public ResultModel view(String id, Principal p) throws DaoException, ServiceException {
    	/**查询货品辅材BOM信息**/
    	MetaSkuMaterialBom entity =  dao.selectByPrimaryKey(id);
    	if(entity == null) throw new ServiceException(ErrorCode.DATA_EMPTY);
    	SkuMaterialBomVo vo = chg(entity);
        return new ResultModel().setObj(vo);
    }

    /**
     * 添加货品辅材BOM
     * @param vo 
     * @return
     */
    @Transactional(readOnly=false, rollbackFor=Exception.class)
    public ResultModel add(SkuMaterialBomVo vo, Principal p) throws DaoException, ServiceException {
    	/**添加货品辅材BOM信息**/
    	MetaSkuMaterialBom entity = vo.getEntity();
    	if(entity == null) throw new ServiceException(ErrorCode.DATA_EMPTY);
    	entity.clear();
		//同一仓库货品组合不允许重复
    	StringBuffer skuInfo = new StringBuffer();
    	StringBuffer skuNoInfo = new StringBuffer();
        if(vo.getSkuBomList() != null) {
        	vo.setSkuBomList(sortOrder(vo.getSkuBomList()));
        	
        	for(int i = 0; i < vo.getSkuBomList().size(); i++) {
        		SkuBomVo skuBomVo = vo.getSkuBomList().get(i);
        		if (i == 0) {
        			skuInfo.append(skuBomVo.getSkuVo().getEntity().getSkuName()).append("X").append(skuBomVo.getEntity().getNumber());
        			skuNoInfo.append(skuBomVo.getSkuVo().getEntity().getSkuNo());
            	} else {
            		skuInfo.append(",").append(skuBomVo.getSkuVo().getEntity().getSkuName()).append("X").append(skuBomVo.getEntity().getNumber());
            		skuNoInfo.append(",").append(skuBomVo.getSkuVo().getEntity().getSkuNo());
        		}
        	}
        } else {
        	throw new BizException("sku_bom_is_null");
        }
        if(vo.getMaterialBomList() == null) {
        	throw new BizException("material_bom_is_null");
        }
        String md5 = MD5Util.md5(skuInfo.toString() + LoginUtil.getWareHouseId() + p.getOrgId());
		MetaSkuMaterialBom su = new MetaSkuMaterialBom().setIdentificationCode(md5);
		MetaSkuMaterialBom old = dao.selectOne(su);
    	if(old != null) throw new ServiceException("err_sku_info_rep");
    	if (entity.getStatus() == null) {
        	entity.setStatus(Constant.STATUS_OPEN);
    	}
    	String id = IdUtil.getUUID();
    	entity.setBomId(id);
    	entity.setSkuInfoComment(skuInfo.toString());
    	entity.setSkuNoComment(skuNoInfo.toString());
    	entity.setIdentificationCode(md5);
    	entity.setOrgId(p.getOrgId());
    	entity.setWarehouseId(LoginUtil.getWareHouseId());
    	entity.setCreatePerson(p.getUserId());
    	entity.setCreateTime(new Date());
        int r = dao.insertSelective(entity);
        if(r == 0) throw new DaoException(ErrorCode.DB_EXCEPTION);

    	/**批量添加货品BOM信息**/
        List<SkuBomVo> skuBomlist = vo.getSkuBomList();
    	for(SkuBomVo skuBomVo: skuBomlist) {
    		MetaSkuBom skuBom = skuBomVo.getEntity();
    		skuBom.setBomId(id);
    		skuBomService.add(skuBomVo, p);
    	}
        /**批量添加辅材BOM信息**/
	    List<MaterialBomVo> materilBomlist = vo.getMaterialBomList();
		for(MaterialBomVo materialBomVo: materilBomlist) {
			MetaMaterialBom materialBom = materialBomVo.getEntity();
			materialBom.setBomId(id);
			materialBomService.add(materialBomVo, p);
		}
        /**返回货品辅材BOM信息**/
        return new ResultModel().setObj(view(entity.getBomId(), p));
    }

    private List<SkuBomVo> sortOrder(List<SkuBomVo> skuBomList) {
    	for (int i = 0; i < skuBomList.size(); i++) {
    		SkuBomVo vo1 = skuBomList.get(i);
			for (int j = i + 1; j < skuBomList.size(); j++) {
				SkuBomVo vo2 = skuBomList.get(j);
				String skuNo1 = vo1.getSkuVo().getEntity().getSkuNo();
				String skuNo2 = vo2.getSkuVo().getEntity().getSkuNo();
				if (skuNo1.compareTo(skuNo2) > 0) {
//					SkuBomVo temp1 = (SkuBomVo) vo1.clone();
					SkuBomVo temp1 = new SkuBomVo();
					BeanUtils.copyProperties(vo1, temp1);
//					SkuBomVo temp2 = (SkuBomVo) vo2.clone();
					SkuBomVo temp2 = new SkuBomVo();
					BeanUtils.copyProperties(vo2, temp2);
					skuBomList.remove(j);
					skuBomList.add(j, temp1);
					skuBomList.remove(i);
					skuBomList.add(i, temp2);
					i--;
					break;
				}
			}
		}
		return skuBomList;
	}

	/**
     * 修改货品辅材BOM
     * @param vo 
     * @return
     */
    @Transactional(readOnly=false, rollbackFor=Exception.class)
    public ResultModel update(SkuMaterialBomVo vo, Principal p) throws DaoException, ServiceException {
    	/**修改货品辅材BOM信息**/
    	MetaSkuMaterialBom entity = vo.getEntity();
    	if(entity == null) throw new ServiceException(ErrorCode.DATA_EMPTY);
    	String id = entity.getBomId();
    	entity.clear();
		//同一仓库货品组合不允许重复
    	StringBuffer skuInfo = new StringBuffer();
    	StringBuffer skuNoInfo = new StringBuffer();
        if(vo.getSkuBomList() != null) {
        	vo.setSkuBomList(sortOrder(vo.getSkuBomList()));
        	for(int i = 0; i < vo.getSkuBomList().size(); i++) {
        		SkuBomVo skuBomVo = vo.getSkuBomList().get(i);
        		if (i == 0) {
        			skuInfo.append(skuBomVo.getSkuVo().getEntity().getSkuName()).append("X").append(skuBomVo.getEntity().getNumber());
        			skuNoInfo.append(skuBomVo.getSkuVo().getEntity().getSkuNo());
            	} else {
            		skuInfo.append(",").append(skuBomVo.getSkuVo().getEntity().getSkuName()).append("X").append(skuBomVo.getEntity().getNumber());
            		skuNoInfo.append(",").append(skuBomVo.getSkuVo().getEntity().getSkuNo());
        		}
        	}
        } else {
        	throw new BizException("sku_bom_is_null");
        }
        if(vo.getMaterialBomList() == null) {
        	throw new BizException("material_bom_is_null");
        }
        String md5 = MD5Util.md5(skuInfo.toString() + LoginUtil.getWareHouseId() + p.getOrgId());
		MetaSkuMaterialBom su = new MetaSkuMaterialBom().setIdentificationCode(md5);
		List<MetaSkuMaterialBom> old = dao.select(su);
    	if(old != null) {
    		if (old.size() > 1) {
        		throw new ServiceException("err_sku_info_rep");
    		}
			if (old.size() != 0 && !old.get(0).getBomId().equals(vo.getEntity().getBomId())) {
        		throw new ServiceException("err_sku_info_rep");
    		}
    	}
    	entity.setSkuInfoComment(skuInfo.toString());
    	entity.setSkuNoComment(skuNoInfo.toString());
    	entity.setIdentificationCode(md5);
    	entity.setOrgId(p.getOrgId());
    	entity.setWarehouseId(LoginUtil.getWareHouseId());
    	entity.setUpdatePerson(p.getUserId());
    	entity.setUpdateTime(new Date());
        int r = dao.updateByPrimaryKeySelective(entity);
        if(r == 0) throw new DaoException(ErrorCode.DB_EXCEPTION);
        /**批量删除货品BOM和辅材BOM的信息**/
        MetaSkuBom paramSkuBom = new MetaSkuBom();
        paramSkuBom.setBomId(id);
		skuBomService.deleteByRecord(paramSkuBom);
        MetaMaterialBom paramMaterialBom = new MetaMaterialBom();
        paramMaterialBom.setBomId(id);
		materialBomService.deleteByRecord(paramMaterialBom);
    	/**批量添加货品BOM信息**/
        List<SkuBomVo> skuBomlist = vo.getSkuBomList();
    	for(SkuBomVo skuBomVo: skuBomlist) {
    		MetaSkuBom skuBom = skuBomVo.getEntity();
    		skuBom.setBomId(id);
    		skuBomService.add(skuBomVo, p);
    	}
        /**批量添加辅材BOM信息**/
	    List<MaterialBomVo> materilBomlist = vo.getMaterialBomList();
		for(MaterialBomVo materialBomVo: materilBomlist) {
			MetaMaterialBom materialBom = materialBomVo.getEntity();
			materialBom.setBomId(id);
			materialBomService.add(materialBomVo, p);
		}
        /**返回货品辅材BOM信息**/
        return new ResultModel().setObj(view(entity.getBomId(), p));
    }

    /**
     * 生效货品辅材BOM
     * @param id 
     * @return
     */
    @Transactional(readOnly=false, rollbackFor=Exception.class)
    public ResultModel enable(List<String> idList) throws DaoException, ServiceException {
    	for (String id : idList) {
	    	MetaSkuMaterialBom old =  dao.selectByPrimaryKey(id);
	    	if(old == null) throw new ServiceException(ErrorCode.DATA_EMPTY);
	    	if(old.getStatus() != Constant.STATUS_OPEN) throw new ServiceException(ErrorCode.STATUS_NO_RIGHT);
	    	MetaSkuMaterialBom obj = new MetaSkuMaterialBom();
	    	obj.setBomId(id);
	    	obj.setStatus(Constant.STATUS_ACTIVE);
	        int r = dao.updateByPrimaryKeySelective(obj);
	        if(r == 0) throw new DaoException(ErrorCode.DB_EXCEPTION);
    	}
        ResultModel m = new ResultModel();
    	return m;
    }
    
    
    /**
     * 失效货品辅材BOM 在有库存和未完成收货单的情况下不能失效
     * @param id 
     * @return
     * @throws Exception 
     */
    @Transactional(readOnly=false, rollbackFor=Exception.class)
    public ResultModel disable(List<String> idList) throws Exception {
    	for (String id : idList) {
	    	MetaSkuMaterialBom obj = new MetaSkuMaterialBom();
	    	obj.setBomId(id);
	    	obj.setStatus(Constant.STATUS_OPEN);
	        int r = dao.updateByPrimaryKeySelective(obj);
	        if(r == 0) throw new DaoException(ErrorCode.DB_EXCEPTION);
    	}
    	ResultModel m = new ResultModel();
        return m;
    }

    @Override
	public MetaSkuMaterialBom get(String id) {
		MetaSkuMaterialBom obj =  dao.selectByPrimaryKey(id);
		return obj;
	}

    @Override
	public MetaSkuMaterialBom query(MetaSkuMaterialBom entity) {
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
	public ResultModel saveAndEnable(SkuMaterialBomVo vo) throws DaoException, ServiceException {
    	Principal p = LoginUtil.getLoginUser();
    	ResultModel r = null;
		if (StringUtil.isTrimEmpty(vo.getEntity().getBomId())) {
			r = this.add(vo, p);
			String bomId = vo.getEntity().getBomId();
			List<String> bomIdList = new ArrayList<String>();
			bomIdList.add(bomId);
			this.enable(bomIdList);
		} else {
			r = this.update(vo, p);
			String bomId = vo.getEntity().getBomId();
			List<String> bomIdList = new ArrayList<String>();
			bomIdList.add(bomId);
			this.enable(bomIdList);
		}
		return r;
	}

	/**
	 * 批量把货品辅材BOM实体类转化成VO对象
	 * @param list
	 * @return
	 * @throws ServiceException
	 * @throws DaoException
	 */
	private List<SkuMaterialBomVo> chg(List<MetaSkuMaterialBom> list) throws ServiceException, DaoException {
    	List<SkuMaterialBomVo> r = new ArrayList<SkuMaterialBomVo>();
    	if(list != null) for(MetaSkuMaterialBom entity : list) {
    		r.add(chg(entity));
    	}
    	return r;
	}
	/**
	 * 把货品辅材BOM实体类转化成VO对象，同时补充一些扩展属性
	 * @param entity
	 * @return
	 * @throws DaoException 
	 * @throws ServiceException 
	 * @throws Exception 
	 */
	private SkuMaterialBomVo chg(MetaSkuMaterialBom entity) throws ServiceException, DaoException {
		if (entity == null) {
			return null;
		}
		SkuMaterialBomVo vo = new SkuMaterialBomVo(entity);
		String bomId = entity.getBomId();
		SkuBomVo skuBomVo = new SkuBomVo();
		skuBomVo.getEntity().setBomId(bomId);
		vo.setSkuBomList(this.skuBomService.listNoPage(skuBomVo, LoginUtil.getLoginUser()));
		MaterialBomVo materialBomVo = new MaterialBomVo();
		materialBomVo.getEntity().setBomId(bomId);
		vo.setMaterialBomList(this.materialBomService.listNoPage(materialBomVo, LoginUtil.getLoginUser()));
		SysUser createPerson = userService.get(entity.getCreatePerson());
		vo.setCreatePersonName(createPerson.getUserName());
		if (StringUtil.isNoneBlank(entity.getUpdatePerson())) {
			SysUser updatePerson = userService.get(entity.getUpdatePerson());
			vo.setUpdatePersonName(updatePerson.getUserName());
		}
		vo.setStatusName(paramService.getValue(CacheName.STATUS, entity.getStatus()));
		return vo;
	}

	@Override
	public SkuMaterialBomVo selectOne(SkuMaterialBomVo bomVo, Principal loginUser) throws ServiceException, DaoException {
		return chg(this.dao.selectOne(bomVo.getEntity()));
	}
    

}