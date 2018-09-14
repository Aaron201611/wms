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
import com.yunkouan.wms.modules.meta.dao.IWorkAreaDao;
import com.yunkouan.wms.modules.meta.entity.MetaWorkArea;
import com.yunkouan.wms.modules.meta.service.IWorkAreaService;
import com.yunkouan.wms.modules.meta.vo.WorkAreaVo;

/**
 * 工作区服务实现类
 */
@Service
@Transactional(readOnly=true)
public class WorkAreaServiceImpl extends BaseService implements IWorkAreaService {
    /**工作区数据层接口*/
	@Autowired
    private IWorkAreaDao dao;

    /**
     * 工作区列表数据查询
     * @param vo 
     * @param page 
     * @return
     */
    public ResultModel list(WorkAreaVo vo, Principal p) throws DaoException, ServiceException {
    	vo.getEntity().setOrgId(p.getOrgId());
    	/**设置分页参数**/
		Page<MetaWorkArea> page = PageHelper.startPage(vo.getCurrentPage()+1, vo.getPageSize());
		/**调用通用查询接口**/
    	List<MetaWorkArea> list = dao.selectByExample(vo.getExample());
    	/**把实体类封装成VO**/
    	List<WorkAreaVo> r = new ArrayList<WorkAreaVo>();
    	if(list != null) for(int i=0; i<list.size(); ++i) {
    		r.add(new WorkAreaVo(list.get(i)));
    	}
    	/**返回前端所需的分页数据和VO**/
    	return new ResultModel().setPage(page).setList(r);
    }

    /**
     * 查询工作区详情
     * @param id 
     * @return
     */
    public ResultModel view(String id, Principal p) throws DaoException, ServiceException {
    	if(StringUtil.isTrimEmpty(id)) throw new ServiceException(ErrorCode.KEY_EMPTY);
    	MetaWorkArea obj =  dao.selectByPrimaryKey(id);
        return new ResultModel().setObj(new WorkAreaVo(obj));
    }

    /**
     * 添加工作区
     * @param vo 
     * @return
     * @throws DaoException 
     * @throws ServiceException 
     */
    @Transactional(readOnly=false, rollbackFor=Exception.class)
    public ResultModel add(WorkAreaVo vo, Principal p) throws DaoException, ServiceException {
    	/**保存实体信息，注意清空不能由前端传递的参数值**/
    	MetaWorkArea entity = vo.getEntity();
    	if(entity == null) throw new ServiceException(ErrorCode.DATA_EMPTY);
    	entity.clear();
    	entity.setWorkAreaNo(IdUtil.getUUID());
    	String id = IdUtil.getUUID();
    	entity.setWorkAreaId(id);
    	entity.setCreatePerson(p.getUserId());
    	entity.setWorkAreaStatus(Constant.STATUS_OPEN);
    	entity.setOrgId(p.getOrgId());
    	entity.setWarehouseId(LoginUtil.getWareHouseId());
    	entity.setWorkAreaId2(context.getStrategy4Id().getWorkAreaSeq());
        int r = dao.insertSelective(entity);
        if(r == 0) throw new DaoException(ErrorCode.DB_EXCEPTION);
        /**给前端返回VO**/
        return view(id, p);
    }

    /**
     * 修改工作区
     * @param vo 
     * @return
     * @throws DaoException 
     * @throws ServiceException 
     */
    @Transactional(readOnly=false, rollbackFor=Exception.class)
    public ResultModel update(WorkAreaVo vo, Principal p) throws DaoException, ServiceException {
    	/**修改实体信息，注意清空不能由前端传递的参数值**/
    	MetaWorkArea entity = vo.getEntity();
    	if(entity == null) throw new ServiceException(ErrorCode.DATA_EMPTY);
    	if(StringUtil.isTrimEmpty(entity.getWorkAreaId())) throw new ServiceException(ErrorCode.KEY_EMPTY);
    	MetaWorkArea old = dao.selectOne(new MetaWorkArea().setWorkAreaNo(entity.getWorkAreaNo()).setOrgId(entity.getOrgId()));
    	if(old != null && !old.getWorkAreaId().equals(entity.getWorkAreaId())) throw new ServiceException(ErrorCode.NO_NOT_UNIQUE);
    	entity.clear();
    	entity.setUpdatePerson(p.getUserId());
        int r = dao.updateByPrimaryKeySelective(entity);
        if(r == 0) throw new DaoException(ErrorCode.DB_EXCEPTION);
        /**给前端返回VO**/
        return view(entity.getWorkAreaId(), p);
    }

    /**
     * 生效工作区
     * @param id 
     * @return
     * @throws DaoException 
     * @throws ServiceException 
     */
    @Transactional(readOnly=false, rollbackFor=Exception.class)
    public ResultModel enable(String id) throws DaoException, ServiceException {
    	ResultModel m = new ResultModel();
    	if(StringUtil.isTrimEmpty(id)) throw new ServiceException(ErrorCode.KEY_EMPTY);
    	MetaWorkArea old =  dao.selectByPrimaryKey(id);
    	if(old == null) throw new ServiceException(ErrorCode.DATA_EMPTY);
    	if(old.getWorkAreaStatus() != Constant.STATUS_OPEN) throw new ServiceException(ErrorCode.STATUS_NO_RIGHT);
    	MetaWorkArea obj = new MetaWorkArea();
    	obj.setWorkAreaId(id);
    	obj.setWorkAreaStatus(Constant.STATUS_ACTIVE);
        int r = dao.updateByPrimaryKeySelective(obj);
        if(r == 0) throw new DaoException(ErrorCode.DB_EXCEPTION);
        return m;
    }

    /**
     * 失效工作区
     * @param id 
     * @return
     * @throws DaoException 
     * @throws ServiceException 
     */
    @Transactional(readOnly=false, rollbackFor=Exception.class)
    public ResultModel disable(String id) throws DaoException, ServiceException {
    	ResultModel m = new ResultModel();
    	if(StringUtil.isTrimEmpty(id)) throw new ServiceException(ErrorCode.KEY_EMPTY);
    	MetaWorkArea old =  dao.selectByPrimaryKey(id);
    	if(old == null) throw new ServiceException(ErrorCode.DATA_EMPTY);
    	if(old.getWorkAreaStatus() != Constant.STATUS_ACTIVE) throw new ServiceException(ErrorCode.STATUS_NO_RIGHT);
    	MetaWorkArea obj = new MetaWorkArea();
    	obj.setWorkAreaId(id);
    	obj.setWorkAreaStatus(Constant.STATUS_OPEN);
        int r = dao.updateByPrimaryKeySelective(obj);
        if(r == 0) throw new DaoException(ErrorCode.DB_EXCEPTION);
        return m;
    }

    /**
     * 取消工作区
     */
    @Transactional(readOnly=false, rollbackFor=Exception.class)
	public ResultModel cancel(String id) throws ServiceException, DaoException {
    	ResultModel m = new ResultModel();
    	if(StringUtil.isTrimEmpty(id)) throw new ServiceException(ErrorCode.KEY_EMPTY);
    	MetaWorkArea old =  dao.selectByPrimaryKey(id);
    	if(old == null) throw new ServiceException(ErrorCode.DATA_EMPTY);
    	if(old.getWorkAreaStatus()!= Constant.STATUS_OPEN) throw new ServiceException(ErrorCode.STATUS_NO_RIGHT);
    	MetaWorkArea obj = new MetaWorkArea();
    	obj.setWorkAreaId(id);
    	obj.setWorkAreaStatus(Constant.STATUS_CANCEL);
        int r = dao.updateByPrimaryKeySelective(obj);
        if(r == 0) throw new DaoException(ErrorCode.DB_EXCEPTION);
        return m;
	}
}