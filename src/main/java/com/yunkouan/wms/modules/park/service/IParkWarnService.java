package com.yunkouan.wms.modules.park.service;

import java.util.List;

import com.yunkouan.entity.ResultModel;
import com.yunkouan.wms.modules.park.entity.ParkWarn;
import com.yunkouan.wms.modules.park.vo.ParkWarnVo;

/**
 * 出租告警服务接口
 */
public interface IParkWarnService {
	

	/**
	 * 仓库出租告警列表数据查询
	 * @param reqParam
	 * @return
	 * @version 2017年3月8日 下午3:27:12<br/>
	 * @author Aaron He<br/>
	 */
    public ResultModel qryPageList(ParkWarnVo reqParam)throws Exception;
    
    /**
     * 查询出租警告列表
     * @param parkWarnVo
     * @return
     */
    public List<ParkWarn> qryList(ParkWarnVo parkWarnVo);

    /**
     * 查看仓库出租告警详情
     * @param warnId
     * @return
     * @version 2017年3月8日 下午3:27:41<br/>
     * @author Aaron He<br/>
     */
    public ParkWarnVo view(String warnId)throws Exception;

    /**
     * 添加仓库出租告警
     * @param warnVo
     * @return
     * @version 2017年3月8日 下午3:28:27<br/>
     * @author Aaron He<br/>
     */
    public int add(ParkWarnVo warnVo,String operator)throws Exception;

    /**
     * 关闭仓库出租告警提示信息
     * @param warnId
     * @return
     * @version 2017年3月8日 下午3:30:16<br/>
     * @author Aaron He<br/>
     */
    public int close(String warnId,String operator)throws Exception;
    
    /**
     * 根据出租id关闭预警
     * @param rentId
     * @param userId
     * @throws Excepton
     */
    public void closeByRentId(String rentId,String userId)throws Exception;

}