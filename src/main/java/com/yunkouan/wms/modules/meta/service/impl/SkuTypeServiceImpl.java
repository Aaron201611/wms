package com.yunkouan.wms.modules.meta.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yunkouan.entity.ResultModel;
import com.yunkouan.exception.BizException;
import com.yunkouan.exception.DaoException;
import com.yunkouan.exception.ServiceException;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.common.constant.ErrorCode;
import com.yunkouan.wms.common.service.BaseService;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.IdUtil;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.meta.dao.ISkuTypeDao;
import com.yunkouan.wms.modules.meta.entity.MetaMerchant;
import com.yunkouan.wms.modules.meta.entity.MetaSkuType;
import com.yunkouan.wms.modules.meta.service.IMerchantService;
import com.yunkouan.wms.modules.meta.service.ISkuTypeService;
import com.yunkouan.wms.modules.meta.vo.SkuTypeVo;

/**
 * 货品服务实现类
 * @author tphe06 2017年2月14日
 */
@Service
@Transactional(readOnly=true)
public class SkuTypeServiceImpl extends BaseService implements ISkuTypeService {
    /**货品数据层接口*/
	@Autowired
    private ISkuTypeDao dao;
	@Autowired
	private IMerchantService service;

    /**
     * 货品类型列表数据查询
     * @param vo 
     * @param page 
     * @return
     */
    public ResultModel list(SkuTypeVo vo, Principal p) throws DaoException, ServiceException {
		Page<MetaSkuType> page = PageHelper.startPage(vo.getCurrentPage()+1, vo.getPageSize());
		List<MetaSkuType> list = dao.selectByVo(vo.likeVo());
    	List<SkuTypeVo> r = new ArrayList<SkuTypeVo>();
    	if(list != null) for(int i=0; i<list.size(); ++i) {
    		MetaSkuType entity = list.get(i);
    		SkuTypeVo n = new SkuTypeVo(entity);
        	if(!StringUtils.isBlank(entity.getOwner())) {
        		MetaMerchant m = service.get(entity.getOwner());
        		if(m != null) n.setMerchantId(m.getMerchantName());
        	}
        	//查找是否有子集，add by 王通
        	List<MetaSkuType> objList = dao.selectByExample(n.getExample(entity.getSkuTypeId()));
        	if (objList != null && !objList.isEmpty()) {
        		n.setHasChild(true);
        	} else {
        		n.setHasChild(false);
        	}
        	//查找父类编码
        	if (!StringUtils.isBlank(entity.getParentId())) {
        		MetaSkuType parent = dao.selectByPrimaryKey(entity.getParentId());
        		if (parent != null) {
        			n.setParent(parent);
        			n.setParentNo(parent.getSkuTypeNo());
        		}
        	}
    		r.add(n);
    	}
    	return new ResultModel().setPage(page).setList(r);
    }

    /**
     * 查询货品类型详情
     * @param id 
     * @return
     */
    public ResultModel view(String id, Principal p) throws DaoException, ServiceException {
    	MetaSkuType obj =  dao.selectByPrimaryKey(id);
    	SkuTypeVo vo = new SkuTypeVo(obj);
    	if(!StringUtils.isBlank(vo.getEntity().getParentId())) {
    		MetaSkuType p1 = dao.selectByPrimaryKey(vo.getEntity().getParentId());
    		if(p1 != null) vo.setParentId(p1.getSkuTypeName());
    		vo.setParent(p1);
    	}
    	if(!StringUtils.isBlank(vo.getEntity().getOwner())) {
    		MetaMerchant m = service.get(vo.getEntity().getOwner());
    		if(m != null) vo.setMerchantId(m.getMerchantName());
    	}
    	//查找是否有子集，add by 王通
    	List<MetaSkuType> objList = dao.selectByExample(vo.getExample(id));
    	if (objList != null && !objList.isEmpty()) {
    		vo.setHasChild(true);
    	}
        return new ResultModel().setObj(vo);
    }

    private byte[] lock = new byte[1];
    /**
     * 添加货品类型
     * @param entity 
     * @return
     * @throws DaoException 
     * @throws ServiceException 
     */
    @Transactional(readOnly=false, rollbackFor=Exception.class)
    public ResultModel add(SkuTypeVo vo, Principal p) throws DaoException, ServiceException {
    	MetaSkuType entity = vo.getEntity();
    	if(entity == null) throw new ServiceException(ErrorCode.DATA_EMPTY);
    	entity.clear();
    	//检查编号是否唯一
    	MetaSkuType old1 = dao.selectOne(new MetaSkuType().setSkuTypeNo(entity.getSkuTypeNo()).setOrgId(p.getOrgId()));
    	if(old1 != null) throw new ServiceException(ErrorCode.NO_NOT_UNIQUE);
    	List<MetaSkuType> old2 = dao.select(new MetaSkuType().setSkuTypeName(entity.getSkuTypeName()).setOrgId(p.getOrgId()));
    	if(!old2.isEmpty()) throw new ServiceException("err_sku_type_name_rep");
    	//检查货品类型跟上级货品类型是否相同货主
//    	if(StringUtils.isNotBlank(entity.getParentId())) {
//	    	MetaSkuType parent = dao.selectByPrimaryKey(entity.getParentId());
//	    	if(parent != null && !parent.getOwner().equals(entity.getOwner())) throw new ServiceException("valid_skutype_not_same_owner");
//    	}
    	//自动生成货品类型编码
    	synchronized(lock) {
    		String no = IdUtil.addOne(dao.getMaxNo(), IdUtil.INIT);
    		entity.setSkuTypeNo(no);
    	}
    	//补充系统生成的字段
    	entity.setSkuTypeStatus(Constant.STATUS_OPEN);
    	String id = IdUtil.getUUID();
    	entity.setSkuTypeId(id);
    	entity.setCreatePerson(p.getUserId());
    	entity.setOrgId(p.getOrgId());
    	entity.setSkuTypeId2(context.getStrategy4Id().getSkuTypeSeq());
    	//填充层级字段（父级分类为空则表示第一级）
    	entity.setLevelCode(getLevel(entity.getParentId()));
        int r = dao.insertSelective(entity);
        if(r == 0) throw new DaoException(ErrorCode.DB_EXCEPTION);
        return view(id, p);
    }
    private String getLevel(String parentid) {
    	List<MetaSkuType> list = dao.selectAll();
    	MetaSkuType parent = null;
    	if(StringUtils.isNotBlank(parentid)) for(MetaSkuType t : list) {
    		if(t.getSkuTypeId().equals(parentid)) parent = t;
    	}
    	//第一条数据
    	if(list == null || list.size() == 0) return Constant.ROOT_CODE;
    	//过滤数据，只保留相同层级记录
    	List<MetaSkuType> filter = new ArrayList<MetaSkuType>();
    	if(parent != null) {
    		for(MetaSkuType t : list) {
    			if(parentid.equals(t.getParentId()) && t.getLevelCode().startsWith(parent.getLevelCode())) filter.add(t);
    		}
    	} else {
    		list.forEach(t->{
    			if(t.getLevelCode().length() == 3) filter.add(t);
    		});
    	}
    	//从大到小排序
    	Collections.sort(filter, new Comparator<MetaSkuType>(){
			@Override
			public int compare(MetaSkuType o1,MetaSkuType o2) {
				return Integer.parseInt(o2.getLevelCode()) - Integer.parseInt(o1.getLevelCode());
			}
    	});
    	//第一个子节点编码为父编码+001后缀
    	if(filter.size() == 0 && parent != null) return parent.getLevelCode().concat(Constant.ROOT_CODE);
    	//最大节点数据编码+1
    	MetaSkuType max = filter.get(0);
    	String code = String.valueOf(Integer.parseInt(max.getLevelCode()) + 1);
    	return StringUtil.fillZero(code, max.getLevelCode().length());
    }

    /**
     * 修改货品类型
     * @param entity 
     * @return
     * @throws DaoException 
     * @throws ServiceException 
     */
    @Transactional(readOnly=false, rollbackFor=Exception.class)
    public ResultModel update(SkuTypeVo vo, Principal p) throws DaoException, ServiceException {
    	MetaSkuType entity = vo.getEntity();
    	if(entity == null) throw new ServiceException(ErrorCode.DATA_EMPTY);
    	entity.clear();
    	//检查编号是否唯一
    	MetaSkuType old1 = dao.selectOne(new MetaSkuType().setSkuTypeNo(entity.getSkuTypeNo()).setOrgId(p.getOrgId()));
    	if(old1 != null && !old1.getSkuTypeId().equals(entity.getSkuTypeId()) ) throw new ServiceException(ErrorCode.NO_NOT_UNIQUE);
    	List<MetaSkuType> old2 = dao.select(new MetaSkuType().setSkuTypeName(entity.getSkuTypeName()).setOrgId(p.getOrgId()));
    	if(old2 != null && (old2.size() == 1 && !old2.get(0).getSkuTypeId().equals(entity.getSkuTypeId())) || (old2.size()>1) ) throw new ServiceException("err_sku_type_name_rep");
    	
    	//检查货品类型跟上级货品类型是否相同货主
//    	if(StringUtils.isNotBlank(entity.getParentId())) {
//	    	MetaSkuType parent = dao.selectByPrimaryKey(entity.getParentId());
//	    	if(parent != null && !parent.getOwner().equals(entity.getOwner())) throw new ServiceException("valid_skutype_not_same_owner");
//    	}
    	//填充层级字段（父级分类为空则表示第一级）
    	entity.setLevelCode(getLevel(entity.getParentId()));
    	//补充其他字段
    	entity.setUpdatePerson(p.getUserId());
    	entity.setUpdateTime(new Date());
        int r = dao.updateByPrimaryKeySelective(entity);
        if(r == 0) throw new DaoException(ErrorCode.DB_EXCEPTION);
        return view(entity.getSkuTypeId(), p);
    }

    /**
     * 生效货品类型
     * @param id 
     * @return
     * @throws DaoException 
     * @throws ServiceException 
     */
    @Transactional(readOnly=false, rollbackFor=Exception.class)
    public ResultModel enable(List<String> idList) throws DaoException, ServiceException {
    	Principal p = LoginUtil.getLoginUser();
    	for (String id : idList) {
	    	MetaSkuType old =  dao.selectByPrimaryKey(id);
	    	if(old == null) throw new ServiceException(ErrorCode.DATA_EMPTY);
	    	if(old.getSkuTypeStatus() != Constant.STATUS_OPEN) throw new ServiceException(ErrorCode.STATUS_NO_RIGHT);
	    	MetaSkuType obj = new MetaSkuType();
	    	obj.setSkuTypeId(id);
	    	obj.setSkuTypeStatus(Constant.STATUS_ACTIVE);
	    	obj.setUpdatePerson(p.getUserId());
	    	obj.setUpdateTime(new Date());
	        int r = dao.updateByPrimaryKeySelective(obj);
	        if(r == 0) throw new DaoException(ErrorCode.DB_EXCEPTION);
    	}
        ResultModel m = new ResultModel();
    	return m;
    }

    /**
     * 失效货品类型
     * @param id 
     * @return
     * @throws DaoException 
     * @throws ServiceException 
     */
    @Transactional(readOnly=false, rollbackFor=Exception.class)
    public ResultModel disable(List<String> idList) throws DaoException, ServiceException {
    	Principal p = LoginUtil.getLoginUser();
    	for (String id : idList) {
	    	MetaSkuType old =  dao.selectByPrimaryKey(id);
	    	if(old == null) throw new ServiceException(ErrorCode.DATA_EMPTY);
	    	if(old.getSkuTypeStatus() != Constant.STATUS_ACTIVE) throw new ServiceException(ErrorCode.STATUS_NO_RIGHT);
	    	MetaSkuType obj = new MetaSkuType();
	    	obj.setSkuTypeId(id);
	    	obj.setSkuTypeStatus(Constant.STATUS_OPEN);
	    	obj.setUpdatePerson(p.getUserId());
	    	obj.setUpdateTime(new Date());
	        int r = dao.updateByPrimaryKeySelective(obj);
	        if(r == 0) throw new DaoException(ErrorCode.DB_EXCEPTION);
    	}
    	ResultModel m = new ResultModel();
        return m;
    }

    /**
     * 取消货品类型
     */
    @Transactional(readOnly=false, rollbackFor=Exception.class)
	public ResultModel cancel(List<String> idList) throws ServiceException, DaoException {
    	Principal p = LoginUtil.getLoginUser();
    	for (String id : idList) {
	    	MetaSkuType old =  dao.selectByPrimaryKey(id);
	    	if(old == null) throw new ServiceException(ErrorCode.DATA_EMPTY);
	    	if(old.getSkuTypeStatus() != Constant.STATUS_OPEN) throw new ServiceException(ErrorCode.STATUS_NO_RIGHT);
	    	MetaSkuType obj = new MetaSkuType();
	    	obj.setSkuTypeId(id);
	    	obj.setSkuTypeStatus(Constant.STATUS_CANCEL);
	    	obj.setUpdatePerson(p.getUserId());
	    	obj.setUpdateTime(new Date());
	        int r = dao.updateByPrimaryKeySelective(obj);
	        if(r == 0) throw new DaoException(ErrorCode.DB_EXCEPTION);
    	}
        ResultModel m = new ResultModel();
        return m;
	}

	/**
	* @Description: 查询货品类型详情
	* @param id
	* @return
	* @throws ServiceException
	* @throws DaoException
	 */
	@Override
	public MetaSkuType get(String id) throws ServiceException, DaoException {
//    	MetaSkuType obj =  dao.selectByPrimaryKey(id);
		//利用缓存功能
		List<MetaSkuType> list = dao.selectByExample(new SkuTypeVo().setStatusNo(Constant.STATUS_CANCEL).getExample());
		if(list != null) for(int i=0; i<list.size(); ++i) {
			if(list.get(i).getSkuTypeId().equals(id)) return list.get(i);
		}
    	return null;
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
	public ResultModel saveAndEnable(SkuTypeVo vo) throws DaoException, ServiceException {
    	Principal p = LoginUtil.getLoginUser();
    	ResultModel r = null;
		if (StringUtil.isTrimEmpty(vo.getEntity().getSkuTypeId())) {
			r = this.add(vo, p);
			String skuId = vo.getEntity().getSkuTypeId();
			List<String> skuList = new ArrayList<String>();
			skuList.add(skuId);
			this.enable(skuList);
		} else {
			r = this.update(vo, p);
			String skuId = vo.getEntity().getSkuTypeId();
			List<String> skuList = new ArrayList<String>();
			skuList.add(skuId);
			this.enable(skuList);
		}
		return r;
	}

	@Override
	public ResultModel toplist(Principal p) throws ServiceException, DaoException {
		MetaSkuType entity = new MetaSkuType();
		entity.setOrgId(p.getOrgId());
		List<MetaSkuType> list = dao.selectByExample(new SkuTypeVo(entity).setStatusNo(Constant.STATUS_CANCEL).getExample());
    	List<SkuTypeVo> r = new ArrayList<SkuTypeVo>();
    	if(list != null) list.forEach(t->{
    		if(t.getLevelCode() != null && t.getLevelCode().length() == 3) {
	    		SkuTypeVo vo = new SkuTypeVo(t);
	    		r.add(vo);
    		}
    	});
    	return new ResultModel().setList(r);
    }

	@Override
	public ResultModel selectList(SkuTypeVo vo, Principal p) throws ServiceException, DaoException {
		if(vo == null || vo.getEntity() == null || vo.getEntity().getParentId() == null) return new ResultModel();
		vo.setStatusNo(Constant.STATUS_CANCEL);
		if(p != null){
			vo.getEntity().setOrgId(p.getOrgId());
		}
		List<MetaSkuType> list = dao.selectByExample(vo.getExample());
    	List<SkuTypeVo> r = new ArrayList<SkuTypeVo>();
    	if(list != null) list.forEach(t->{
    		if(t.getParentId().equals(vo.getEntity().getParentId())) {
	    		SkuTypeVo vo1 = new SkuTypeVo(t);
	    		r.add(vo1);
    		}
    	});
    	return new ResultModel().setList(r);
    }

	@Override
	public MetaSkuType get(MetaSkuType entity) throws ServiceException, DaoException {
		return dao.selectOne(entity);
	}

	/**
	 * @param skuTypeName$
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年9月16日 上午10:24:08<br/>
	 * @author 王通<br/>
	 */
	@Override
	public MetaSkuType selectChild(String skuTypeName,Integer skuTypeStatus) throws BizException {
		List<MetaSkuType> list = this.dao.selectByExample(
				new SkuTypeVo(
						new MetaSkuType().setSkuTypeName(skuTypeName)
						.setSkuTypeStatus(skuTypeStatus))
				.getExampleChild());
		if (list != null && !list.isEmpty()) {
			if (list.size() > 1) {
				throw new BizException("err_result_too_many");
			} else {
				return list.get(0);
			}
		}
		return null;
	}
}