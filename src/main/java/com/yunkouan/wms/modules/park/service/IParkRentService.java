package com.yunkouan.wms.modules.park.service;

import java.util.List;

import com.yunkouan.entity.ResultModel;
import com.yunkouan.wms.modules.park.vo.ParkRentVo;

/**
 * 仓库出租服务接口
 */
public interface IParkRentService {

    /**
     * 仓库出租列表数据分页查询
     * @param rent 
     * @param page 
     * @return
     */
    public ResultModel qryPageList(ParkRentVo reqParm) throws Exception;
    
    /**
     * 仓库出租列表查询
     * @param reqParm
     * @return
     * @throws Exception
     * @version 2017年3月8日 下午4:36:06<br/>
     * @author Aaron He<br/>
     */
    public List<ParkRentVo> qryList(ParkRentVo reqParm)throws Exception;

    /**
     * 查看仓库出租详情
     * @param rentId
     * @return
     * @version 2017年3月8日 上午10:41:11<br/>
     * @author Aaron He<br/>
     */
    public ParkRentVo view(String rentId)throws Exception;

    /**
     * 添加仓库出租
     * @param reqParm
     * @return
     * @version 2017年3月8日 上午10:41:33<br/>
     * @author Aaron He<br/>
     */
    public ParkRentVo add(ParkRentVo reqParm,String operator)throws Exception;

    /**
     * 修改仓库出租
     * @param reqParm
     * @return
     * @version 2017年3月8日 上午10:42:28<br/>
     * @author Aaron He<br/>
     */
    public ParkRentVo update(ParkRentVo reqParm,String operator)throws Exception;
    
    /**
     * 保存并生效
     * @return
     * @throws Exception
     */
    public ParkRentVo addAndEnable(ParkRentVo parkRentVo,String operator) throws Exception;

    /**
     * 生效仓库出租
     * @param rentId
     * @version 2017年3月8日 上午10:42:43<br/>
     * @author Aaron He<br/>
     */
    public void enable(String rentId,String operator)throws Exception;

    /**
     * 失效仓库出租
     * @param rentId
     * @version 2017年3月8日 上午10:42:52<br/>
     * @author Aaron He<br/>
     */
    public void disable(String rentId,String operator)throws Exception;
    
    /**
     * 警告
     * @param rentId
     * @version 2017年3月8日 下午6:06:02<br/>
     * @author Aaron He<br/>
     */
    public void warn()throws Exception;

}