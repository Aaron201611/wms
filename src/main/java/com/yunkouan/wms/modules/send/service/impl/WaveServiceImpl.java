package com.yunkouan.wms.modules.send.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.util.StringUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yunkouan.entity.ResultModel;
import com.yunkouan.exception.BizException;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.common.constant.BizStatus;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.common.service.BaseService;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.FqDataUtils;
import com.yunkouan.wms.common.util.IdUtil;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.inv.service.IStockService;
import com.yunkouan.wms.modules.meta.dao.ISkuDao;
import com.yunkouan.wms.modules.meta.entity.MetaSku;
import com.yunkouan.wms.modules.meta.service.IMerchantService;
import com.yunkouan.wms.modules.meta.service.ISkuService;
import com.yunkouan.wms.modules.send.dao.IWaveDao;
import com.yunkouan.wms.modules.send.entity.SendDelivery;
import com.yunkouan.wms.modules.send.entity.SendPick;
import com.yunkouan.wms.modules.send.entity.SendWave;
import com.yunkouan.wms.modules.send.service.ICreatePickService;
import com.yunkouan.wms.modules.send.service.IDeliveryService;
import com.yunkouan.wms.modules.send.service.IPickDetailService;
import com.yunkouan.wms.modules.send.service.IPickService;
import com.yunkouan.wms.modules.send.service.IWaveService;
import com.yunkouan.wms.modules.send.vo.DeliveryDetailVo;
import com.yunkouan.wms.modules.send.vo.SendDeliveryVo;
import com.yunkouan.wms.modules.send.vo.SendPickLocationVo;
import com.yunkouan.wms.modules.send.vo.SendPickVo;
import com.yunkouan.wms.modules.send.vo.SendWaveVo;

import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 波次单业务实现类
 */
@Service
public class WaveServiceImpl extends BaseService implements IWaveService,ICreatePickService {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private IWaveDao waveDao;

	@Autowired
	private IDeliveryService deliveryService;

	@Autowired
	private IPickService pickService;

	@Autowired
	private IPickDetailService pickDetailService;//拣货明细接口

	//	@Autowired
	//	private ICommonService commonService;

	@Autowired
	public IStockService stockService;//库存服务

	@Autowired 
	private IMerchantService merchantService;//客商接口
	
	@Autowired
	public ISkuService iSkuService;//库存服务

	public static final int DEFAULT_PAGE_SIZE = 10;

	/**
	 * 新增波次单
	 * 1、查询发货单
	 * 2、检查发货量，发货单数量小于2，不能创建波次单
	 * 3、统计订单数量
	 * 4、统计货品种数
	 * 5、生成波次单号
	 * 6、保存波次单
	 * 7、生成的波次单号回写至发货单
	 * @param param
	 * @version 2017年3月1日 下午3:54:39<br/>
	 * @author Aaron He<br/>
	 */
	@Transactional(rollbackFor=Exception.class)
	public SendWaveVo serchAndAdd(SendWaveVo param,String orgId,String warehouseId,String operator)throws Exception{		
		List<String> idList = param.toDeliveryIdList();
		//1、根据idList查询发货单
		List<SendDeliveryVo> deliveryList = deliveryService.qryByIds(idList);
		//2、发货单数量小于2，不能创建波次单
		if(deliveryList.size() < 2)
			throw new BizException(BizStatus.DELIVERY_QTY_LESS.getReasonPhrase(),"2");
		//3、统计订单数量
		param.setSendDeliberyVoList(deliveryList);
		param.countOrderTotal();
		//4、统计货品种数
		param.calSkuQty();
		//5、生成波次单号
		String waveNo = context.getStrategy4No(orgId, warehouseId).getWaveNo(orgId);
		String waveId = IdUtil.getUUID();
		//波次单状态为打开
		SendWave wave = param.getSendWave();

		//获取发货单的类型，保存为波次单的类型
		wave.setDeliveryType(deliveryList.get(0).getSendDelivery().getDocType());
		wave.setWaveId(waveId);
		wave.setWaveNo(waveNo);
		wave.setWaveStatus(Constant.WAVE_STATUS_OPEN);
		wave.setOrgId(orgId);
		wave.setWarehouseId(warehouseId);
		wave.setDeliveryAmount(deliveryList.size());
		wave.setCreatePerson(operator);
		wave.setUpdatePerson(operator);
		//设置默认值
		wave.defaultValue();
		//6、保存波次单
		wave.setWaveId2(context.getStrategy4Id().getWaveSeq());
		waveDao.insertSelective(wave);

		//7、生成的波次单号回写至发货单
		List<String> deliveryIds = param.toDeliveryIdList();
		deliveryService.batchUpdateWaveId(waveId, deliveryIds, Constant.SEND_STATUS_OPEN,operator);

		return param;
	}

	/**
	 * 保存并生效
	 * 1、判断是否存在waveid，不存在则新增，存在则更新
	 * 2、生效波次单
	 * @param param
	 * @param orgId
	 * @param warehouseId
	 * @param operator
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public SendWaveVo saveAndEnable(SendWaveVo sendWaveVo,String orgId,String warehouseId,String operator)throws Exception{
		//1、判断是否存在waveid，不存在则新增，存在则更新
		SendWaveVo waveVo = new SendWaveVo();
		if(sendWaveVo == null || StringUtils.isEmpty(sendWaveVo.getSendWave().getWaveId())){
			waveVo = serchAndAdd(sendWaveVo, orgId, warehouseId, operator);
		}else{
			waveVo = searchAndUpdate(sendWaveVo, orgId, warehouseId, operator);
		}
		//2、生效波次单
		//		active(waveVo.getSendWave().getWaveId(), orgId, warehouseId, operator);
		enable(waveVo.getSendWave().getWaveId(), orgId, warehouseId, operator);
		waveVo.getSendWave().setWaveStatus(Constant.WAVE_STATUS_ACTIVE);
		return waveVo;
	}


	/**
	 * 查询波次单分页
	 * @param sendWaveVo
	 * @param orgId
	 * @param warehouseId
	 * @return
	 * @version 2017年3月1日 下午7:20:14<br/>
	 * @author Aaron He<br/>
	 */
	public ResultModel qryPageList(SendWaveVo sendWaveVo,String orgId,String warehouseId){
		if(sendWaveVo == null || sendWaveVo.getSendWave() == null) return null;

		//不选择状态就默认查询除取消以外的所有状态
		if(sendWaveVo.getSendWave().getWaveStatus() == null){
			sendWaveVo.setWaveStatusList(Arrays.asList(Constant.WAVE_STATUS_ACTIVE,Constant.WAVE_STATUS_OPEN));
		}
		ResultModel result = new ResultModel();

		//分页设置
		Page<SendWave> page = null;
		if ( sendWaveVo.getCurrentPage() == null ) {
			sendWaveVo.setCurrentPage(0);
		}
		if ( sendWaveVo.getPageSize() == null ) {
			sendWaveVo.setPageSize(DEFAULT_PAGE_SIZE);
		}
		page = PageHelper.startPage(sendWaveVo.getCurrentPage()+1, sendWaveVo.getPageSize());
		//查询波次单列表
		List<SendWave> list = waveDao.selectByExample(sendWaveVo.getConditionExample());
		if ( list == null || list.isEmpty() ) {
			return result;
		}
		result.setPage(page);

		List<SendWaveVo> results = new ArrayList<SendWaveVo>();
		for (SendWave wave : list) {
			SendWaveVo vo = new SendWaveVo();
			vo.setSendWave(wave);
			results.add(vo);
		}	
		result.setList(results);
		return result;
	}

	/**
	 * 查询波次单列表
	 * @param sendWaveVo
	 * @return
	 * @version 2017年3月7日 上午9:23:00<br/>
	 * @author Aaron He<br/>
	 */
	public List<SendWaveVo> qryList(SendWaveVo param){
		if(param == null || param.getSendWave() == null) return null;

		//查询波次单列表
		List<SendWave> list = waveDao.selectByExample(param.getConditionExample());
		List<SendWaveVo> results = new ArrayList<SendWaveVo>();
		for (SendWave wave : list) {
			SendWaveVo vo = new SendWaveVo();
			vo.setSendWave(wave);
			results.add(vo);
		}
		return results;
	}

	/**
	 * 删除波次单下面的发货单
	 * @param sendWaveVo
	 * @param orgId
	 * @param warehouseId
	 * @param operator
	 * @version 2017年3月2日 下午1:16:41<br/>
	 * @author Aaron He<br/>
	 */
	@Transactional(rollbackFor=Exception.class)
	public void delDeliveries(SendWaveVo sendWaveVo,String orgId,String warehouseId,String operator) throws Exception{
		if(sendWaveVo == null || sendWaveVo.getSendWave() == null)
			throw new BizException(BizStatus.DATA_IS_NULL.getReasonPhrase());

		//波次单是否为打开
		checkStatus(sendWaveVo.getSendWave().getWaveId(),Constant.WAVE_STATUS_OPEN,BizStatus.WAVE_STATUS_IS_NOT_OPEN.getReasonPhrase());
		//删除波次单下面的发货单
		List<String> deliveryIds = sendWaveVo.toDeliveryIdList();		
		if(deliveryIds == null || deliveryIds.isEmpty()) return;

		deliveryService.batchUpdateWaveId(null, deliveryIds,Constant.SEND_STATUS_OPEN,operator);

		//根据波次单查询发货单
		SendWaveVo waveVo = getSendWaveVoById(sendWaveVo.getSendWave().getWaveId());
		//统计数量
		waveVo.countOrderTotal();
		//统计货品种数
		waveVo.calSkuQty();
		//更新波次单
		sendWaveVo.getSendWave().setUpdatePerson(operator);
		sendWaveVo.getSendWave().setDeliveryAmount(waveVo.getSendDeliberyVoList().size());
		sendWaveVo.getSendWave().setUpdateTime(new Date());
		waveDao.updateByPrimaryKeySelective(sendWaveVo.getSendWave());
	}

	/**
	 * 查询发货单并更新波次单
	 * @param sendWaveVo
	 * @param orgId
	 * @param warehouseId
	 * @param operator
	 * @throws Exception
	 * @version 2017年3月2日 下午1:57:43<br/>
	 * @author Aaron He<br/>
	 */
	@Transactional(rollbackFor=Exception.class)
	public SendWaveVo searchAndUpdate(SendWaveVo param,String orgId,String warehouseId,String operator)throws Exception{
		if(param == null)
			throw new BizException(BizStatus.DATA_IS_NULL.getReasonPhrase());
		//波次单是否打开
		checkStatus(param.getSendWave().getWaveId(),Constant.WAVE_STATUS_OPEN,BizStatus.WAVE_STATUS_IS_NOT_OPEN.getReasonPhrase());	
		//2、发货单数量小于2，不能创建波次单
		if(param.getSendDeliberyVoList().size() < 2)
			throw new BizException(BizStatus.DELIVERY_QTY_LESS.getReasonPhrase(),"2");
		//获取原波次单
		SendWaveVo orgVo = getSendWaveVoById(param.getSendWave().getWaveId());
		List<String> delIds = orgVo.toDeliveryIdList();		
		if(delIds != null && !delIds.isEmpty()){
			//删除波次单下原来的发货单
			deliveryService.batchUpdateWaveId(null, delIds,Constant.SEND_STATUS_OPEN,operator);
		};		
		//根据条件查询新关联的发货单
		List<String> idList = param.toDeliveryIdList();
		if(idList != null && !idList.isEmpty()){
			//更新新添加发货单的波次单号
			deliveryService.batchUpdateWaveId(param.getSendWave().getWaveId(), idList,Constant.SEND_STATUS_OPEN,operator);
			//根据idList查询发货单
			List<SendDeliveryVo> deliveryList = deliveryService.qryByIds(idList);
			//获取波次单
			param.setSendDeliberyVoList(deliveryList);
		};				
		//统计订单数量
		param.countOrderTotal();
		//统计货品种数
		param.calSkuQty();
		//更新波次单
		param.getSendWave().setDeliveryAmount(param.getSendDeliberyVoList().size());
		param.getSendWave().setUpdatePerson(operator);
		param.getSendWave().setUpdateTime(new Date());
		waveDao.updateByPrimaryKeySelective(param.getSendWave());

		return param;
	}

	/**
	 * 更新波次单发货数量
	 * @param waveId
	 * @param userId
	 * @throws Exception
	 */
	public void updateQty(String waveId,String userId)throws Exception{
		//查询波次单
		SendWaveVo waveVo = getSendWaveVoById(waveId);
		//计算sku数量
		waveVo.countOrderTotal();
		waveVo.getSendWave().setUpdatePerson(userId);
		waveVo.getSendWave().setUpdateTime(new Date());
		waveDao.updateByPrimaryKeySelective(waveVo.getSendWave());
	}

	/**
	 * 查看波次单
	 * @param waveId
	 * @return
	 * @version 2017年3月1日 下午7:26:44<br/>
	 * @author Aaron He<br/>
	 */
	public SendWaveVo view(SendWaveVo reqParam,String warehouseId){

		//查询波次单及发货单列表
		SendWaveVo result = new SendWaveVo();
		SendWave record = waveDao.selectByPrimaryKey(reqParam.getSendWave().getWaveId());
		result.setSendWave(record);

		return result;
	}

	/**
	 * 查询波次单的发货单明细
	 * @param waveVo
	 * @return
	 * @throws Exception
	 */
	public SendWaveVo printInfo(SendWaveVo waveVo) throws Exception{
		SendWave entity = waveDao.selectByPrimaryKey(waveVo.getSendWave().getWaveId());
		waveVo.setSendWave(entity);
		//获取登录用户
		Principal loginUser = LoginUtil.getLoginUser();
		//根据波次单查询发货单
		SendDelivery delivery = new SendDelivery();
		delivery.setOrgId(loginUser.getOrgId());
		delivery.setWarehouseId(LoginUtil.getWareHouseId());
		delivery.setWaveId(waveVo.getSendWave().getWaveId());
		List<SendDeliveryVo> deliveryVoList = deliveryService.qryList(delivery);
		for(SendDeliveryVo vo:deliveryVoList){
			SendDeliveryVo delVo = deliveryService.getDeliveryById(vo.getSendDelivery().getDeliveryId());
			if(delVo.getDeliveryDetailVoList() != null && !delVo.getDeliveryDetailVoList().isEmpty()){
				List<DeliveryDetailVo> detailVoList = new ArrayList<DeliveryDetailVo>();
				for(DeliveryDetailVo ddVo:delVo.getDeliveryDetailVoList()){
					MetaSku sku = FqDataUtils.getSkuById(iSkuService, ddVo.getDeliveryDetail().getSkuId());
					ddVo.setSkuNo(sku.getSkuNo());
					List<SendPickLocationVo> planLocationList = deliveryService.qryPlanLocationByDetailId(ddVo.getDeliveryDetail().getDeliveryDetailId(),loginUser.getOrgId(),LoginUtil.getWareHouseId());
					ddVo.setPlanPickLocations(planLocationList);
					detailVoList.add(ddVo);
				}
				delVo.setDeliveryDetailVoList(detailVoList);
			}
			waveVo.getSendDeliberyVoList().add(delVo);
		}
		return waveVo;
	}

	/**
	 * 生效波次单
	 * 1、检查波次单是否打开
	 * 2、生成拣货单
	 * 3、生效发货单
	 * 4、保存拣货单，并分配库位
	 * 5、生效波次单
	 * @param waveId
	 * @throws Exception
	 * @version 2017年3月1日 下午7:45:41<br/>
	 * @author Aaron He<br/>
	 */
	@Transactional(rollbackFor=Exception.class)
	public void active(String waveId,String orgId,String warehouseId,String operator) throws Exception{
		//1、检查波次单是否打开
		checkStatus(waveId,Constant.WAVE_STATUS_OPEN,BizStatus.WAVE_STATUS_IS_NOT_OPEN.getReasonPhrase());
		//查询波次单
		SendWaveVo swVo = getSendWaveVoById(waveId);			
		//2、生成拣货单
		SendPickVo pickVo = new SendPickVo();
		pickVo.createPickFromWave(swVo, operator);	
		for(SendDeliveryVo vo : swVo.getSendDeliberyVoList()) {	
			//查询发货明细
			List<DeliveryDetailVo> deliveryDetailVoList = deliveryService.getDetailsByDeliveryId(vo.getSendDelivery().getDeliveryId());
			//创建拣货明细
			pickVo.createPickDetaiVoList(deliveryDetailVoList,vo.getSendDelivery().getOwner(),operator);
			//3、生效发货单
			deliveryService.enable(vo.getSendDelivery().getDeliveryId(), operator);
		}
		//4、保存拣货单，并分配库位
		pickService.SavePickAndAssgn(pickVo, operator);
		//5、生效波次单
		updateWaveStatus(waveId,operator,Constant.WAVE_STATUS_ACTIVE);
	}

	/**
	 * 生效波次单（新）
	 * @param waveId
	 * @param orgId
	 * @param warehouseId
	 * @param operator
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void enable(String waveId,String orgId,String warehouseId,String operator) throws Exception{
		//1、检查波次单是否打开
		checkStatus(waveId,Constant.WAVE_STATUS_OPEN,BizStatus.WAVE_STATUS_IS_NOT_OPEN.getReasonPhrase());
		//查询波次单
		SendWaveVo swVo = getSendWaveVoById(waveId);			
		//2、生成拣货单
		SendPickVo pickVo = new SendPickVo();
		pickVo.createPickFromWave(swVo, operator);	
		//创建拣货单号
		String pickNo = context.getStrategy4No(orgId, warehouseId).getPickNo(orgId, pickVo.getSendPick().getDocType());
		pickVo.getSendPick().setPickNo(pickNo);
		//		pickVo.createPickNo(commonService);

		//创建拣货明细列表		
		for(SendDeliveryVo vo : swVo.getSendDeliberyVoList()) {	
			//查询发货明细
			List<DeliveryDetailVo> deliveryDetailVoList = deliveryService.getDetailsByDeliveryId(vo.getSendDelivery().getDeliveryId());
			//创建拣货明细
			pickVo.createPickDetaiVoList(deliveryDetailVoList,vo.getSendDelivery().getOwner(),operator);					
		}
		//自动分配库位
		pickVo = context.getStrategy4Pickup(orgId, warehouseId).allocation_new(pickVo, operator);
		//判断是否有异常
		List<BizException> exceptions=pickVo.getExceptions();
		if(exceptions!=null&&exceptions.size()>0){
			String exceptionall=groupMessage(exceptions);
			throw new BizException(exceptionall);
		}
		//创建计划拣货库位
		if(pickVo.getSendPickDetailVoList() != null && !pickVo.getSendPickDetailVoList().isEmpty()){
			pickVo.createPlanLocationsByHandle(pickVo,operator);
		}
		//保存拣货单
		pickService.saveNewPick(pickVo,operator);	
		for(SendDeliveryVo vo : swVo.getSendDeliberyVoList()) {	
			//生效发货单
			//			deliveryService.updateStatus(vo.getSendDelivery().getDeliveryId(),operator,Constant.SEND_STATUS_ACTIVE);
			deliveryService.enable(vo.getSendDelivery().getDeliveryId(), operator);
		}
		//5、生效波次单
		updateWaveStatus(waveId,operator,Constant.WAVE_STATUS_ACTIVE);
	}

	/**
	 * 失效波次单
	 * 1、波次单是否生效
	 * 2、检查所有拣货单状态是否打开
	 * 3、删除所有拣货单
	 * 4、所有发货单由生效变为打开
	 * 5、波次单由生效置为打开
	 * @param waveId
	 * @throws Exception
	 * @version 2017年3月1日 下午7:45:41<br/>
	 * @author Aaron He<br/>
	 */
	@Transactional(rollbackFor=Exception.class)
	public void disable(String waveId,String orgId,String warehouseId,String operator) throws Exception{

		//1、检查波次单是否生效
		checkStatus(waveId,Constant.WAVE_STATUS_ACTIVE,BizStatus.WAVE_STATUS_IS_NOT_ENABLE.getReasonPhrase());

		//2、检查所有拣货单状态是否打开
		List<SendPickVo> list = pickService.qryPicksByWaveId(waveId, orgId, warehouseId);
		if(list.stream().anyMatch(t->(t.getSendPick().getPickStatus().intValue() != Constant.PICK_STATUS_OPEN
				&& t.getSendPick().getPickStatus().intValue() != Constant.PICK_STATUS_CANCAL)))
			throw new BizException(BizStatus.PICK_STATUS_IS_NOT_OPEN.getReasonPhrase());
		//3、取消所有拣货单
		//		SendPickVo sendPickVo = new SendPickVo();
		//		sendPickVo.getSendPick().setWarehouseId(warehouseId);
		//		sendPickVo.getSendPick().setOrgId(orgId);
		//		sendPickVo.getSendPick().setWaveId(waveId);
		//		pickService.delByParam(sendPickVo.getConditionExample());
		SendPick pick = new SendPick();
		pick.setPickStatus(Constant.PICK_STATUS_CANCAL);
		pick.setUpdatePerson(operator);
		pick.setUpdateTime(new Date());

		SendPickVo paramVo = new SendPickVo();
		paramVo.getSendPick().setWaveId(waveId);
		paramVo.getSendPick().setOrgId(orgId);
		paramVo.getSendPick().setWarehouseId(warehouseId);

		pickService.updatePickByExample(pick, paramVo.getConditionExample());

		//4、所有发货单由生效变为打开
		SendDeliveryVo param = new SendDeliveryVo();
		param.getSendDelivery().setWaveId(waveId);
		param.getSendDelivery().setOrgId(orgId);
		param.getSendDelivery().setWarehouseId(warehouseId);
		param.getSendDelivery().setDeliveryStatus(Constant.SEND_STATUS_ACTIVE);
		deliveryService.updateStatusByParam(Constant.SEND_STATUS_OPEN,operator,param);
		//5、波次单由生效置为打开
		updateWaveStatus(waveId,operator,Constant.WAVE_STATUS_OPEN);
	}

	/**
	 * 取消波次单
	 * 1、波次单状态是否打开
	 * 2、删除所有关联的发货单
	 * 3、波次单由打开变为取消
	 * @param waveId
	 * @throws Exception
	 * @version 2017年3月1日 下午7:45:41<br/>
	 * @author Aaron He<br/>
	 */
	@Transactional(rollbackFor=Exception.class)
	public void abolish(String waveId,String orgId,String warehouseId,String operator) throws Exception{
		//1、波次单状态是否打开
		checkStatus(waveId,Constant.WAVE_STATUS_OPEN,BizStatus.WAVE_STATUS_IS_NOT_OPEN.getReasonPhrase());
		//2、删除所有关联的发货单
		deliveryService.delWaveId(waveId, orgId, warehouseId, operator);
		//3、波次单由打开变为取消
		updateWaveStatus(waveId,operator,Constant.WAVE_STATUS_CANCAL);
	}

	/**
	 * 检查状态
	 * @param waveId
	 * @param status
	 * @param msg
	 * @version 2017年3月1日 下午7:55:42<br/>
	 * @author Aaron He<br/>
	 */
	public void checkStatus(String waveId,int status,String msg){
		int sta = getStatusById(waveId);
		if(status != sta)
			throw new BizException(msg);
	}

	/**
	 * 获取波次单
	 * @param waveId
	 * @return
	 * @version 2017年3月1日 下午7:49:29<br/>
	 * @author Aaron He<br/>
	 */
	public Integer getStatusById(String waveId){
		SendWave wave = waveDao.selectByPrimaryKey(waveId);
		if(wave != null) return wave.getWaveStatus();
		return null;		
	}

	/**
	 * 获取波次单及发货单列表
	 * @param waveId
	 * @return
	 * @version 2017年3月1日 下午7:49:29<br/>
	 * @author Aaron He<br/>
	 */
	public SendWaveVo getSendWaveVoById(String waveId){
		SendWaveVo waveVo = new SendWaveVo();
		//查询波次单
		SendWave entity = waveDao.selectByPrimaryKey(waveId);
		//根据波次单查询发货单
		SendDelivery delivery = new SendDelivery();
		delivery.setOrgId(entity.getOrgId());
		delivery.setWarehouseId(entity.getWarehouseId());
		delivery.setWaveId(waveId);
		List<SendDeliveryVo> deliveries = deliveryService.qryList(delivery);

		FqDataUtils fdu = new FqDataUtils();
		for(SendDeliveryVo deliveryVo:deliveries){
			//查询发货方，
			deliveryVo.setSenderName(fdu.getMerchantNameById(merchantService, deliveryVo.getSendDelivery().getSender()));	
		}
		waveVo.setSendWave(entity);
		waveVo.setSendDeliberyVoList(deliveries);

		return waveVo;
	}

	/**
	 * 查询波次单实体
	 * @param waveId
	 * @return
	 */
	public SendWave getEntityById(String waveId){
		//查询波次单
		SendWave entity = waveDao.selectByPrimaryKey(waveId);
		return entity;
	}

	/**
	 * 根据单号查询
	 * @param deliveryNo
	 * @version 2017年3月7日 下午6:34:25<br/>
	 * @author Aaron He<br/>
	 */
	public List<SendWave> qryByWaveNo(String waveNo,String orgId,String warehouseId){
		SendWaveVo param = new SendWaveVo();
		param.setWaveNoLike(waveNo);
		param.getSendWave().setOrgId(orgId);
		param.getSendWave().setWarehouseId(warehouseId);
		List<SendWave> list = waveDao.selectByExample(param.getConditionExample());
		if(list == null || list.isEmpty()) return null;
		return list;
	}

	/**
	 * 根据波次单id查询发货明细
	 * @param waveId
	 * @return
	 * @throws Exception
	 */
	public List<DeliveryDetailVo> qryDeliveryDetailsByWaveId(String waveId) throws Exception{
		SendWaveVo waveVo = getSendWaveVoById(waveId);
		List<DeliveryDetailVo> resultList = new ArrayList<DeliveryDetailVo>();
		if(waveVo.getSendDeliberyVoList().isEmpty()) return null;

		for(SendDeliveryVo delVo:waveVo.getSendDeliberyVoList()){
			SendDeliveryVo vo = deliveryService.view(delVo.getSendDelivery().getDeliveryId());
			if(vo.getDeliveryDetailVoList().isEmpty()) continue;
			resultList.addAll(vo.getDeliveryDetailVoList());
		}
		return resultList;
	}

	/**
	 * 拣货确认后波次单处理
	 * 1、根据波次id查询对应的波次单
	 * 2、拣货确认后，处理对应的发货单，更新发货单的订单数量
	 * 3、统计拣货数量
	 * 4、更新波次单的订单数量
	 * 5、保存波次单
	 * @param waveId
	 * @param operator
	 * @throws Exception
	 * @version 2017年3月6日 下午2:24:06<br/>
	 * @author Aaron He<br/>
	 */
	@Transactional(rollbackFor=Exception.class)
	public SendWaveVo updateAfterConfirmPick(String waveId,String operator)throws Exception{
		//1、根据波次id查询对应的波次单
		SendWaveVo waveVo = getSendWaveVoById(waveId);
		if(waveVo.getSendDeliberyVoList() == null || waveVo.getSendDeliberyVoList().isEmpty())return null;

		//2、拣货确认后，处理对应的发货单，更新发货单的订单数量
		List<SendDeliveryVo> deVoList = new ArrayList<SendDeliveryVo>();
		for (SendDeliveryVo deliveryVo : waveVo.getSendDeliberyVoList()) {
			SendDeliveryVo vo = deliveryService.updateAfterConfirmPick(deliveryVo.getSendDelivery().getDeliveryId(), operator);
			deVoList.add(vo);
		}
		waveVo.setSendDeliberyVoList(deVoList);
		//3、统计拣货数量
		waveVo.calPickQty();
		//4、更新波次单的订单数量
		waveVo.countPickTotal();
		waveVo.getSendWave().setUpdatePerson(operator);
		waveVo.getSendWave().setUpdateTime(new Date());
		//5、保存波次单
		waveDao.updateByPrimaryKeySelective(waveVo.getSendWave());

		return waveVo;
	}

	/**
	 * 拣货确认更新波次
	 * @param sendPickVo
	 * @param userId
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void updateAfterCompletePick(SendPickVo sendPickVo,String userId) throws Exception{
		Double totalPickQty = sendPickVo.getSendPickDetailVoList()
				.stream().mapToDouble(p->p.getSendPickDetail().getPickQty()).sum();
		Double pickWeight = sendPickVo.getSendPickDetailVoList()
				.stream().mapToDouble(t->t.getSendPickDetail().getPickWeight()).sum();
		Double pickVolume = sendPickVo.getSendPickDetailVoList()
				.stream().mapToDouble(t->t.getSendPickDetail().getPickVolume()).sum();

		SendWave wave = waveDao.selectByPrimaryKey(sendPickVo.getSendPick().getWaveId());
		wave.calPickQty(totalPickQty);
		wave.calPickdWeight(pickWeight);
		wave.calPickVolume(pickVolume);
		wave.setUpdatePerson(userId);
		wave.setUpdateTime(new Date());	
		waveDao.updateByPrimaryKeySelective(wave);
	}

	/**
	 * 登记到业务统计表
	 * @param waveVo
	 * @param operator
	 * @version 2017年3月11日 下午2:02:55<br/>
	 * @author Aaron He<br/>
	 */
	public void addOrgBusiStas(SendWaveVo waveVo,String operator) throws Exception{
		if(waveVo == null) return;
		for (SendDeliveryVo delivberyVo : waveVo.getSendDeliberyVoList()) {
			deliveryService.addOrgBusiStas(delivberyVo, operator);
		}
	}

	/**
	 * 更新波次单状态
	 * @param waveId
	 * @param operator
	 * @param status
	 * @version 2017年3月2日 下午3:18:16<br/>
	 * @author Aaron He<br/>
	 */
	public void updateWaveStatus(String waveId,String operator,int status){
		SendWave wave = new SendWave();
		wave.setWaveId(waveId);
		wave.setWaveStatus(status);
		wave.setUpdatePerson(operator);
		wave.setUpdateTime(new Date());
		waveDao.updateByPrimaryKeySelective(wave);
	}

	/**
	 * 从波次单生成拣货单
	 * @throws Exception 
	 */
	public SendPickVo createPick(String id,String operator) throws Exception {
		if(StringUtil.isTrimEmpty(id)) return null;
		//查询波次单
		SendWaveVo swVo = getSendWaveVoById(id);				
		//生成拣货单
		SendPickVo pickVo = new SendPickVo();
		pickVo.createPickFromWave(swVo, operator);	

		for(SendDeliveryVo vo : swVo.getSendDeliberyVoList()) {	
			//查询发货明细
			List<DeliveryDetailVo> ddList = deliveryService.getDetailsByDeliveryId(vo.getSendDelivery().getDeliveryId());
			//创建拣货明细
			pickVo.createPickDetaiVoList(ddList,vo.getSendDelivery().getOwner(),operator);			
		}
		return pickVo;
	}


	/**
	 * 查询波次单列表
	 * @param sendWaveVo
	 * @return
	 * @version 2017年3月7日 上午9:23:00<br/>
	 * @author Aaron He<br/>
	 */
	public List<SendWaveVo> qryNotFinishList(SendWaveVo sendWaveVo){
		//查询所有由波次单发起，打开，生效的,工作中的拣货单
		SendPickVo sendPickVo = new SendPickVo();
		sendPickVo.getSendPick().setOrgId(sendWaveVo.getSendWave().getOrgId());
		sendPickVo.getSendPick().setWarehouseId(sendWaveVo.getSendWave().getWarehouseId());
		sendPickVo.setWaveIsNotNull(true);
		sendPickVo.setPickStatusList(Arrays.asList(Constant.PICK_STATUS_OPEN,Constant.PICK_STATUS_ACTIVE));
		List<SendPickVo> pickList = pickService.qryListByParam(sendPickVo);	
		List<String> wpList = null;
		if(pickList != null && !pickList.isEmpty()){
			wpList = pickList.stream().map(t->t.getSendPick().getWaveId()).collect(Collectors.toList());
		}

		//查询待拣货数量不为0的波次单
		Criteria c = sendWaveVo.defaultCondition().getExample().getOredCriteria().get(0);
		c.andCondition("order_qty - pick_qty > 0");
		c.andEqualTo("waveStatus",Constant.WAVE_STATUS_ACTIVE);
		//过滤生效发货单中已有关联生效拣货单的发货单
		if(wpList != null && !wpList.isEmpty()){
			c.andNotIn("waveId", wpList);
		}

		List<SendWave> list = waveDao.selectByExample(sendWaveVo.getExample());
		if(list == null || list.isEmpty()) return null;

		List<SendWaveVo> voList = new ArrayList<SendWaveVo>();
		for(SendWave wave:list){
			SendWaveVo vo = new SendWaveVo();
			vo.setSendWave(wave);
			voList.add(vo);
		}
		return voList;

	}

	/**
	 * 查询波次单数量
	 * @param sendWaveVo
	 * @param orgId
	 * @param warehouseId
	 * @return
	 */
	public int count(SendWaveVo sendWaveVo){
		sendWaveVo.setOrgIdAndWareHouseId();
		int num = waveDao.selectCountByExample(sendWaveVo.getConditionExample());

		return num;
	}
	/**
	 * 查询波次单数量
	 * @param sendWaveVo
	 * @param orgId
	 * @param warehouseId
	 * @return
	 */
	public int count2(SendWaveVo sendWaveVo){
		int num = waveDao.selectCountByExample(sendWaveVo.getConditionExample());

		return num;
	}
	public List<String>getTask(String orgId){
		return waveDao.getTask(orgId);
	}

	public static void main(String[] args) {
		List<SendDelivery> list = new ArrayList<SendDelivery>();
		for(int i=0;i<2;i++){
			SendDelivery entity = new SendDelivery();
			entity.setCity("abc"+i);
			list.add(entity);
		}
		for(SendDelivery record:list){
			record = null;
			System.out.println("-------");
		}
		list.stream().forEach(t->{
			System.out.println(t.getCity());
		});
		for(int i=0;i<list.size();i++){
			SendDelivery record = list.get(i);
			record = null;
			System.out.println("=========");
		}
		list.stream().forEach(t->{
			System.out.println(t.getCity());
		});
	}
	private String groupMessage(List<BizException>exceptions ){
		String exceptionall="";
		Map<String,Double>map=new HashMap<>();
		Map<String,Double>map2=new HashMap<>();
		Map<String,String>map3=new HashMap<>();
		Map<String,String>map4=new HashMap<>();
		for(BizException e:exceptions){
			String skuId=(String) e.getParam()[0];
			String bachNo=(String) e.getParam()[1];
			double ableStock= (double) e.getParam()[2];
			double batchableStock= (double) e.getParam()[3];
			double needStock= (double) e.getParam()[4];
			if(map.get(skuId+bachNo)==null){//没有新增，有则更新
				map.put(skuId+bachNo, needStock);
				map2.put(skuId+bachNo, ableStock+batchableStock);
				map3.put(skuId+bachNo,iSkuService.get(skuId).getSkuName());
				map4.put(skuId+bachNo,bachNo);
			}else{
				map.put(skuId+bachNo, map.get(skuId+bachNo)+needStock);
			}
		}
		Iterator it = map.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			Object key = entry.getKey();
			Object value = entry.getValue();
			if(StringUtil.isNotEmpty(map4.get(key))){
				exceptionall+="</br>批次号"+map4.get(key)+map3.get(key)+"需要库存"+value+",实际库存"+map2.get(key)+"</br>";
			}else{
				exceptionall+="</br>"+map3.get(key)+",需要库存"+value+",实际库存"+map2.get(key)+"</br>";
			}
			
		}
		return exceptionall;
	}
}