package com.yunkouan.wms.modules.meta.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yunkouan.entity.ResultModel;
import com.yunkouan.exception.DaoException;
import com.yunkouan.exception.ServiceException;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.common.constant.ErrorCode;
import com.yunkouan.wms.common.service.BaseService;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.IdUtil;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.meta.dao.IWorkGroupDao;
import com.yunkouan.wms.modules.meta.entity.MetaWorkGroup;
import com.yunkouan.wms.modules.meta.service.IWorkGroupService;
import com.yunkouan.wms.modules.meta.vo.WorkGroupVo;

/**
 * 班组服务实现类
 */
@Service
@Transactional(readOnly=true)
public class WorkGroupServiceImpl extends BaseService implements IWorkGroupService {
    /**班组数据层接口*/
	@Autowired
    private IWorkGroupDao dao;

    /**
     * 班组列表数据查询
     * @param vo 
     * @param page 
     * @return
     */
    public ResultModel list(WorkGroupVo vo, Principal p) throws DaoException, ServiceException {
    	vo.getEntity().setOrgId(p.getOrgId());
    	/**设置分页参数**/
		Page<MetaWorkGroup> page = PageHelper.startPage(vo.getCurrentPage()+1, vo.getPageSize());
		/**调用通用查询接口**/
    	List<MetaWorkGroup> list = dao.selectByExample(vo.getExample());
    	/**把实体类封装成VO**/
    	List<WorkGroupVo> r = new ArrayList<WorkGroupVo>();
    	if(list != null) for(int i=0; i<list.size(); ++i) {
    		r.add(new WorkGroupVo(list.get(i)));
    	}
    	/**返回前端所需的分页数据和VO**/
    	return new ResultModel().setPage(page).setList(r);
    }

    /**
     * 查询班组详情
     * @param id 
     * @return
     */
    public ResultModel view(String id, Principal p) throws DaoException, ServiceException {
    	if(StringUtil.isTrimEmpty(id)) throw new ServiceException(ErrorCode.KEY_EMPTY);
    	MetaWorkGroup obj =  dao.selectByPrimaryKey(id);
        return new ResultModel().setObj(new WorkGroupVo(obj));
    }

    /**
     * 添加班组
     * @param vo 
     * @return
     * @throws DaoException 
     * @throws ServiceException 
     */
    @Transactional(readOnly=false, rollbackFor=Exception.class)
    public ResultModel add(WorkGroupVo vo, Principal p) throws DaoException, ServiceException {
    	/**保存实体信息，注意清空不能由前端传递的参数值**/
    	MetaWorkGroup entity = vo.getEntity();
    	if(entity == null) throw new ServiceException(ErrorCode.DATA_EMPTY);
    	entity.clear();
    	entity.setWorkGroupNo(IdUtil.getUUID());
    	String id = IdUtil.getUUID();
    	entity.setWorkGroupId(id);
    	entity.setCreatePerson(p.getUserId());
    	entity.setWorkGroupStatus(Constant.STATUS_OPEN);
    	entity.setOrgId(p.getOrgId());
    	entity.setWarehouseId(LoginUtil.getWareHouseId());
    	entity.setWorkGroupId2(context.getStrategy4Id().getWorkGroupSeq());
        int r = dao.insertSelective(entity);
        if(r == 0) throw new DaoException(ErrorCode.DB_EXCEPTION);
        /**给前端返回VO**/
        return view(id, p);
    }

    /**
     * 修改班组
     * @param vo 
     * @return
     * @throws DaoException 
     * @throws ServiceException 
     */
    @Transactional(readOnly=false, rollbackFor=Exception.class)
    public ResultModel update(WorkGroupVo vo, Principal p) throws DaoException, ServiceException {
    	/**修改实体信息，注意清空不能由前端传递的参数值**/
    	MetaWorkGroup entity = vo.getEntity();
    	if(entity == null) throw new ServiceException(ErrorCode.DATA_EMPTY);
    	if(StringUtil.isTrimEmpty(entity.getWorkGroupId())) throw new ServiceException(ErrorCode.KEY_EMPTY);
    	MetaWorkGroup old = dao.selectOne(new MetaWorkGroup().setWorkGroupNo(entity.getWorkGroupNo()).setOrgId(entity.getOrgId()));
    	if(old != null && !old.getWorkGroupId().equals(entity.getWorkGroupId())) throw new ServiceException(ErrorCode.NO_NOT_UNIQUE);
    	entity.clear();
    	entity.setUpdatePerson(p.getUserId());
        int r = dao.updateByPrimaryKeySelective(entity);
        if(r == 0) throw new DaoException(ErrorCode.DB_EXCEPTION);
        /**给前端返回VO**/
        return view(entity.getWorkGroupId(), p);
    }

    /**
     * 生效班组
     * @param id 
     * @return
     * @throws DaoException 
     * @throws ServiceException 
     */
    @Transactional(readOnly=false, rollbackFor=Exception.class)
    public ResultModel enable(String id) throws DaoException, ServiceException {
    	ResultModel m = new ResultModel();
    	if(StringUtil.isTrimEmpty(id)) throw new ServiceException(ErrorCode.KEY_EMPTY);
    	MetaWorkGroup old =  dao.selectByPrimaryKey(id);
    	if(old == null) throw new ServiceException(ErrorCode.DATA_EMPTY);
    	if(old.getWorkGroupStatus() != Constant.STATUS_OPEN) throw new ServiceException(ErrorCode.STATUS_NO_RIGHT);
    	MetaWorkGroup obj = new MetaWorkGroup();
    	obj.setWorkGroupId(id);
    	obj.setWorkGroupStatus(Constant.STATUS_ACTIVE);
        int r = dao.updateByPrimaryKeySelective(obj);
        if(r == 0) throw new DaoException(ErrorCode.DB_EXCEPTION);
        return m;
    }

    /**
     * 失效班组
     * @param id 
     * @return
     * @throws DaoException 
     * @throws ServiceException 
     */
    @Transactional(readOnly=false, rollbackFor=Exception.class)
    public ResultModel disable(String id) throws DaoException, ServiceException {
    	ResultModel m = new ResultModel();
    	if(StringUtil.isTrimEmpty(id)) throw new ServiceException(ErrorCode.KEY_EMPTY);
    	MetaWorkGroup old =  dao.selectByPrimaryKey(id);
    	if(old == null) throw new ServiceException(ErrorCode.DATA_EMPTY);
    	if(old.getWorkGroupStatus() != Constant.STATUS_ACTIVE) throw new ServiceException(ErrorCode.STATUS_NO_RIGHT);
    	MetaWorkGroup obj = new MetaWorkGroup();
    	obj.setWorkGroupId(id);
    	obj.setWorkGroupStatus(Constant.STATUS_OPEN);
        int r = dao.updateByPrimaryKeySelective(obj);
        if(r == 0) throw new DaoException(ErrorCode.DB_EXCEPTION);
        return m;
    }

    /**
     * 取消班组
     */
    @Transactional(readOnly=false, rollbackFor=Exception.class)
	public ResultModel cancel(String id) throws ServiceException, DaoException {
    	ResultModel m = new ResultModel();
    	if(StringUtil.isTrimEmpty(id)) throw new ServiceException(ErrorCode.KEY_EMPTY);
    	MetaWorkGroup old =  dao.selectByPrimaryKey(id);
    	if(old == null) throw new ServiceException(ErrorCode.DATA_EMPTY);
    	if(old.getWorkGroupStatus()!= Constant.STATUS_OPEN) throw new ServiceException(ErrorCode.STATUS_NO_RIGHT);
    	MetaWorkGroup obj = new MetaWorkGroup();
    	obj.setWorkGroupId(id);
    	obj.setWorkGroupStatus(Constant.STATUS_CANCEL);
        int r = dao.updateByPrimaryKeySelective(obj);
        if(r == 0) throw new DaoException(ErrorCode.DB_EXCEPTION);
        return m;
	}
}