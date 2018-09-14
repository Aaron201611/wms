package com.yunkouan.wms.modules.sys.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunkouan.entity.ResultModel;
import com.yunkouan.exception.BizException;
import com.yunkouan.saas.modules.sys.entity.SysAuth;
import com.yunkouan.saas.modules.sys.service.IAuthService;
import com.yunkouan.saas.modules.sys.service.IWarehouseService;
import com.yunkouan.saas.modules.sys.vo.AuthVo;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.common.service.BaseService;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.inv.entity.InvAdjust;
import com.yunkouan.wms.modules.inv.entity.InvCount;
import com.yunkouan.wms.modules.inv.entity.InvShift;
import com.yunkouan.wms.modules.inv.service.IAdjustService;
import com.yunkouan.wms.modules.inv.service.ICountService;
import com.yunkouan.wms.modules.inv.service.IShiftService;
import com.yunkouan.wms.modules.inv.service.IStockService;
import com.yunkouan.wms.modules.inv.vo.InvAdjustVO;
import com.yunkouan.wms.modules.inv.vo.InvCountVO;
import com.yunkouan.wms.modules.inv.vo.InvShiftVO;
import com.yunkouan.wms.modules.inv.vo.InvWarnVO;
import com.yunkouan.wms.modules.monitor.entity.ExceptionLog;
import com.yunkouan.wms.modules.monitor.service.IExceptionLogService;
import com.yunkouan.wms.modules.monitor.service.IShelflifeWarningService;
import com.yunkouan.wms.modules.monitor.vo.ExceptionLogVO;
import com.yunkouan.wms.modules.monitor.vo.ShelflifeWarningVO;
import com.yunkouan.wms.modules.rec.entity.RecAsn;
import com.yunkouan.wms.modules.rec.entity.RecPutaway;
import com.yunkouan.wms.modules.rec.service.IASNExtlService;
import com.yunkouan.wms.modules.rec.service.IPutawayExtlService;
import com.yunkouan.wms.modules.rec.vo.RecAsnVO;
import com.yunkouan.wms.modules.rec.vo.RecPutawayVO;
import com.yunkouan.wms.modules.send.entity.SendDelivery;
import com.yunkouan.wms.modules.send.entity.SendPick;
import com.yunkouan.wms.modules.send.entity.SendWave;
import com.yunkouan.wms.modules.send.service.IDeliveryService;
import com.yunkouan.wms.modules.send.service.IPickService;
import com.yunkouan.wms.modules.send.service.IWaveService;
import com.yunkouan.wms.modules.send.vo.SendDeliveryVo;
import com.yunkouan.wms.modules.send.vo.SendPickVo;
import com.yunkouan.wms.modules.send.vo.SendWaveVo;
import com.yunkouan.wms.modules.sys.service.IHomepageService;
import com.yunkouan.wms.modules.sys.vo.AccountVo;
import com.yunkouan.wms.modules.sys.vo.EventDetailVO;
import com.yunkouan.wms.modules.sys.vo.EventVO;
import com.yunkouan.wms.modules.sys.vo.RoleWarehouse;

/**
 * 移位服务实现类
 */
@Service
public class HomepageServiceImpl extends BaseService implements IHomepageService {
	/**
	 * 数据层接口
	 */
	//	@Autowired
	//	private IHomepageDao homepageDao;
	/**
	 * 外部服务-用户
	 */
	//	@Autowired
	//	private IUserService userService;
	/**
	 * 外部服务-收货单
	 */
	@Autowired
	IASNExtlService asnService;
	/**
	 * 外部服务-上架单
	 */
	@Autowired
	private IPutawayExtlService putawayExtlService;
	/**
	 * 外部服务-发货单
	 */
	@Autowired
	private IDeliveryService deliveryService;
	/**
	 * 外部服务-异常单
	 */
	@Autowired
	private IExceptionLogService exceptionLogService;
	/**
	 * 外部服务-拣货单
	 */
	@Autowired
	private IPickService pickService;
	/**@Fields 权限服务接口**/
	@Autowired
	private IAuthService service;

	/**
	 * 外部服务-波次单
	 */
	@Autowired
	private IWaveService waveService;
	/**
	 * 外部服务-盘点单
	 */
	@Autowired
	private ICountService countService;
	/**
	 * 外部服务-保质期预警
	 */
	@Autowired
	private IShelflifeWarningService shelflifeService;
	/**
	 * 库存业务服务类
	 * @author 王通<br/>
	 */
	@Autowired
	private IStockService stockService;
	/**
	 * 外部服务-移位单
	 */
	@Autowired
	private IShiftService shiftService;
	/**
	 * 外部服务-盈亏调整单
	 */
	@Autowired
	private IAdjustService adjustService;

	@Autowired
	private IWarehouseService iWarehouseService;

	@Autowired
	private AccountServiceImpl accountServiceImpl;

	/**
	 * Default constructor
	 */
	public HomepageServiceImpl() {
	}

	/**
	 * @return
	 * @throws Exception
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年5月15日 上午9:48:46<br/>
	 * @author 王通<br/>
	 */
	@Override
	@Transactional(readOnly=false)
	public EventVO list() throws Exception {
		Principal loginUser = LoginUtil.getLoginUser();
		// 验证用户是否登录
		if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
			throw new BizException("valid_common_user_no_login");
		}
		//获取当前用户权限
		SysAuth entity = new SysAuth()
				.setAuthStatus(Constant.STATUS_ACTIVE)
				//		.setParentId("23924E36B12F43A28EADD7AEFE5E7F7B")
				.setAuthLevel(loginUser.getAuthLevel());
		AuthVo vo = new AuthVo(entity);
		vo.setAccountId(loginUser.getAccountId());
		vo.setOrgId(loginUser.getOrgId())
		.setWarehouseId(LoginUtil.getWareHouseId());
		List<SysAuth> list = null;
		if(loginUser.getAuthLevel() != SysAuth.AUTH_LEVEL_ORG_USER) {
			list = new ArrayList<SysAuth>();
		} else {
			list = service.query(vo);
		}
		List<AuthVo> result = new ArrayList<AuthVo>();
		if(list != null) for(int i=0; i<list.size(); ++i) {
			//查询二级权限
			result.add(new AuthVo(list.get(i)).clearAllNull());
		}
		//获取权限end
		Integer eventNum = 0;
		List<EventDetailVO> homepageVoList = new ArrayList<EventDetailVO>();
		// 查询打开状态收货单
		if (hasPermission(result, "asn.view")) {
			RecAsnVO recAsnVO = new RecAsnVO();
			RecAsn recAsn = new RecAsn();
			recAsnVO.setAsn(recAsn);
			recAsn.setAsnStatus(Constant.ASN_STATUS_OPEN);
			Integer asnCount = this.asnService.countAsnByExample(recAsnVO);
			if (asnCount > 0) {
				homepageVoList.add(new EventDetailVO("/storeMgmt/receiveMgmt?status=" + Constant.STATUS_OPEN, "待处理的收货单" , asnCount));
				eventNum += asnCount;
			}
		}
		// 查询打开状态上架单
		if (hasPermission(result, "putaway.view")) {
			RecPutawayVO recPutawayVO = new RecPutawayVO();
			RecPutaway putaway = new RecPutaway();
			recPutawayVO.setPutaway(putaway);
			putaway.setPutawayStatus(Constant.PUTAWAY_STATUS_OPEN);
			List<RecPutaway> recPutawayList = putawayExtlService.listPutawayByExample(recPutawayVO);
			if (recPutawayList != null && recPutawayList.size() > 0) {
				homepageVoList.add(new EventDetailVO("/storeMgmt/shelvesMgmt?status=" + Constant.STATUS_OPEN, "待处理的上架单" , recPutawayList.size()));
				eventNum += recPutawayList.size();
			}
		}
		// 查询打开状态发货单
		if (hasPermission(result, "send.view")) {
			SendDelivery sendDelivery = new SendDelivery();
			sendDelivery.setDeliveryStatus(Constant.STATUS_OPEN);
			List<SendDeliveryVo> sendDeliveryVoList = this.deliveryService.qryList(sendDelivery);
			if (sendDeliveryVoList != null && sendDeliveryVoList.size() > 0) {
				homepageVoList.add(new EventDetailVO("/storeMgmt/deliveryGoodMgmt?status=" + Constant.STATUS_OPEN, "待处理的发货单" , sendDeliveryVoList.size()));
				eventNum += sendDeliveryVoList.size();
			}
		}
		// 查询打开状态拣货单
		if (hasPermission(result, "pick.view")) {
			SendPickVo sendPickVo = new SendPickVo();
			SendPick sendPick = new SendPick();
			sendPickVo.setSendPick(sendPick);
			sendPick.setPickStatus(Constant.SEND_STATUS_OPEN);
			List<SendPickVo> sendPickVoList =  pickService.qryListByParam(sendPickVo);
			if (sendPickVoList != null && sendPickVoList.size() > 0) {
				homepageVoList.add(new EventDetailVO("/storeMgmt/pickingMgmt?status=" + Constant.STATUS_OPEN, "待处理的拣货单" , sendPickVoList.size()));
				eventNum += sendPickVoList.size();
			}
		}
		// 查询打开状态波次单
		if (hasPermission(result, "wave.view")) {
			SendWaveVo sendWaveVo = new SendWaveVo();
			SendWave sendWave = new SendWave();
			sendWave.setWaveStatus(Constant.STATUS_OPEN);
			sendWaveVo.setSendWave(sendWave);
			Integer waveCount = this.waveService.count(sendWaveVo);
			if (waveCount > 0) {
				homepageVoList.add(new EventDetailVO("/storeMgmt/waveCountMgmt?status=" + Constant.STATUS_OPEN, "待处理的波次单" , waveCount));
				eventNum += waveCount;
			}
		}
		// 查询打开状态盘点单
		if (hasPermission(result, "count.view")) {
			InvCountVO countVo = new InvCountVO();
			InvCount count = new InvCount();
			countVo.setInvCount(count);
			count.setCountStatus(Constant.STATUS_OPEN);
			Integer countCount = this.countService.countByExample(countVo);
			if (countCount > 0) {
				homepageVoList.add(new EventDetailVO("/storeMgmt/takeStock?status=" + Constant.STATUS_OPEN, "待处理的盘点单" , countCount));
				eventNum += countCount;
			}
		}
		// 查询打开状态盈亏调整单
		if (hasPermission(result, "adjust.view")) {
			InvAdjustVO adjustVo = new InvAdjustVO();
			InvAdjust adjust = new InvAdjust();
			adjustVo.setInvAdjust(adjust);
			adjust.setAdjustStatus(Constant.STATUS_OPEN);
			Integer adjustCount = this.adjustService.countByExample(adjustVo);
			if (adjustCount > 0) {
				homepageVoList.add(new EventDetailVO("/storeMgmt/profitLossAdjustment?status=" + Constant.STATUS_OPEN, "待处理的盈亏调整单" , adjustCount));
				eventNum += adjustCount;
			}
		}
		// 查询打开状态移位单
		if (hasPermission(result, "shift.view")) {
			InvShiftVO shiftVo = new InvShiftVO();
			InvShift shift = new InvShift();
			shiftVo.setInvShift(shift);
			shift.setShiftStatus(Constant.STATUS_OPEN);
			Integer shiftCount = this.shiftService.countByExample(shiftVo);
			if (shiftCount > 0) {
				homepageVoList.add(new EventDetailVO("/storeMgmt/gression?status=" + Constant.STATUS_OPEN, "待处理的移位单" , shiftCount));
				eventNum += shiftCount;
			}
		}

		// 查询未处理的异常
		if (hasPermission(result, "exceptionLog.view")) {
			ExceptionLogVO expVo = new ExceptionLogVO();
			ExceptionLog exp = new ExceptionLog();
			expVo.setExceptionLog(exp);
			exp.setExStatus(Constant.STATUS_OPEN);
			Integer count = this.exceptionLogService.countByExample(expVo);
			if (count > 0) {
				homepageVoList.add(new EventDetailVO("/monitoringCenter/exceptionMgmt?status=" + Constant.STATUS_OPEN, "待处理的异常" , count));
				eventNum += count;
			}
		}
		// 查询未处理的库存预警
		if (hasPermission(result, "stockWarning.view")) {
			InvWarnVO paramVo = new InvWarnVO();
			Integer count = this.stockService.countWarnByExample(paramVo);
			if (count > 0) {
				homepageVoList.add(new EventDetailVO("/monitoringCenter/inventoryWarning?status=" + Constant.STATUS_OPEN, "货品库存预警数" , count));
				eventNum += count;
			}
		}

		// 查询未处理的保质期预警
		if (hasPermission(result, "shelflifeWarning.view")) {
			Integer count = this.shelflifeService.count();
			if (count > 0) {
				homepageVoList.add(new EventDetailVO("/monitoringCenter/shelflifeWarning?status=" + Constant.STATUS_OPEN, "待处理的保质期预警" , count));
				eventNum += count;
			}
		}
		EventVO eventVo = new EventVO();
		eventVo.setEventList(homepageVoList);
		eventVo.setEventNum(eventNum);

		//		ObjectMapper objectMapper = new ObjectMapper();
		//		objectMapper.setSerializationInclusion(Include.NON_NULL);
		//		log.debug(objectMapper.writeValueAsString(eventVo));
		return eventVo;
	}

	/**
	 * @param result
	 * @param string
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年6月3日 下午3:20:32<br/>
	 * @author 王通<br/>
	 */
	private boolean hasPermission(List<AuthVo> result, String auth) {
		for (AuthVo authVo : result) {
			if (StringUtils.equals(authVo.getEntity().getAuthNo(), auth)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 查看当前登录用户所有拥有权限的的仓库任务
	 * 
	 */
	public List<EventVO> warehouseTasks() throws Exception {
		Principal loginUser = LoginUtil.getLoginUser();
		// 验证用户是否登录
		if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
			throw new BizException("valid_common_user_no_login");
		}
		String orgId=loginUser.getOrgId();
		//根据登录用户企业id获取所有的企业所有的仓库
//		MetaWarehouseVO metaWarehouseVO=new MetaWarehouseVO();
//		MetaWarehouse metaWarehouse=new MetaWarehouse();
//		metaWarehouse.setOrgId(orgId);
//		metaWarehouseVO.setWarehouse(metaWarehouse);
//		List<MetaWarehouse> warehouseList=iWarehouseService.getWarehouseList(metaWarehouseVO);
		ResultModel resultModel=accountServiceImpl.view(loginUser.getAccountId(), loginUser);
		AccountVo accountVo=(AccountVo) resultModel.getObj();
		List<RoleWarehouse> roles=accountVo.getList();
		if(roles.isEmpty())return null;
		//测试发现可能有重复的仓库id
		Set<String> ids=new HashSet<>();
		for(RoleWarehouse role:roles){
			ids.add(role.getWarehouseId());
		}

		List<String> asnTaskList=asnService.getTask(orgId);
		List<String> putawayTaskList=putawayExtlService.getTask(orgId);
		List<String> deliveryTaskList=deliveryService.getTask(orgId);
		List<String> pickTaskList=pickService.getTask(orgId);

		List<String> waveTaskList=waveService.getTask(orgId);
		List<String> countTaskList=countService.getTask(orgId);
		List<String> adjustTaskList=adjustService.getTask(orgId);
		List<String> shiftTaskList=shiftService.getTask(orgId);


		ExceptionLogVO expVo = new ExceptionLogVO();
		ExceptionLog exp = new ExceptionLog();
		exp.setOrgId(orgId);
		expVo.setExceptionLog(exp);
		exp.setExStatus(Constant.STATUS_OPEN);
		List<ExceptionLog> exceptionTaskList=exceptionLogService.countByExample2(expVo);

		InvWarnVO paramVo = new InvWarnVO();
		paramVo.setOrgId(orgId);
		List<InvWarnVO> countWarnTaskList = this.stockService.countWarnByExample2(paramVo);

		List<ShelflifeWarningVO> shelflifeTaskList=shelflifeService.count(orgId);

		List<EventVO> eventVoList=new ArrayList<>();

		//for(MetaWarehouse warehouse:warehouseList){
		for(String warehouseId:ids){
			//String warehouseId=warehouse.getWarehouseId();

			EventVO eventVO=new EventVO();
			List<EventDetailVO> homepageVoList = new ArrayList<EventDetailVO>();
			int countAll=0;
			if(asnTaskList!=null&&asnTaskList.size()>0){
				int count=0;
				for(String str:asnTaskList){
					if(warehouseId.equals(str)){
						count++;
					}
				}
				countAll+=count;
				if(count!=0)
					homepageVoList.add(new EventDetailVO("", "待处理的收货单" , count));
			}
			if(putawayTaskList!=null&&putawayTaskList.size()>0){
				int count=0;
				for(String str:putawayTaskList){
					if(warehouseId.equals(str)){
						count++;
					}
				}
				countAll+=count;
				if(count!=0)
					homepageVoList.add(new EventDetailVO("", "待处理的上架单" , count));
			}
			if(deliveryTaskList!=null&&deliveryTaskList.size()>0){
				int count=0;
				for(String str:deliveryTaskList){
					if(warehouseId.equals(str)){
						count++;
					}
				}
				countAll+=count;
				if(count!=0)
					homepageVoList.add(new EventDetailVO("", "待处理的发货单" , count));
			}
			if(pickTaskList!=null&&pickTaskList.size()>0){
				int count=0;
				for(String str:pickTaskList){
					if(warehouseId.equals(str)){
						count++;
					}
				}
				countAll+=count;
				if(count!=0)
					homepageVoList.add(new EventDetailVO("", "待处理的拣货单" , count));
			}
			if(waveTaskList!=null&&waveTaskList.size()>0){
				int count=0;
				for(String str:waveTaskList){
					if(warehouseId.equals(str)){
						count++;
					}
				}
				countAll+=count;
				if(count!=0)
					homepageVoList.add(new EventDetailVO("", "待处理的波次单" , count));
			}
			if(countTaskList!=null&&countTaskList.size()>0){
				int count=0;
				for(String str:countTaskList){
					if(warehouseId.equals(str)){
						count++;
					}
				}
				countAll+=count;
				if(count!=0)
					homepageVoList.add(new EventDetailVO("", "待处理的盘点单" , count));
			}
			if(adjustTaskList!=null&&adjustTaskList.size()>0){
				int count=0;
				for(String str:adjustTaskList){
					if(warehouseId.equals(str)){
						count++;
					}
				}
				countAll+=count;
				if(count!=0)
					homepageVoList.add(new EventDetailVO("", "待处理的盈亏调整单" , count));
			}
			if(shiftTaskList!=null&&shiftTaskList.size()>0){
				int count=0;
				for(String str:shiftTaskList){
					if(warehouseId.equals(str)){
						count++;
					}
				}
				countAll+=count;
				if(count!=0)
					homepageVoList.add(new EventDetailVO("", "待处理的移位单" , count));
			}
			if(exceptionTaskList!=null&&exceptionTaskList.size()>0){
				int count=0;
				for(ExceptionLog str:exceptionTaskList){
					if(warehouseId.equals(str.getWarehouseId())){
						count++;
					}
				}
				countAll+=count;
				if(count!=0)
					homepageVoList.add(new EventDetailVO("", "待处理的异常" , count));
			}
			if(countWarnTaskList!=null&&countWarnTaskList.size()>0){
				int count=0;
				for(InvWarnVO str:countWarnTaskList){
					if(warehouseId.equals(str.getWarehouseId())){
						count++;
					}
				}
				countAll+=count;
				if(count!=0)
					homepageVoList.add(new EventDetailVO("", "待处理的库存预警" , count));
			}
			if(shelflifeTaskList!=null&&shelflifeTaskList.size()>0){
				int count=0;
				for(ShelflifeWarningVO str:shelflifeTaskList){
					if(warehouseId.equals(str.getWarehouseId())){
						count++;
					}
				}
				countAll+=count;
				if(count!=0)
					homepageVoList.add(new EventDetailVO("", "待处理的保质期预警" , count));
			}
			eventVO.setEventNum(countAll);
			//eventVO.setWarehouseName(warehouse.getWarehouseName());
			eventVO.setWarehouseName(getWarehouseName(roles,warehouseId));
			eventVoList.add(eventVO);
			eventVO.setEventList(homepageVoList);
		}
		return eventVoList;
	}
	private String getWarehouseName(List<RoleWarehouse> roles,String warehouseId){
		for(RoleWarehouse role:roles){
			if(warehouseId.equals(role.getWarehouseId())){
				return role.getWarehouseName();
			}
		}
		return null;
	}
}