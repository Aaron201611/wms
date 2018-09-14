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
import com.yunkouan.wms.modules.meta.dao.IMachineDao;
import com.yunkouan.wms.modules.meta.entity.MetaMachine;
import com.yunkouan.wms.modules.meta.service.IMachineService;
import com.yunkouan.wms.modules.meta.vo.MachineVo;

/**
 * 设备服务实现类
 */
@Service
@Transactional(readOnly=true)
public class MachineServieImpl extends BaseService implements IMachineService {
    /**设备数据层接口*/
	@Autowired
    private IMachineDao dao;

    /**
     * 设备列表数据查询
     * @param vo 
     * @param page 
     * @return
     */
    public ResultModel list(MachineVo vo, Principal p) throws DaoException, ServiceException {
    	vo.getEntity().setOrgId(p.getOrgId());
    	/**设置分页参数**/
		Page<MetaMachine> page = PageHelper.startPage(vo.getCurrentPage()+1, vo.getPageSize());
		/**调用通用查询接口**/
    	List<MetaMachine> list = dao.selectByExample(vo.getExample());
    	/**把实体类封装成VO**/
    	List<MachineVo> r = new ArrayList<MachineVo>();
    	if(list != null) for(int i=0; i<list.size(); ++i) {
    		r.add(new MachineVo(list.get(i)));
    	}
    	/**返回前端所需的分页数据和VO**/
    	return new ResultModel().setPage(page).setList(r);
    }

    /**
     * 查询设备详情
     * @param id 
     * @return
     */
    public ResultModel view(String id, Principal p) throws DaoException, ServiceException {
    	if(StringUtil.isTrimEmpty(id)) throw new ServiceException(ErrorCode.KEY_EMPTY);
    	MetaMachine obj =  dao.selectByPrimaryKey(id);
        return new ResultModel().setObj(new MachineVo(obj));
    }

    /**
     * 添加设备
     * @param vo 
     * @return
     * @throws DaoException 
     * @throws ServiceException 
     */
    @Transactional(readOnly=false, rollbackFor=Exception.class)
    public ResultModel add(MachineVo vo, Principal p) throws DaoException, ServiceException {
    	/**保存实体信息，注意清空不能由前端传递的参数值**/
    	MetaMachine entity = vo.getEntity();
    	if(entity == null) throw new ServiceException(ErrorCode.DATA_EMPTY);
    	entity.clear();
    	String id = IdUtil.getUUID();
    	entity.setMachineId(id);
    	entity.setCreatePerson(p.getUserId());
    	entity.setMachineStatus(Constant.STATUS_OPEN);
    	entity.setOrgId(p.getOrgId());
    	entity.setWarehouseId(LoginUtil.getWareHouseId());
    	entity.setMachineId2(context.getStrategy4Id().getMachineSeq());
        int r = dao.insertSelective(entity);
        if(r == 0) throw new DaoException(ErrorCode.DB_EXCEPTION);
        /**给前端返回VO**/
        return view(id, p);
    }

    /**
     * 修改设备
     * @param vo 
     * @return
     * @throws DaoException 
     * @throws ServiceException 
     */
    @Transactional(readOnly=false, rollbackFor=Exception.class)
    public ResultModel update(MachineVo vo, Principal p) throws DaoException, ServiceException {
    	/**修改实体信息，注意清空不能由前端传递的参数值**/
    	MetaMachine entity = vo.getEntity();
    	if(entity == null) throw new ServiceException(ErrorCode.DATA_EMPTY);
    	if(StringUtil.isTrimEmpty(entity.getMachineId())) throw new ServiceException(ErrorCode.KEY_EMPTY);
    	MetaMachine old = dao.selectOne(new MetaMachine().setMachineNo(entity.getMachineNo()).setOrgId(entity.getOrgId()));
    	if(old != null && !old.getMachineId().equals(entity.getMachineId())) throw new ServiceException(ErrorCode.NO_NOT_UNIQUE);
    	entity.clear();
    	entity.setUpdatePerson(p.getUserId());
        int r = dao.updateByPrimaryKeySelective(entity);
        if(r == 0) throw new DaoException(ErrorCode.DB_EXCEPTION);
        /**给前端返回VO**/
        return view(entity.getMachineId(), p);
    }

    /**
     * 生效设备
     * @param id 
     * @return
     * @throws DaoException 
     * @throws ServiceException 
     */
    @Transactional(readOnly=false, rollbackFor=Exception.class)
    public ResultModel enable(String id) throws DaoException, ServiceException {
    	ResultModel m = new ResultModel();
    	if(StringUtil.isTrimEmpty(id)) throw new ServiceException(ErrorCode.KEY_EMPTY);
    	MetaMachine old =  dao.selectByPrimaryKey(id);
    	if(old == null) throw new ServiceException(ErrorCode.DATA_EMPTY);
    	if(old.getMachineStatus() != Constant.STATUS_OPEN) throw new ServiceException(ErrorCode.STATUS_NO_RIGHT);
    	MetaMachine obj = new MetaMachine();
    	obj.setMachineId(id);
    	obj.setMachineStatus(Constant.STATUS_ACTIVE);
        int r = dao.updateByPrimaryKeySelective(obj);
        if(r == 0) throw new DaoException(ErrorCode.DB_EXCEPTION);
        return m;
    }

    /**
     * 失效设备
     * @param id 
     * @return
     * @throws DaoException 
     * @throws ServiceException 
     */
    @Transactional(readOnly=false, rollbackFor=Exception.class)
    public ResultModel disable(String id) throws DaoException, ServiceException {
    	ResultModel m = new ResultModel();
    	if(StringUtil.isTrimEmpty(id)) throw new ServiceException(ErrorCode.KEY_EMPTY);
    	MetaMachine old =  dao.selectByPrimaryKey(id);
    	if(old == null) throw new ServiceException(ErrorCode.DATA_EMPTY);
    	if(old.getMachineStatus() != Constant.STATUS_ACTIVE) throw new ServiceException(ErrorCode.STATUS_NO_RIGHT);
    	MetaMachine obj = new MetaMachine();
    	obj.setMachineId(id);
    	obj.setMachineStatus(Constant.STATUS_OPEN);
        int r = dao.updateByPrimaryKeySelective(obj);
        if(r == 0) throw new DaoException(ErrorCode.DB_EXCEPTION);
        return m;
    }

    /**
     * 取消设备
     */
    @Transactional(readOnly=false, rollbackFor=Exception.class)
	public ResultModel cancel(String id) throws ServiceException, DaoException {
    	ResultModel m = new ResultModel();
    	if(StringUtil.isTrimEmpty(id)) throw new ServiceException(ErrorCode.KEY_EMPTY);
    	MetaMachine old =  dao.selectByPrimaryKey(id);
    	if(old == null) throw new ServiceException(ErrorCode.DATA_EMPTY);
    	if(old.getMachineStatus() != Constant.STATUS_OPEN) throw new ServiceException(ErrorCode.STATUS_NO_RIGHT);
    	MetaMachine obj = new MetaMachine();
    	obj.setMachineId(id);
    	obj.setMachineStatus(Constant.STATUS_CANCEL);
        int r = dao.updateByPrimaryKeySelective(obj);
        if(r == 0) throw new DaoException(ErrorCode.DB_EXCEPTION);
        return m;
	}
}