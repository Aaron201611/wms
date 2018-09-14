package com.yunkouan.wms.modules.park.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yunkouan.entity.ResultModel;
import com.yunkouan.exception.BizException;
import com.yunkouan.wms.common.constant.BizStatus;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.common.service.BaseService;
import com.yunkouan.wms.common.util.IdUtil;
import com.yunkouan.wms.modules.park.dao.IParkWarnDao;
import com.yunkouan.wms.modules.park.entity.ParkWarn;
import com.yunkouan.wms.modules.park.service.IParkRentService;
import com.yunkouan.wms.modules.park.service.IParkWarnService;
import com.yunkouan.wms.modules.park.vo.ParkRentVo;
import com.yunkouan.wms.modules.park.vo.ParkWarnVo;
import com.yunkouan.wms.modules.send.entity.SendDelivery;

/**
 * 仓库出租告警服务实现类
 */
@Service
public class ParkWarnServiceImpl extends BaseService implements IParkWarnService {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
    private IParkWarnDao parkWarnDao;
	
	@Autowired
	private IParkRentService parkRentService;

	
	public static final int DEFAULT_PAGE_SIZE = 10;

	/**
	 * 仓库出租告警列表数据查询
	 * @param parkWarnVo
	 * @return
	 * @version 2017年3月8日 下午3:27:12<br/>
	 * @author Aaron He<br/>
	 */
    public ResultModel qryPageList(ParkWarnVo parkWarnVo) throws Exception{
    	if(parkWarnVo == null)
    		throw new BizException(BizStatus.PARAMETER_IS_NULL.getReasonPhrase());
    	
    	//查询出租管理的列表
    	ParkRentVo param = new ParkRentVo();
    	param.setOrgName(parkWarnVo.getOrgName());
    	param.setWarehouseName(parkWarnVo.getWarehouseName());
    	param.setMerchantName(parkWarnVo.getMerchantName());
    	param.getParkRent().setRentStatus(Constant.PARK_RENT_STATUS_ACTIVE);
    	List<ParkRentVo> rentVoList = parkRentService.qryList(param);
    	
    	ResultModel result = new ResultModel();
    	if(rentVoList == null || rentVoList.isEmpty()) return result;
    	
    	//查询预警列表
    	Map<String,ParkRentVo> rentMap = ParkRentVo.parseListToMap(rentVoList);   	
    	List<String> rentIdList = ParkRentVo.idToList(rentVoList);   	
    	parkWarnVo.setRentIdList(rentIdList);
    	parkWarnVo.getParkWarn().setWarnStatus(Constant.RENT_WARN_STATUS_OPEN);
		
		//分页设置
		Page<SendDelivery> page = null;
		if ( parkWarnVo.getCurrentPage() == null ) {
			parkWarnVo.setCurrentPage(0);
		}
		if ( parkWarnVo.getPageSize() == null ) {
			parkWarnVo.setPageSize(DEFAULT_PAGE_SIZE);
		}
		page = PageHelper.startPage(parkWarnVo.getCurrentPage()+1, parkWarnVo.getPageSize());
		
		List<ParkWarn> list = parkWarnDao.selectByExample(parkWarnVo.getConditionExample());
		
		if ( list == null || list.isEmpty() ) {
			return result;
		}
		result.setPage(page);		
		
		List<ParkWarnVo> results = new ArrayList<ParkWarnVo>();
		for (ParkWarn warn : list) {
			ParkWarnVo vo = new ParkWarnVo();
			vo.setParkWarn(warn);
			ParkRentVo rentVo = rentMap.get(warn.getRentId());
			vo.setParkRentVo(rentVo);
			results.add(vo);
		}	
		result.setList(results);
		return result;
		
    }
    
    
    public List<ParkWarn> qryList(ParkWarnVo parkWarnVo){
    	List<ParkWarn> list = parkWarnDao.selectByExample(parkWarnVo.getConditionExample());
    	return list;
    }

    /**
     * 查看仓库出租告警详情
     * @param warnId
     * @return
     * @version 2017年3月8日 下午3:27:41<br/>
     * @author Aaron He<br/>
     */
    public ParkWarnVo view(String warnId)throws Exception{
    	//获取警告信息
    	ParkWarn warn = parkWarnDao.selectByPrimaryKey(warnId);
    	//获取出租信息
    	ParkRentVo rentVo = parkRentService.view(warn.getRentId());
    	ParkWarnVo warnVo = new ParkWarnVo();
    	warnVo.setParkRentVo(rentVo);
    	warnVo.setParkWarn(warn);
    	
    	return warnVo;
    }

    /**
     * 添加仓库出租告警
     * @param warnVo
     * @return
     * @version 2017年3月8日 下午3:28:27<br/>
     * @author Aaron He<br/>
     */
    public int add(ParkWarnVo reqParam,String operator)throws Exception{
    	if(reqParam == null)
    		throw new BizException(BizStatus.PARAMETER_IS_NULL.getReasonPhrase());
    	
    	reqParam.getParkWarn().setWarnId(IdUtil.getUUID());
    	reqParam.getParkWarn().setCreatePerson(operator);
    	reqParam.getParkWarn().setUpdatePerson(operator);
    	reqParam.getParkWarn().setWarnStatus(Constant.RENT_WARN_STATUS_OPEN);
    	reqParam.getParkWarn().setWarnId2(context.getStrategy4Id().getWarnSeq());
    	int num = parkWarnDao.insertSelective(reqParam.getParkWarn());
    	return num;
    }

    /**
     * 关闭仓库出租告警提示信息
     * @param warnId
     * @return
     * @version 2017年3月8日 下午3:30:16<br/>
     * @author Aaron He<br/>
     */
    public int close(String warnId,String operator)throws Exception{
    	ParkWarn warn = parkWarnDao.selectByPrimaryKey(warnId);
    	warn.setWarnId(warnId);
    	warn.setUpdatePerson(operator);
    	warn.setUpdateTime(new Date());
    	warn.setWarnStatus(Constant.RENT_WARN_STATUS_CLOSE);
    	
    	int num = parkWarnDao.updateByPrimaryKeySelective(warn);
    	return num;
    }
    
    /**
     * 根据出租id关闭预警
     * @param rentId
     * @param userId
     * @throws Excepton
     */
    public void closeByRentId(String rentId,String userId)throws Exception{
    	ParkWarnVo parkWarnVo = new ParkWarnVo();
    	parkWarnVo.getParkWarn().setRentId(rentId);
    	
    	ParkWarn record = new ParkWarn();
    	record.setWarnStatus(Constant.RENT_WARN_STATUS_CLOSE);
    	record.setUpdatePerson(userId);
    	record.setUpdateTime(new Date());
    	
    	parkWarnDao.updateByExampleSelective(record, parkWarnVo.getConditionExample());
    	
    }

}