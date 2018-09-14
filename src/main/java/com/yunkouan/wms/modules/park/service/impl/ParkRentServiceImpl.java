package com.yunkouan.wms.modules.park.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.StringUtil;
import com.yunkouan.entity.ResultModel;
import com.yunkouan.exception.BizException;
import com.yunkouan.saas.modules.sys.entity.MetaWarehouse;
import com.yunkouan.saas.modules.sys.service.IOrgService;
import com.yunkouan.saas.modules.sys.service.IWarehouseExtlService;
import com.yunkouan.wms.common.constant.BizStatus;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.common.service.BaseService;
import com.yunkouan.wms.common.util.FqDataUtils;
import com.yunkouan.wms.common.util.IdUtil;
import com.yunkouan.wms.modules.meta.service.IMerchantService;
import com.yunkouan.wms.modules.park.dao.IParkRentDao;
import com.yunkouan.wms.modules.park.entity.ParkRent;
import com.yunkouan.wms.modules.park.entity.ParkWarn;
import com.yunkouan.wms.modules.park.service.IParkRentService;
import com.yunkouan.wms.modules.park.service.IParkWarnService;
import com.yunkouan.wms.modules.park.vo.ParkRentVo;
import com.yunkouan.wms.modules.park.vo.ParkWarnVo;
import com.yunkouan.wms.modules.send.entity.SendDelivery;

import tk.mybatis.mapper.entity.Example;

/**
 * 仓库出租服务实现类
 */
@Service
public class ParkRentServiceImpl extends BaseService implements IParkRentService {

	protected Logger logger = LoggerFactory.getLogger(getClass());
    
	@Autowired
    private IParkRentDao parkRentDao;	
	
	@Autowired
	private IWarehouseExtlService wrehouseExtlService;
	
	@Autowired
	private IOrgService orgService;//企业服务
	
	@Autowired
	private IMerchantService merchantService;//客商服务
	
	
	@Autowired
	private IParkWarnService parkWarnService;//预警服务
	
	public static final int DEFAULT_PAGE_SIZE = 10;
	

    /**
     * 仓库出租列表数据分页查询
     * @param rent 
     * @param page 
     * @return
     */
    public ResultModel qryPageList(ParkRentVo reqParam) throws Exception{
    	if(reqParam == null)
    		throw new BizException(BizStatus.PARAMETER_IS_NULL.getReasonPhrase());
    	ResultModel result = new ResultModel();	
    	//根据企业名称查询id列表
    	List<String> orgList = null;
    	if(StringUtils.isNotEmpty(reqParam.getOrgName())){
    		orgList = orgService.list(reqParam.getOrgName(),Constant.ORGTYPE_PARK);
    		if(orgList.isEmpty()) return result;
    		reqParam.setOrgIdList(orgList);
    	}
    	//根据客商名称查询id列表
    	List<String> merchantList = null;
    	if(StringUtils.isNotEmpty(reqParam.getMerchantName())){
//    		merchantList = merchantService.list(reqParam.getMerchantName());
    		merchantList = orgService.list(reqParam.getMerchantName(),Constant.ORGTYPE_ORG);
    		if(merchantList.isEmpty()) return result;
    		reqParam.setMerchantIdList(merchantList);
    	}
    	//根据仓库名称查询id列表
    	List<MetaWarehouse> mlist = null;
    	List<String> wearhouseList = null;
    	if(StringUtils.isNotEmpty(reqParam.getWarehouseName())){
    		mlist = wrehouseExtlService.listWareHouseByName(reqParam.getWarehouseName(), true);
    		wearhouseList = mlist.stream().map(p->p.getWarehouseId()).collect(Collectors.toList());
    		if(wearhouseList.isEmpty()) return result;
    		reqParam.setWarehouseIdList(wearhouseList);
    	}
    	
		//分页设置
		Page<SendDelivery> page = null;
		if ( reqParam.getCurrentPage() == null ) {
			reqParam.setCurrentPage(0);
		}
		if ( reqParam.getPageSize() == null ) {
			reqParam.setPageSize(DEFAULT_PAGE_SIZE);
		}
		page = PageHelper.startPage(reqParam.getCurrentPage()+1, reqParam.getPageSize());
		//查询发货单列表	
		List<ParkRent> list = parkRentDao.selectByExample(reqParam.getConditionExample());
		if ( list == null || list.isEmpty() ) {
			return result;
		}
		result.setPage(page);		
		FqDataUtils fdu = new FqDataUtils();
		List<ParkRentVo> results = new ArrayList<ParkRentVo>();
		for (ParkRent rent : list) {
			ParkRentVo vo = new ParkRentVo();
			vo.setParkRent(rent);
			//查询企业
			vo.setOrgName(fdu.getOrgNameById(orgService, rent.getOrgId()));
			//查询客商
			vo.setMerchantName(fdu.getOrgNameById(orgService, rent.getMerchantId()));
			//查询仓库
			vo.setWarehouseName(fdu.getWarehouseNameById(wrehouseExtlService, rent.getWarehouseId()));
			results.add(vo);
		}	
		result.setList(results);
		return result;
    }
    
    /**
     * 仓库出租列表查询
     * @param reqParm
     * @return
     * @throws Exception
     * @version 2017年3月8日 下午4:36:06<br/>
     * @author Aaron He<br/>
     */
    public List<ParkRentVo> qryList(ParkRentVo reqParam)throws Exception{
    	if(reqParam == null)
    		throw new BizException(BizStatus.PARAMETER_IS_NULL.getReasonPhrase());
    	//根据企业名称查询id列表
    	List<String> orgList = null;//orgService.list(reqParam.getOrgName());
    	if(StringUtils.isNotEmpty(reqParam.getOrgName())){
			orgList = orgService.list(reqParam.getOrgName(),Constant.ORGTYPE_PARK);
			if(orgList.isEmpty()) return null;
			reqParam.setOrgIdList(orgList);
    	}
    	//根据客商名称查询id列表
    	List<String> merchantList = null;
    	if(StringUtils.isNotEmpty(reqParam.getMerchantName())){
//    		merchantList = merchantService.list(reqParam.getMerchantName());
    		merchantList = orgService.list(reqParam.getMerchantName(),Constant.ORGTYPE_ORG);
    		if(merchantList.isEmpty()) return null;
    		reqParam.setMerchantIdList(merchantList);
    	}
    	//根据仓库名称查询id列表
    	List<MetaWarehouse> mlist = null;
    	List<String> wearhouseList = null;
    	if(StringUtils.isNotEmpty(reqParam.getWarehouseName())){
    		mlist = wrehouseExtlService.listWareHouseByName(reqParam.getWarehouseName(), true);
    		wearhouseList = mlist.stream().map(p->p.getWarehouseId()).collect(Collectors.toList());
    		if(wearhouseList.isEmpty()) return null;
    		reqParam.setWarehouseIdList(wearhouseList);
    	}
    	reqParam.getParkRent().setRentStatus(Constant.PARK_RENT_STATUS_ACTIVE);
    	//查询发货单列表
		List<ParkRent> list = parkRentDao.selectByExample(reqParam.getConditionExample());
		List<ParkRentVo> voList = new ArrayList<ParkRentVo>();
		FqDataUtils fdu = new FqDataUtils();
		for (ParkRent parkRent : list) {
			ParkRentVo vo = new ParkRentVo();
			//查询企业
			vo.setOrgName(fdu.getOrgNameById(orgService, parkRent.getOrgId()));
			//查询租赁客商
			vo.setMerchantName(fdu.getOrgNameById(orgService, parkRent.getMerchantId()));
			//查询仓库
			vo.setWarehouseName(fdu.getWarehouseNameById(wrehouseExtlService, parkRent.getWarehouseId()));
			
			vo.setParkRent(parkRent);
			voList.add(vo);
		}
		return voList; 	
    }
  

    /**
     * 查看仓库出租详情 
     * @param rentId
     * @return
     * @version 2017年3月8日 上午10:41:11<br/>
     * @author Aaron He<br/>
     */
    public ParkRentVo view(String rentId) throws Exception{
    	if(StringUtil.isEmpty(rentId))
    		throw new BizException(BizStatus.PARAMETER_IS_NULL.getReasonPhrase());
    	ParkRent rent = parkRentDao.selectByPrimaryKey(rentId);
    	ParkRentVo vo = new ParkRentVo();
		vo.setParkRent(rent);
		FqDataUtils fdu = new FqDataUtils();
		//查询企业
		vo.setOrgName(fdu.getOrgNameById(orgService, rent.getOrgId()));
		//查询客商
		vo.setMerchantName(fdu.getOrgNameById(orgService, rent.getMerchantId()));
		//查询仓库
		vo.setWarehouseName(fdu.getWarehouseNameById(wrehouseExtlService, rent.getWarehouseId()));
		return vo;
    }

    /**
     * 添加仓库出租
     * @param reqParm
     * @return
     * @version 2017年3月8日 上午10:41:33<br/>
     * @author Aaron He<br/>
     */
    public ParkRentVo add(ParkRentVo reqParam,String operator) throws Exception{
    	if(reqParam == null)
    		throw new BizException(BizStatus.PARAMETER_IS_NULL.getReasonPhrase());
    	
    	if(hasRented(reqParam.getParkRent().getWarehouseId(),reqParam.getParkRent().getStartTime(),reqParam.getParkRent().getEndTime(),null))
    		throw new BizException(BizStatus.PARKRENT_WAREHOUSE_HAS_RENTED.getReasonPhrase());
    	
    	reqParam.getParkRent().setCreatePerson(operator);
    	reqParam.getParkRent().setUpdatePerson(operator);
    	reqParam.getParkRent().setRentId(IdUtil.getUUID());
    	reqParam.getParkRent().setRentStatus(Constant.PARK_RENT_STATUS_OPEN);
    	reqParam.getParkRent().setRentId2(context.getStrategy4Id().getRentSeq());
    	int flag = parkRentDao.insertSelective(reqParam.getParkRent());
    	
    	return reqParam;
    }

    /**
     * 修改仓库出租
     * @param reqParm
     * @return
     * @version 2017年3月8日 上午10:42:28<br/>
     * @author Aaron He<br/>
     */
    public ParkRentVo update(ParkRentVo reqParam,String operator){
    	if(reqParam == null)
    		throw new BizException(BizStatus.PARAMETER_IS_NULL.getReasonPhrase());
    	
    	if(hasRented(reqParam.getParkRent().getWarehouseId(),reqParam.getParkRent().getStartTime(),reqParam.getParkRent().getEndTime(),Arrays.asList(reqParam.getParkRent().getRentId())))
    		throw new BizException(BizStatus.PARKRENT_WAREHOUSE_HAS_RENTED.getReasonPhrase());
    	
    	reqParam.getParkRent().setUpdatePerson(operator);
    	reqParam.getParkRent().setUpdateTime(new Date());
    	int flag = parkRentDao.updateByPrimaryKeySelective(reqParam.getParkRent());
    	
    	return reqParam;
    }
    
    /**
     * 保存并生效
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor=Exception.class)
    public ParkRentVo addAndEnable(ParkRentVo parkRentVo,String operator) throws Exception{
    	ParkRentVo rentVo = new ParkRentVo();
		if(StringUtil.isEmpty(parkRentVo.getParkRent().getRentId())){
			rentVo = add(parkRentVo, operator);
		}else{
			rentVo = update(parkRentVo, operator);
		}
		enable(rentVo.getParkRent().getRentId(),operator);
		rentVo.getParkRent().setRentStatus(Constant.PARK_RENT_STATUS_ACTIVE);
		return rentVo;
    }

    /**
     * 生效仓库出租
     * @param rentId
     * @version 2017年3月8日 上午10:42:43<br/>
     * @author Aaron He<br/>
     */
    public void enable(String rentId,String operator) throws Exception{
    	if(StringUtils.isEmpty(rentId))
    		throw new BizException(BizStatus.PARAMETER_IS_NULL.getReasonPhrase());
    	
    	//检查是否打开状态
    	checkStatus(rentId,Constant.PARK_RENT_STATUS_OPEN,BizStatus.PARKRENT_STATUS_IS_NOT_OPEN.getReasonPhrase());
    	
    	//更新状态为生效
    	updateStatus(rentId, operator, Constant.PARK_RENT_STATUS_ACTIVE);
    	
    }
    

    /**
     * 失效仓库出租
     * 1、检查是否打开状态
     * 2、更新状态为生效
     * 3、关闭预警信息
     * @param rentId
     * @version 2017年3月8日 上午10:42:52<br/>
     * @author Aaron He<br/>
     */
    @Transactional(rollbackFor=Exception.class)
    public void disable(String rentId,String operator)throws Exception{
    	if(StringUtils.isEmpty(rentId))
    		throw new BizException(BizStatus.PARAMETER_IS_NULL.getReasonPhrase());
    	
    	//1、检查是否生效状态
    	checkStatus(rentId,Constant.PARK_RENT_STATUS_ACTIVE,BizStatus.PARKRENT_STATUS_IS_NOT_ENABLE.getReasonPhrase());
    	
    	//2、更新状态为生效
    	updateStatus(rentId, operator, Constant.PARK_RENT_STATUS_OPEN);
    	
    	//3、关闭预警信息
    	parkWarnService.closeByRentId(rentId, operator);
    }
    
    /**
     * 更新状态
     * @param rentId
     * @param operator
     * @version 2017年3月8日 下午1:26:11<br/>
     * @author Aaron He<br/>
     */
    public void updateStatus(String rentId,String operator,int status) throws Exception{
    	ParkRent rent = new ParkRent();
    	rent.setRentId(rentId);
    	rent.setUpdatePerson(operator);
    	rent.setUpdateTime(new Date());
    	rent.setRentStatus(status);
    	parkRentDao.updateByPrimaryKeySelective(rent);
    }
    
    /**
     * 检验仓库是否已经出租
     * @param warehouseId
     * @param startTime
     * @param endTime
     * @return
     */
    public boolean hasRented(String warehouseId,Date startTime,Date endTime,List<String> notInIds){
    	Example example = new Example(ParkRent.class);
    	example.createCriteria().andEqualTo("warehouseId",warehouseId)
    							.andBetween("startTime", startTime, endTime)
    							.andNotIn("rentId", notInIds);
    	example.or().andEqualTo("warehouseId",warehouseId).andBetween("endTime", startTime, endTime).andNotIn("rentId", notInIds);
    	example.or().andEqualTo("warehouseId",warehouseId).andLessThanOrEqualTo("startTime", startTime).andGreaterThanOrEqualTo("endTime", endTime).andNotIn("rentId", notInIds);
    	int num = parkRentDao.selectCountByExample(example);
    	if(num > 0 ) return true;
    	return false;
    	
    }
    
    /**
     * 检查状态
     * @param rentId
     * @param isStatus
     * @param msg
     * @version 2017年3月8日 下午1:27:29<br/>
     * @author Aaron He<br/>
     */
    public void checkStatus(String rentId,int isStatus,String msg){
    	ParkRent rent = parkRentDao.selectByPrimaryKey(rentId);
    	if(rent.getRentStatus().intValue() != isStatus)
    		throw new BizException(msg);
    }
    
    /**
     * 警告
     * @param rentId
     * @version 2017年3月8日 下午6:06:02<br/>
     * @author Aaron He<br/>
     */
    @Transactional(rollbackFor=Exception.class)
    public void warn() throws Exception{
    	//查询生效而且没有生成预警记录的出租记录列表
    	ParkRentVo rentVo = new ParkRentVo();
    	rentVo.getParkRent().setRentStatus(Constant.PARK_RENT_STATUS_ACTIVE);
    	
    	//查询已创建预警的出租id列表
    	ParkWarnVo parkWarnVo = new ParkWarnVo();
    	parkWarnVo.getParkWarn().setWarnStatus(Constant.RENT_WARN_STATUS_OPEN);
    	List<ParkWarn> warnList = parkWarnService.qryList(parkWarnVo);
    	List<String> notInRentIds = null;
    	if(warnList != null && !warnList.isEmpty()){
    		notInRentIds = warnList.stream().map(t->t.getRentId()).collect(Collectors.toList());
    		rentVo.setNotInRentIds(notInRentIds);
    	}
    	
    	List<ParkRent> list = parkRentDao.selectByExample(rentVo.getConditionExample());
    	
    	//根据仓库出租管理中的租期结束日期及预警提前期，触发生成预警记录
    	Date now = new Date();
//    	Date warnDate = DateUtils.addDays(rent.getEndTime(),-rent.getWarnDays());
    	for(ParkRent entity:list){
    		if(DateUtils.addDays(entity.getEndTime(),entity.getWarnDays()).compareTo(now) > 0){
    			ParkWarnVo warnVo = new ParkWarnVo();
        		ParkWarn warn = new ParkWarn();
        		warn.setWarnMsg(ParkWarnVo.PARK_RENT_EXPIRE);
        		warn.setRentId(entity.getRentId());
        		warn.setMerchantId(entity.getMerchantId());
        		warn.setOrgId(entity.getOrgId());
        		warnVo.setParkWarn(warn);
        		String operator = null;
        		parkWarnService.add(warnVo, operator);
    		}  		
    	}

    }
    
    public static void main(String[] args) {
    	Date now = new Date();
    	int amount = -5;
    	Date date = DateUtils.addDays(now, amount);
    	System.out.println(now.compareTo(date));
	}
    
}