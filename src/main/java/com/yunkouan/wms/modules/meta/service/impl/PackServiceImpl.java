package com.yunkouan.wms.modules.meta.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yunkouan.entity.ResultModel;
import com.yunkouan.exception.DaoException;
import com.yunkouan.exception.ServiceException;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.common.constant.BillPrefix;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.common.constant.ErrorCode;
import com.yunkouan.wms.common.service.BaseService;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.IdUtil;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.meta.dao.IPackDao;
import com.yunkouan.wms.modules.meta.entity.MetaPack;
import com.yunkouan.wms.modules.meta.service.IPackService;
import com.yunkouan.wms.modules.meta.vo.PackVo;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 包装服务实现类
 * @author tphe06 2017年2月14日
 */
@Service
@Transactional(readOnly=true)
public class PackServiceImpl extends BaseService implements IPackService {
    /**包装数据层接口*/
	@Autowired
    private IPackDao dao;

    /**
     * 包装列表数据查询
     * @param vo 
     * @param page 
     * @return
     */
    public ResultModel list(PackVo vo, Principal p) throws DaoException, ServiceException {
    	vo.getEntity().setOrgId(p.getOrgId());
		Page<MetaPack> page = PageHelper.startPage(vo.getCurrentPage()+1, vo.getPageSize());
		Example example = vo.getExample();
		this.defWhere(example);
    	List<MetaPack> list = dao.selectByExample(example);
    	List<PackVo> r = new ArrayList<PackVo>();
    	if(list != null) for(int i=0; i<list.size(); ++i) {
    		r.add(new PackVo(list.get(i)));
    	}
    	return new ResultModel().setPage(page).setList(r);
    }

    /**
     * 查询包装详情
     * @param id 
     * @return
     */
    public ResultModel view(String id, Principal p) throws DaoException, ServiceException {
    	MetaPack entity =  dao.selectByPrimaryKey(id);
    	if(entity == null) throw new ServiceException(ErrorCode.DATA_EMPTY);
        return new ResultModel().setObj(new PackVo(entity));
    }

    private static byte[] lock = new byte[0];
    /**
     * 添加包装
     * @param vo 
     * @return
     */
    @Transactional(readOnly=false, rollbackFor=Exception.class)
    public ResultModel add(PackVo vo, Principal p) throws DaoException, ServiceException {
    	MetaPack entity = vo.getEntity();
    	if(entity == null) throw new ServiceException(ErrorCode.DATA_EMPTY);
    	entity.clear();
    	synchronized(lock) {
	    	String prefix = dao.selectMaxNo(new MetaPack().setPackNo(BillPrefix.DOCUMENT_PACK.getPrefix()+"%").setOrgId(p.getOrgId()).setWarehouseId(LoginUtil.getWareHouseId()));
	    	if(StringUtils.isEmpty(prefix)) {
	    		prefix = "1";
	    	} else {
	    		prefix = prefix.replaceFirst(BillPrefix.DOCUMENT_PACK.getPrefix(), "");
	    		prefix = String.valueOf(Integer.parseInt(prefix) + 1);
	    	}
	    	prefix = StringUtil.fillZero(prefix, 6);
	    	entity.setPackNo(BillPrefix.DOCUMENT_PACK.getPrefix()+prefix);
    	}
    	entity.setPackStatus(Constant.STATUS_OPEN);
    	String id = IdUtil.getUUID();
    	entity.setPackId(id);
    	entity.setOrgId(p.getOrgId());
    	entity.setWarehouseId(LoginUtil.getWareHouseId());
    	entity.setCreatePerson(p.getUserId());
    	//长宽高、体积、重量为空的情况下，置为0
    	if (entity.getPackLength() == null) {
    		entity.setPackLength(0.0);
    	}
    	if (entity.getPackWide() == null) {
    		entity.setPackWide(0.0);
    	}
    	if (entity.getPackHeight() == null) {
    		entity.setPackHeight(0.0);
    	}
    	if (entity.getWeight() == null) {
    		entity.setWeight(0.0);
    	}
    	if (entity.getVolume() == null) {
    		entity.setVolume(0.0);
    	}
    	entity.setPackId2(context.getStrategy4Id().getPackSeq());
    	int r = dao.insertSelective(entity);
    	if(r == 0) throw new DaoException(ErrorCode.DB_EXCEPTION);
        return view(id, p);
    }

    /**
     * 修改包装
     * @param vo 
     * @return
     */
    @Transactional(readOnly=false, rollbackFor=Exception.class)
    public ResultModel update(PackVo vo, Principal p) throws DaoException, ServiceException {
    	MetaPack entity = vo.getEntity();
    	if(entity == null) throw new ServiceException(ErrorCode.DATA_EMPTY);
    	entity.clear();
    	entity.setOrgId(p.getOrgId());
    	entity.setWarehouseId(LoginUtil.getWareHouseId());
    	entity.setUpdatePerson(p.getUserId());
    	//长宽高、体积、重量为空的情况下，置为0
    	if (entity.getPackLength() == null) {
    		entity.setPackLength(0.0);
    	}
    	if (entity.getPackWide() == null) {
    		entity.setPackWide(0.0);
    	}
    	if (entity.getPackHeight() == null) {
    		entity.setPackHeight(0.0);
    	}
    	if (entity.getWeight() == null) {
    		entity.setWeight(0.0);
    	}
    	if (entity.getVolume() == null) {
    		entity.setVolume(0.0);
    	}
    	int r = dao.updateByPrimaryKeySelective(entity);
    	if(r == 0) throw new DaoException(ErrorCode.DB_EXCEPTION);
        return view(entity.getPackId(), p);
    }

    /**
     * 生效包装
     * @param id 
     * @return
     */
    @Transactional(readOnly=false, rollbackFor=Exception.class)
    public ResultModel enable(List<String> idList) throws DaoException, ServiceException {
    	for (String id : idList) {
	    	MetaPack old =  dao.selectByPrimaryKey(id);
	    	if(old == null) throw new ServiceException(ErrorCode.DATA_EMPTY);
	    	if(old.getPackStatus() != Constant.STATUS_OPEN) throw new ServiceException(ErrorCode.STATUS_NO_RIGHT);
	    	MetaPack obj = new MetaPack();
	    	obj.setPackId(id);
	    	obj.setPackStatus(Constant.STATUS_ACTIVE);
	        int r = dao.updateByPrimaryKeySelective(obj);
	        if(r == 0) throw new DaoException(ErrorCode.DB_EXCEPTION);
    	}
         ResultModel m = new ResultModel();
    	 return m;
    }

    /**
     * 失效包装
     * @param id 
     * @return
     */
    @Transactional(readOnly=false, rollbackFor=Exception.class)
    public ResultModel disable(List<String> idList) throws DaoException, ServiceException {
    	for (String id : idList) {
	    	MetaPack old =  dao.selectByPrimaryKey(id);
	    	if(old == null) throw new ServiceException(ErrorCode.DATA_EMPTY);
	    	if(old.getPackStatus() != Constant.STATUS_ACTIVE) throw new ServiceException(ErrorCode.STATUS_NO_RIGHT);
	    	MetaPack obj = new MetaPack();
	    	obj.setPackId(id);
	    	obj.setPackStatus(Constant.STATUS_OPEN);
	        int r = dao.updateByPrimaryKeySelective(obj);
	        if(r == 0) throw new DaoException(ErrorCode.DB_EXCEPTION);
    	}
    	ResultModel m = new ResultModel();
        return m;
    }

    @Transactional(readOnly=false, rollbackFor=Exception.class)
	public ResultModel cancel(List<String> idList) throws DaoException, ServiceException {
    	for (String id : idList) {
	    	MetaPack old =  dao.selectByPrimaryKey(id);
	    	if(old == null) throw new ServiceException(ErrorCode.DATA_EMPTY);
	    	if(old.getPackStatus() != Constant.STATUS_OPEN) throw new ServiceException(ErrorCode.STATUS_NO_RIGHT);
	    	MetaPack obj = new MetaPack();
	    	obj.setPackId(id);
	    	obj.setPackStatus(Constant.STATUS_CANCEL);
	        int r = dao.updateByPrimaryKeySelective(obj);
	        if(r == 0) throw new DaoException(ErrorCode.DB_EXCEPTION);
    	}
    	ResultModel m = new ResultModel();
        return m;
	}

	@Override
	public MetaPack get(String id) {
		MetaPack obj =  dao.selectByPrimaryKey(id);
		return obj;
	}
	
	/**
	 * 设置默认查询条件
	 * @param example 
	 * @version 2017年3月8日 下午1:59:29<br/>
	 * @author andy wang<br/>
	 */
	private void defWhere ( Example example ) {
		if ( example == null || example.getOredCriteria() == null || example.getOredCriteria().isEmpty() ) {
			return ;
		}
		Criteria criteria = example.getOredCriteria().get(0);
		Principal loginUser = LoginUtil.getLoginUser();
		criteria.andEqualTo("orgId", loginUser.getOrgId());
		criteria.andEqualTo("warehouseId", LoginUtil.getWareHouseId());
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
	public ResultModel saveAndEnable(PackVo vo) throws DaoException, ServiceException {
    	Principal p = LoginUtil.getLoginUser();
    	ResultModel r = null;
		if (StringUtil.isTrimEmpty(vo.getEntity().getPackId())) {
			r = this.add(vo, p);
			String skuId = vo.getEntity().getPackId();
			List<String> skuList = new ArrayList<String>();
			skuList.add(skuId);
			this.enable(skuList);
		} else {
			r = this.update(vo, p);
			String skuId = vo.getEntity().getPackId();
			List<String> skuList = new ArrayList<String>();
			skuList.add(skuId);
			this.enable(skuList);
		}
		return r;
	}
}