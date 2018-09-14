package com.yunkouan.wms.modules.meta.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yunkouan.entity.ResultModel;
import com.yunkouan.exception.BizException;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.common.service.BaseService;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.IdUtil;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.meta.dao.IVehicleDao;
import com.yunkouan.wms.modules.meta.entity.MetaVehicle;
import com.yunkouan.wms.modules.meta.service.IVehicleService;
import com.yunkouan.wms.modules.meta.vo.VehicleVo;

/**
 * 车辆管理服务类
 * @author Aaron
 *
 */
@Service
public class VehicleServiceImpl extends BaseService implements IVehicleService{

	@Autowired
	private IVehicleDao vehicleDao;

	/**
	 * 新增
	 */
	public VehicleVo add(VehicleVo vehicleVo,Principal p) throws Exception{
		if(vehicleVo == null) throw new BizException("data_is_null");
		
		//检查车牌号是否重复
		checkCarNoisDup(vehicleVo.getEntity().getCarNo());
		
		String id = IdUtil.getUUID();
		vehicleVo.getEntity().setId(id);
		vehicleVo.getEntity().setStatus(Constant.VEHICLE_STATUS_OPEN);
		vehicleVo.getEntity().setOrgId(p.getOrgId());
		vehicleVo.getEntity().setWarehouseId(LoginUtil.getWareHouseId());
		vehicleVo.getEntity().setCreatePerson(p.getUserId());
		vehicleVo.getEntity().setUpdatePerson(p.getUserId());
		vehicleDao.insertSelective(vehicleVo.getEntity());
		
		return vehicleVo;
	}
	
	/**
	 * 新增生效
	 * @param vehicleVo
	 * @param p
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public VehicleVo saveAndEnable(VehicleVo vehicleVo,Principal p) throws Exception{
		vehicleVo = addOrUpdate(vehicleVo,p);
		
		enable(vehicleVo.getEntity().getId(), p);
		
		vehicleVo.getEntity().setStatus(Constant.VEHICLE_STATUS_ACTIVE);
		
		return vehicleVo;
	}
	
	/**
	 * 检查车牌号是否已存在
	 * @param carNo
	 */
	public void checkCarNoisDup(String carNo){
		VehicleVo vcVo = new VehicleVo(new MetaVehicle());
		vcVo.getEntity().setCarNo(carNo);
		
		List<MetaVehicle> list = vehicleDao.selectByExample(vcVo.getExample());
		if(list != null && list.size()>0)
			throw new BizException("car_no_is_exist");
	}


	/**
	 * 更新
	 */
	public VehicleVo update(VehicleVo vehicleVo,Principal p) {
		if(vehicleVo == null) throw new BizException("data_is_null");
		
		vehicleVo.getEntity().setUpdatePerson(p.getUserId());
		vehicleDao.updateByPrimaryKeySelective(vehicleVo.getEntity());
		return vehicleVo;
	}
	
	/**
	 * 新增或修改
	 * @param vehicleVo
	 * @param p
	 * @return
	 * @throws Exception
	 */
	public VehicleVo addOrUpdate(VehicleVo vehicleVo,Principal p) throws Exception{
		if(vehicleVo == null) throw new BizException("data_is_null");
    	VehicleVo vo = null;
    	if(StringUtil.isNotBlank(vehicleVo.getEntity().getId())){
    		vo = update(vehicleVo,p);
    	}else{
    		vo = add(vehicleVo,p);
    	}
    	return vo;
	}

	/**
	 * 分页查询
	 */
	public ResultModel pageList(VehicleVo vehicleVo) {
		
		ResultModel result = new ResultModel();
		Page<VehicleVo> page = null;
		if ( vehicleVo.getCurrentPage() == null ) {
			vehicleVo.setCurrentPage(0);
		}
		if ( vehicleVo.getPageSize() == null ) {
			vehicleVo.setPageSize(20);
		}
		page = PageHelper.startPage(vehicleVo.getCurrentPage()+1, vehicleVo.getPageSize());
		
		//不查询取消状态的记录
		if(vehicleVo.getEntity().getStatus() == null){
			vehicleVo.setStatusLessThan(Constant.VEHICLE_STATUS_CANCAL);
		}
		List<MetaVehicle> recordList = vehicleDao.selectByExample(vehicleVo.getExample());
		if(recordList == null || recordList.isEmpty()) return null;
		result.setPage(page);
		
		List<VehicleVo> voList = new ArrayList<VehicleVo>();
		for(MetaVehicle vehicle:recordList){
			VehicleVo vo = new VehicleVo(new MetaVehicle());
			vo.setEntity(vehicle);
			voList.add(vo);
		}
		result.setList(voList);
		
		return result;
	}
	
	/**
	 * 查询
	 * @param vehicleVo
	 * @return
	 */
	public List<VehicleVo> list(VehicleVo vehicleVo){
		
		List<MetaVehicle> recordList = vehicleDao.selectByExample(vehicleVo.getExample());
		
		List<VehicleVo> voList = new ArrayList<VehicleVo>();
		for(MetaVehicle vehicle:recordList){
			VehicleVo vo = new VehicleVo(new MetaVehicle());
			vo.setEntity(vehicle);
			voList.add(vo);
		}
		return voList;
	}
	
	/**
	 * 查看
	 * @param vId
	 * @return
	 */
	public VehicleVo view(String vId){
		MetaVehicle record = vehicleDao.selectByPrimaryKey(vId);
		VehicleVo vo = new VehicleVo(new MetaVehicle());
		vo.setEntity(record);
		
		return vo;
	}

	/**
	 * 生效
	 */
	public void enable(String vId,Principal p) throws Exception{
		//检查状态 是否打开
		queryAndCheckStatus(vId,Constant.VEHICLE_STATUS_OPEN,"vehicle_stauts_is_not_open");
		
		MetaVehicle entity = new MetaVehicle();
		entity.setId(vId);
		entity.setStatus(Constant.VEHICLE_STATUS_ACTIVE);
		entity.setUpdatePerson(p.getUserId());
		vehicleDao.updateByPrimaryKeySelective(entity);
	}

	/**
	 * 失效
	 */
	public void disable(String vId,Principal p) throws Exception {
		//检查状态 是否生效
		queryAndCheckStatus(vId,Constant.VEHICLE_STATUS_ACTIVE,"vehicle_stauts_is_not_active");
		
		MetaVehicle entity = new MetaVehicle();
		entity.setId(vId);
		entity.setStatus(Constant.VEHICLE_STATUS_ACTIVE);
		entity.setUpdatePerson(p.getUserId());
		vehicleDao.updateByPrimaryKeySelective(entity);
		
	}

	/**
	 * 取消
	 */
	public void cancel(String vId,Principal p) throws Exception{
		//检查状态 是否打开
		queryAndCheckStatus(vId,Constant.VEHICLE_STATUS_OPEN,"vehicle_stauts_is_not_open");
		
		MetaVehicle entity = new MetaVehicle();
		entity.setId(vId);
		entity.setStatus(Constant.VEHICLE_STATUS_CANCAL);
		entity.setUpdatePerson(p.getUserId());
		vehicleDao.updateByPrimaryKeySelective(entity);
		
	}
	
	public void queryAndCheckStatus(String vId,int isStatus,String msg){
		MetaVehicle record = vehicleDao.selectByPrimaryKey(vId);
		if(record.getStatus() != isStatus)
			throw new BizException(msg);
	}
	
	
}
