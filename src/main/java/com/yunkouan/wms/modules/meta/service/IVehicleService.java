package com.yunkouan.wms.modules.meta.service;

import java.util.List;

import com.yunkouan.entity.ResultModel;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.modules.meta.vo.VehicleVo;

public interface IVehicleService {

	/**
	 * 新增
	 * @param vehicleVo
	 * @return
	 */
	public VehicleVo add(VehicleVo vehicleVo,Principal p) throws Exception;
	
	/**
	 * 检查车牌号是否已存在
	 * @param carNo
	 */
	public void checkCarNoisDup(String carNo);
	
	/**
	 * 新增生效
	 * @param vehicleVo
	 * @param p
	 * @return
	 * @throws Exception
	 */
	public VehicleVo saveAndEnable(VehicleVo vehicleVo,Principal p) throws Exception;
	
	/**
	 * 新增或修改
	 * @param vehicleVo
	 * @param p
	 * @return
	 * @throws Exception
	 */
	public VehicleVo addOrUpdate(VehicleVo vehicleVo,Principal p) throws Exception;
	
	/**
	 * 修改
	 * @param vehicleVo
	 * @return
	 */
	public VehicleVo update(VehicleVo vehicleVo,Principal p) throws Exception;
	
	/**
	 * 分页查询
	 * @param vehicleVo
	 * @return
	 */
	public ResultModel pageList(VehicleVo vehicleVo);
	
	/**
	 * 查询
	 * @param vehicleVo
	 * @return
	 */
	public List<VehicleVo> list(VehicleVo vehicleVo);
	
	/**
	 * 查看
	 * @param vId
	 * @return
	 */
	public VehicleVo view(String vId);
	
	/**
	 * 生效
	 * @param vId
	 */
	public void enable(String vId,Principal p) throws Exception;
	
	/**
	 * 失效
	 * @param vId
	 */
	public void disable(String vId,Principal p) throws Exception;
	
	/**
	 * 取消
	 * @param vId
	 */
	public void cancel(String vId,Principal p) throws Exception;
}
