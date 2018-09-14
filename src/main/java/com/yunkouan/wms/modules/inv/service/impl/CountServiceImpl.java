package com.yunkouan.wms.modules.inv.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yunkouan.excel.impt.ExcelExport;
import com.yunkouan.exception.BizException;
import com.yunkouan.saas.modules.sys.entity.MetaArea;
import com.yunkouan.saas.modules.sys.entity.MetaLocation;
import com.yunkouan.saas.modules.sys.entity.SysUser;
import com.yunkouan.saas.modules.sys.service.ISysParamService;
import com.yunkouan.saas.modules.sys.service.IUserService;
import com.yunkouan.util.NumberUtil;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.common.constant.CacheName;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.common.service.BaseService;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.IdUtil;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.common.util.MSMQUtil;
import com.yunkouan.wms.modules.intf.vo.Head;
import com.yunkouan.wms.modules.intf.vo.MSMQ;
import com.yunkouan.wms.modules.inv.dao.ICountDao;
import com.yunkouan.wms.modules.inv.dao.ICountDetailDao;
import com.yunkouan.wms.modules.inv.entity.InvAdjust;
import com.yunkouan.wms.modules.inv.entity.InvAdjustDetail;
import com.yunkouan.wms.modules.inv.entity.InvCount;
import com.yunkouan.wms.modules.inv.entity.InvCountDetail;
import com.yunkouan.wms.modules.inv.entity.InvStock;
import com.yunkouan.wms.modules.inv.service.IAdjustService;
import com.yunkouan.wms.modules.inv.service.ICountService;
import com.yunkouan.wms.modules.inv.service.IStockService;
import com.yunkouan.wms.modules.inv.vo.InvAdjustDetailVO;
import com.yunkouan.wms.modules.inv.vo.InvAdjustVO;
import com.yunkouan.wms.modules.inv.vo.InvCountDetailVO;
import com.yunkouan.wms.modules.inv.vo.InvCountDetailVO4Excel;
import com.yunkouan.wms.modules.inv.vo.InvCountVO;
import com.yunkouan.wms.modules.inv.vo.InvStockVO;
import com.yunkouan.wms.modules.message.service.IMsmqMessageService;
import com.yunkouan.wms.modules.message.vo.MsmqMessageVo;
import com.yunkouan.wms.modules.meta.entity.MetaMerchant;
import com.yunkouan.wms.modules.meta.entity.MetaSku;
import com.yunkouan.wms.modules.meta.service.IAreaExtlService;
import com.yunkouan.wms.modules.meta.service.ILocationExtlService;
import com.yunkouan.wms.modules.meta.service.IMerchantService;
import com.yunkouan.wms.modules.meta.service.ISkuService;
import com.yunkouan.wms.modules.rec.entity.RecAsn;
import com.yunkouan.wms.modules.rec.service.IASNExtlService;
import com.yunkouan.wms.modules.send.util.TransmitXmlUtil;
import com.yunkouan.wms.modules.ts.service.ITaskService;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 盘点服务实现类
 */
@Service
public class CountServiceImpl extends BaseService implements ICountService {

	private static Log log = LogFactory.getLog(CountServiceImpl.class);

	/**
     * 数据层接口
     */
	@Autowired
    private ICountDao dao;
	/**
     * 详情数据层接口
     */
	@Autowired
    private ICountDetailDao detailDao;
	/**
	 * 外部服务-货商
	 */
	@Autowired
	IMerchantService merchantService;
	/**
	 * 外部服务-任务单
	 */
	@Autowired
	private ITaskService taskService;
	/**
	 * 外部服务-收货单
	 */
	@Autowired
	IASNExtlService asnService;
	/**
	 * 外部服务-通用
	 */
//	@Autowired
//	ICommonService commonService;
	/**
	 * 外部服务-库存
	 */
	@Autowired
	IStockService stockService;

	/**
	 * 外部服务-库区
	 */
	@Autowired
	IAreaExtlService areaService;
	/**
	 * 外部服务-库位
	 */
	@Autowired
	ILocationExtlService locationService;
	/**
	 * 外部服务-货品
	 */
	@Autowired
	ISkuService skuService;
	/**
	 * 外部服务-用户
	 */
	@Autowired
	IUserService userService;
	/**
	 * 外部服务-调账单
	 */
	@Autowired
	IAdjustService adjustService;
	
	@Autowired
	private IMsmqMessageService msmqMessageService;
	
	@Autowired
	private ISysParamService paramService;

	/**
	 * 根据ID获取盘点单详情
	 * @param id
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月11日 下午1:14:12<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
	@Override
	@Transactional(readOnly = false)
    public InvCountVO view(String id) throws Exception {
		// 获取当前用户信息
		Principal loginUser = LoginUtil.getLoginUser();
		if (loginUser == null) {
			throw new BizException("valid_common_user_no_login");
		}
		if ( id == null ) {
			throw new BizException("err_count_null");
		}
		// 查询盘点单详情 
		InvCount count = this.dao.selectByPrimaryKey(id);
		
		return chg(count);
    }

	private InvCountVO chg(InvCount count) throws Exception {
		if (count == null) {
			throw new BizException("err_count_null");
		}
		//组装信息
		count.setWarehouseId(LoginUtil.getWareHouseId());
		count.setOrgId(LoginUtil.getLoginUser().getOrgId());
		// 组装信息
		InvCountVO countVo = new InvCountVO(count).searchCache();
		// 查询货品信息
		if (!StringUtil.isTrimEmpty(count.getSkuId())) {
			MetaSku metaSkuCount = skuService.get(count.getSkuId());
			if (metaSkuCount != null) {
				countVo.setSkuName(metaSkuCount.getSkuName());
			}
		}
		countVo.setApplyNo(getApplyNo());
		// 查询创建人信息
		SysUser createPerson = null;
		if (!StringUtil.isTrimEmpty(count.getCreatePerson())) {
			createPerson = userService.get(count.getCreatePerson());
			if ( createPerson != null ) {
				countVo.setCreateUserName(createPerson.getUserName());
			}
		}
		// 查询修改人信息
		SysUser updatePerson = null;
		if ( StringUtil.equals(count.getCreatePerson(), count.getUpdatePerson()) ) {
			updatePerson = createPerson;
		} else {
			if (!StringUtil.isTrimEmpty(count.getCreatePerson())) {
				updatePerson = userService.get(count.getUpdatePerson());
			}
		}
		if ( updatePerson != null ) {
			countVo.setUpdateUserName(updatePerson.getUserName());
		}
		// 查询作业人信息
		SysUser opPerson = null;
		if (!StringUtil.isTrimEmpty(count.getOpPerson())) {
			if ( count.getOpPerson().equals(count.getCreatePerson()) ) {
				opPerson = createPerson;
			} else {
				opPerson = userService.get(count.getOpPerson());
			}
		}
		if ( opPerson != null ) {
			countVo.setOpPersonName(opPerson.getUserName());
		}
		Integer ret = this.detailDao.selectLocationCount(countVo);
		countVo.setLocationQty(ret);
		// 查询库位信息
		if (!StringUtil.isTrimEmpty(count.getLocationId())) {
			MetaLocation locationCount = this.locationService.findLocById(count.getLocationId());
			if (locationCount != null) {
				countVo.setLocationName(locationCount.getLocationName());
			}
		}
		//货主名称
		if (!StringUtil.isTrimEmpty(count.getOwner())) {
			MetaMerchant merchant = merchantService.get(count.getOwner());
			if (merchant != null) {
				countVo.setOwnerName(merchant.getMerchantName());
				countVo.setMerchantShortName(merchant.getMerchantShortName());
			}
		}
		//盘点状态名称
		if (count.getCountStatus() != null) {
			countVo.setStatusName(paramService.getValue(CacheName.STATUS, count.getCountStatus()));
		}
		//盘点类型名称
		if (count.getCountType() != null) {
			countVo.setCountTypeName(paramService.getValue(CacheName.COUNT_TYPE, count.getCountType()));
		}
		//冻结状态名称
		if (count.getIsBlockLocation() != null) {
			countVo.setIsFreezeName(paramService.getValue(CacheName.ISBLOCK, count.getIsBlockLocation()));
		}
		// 查询盘点单明细
		List<InvCountDetailVO> listInvCountDetailVO = new ArrayList<InvCountDetailVO>();
		List<InvCountDetail> listInvCountDetail = detailDao.selectCountDetail(count.getCountId());
		if ( listInvCountDetail != null && !listInvCountDetail.isEmpty() ) {
			for (int i = 0; i < listInvCountDetail.size(); i++) {
				InvCountDetail detail = listInvCountDetail.get(i);
				InvCountDetailVO invCountDetailVO = new InvCountDetailVO(detail).searchCache();
				// 查询货品信息
				MetaSku metaSku = skuService.get(detail.getSkuId());
				invCountDetailVO.setSkuName(metaSku.getSkuName());
				invCountDetailVO.setSkuNo(metaSku.getSkuNo());
				MetaMerchant metaMerchant = merchantService.get(metaSku.getOwner());
				invCountDetailVO.setOwnerName(metaMerchant.getMerchantShortName());
				invCountDetailVO.setSpecModel(metaSku.getSpecModel());
				invCountDetailVO.setMeasureUnit(metaSku.getMeasureUnit());
				invCountDetailVO.setSkuBarCode(metaSku.getSkuBarCode());

				Double changeQty = NumberUtil.sub(detail.getRealCountQty(),detail.getStockQty());
				invCountDetailVO.setChangeQty(changeQty);
				invCountDetailVO.setChangeWeight(NumberUtil.mul(metaSku.getPerWeight(),changeQty));
				invCountDetailVO.setChangeValue(NumberUtil.mul(metaSku.getPerPrice(),changeQty));
				// 查询库位信息
				MetaLocation location = this.locationService.findLocById(detail.getLocationId());
				invCountDetailVO.setLocationName(location.getLocationName());
				invCountDetailVO.setLocationNo(location.getLocationNo());
				MetaArea area = this.areaService.findAreaById(location.getAreaId());
				if (area != null) {
					invCountDetailVO.setAreaName(area.getAreaName());
				}
				if (!StringUtil.isTrimEmpty(detail.getAsnId())) {
					RecAsn asn = asnService.findAsnById(detail.getAsnId());
					if (asn != null) {
						invCountDetailVO.setAsnNo(asn.getAsnNo());
					}
				}
				
				listInvCountDetailVO.add(invCountDetailVO);
			}
		}
		countVo.setListInvCountDetailVO(listInvCountDetailVO);
		return countVo;
	}
    /**
     * 新增盘点单
     * @param count
     * @required 
     * @optional  
     * @Description 
     * @version 2017年3月11日 下午1:24:45<br/>
     * @author 王通<br/>
     * @throws Exception 
     */
	@Override
	@Transactional(readOnly = false)
    public InvCountVO add(InvCountVO vo) throws Exception {
    	Principal loginUser = LoginUtil.getLoginUser();
    	// 验证用户是否登录
    	if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
    		throw new BizException("valid_common_user_no_login");
    	}
		if ( vo == null || vo.getInvCount() == null ) {
    		throw new BizException("valid_common_data_empty");
    	}
		InvCountVO invCountVo = new InvCountVO();
    	InvCount count = vo.getInvCount();
    	//校验字段 必填项
    	if (count.getIsBlockLocation() == null || count.getCountType() == null) {
    		throw new BizException("valid_common_data_empty");
    	}
		// 新增方法
    	// 设置uuid
		String countId = IdUtil.getUUID();
    	count.setCountId(countId);
    	// 设置创建人/修改人/企业/仓库
    	String orgId = loginUser.getOrgId();
    	count.setCreatePerson(loginUser.getUserId());
    	count.setCreateTime(new Date());
    	count.setUpdatePerson(loginUser.getUserId());
    	count.setUpdateTime(new Date());
    	count.setOrgId(orgId);
    	count.setWarehouseId(LoginUtil.getWareHouseId());
    	// 设置盘点单单号
    	count.setCountNo(context.getStrategy4No(orgId, LoginUtil.getWareHouseId()).getCountNo(orgId));
    	// 设置状态
    	Integer countStatus = count.getCountStatus();
    	if (countStatus == null || countStatus == 0) {
        	count.setCountStatus(Constant.STATUS_OPEN);
    	}
    	count.setCountId2(context.getStrategy4Id().getCountSeq());
    	invCountVo.setInvCount(count);
    	this.dao.insertSelective(count);
    	List<InvCountDetailVO> listCountDetailVo = vo.getListInvCountDetailVO();
    	if ( listCountDetailVo != null && !listCountDetailVo.isEmpty() ) {
    		//检查是否有重复数据，抛出异常
    		for (int i = 0; i < listCountDetailVo.size(); i++) {
    			InvCountDetailVO invCountDetailVO = listCountDetailVo.get(i);
    			InvCountDetail invCountDetail = invCountDetailVO.getInvCountDetail();
    			String skuId = invCountDetail.getSkuId();
    			String locationId = invCountDetail.getLocationId();
    			String batchNo = invCountDetail.getBatchNo();
    			String asnDetailId = invCountDetail.getAsnDetailId();
    			for (int j = i+1; j < listCountDetailVo.size(); j++) {
        			InvCountDetailVO invCountDetailVO2 = listCountDetailVo.get(j);
        			InvCountDetail invCountDetail2 = invCountDetailVO2.getInvCountDetail();
        			String skuId2 = invCountDetail2.getSkuId();
        			String locationId2 = invCountDetail2.getLocationId();
        			String batchNo2 = invCountDetail2.getBatchNo();
        			String asnDetailId2 = invCountDetail2.getAsnDetailId();
        			//判断重复
        			if (StringUtil.equals(skuId, skuId2) && StringUtil.equals(locationId,locationId2) && StringUtil.equals(batchNo,batchNo2) && StringUtil.equals(asnDetailId, asnDetailId2)) {
                		throw new BizException("err_common_repeat");
        			}
    			}
    		}
    		List<InvCountDetail> listCountDetail = new ArrayList<InvCountDetail>();
    		//增加盘点单明细
    		for (int i = 0; i < listCountDetailVo.size(); i++) {
    			//校验
    			InvCountDetailVO invCountDetailVO = listCountDetailVo.get(i);
    			InvCountDetail invCountDetail = invCountDetailVO.getInvCountDetail();
    			if (invCountDetail == null) {
    				throw new BizException("err_count_detail_null");
    			}
    			String skuId = invCountDetail.getSkuId();
    			String localtionId = invCountDetail.getLocationId();
    			//验证货品id和库位id是否存在
    			MetaLocation location = this.locationService.findLocById(localtionId);
    			MetaSku skuRet = this.skuService.get(skuId);
    			if (skuRet == null) {
    				throw new BizException("err_count_sku_null");
    			}
    			if (location == null) {
    				throw new BizException("err_count_location_null");
    			}
    			Double qty = invCountDetail.getStockQty();
    			if (qty == null || qty == 0) {
    				throw new BizException("err_count_qty_null");
    			}
    			//设置主单号
    			invCountDetail.setCountId(countId);
    			//设置自身唯一单号
    			invCountDetail.setCountDetailId(IdUtil.getUUID());
    			//其他设置
    			invCountDetail.setOrgId(loginUser.getOrgId());
    			invCountDetail.setWarehouseId(LoginUtil.getWareHouseId());
    			invCountDetail.setCreatePerson(loginUser.getUserId());
    			invCountDetail.setCreateTime(new Date());
    			invCountDetail.setUpdatePerson(loginUser.getUserId());
    			invCountDetail.setUpdateTime(new Date());
    			invCountDetail.setCountDetailId2(context.getStrategy4Id().getCountDetailSeq());
    			listCountDetail.add(invCountDetail);
    		}
        	this.detailDao.add(listCountDetail);
        	invCountVo.setListInvCountDetailVO(listCountDetailVo);
		}
		return invCountVo;
    }

    /**
     * 盘点单生效
     * @param id
     * @required 
     * @optional  
     * @Description 逐个锁库位
     * @version 2017年3月11日 下午1:24:47<br/>
     * @author 王通<br/>
     * @throws Exception 
     */
	@Override
	@Transactional(readOnly = false)
    public void enable(InvCountVO vo) throws Exception {
		Principal loginUser = LoginUtil.getLoginUser();
    	// 验证用户是否登录
    	if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
    		throw new BizException("valid_common_user_no_login");
    	}
		if (vo == null || vo.getCountIdList() == null || vo.getCountIdList().isEmpty()) {
			throw new BizException("err_count_null");
		}
		//检查是否指派作业人员
//		if(StringUtil.isTrimEmpty(vo.getTsTaskVo().getTsTask().getOpPerson())){
//			throw new BizException(BizStatus.TASK_OPPERSON_IS_NULL.getReasonPhrase());
//		}
		List<String> countIdList = vo.getCountIdList();
    	//获取登录信息中的企业和仓库
		for (String countId : countIdList) {
			InvCountVO countVo = this.view(countId);
			if (countVo == null || countVo.getInvCount() == null) {
				throw new BizException("err_count_null");
			}
			InvCount count = countVo.getInvCount();
			if (count.getCountStatus() != Constant.STATUS_OPEN) {
				throw new BizException("err_count_status_not_open");
			}
			List<InvCountDetailVO> countDetailVoList = countVo.getListInvCountDetailVO();
			if (countDetailVoList == null || countDetailVoList.isEmpty()) {
				throw new BizException("err_count_detail_null");
			}
			//盘点是否需要冻结库位
			Integer block = count.getIsBlockLocation();
			if (block != null && block == Constant.COUNT_FREEZE) {
				for (InvCountDetailVO countDetailVo : countDetailVoList) {
					String locationId = countDetailVo.getInvCountDetail().getLocationId();
	    			//冻结库位
	    			this.locationService.blockLoc(locationId, block);
				}
			}
			InvCount countRecord = new InvCount();
			countRecord.setCountId(countId);
			countRecord.setCountStatus(Constant.STATUS_ACTIVE);
			this.dao.updateByPrimaryKeySelective(countRecord);
			//新建任务
//			taskService.create(vo.getTsTaskVo().getTsTask().getOpPerson(),Constant.TASK_TYPE_COUNT,countId);
			taskService.create(Constant.TASK_TYPE_COUNT,countId);
		}
    }

    /**
     * 
     * @param id
     * @required 
     * @optional  
     * @Description 
     * @version 2017年3月11日 下午1:24:53<br/>
     * @author 王通<br/>
     */
	@Override
	@Transactional(readOnly = false)
    public void cancel(List<String> countIdList)  throws Exception {
		Principal loginUser = LoginUtil.getLoginUser();
    	// 验证用户是否登录
    	if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
    		throw new BizException("valid_common_user_no_login");
    	}
		if (countIdList == null || countIdList.isEmpty()) {
			throw new BizException("err_count_null");
		}
    	//获取登录信息中的企业和仓库
		for (String countId : countIdList) {
			InvCountVO countVo = this.view(countId);
			if (countVo == null) {
				throw new BizException("err_count_null");
			}
			InvCount count = countVo.getInvCount();
			if (count.getCountStatus() != Constant.STATUS_OPEN) {
				throw new BizException("err_count_status_not_open");
			}
			InvCount countRecord = new InvCount();
			countRecord.setCountId(countId);
			countRecord.setCountStatus(Constant.STATUS_CANCEL);
			this.dao.updateByPrimaryKeySelective(countRecord);
		}
    }

	/**
	 * @param count
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月11日 下午1:27:08<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
	@Override
	@Transactional(readOnly = false)
	public InvCountVO update(InvCountVO vo) throws Exception {
    	Principal loginUser = LoginUtil.getLoginUser();
    	// 验证用户是否登录
    	if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
    		throw new BizException("valid_common_user_no_login");
    	}
		if ( vo == null || vo.getInvCount() == null ) {
    		throw new BizException("err_count_null");
    	}
		InvCountVO invCountVo = new InvCountVO();
    	InvCount count = vo.getInvCount();
    	String countId = count.getCountId();
		if (StringUtil.isEmpty(countId)) {
			throw new BizException("err_count_null");
		}
		// 修改方法
		// 检查盘点单是否存在
		InvCount findCount = this.dao.selectByPrimaryKey(countId);
		if (findCount == null || StringUtil.isEmpty(findCount.getCountId())) {
			throw new BizException("err_count_null");
		} else if (!findCount.getCountStatus().equals(Constant.STATUS_OPEN)) {
			throw new BizException("err_count_status_not_open");
		}
		
		// 设置修改人/企业/仓库
    	count.setCountStatus(Constant.STATUS_OPEN);
    	count.setUpdatePerson(loginUser.getUserId());
    	count.setUpdateTime(new Date());
    	invCountVo.setInvCount(count);
    	this.dao.updateByPrimaryKeySelective(count);
    	List<InvCountDetailVO> listCountDetailVo = vo.getListInvCountDetailVO();
    	if ( listCountDetailVo != null && !listCountDetailVo.isEmpty() ) {
    		//检查是否有重复数据，并抛出异常
    		for (int i = 0; i < listCountDetailVo.size(); i++) {
    			InvCountDetailVO invCountDetailVO = listCountDetailVo.get(i);
    			InvCountDetail invCountDetail = invCountDetailVO.getInvCountDetail();
    			String skuId = invCountDetail.getSkuId();
    			String locationId = invCountDetail.getLocationId();
    			String batchNo = invCountDetail.getBatchNo();
    			String asnDetailId = invCountDetail.getAsnDetailId();
    			for (int j = i+1; j < listCountDetailVo.size(); j++) {
        			InvCountDetailVO invCountDetailVO2 = listCountDetailVo.get(j);
        			InvCountDetail invCountDetail2 = invCountDetailVO2.getInvCountDetail();
        			String skuId2 = invCountDetail2.getSkuId();
        			String locationId2 = invCountDetail2.getLocationId();
        			String batchNo2 = invCountDetail2.getBatchNo();
        			String asnDetailId2 = invCountDetail2.getAsnDetailId();
        			//判断重复
        			if (StringUtil.equals(skuId, skuId2) && StringUtil.equals(locationId,locationId2) && StringUtil.equals(batchNo,batchNo2) && StringUtil.equals(asnDetailId, asnDetailId2)) {
//        				listCountDetailVo.remove(j);
//        				j--;
        				throw new BizException("err_common_repeat");
        			}
    			}
    		}
    		List<InvCountDetail> listCountDetail = new ArrayList<InvCountDetail>();
    		//清空盘点单明细
    		InvCountDetail delCountDetail = new InvCountDetail();
    		delCountDetail.setCountId(countId);
    		this.detailDao.delete(delCountDetail);
    		//添加盘点单明细
    		for (int i = 0; i < listCountDetailVo.size(); i++) {
    			InvCountDetailVO invCountDetailVO = listCountDetailVo.get(i);
    			InvCountDetail invCountDetail = invCountDetailVO.getInvCountDetail();
    			String locationId = invCountDetail.getLocationId();
    			String skuId = invCountDetail.getSkuId();
    			//验证货品id和库位id是否存在
    			MetaLocation location = this.locationService.findLocById(locationId);
    			MetaSku skuRet = this.skuService.get(skuId);
    			if (skuRet == null) {
    				throw new BizException("err_count_sku_null");
    			}
    			if (location == null) {
    				throw new BizException("err_count_location_null");
    			}
    			//设置主单号
    			invCountDetail.setCountId(countId);
    			//设置自身唯一单号
    			invCountDetail.setCountDetailId(IdUtil.getUUID());
    			//其他设置
    			invCountDetail.setOrgId(loginUser.getOrgId());
    			invCountDetail.setWarehouseId(LoginUtil.getWareHouseId());
    			invCountDetail.setCreatePerson(loginUser.getUserId());
    			invCountDetail.setCreateTime(new Date());
    			invCountDetail.setUpdatePerson(loginUser.getUserId());
    			invCountDetail.setUpdateTime(new Date());
    			listCountDetail.add(invCountDetail);
    		}
        	this.detailDao.add(listCountDetail);
        	invCountVo.setListInvCountDetailVO(listCountDetailVo);
		}
		return invCountVo ;
	}


	/**
	 * @param countVO
	 * @return
	 * @throws Exception
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月14日 下午3:38:02<br/>
	 * @author 王通<br/>
	 */
	@Override
	@Transactional(readOnly = false)
	public Page<InvCountVO> list(InvCountVO vo) throws Exception {
		// 获取当前用户信息
		Principal loginUser = LoginUtil.getLoginUser();
		if (loginUser == null) {
			throw new BizException("valid_common_user_no_login");
		}
		if ( vo == null ) {
    		vo = new InvCountVO();
		}
		if ( vo.getInvCount() == null ) {
			vo.setInvCount(new InvCount());
		}
		Page<InvCountVO> page = null;
		//组装信息
		vo.getInvCount().setWarehouseId(LoginUtil.getWareHouseId());
		vo.getInvCount().setOrgId(loginUser.getOrgId());
		// 查询盘点单列表 
		page = PageHelper.startPage(vo.getCurrentPage()+1, vo.getPageSize());
		if (!StringUtil.isTrimEmpty(vo.getOwnerName())) {
			vo.setOwnerName(StringUtil.likeEscapeH(vo.getOwnerName()));
		}
		List<InvCount> listInvCount = this.dao.list(vo);
		if ( listInvCount == null || listInvCount.isEmpty() ) {
			return null;
		}
		// 组装信息
		List<InvCountVO> listVO = new ArrayList<InvCountVO>();
		for (int i = 0; i < listInvCount.size(); i++) {
			InvCount count = listInvCount.get(i);
			if ( count == null ) {
				continue;
			}
			InvCountVO countVo = new InvCountVO(count).searchCache();
//			InvCountVO countVo = chg(count);
			listVO.add(countVo);
			// 统计库位数
//			if (count.getLocationId() != null) {
//				countVo.setLocationQty(1);
//			} else {
				Integer ret = this.detailDao.selectLocationCount(countVo);
				countVo.setLocationQty(ret);
//			}
			// 查询创建人信息
			SysUser createPerson = null;
			if (!StringUtil.isTrimEmpty(count.getCreatePerson())) {
				createPerson = userService.get(count.getCreatePerson());
				if ( createPerson != null ) {
					countVo.setCreateUserName(createPerson.getUserName());
				}
			}
			// 查询修改人信息
			SysUser updatePerson = null;
			if ( StringUtil.equals(count.getCreatePerson(), count.getUpdatePerson()) ) {
				updatePerson = createPerson;
			} else {
				if (!StringUtil.isTrimEmpty(count.getCreatePerson())) {
					updatePerson = userService.get(count.getUpdatePerson());
				}
			}
			if ( updatePerson != null ) {
				countVo.setUpdateUserName(updatePerson.getUserName());
			}
			// 查询作业人信息
			SysUser opPerson = null;
			if (!StringUtil.isTrimEmpty(count.getOpPerson())) {
				if ( count.getOpPerson().equals(count.getCreatePerson()) ) {
					opPerson = createPerson;
				} else {
					opPerson = userService.get(count.getOpPerson());
				}
			}
			if ( opPerson != null ) {
				countVo.setOpPersonName(opPerson.getUserName());
			}
			//盘点状态名称
			if (count.getCountStatus() != null) {
				countVo.setStatusName(paramService.getValue(CacheName.STATUS, count.getCountStatus()));
			}
			//盘点类型名称
			if (count.getCountType() != null) {
				countVo.setCountTypeName(paramService.getValue(CacheName.COUNT_TYPE, count.getCountType()));
			}
			//冻结状态名称
			if (count.getIsBlockLocation() != null) {
				countVo.setIsFreezeName(paramService.getValue(CacheName.ISBLOCK, count.getIsBlockLocation()));
			}
			if (count.getResult() != null) {
				if (count.getResult() == 0) {
					countVo.setResultName("正常");
				}
				if (count.getResult() == 1) {
					countVo.setResultName("异常");
				}
//				countVo.setResultName(paramService.getValue(CacheName.COUNT_RESULT, count.getResult()));
			}
			//货主名称
			if (!StringUtil.isTrimEmpty(count.getOwner())) {
				MetaMerchant merchant = merchantService.get(count.getOwner());
				if (merchant != null) {
					countVo.setOwnerName(merchant.getMerchantName());
					countVo.setMerchantShortName(merchant.getMerchantShortName());
				}
			}
			
		}
		page.clear();
		page.addAll(listVO);
		return page;
	}
	/**
	 * @param countVO
	 * @return
	 * @throws Exception
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月14日 下午3:38:02<br/>
	 * @author 王通<br/>
	 */
	@Override
	public Page<InvCountVO> list4Print(InvCountVO vo) throws Exception {
		// 获取当前用户信息
		Principal loginUser = LoginUtil.getLoginUser();
		if (loginUser == null) {
			throw new BizException("valid_common_user_no_login");
		}
		if ( vo == null ) {
    		vo = new InvCountVO();
		}
		if ( vo.getInvCount() == null ) {
			vo.setInvCount(new InvCount());
		}
		Page<InvCountVO> page = null;
		//组装信息
		vo.getInvCount().setWarehouseId(LoginUtil.getWareHouseId());
		vo.getInvCount().setOrgId(loginUser.getOrgId());
		// 查询盘点单列表 
		page = PageHelper.startPage(vo.getCurrentPage()+1, vo.getPageSize());
		if (!StringUtil.isTrimEmpty(vo.getOwnerName())) {
			vo.setOwnerName(StringUtil.likeEscapeH(vo.getOwnerName()));
		}
		List<InvCount> listInvCount = this.dao.list(vo);
		if ( listInvCount == null || listInvCount.isEmpty() ) {
			return null;
		}
		// 组装信息
		List<InvCountVO> listVO = new ArrayList<InvCountVO>();
		for (int i = 0; i < listInvCount.size(); i++) {
			InvCount count = listInvCount.get(i);
			if ( count == null ) {
				continue;
			}
			InvCountVO countVo = new InvCountVO(count).searchCache();
			listVO.add(countVo);
			// 统计库位数
			if (count.getLocationId() != null) {
				countVo.setLocationQty(1);
			} else {
				Integer ret = this.detailDao.selectLocationCount(countVo);
				countVo.setLocationQty(ret);
			}
			// 查询创建人信息
			SysUser createPerson = null;
			if (!StringUtil.isTrimEmpty(count.getCreatePerson())) {
				createPerson = userService.get(count.getCreatePerson());
				if ( createPerson != null ) {
					countVo.setCreateUserName(createPerson.getUserName());
				}
			}
			// 查询修改人信息
			SysUser updatePerson = null;
			if ( StringUtil.equals(count.getCreatePerson(), count.getUpdatePerson()) ) {
				updatePerson = createPerson;
			} else {
				if (!StringUtil.isTrimEmpty(count.getCreatePerson())) {
					updatePerson = userService.get(count.getUpdatePerson());
				}
			}
			if ( updatePerson != null ) {
				countVo.setUpdateUserName(updatePerson.getUserName());
			}
			// 查询作业人信息
			SysUser opPerson = null;
			if (!StringUtil.isTrimEmpty(count.getOpPerson())) {
				if ( count.getOpPerson().equals(count.getCreatePerson()) ) {
					opPerson = createPerson;
				} else {
					opPerson = userService.get(count.getOpPerson());
				}
			}
			if ( opPerson != null ) {
				countVo.setOpPersonName(opPerson.getUserName());
			}
			//盘点状态名称
			if (count.getCountStatus() != null) {
				countVo.setStatusName(paramService.getValue(CacheName.STATUS, count.getCountStatus()));
			}
			//盘点类型名称
			if (count.getCountType() != null) {
				countVo.setCountTypeName(paramService.getValue(CacheName.COUNT_TYPE, count.getCountType()));
			}
			//冻结状态名称
			if (count.getIsBlockLocation() != null) {
				countVo.setIsFreezeName(paramService.getValue(CacheName.ISBLOCK, count.getIsBlockLocation()));
			}
			if (count.getResult() != null) {
				if (count.getResult() == 0) {
					countVo.setResultName("正常");
				}
				if (count.getResult() == 1) {
					countVo.setResultName("异常");
				}
//				countVo.setResultName(paramService.getValue(CacheName.COUNT_RESULT, count.getResult()));
			}
			//货主名称
			if (!StringUtil.isTrimEmpty(count.getOwner())) {
				MetaMerchant merchant = merchantService.get(count.getOwner());
				if (merchant != null) {
					countVo.setOwnerName(merchant.getMerchantName());
					countVo.setMerchantShortName(merchant.getMerchantShortName());
				}
			}
			
			// 查询盘点单明细
			List<InvCountDetailVO> listInvCountDetailVO = new ArrayList<InvCountDetailVO>();
			List<InvCountDetail> listInvCountDetail = detailDao.selectCountDetail(count.getCountId());
			if ( listInvCountDetail != null && !listInvCountDetail.isEmpty() ) {
				for (int j = 0; j < listInvCountDetail.size(); j++) {
					InvCountDetail detail = listInvCountDetail.get(j);
					InvCountDetailVO invCountDetailVO = new InvCountDetailVO(detail).searchCache();
					// 查询货品信息
					MetaSku metaSku = skuService.get(detail.getSkuId());
					invCountDetailVO.setSkuName(metaSku.getSkuName());
					invCountDetailVO.setSkuNo(metaSku.getSkuNo());
					invCountDetailVO.setSpecModel(metaSku.getSpecModel());
					invCountDetailVO.setMeasureUnit(metaSku.getMeasureUnit());
					invCountDetailVO.setSkuBarCode(metaSku.getSkuBarCode());
					MetaMerchant merchant_s = merchantService.get(metaSku.getOwner());
					invCountDetailVO.setOwnerName(merchant_s.getMerchantName());
					// 查询库位信息
					MetaLocation location = this.locationService.findLocById(detail.getLocationId());
					invCountDetailVO.setLocationName(location.getLocationName());
					invCountDetailVO.setLocationNo(location.getLocationNo());
					MetaArea area = this.areaService.findAreaById(location.getAreaId());
					if (area != null) {
						invCountDetailVO.setAreaName(area.getAreaName());
					}
					if (!StringUtil.isTrimEmpty(detail.getAsnId())) {
						RecAsn asn = asnService.findAsnById(detail.getAsnId());
						if (asn != null) {
							invCountDetailVO.setAsnNo(asn.getAsnNo());
						}
					}
					listInvCountDetailVO.add(invCountDetailVO);
				}
			}
			countVo.setListInvCountDetailVO(listInvCountDetailVO);
		}
		page.clear();
		page.addAll(listVO);
		return page;
	}
	/**
	 * @param countIdList
	 * @throws Exception
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月14日 下午3:38:02<br/>
	 * @author 王通<br/>
	 */
	@Override
	@Transactional(readOnly = false)
	public void disable(InvCountVO vo) throws Exception {
		Principal loginUser = LoginUtil.getLoginUser();
    	// 验证用户是否登录
    	if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
    		throw new BizException("valid_common_user_no_login");
    	}
		if (vo == null || vo.getCountIdList() == null || vo.getCountIdList().isEmpty()) {
			throw new BizException("err_count_null");
		}
		List<String> countIdList = vo.getCountIdList();
    	//获取登录信息中的企业和仓库
		for (String countId : countIdList) {
			InvCountVO countVo = this.view(countId);
			if (countVo == null || countVo.getInvCount() == null) {
				throw new BizException("err_count_null");
			}
			InvCount count = countVo.getInvCount();
			if (count.getCountStatus() != Constant.STATUS_ACTIVE) {
				throw new BizException("err_count_status_active");
			}
			List<InvCountDetailVO> countDetailVoList = countVo.getListInvCountDetailVO();
			if (countDetailVoList == null || countDetailVoList.isEmpty()) {
				throw new BizException("err_count_detail_null");
			}
			//盘点是否需要冻结库位
			Integer block = count.getIsBlockLocation();
			if (block != null && block == Constant.COUNT_FREEZE) {
				for (InvCountDetailVO countDetailVo : countDetailVoList) {
					String locationId = countDetailVo.getInvCountDetail().getLocationId();
	    			//解冻库位
	    			this.locationService.blockLoc(locationId, Constant.COUNT_NO_FREEZE);
				}
			}
			InvCount countRecord = new InvCount();
			countRecord.setCountId(countId);
			countRecord.setCountStatus(Constant.STATUS_OPEN);
			this.dao.updateByPrimaryKeySelective(countRecord);
			//取消任务
			taskService.cancelByOpId(countId, loginUser.getUserId());
		}
	}

	/**
	 * @param countVo
	 * @throws Exception
	 * @required 
	 * @optional  
	 * @Description 
	 * 1、验证是否登录
	 * 2、判断单据状态
	 * 3、判断重复数据
	 * 4、盘点数量异常时新增调账单
	 * 5、全部确认完成时
	 * @version 2017年3月14日 下午3:38:02<br/>
	 * @author 王通<br/>
	 */
	@Override
	@Transactional(readOnly = false)
	public void confirm(InvCountVO countVo) throws Exception {
		Principal loginUser = LoginUtil.getLoginUser();
    	// 验证用户是否登录
    	if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
    		throw new BizException("valid_common_user_no_login");
    	}
		if (countVo == null) {
			throw new BizException("err_count_null");
		}
		InvCount count = countVo.getInvCount();
		if (count == null) {
			throw new BizException("err_count_null");
		}
		String countId = count.getCountId();
		if (countId == null) {
			throw new BizException("err_count_null");
		}
    	//判断单据状态
    	count = this.dao.selectByPrimaryKey(countId);
		if (count == null) {
			throw new BizException("err_count_null");
		}
		if (count.getCountStatus() != Constant.STATUS_ACTIVE  && count.getCountStatus() != Constant.STATUS_WORKING) {
			throw new BizException("err_count_status_cannot_finish");
		}
		Integer isFreezeLocation = countVo.getInvCount().getIsBlockLocation();
		List<InvCountDetailVO> countDetailVoList = countVo.getListInvCountDetailVO();
		if (countDetailVoList != null && !countDetailVoList.isEmpty()) {
			//生成调账单主记录
			InvAdjustVO adjustVo = new InvAdjustVO();
			InvAdjust adjust = new InvAdjust();
			if (count.getCountType() == Constant.COUNT_TYPE_BAD) {
				adjust.setDataFrom(Constant.ADJUST_DATE_FROM_BAD_COUNT);
			} else {
				adjust.setDataFrom(Constant.ADJUST_DATE_FROM_COUNT);
			}
			adjustVo.setInvAdjust(adjust);
			List<InvAdjustDetailVO> adjDetailVoList = new ArrayList<InvAdjustDetailVO>();
			adjustVo.setAdjDetailVoList(adjDetailVoList);
    		for (int i = 0; i < countDetailVoList.size(); i++) {
    			InvCountDetailVO invCountDetailVO = countDetailVoList.get(i);
    			InvCountDetail invCountDetail = invCountDetailVO.getInvCountDetail();
    			if (invCountDetail.getResult() != null) {
    				//有盘点结果的记录，不进行再次盘点
    				continue;
    			}
    			String skuId = invCountDetail.getSkuId();
    			String locationId = invCountDetail.getLocationId();
    			String batchNo = invCountDetail.getBatchNo();
    			String asnDetailId = invCountDetail.getAsnDetailId();
    			//检查是否有重复数据，抛出异常
    			for (int j = i+1; j < countDetailVoList.size(); j++) {
        			InvCountDetailVO invCountDetailVO2 = countDetailVoList.get(j);
        			InvCountDetail invCountDetail2 = invCountDetailVO2.getInvCountDetail();
        			String skuId2 = invCountDetail2.getSkuId();
        			String locationId2 = invCountDetail2.getLocationId();
        			String batchNo2 = invCountDetail2.getBatchNo();
        			String asnDetailId2 = invCountDetail2.getAsnDetailId();
        			//判断重复
        			if (StringUtil.equals(skuId, skuId2) && StringUtil.equals(locationId,locationId2) && StringUtil.equals(batchNo,batchNo2) && StringUtil.equals(asnDetailId, asnDetailId2)) {
                		throw new BizException("err_common_repeat");
        			}
    			}
    			//开始进行盘点确认
				InvCountDetail countDetail = invCountDetailVO.getInvCountDetail();
				String countDetailId = countDetail.getCountDetailId();
				Double countQty = countDetail.getRealCountQty();
				Double stockQty = countDetail.getStockQty();
				//校验字段
				if (stockQty == null || countQty == null) {
					throw new BizException("err_count_detail_qty_null");
				}
				//判断是否有明细id，有明细id时为确认盘点，无明细id时需新增明细
				if (StringUtil.isTrimEmpty(countDetailId)) {
					//新增明细,额外校验字段
					//验证货品id和库位id是否存在
	    			MetaLocation location = this.locationService.findLocById(locationId);
	    			MetaSku skuRet = this.skuService.get(skuId);
	    			if (skuRet == null) {
	    				throw new BizException("err_meta_sku_null");
	    			}
	    			if (location == null) {
	    				throw new BizException("err_main_location_null");
	    			}
					countDetail.setCountDetailId(IdUtil.getUUID());
					countDetail.setCountId(countId);
					countDetail.setResult(countQty.doubleValue() == stockQty.doubleValue()? 0 : 1);
					//组装用户信息
					countDetail.setWarehouseId(LoginUtil.getWareHouseId());
					countDetail.setOrgId(loginUser.getOrgId());
					countDetail.setCreatePerson(loginUser.getUserId());
					countDetail.setCreateTime(new Date());
					countDetail.setUpdatePerson(loginUser.getUserId());
					countDetail.setUpdateTime(new Date());
					this.detailDao.insertSelective(countDetail);
				} else{
					//明细确认,更新实际盘点数量
					InvCountDetail record = new InvCountDetail();
					record.setCountDetailId(countDetailId);
					record.setRealCountQty(countQty);
					record.setResult(countQty.doubleValue() == stockQty.doubleValue()? 0 : 1);
					record.setUpdatePerson(loginUser.getUserId());
					record.setUpdateTime(new Date());
					this.detailDao.updateByPrimaryKeySelective(record);
				}
				if (countQty.doubleValue() != stockQty.doubleValue()) {
					//生成调账单明细记录
					InvAdjustDetailVO adjDetailVo = new InvAdjustDetailVO();
					adjDetailVoList.add(adjDetailVo);
					InvAdjustDetail adjDetail = new InvAdjustDetail();
					adjDetailVo.setInvAdjustDetail(adjDetail);
					adjDetail.setAdjustType(countQty > stockQty ? Constant.ADJUST_TYPE_IN : Constant.ADJUST_TYPE_OUT);
					adjDetail.setStockQty(stockQty);
					adjDetail.setDifferenceQty(countQty - stockQty);
					adjDetail.setRealQty(countQty);
					adjDetail.setSkuId(countDetail.getSkuId());
					adjDetail.setLocationId(countDetail.getLocationId());
					adjDetail.setBatchNo(batchNo);
					adjDetail.setAsnDetailId(asnDetailId);
					adjust.setCountId(countId);
					adjust.setAdjustStatus(Constant.STATUS_OPEN);
				}
				//解锁库位
				if (isFreezeLocation.intValue() == Constant.COUNT_FREEZE) {
					this.locationService.blockLoc(countDetail.getLocationId(), Constant.COUNT_NO_FREEZE);
				}
			}
    		//确认盘点异常的情况下生成调账单
			if (!adjDetailVoList.isEmpty()) {
				this.adjustService.add(adjustVo);
			}
		} else {
			throw new BizException("err_count_detail_null");
		}
		InvCount countRecord = new InvCount();
		countRecord.setCountId(countId);
		countRecord.setOpPerson(count.getOpPerson());
		countRecord.setOpTime(new Date());
		//增加不良品调整单相关字段
		countRecord.setHgNo(countVo.getInvCount().getHgNo());
		countRecord.setApplyTime(countVo.getInvCount().getApplyTime());
		countRecord.setApplyPerson(countVo.getInvCount().getApplyPerson());
		countRecord.setApplyWarehouse(countVo.getInvCount().getApplyWarehouse());
		//判断是否盘点完毕
		Example example = new Example(InvCountDetail.class);
		Criteria c = example.createCriteria();
		c.andEqualTo("countId", countId);
		List<InvCountDetail> ret = this.detailDao.selectByExample(example);
		if (ret != null) {
			//是否完成
			boolean finish = true;
			//是否有异常
			boolean confirmNormal = true;
			for (InvCountDetail countDetail : ret) {
				if (countDetail.getResult() == null) {
					finish = false;
					break;
				} else if (countDetail.getResult() == 1) {
					confirmNormal = false;
				}
			}
			if (finish) {
				//全部盘点完毕，修改盘点状态为盘点完成
				countRecord.setCountStatus(Constant.STATUS_FINISH);
				if (confirmNormal) {
					countRecord.setResult(Constant.COUNT_RESULT_NORMAL);
				} else {
					countRecord.setResult(Constant.COUNT_RESULT_ABNORMAL);
				}
				//完成任务单
				this.taskService.finishByOpId(countId, loginUser.getUserId());
			}
			countRecord.setOpPerson(loginUser.getUserId());
			countRecord.setOpTime(new Date());
		}
		this.dao.updateByPrimaryKeySelective(countRecord);
		//完成任务单
	}

	/**
	 * @param id
	 * @throws Exception
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月14日 下午3:38:02<br/>
	 * @author 王通<br/>
	 */
	@Override
	@Transactional(readOnly = false)
	public void print(String id) throws Exception {
		Principal loginUser = LoginUtil.getLoginUser();
    	// 验证用户是否登录
    	if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
    		throw new BizException("valid_common_user_no_login");
    	}
		InvCountVO countVo = this.view(id);
		if (countVo == null) {
			throw new BizException("err_count_null");
		}
		InvCount count = countVo.getInvCount();
		if (count.getCountStatus() != Constant.STATUS_ACTIVE) {
			throw new BizException("err_count_status_active");
		}
		InvCount countRecord = new InvCount();
		countRecord.setCountId(id);
		countRecord.setCountStatus(Constant.STATUS_WORKING);
		this.dao.updateByPrimaryKeySelective(countRecord);
		
	}

	/**
	 * @param vo
	 * @return
	 * @throws Exception
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年4月19日 下午1:15:59<br/>
	 * @author 王通<br/>
	 */
	@Override
	@Transactional(readOnly = false)
	public InvCountVO saveAndEnable(InvCountVO vo) throws Exception {
		String countId = vo.getInvCount().getCountId();
    	InvCountVO countVo = null;
    	if (StringUtil.isTrimEmpty(countId)) {
            countVo = this.add(vo);
            countId = countVo.getInvCount().getCountId();
    	} else {
            countVo = this.update(vo);
    	}
        List<String> idList = new ArrayList<String>();
        idList.add(countId);
        vo.setCountIdList(idList);
        this.enable(vo);
        return countVo;
	}

	/**
	 * @param id
	 * @throws Exception
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月14日 下午3:38:02<br/>
	 * @author 王通<br/>
	 */
	@Override
	@Transactional(readOnly = false)
	public Integer countByExample(InvCountVO vo) throws Exception {
		Principal loginUser = LoginUtil.getLoginUser();
    	// 验证用户是否登录
    	if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
    		throw new BizException("valid_common_user_no_login");
    	}
    	vo.getInvCount().setWarehouseId(LoginUtil.getWareHouseId());
    	vo.getInvCount().setOrgId(loginUser.getOrgId());
    	return dao.selectCountByExample(vo.getExample());
	}
	@Override
	@Transactional(readOnly = true)
	public Integer countByExample2(InvCountVO vo) throws Exception {
    	return dao.selectCountByExample(vo.getExample());
	}
	
	public List<String>getTask(String orgId){
		return dao.getTask(orgId);
	}
	/**
	 * 快速作业确认，用于手持终端
	 * @param countVo
	 * @throws Exception
	 * @required 
	 * @optional  
	 * @Description 
	 * 1、校验库存，无库存报异常
	 * 2、新增盘点单，状态为完成
	 * 3、如有变动，生成调账单
	 * @version 2017年7月20日 上午11:15:48<br/>
	 * @author 王通<br/>
	 */
	@Override
	@Transactional(readOnly = false)
	public void quickConfirm(InvCountVO countVo) throws Exception {
		Principal loginUser = LoginUtil.getLoginUser();
    	// 验证用户是否登录
    	if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
    		throw new BizException("valid_common_user_no_login");
    	}
		//校验参数不为空
		if ( countVo == null || countVo.getListInvCountDetailVO() == null ) {
    		throw new BizException("valid_common_data_empty");
    	}
		//--新增盘点单，状态为完成
    	InvCount count = new InvCount();
    	//校验字段 必填项
    	count.setIsBlockLocation(0);
    	count.setCountType(Constant.COUNT_TYPE_ALL);
    	// 设置uuid
		String countId = IdUtil.getUUID();
    	count.setCountId(countId);
    	// 设置创建人/修改人/企业/仓库
    	String orgId = loginUser.getOrgId();
    	String warehouseId = LoginUtil.getWareHouseId();
    	Date now = new Date();
    	count.setCreatePerson(loginUser.getUserId());
    	count.setCreateTime(now);
    	count.setUpdatePerson(loginUser.getUserId());
    	count.setUpdateTime(now);
    	count.setOrgId(orgId);
    	count.setWarehouseId(warehouseId);
    	count.setOpPerson(loginUser.getUserId());
    	count.setOpTime(now);
    	// 设置盘点单单号
    	count.setCountNo(context.getStrategy4No(orgId, LoginUtil.getWareHouseId()).getCountNo(orgId));
    	// 设置状态
    	count.setCountStatus(Constant.STATUS_FINISH);
		count.setResult(0);
		//--新增调账单，状态为待调账
    	//生成调账单主记录
		InvAdjustVO adjustVo = new InvAdjustVO();
		InvAdjust adjust = new InvAdjust();
		adjust.setDataFrom(Constant.ADJUST_DATE_FROM_COUNT);
		adjust.setAdjustStatus(Constant.STATUS_OPEN);
		adjustVo.setInvAdjust(adjust);
		List<InvAdjustDetailVO> adjDetailVoList = new ArrayList<InvAdjustDetailVO>();
		adjustVo.setAdjDetailVoList(adjDetailVoList);
		List<InvCountDetailVO>  countDetailVoList = countVo.getListInvCountDetailVO();
		//--明细部分循环
		for (int i = 0; i < countDetailVoList.size(); i++) {
			//校验必填项（库位号、货品条码、实存）
			InvCountDetailVO detailVo = countDetailVoList.get(i);
			InvCountDetail invCountDetail = detailVo.getInvCountDetail();
			String locationNo = detailVo.getLocationNo();
//			String skuBarCode = detailVo.getSkuBarCode();
			String skuNo = detailVo.getSkuNo();
			String batchNo = invCountDetail.getBatchNo();
			String locationId = null;
			if (locationNo == null) {
				throw new BizException("err_count_location_null");
			} else {
				MetaLocation location = locationService.findLocByNo(locationNo, null);
				if (location == null) {
					throw new BizException("err_count_location_not_exists");
				}
				if (location.getLocationType() == Constant.LOCATION_TYPE_TEMPSTORAGE) {
					throw new BizException("err_count_location_temp");
				}
				locationId = location.getLocationId();
			}
			String skuId = null;
			if (skuNo == null) {
				throw new BizException("err_count_sku_null");
			} else {
				MetaSku queryEntity = new MetaSku();
//				queryEntity.setSkuBarCode(skuBarCode);
				queryEntity.setSkuNo(skuNo);
				MetaSku sku = skuService.query(queryEntity);
				if (sku == null) {
					throw new BizException("err_count_sku_not_exists");
				}
				skuId = sku.getSkuId();
			}
			Double realQty = invCountDetail.getRealCountQty();
			if (realQty == null || realQty <= 0) {
				throw new BizException("err_count_qty");
			}
			//--检测重复
			for (int j = i+1; j < countDetailVoList.size(); j++) {
				//库位号、货品条码、实存相同则重复
				InvCountDetailVO detailVo2 = countDetailVoList.get(j);
				InvCountDetail invCountDetail2 = detailVo2.getInvCountDetail();
				String locationNo2 = detailVo2.getLocationNo();
				String skuNo2 = detailVo2.getSkuBarCode();
				String batchNo2 = invCountDetail2.getBatchNo();
				if (StringUtil.equals(locationNo, locationNo2) && StringUtil.equals(skuNo, skuNo2) && StringUtil.equals(batchNo, batchNo2)) {
					throw new BizException("err_common_repeat");
				}
			}
			//校验库存，无库存报异常
			InvStockVO stockVo = new InvStockVO();
			InvStock stock = new InvStock();
			stockVo.setInvStock(stock);
			stockVo.setContainBatch(true);
			stockVo.setContainTemp(false);
			stock.setSkuId(skuId);
			stock.setLocationId(locationId);
			stock.setBatchNo(batchNo);
			List<InvStock> stockList = stockService.list(stockVo);
			if (stockList == null || stockList.isEmpty() || stockList.get(0) == null) {
				throw new BizException("err_count_stock_not_exists");
			}
			Double stockQty = stockList.get(0).getStockQty();
			invCountDetail.setCountDetailId(IdUtil.getUUID());
			invCountDetail.setCountId(countId);
			invCountDetail.setStockQty(stockQty);
			invCountDetail.setSkuId(skuId);
			invCountDetail.setLocationId(locationId);
			invCountDetail.setOrgId(orgId);
			invCountDetail.setWarehouseId(warehouseId);
			invCountDetail.setCreatePerson(loginUser.getUserId());
			invCountDetail.setCreateTime(now);
			invCountDetail.setUpdatePerson(loginUser.getUserId());
			invCountDetail.setUpdateTime(now);
			//--新增盘点明细，同时校验数量，如果有异常，生成调账单明细，修改盘点单异常状态为1（异常）
			if (stockQty.doubleValue() != realQty.doubleValue()) {
				invCountDetail.setResult(1);
				count.setResult(1);
				//生成调账单明细记录
				InvAdjustDetailVO adjDetailVo = new InvAdjustDetailVO();
				adjDetailVoList.add(adjDetailVo);
				InvAdjustDetail adjDetail = new InvAdjustDetail();
				adjDetailVo.setInvAdjustDetail(adjDetail);
				adjDetail.setAdjustType(realQty > stockQty ? Constant.ADJUST_TYPE_IN : Constant.ADJUST_TYPE_OUT);
				adjDetail.setStockQty(stockQty);
				adjDetail.setDifferenceQty(realQty - stockQty);
				adjDetail.setRealQty(realQty);
				adjDetail.setSkuId(skuId);
				adjDetail.setLocationId(locationId);
				adjDetail.setBatchNo(batchNo);
			} else {
				invCountDetail.setResult(0);
			}
		}
		//如果调账单明细不为空，则保存调账单
		if (!adjDetailVoList.isEmpty()) {
			adjust.setCountId(countId);
			this.adjustService.add(adjustVo);
		}
		//--保存盘点主单
    	this.dao.insertSelective(count);
    	for (InvCountDetailVO detailVo : countDetailVoList) {
        	//--保存盘点详情单
    		InvCountDetail invCountDetail = detailVo.getInvCountDetail();
    		this.detailDao.insertSelective(invCountDetail);
    	}
	}
	
	/**
	 * 传送期末库存数据对接仓储企业联网监管系统
	 * @throws Exception
	 */
	public void transmitStocksXML(String id) throws Exception{
		
		Principal loginUser = LoginUtil.getLoginUser();
		int currentPage = 0;
		int pageSize = 2000;//2000
		String messsageId = IdUtil.getUUID();
		String emsNo = paramService.getKey(CacheName.EMS_NO);
		String sendId = paramService.getKey(CacheName.TRAED_CODE);
		String receiveId = paramService.getKey(CacheName.CUSTOM_CODE);
		String sendChnalName = paramService.getKey(CacheName.MSMQ_SEND_CHNALNAME);
		String label = paramService.getKey(CacheName.PARAM_MESSAGE_LABEL);
		String messageType = paramService.getKey(CacheName.MSMQ_MESSAGE_TYPE_COUNT);
		Date today = new Date();
		String dateTime = DateFormatUtils.format(today, "yyyy-MM-dd HH:mm:ss").replaceAll(" ", "T");
		String date = DateFormatUtils.format(today, "yyyy-MM-dd");
		Page<InvStockVO> stockPage = new Page<InvStockVO>();
		try {
			do{
				//查询2000条库存记录
				InvStockVO stockVo = new InvStockVO();
				stockVo.setCurrentPage(currentPage);
				stockVo.setPageSize(pageSize);
				stockPage = stockService.listByPage(stockVo);
				//发送期末库存数据到仓储企业联网监管系统
				if(stockPage != null && !stockPage.isEmpty()){
					MSMQ msmq = new MSMQ();
					//报文头参数
					Head head = new Head();
					head.setMESSAGE_ID(messsageId);
					head.setMESSAGE_TYPE(messageType);
					head.setEMS_NO(emsNo);
					head.setFUNCTION_CODE("A");//功能代码 新增
					head.setMESSAGE_DATE(dateTime);
					head.setSENDER_ID(sendId);
					head.setRECEIVER_ID(receiveId);
					head.setSEND_TYPE("0");
					msmq.setHead(head);
					
					List<Map<String,String>> mapList = new ArrayList<Map<String,String>>();
					//报文体参数
					for(InvStockVO stoVo:stockPage){
						Map<String,String> map = new HashMap<String,String>();
						map.put("MESSAGE_ID", messsageId);
						map.put("EMS_NO", emsNo);
						map.put("COP_G_NO",StringUtil.isBlank(stoVo.getSku().getHgGoodsNo())?stoVo.getSku().getSkuNo():stoVo.getSku().getHgGoodsNo());
						map.put("G_NO", stoVo.getSku().getgNo());
						map.put("QTY", stoVo.getInvStock().getStockQty().toString());
						map.put("UNIT", stoVo.getMeasureUnit());
						map.put("STOCK_DATE", date);
						map.put("GOODS_NATURE", stoVo.getSku().getGoodsNature()==null?"":stoVo.getSku().getGoodsNature()+"");
						map.put("DATA_TYPE", "B");
						map.put("WHS_CODE", stoVo.getWarehouseNo());
						map.put("LOCATION_CODE", stoVo.getLocationNo());
						map.put("OWNER_CODE", stoVo.getHgMerchantNo());
						map.put("OWNER_NAME", stoVo.getOwnerName());
						mapList.add(map);
					}
					msmq.setBodyMaps(mapList);
					
					//创建期末库存报文
					TransmitXmlUtil tranUtil = new TransmitXmlUtil(msmq);
					String xmlStr = tranUtil.formXml();
					
					//发送报文
					if(log.isInfoEnabled()) log.info("MSMQ_Message_"+messsageId+"_实盘报文：["+xmlStr+"]");
					if(log.isInfoEnabled()) log.info("Queue名称：["+sendChnalName+"]");
					boolean result = MSMQUtil.send(sendChnalName, label, xmlStr, messsageId.getBytes());
					if(log.isInfoEnabled()) log.info("MSMQ_Message_"+messsageId+"_实盘发送接口运行结果：["+result+"]");
					
					//保存报文
					MsmqMessageVo messageVo = new MsmqMessageVo();
					messageVo.getEntity().setMessageId(messsageId);
					messageVo.getEntity().setContent(xmlStr);
					messageVo.getEntity().setOrderNo(id);
					messageVo.getEntity().setSender(Constant.SYSTEM_TYPE_WMS);
					messageVo.getEntity().setFunctionType(Constant.FUNCTION_TYPE_COUNT);
					messageVo.getEntity().setSendTime(new Date());
					messageVo.getEntity().setCreatePerson(loginUser.getUserId());
					messageVo.getEntity().setOrgId(loginUser.getOrgId());
					messageVo.getEntity().setWarehouseId(LoginUtil.getWareHouseId());
					msmqMessageService.add(messageVo);
					
					//更新盘点单状态
					if(result){
						InvCountVO countVo = new InvCountVO(new InvCount());
						countVo.getInvCount().setCountId(id);
						countVo.getInvCount().setTransStatus(Constant.SYNCSTOCK_STATUS_SEND_SUCCESS);
						dao.updateByPrimaryKeySelective(countVo.getInvCount());
					}
					
					currentPage++;
				}
			}
			while(currentPage > stockPage.getPages());
		} catch (Exception e) {
			if(log.isErrorEnabled()) log.error(e.getMessage());
		}
	}
	
	public static void main(String[] args) {
		Date today = new Date();
		String date = DateFormatUtils.format(today, "yyyy-MM-dd HH:MM:SS").replace(" ", "T");
		System.out.println(date);
	}

	@Override
	public ResponseEntity<byte[]> downloadExcel(InvCountVO vo) throws Exception {
		// 导出库存  
	      ExcelExport<InvCountDetailVO4Excel> ex = new ExcelExport<InvCountDetailVO4Excel>();  
	      String[] headers = { 
	    			"序号", "货主简称", "库区", "库位", 
	    			"货品名称","货品条码", "批次", 
	    			"规格", "计量单位", "系统数量",
	    			"实际数量","在库天数", "到货日期"};  
	      List<InvCountDetailVO4Excel> dataset = vo2excelVo(view(vo.getCountIdList().get(0)));
	      ResponseEntity<byte[]> bytes = ex.exportExcel2Array("盘点统计", headers, dataset, "yyyy-MM-dd", "盘点统计.xls");
	      return bytes;
	}

	private List<InvCountDetailVO4Excel> vo2excelVo(InvCountVO vo) {
		List<InvCountDetailVO4Excel> retList = new ArrayList<InvCountDetailVO4Excel>();
		for (int j = 0; j < vo.getListInvCountDetailVO().size(); j++) {
			InvCountDetailVO detailVo = vo.getListInvCountDetailVO().get(j);
			InvCountDetailVO4Excel vo4excel = new InvCountDetailVO4Excel((j+1), detailVo);
			retList.add(vo4excel);
		}
		return retList;
	}

	@Override
	public String getApplyNo() {
		Principal loginUser = LoginUtil.getLoginUser();
		String orgId = loginUser.getOrgId();
		String no = context.getStrategy4No(orgId, LoginUtil.getWareHouseId()).getApplyRejectsNo(orgId);
		return no;
	}
}