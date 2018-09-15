package com.yunkouan.wms.modules.send.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yunkouan.entity.ResultModel;
import com.yunkouan.exception.BizException;
import com.yunkouan.saas.modules.sys.entity.MetaLocation;
import com.yunkouan.saas.modules.sys.service.IUserService;
import com.yunkouan.saas.modules.sys.service.IWarehouseExtlService;
import com.yunkouan.util.NumberUtil;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.common.constant.BizStatus;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.common.service.BaseService;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.FqDataUtils;
import com.yunkouan.wms.common.util.IdUtil;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.inv.entity.InvLog;
import com.yunkouan.wms.modules.inv.entity.InvStock;
import com.yunkouan.wms.modules.inv.service.IStockService;
import com.yunkouan.wms.modules.inv.vo.InvStockVO;
import com.yunkouan.wms.modules.meta.entity.MetaMerchant;
import com.yunkouan.wms.modules.meta.entity.MetaSku;
import com.yunkouan.wms.modules.meta.service.ILocationExtlService;
import com.yunkouan.wms.modules.meta.service.IMerchantService;
import com.yunkouan.wms.modules.meta.service.IPackService;
import com.yunkouan.wms.modules.meta.service.ISkuService;
import com.yunkouan.wms.modules.monitor.entity.ExceptionLog;
import com.yunkouan.wms.modules.monitor.service.IExceptionLogService;
import com.yunkouan.wms.modules.park.service.IParkOrgBusiStasService;
import com.yunkouan.wms.modules.send.dao.IDeliveryDao;
import com.yunkouan.wms.modules.send.dao.IPickDao;
import com.yunkouan.wms.modules.send.dao.IPickDetailDao;
import com.yunkouan.wms.modules.send.dao.IPickLocationDao;
import com.yunkouan.wms.modules.send.entity.SendDelivery;
import com.yunkouan.wms.modules.send.entity.SendPick;
import com.yunkouan.wms.modules.send.entity.SendPickDetail;
import com.yunkouan.wms.modules.send.entity.SendPickLocation;
import com.yunkouan.wms.modules.send.entity.SendWave;
import com.yunkouan.wms.modules.send.service.AllocateStrategy;
import com.yunkouan.wms.modules.send.service.ICreatePickService;
import com.yunkouan.wms.modules.send.service.IDeliveryService;
import com.yunkouan.wms.modules.send.service.IPickDetailService;
import com.yunkouan.wms.modules.send.service.IPickLocationService;
import com.yunkouan.wms.modules.send.service.IPickService;
import com.yunkouan.wms.modules.send.service.ISendDeliveryLogService;
import com.yunkouan.wms.modules.send.service.IWaveService;
import com.yunkouan.wms.modules.send.util.ExceptionLogUtil;
import com.yunkouan.wms.modules.send.util.PickSplitUtil;
import com.yunkouan.wms.modules.send.util.ServiceConstant;
import com.yunkouan.wms.modules.send.util.StockOperate;
import com.yunkouan.wms.modules.send.vo.SendDeliveryVo;
import com.yunkouan.wms.modules.send.vo.SendPickDetailVo;
import com.yunkouan.wms.modules.send.vo.SendPickLocationVo;
import com.yunkouan.wms.modules.send.vo.SendPickVo;
import com.yunkouan.wms.modules.send.vo.SendWaveVo;
import com.yunkouan.wms.modules.ts.service.ITaskService;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 拣货单业务实现类
 */
@Service
public class PickServiceImpl extends BaseService implements IPickService {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	public IPickDao pickDao;

	@Autowired
	public IPickDetailDao pickDetailDao;
	
	@Autowired
	public IDeliveryDao deliveryDao;
	
	@Autowired
	public IPickDetailService pickDetailService;
	
	@Autowired
	public IPickLocationService pickLocationService;
	
	@Autowired
	public IPickLocationDao pickLocationDao;
	
	@Autowired
	public IDeliveryService deliveryService;
	
	@Autowired
	public IStockService stockService;//库存服务
	
	@Autowired 
	public IMerchantService merchantService;//客商接口
	
	@Autowired 
	public IPackService packService;//包装接口
	
	@Autowired
	public IWaveService waveService;//波次单接口
	
	@Autowired 
	public ISkuService skuService;//货品接口
	
	@Autowired
	private IWarehouseExtlService wrehouseExtlService;//仓库服务
	
	@Autowired
	private IExceptionLogService exceptionLogService;//异常服务
	
//	@Autowired
//	private ICommonService commonService;
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private ILocationExtlService locExtlService;
	
	@Autowired
	private ITaskService taskService;
	
	@Autowired
	private ILocationExtlService locationExtlService;//库位服务
	
	@Autowired
	private IParkOrgBusiStasService parkOrgBusiStasService;//园区企业业务统计
	
	@Autowired
	private ISendDeliveryLogService deliveryLogService;
	
	@Autowired
	private TransactionTemplate transactionTemplate;	
	
	public static final int DEFAULT_PAGE_SIZE = 10;//默认页面记录数

	/**
	 * 查询拣货单列表
	 * @param sendPickVo
	 * @param orgId
	 * @param wareHouseId
	 * @return
	 * @version 2017年2月16日 下午1:24:45<br/> 
	 * @author Aaron He<br/>
	 */
	public ResultModel qryPageList(SendPickVo sendPickVo)throws Exception{
		
		ResultModel result = new ResultModel();
		if(sendPickVo == null || sendPickVo.getSendPick() == null) return result;
		//根据单号查询对象
		if(StringUtils.isNotEmpty(sendPickVo.getDeliveryNo())) {
			List<SendDelivery> entitys = deliveryService.qryByDeliveryNo(sendPickVo.getDeliveryNo(), sendPickVo.getSendPick().getOrgId(), sendPickVo.getSendPick().getWarehouseId());			
			if(entitys == null || entitys.isEmpty()) return result;
			List<String> deliveryIds = entitys.stream().map(t->t.getDeliveryId()).collect(Collectors.toList());
			sendPickVo.setDetailIdList(deliveryIds);
			
		}else if(StringUtils.isNotEmpty(sendPickVo.getWaveNo())){
			List<SendWave> entitys = waveService.qryByWaveNo(sendPickVo.getWaveNo(), sendPickVo.getSendPick().getOrgId(), sendPickVo.getSendPick().getWarehouseId());
			if(entitys == null || entitys.isEmpty()) return result;
			List<String> waveIds = entitys.stream().map(t->t.getWaveId()).collect(Collectors.toList());
				sendPickVo.setWaveIdList(waveIds);		
		}
		if(StringUtils.isNotEmpty(sendPickVo.getOwnerName())){
			//查询owner
			List<String> merchantList = merchantService.list(sendPickVo.getOwnerName());
			if(merchantList == null || merchantList.isEmpty()) return result;
			sendPickVo.setOwnerList(merchantList);
		}
		//如果不选择状态则默认查询除取消以外的所有状态
		if(sendPickVo.getSendPick().getPickStatus() == null){
			sendPickVo.setPickStatusList(Arrays.asList(Constant.PICK_STATUS_ACTIVE,
														Constant.PICK_STATUS_FINISH,
														Constant.PICK_STATUS_OPEN,
														Constant.PICK_STATUS_WORKING));
		}
		
		//分页设置
		Page<SendPick> page = null;
		if ( sendPickVo.getCurrentPage() == null ) {
			sendPickVo.setCurrentPage(0);
		}
		if ( sendPickVo.getPageSize() == null ) {
			sendPickVo.setPageSize(DEFAULT_PAGE_SIZE);
		}
		page = PageHelper.startPage(sendPickVo.getCurrentPage()+1, sendPickVo.getPageSize());
		//查询发货单列表
		List<SendPick> list = pickDao.selectByExample(sendPickVo.getConditionExample());
		if ( list == null || list.isEmpty() ) {
			return result;
		}
		result.setPage(page);		
		
		List<SendPickVo> results = new ArrayList<SendPickVo>();
		for (SendPick entity : list) {
			SendPickVo vo = new SendPickVo();		
			vo.setSendPick(entity);
			MetaMerchant metaMerchant = merchantService.get(entity.getOwner());
			if (metaMerchant != null) {
				vo.setOwnerName(metaMerchant.getMerchantName());
				vo.setMerchantShortName(metaMerchant.getMerchantShortName());
			}
			//查询发货单号
			vo.setDeliveryNo(FqDataUtils.getDeliveryNoById(deliveryService, entity.getDeliveryId()));
			//查询波次单号
			vo.setWaveNo(FqDataUtils.getWaveNoById(waveService, entity.getWaveId()));
			//查询作业人员
			vo.setOpPersonName(FqDataUtils.getUserNameById(userService, entity.getOpPerson()));
			
			results.add(vo);
		}	
		result.setList(results);
		return result;		
	}
	
	
	/**
	 * 查询打印分页
	 * @param sendPickVo
	 * @param p
	 * @return
	 * @throws Exception
	 */
	public ResultModel qryPrintPageList(SendPickVo sendPickVo,Principal p) throws Exception{
		ResultModel result = new ResultModel();
		if(sendPickVo == null || sendPickVo.getSendPick() == null) return result;
		//分页设置
		Page<SendPickVo> page = null;
		if ( sendPickVo.getCurrentPage() == null ) {
			sendPickVo.setCurrentPage(0);
		}
		if ( sendPickVo.getPageSize() == null ) {
			sendPickVo.setPageSize(DEFAULT_PAGE_SIZE);
		}
		page = PageHelper.startPage(sendPickVo.getCurrentPage()+1, sendPickVo.getPageSize());
		
		sendPickVo.setOrgId(p.getOrgId());
		sendPickVo.setWarehouseId(LoginUtil.getWareHouseId());
		List<SendPick> pickList = pickDao.qryPrintList(sendPickVo);
		
		if ( pickList == null || pickList.isEmpty() ) {
			return result;
		}
		result.setPage(page);
		
		List<String> deliveryIdsList = new ArrayList<String>();
		for(SendPick pick:pickList){
			deliveryIdsList.add(pick.getDeliveryId());
		}
		//查询发货单列表
		SendDeliveryVo deliveryVo = new SendDeliveryVo();
		deliveryVo.setLoadConfirmIds(deliveryIdsList);
		List<SendDeliveryVo> deliveryVoList = deliveryService.qryByIds(deliveryIdsList);
		Map<String,SendDeliveryVo> map = new HashMap<String,SendDeliveryVo>();
		for(SendDeliveryVo vo:deliveryVoList){
			map.put(vo.getSendDelivery().getDeliveryId(), vo);
		}
		
		List<SendPickVo> pickVoList = new ArrayList<SendPickVo>();
		for(SendPick pick:pickList){
			SendPickVo pickVo = new SendPickVo();
			pickVo.setSendPick(pick);
			pickVo.setSendDeliveryVo(map.get(pick.getDeliveryId()));
			pickVoList.add(pickVo);
		}
		result.setList(pickVoList);
		return result;
		
		
	}
	
	/**
	 * 查询拣货单列表
	 * @param param
	 * @return
	 */
	public List<SendPickVo> qryListByParam(SendPickVo param){
		param.setOrgIdAndWareHouseId();
		List<SendPickVo> results = new ArrayList<SendPickVo>();
		List<SendPick> list = pickDao.selectByExample(param.getConditionExample());
		if ( list == null || list.isEmpty() ) {
			return null;
		}
		for (SendPick entity : list) {
			SendPickVo vo = new SendPickVo();		
			vo.setSendPick(entity);
			
			results.add(vo);
		}	
		return results;		
	}
	/**
	 * 查询拣货单列表
	 * @param param
	 * @return
	 */
	public List<SendPickVo> qryListByParam2(SendPickVo param){
		List<SendPickVo> results = new ArrayList<SendPickVo>();
		List<SendPick> list = pickDao.selectByExample(param.getConditionExample());
		if ( list == null || list.isEmpty() ) {
			return null;
		}
		for (SendPick entity : list) {
			SendPickVo vo = new SendPickVo();		
			vo.setSendPick(entity);
			
			results.add(vo);
		}	
		return results;		
	}
	/**
	 * 创建并保存拣货单
	 * 
	 * 1、生成拣货单，生成拣货单明细
	 * 2、生成计划拣货库位明细
	 * 3、创建拣货库位明细，按货品先进先出原则分配数量，分配库位
	 * @param pickVo
	 * @param oprator
	 * @throws Exception
	 * @version 2017年3月3日 下午2:45:21<br/>
	 * @author Aaron He<br/>
	 */
	@Transactional(rollbackFor=Exception.class)
	public SendPickVo createPickAndSave(SendPickVo reqVo,String pickNo,String operator)throws Exception{
		if(reqVo == null) 
			throw new BizException(BizStatus.DATA_IS_NULL.getReasonPhrase());
		
		//1、生成拣货单，生成拣货单明细
		SendPickVo pickVo = null;
		//若waveId存在，则按波次单创建拣货单
		if(!StringUtil.isTrimEmpty(reqVo.getSendPick().getWaveId()))
			pickVo = ((ICreatePickService)waveService).createPick(reqVo.getSendPick().getWaveId(), operator);
		//若发货单id存在，则按发货单创建拣货单
		if(!StringUtil.isTrimEmpty(reqVo.getSendPick().getDeliveryId()))
			pickVo = ((ICreatePickService)deliveryService).createPick(reqVo.getSendPick().getDeliveryId(), operator);	
		//创建拣货单号
		if(StringUtils.isEmpty(pickNo)) {
			Principal p = LoginUtil.getLoginUser();
			pickNo = context.getStrategy4No(p.getOrgId(), LoginUtil.getWareHouseId()).getPickNo(p.getOrgId(), reqVo.getSendPick().getDocType());
		}
		pickVo.getSendPick().setPickNo(pickNo);
		
		//2、生成计划拣货库位明细
		if(reqVo.getSendPickDetailVoList() != null && !reqVo.getSendPickDetailVoList().isEmpty()){
			pickVo.createPlanLocationsByHandle(reqVo,operator);
		}				
		//保存拣货单
		saveNewPick(pickVo,operator);
		pickVo.setTsTaskVo(reqVo.getTsTaskVo());
		
		return pickVo;
	}
	
	/**
	 * 生成拣货单
	 * @param pickVo
	 * @param operator
	 * @throws Exception
	 * @version 2017年2月22日 上午11:40:51<br/>
	 * @author Aaron He<br/>
	 */
	public void SavePickAndAssgn(SendPickVo pickVo,String operator)throws Exception{
		Principal loginUser = LoginUtil.getLoginUser();
		//创建拣货单号
		String pickNo = context.getStrategy4No(loginUser.getOrgId(), LoginUtil.getWareHouseId()).getPickNo(loginUser.getOrgId(), pickVo.getSendPick().getDocType());
		pickVo.getSendPick().setPickNo(pickNo);
		//自动分配库位
		pickVo = context.getStrategy4Pickup(loginUser.getOrgId(), LoginUtil.getWareHouseId()).allocation_new(pickVo, operator);
		//创建计划拣货库位
		if(pickVo.getSendPickDetailVoList() != null && !pickVo.getSendPickDetailVoList().isEmpty()){
			pickVo.createPlanLocationsByHandle(pickVo,operator);
		}
		//保存拣货单
		saveNewPick(pickVo,operator);
	}
	
	/**
	 * 保存并生效
	 * 1、根据拣货id判断是否新增或更新拣货单
	 * 2、生效拣货单
	 * @param pickVo
	 * @param pickNo
	 * @param oprator
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public SendPickVo saveAndEnable(SendPickVo sendPickVo,String userId)throws Exception{
		SendPickVo vo = new SendPickVo();		
		//1、根据拣货id判断是否新增或更新拣货单
		if(StringUtils.isEmpty(sendPickVo.getSendPick().getPickId())){
			vo = createPickAndSave(sendPickVo,null,userId);
		}else{
			vo = update(sendPickVo, userId);
		}
		//2、生效拣货单
		active(vo.getSendPick().getPickId(), userId);
		vo.getSendPick().setPickStatus(Constant.PICK_STATUS_ACTIVE);
		return vo;
	}
	
	/**
	 * 保存新拣货单
	 * @param pickVo
	 * @param operator
	 * @return
	 * @throws Exception
	 * @version 2017年2月21日 下午5:52:34<br/>
	 * @author Aaron He<br/>
	 */
	public void saveNewPick(SendPickVo pickVo,String operator) throws Exception{		
		
		if(pickVo.getSendPickDetailVoList() != null && !pickVo.getSendPickDetailVoList().isEmpty()){
			//统计拣货单计划拣货总数
			pickVo.calOrder();
			pickVo.getSendPick().defaultValue();
			//提交事务
			transactionTemplate.execute(new TransactionCallbackWithoutResult() {

				protected void doInTransactionWithoutResult(TransactionStatus arg0) {
					//保存拣货单
					pickVo.getSendPick().setPickId2(context.getStrategy4Id().getPickSeq());
					pickDao.insertSelective(pickVo.getSendPick());
					//保存拣货明细
					pickVo.getSendPickDetailVoList().stream().forEach(t->{
						//创建拣货明细
						SendPickDetail pickDetail = t.getSendPickDetail();
						pickDetail.setPickDetailId2(context.getStrategy4Id().getPickDetailSeq());
						pickDetailDao.insertSelective(pickDetail);
						//保存拣货库位
						if(t.getPlanPickLocations() != null && !t.getPlanPickLocations().isEmpty()){
							for(SendPickLocationVo locationVo:t.getPlanPickLocations()){
								locationVo.getSendPickLocation().setPickLocationId2(context.getStrategy4Id().getPickLocationSeq());
								pickLocationDao.insertSelective(locationVo.getSendPickLocation());
							}			
						}//if
					});
				}//doInTransactionWithoutResult
				
			});//transactionTemplate
		}//if		
	}
	
	
	/**
	 * 更新拣货单
	 * 1、检查状态是否打开
	 * 2、检验计划拣货明细与库位计划拣货数量之和是否一致
	 * 3、对比修改后的拣货单的单据类型，若类型改变则新建拣货单
	 * 4、统计拣货单计划拣货总数
	 * 5、更新拣货单
	 * @param pickVo
	 * @param operator
	 * @return
	 * @throws Exception
	 * @version 2017年2月22日 上午10:15:36<br/>
	 * @author Aaron He<br/>
	 */
	@Transactional(rollbackFor=Exception.class)
	public SendPickVo update(SendPickVo pickVo,String operator) throws Exception{
		
		//1、检查状态是否打开
		int status = getPickStatus(pickVo.getSendPick().getPickId());
		if(status != Constant.PICK_STATUS_OPEN)
			throw new BizException(BizStatus.PICK_STATUS_IS_NOT_OPEN.getReasonPhrase());
		//2、检验计划拣货明细与库位计划拣货数量之和是否一致
		for (SendPickDetailVo t : pickVo.getSendPickDetailVoList()) {
			SendPickDetail pickDetail = t.getSendPickDetail();
			checkPlanQty(t.getPlanPickLocations(),
					pickDetail.getOrderQty(),
					pickDetail.getOrderWeight().doubleValue(),
					pickDetail.getOrderVolume().doubleValue());
		}
		//获取原拣货单
		SendPickVo pickVo_org = getPickVoById(pickVo.getSendPick().getPickId());
		
		//3、对比修改后的拣货单的单据类型，若类型改变则新建拣货单
		if(pickVo.getSendPick().getDocType().intValue() != pickVo_org.getSendPick().getDocType().intValue()){
			//删除原拣货单
			updateStatus(pickVo.getSendPick().getPickId(),operator,Constant.PICK_STATUS_CANCAL);
			//新建拣货单
			pickVo = createPickAndSave(pickVo,pickVo_org.getSendPick().getPickNo(),operator);
			
		}else{
			//若源发货单改变则新建拣货单
			if((pickVo.getSendPick().getDocType().intValue() == Constant.PICKTYPE_FORM_DELIVERY)
				&& !pickVo.getSendPick().getDeliveryId().equals(pickVo_org.getSendPick().getDeliveryId())){
				//删除原拣货单
				updateStatus(pickVo.getSendPick().getPickId(),operator,Constant.PICK_STATUS_CANCAL);
				//新建拣货单
				pickVo =  createPickAndSave(pickVo,pickVo_org.getSendPick().getPickNo(),operator);
			}
			//若原波次单改变则新建拣货单
			else if((pickVo.getSendPick().getDocType().intValue() == Constant.PICKTYPE_FORM_WAVE)
					&& !pickVo.getSendPick().getWaveId().equals(pickVo_org.getSendPick().getWaveId())){
				//删除原拣货单
				updateStatus(pickVo.getSendPick().getPickId(),operator,Constant.PICK_STATUS_CANCAL);
				//新建拣货单
				pickVo =  createPickAndSave(pickVo,pickVo_org.getSendPick().getPickNo(),operator);
			}
			else{
				//更新计划拣货库位
				if(pickVo.getSendPickDetailVoList() != null && !pickVo.getSendPickDetailVoList().isEmpty()){			
					pickVo.getSendPickDetailVoList().stream().forEach(t->{
						//删除原计划库位
						SendPickLocation delParam = new SendPickLocation();
						delParam.setPickDetailId(t.getSendPickDetail().getPickDetailId());
						delParam.setPickType(Constant.PICK_TYPE_PLAN);
						pickLocationDao.delete(delParam);
						//创建新计划拣货库位明细
						t.createPlanPickLocations(t.getPlanPickLocations(), operator);
						//保存数据库
						for(SendPickLocationVo locationVo:t.getPlanPickLocations()){
							locationVo.getSendPickLocation().setPickLocationId2(context.getStrategy4Id().getPickLocationSeq());
							pickLocationDao.insertSelective(locationVo.getSendPickLocation());
						}	
					});
				}
				//4、统计拣货单计划拣货总数
				pickVo.calOrder();
				pickVo.getSendPick().defaultValue();
				//5、更新拣货单
				pickVo.getSendPick().setUpdatePerson(operator);
				pickVo.getSendPick().setUpdateTime(new Date());
				pickDao.updateByPrimaryKeySelective(pickVo.getSendPick());
			}		
		}
		return pickVo;
	}
	
	/**
	 * 根据发货单id查询是否所有的拣货单状态都是打开
	 * @param deliveryId
	 * @return
	 * @version 2017年2月16日 下午2:15:36<br/>
	 * @author Aaron He<br/>
	 */
	public boolean isOpenByDeliveryId(String deliveryId)throws Exception{
		if(StringUtils.isEmpty(deliveryId)) return false;
		
		//根据发货单id查询拣货单列表
		SendPickVo param = new SendPickVo();
		param.getSendPick().setDeliveryId(deliveryId);
		List<SendPick> list = pickDao.selectByExample(param.getConditionExample());
		//检查是否所有拣货单的状态都是打开
		for (SendPick sendPick : list) {
			if(sendPick.getPickStatus() != Constant.PICK_STATUS_OPEN 
					&& sendPick.getPickStatus() != Constant.PICK_STATUS_CANCAL){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 根据发货单id取消所有拣货单
	 * @param deliveryId
	 * @return
	 * @version 2017年2月16日 下午2:41:48<br/>
	 * @author Aaron He<br/>
	 */
	public int delByParam(Example example) throws Exception{
		if(example == null) return 0;
		//根据发货单id取消拣货单			     
		int num = pickDao.deleteByExample(example);
		return num;
	}
	
	/**
	 * 批量生效拣货单
	 * @param pickVo
	 * @param operator
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void batchEnable(SendPickVo pickVo,String operator) throws Exception{
		if(pickVo.getPickIdList() == null || pickVo.getPickIdList().size() == 0) return;
		
		for(String pickId:pickVo.getPickIdList()){
			active(pickId,operator);
		}
	}
	
	/**
	 * 生效拣货单
	 * 1、检查拣货单状态是否打开
	 * 2、检查库位计划拣货数量是否不为0，库位是否不为空
	 * 3、检查库位计划拣货数量总数是否 与计划拣货数量是否一致
	 * 4、审核计划拣货数量是否大于等于该库位的可拣货数量,计划拣货数量累加至拣货库位对应库存记录中的“拣货分配数量”
	 * 5、锁定库存预分配数量
	 * 6、拣货单生效后，状态由“打开”变更为“生效”
	 * 7、保存指派作业人员任务
	 * @param pickId
	 * @param operator
	 * @return
	 * @throws Exception
	 * @version 2017年2月16日 下午6:41:55<br/>
	 * @author Aaron He<br/>
	 */
	@Transactional(rollbackFor=Exception.class)
	public int active(String pickId,String operator) throws Exception{
		if(StringUtils.isEmpty(pickId)) return 0;
		
		//1、检查拣货单状态是否打开
		int status = getPickStatus(pickId);
		if(status != Constant.PICK_STATUS_OPEN)
			throw new BizException(BizStatus.PICK_STATUS_IS_NOT_OPEN.getReasonPhrase());
		//检查是否指派作业人员
//		if(StringUtil.isTrimEmpty(pickVo.getTsTaskVo().getTsTask().getOpPerson())){
//			throw new BizException(BizStatus.TASK_OPPERSON_IS_NULL.getReasonPhrase());
//		}
		
		//查询拣货单明细
		SendPickDetail param = new SendPickDetail();
		param.setPickId(pickId);
		List<SendPickDetail> details = pickDetailDao.qryList(param);
		for (SendPickDetail pickDetail : details) {
			//查询计划拣货库位明细	
			SendPickLocationVo pl = new SendPickLocationVo();
			pl.getSendPickLocation().setPickDetailId(pickDetail.getPickDetailId());
			pl.getSendPickLocation().setPickType(Constant.PICK_TYPE_PLAN);
			List<SendPickLocationVo> records = pickLocationService.qryPickLocations(pl);
			
			if(records == null || records.isEmpty())
				throw new BizException(BizStatus.PLAN_PICK_NUMANDLOCATION_ERROR.getReasonPhrase());
			
			//2、检查库位计划拣货数量是否不为0，库位是否不为空
			if(records.stream().anyMatch(p->(p.getSendPickLocation().getPickQty() == null || p.getSendPickLocation().getPickQty() == 0 || p.getSendPickLocation().getLocationId() == null))) 
				throw new BizException(BizStatus.PLAN_PICK_NUMANDLOCATION_ERROR.getReasonPhrase());
			//3、检查库位计划拣货数量总数是否 与计划拣货数量是否一致
			checkPlanQty(records,pickDetail.getOrderQty(),pickDetail.getOrderWeight(),pickDetail.getOrderVolume());
			
			//4、审核计划拣货数量是否大于等于该库位的可拣货数量,计划拣货数量累加至拣货库位对应库存记录中的“拣货分配数量”
			for (SendPickLocationVo pickLocationVo : records) {
				//检查拣货区库位库存是否足够,
				InvStockVO stockVo = StockOperate.getInvStockVO(pickDetail.getSkuId(),
														pickLocationVo.getSendPickLocation().getBatchNo(), 
														null, 
														pickLocationVo.getSendPickLocation().getLocationId(), 
														pickLocationVo.getSendPickLocation().getAsnDetailId(),
														pickLocationVo.getSendPickLocation().getPickQty(), 
														null,
														true,
														Arrays.asList(Constant.LOCATION_TYPE_PICKUP,Constant.LOCATION_TYPE_STORAGE));
				stockService.checkStock(stockVo);
				//5、锁定库存预分配数量
				stockService.lockOutStock(stockVo);
			}
		};	
		//6、拣货单生效后，状态由“打开”变更为“生效”
		int num = updateStatus(pickId,operator,Constant.PICK_STATUS_ACTIVE);
		//新建任务
//		taskService.create(pickVo.getTsTaskVo().getTsTask().getOpPerson(),Constant.TASK_TYPE_PICK,pickId);
//		taskService.create(Constant.TASK_TYPE_PICK,pickId);
		return num;
	}
		
	/**
	 * 失效拣货单
	 * 1、检查状态是否生效
	 * 2、释放拣货库位对应库存记录的“拣货分配数量”
	 * 3、拣货单状态由“生效”变更为“打开”
	 * @param pickId
	 * @param operator
	 * @return
	 * @throws Exception
	 * @version 2017年2月16日 下午6:43:35<br/>
	 * @author Aaron He<br/>
	 */
	@Transactional(rollbackFor=Exception.class)
	public int diable(String pickId,String operator)throws Exception{
		if(StringUtils.isEmpty(pickId)) return 0;
		//1、检查状态是否生效
		int status = getPickStatus(pickId);
		if(status != Constant.PICK_STATUS_ACTIVE)
			throw new BizException(BizStatus.PICK_STATUS_IS_NOT_ACTIVE.getReasonPhrase());
		//查询拣货明细
		List<SendPickDetailVo> pickDetailVoList = pickDetailService.qryPickDetails(pickId);
		
		//2、释放拣货库位对应库存记录的“拣货分配数量”
		for (SendPickDetailVo pickDetailVo : pickDetailVoList) {
			//根据拣货明细查询库位计划拣货列表
			SendPickLocationVo loParam = new SendPickLocationVo();
			loParam.getSendPickLocation().setPickDetailId(pickDetailVo.getSendPickDetail().getPickDetailId());
			loParam.getSendPickLocation().setPickType(Constant.PICK_TYPE_PLAN);
			List<SendPickLocationVo> locList = pickLocationService.qryPickLocations(loParam);
			//释放预分配库存
			for (SendPickLocationVo pickLocationVo : locList) {
				InvStockVO stockVo = StockOperate.getInvStockVO(pickDetailVo.getSendPickDetail().getSkuId(),
						pickLocationVo.getSendPickLocation().getBatchNo(), 
						null, 
						pickLocationVo.getSendPickLocation().getLocationId(), 
						pickLocationVo.getSendPickLocation().getAsnDetailId(),
						pickLocationVo.getSendPickLocation().getPickQty(), 
						null,
						true,
						null);
				stockService.unlockOutStock(stockVo);
			}	
		}
		//3、拣货单状态由“生效”变更为“打开”
		int num = updateStatus(pickId,operator,Constant.PICK_STATUS_OPEN);
		//检查任务,若是有任务且状态是执行中则要取消任务。
		taskService.cancelByOpId(pickId, operator);
		
		return num;
	}
	
	
	/**
	 * 取消拣货单
	 * 1、拣货单状态是否打开
	 * 2、拣货单状态由“打开”自动变更为“取消”
	 * 3、新增异常记录
	 * @param pickId
	 * @param operator
	 * @return
	 * @throws Exception
	 * @version 2017年2月16日 下午6:44:42<br/>
	 * @author Aaron He<br/>
	 */
	@Transactional(rollbackFor=Exception.class)
	public int abolish(String pickId,String operator,boolean isAddExcetionLog) throws Exception{
		if(StringUtils.isEmpty(pickId)) return 0;
		//1、拣货单状态是否打开
		SendPick pick = pickDao.selectByPrimaryKey(pickId);
		if(pick == null) 
			throw new BizException(BizStatus.DATA_IS_NULL.getReasonPhrase());
		
		if(pick.getPickStatus() != Constant.PICK_STATUS_OPEN)
			throw new BizException(BizStatus.PICK_STATUS_IS_NOT_OPEN.getReasonPhrase());
		//2、拣货单状态由“打开”自动变更为“取消”
		int num = updateToCancelStatus(pick,operator);
		//3、新增异常记录
		if(isAddExcetionLog){
			ExceptionLog exceptionLog =  ExceptionLogUtil.createInstance(
					Constant.EXCEPTION_TYPE_OTHER_ABNORMAL, 
					pick.getPickNo(), 
					Constant.EXCEPTION_STATUS_TO_BE_HANDLED,
					Constant.EXCEPTION_LEVEL_SLIGHT,
					ServiceConstant.PICK_CANCEL);
			exceptionLogService.add(exceptionLog);
			
		}	
		return num;
	}
	
	/**
	 * 更新拣货单为取消状态
	 * @param pick
	 * @param operator
	 * @return
	 */
	public int updateToCancelStatus(SendPick pick,String operator){
		pick.setDeliveryId(null);
		pick.setPickStatus(Constant.PICK_STATUS_CANCAL);
		pick.setUpdatePerson(operator);
		pick.setUpdateTime(new Date());
		int flag = pickDao.updateByPrimaryKey(pick);
		return flag;
	}
	
	/**
	 * 作业中
	 * 1、检查是否生效
	 * 2、拣货单状态由“生效”自动变更为“作业中”
	 * @param pickId
	 * @param operator
	 * @return
	 * @throws Exception
	 * @version 2017年2月16日 下午7:05:16<br/>
	 * @author Aaron He<br/>
	 */
	public int toWork(String pickId,String operator)throws Exception{
		if(StringUtils.isEmpty(pickId)) return 0;
		//1、检查是否生效
		int status = getPickStatus(pickId);
		if(status != Constant.PICK_STATUS_ACTIVE)
			throw new BizException(BizStatus.PICK_STATUS_IS_NOT_ACTIVE.getReasonPhrase()); 
		//2、拣货单状态由“生效”自动变更为“作业中”
		int num = updateStatus(pickId,operator,Constant.PICK_STATUS_WORKING);
		
		return num;
	}
	
	/**
	 * 打印
	 * @param pickId
	 * @param operator
	 * @return
	 * @throws Exception
	 */
	public SendPickVo print(String pickId)throws Exception{
		SendPickVo pickVo = view(pickId);
		SendDeliveryVo sendDeliveryVo = deliveryService.view(pickVo.getSendPick().getDeliveryId());
		pickVo.setSendDeliveryVo(sendDeliveryVo);
		
		return pickVo;
	}
	
	/**
	 * 获取拣货单详情
	 * @param pickId
	 * @return
	 * @version 2017年2月17日 下午3:35:17<br/>
	 * @author Aaron He<br/>
	 */
	public SendPickVo view(String pickId) throws Exception{
		if(StringUtils.isEmpty(pickId)) 
			throw new BizException(BizStatus.PARAMETER_ERROR.getReasonPhrase());;
		
		SendPickVo pickVo = new SendPickVo();
		SendPick pick = pickDao.selectByPrimaryKey(pickId);
		
		//仓库名称
		pickVo.setWarehouseName(FqDataUtils.getWarehouseNameById(wrehouseExtlService, pick.getWarehouseId()));
		//货主
		pickVo.setOwnerName(FqDataUtils.getMerchantNameById(merchantService, pick.getOwner()));
		pickVo.setSendPick(pick);
		
		//查询发货单号
		pickVo.setDeliveryNo(FqDataUtils.getDeliveryNoById(deliveryService, pick.getDeliveryId()));
		//查询波次单号
		pickVo.setWaveNo(FqDataUtils.getWaveNoById(waveService, pick.getWaveId()));
		
		//查询拣货明细
		List<SendPickDetailVo> pickDetailVoList = pickDetailService.qryPickDetails(pickId);
		for (SendPickDetailVo t : pickDetailVoList) {
			//货品信息 包装规格
			MetaSku sku = FqDataUtils.getSkuById(skuService, t.getSendPickDetail().getSkuId());
			if(sku != null){
				t.setSkuName(sku.getSkuName());
				t.setSpecModel(sku.getSpecModel());
				t.setSkuNo(sku.getSkuNo());
				t.setSkuBarCode(sku.getSkuBarCode());
				t.setMeasureUnit(sku.getMeasureUnit());
				MetaMerchant m = merchantService.get(sku.getOwner());
				if(m != null) t.setBookNo(m.getHgMerchantNo());
			}
			//查询计划库位
			SendPickLocationVo planParam = new SendPickLocationVo();
			planParam.getSendPickLocation().setPickDetailId(t.getSendPickDetail().getPickDetailId());
			planParam.getSendPickLocation().setPickType(Constant.PICK_TYPE_PLAN);
			List<SendPickLocationVo> planLocVos = pickLocationService.qryPickLocations(planParam);
			//查询实际库位
			SendPickLocationVo realParam = new SendPickLocationVo();
			realParam.getSendPickLocation().setPickDetailId(t.getSendPickDetail().getPickDetailId());
			realParam.getSendPickLocation().setPickType(Constant.PICK_TYPE_REAL);
			List<SendPickLocationVo> realLocVos = pickLocationService.qryPickLocations(realParam);
			t.setPlanPickLocations(planLocVos);
			t.setRealPickLocations(realLocVos);
		}
		pickVo.setSendPickDetailVoList(pickDetailVoList);
		
		return pickVo;	
	}
	
	/**
	 * 根据id查询拣货单Vo
	 * @param pickId
	 * @return
	 * @version 2017年3月14日 下午4:55:32<br/>
	 * @author Aaron He<br/>
	 */
	public SendPickVo getPickVoById(String pickId){
		SendPickVo pickVo = new SendPickVo();
		//查询拣货单
		SendPick pick = pickDao.selectByPrimaryKey(pickId);
		//查询拣货单明细
		List<SendPickDetailVo> pickDetails = pickDetailService.qryPickDetails(pickId);
		pickVo.setSendPick(pick);
		pickVo.setSendPickDetailVoList(pickDetails);
		return pickVo;
	}
	
	/**
	 * 拆分拣货单
	 * 1、检查状态是否打开
	 * 2、按原则拆分拣货单
	 * 3、子拣货单保存数据库
	 * 4、原拣货单取消
	 * @param pickVo
	 * @param orgId
	 * @param wareHouseId
	 * @param operator
	 * @return
	 * @throws Exception
	 * @version 2017年2月17日 下午4:29:11<br/>
	 * @author Aaron He<br/>
	 */
	@Transactional(rollbackFor=Exception.class)
	public void splitPick(SendPickVo pickVo,String orgId,String wareHouseId,String operator)throws Exception{
		if(pickVo == null || pickVo.getSendPick() == null 
				|| pickVo.getSendPickDetailVoList() == null || pickVo.getSendPickDetailVoList().isEmpty()
				|| StringUtils.isEmpty(orgId) ||StringUtils.isEmpty(wareHouseId)) 
			throw new BizException(BizStatus.PARAMETER_ERROR.getReasonPhrase());
		//1、检查状态是否打开
		int status = getPickStatus(pickVo.getSendPick().getPickId());
		if(status != Constant.PICK_STATUS_OPEN)
			throw new BizException(BizStatus.PICK_STATUS_IS_NOT_OPEN.getReasonPhrase());
	
		//查询原拣货单及明细
		SendPickVo parentVo = getPickVoById(pickVo.getSendPick().getPickId());			
		//2、按原则拆分拣货单
		PickSplitUtil pickSplitUtil = new PickSplitUtil();
		pickSplitUtil.setParentVo(parentVo);
		pickSplitUtil.split(pickVo, operator);
		//如果拆分以后子拣货单没有明细，则不拆分
		if(pickSplitUtil.getPickVo_1().getSendPickDetailVoList().isEmpty() ||
				pickSplitUtil.getPickVo_2().getSendPickDetailVoList().isEmpty()){
			throw new BizException(BizStatus.PICK_NUM_NO_SPLIT.getReasonPhrase());
		}
		//3、子拣货单保存数据库
		saveNewPick(pickSplitUtil.getPickVo_1(), operator);
		saveNewPick(pickSplitUtil.getPickVo_2(), operator);
		//4、原拣货单取消
		updateStatus(pickVo.getSendPick().getPickId(),operator,Constant.PICK_STATUS_CANCAL);
	}
	
	
	/**
	 * 取消拆分
	 * 1、检查所有子拣货单都是打开状态
	 * 2、父拣货单变更为打开
	 * 3、删除子拣货单
	 * @param pickId
	 * @param orgId
	 * @param wareHouseId
	 * @param operator
	 * @throws Exception
	 * @version 2017年2月20日 下午3:19:58<br/>
	 * @author Aaron He<br/>
	 */
	@Transactional(rollbackFor=Exception.class)
	public void removeSplit(String parentId,String operator)throws Exception{
		//1、检查所有子拣货单都是打开状态
		SendPickVo sendPickVo = new SendPickVo();
		sendPickVo.getSendPick().setParentId(parentId);
		List<SendPick> list = pickDao.selectByExample(sendPickVo.getConditionExample());
		//检查是否有父单
//		if(list == null || list.isEmpty())
//			throw new BizException(BizStatus.NO_SUB_SHEET.getReasonPhrase());
		
		for(SendPick sendPick:list){
			//若子单是取消状态，则需判断是否有拆分子单
			if(sendPick.getPickStatus().intValue() == Constant.PICK_STATUS_CANCAL){
				
				SendPickVo subPickVo = new SendPickVo();
				subPickVo.getSendPick().setParentId(sendPick.getPickId());
				List<SendPick> subPickList = pickDao.selectByExample(subPickVo.getConditionExample());
				//若子单是拆分以后取消的，则需提示“请先对相邻子单的子单进行取消拆分操作！”
				if(subPickList != null && !subPickList.isEmpty()){
					throw new BizException(BizStatus.SUB_PICK_HAS_SPLIT.getReasonPhrase());
				}
				//若子单是正常取消的，则需提示“相邻子单处于非打开状态，不能进行取消拆分操作！”
				else{
					throw new BizException(BizStatus.SUB_PICK_IS_NOT_OPEN.getReasonPhrase());
				}
			}
			//若子单不是打开，则需提示“相邻子单处于非打开状态，不能进行取消拆分操作！”
			else if(sendPick.getPickStatus().intValue() != Constant.PICK_STATUS_OPEN){
				throw new BizException(BizStatus.SUB_PICK_IS_NOT_OPEN.getReasonPhrase());
			}
		}
			
//		if(list.stream().anyMatch(p->p.getPickStatus().intValue() != Constant.PICK_STATUS_OPEN))
//			throw new BizException(BizStatus.SUB_PICK_IS_NOT_OPEN.getReasonPhrase());
		//2、父拣货单变更为打开
		int num = updateStatus(parentId, operator, Constant.PICK_STATUS_OPEN);
		//3、删除子拣货单
		if(num > 0){
			for (SendPick sendPick : list) {
				pickDao.deleteByPrimaryKey(sendPick.getPickId());
			}
		}
	}
	
	/**
	 * 自动分配库位
	 * 1、删除原来的计划库位明细记录
	 * 2、检查仓库库存数量是否足够
	 * 3、按fifo规则分配库位
	 * 4、创建计划拣货库位
	 * 5、保存拣货库位
	 * @param pickId
	 * @param operator
	 * @return
	 * @throws Exception
	 * @version 2017年2月20日 下午4:02:41<br/>
	 * @author Aaron He<br/>
	 */
	@Transactional(rollbackFor=Exception.class)
	public SendPickVo autoAllocateLocation(SendPickVo pickVo,String operator) throws Exception{
		
		List<SendPickDetailVo> pickDetailVoList = pickVo.getSendPickDetailVoList();
		for (SendPickDetailVo sendPickDetailVo : pickDetailVoList) {
			sendPickDetailVo.setPlanPickLocations(new ArrayList<SendPickLocationVo>());
			SendPickDetail spd = sendPickDetailVo.getSendPickDetail();
			
			//查询库存列表,若该货品库存，批次号为空，需查询该货品所有库存，则containBatch=false,若需查询无批次库存，批次号为空，containBatch=true
			//查询库存列表，这里用拣货明细的批次号
			InvStockVO stockVo = StockOperate.getInvStockVO(spd.getSkuId(),spd.getBatchNo(),null,null,null,null,false,false,Arrays.asList(Constant.LOCATION_TYPE_PICKUP));
			//获取货品的库存列表
			List<InvStock> stockList = stockService.list(stockVo);
			//2、检查仓库库存数量是否足够，若库存不够，则抛出异常
			stockVo.setFindNum(spd.getOrderQty());
			stockService.checkStock(stockVo);
			//3、按fifo规则分配库位
			AllocateStrategy allocateStrategy = new FifoAallocateStrategy();
			//按fifo规则分配库位，获取库位与批次号，生成拣货计划库位明细列表，
			List<SendPickLocation> locations = allocateStrategy.allocate(stockList, spd);
			for(SendPickLocation t:locations){
				SendPickLocationVo pickLocationVo = new SendPickLocationVo();
				pickLocationVo.setSendPickLocation(t);
				//获取库位名称
				pickLocationVo.setLocationComment(FqDataUtils.getLocNameById(locExtlService, t.getLocationId()));
				sendPickDetailVo.getPlanPickLocations().add(pickLocationVo);
			};
		}
		return pickVo;
	}

	/**
	 * 自动分配拣货库位（新）
	 * @param pickVo
	 * @param operator
	 * @return
	 * @throws Exception
	 */
	public SendPickVo allocation_new(SendPickVo pickVo,String operator) throws Exception{
		if(pickVo.getSendPickDetailVoList().isEmpty()) return null;
		List<SendPickDetailVo> pickDetailVoList = pickVo.getSendPickDetailVoList();
		//保存有批次号的sku拣货明细列表
		Map<String,List<SendPickDetailVo>> hasBatchNoMap = new HashMap<String,List<SendPickDetailVo>>();
		//若sku没有批次号，则存放在此map中
		Map<String,List<SendPickDetailVo>> noBatchNoMap = new HashMap<String,List<SendPickDetailVo>>();
		
		for(SendPickDetailVo pickDetailVo:pickDetailVoList){
			pickDetailVo.setPlanPickLocations(new ArrayList<SendPickLocationVo>());
			List<SendPickDetailVo> pdVoList = new ArrayList<SendPickDetailVo>();
			//保存在缺少批次号的Map中
			if(noBatchNoMap.containsKey(pickDetailVo.getSendPickDetail().getSkuId())){
				pdVoList = noBatchNoMap.get(pickDetailVo.getSendPickDetail().getSkuId());
				if(StringUtil.isNotEmpty(pickDetailVo.getSendPickDetail().getBatchNo())){
					pdVoList.add(0,pickDetailVo);
				}else{
					pdVoList.add(pickDetailVo);
				}
				continue;
			}
			//保存在有批次号的map中
			if(hasBatchNoMap.containsKey(pickDetailVo.getSendPickDetail().getSkuId())){
				pdVoList = hasBatchNoMap.get(pickDetailVo.getSendPickDetail().getSkuId());					
			}
			pdVoList.add(pickDetailVo);
			if(StringUtil.isNotEmpty(pickDetailVo.getSendPickDetail().getBatchNo())){			
				hasBatchNoMap.put(pickDetailVo.getSendPickDetail().getSkuId(), pdVoList);
			}else{
				noBatchNoMap.put(pickDetailVo.getSendPickDetail().getSkuId(), pdVoList);
				hasBatchNoMap.remove(pickDetailVo.getSendPickDetail().getSkuId());
			}
		}
		String order = "t1.batch_no ASC ,(t1.stock_qty-t1.pick_qty) ASC";
		//hasBatchNoMap转换成Map<skuId,Map<batchNo,List<SendPickDetailVo>>>
		Map<String,Map<String,List<SendPickDetailVo>>> skuMap = new HashMap<String,Map<String,List<SendPickDetailVo>>>();
		if(!hasBatchNoMap.isEmpty()){
			//转换成Map<skuId,Map<batchNo,List<SendPickDetailVo>>>
			for(String skuId:hasBatchNoMap.keySet()){
				List<SendPickDetailVo> pickDetailList = hasBatchNoMap.get(skuId);
				Map<String,List<SendPickDetailVo>> batchNoMap = pickDetailList.stream().collect(Collectors.groupingBy(t->t.getSendPickDetail().getBatchNo()));
				skuMap.put(skuId, batchNoMap);
			}
			//根据skuid，batchNo查询库存列表
			for(String skuId:skuMap.keySet()){
				Map<String,List<SendPickDetailVo>> batchNoMap = skuMap.get(skuId);
				for(String batchNo:batchNoMap.keySet()){
					InvStockVO stockVo = StockOperate.getInvStockVO(skuId,batchNo,null,null,null,null,false,true,Arrays.asList(Constant.LOCATION_TYPE_PICKUP,Constant.LOCATION_TYPE_STORAGE));
					stockVo.setResultOrder(order);
					//获取货品的库存列表
					List<InvStock> stockList = stockService.list(stockVo);
					//设定fifo规则
					AllocateStrategy allocateStrategy = new FifoAallocateStrategy();
					//按fifo规则分配库位，获取库位与批次号，生成拣货计划库位明细列表
					List<SendPickDetailVo> list = batchNoMap.get(batchNo);
					for(SendPickDetailVo pickDevailVo:list){
						List<SendPickLocation> locations = allocateStrategy.allocate_new(stockList,null,pickDevailVo.getSendPickDetail(),true);
						for(SendPickLocation t:locations){
							SendPickLocationVo pickLocationVo = new SendPickLocationVo();
							pickLocationVo.setSendPickLocation(t);
							//获取库位名称
							pickLocationVo.setLocationComment(FqDataUtils.getLocNameById(locExtlService, t.getLocationId()));
							pickDevailVo.getPlanPickLocations().add(pickLocationVo);
						};
					}										
				}
			}
		}		
		//根据skuid查询所有库存
		if(!noBatchNoMap.isEmpty()){
			for(String skuId:noBatchNoMap.keySet()){
				InvStockVO stockVo = StockOperate.getInvStockVO(skuId,null,null,null,null,null,false,false,Arrays.asList(Constant.LOCATION_TYPE_PICKUP,Constant.LOCATION_TYPE_STORAGE));
				stockVo.setResultOrder(order);
				//获取货品的库存列表
				List<InvStock> stockList = stockService.list(stockVo);
				//拆分成有批次的库存列表与没批次的库存列表
				List<InvStock> hasBatchNoStockList = new ArrayList<InvStock>();
				List<InvStock> noBatchNoStockList = new ArrayList<InvStock>();
				for(InvStock stock:stockList){
					if(StringUtil.isTrimEmpty(stock.getBatchNo())){
						noBatchNoStockList.add(stock);
					}else{
						hasBatchNoStockList.add(stock);
					}
				}
				//设定fifo规则
				AllocateStrategy allocateStrategy = new FifoAallocateStrategy();
				//按fifo规则分配库位，获取库位与批次号，生成拣货计划库位明细列表
				List<SendPickDetailVo> list = noBatchNoMap.get(skuId);
				for(SendPickDetailVo pickDevailVo:list){
					Boolean hasBatchNo = StringUtils.isNotEmpty(pickDevailVo.getSendPickDetail().getBatchNo());
					
					List<SendPickLocation> locations = allocateStrategy.allocate_new(hasBatchNoStockList,noBatchNoStockList,pickDevailVo.getSendPickDetail(),hasBatchNo);
					for(SendPickLocation t:locations){
						SendPickLocationVo pickLocationVo = new SendPickLocationVo();
						pickLocationVo.setSendPickLocation(t);
						//获取库位名称
						pickLocationVo.setLocationComment(FqDataUtils.getLocNameById(locExtlService, t.getLocationId()));
						pickDevailVo.getPlanPickLocations().add(pickLocationVo);
					};
				}
			}
		}
		return pickVo;
	}
	
	
	/**
	 * 手持终端拣货确认
	 * 1、根据库位代码查找库位id
	 * 2、拣货确认
	 * @param pickVo
	 * @param operator
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void confirmPick_pda(SendPickVo pickVo,String operator)throws Exception{
		//根据库位代码查找库位id
		for(SendPickDetailVo pickDetailVo:pickVo.getSendPickDetailVoList()){
			for(SendPickLocationVo locationVo:pickDetailVo.getRealPickLocations()){
				//根据库位代码查找库位
				if(StringUtil.isTrimEmpty(locationVo.getLocationNo())){
					throw new BizException(BizStatus.REAL_PICK_NUMANDLOCATION_ERROR.getReasonPhrase());
				}
				MetaLocation location = locationExtlService.findLocByNo(locationVo.getLocationNo(), null);
				locationVo.getSendPickLocation().setLocationId(location.getLocationId());
			}
		}
		//拣货确认
		completePick(pickVo,operator);
	}
	
	
	/**
	 * 拣货确认
	 * 1、检查状态是否生效或作业中
	 * 2、释放“拣货分配数量”扣减计划拣货数量
	 * 3、更新相应库位库存数量【库存数量- 实际数量】
	 * 4、对比计划与实际数量是否一致
	 * 5、统计实际数量，实际重量，实际体积
	 * 6、更新拣货明细实际拣货数量
	 * 7、若实际拣货数量是否小于待拣货数量，则新增异常日志
	 * 8、拣货单状态由“生效”或“作业中”自动变更为“作业完成”
	 * 9、更新发货单拣货数量及状态
	 * 10、登记到企业业务统计
	 * 
	 * @param pickVo
	 * @param operator
	 * @throws Exception
	 * @version 2017年2月23日 上午11:23:13<br/>
	 * @author Aaron He<br/>
	 */
	@Transactional(rollbackFor=Exception.class)
	public void confirmPick(SendPickVo pickVo,String operator)throws Exception{
		//1、检查状态是否生效或作业中
		int status = getPickStatus(pickVo.getSendPick().getPickId());
		if(status != Constant.PICK_STATUS_ACTIVE &&  status != Constant.PICK_STATUS_WORKING)
			throw new BizException(BizStatus.PICK_STATUS_IS_NOT_ACTIVE.getReasonPhrase());
		//查询拣货单
		SendPick pick = pickDao.selectByPrimaryKey(pickVo.getSendPick().getPickId());
		pick.setOpPerson(pickVo.getSendPick().getOpPerson());		
		pickVo.setSendPick(pick);
		//检查拣货单的数据类型
		if(pick.getDocType() == null)
			throw new BizException(BizStatus.PICK_DOCTYPE_IS_NULL.getReasonPhrase());
		
		//查询拣货明细
		List<SendPickDetailVo> pickDetailVoList = pickVo.getSendPickDetailVoList();//qryPickDetails(pickVo.getSendPick().getPickId());
		//Map<skuId,message>
		Map<String,String> differSkuMap = new HashMap<String,String>();//数量不一致的detailid
		//2、释放“拣货分配数量”扣减计划拣货数量
		for (SendPickDetailVo pickDetailVo : pickDetailVoList) {
			//从数据库查询拣货明细
			SendPickDetailVo deVo = pickDetailService.getVoById(pickDetailVo.getSendPickDetail().getPickDetailId());
			pickDetailVo.setSendPickDetail(deVo.getSendPickDetail());
			SendPickDetail pickDetail = pickDetailVo.getSendPickDetail();		
			//查询计划拣货库位
			SendPickLocationVo loParam = new SendPickLocationVo();
			loParam.getSendPickLocation().setPickDetailId(pickDetailVo.getSendPickDetail().getPickDetailId());
			loParam.getSendPickLocation().setPickType(Constant.PICK_TYPE_PLAN);
			List<SendPickLocationVo> planLocVos = pickLocationService.qryPickLocations(loParam);
			pickDetailVo.setPlanPickLocations(planLocVos);
			//释放“拣货分配数量”扣减计划拣货数量
			for (SendPickLocationVo pickLocationVo : pickDetailVo.getPlanPickLocations()) {
				SendPickLocation plan = pickLocationVo.getSendPickLocation();
				InvStockVO stockVo = StockOperate.getInvStockVO(pickDetailVo.getSendPickDetail().getSkuId(),
																plan.getBatchNo(),
																null, 
																plan.getLocationId(), 
																plan.getAsnDetailId(),
																plan.getPickQty(), 
																null,
																true,
																Arrays.asList(Constant.LOCATION_TYPE_PICKUP,Constant.LOCATION_TYPE_STORAGE));
				stockService.unlockOutStock(stockVo);
			}
			
			if(pickDetailVo.getRealPickLocations().isEmpty()) 
				throw new BizException(BizStatus.REAL_PICK_NUMANDLOCATION_ERROR.getReasonPhrase());
			
			for(SendPickLocationVo p:pickDetailVo.getRealPickLocations()){
				SendPickLocation pickLocation = p.getSendPickLocation();
				//3、更新相应库位库存数量【库存数量- 实际数量】
				InvStockVO stockVo = StockOperate.getInvStockVO(pickDetailVo.getSendPickDetail().getSkuId(),
																pickLocation.getBatchNo(), 
																null, 
																pickLocation.getLocationId(), 
																pickLocation.getAsnDetailId(),
																pickLocation.getPickQty(), 
																null,
																true,
																Arrays.asList(Constant.LOCATION_TYPE_PICKUP,Constant.LOCATION_TYPE_STORAGE));
				InvLog log = StockOperate.getInvLogVo(pickDetailVo.getSendPickDetail().getSkuId(), 
													pickDetailVo.getSendPickDetail().getBatchNo(), 
													pickLocation.getLocationId(), 
													pickLocation.getPickQty(), 
													operator, 
													pickVo.getSendPick().getPickId());
				stockVo.setInvLog(log);
				stockService.outStock(stockVo);
				
				//检查库位是否为空
				if(pickLocation.getLocationId() == null || pickLocation.getPickQty() == null)
					throw new BizException(BizStatus.REAL_PICK_NUMANDLOCATION_ERROR.getReasonPhrase());
				pickLocation.setPickLocationId(IdUtil.getUUID());
				pickLocation.setPickDetailId(pickDetailVo.getSendPickDetail().getPickDetailId());
				pickLocation.setPickType(Constant.PICK_TYPE_REAL);	
				pickLocation.setPackId(pickDetail.getPackId());
				pickLocation.setMeasureUnit(pickDetail.getMeasureUnit());
				pickLocation.setCreatePerson(operator);
				pickLocation.setUpdatePerson(operator);
				pickLocation.defaultValue();
				pickLocation.setPickLocationId2(context.getStrategy4Id().getPickLocationSeq());
				//保存实际拣货货品库位明细
				pickLocationDao.insertSelective(pickLocation);
			}
			//4、对比计划与实际数量是否一致
			String msg = pickDetailVo.getSkuDifferMessage();
			if(msg != null){
				differSkuMap.put(pickDetail.getSkuId(),msg);
			}

			//5、统计实际数量，实际重量，实际体积
			pickDetailVo.statisAndSetRealPickQty();
			//更新拣货明细实际拣货数量			
			pickDetail.setUpdatePerson(operator);
			pickDetail.setUpdateTime(new Date());
			pickDetailDao.updateByPrimaryKeySelective(pickDetail);	
		}
		//6、若实际拣货数量是否小于待拣货数量，则新增异常日志
		if(!differSkuMap.isEmpty()){
			String msg = "";
			for(String skuId:differSkuMap.keySet()){
				MetaSku sku = FqDataUtils.getSkuById(skuService, skuId);
				msg += String.format(ServiceConstant.ERRLOG_PICK_NUM_NOT_CONFORM,sku.getSkuName(),sku.getSkuNo(),differSkuMap.get(skuId))
						+System.getProperty("line.separator");
			}
			//查询货品名称
			newExceptionLog(Constant.EXCEPTION_TYPE_NOT_CONFORM, 
					pickVo.getSendPick().getPickNo(), 
					Constant.EXCEPTION_STATUS_TO_BE_HANDLED, 
					Constant.EXCEPTION_LEVEL_SLIGHT,
					msg);
		}

		//7、更新拣货单实际拣货总数
		pickVo.calPickQty();
		//8、拣货单状态由“生效”或“作业中”自动变更为“作业完成”
		pickVo.getSendPick().setPickStatus(Constant.PICK_STATUS_FINISH);
		pickVo.getSendPick().setOpTime(new Date());
		pickVo.getSendPick().setUpdatePerson(operator);
		pickVo.getSendPick().setUpdateTime(new Date());
		pickDao.updateByPrimaryKeySelective(pickVo.getSendPick());	
		
		//更新发货单，发货明细拣货数量		
		deliveryService.updateAfterCompletePick(pickVo,operator);
		if(pick.getDocType() == Constant.PICKTYPE_FORM_DELIVERY){
			SendDeliveryVo deliveryVo = deliveryService.getDeliveryById(pickVo.getSendPick().getDeliveryId());
			//10、登记到企业业务统计
			parkOrgBusiStasService.newBusiStas(deliveryVo.getSendDelivery().getDocType(), 
					deliveryVo.getSendDelivery().getDeliveryNo(), 
					deliveryVo.getSendDelivery().getOrgId(), 
					deliveryVo.getSendDelivery().getWarehouseId(), 
					pickVo.getSendPick().getRealPickQty(), 
					pickVo.getSendPick().getRealPickWeight(), 
					pickVo.getSendPick().getRealPickVolume(), 
					operator);
			//新增发货操作日志
			deliveryLogService.addNewDeliveryLog(deliveryVo.getSendDelivery().getDeliveryId(),
					operator,Constant.DELIVERY_LOG_TYPE_PICK,deliveryVo.getSendDelivery().getOrgId(),
					deliveryVo.getSendDelivery().getWarehouseId());
		}
		if(pick.getDocType().intValue() == Constant.PICKTYPE_FORM_WAVE) {
			//更新波次单拣货数量
			waveService.updateAfterCompletePick(pickVo,operator);
			SendWaveVo waveVo = waveService.getSendWaveVoById(pickVo.getSendPick().getWaveId());
			//10、登记到企业业务统计
//			waveService.addOrgBusiStas(waveVo, operator);
			parkOrgBusiStasService.newBusiStas(null, 
					waveVo.getSendWave().getWaveNo(), 
					waveVo.getSendWave().getOrgId(), 
					waveVo.getSendWave().getWarehouseId(), 
					pickVo.getSendPick().getRealPickQty(), 
					pickVo.getSendPick().getRealPickWeight(), 
					pickVo.getSendPick().getRealPickVolume(), 
					operator);
		}
		//更新任务状态
		taskService.finishByOpId(pickVo.getSendPick().getPickId(),operator);

	}
	
	/**
	 * 完成拣货（芜湖）
	 * @param pickVo
	 * @param operator
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void completePick(SendPickVo pickVo,String operator)throws Exception{
		//1、检查状态是否生效或作业中
		int status = getPickStatus(pickVo.getSendPick().getPickId());
		if(status != Constant.PICK_STATUS_ACTIVE &&  status != Constant.PICK_STATUS_WORKING)
			throw new BizException(BizStatus.PICK_STATUS_IS_NOT_ACTIVE.getReasonPhrase());
		//查询拣货单
		SendPick pick = pickDao.selectByPrimaryKey(pickVo.getSendPick().getPickId());
		pick.setOpPerson(pickVo.getSendPick().getOpPerson());		
		pickVo.setSendPick(pick);
		//检查拣货单的数据类型
		if(pick.getDocType() == null)
			throw new BizException(BizStatus.PICK_DOCTYPE_IS_NULL.getReasonPhrase());
		
		//查询拣货明细
		List<SendPickDetailVo> pickDetailVoList = pickVo.getSendPickDetailVoList();//qryPickDetails(pickVo.getSendPick().getPickId());
		//Map<skuId,message>
//		Map<String,String> differSkuMap = new HashMap<String,String>();//数量不一致的detailid
		//2、释放“拣货分配数量”扣减计划拣货数量
		// 补货操作对象
		Set<String> skuIdSet = new HashSet<String>();
		// 对应发货单对象
		Set<String> deliveryIdSet = new HashSet<String>();
		
		for (SendPickDetailVo pickDetailVo : pickDetailVoList) {
			//从数据库查询拣货明细
			SendPickDetailVo deVo = pickDetailService.getVoById(pickDetailVo.getSendPickDetail().getPickDetailId());
			pickDetailVo.setSendPickDetail(deVo.getSendPickDetail());
			SendPickDetail pickDetail = pickDetailVo.getSendPickDetail();		
			//查询计划拣货库位
			SendPickLocationVo loParam = new SendPickLocationVo();
			loParam.getSendPickLocation().setPickDetailId(pickDetailVo.getSendPickDetail().getPickDetailId());
			loParam.getSendPickLocation().setPickType(Constant.PICK_TYPE_PLAN);
			List<SendPickLocationVo> planLocVos = pickLocationService.qryPickLocations(loParam);
			pickDetailVo.setPlanPickLocations(planLocVos);
			//释放“拣货分配数量”扣减计划拣货数量
			for (SendPickLocationVo pickLocationVo : pickDetailVo.getPlanPickLocations()) {
				SendPickLocation plan = pickLocationVo.getSendPickLocation();
				InvStockVO stockVo = StockOperate.getInvStockVO(pickDetailVo.getSendPickDetail().getSkuId(),
																plan.getBatchNo(),
																null, 
																plan.getLocationId(), 
																plan.getAsnDetailId(),
																plan.getPickQty(), 
																null,
																true,
																Arrays.asList(Constant.LOCATION_TYPE_PICKUP,Constant.LOCATION_TYPE_STORAGE));
				stockService.unlockOutStock(stockVo);
			}
			
			if(pickDetailVo.getRealPickLocations().isEmpty()) 
				throw new BizException(BizStatus.REAL_PICK_NUMANDLOCATION_ERROR.getReasonPhrase());
			for(SendPickLocationVo p:pickDetailVo.getRealPickLocations()){
				SendPickLocation pickLocation = p.getSendPickLocation();
				//检查库位是否为空
				if(pickLocation.getLocationId() == null || pickLocation.getPickQty() == null)
					throw new BizException(BizStatus.REAL_PICK_NUMANDLOCATION_ERROR.getReasonPhrase());
				pickLocation.setPickLocationId(IdUtil.getUUID());
				pickLocation.setPickDetailId(pickDetailVo.getSendPickDetail().getPickDetailId());
				pickLocation.setPickType(Constant.PICK_TYPE_REAL);	
				pickLocation.setPackId(pickDetail.getPackId());
				pickLocation.setMeasureUnit(pickDetail.getMeasureUnit());
				pickLocation.setCreatePerson(operator);
				pickLocation.setUpdatePerson(operator);
				pickLocation.defaultValue();
				pickLocation.setPickLocationId2(context.getStrategy4Id().getPickLocationSeq());
				//保存实际拣货货品库位明细
				pickLocationDao.insertSelective(pickLocation);
			}
			deliveryIdSet.add(pickDetailVo.getSendPickDetail().getDeliveryId());
			skuIdSet.add(pickDetailVo.getSendPickDetail().getSkuId());
			//4、对比计划与实际数量是否一致
			String msg = pickDetailVo.getSkuDifferMessage();
			if(msg != null){
//				differSkuMap.put(pickDetail.getSkuId(),msg);
				throw new BizException(msg);
			}

			//5、统计实际数量，实际重量，实际体积
			pickDetailVo.statisAndSetRealPickQty();
			//更新拣货明细实际拣货数量			
			pickDetail.setUpdatePerson(operator);
			pickDetail.setUpdateTime(new Date());
			pickDetailDao.updateByPrimaryKeySelective(pickDetail);	
		}
		//拣完货，从存放区移位到发货区库
		//新增，如果是波次拣货单，则需要批量获取发货单进行移位
		if (StringUtil.isNoneBlank(pick.getWaveId())) {
//			for (String deliveryId : deliveryIdSet) {
				stockService.shiftToSend(deliveryIdSet.iterator().next());
//			}
		} else {
			stockService.shiftToSend(pick.getDeliveryId());
		}
		//6、若实际拣货数量是否小于待拣货数量，则新增异常日志
//		if(!differSkuMap.isEmpty()){
//			String msg = "";
//			FqDataUtils fdu = new FqDataUtils();
//			for(String skuId:differSkuMap.keySet()){
//				MetaSku sku = fdu.getSkuById(skuService, skuId);
//				msg += String.format(ServiceConstant.ERRLOG_PICK_NUM_NOT_CONFORM,sku.getSkuName(),sku.getSkuNo(),differSkuMap.get(skuId))
//						+System.getProperty("line.separator");
//			}
//			//查询货品名称
//			newExceptionLog(Constant.EXCEPTION_TYPE_NOT_CONFORM, 
//					pickVo.getSendPick().getPickNo(), 
//					Constant.EXCEPTION_STATUS_TO_BE_HANDLED, 
//					Constant.EXCEPTION_LEVEL_SLIGHT,
//					msg);
//		}

		//7、更新拣货单实际拣货总数
		pickVo.calPickQty();
		//8、拣货单状态由“生效”或“作业中”自动变更为“作业完成”
		pickVo.getSendPick().setPickStatus(Constant.PICK_STATUS_FINISH);
		pickVo.getSendPick().setOpTime(new Date());
		pickVo.getSendPick().setUpdatePerson(operator);
		pickVo.getSendPick().setUpdateTime(new Date());
		pickDao.updateByPrimaryKeySelective(pickVo.getSendPick());	
		
		//更新发货单，发货明细拣货数量		
		deliveryService.updateAfterCompletePick(pickVo,operator);
		if(pick.getDocType() == Constant.PICKTYPE_FORM_DELIVERY){
			SendDeliveryVo deliveryVo = deliveryService.getDeliveryById(pickVo.getSendPick().getDeliveryId());
			//10、登记到企业业务统计
			parkOrgBusiStasService.newBusiStas(deliveryVo.getSendDelivery().getDocType(), 
					deliveryVo.getSendDelivery().getDeliveryNo(), 
					deliveryVo.getSendDelivery().getOrgId(), 
					deliveryVo.getSendDelivery().getWarehouseId(), 
					pickVo.getSendPick().getRealPickQty(), 
					pickVo.getSendPick().getRealPickWeight(), 
					pickVo.getSendPick().getRealPickVolume(), 
					operator);
			//新增发货操作日志
			deliveryLogService.addNewDeliveryLog(deliveryVo.getSendDelivery().getDeliveryId(),
					operator,Constant.DELIVERY_LOG_TYPE_PICK,deliveryVo.getSendDelivery().getOrgId(),
					deliveryVo.getSendDelivery().getWarehouseId());
		}
		if(pick.getDocType().intValue() == Constant.PICKTYPE_FORM_WAVE) {
			//更新波次单拣货数量
			waveService.updateAfterCompletePick(pickVo,operator);
			SendWaveVo waveVo = waveService.getSendWaveVoById(pickVo.getSendPick().getWaveId());
			//10、登记到企业业务统计
//			waveService.addOrgBusiStas(waveVo, operator);
			parkOrgBusiStasService.newBusiStas(null, 
					waveVo.getSendWave().getWaveNo(), 
					waveVo.getSendWave().getOrgId(), 
					waveVo.getSendWave().getWarehouseId(), 
					pickVo.getSendPick().getRealPickQty(), 
					pickVo.getSendPick().getRealPickWeight(), 
					pickVo.getSendPick().getRealPickVolume(), 
					operator);
			
		   for(SendDeliveryVo delVo:waveVo.getSendDeliberyVoList()){
			 //新增发货操作日志
				deliveryLogService.addNewDeliveryLog(delVo.getSendDelivery().getDeliveryId(),
						operator,Constant.DELIVERY_LOG_TYPE_PICK,waveVo.getSendWave().getOrgId(),
						waveVo.getSendWave().getWarehouseId());
		   }
		}
		//更新任务状态
		taskService.finishByOpId(pickVo.getSendPick().getPickId(),operator);

		//检查拣货区库存是否足够,若库存不足，则生成补货移位单
		if (!skuIdSet.isEmpty()) {
			stockService.repPickSku(skuIdSet, pick.getPickNo());
		}
		
		//TODO 发送海关辅助系统
//		try {
//			SendPickVo vo = this.view(pickVo.getSendPick().getPickId());
//			context.getStrategy4Assis().request(vo);
//		} catch(Exception e) {
//			if(logger.isErrorEnabled()) logger.error(e.getMessage(), e);
//		}
	}
	
	/**
	 * 批量作业确认（芜湖）
	 * @param pickVo
	 * @param operator
	 * @throws Exception
	 */
	public void batchCompletePick(SendPickVo baPickVo,String operator)throws Exception{
		if(baPickVo == null) return;
		if(baPickVo.getPickIdList() == null || baPickVo.getPickIdList().isEmpty()) return;
		
		for(String pickId:baPickVo.getPickIdList()){
			//查询拣货单
			SendPickVo pickVo = new SendPickVo();
			SendPick pick = pickDao.selectByPrimaryKey(pickId);
			//检查状态是否生效或作业中
			if(pick.getPickStatus() != Constant.PICK_STATUS_ACTIVE &&  pick.getPickStatus() != Constant.PICK_STATUS_WORKING)
				throw new BizException(BizStatus.PICK_STATUS_IS_NOT_ACTIVE.getReasonPhrase());
			
			pick.setOpPerson(operator);		
			pickVo.setSendPick(pick);
			//查询拣货明细
			List<SendPickDetailVo> pickDetailVoList = pickDetailService.qryPickDetails(pickId);//qryPickDetails(pickVo.getSendPick().getPickId());
			// 补货操作对象
			Set<String> skuIdSet = new HashSet<String>();
			// 对应发货单对象
			Set<String> deliveryIdSet = new HashSet<String>();
			//释放计划数量
			for(SendPickDetailVo pickDetailVo:pickDetailVoList){
				//从数据库查询拣货明细
				SendPickDetailVo deVo = pickDetailService.getVoById(pickDetailVo.getSendPickDetail().getPickDetailId());
				pickDetailVo.setSendPickDetail(deVo.getSendPickDetail());
				SendPickDetail pickDetail = pickDetailVo.getSendPickDetail();
				//查询计划拣货库位
				SendPickLocationVo loParam = new SendPickLocationVo();
				loParam.getSendPickLocation().setPickDetailId(pickDetailVo.getSendPickDetail().getPickDetailId());
				loParam.getSendPickLocation().setPickType(Constant.PICK_TYPE_PLAN);
				List<SendPickLocationVo> planLocVos = pickLocationService.qryPickLocations(loParam);
				pickDetailVo.setPlanPickLocations(planLocVos);
				//释放“拣货分配数量”扣减计划拣货数量
				for (SendPickLocationVo pickLocationVo : pickDetailVo.getPlanPickLocations()) {
					SendPickLocation plan = pickLocationVo.getSendPickLocation();
					InvStockVO stockVo = StockOperate.getInvStockVO(pickDetailVo.getSendPickDetail().getSkuId(),
																	plan.getBatchNo(),
																	null, 
																	plan.getLocationId(), 
																	plan.getAsnDetailId(),
																	plan.getPickQty(), 
																	null,
																	true,
																	Arrays.asList(Constant.LOCATION_TYPE_PICKUP,Constant.LOCATION_TYPE_STORAGE));
					stockService.unlockOutStock(stockVo);
					
					//新建实际拣货库位，与计划一致
					SendPickLocationVo realLocationVo = new SendPickLocationVo();
					SendPickLocation realPickLocation = new SendPickLocation();
					BeanUtils.copyProperties(realPickLocation, plan);
					realPickLocation.setPickLocationId(IdUtil.getUUID());
					realPickLocation.setPickDetailId(pickDetailVo.getSendPickDetail().getPickDetailId());
					realPickLocation.setPickType(Constant.PICK_TYPE_REAL);	
					realPickLocation.setPackId(pickDetail.getPackId());
					realPickLocation.setMeasureUnit(pickDetail.getMeasureUnit());
					realPickLocation.setCreatePerson(operator);
					realPickLocation.setUpdatePerson(operator);
					realPickLocation.defaultValue();
					realPickLocation.setPickLocationId2(context.getStrategy4Id().getPickLocationSeq());
					//保存实际拣货货品库位明细
					pickLocationDao.insertSelective(realPickLocation);	
					realLocationVo.setSendPickLocation(realPickLocation);
					pickDetailVo.getRealPickLocations().add(realLocationVo);
				}
				deliveryIdSet.add(pickDetailVo.getSendPickDetail().getDeliveryId());
				skuIdSet.add(pickDetailVo.getSendPickDetail().getSkuId());
				
				//5、统计实际数量，实际重量，实际体积
				pickDetailVo.statisAndSetRealPickQty();
				//更新拣货明细实际拣货数量			
				pickDetail.setUpdatePerson(operator);
				pickDetail.setUpdateTime(new Date());
				pickDetailDao.updateByPrimaryKeySelective(pickDetail);	
			}
			pickVo.setSendPickDetailVoList(pickDetailVoList);
			//拣完货，从存放区移位到发货区库
			//新增，如果是波次拣货单，则需要批量获取发货单进行移位
			if (StringUtil.isNoneBlank(pick.getWaveId())) {
//				for (String deliveryId : deliveryIdSet) {
					stockService.shiftToSend(deliveryIdSet.iterator().next());
//				}
			} else {
				stockService.shiftToSend(pick.getDeliveryId());
			}
			//7、更新拣货单实际拣货总数
			pickVo.calPickQty();
			//8、拣货单状态由“生效”或“作业中”自动变更为“作业完成”
			pickVo.getSendPick().setPickStatus(Constant.PICK_STATUS_FINISH);
			pickVo.getSendPick().setOpTime(new Date());
			pickVo.getSendPick().setUpdatePerson(operator);
			pickVo.getSendPick().setUpdateTime(new Date());
			pickDao.updateByPrimaryKeySelective(pickVo.getSendPick());	
			
			//更新发货单，发货明细拣货数量		
			deliveryService.updateAfterCompletePick(pickVo,operator);
			if(pick.getDocType() == Constant.PICKTYPE_FORM_DELIVERY){
				SendDeliveryVo deliveryVo = deliveryService.getDeliveryById(pickVo.getSendPick().getDeliveryId());
				//10、登记到企业业务统计
				parkOrgBusiStasService.newBusiStas(deliveryVo.getSendDelivery().getDocType(), 
						deliveryVo.getSendDelivery().getDeliveryNo(), 
						deliveryVo.getSendDelivery().getOrgId(), 
						deliveryVo.getSendDelivery().getWarehouseId(), 
						pickVo.getSendPick().getRealPickQty(), 
						pickVo.getSendPick().getRealPickWeight(), 
						pickVo.getSendPick().getRealPickVolume(), 
						operator);
				//新增发货操作日志
				deliveryLogService.addNewDeliveryLog(deliveryVo.getSendDelivery().getDeliveryId(),
						operator,Constant.DELIVERY_LOG_TYPE_PICK,deliveryVo.getSendDelivery().getOrgId(),
						deliveryVo.getSendDelivery().getWarehouseId());
			}
			
			if(pick.getDocType().intValue() == Constant.PICKTYPE_FORM_WAVE) {
				//更新波次单拣货数量
				waveService.updateAfterCompletePick(pickVo,operator);
				SendWaveVo waveVo = waveService.getSendWaveVoById(pickVo.getSendPick().getWaveId());
				parkOrgBusiStasService.newBusiStas(null, 
						waveVo.getSendWave().getWaveNo(), 
						waveVo.getSendWave().getOrgId(), 
						waveVo.getSendWave().getWarehouseId(), 
						pickVo.getSendPick().getRealPickQty(), 
						pickVo.getSendPick().getRealPickWeight(), 
						pickVo.getSendPick().getRealPickVolume(), 
						operator);
				
			   for(SendDeliveryVo delVo:waveVo.getSendDeliberyVoList()){
				 //新增发货操作日志
					deliveryLogService.addNewDeliveryLog(delVo.getSendDelivery().getDeliveryId(),
							operator,Constant.DELIVERY_LOG_TYPE_PICK,waveVo.getSendWave().getOrgId(),
							waveVo.getSendWave().getWarehouseId());
			   }
			}
			//更新任务状态
			taskService.finishByOpId(pickVo.getSendPick().getPickId(),operator);

			//检查拣货区库存是否足够,若库存不足，则生成补货移位单
			if (!skuIdSet.isEmpty()) {
				stockService.repPickSku(skuIdSet, pick.getPickNo());
			}
		}
	}
	
	/**
	 * 根据发货单明细查询拣货数量
	 * @param deliberyDetailIds
	 * @return
	 * @throws Exception
	 * @version 2017年2月23日 下午1:42:11<br/>
	 * @author Aaron He<br/>
	 */
	public List<SendPickDetail> qryPickQtyByDeliveryDetails(List<String> deliberyDetailIds,String orgId,String warehouseId) throws Exception{
		List<SendPickDetail> list = null;
		
		//用发货单明细id查询对应的拣货单明细
		Example example = new Example(SendPickDetail.class);
		example.createCriteria().andEqualTo("orgId",orgId)
								.andEqualTo("warehouseId",warehouseId)
								.andIn("deliveryDetailId",deliberyDetailIds);
		list = pickDetailDao.selectByExample(example);
		return list;
	}
	
	/**
	 * 根据发货明细查询拣货明细包括实际拣货库位列表
	 * @param deliveryDetailIds
	 * @param orgId
	 * @param warehouseId
	 * @return
	 * @throws Exception
	 */
	public ResultModel qryPickDetailVosAndRealLoc(List<String> deliveryDetailIds,String orgId,String warehouseId,Boolean hasPage) throws Exception{
		//查询拣货明细
		List<SendPickDetail> list = qryPickQtyByDeliveryDetails(deliveryDetailIds,orgId,warehouseId);
		if(list == null || list.isEmpty()) return null;
		List<String> pickDetailIds = list.stream().parallel().map(t->t.getPickDetailId()).collect(Collectors.toList());
		
		//查询实际拣货库位列表
		SendPickLocationVo realParam = new SendPickLocationVo();
		realParam.setPickDetailIds(pickDetailIds);
		realParam.getSendPickLocation().setPickType(Constant.PICK_TYPE_REAL);
		
		//是否用分页查询
		ResultModel result = new ResultModel();
		Page<SendPickLocation> page = null;
		if(hasPage){
			if ( realParam.getCurrentPage() == null ) {
				realParam.setCurrentPage(0);
			}
			if ( realParam.getPageSize() == null ) {
				realParam.setPageSize(DEFAULT_PAGE_SIZE);
			}
			page = PageHelper.startPage(realParam.getCurrentPage()+1, realParam.getPageSize());
		}
		//查询实际拣货库位列表
		List<SendPickLocationVo> realLocVos = pickLocationService.qryPickLocations(realParam);
		result.setPage(page);
		result.setList(realLocVos);
		
		Map<String,List<SendPickLocationVo>> pickDetailMap = new HashMap<String,List<SendPickLocationVo>>();
		for(SendPickLocationVo locationVo:realLocVos){
			List<SendPickLocationVo> locList = null;
			if(pickDetailMap.containsKey(locationVo.getSendPickLocation().getPickDetailId())){
				locList = pickDetailMap.get(locationVo.getSendPickLocation().getPickDetailId());
			}else{
				locList = new ArrayList<SendPickLocationVo>();
			}
			locList.add(locationVo);
			pickDetailMap.put(locationVo.getSendPickLocation().getPickDetailId(), locList);
		}
		
//		List<SendPickDetailVo> pDetailVoList = new ArrayList<SendPickDetailVo>();
		for(SendPickDetail pDetail:list){
			SendPickDetailVo pDetailVo = new SendPickDetailVo();
			pDetailVo.setSendPickDetail(pDetail);
			pDetailVo.setRealPickLocations(pickDetailMap.get(pDetail.getPickDetailId()));
		}
		
		return result;
	}
	
	/**
	 * 根据波次单id查询拣货单列表
	 * @param waveId
	 * @param orgId
	 * @param warehouseId
	 * @return
	 * @version 2017年3月2日 下午4:17:35<br/>
	 * @author Aaron He<br/>
	 */
	public List<SendPickVo> qryPicksByWaveId(String waveId,String orgId,String warehouseId){
		SendPick param = new SendPick();
		param.setWaveId(waveId);
		param.setOrgId(orgId);
		param.setWarehouseId(warehouseId);
		List<SendPick> list = pickDao.qryList(param);
		List<SendPickVo> pickVos = new ArrayList<SendPickVo>();
		for (SendPick pick : list) {
			SendPickVo vo = new SendPickVo();
			vo.setSendPick(pick);
			pickVos.add(vo);
		}
		return pickVos;
	}
	
	
	/**
	 * 查询计划拣货货品的拣货单列表
	 * @param param
	 * @return
	 */
	@Transactional
	public List<SendPickVo> qryPicksByPlanLocation(SendPickDetailVo param) throws Exception{
		List<SendPickVo> pickVoList = new ArrayList<SendPickVo>();
		//查询库位对应的拣货计划库位列表
		param.getPlanPickLocations().get(0).getSendPickLocation().setPickType(Constant.PICK_TYPE_PLAN);
		List<SendPickLocationVo> list = pickLocationService.qryPickLocations(param.getPlanPickLocations().get(0));
		if(list == null || list.isEmpty()) return null;
		//统计拣货明细的拣货数量  Map<detailid,int>
		Map<String,Double> detailNumMap = new HashMap<String,Double>();
		for(SendPickLocationVo sendPickLocationVo:list){
			String pickDetailId = sendPickLocationVo.getSendPickLocation().getPickDetailId();
			Double num = sendPickLocationVo.getSendPickLocation().getPickQty();
			if(detailNumMap.containsKey(pickDetailId)){
				num += detailNumMap.get(pickDetailId);
			}
			detailNumMap.put(pickDetailId, num);
		}
		
		//根据sku，批次，与计划拣货库位，查询拣货明细列表
		param.setPlanPickLocations(list);
		param.qryDetailIdsByLocations(list);
		//获取登录用户
		Principal loginUser = LoginUtil.getLoginUser();
		//获取企业id与仓库id
		String orgId = loginUser.getOrgId();
		String warehouseId = LoginUtil.getWareHouseId();
		param.getSendPickDetail().setOrgId(orgId);
		param.getSendPickDetail().setWarehouseId(warehouseId);
		//根据拣货库位明细查询对应的拣货单明细
		List<SendPickDetailVo> detailVoList = pickDetailService.qryDetails(param);
		//统计拣货单的拣货数量 Map<pickid,num>
		Map<String,Double> pickNumMap = new HashMap<String,Double>();
		for(SendPickDetailVo sendPickDetailVo:detailVoList){
			String pickId = sendPickDetailVo.getSendPickDetail().getPickId();
			Double num = detailNumMap.get(sendPickDetailVo.getSendPickDetail().getPickDetailId());
			if(pickNumMap.containsKey(pickId)){
				num += pickNumMap.get(pickId);
			}
			pickNumMap.put(pickId, num);
		}
		
		if(detailVoList == null || detailVoList.isEmpty()) return null;

		List<String> idList = detailVoList.stream().map(t->t.getSendPickDetail().getPickId())
											.collect(Collectors.toList());

		//根据拣货明细列表查询拣货单
		Example example = new Example(SendPick.class);
		Criteria c = example.createCriteria();
		c.andIn("pickId", idList);
		c.andIn("pickStatus", Arrays.asList(Constant.PICK_STATUS_ACTIVE,Constant.PICK_STATUS_WORKING));
		List<SendPick> pickList = pickDao.selectByExample(example);
		for (SendPick pick : pickList) {
			pick.setPlanPickQty(pickNumMap.get(pick.getPickId()));
			SendPickVo vo = new SendPickVo();
			vo.setSendPick(pick);
			pickVoList.add(vo);
		}
		return pickVoList;
	}
	
	/**
	 * 更新拣货单状态
	 * @param pickId
	 * @param operator
	 * @param status
	 * @return
	 * @throws Exception
	 * @version 2017年2月16日 下午6:49:21<br/>
	 * @author Aaron He<br/>
	 */
	public int updateStatus(String pickId,String operator,int status)throws Exception{
		SendPick pick = new SendPick();
		pick.setOpPerson("");
		pick.setPickId(pickId);
		pick.setPickStatus(status);
		pick.setUpdatePerson(operator);
		pick.setUpdateTime(new Date());
		int num = pickDao.updateByPrimaryKeySelective(pick);
		return num;
	}
	
	/**
	 * 获取拣货单状态
	 * @param pickId
	 * @return
	 * @version 2017年2月16日 下午6:48:19<br/>
	 * @author Aaron He<br/>
	 */
	public int getPickStatus(String pickId){
		if(StringUtils.isEmpty(pickId)) return 0;
		SendPick pick = pickDao.selectByPrimaryKey(pickId);
		if(pick == null) return 0;
		return pick.getPickStatus().intValue();
		
	}
	
	/**
	 * 根据条件更新拣货单
	 * @param pick
	 * @param example
	 */
	public void updatePickByExample(SendPick pick,Example example) throws Exception{
		pickDao.updateByExampleSelective(pick, example);
	}
	
	/**
	 * 检查库位计划拣货数量总数是否 与计划拣货数量是否一致
	 * @param pickLocations
	 * @param orderQty
	 * @version 2017年2月22日 上午10:57:56<br/>
	 * @author Aaron He<br/>
	 * @throws Exception 
	 */
	public void checkPlanQty(List<SendPickLocationVo> pickLocations,double orderQty,double orderWeight,double orderVolume) throws Exception{
		if(pickLocations == null || pickLocations.isEmpty()) 
			throw new BizException(BizStatus.PLAN_PICK_NUMANDLOCATION_ERROR.getReasonPhrase());
		double qtySum = 0d;
		double weightSum = 0.0;
		double volumeSum = 0.0;
		for(SendPickLocationVo locaVo:pickLocations){
			if(StringUtil.isTrimEmpty(locaVo.getSendPickLocation().getLocationId())){
				throw new BizException(BizStatus.PLAN_PICK_NUMANDLOCATION_ERROR.getReasonPhrase());
			}
			if(locaVo.getSendPickLocation().getPickQty().doubleValue() == 0){
				throw new BizException(BizStatus.PLAN_PICK_NUMANDLOCATION_ERROR.getReasonPhrase());
			}
			qtySum += locaVo.getSendPickLocation().getPickQty();
			weightSum += locaVo.getSendPickLocation().getPickWeight();
			volumeSum += locaVo.getSendPickLocation().getPickVolume();		
		}
		weightSum = NumberUtil.round(weightSum, 2);
		volumeSum = NumberUtil.round(volumeSum, 6);
		
		//检查库位计划拣货数量总数是否 与计划拣货数量是否一致
		if(qtySum != orderQty)
			throw new BizException(BizStatus.ORDER_QTY_NOT_EQUAL_PLANPICK_QTY.getReasonPhrase());
		if(weightSum != orderWeight)
			throw new BizException(BizStatus.ORDER_WEIGHT_NOT_EQUAL_PLANPICK_WEIGHT.getReasonPhrase());
		if(volumeSum != orderVolume)
			throw new BizException(BizStatus.ORDER_VOLUME_NOT_EQUAL_PLANPICK_VOLUME.getReasonPhrase());
	}
	
	/**
	 * 打印拣货单与快递单
	 * 1、检查拣货单状态是否生效
	 * 2、更新拣货单打印次数
	 * 3、更新快递单打印次数
	 * @param sendPickVo
	 * @param p
	 */
	@Transactional(rollbackFor=Exception.class)
	public void printPickAndExpress(List<SendPickVo> pickVoList,Principal p)throws Exception{
		if(pickVoList == null || pickVoList.isEmpty()) return;
		for(SendPickVo sendPickVo:pickVoList){
			//1、检查状态是生效以后的状态
			int status = getPickStatus(sendPickVo.getSendPick().getPickId());
			if(status == Constant.PICK_STATUS_OPEN || status == Constant.PICK_STATUS_CANCAL)
				throw new BizException(BizStatus.PICK_STATUS_IS_NOT_ACTIVE.getReasonPhrase());
			//更新拣货单打印次数
			pickDao.incPrintTimes(sendPickVo.getSendPick());
			//更新快递单打印次数
			deliveryDao.incExpressPrintTimes(sendPickVo.getSendPick().getDeliveryId());
		}
	}
	
	/**
	 * 新增异常
	 * @param exceptionType
	 * @param involveBill
	 * @param exceptionStatus
	 * @param exceptionDesc
	 * @throws Exception
	 * @version 2017年3月6日 上午10:46:23<br/>
	 * @author Aaron He<br/>
	 */
	public void newExceptionLog(int excType,String involveBill,int excStatus,int excLevel,String excDesc)throws Exception{
		ExceptionLog exceptionLog = new ExceptionLog();
		exceptionLog.setExType(excType);
		exceptionLog.setInvolveBill(involveBill);
		exceptionLog.setExStatus(excStatus);
		exceptionLog.setExDesc(excDesc);
		exceptionLog.setExLevel(excLevel);
		exceptionLogService.add(exceptionLog);		
	}
	
	public static void main(String[] args) {
		System.out.println(String.format(ServiceConstant.ERRLOG_PICK_NUM_NOT_CONFORM,"123"));
	}


	@Override
	public int updateAssisStatus(String pickId, String status) {
		SendPick pick = new SendPick();
		pick.setPickId(pickId);
		pick.setAssisStatus(status);
		pick.setUpdateTime(new Date());
		return pickDao.updateByPrimaryKeySelective(pick);
	}
	
	public List<String>getTask(String orgId){
		return pickDao.getTask(orgId);
	}

	@Override
	public List<SendPickVo> queryByDeliveryId(String deliveryId) throws Exception {
		SendPick entity = new SendPick();
		entity.setDeliveryId(deliveryId);
		List<SendPick> picks = pickDao.select(entity);
		List<SendPickVo> vos = new ArrayList<SendPickVo>();
		if(picks != null) for(SendPick pick : picks) {
			vos.add(view(pick.getPickId()));
		}
		return vos;
	}

	@Override
	public List<SendPickVo> queryByWaveId(String waveId) throws Exception {
		SendPick entity = new SendPick();
		entity.setWaveId(waveId);
		List<SendPick> picks = pickDao.select(entity);
		List<SendPickVo> vos = new ArrayList<SendPickVo>();
		if(picks != null) for(SendPick pick : picks) {
			vos.add(view(pick.getPickId()));
		}
		return vos;
	}
}