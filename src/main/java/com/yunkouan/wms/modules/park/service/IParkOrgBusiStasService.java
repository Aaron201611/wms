package com.yunkouan.wms.modules.park.service;

import java.util.List;

import com.yunkouan.entity.ResultModel;
import com.yunkouan.wms.modules.park.vo.ParkOrgBusiStasVo;

/**
 * 企业业务统计服务接口
 *@Description TODO
 *@author Aaron
 *@date 2017年3月9日 下午7:36:43
 *version v1.0
 */
public interface IParkOrgBusiStasService {

	/**
	 * 查询分页信息
	 * @param reqParam
	 * @return
	 * @version 2017年3月9日 下午7:59:33<br/>
	 * @author Aaron He<br/>
	 */
	public ResultModel qryPageList(ParkOrgBusiStasVo reqParam);
	
	/**
	 * 新增
	 * @param reqParam
	 * @return
	 * @version 2017年3月10日 上午11:54:06<br/>
	 * @author Aaron He<br/>
	 */
	public ParkOrgBusiStasVo add(ParkOrgBusiStasVo reqParam);
	
	/**
	 * 新增统计记录
	 * @param docType
	 * @param busiNo
	 * @param orgId
	 * @param warehouseId
	 * @param qty
	 * @param weight
	 * @param volume
	 * @param operator
	 * @return
	 * @throws Exception
	 */
	public ParkOrgBusiStasVo newBusiStas(Integer docType,String busiNo,String orgId,String warehouseId,double qty,double weight,double volume,String operator)throws Exception;
}
