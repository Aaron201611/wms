package com.yunkouan.wms.modules.inv.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yunkouan.entity.ResultModel;
import com.yunkouan.exception.BizException;
import com.yunkouan.saas.modules.sys.entity.MetaLocation;
import com.yunkouan.saas.modules.sys.entity.SysUser;
import com.yunkouan.saas.modules.sys.service.ISysParamService;
import com.yunkouan.saas.modules.sys.service.IUserService;
import com.yunkouan.saas.modules.sys.service.IWarehouseExtlService;
import com.yunkouan.wms.modules.ts.service.ITaskService;
import com.yunkouan.util.NumberUtil;
import com.yunkouan.util.PubUtil;
import com.yunkouan.util.StringUtil;
import com.yunkouan.valid.ValidSave;
import com.yunkouan.wms.common.constant.CacheName;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.common.service.BaseService;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.FqDataUtils;
import com.yunkouan.wms.common.util.IdUtil;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.assistance.service.IAssisService;
import com.yunkouan.wms.modules.assistance.vo.MessageData;
import com.yunkouan.wms.modules.inv.dao.IShiftDao;
import com.yunkouan.wms.modules.inv.dao.IShiftDetailDao;
import com.yunkouan.wms.modules.inv.entity.InvLog;
import com.yunkouan.wms.modules.inv.entity.InvShift;
import com.yunkouan.wms.modules.inv.entity.InvShiftDetail;
import com.yunkouan.wms.modules.inv.entity.InvStock;
import com.yunkouan.wms.modules.inv.service.IShiftService;
import com.yunkouan.wms.modules.inv.service.IStockService;
import com.yunkouan.wms.modules.inv.util.InvUtil;
import com.yunkouan.wms.modules.inv.vo.InvOutLockDetailVO;
import com.yunkouan.wms.modules.inv.vo.InvShiftDetailVO;
import com.yunkouan.wms.modules.inv.vo.InvShiftVO;
import com.yunkouan.wms.modules.inv.vo.InvStockVO;
import com.yunkouan.wms.modules.meta.entity.MetaMerchant;
import com.yunkouan.wms.modules.meta.entity.MetaSku;
import com.yunkouan.wms.modules.meta.service.IAreaService;
import com.yunkouan.wms.modules.meta.service.ILocationExtlService;
import com.yunkouan.wms.modules.meta.service.IPackService;
import com.yunkouan.wms.modules.meta.service.ISkuService;
import com.yunkouan.wms.modules.meta.vo.SkuVo;
import com.yunkouan.wms.modules.rec.entity.RecAsn;
import com.yunkouan.wms.modules.rec.service.IASNExtlService;
import com.yunkouan.wms.modules.rec.vo.RecAsnDetailVO;
import com.yunkouan.wms.modules.rec.vo.RecAsnVO;

/**
 * 移位服务实现类
 */
@Service
public class ShiftServiceImpl extends BaseService implements IShiftService {
	/**
     * 数据层接口
     */
	@Autowired
    private IShiftDao dao;
	/**
     * 数据层接口
     */
	@Autowired
    private IShiftDetailDao detailDao;
	/**
	 * 外部服务-包装
	 */
	@Autowired
	IPackService packService;
	/**
	 * 外部服务-库区
	 */
	@Autowired
	IAreaService areaService;

	/**
	 * 外部服务-仓库
	 */
	@Autowired
	IWarehouseExtlService warehouseExtlService;
	/**
	 * 外部服务-库存
	 */
	@Autowired
	IStockService stockService;
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
//	/**
//	 * 外部服务-企业
//	 */
//	@Autowired
//	IOrgService orgService;
	/**
	 * 外部服务-用户
	 */
	@Autowired
	IUserService userService;
	/**
	 * 外部服务-收货单
	 */
	@Autowired
	IASNExtlService asnService;

	/**
	 * 外部服务-任务单
	 */
	@Autowired
	private ITaskService taskService;
	
	@Autowired
	private ISysParamService paramService;

	/**
     * Default constructor
     */
    public ShiftServiceImpl() {
    }

	/**
	 * 日志对象
	 */
	private Logger log = LoggerFactory.getLogger(getClass());
    /**
     * 查询移位单列表
     * @param vo
     * @return
     * @Description 
     * @version 2017年2月16日 下午1:24:22<br/>
     * @author 王通<br/>
     * @throws Exception 
     */
	@Override
	@Transactional(readOnly=false)
    public Page<InvShiftVO> list(InvShiftVO vo) throws Exception {
		Principal loginUser = LoginUtil.getLoginUser();
    	// 验证用户是否登录
    	if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
    		throw new BizException("valid_common_user_no_login");
    	}
    	if ( vo == null ) {
    		vo = new InvShiftVO();
		}
		if ( vo.getInvShift() == null ) {
			vo.setInvShift(new InvShift());
		}
		Page<InvShiftVO> page = null;
		// 包装当前用户信息
		vo.setLoginUser(loginUser);
		vo.setInLocationName(StringUtil.likeEscapeH(vo.getInLocationName()));
		vo.setOutLocationName(StringUtil.likeEscapeH(vo.getOutLocationName()));
		// 查询移位单列表 
		page = PageHelper.startPage(vo.getCurrentPage()+1, vo.getPageSize());
		List<InvShift> listInvShift = this.dao.list(vo);
		if ( listInvShift == null || listInvShift.isEmpty() ) {
			return null;
		}
		// 组装信息
		List<InvShiftVO> listVO = new ArrayList<InvShiftVO>();
		for (int i = 0; i < listInvShift.size(); i++) {
			InvShift shift = listInvShift.get(i);
			if ( shift == null ) {
				continue;
			}
			InvShiftVO shiftVo = new InvShiftVO(shift).searchCache();
			listVO.add(shiftVo);
			// 查询创建人信息
			SysUser createPerson = userService.get(shift.getCreatePerson());
			if ( createPerson != null ) {
				shiftVo.setCreateUserName(createPerson.getUserName());
			}
			// 查询修改人信息
			SysUser updatePerson = null;
			if ( StringUtil.equals(shift.getCreatePerson(), shift.getUpdatePerson())) {
				updatePerson = createPerson;
			} else {
				if (!StringUtil.isTrimEmpty(shift.getUpdatePerson())) {
					updatePerson = userService.get(shift.getUpdatePerson());
				}
			}
			if ( updatePerson != null ) {
				shiftVo.setUpdateUserName(updatePerson.getUserName());
			}
			// 查询作业人员信息
			if ( !StringUtil.isTrimEmpty(shift.getOpPerson())) {
				SysUser opPerson = userService.get(shift.getOpPerson());
				if (opPerson != null) {
					shiftVo.setOpPersonName(opPerson.getUserName());
				}
			}
			//移位单状态
			if (shift.getShiftStatus() != null) {
				shiftVo.setShiftStatusName(paramService.getValue(CacheName.STATUS, shift.getShiftStatus()));
			}
			//移位单移位类型
			if (shift.getShiftType() != null) {
				shiftVo.setShiftTypeName(paramService.getValue(CacheName.SHIFT_TYPE, shift.getShiftType()));
			}
		}
		page.clear();
		page.addAll(listVO);
		return page;
    }
	/**
     * 查询移位单列表
     * @param vo
     * @return
     * @Description 
     * @version 2017年2月16日 下午1:24:22<br/>
     * @author 王通<br/>
     * @throws Exception 
     */
	@Override
	@Transactional(readOnly=false)
    public Page<InvShiftVO> list4Print(InvShiftVO vo) throws Exception {
		Principal loginUser = LoginUtil.getLoginUser();
    	// 验证用户是否登录
    	if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
    		throw new BizException("valid_common_user_no_login");
    	}
    	if ( vo == null ) {
    		vo = new InvShiftVO();
		}
		if ( vo.getInvShift() == null ) {
			vo.setInvShift(new InvShift());
		}
		Page<InvShiftVO> page = null;
		// 包装当前用户信息
		vo.setLoginUser(loginUser);
		vo.setInLocationName(StringUtil.likeEscapeH(vo.getInLocationName()));
		vo.setOutLocationName(StringUtil.likeEscapeH(vo.getOutLocationName()));
		// 查询移位单列表 
		page = PageHelper.startPage(vo.getCurrentPage()+1, vo.getPageSize());
		List<InvShift> listInvShift = this.dao.list(vo);
		if ( listInvShift == null || listInvShift.isEmpty() ) {
			return null;
		}
		// 组装信息
		List<InvShiftVO> listVO = new ArrayList<InvShiftVO>();
		for (int i = 0; i < listInvShift.size(); i++) {
			InvShift shift = listInvShift.get(i);
			if ( shift == null ) {
				continue;
			}
			InvShiftVO shiftVo = new InvShiftVO(shift).searchCache();
			listVO.add(shiftVo);
			// 查询创建人信息
			SysUser createPerson = userService.get(shift.getCreatePerson());
			if ( createPerson != null ) {
				shiftVo.setCreateUserName(createPerson.getUserName());
			}
			// 查询修改人信息
			SysUser updatePerson = null;
			if ( StringUtil.equals(shift.getCreatePerson(), shift.getUpdatePerson())) {
				updatePerson = createPerson;
			} else {
				if (!StringUtil.isTrimEmpty(shift.getUpdatePerson())) {
					updatePerson = userService.get(shift.getUpdatePerson());
				}
			}
			if ( updatePerson != null ) {
				shiftVo.setUpdateUserName(updatePerson.getUserName());
			}
			// 查询作业人员信息
			if ( !StringUtil.isTrimEmpty(shift.getOpPerson())) {
				SysUser opPerson = userService.get(shift.getOpPerson());
				if (opPerson != null) {
					shiftVo.setOpPersonName(opPerson.getUserName());
				}
			}
			//移位单状态
			if (shift.getShiftStatus() != null) {
				shiftVo.setShiftStatusName(paramService.getValue(CacheName.STATUS, shift.getShiftStatus()));
			}
			//移位单移位类型
			if (shift.getShiftType() != null) {
				shiftVo.setShiftTypeName(paramService.getValue(CacheName.SHIFT_TYPE, shift.getShiftType()));
			}
			// 查询移位单明细
			List<InvShiftDetailVO> listInvShiftDetailVO = new ArrayList<InvShiftDetailVO>();
			List<InvShiftDetail> listInvShiftDetail = detailDao.list(shift.getShiftId());
			if ( listInvShiftDetail != null && !listInvShiftDetail.isEmpty() ) {
				for (int j = 0; j < listInvShiftDetail.size(); j++) {
					InvShiftDetail detail = listInvShiftDetail.get(j);
					InvShiftDetailVO invShiftDetailVO = new InvShiftDetailVO(detail).searchCache();
					// 查询货品信息
					MetaSku metaSku = skuService.get(detail.getSkuId());
					invShiftDetailVO.setSkuName(metaSku.getSkuName());
					invShiftDetailVO.setSkuNo(metaSku.getSkuNo());
					invShiftDetailVO.setMeasureUnit(metaSku.getMeasureUnit());
					invShiftDetailVO.setSpecModel(metaSku.getSpecModel());
					invShiftDetailVO.setSkuBarCode(metaSku.getSkuBarCode());
					listInvShiftDetailVO.add(invShiftDetailVO);
					//查询库位信息
					MetaLocation outLocation = locationService.findLocById(detail.getOutLocation());
					MetaLocation inLocation = locationService.findLocById(detail.getInLocation());
					invShiftDetailVO.setInLocation(inLocation.getLocationName());
					invShiftDetailVO.setInLocationNo(inLocation.getLocationNo());
					invShiftDetailVO.setOutLocation(outLocation.getLocationName());
					invShiftDetailVO.setOutLocationNo(outLocation.getLocationNo());
				}
			}
			shiftVo.setListInvShiftDetailVO(listInvShiftDetailVO);
		}
		page.clear();
		page.addAll(listVO);
		return page;
    }
    /**
     * 查看移位单详情
     * @param vo
     * @return 
     * @Description 
     * @version 2017年2月16日 下午1:58:31<br/>
     * @author 王通<br/>
     */
    @Override
	@Transactional(readOnly=false)
    public InvShiftVO view(String id) throws Exception {
		Principal loginUser = LoginUtil.getLoginUser();
    	// 验证用户是否登录
    	if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
    		throw new BizException("valid_common_user_no_login");
    	}
    	InvShiftVO vo = new InvShiftVO();
		if ( id == null ) {
			throw new BizException("err_shift_null");
		}
		// 包装当前用户信息
		vo.setLoginUser(loginUser);
		// 查询移位单详情 
//		InvShift shift = this.dao.find(id);
		InvShift shift = this.dao.selectByPrimaryKey(id);
		if ( shift == null ) {
			throw new BizException("err_shift_null");
		}
		// 组装信息
		InvShiftVO shiftVo = new InvShiftVO(shift).searchCache();
		// 查询创建人信息
		SysUser createPerson = userService.get(shift.getCreatePerson());
		if ( createPerson != null ) {
			shiftVo.setCreateUserName(createPerson.getUserName());
		}
		// 查询修改人信息
		SysUser updatePerson = null;
		if ( shift.getCreatePerson().equals(shift.getUpdatePerson()) ) {
			updatePerson = createPerson;
		} else {
			updatePerson = userService.get(shift.getUpdatePerson());
		}
		if ( updatePerson != null ) {
			shiftVo.setUpdateUserName(updatePerson.getUserName());
		}
		shiftVo.setShiftTypeName(paramService.getValue(CacheName.SHIFT_TYPE, shift.getShiftType()));
		// 查询移位单明细
		List<InvShiftDetailVO> listInvShiftDetailVO = new ArrayList<InvShiftDetailVO>();
		List<InvShiftDetail> listInvShiftDetail = detailDao.list(id);
		if ( listInvShiftDetail != null && !listInvShiftDetail.isEmpty() ) {
			for (int i = 0; i < listInvShiftDetail.size(); i++) {
				InvShiftDetail detail = listInvShiftDetail.get(i);
				InvShiftDetailVO invShiftDetailVO = new InvShiftDetailVO(detail).searchCache();
				// 查询货品信息
				MetaSku metaSku = skuService.get(detail.getSkuId());
				invShiftDetailVO.setSkuName(metaSku.getSkuName());
				invShiftDetailVO.setSkuNo(metaSku.getSkuNo());
				invShiftDetailVO.setMeasureUnit(metaSku.getMeasureUnit());
				invShiftDetailVO.setSpecModel(metaSku.getSpecModel());
				invShiftDetailVO.setSkuBarCode(metaSku.getSkuBarCode());
				listInvShiftDetailVO.add(invShiftDetailVO);
				//查询库位信息
				MetaLocation outLocation = locationService.findLocById(detail.getOutLocation());
				MetaLocation inLocation = locationService.findLocById(detail.getInLocation());
				invShiftDetailVO.setInLocation(inLocation.getLocationName());
				invShiftDetailVO.setInLocationNo(inLocation.getLocationNo());
				invShiftDetailVO.setOutLocation(outLocation.getLocationName());
				invShiftDetailVO.setOutLocationNo(outLocation.getLocationNo());
				// 包装收货单编号
				if (!StringUtil.isTrimEmpty(detail.getAsnId())) {
					RecAsn asn = asnService.findAsnById(detail.getAsnId());
					if (asn != null) {
						invShiftDetailVO.setAsnNo(asn.getAsnNo());
					}
				}
			}
		}
		shiftVo.setListInvShiftDetailVO(listInvShiftDetailVO);
		return shiftVo;
    }

    /**
     * 保存或修改移位单
     * @param vo
     * @return
     * @Description 
     * @version 2017年2月16日 下午5:46:50<br/>
     * @author 王通<br/>
     * @throws Exception 
     */
	@Override
	@Transactional(readOnly = false)
	public InvShiftVO saveOrUpdate(InvShiftVO vo) throws Exception {
		Principal loginUser = LoginUtil.getLoginUser();
    	// 验证用户是否登录
    	if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
    		throw new BizException("valid_common_user_no_login");
    	}
		if ( vo == null || vo.getInvShift() == null ) {
    		log.error("%s.saveOrUpdate --> param is null!", getClass().getName());
    		throw new BizException("err_shift_null");
    	}
		InvShiftVO invShiftVo = new InvShiftVO();
    	InvShift shift = vo.getInvShift();
    	String shiftId = shift.getShiftId();
    	if (StringUtil.isEmpty(shiftId)) {
    		// 新增方法
        	// 设置uuid
    		shiftId = IdUtil.getUUID();
        	shift.setShiftId(shiftId);
        	// 设置创建人/修改人/企业/仓库
        	String orgId = loginUser.getOrgId();
        	shift.setCreatePerson(loginUser.getUserId());
        	shift.setCreateTime(new Date());
        	shift.setUpdatePerson(loginUser.getUserId());
        	shift.setUpdateTime(new Date());
        	shift.setOrgId(orgId);
        	shift.setWarehouseId(LoginUtil.getWareHouseId());
        	// 设置移位单号
        	shift.setShiftNo(context.getStrategy4No(orgId, LoginUtil.getWareHouseId()).getShiftInternalNo(orgId));
        	Integer shiftType = shift.getShiftType();
        	if (!InvUtil.checkShiftType(shiftType)) {
        		throw new BizException("err_shift_type_wrong");
        	}
        	// 设置状态
        	shift.setShiftStatus(Constant.SHIFT_STATUS_OPEN);
        	shift.setShiftId2(context.getStrategy4Id().getShiftSeq());
        	invShiftVo.setInvShift(shift);
        	this.dao.insertSelective(shift);
        	List<InvShiftDetailVO> listShiftDetailVo = vo.getListInvShiftDetailVO();
        	if ( listShiftDetailVo != null && !listShiftDetailVo.isEmpty() ) {
        		List<InvShiftDetail> listShiftDetail = new ArrayList<InvShiftDetail>();
        		//增加移位单明细
        		for (int i = 0; i < listShiftDetailVo.size(); i++) {
        			InvShiftDetailVO invShiftDetailVO = listShiftDetailVo.get(i);
        			InvShiftDetail invShiftDetail = invShiftDetailVO.getInvShiftDetail();
        			String skuId = invShiftDetail.getSkuId();
        			String outLocaltion = invShiftDetail.getOutLocation();
        			String inLocaltion = invShiftDetail.getInLocation();
        			//验证货品id和库位id是否存在
        			MetaLocation outlocation = this.locationService.findLocById(outLocaltion);
        			MetaLocation inlocation = this.locationService.findLocById(inLocaltion);
        			MetaSku skuRet = this.skuService.get(skuId);
        			if (skuRet == null) {
        				throw new BizException("err_shift_sku_null");
        			}
        			if (outlocation == null) {
        				throw new BizException("err_shift_out_location_null");
        			}
        			if (inlocation == null) {
        				throw new BizException("err_shift_in_location_null");
        			}
        			//判断库位是否在暂存区，如果在暂存区，禁止移入移出
        			if (outlocation.getLocationType() == Constant.LOCATION_TYPE_TEMPSTORAGE || inlocation.getLocationType() == Constant.LOCATION_TYPE_TEMPSTORAGE) {
        				throw new BizException("err_shift_location_temp");
        			}
//        			//判断库位是否在发货区，如果在发货区，禁止移入移出
//        			if (outlocation.getLocationType() == Constant.LOCATION_TYPE_SEND || inlocation.getLocationType() == Constant.LOCATION_TYPE_SEND) {
//        				throw new BizException("err_shift_location_send");
//        			}
        			//如果移入库位和移出库位均是存储区或拣货区，判断移出库位货品类型和移入库位货品类型，不同则禁止移位
        			if ((outlocation.getLocationType() == Constant.LOCATION_TYPE_STORAGE 
        					|| outlocation.getLocationType() == Constant.LOCATION_TYPE_PICKUP) 
        					&& (inlocation.getLocationType() == Constant.LOCATION_TYPE_STORAGE 
        					|| inlocation.getLocationType() == Constant.LOCATION_TYPE_PICKUP) ) {
            			if (!StringUtil.equals(areaService.viewArea(outlocation.getAreaId()).getArea().getSkuTypeId(),areaService.viewArea(inlocation.getAreaId()).getArea().getSkuTypeId())) {
            				throw new BizException("err_shift_location_sku_type");
            			}
        			}
        			Double shiftQty = invShiftDetail.getShiftQty();
        			if (shiftQty < 1) {
                		throw new BizException("err_shift_qty_min");
        			}
        			//设置主单号
        			invShiftDetail.setShiftId(shiftId);
        			//设置自身唯一单号
        			invShiftDetail.setShiftDetailId(IdUtil.getUUID());
        			//其他设置
        			invShiftDetail.setOrgId(loginUser.getOrgId());
        			invShiftDetail.setWarehouseId(LoginUtil.getWareHouseId());
        			invShiftDetail.setCreatePerson(loginUser.getUserId());
        			invShiftDetail.setCreateTime(new Date());
        			invShiftDetail.setUpdatePerson(loginUser.getUserId());
        			invShiftDetail.setUpdateTime(new Date());
        			invShiftDetail.setShiftDetailId2(context.getStrategy4Id().getShiftDetailSeq());
        			listShiftDetail.add(invShiftDetail);
        		}
            	this.detailDao.add(listShiftDetail);
            	invShiftVo.setListInvShiftDetailVO(listShiftDetailVo);
    		}
    	} else {
    		// 修改方法
    		// 检查移位单是否存在
    		InvShiftVO findShift = this.view(shiftId);
    		if (findShift == null || StringUtil.isEmpty(findShift.getInvShift().getShiftId())) {
    			throw new BizException("err_shift_null");
    		} else if (!findShift.getInvShift().getShiftStatus().equals(Constant.SHIFT_STATUS_OPEN)) {
    			throw new BizException("err_shift_status_wrong");
    		}
    		
    		// 设置修改人/企业/仓库
        	shift.setUpdatePerson(loginUser.getUserId());
        	shift.setUpdateTime(new Date());
        	invShiftVo.setInvShift(shift);
        	this.dao.updateByPrimaryKeySelective(shift);
        	List<InvShiftDetailVO> listShiftDetailVo = vo.getListInvShiftDetailVO();
    		//清空移位单明细
    		this.detailDao.emptyDetail(shiftId);
        	if ( listShiftDetailVo != null && !listShiftDetailVo.isEmpty() ) {
        		List<InvShiftDetail> listShiftDetail = new ArrayList<InvShiftDetail>();
        		//添加移位单明细
        		for (int i = 0; i < listShiftDetailVo.size(); i++) {
        			InvShiftDetailVO invShiftDetailVO = listShiftDetailVo.get(i);
        			InvShiftDetail invShiftDetail = invShiftDetailVO.getInvShiftDetail();
        			String inLocation = invShiftDetail.getInLocation();
        			String outLocation = invShiftDetail.getOutLocation();
        			String skuId = invShiftDetail.getSkuId();
        			//验证货品id和库位id是否存在
        			MetaLocation outlocation = this.locationService.findLocById(outLocation);
        			MetaLocation inlocation = this.locationService.findLocById(inLocation);
        			MetaSku skuRet = this.skuService.get(skuId);
        			if (skuRet == null) {
        				throw new BizException("err_shift_sku_null");
        			}
        			if (outlocation == null) {
        				throw new BizException("err_shift_out_location_null");
        			}
        			if(inlocation == null) {
        				throw new BizException("err_shift_in_location_null");
        			}
        			//判断库位是否在暂存区，如果在暂存区，禁止移出
        			if (outlocation.getLocationType() == Constant.LOCATION_TYPE_TEMPSTORAGE) {
        				throw new BizException("err_stock_param_location_shift");
        			}
        			//如果移入库位和移出库位均是存储区或拣货区，判断移出库位货品类型和移入库位货品类型，不同则禁止移位
        			if ((outlocation.getLocationType() == Constant.LOCATION_TYPE_STORAGE 
        					|| outlocation.getLocationType() == Constant.LOCATION_TYPE_PICKUP) 
        					&& (inlocation.getLocationType() == Constant.LOCATION_TYPE_STORAGE 
        					|| inlocation.getLocationType() == Constant.LOCATION_TYPE_PICKUP) ) {
            			if (!StringUtil.equals(areaService.viewArea(outlocation.getAreaId()).getArea().getSkuTypeId(),areaService.viewArea(inlocation.getAreaId()).getArea().getSkuTypeId())) {
            				throw new BizException("err_shift_location_sku_type");
            			}
        			}
        			//设置主单号
        			invShiftDetail.setShiftId(shiftId);
        			//设置自身唯一单号
        			invShiftDetail.setShiftDetailId(IdUtil.getUUID());
        			//其他设置
        			invShiftDetail.setOrgId(loginUser.getOrgId());
        			invShiftDetail.setWarehouseId(LoginUtil.getWareHouseId());
        			invShiftDetail.setCreatePerson(loginUser.getUserId());
        			invShiftDetail.setCreateTime(new Date());
        			invShiftDetail.setUpdatePerson(loginUser.getUserId());
        			invShiftDetail.setUpdateTime(new Date());
        			invShiftDetail.setShiftDetailId2(context.getStrategy4Id().getShiftDetailSeq());
        			listShiftDetail.add(invShiftDetail);
        		}
            	this.detailDao.add(listShiftDetail);
            	invShiftVo.setListInvShiftDetailVO(listShiftDetailVo);
    		}
    	}
		return invShiftVo;
	}
	/**
	 * 生效移位单
	 * @param shiftIdList
	 * @throws Exception
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月2日 上午9:43:05<br/>
	 * @author 王通<br/>
	 */
	@Override
	@Transactional(readOnly=false)
	public void enable( InvShiftVO vo ) throws Exception {
		Principal loginUser = LoginUtil.getLoginUser();
    	// 验证用户是否登录
    	if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
    		throw new BizException("valid_common_user_no_login");
    	}
		if (vo == null || vo.getShiftIdList() == null || vo.getShiftIdList().isEmpty()) {
			throw new BizException("err_shift_null");
		}
		List<String> shiftIdList = vo.getShiftIdList();
		//检查是否指派作业人员
//		String opPerson = vo.getTsTaskVo().getTsTask().getOpPerson();
//		if(StringUtil.isTrimEmpty(opPerson)){
//			throw new BizException(BizStatus.TASK_OPPERSON_IS_NULL.getReasonPhrase());
//		}
    	//获取登录信息中的企业和仓库
    	String orgId = loginUser.getOrgId();
    	String warehouseId = LoginUtil.getWareHouseId();
		for (String shiftId : shiftIdList) {
			InvShiftVO shiftVo = this.view(shiftId);
			if (shiftVo == null) {
				throw new BizException("err_shift_null");
			}
			InvShift shift = shiftVo.getInvShift();
			if (shift.getShiftStatus() != Constant.STATUS_OPEN) {
				throw new BizException("err_shift_status_not_open");
			}
			List<InvShiftDetailVO> shiftDetailVoList = shiftVo.getListInvShiftDetailVO();
			if (shiftDetailVoList != null && !shiftDetailVoList.isEmpty()) {
				for (InvShiftDetailVO shiftDetailVo : shiftDetailVoList) {
					InvShiftDetail shiftDetail = shiftDetailVo.getInvShiftDetail();
	    			//验证移位数量出库库存--生效时验证
					Double shiftQty = shiftDetail.getShiftQty();
	    			InvStockVO stockVo = new InvStockVO();
	    			InvStock stock = new InvStock();
	    			String skuId = shiftDetail.getSkuId();
	    			MetaSku sku = skuService.get(skuId);
	    			stock.setSkuId(skuId);
	    			String outLocaltion = shiftDetail.getOutLocation();
	    			String inLocaltion = shiftDetail.getInLocation();
	    			//验证货品id和库位id是否存在
        			MetaLocation outlocation = this.locationService.findLocById(outLocaltion);
        			MetaLocation inlocation = this.locationService.findLocById(inLocaltion);
	    			//判断库位是否在暂存区，如果在暂存区，禁止移入移出
        			if (outlocation.getLocationType() == Constant.LOCATION_TYPE_TEMPSTORAGE || inlocation.getLocationType() == Constant.LOCATION_TYPE_TEMPSTORAGE) {
        				throw new BizException("err_shift_location_temp");
        			}
//        			//判断库位是否在发货区，如果在发货区，禁止移入移出
//        			if (outlocation.getLocationType() == Constant.LOCATION_TYPE_SEND || inlocation.getLocationType() == Constant.LOCATION_TYPE_SEND) {
//        				throw new BizException("err_shift_location_send");
//        			}
        			//如果移入库位和移出库位均是存储区或拣货区，判断移出库位货品类型和移入库位货品类型，不同则禁止移位
        			if ((outlocation.getLocationType() == Constant.LOCATION_TYPE_STORAGE 
        					|| outlocation.getLocationType() == Constant.LOCATION_TYPE_PICKUP) 
        					&& (inlocation.getLocationType() == Constant.LOCATION_TYPE_STORAGE 
        					|| inlocation.getLocationType() == Constant.LOCATION_TYPE_PICKUP) ) {
            			if (!StringUtil.equals(areaService.viewArea(outlocation.getAreaId()).getArea().getSkuTypeId(),areaService.viewArea(inlocation.getAreaId()).getArea().getSkuTypeId())) {
            				throw new BizException("err_shift_location_sku_type");
            			}
        			}
	    			stock.setLocationId(outLocaltion);
	    			stock.setAsnId(shiftDetail.getAsnId());
	    			stock.setAsnDetailId(shiftDetail.getAsnDetailId());
	    			stock.setBatchNo(shiftDetail.getBatchNo());
	    			stock.setOrgId(orgId);
	    			stock.setWarehouseId(warehouseId);
	    			stockVo.setInvStock(stock);
	    			stockVo.setFindNum(shiftQty);
	    			stockVo.setContainBatch(true);
	    			stockVo.setContainTemp(true);
	    			//锁定预分配库存
	    			this.stockService.lockOutStock(stockVo);
	    			//验证移位数量入库库容和包装--生效时验证
	    			stock.setLocationId(inLocaltion);
	    			stock.setSkuId(skuId);
	    			String packId = shiftDetail.getPackId();
	    			stock.setPackId(packId);
	    			//增加预分配库容
	    			this.locationService.addPreusedCapacity(skuId, inLocaltion, packId, new BigDecimal(shiftQty * sku.getPerVolume()));
				}
			} else {
				throw new BizException("err_shift_detail_null");
			}
			InvShift shiftRecord = new InvShift();
			shiftRecord.setShiftId(shiftId);
			shiftRecord.setOpPerson(loginUser.getUserId());
			shiftRecord.setShiftStatus(Constant.SHIFT_STATUS_ACTIVE);
			this.dao.updateByPrimaryKeySelective(shiftRecord);
			//新建任务
//			taskService.create(vo.getTsTaskVo().getTsTask().getOpPerson(),Constant.TASK_TYPE_SHIFT,shiftId);
			taskService.create(Constant.TASK_TYPE_SHIFT,shiftId);
		}
	}

	/**
	 * @param shiftIdList
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月7日 下午4:46:25<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
	@Override
	@Transactional(readOnly=false)
	public void disable(InvShiftVO vo) throws Exception {
		Principal loginUser = LoginUtil.getLoginUser();
    	// 验证用户是否登录
    	if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
    		throw new BizException("valid_common_user_no_login");
    	}
		if (vo == null || vo.getShiftIdList() == null || vo.getShiftIdList().isEmpty()) {
			throw new BizException("err_shift_null");
		}
		List<String> shiftIdList = vo.getShiftIdList();
    	//获取登录信息中的企业和仓库
    	String orgId = loginUser.getOrgId();
    	String warehouseId = LoginUtil.getWareHouseId();
		for (String shiftId : shiftIdList) {
			InvShiftVO shiftVo = this.view(shiftId);
			if (shiftVo == null) {
				throw new BizException("err_shift_null");
			}
			InvShift shift = shiftVo.getInvShift();
			if (shift.getShiftStatus() != Constant.STATUS_ACTIVE) {
				throw new BizException("err_shift_status_not_active");
			}
			List<InvShiftDetailVO> shiftDetailVoList = shiftVo.getListInvShiftDetailVO();
			if (shiftDetailVoList != null && !shiftDetailVoList.isEmpty()) {
				for (InvShiftDetailVO shiftDetailVo : shiftDetailVoList) {
					InvShiftDetail shiftDetail = shiftDetailVo.getInvShiftDetail();
	    			//验证移位数量出库库存--生效时验证
	    			Double shiftQty = shiftDetail.getShiftQty();
	    			InvStockVO stockVo = new InvStockVO();
	    			InvStock stock = new InvStock();
	    			String skuId = shiftDetail.getSkuId();
	    			MetaSku sku = skuService.get(skuId);
	    			stock.setSkuId(skuId);
	    			String outLocation = shiftDetail.getOutLocation();
	    			String inLocation = shiftDetail.getInLocation();
	    			stock.setLocationId(outLocation);
	    			stock.setAsnId(shiftDetail.getAsnId());
	    			stock.setAsnDetailId(shiftDetail.getAsnDetailId());
	    			stock.setBatchNo(shiftDetail.getBatchNo());
	    			stock.setOrgId(orgId);
	    			stock.setWarehouseId(warehouseId);
	    			stockVo.setContainBatch(true);
	    			stockVo.setContainTemp(true);
	    			stockVo.setInvStock(stock);
	    			stockVo.setFindNum(shiftQty);
	    			//解锁预分配库存
	    			this.stockService.unlockOutStock(stockVo);
	    			stock.setLocationId(inLocation);
	    			stock.setSkuId(skuId);
	    			String packId = shiftDetail.getPackId();
	    			stock.setPackId(packId);
	    			//减少预分配库容
	    			this.locationService.addPreusedCapacity(skuId, inLocation, packId, new BigDecimal(-shiftQty * sku.getPerVolume()));
				}
			}
			InvShift shiftRecord = new InvShift();
			shiftRecord.setShiftId(shiftId);
			shiftRecord.setShiftStatus(Constant.SHIFT_STATUS_OPEN);
			this.dao.updateByPrimaryKeySelective(shiftRecord);

			//取消任务
			taskService.cancelByOpId(shiftId, loginUser.getUserId());
		}
	}
	
	/**
	 * @param shiftIdList
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月7日 下午4:46:25<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
	@Override
	@Transactional(readOnly=false)
	public void cancel(InvShiftVO vo) throws Exception {
		Principal loginUser = LoginUtil.getLoginUser();
    	// 验证用户是否登录
    	if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
    		throw new BizException("valid_common_user_no_login");
    	}
		if (vo == null || vo.getShiftIdList() == null || vo.getShiftIdList().isEmpty()) {
			throw new BizException("err_shift_null");
		}
		List<String> shiftIdList = vo.getShiftIdList();
    	//获取登录信息中的企业和仓库
		for (String shiftId : shiftIdList) {
			InvShiftVO shiftVo = this.view(shiftId);
			if (shiftVo == null) {
				throw new BizException("err_shift_null");
			}
			InvShift shift = shiftVo.getInvShift();
			if (shift.getShiftStatus() != Constant.STATUS_OPEN) {
				throw new BizException("err_shift_status_cannot_cancel");
			}
			InvShift shiftRecord = new InvShift();
			shiftRecord.setShiftId(shiftId);
			shiftRecord.setShiftStatus(Constant.SHIFT_STATUS_CANCEL);
			this.dao.updateByPrimaryKeySelective(shiftRecord);
		}
	}
	/**
	 * @param vo
	 * @return
	 * @required shiftId,shiftDetail.shiftDetail.shiftQty,shiftDetail.realShiftQty
	 * @optional  
	 * @Description 
     * 1、解除锁定-预分配   
     * 2、解除锁定-预占用    
     * 3、入库-库存增加			
     * 4、出库-库存减少
	 * @version 2017年3月7日 下午4:46:25<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
	@Override
	@Transactional(readOnly=false)
	public void confirm (InvShiftVO shiftVo) throws Exception {
		Principal loginUser = LoginUtil.getLoginUser();
    	// 验证用户是否登录
    	if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
    		throw new BizException("valid_common_user_no_login");
    	}
		if (shiftVo == null) {
			throw new BizException("err_shift_null");
		}
		String shiftId = shiftVo.getInvShift().getShiftId();
		if (shiftId == null) {
			throw new BizException("err_shift_null");
		}
    	//获取登录信息中的企业和仓库
    	String orgId = loginUser.getOrgId();
    	String warehouseId = LoginUtil.getWareHouseId();
		InvShift shift = this.dao.selectByPrimaryKey(shiftId);
		if (shift == null) {
			throw new BizException("err_shift_null");
		}
		if (shift.getShiftStatus() != Constant.SHIFT_STATUS_ACTIVE  && shift.getShiftStatus() != Constant.SHIFT_STATUS_WORKING) {
			throw new BizException("err_shift_status_cannot_finish");
		}
		String shiftNo = shift.getShiftNo();
		
		InvShiftVO oldShiftVo = this.view(shiftId);
		Map<String, String> map = new HashMap<String, String>();
		for (InvShiftDetailVO detailVo :  oldShiftVo.getListInvShiftDetailVO()) {
			map.put(detailVo.getInvShiftDetail().getShiftDetailId(), detailVo.getInvShiftDetail().getInLocation());
		}
		
		String opPerson = shiftVo.getInvShift().getOpPerson();
		List<InvShiftDetailVO> shiftDetailVoList = shiftVo.getListInvShiftDetailVO();
		if (shiftDetailVoList != null && !shiftDetailVoList.isEmpty()) {
			for (InvShiftDetailVO shiftDetailVo : shiftDetailVoList) {
				InvShiftDetail shiftDetailCon = shiftDetailVo.getInvShiftDetail();
				//校验字段
				String shiftDetailId = shiftDetailCon.getShiftDetailId();
				Double actShiftQty = shiftDetailCon.getRealShiftQty();
				String inLocationId = shiftDetailCon.getInLocation();
				String oldInLocation = map.get(shiftDetailId);
				if (StringUtil.isTrimEmpty(shiftDetailId)) {
					throw new BizException("err_shift_detail_id_null");
				}
				if (StringUtil.isTrimEmpty(inLocationId)) {
					throw new BizException("err_shift_detail_location_null");
				}
				if (actShiftQty == null || actShiftQty == 0) {
					throw new BizException("err_shift_detail_act_qty_null");
				}
				InvShiftDetail shiftDetail = this.detailDao.selectByPrimaryKey(shiftDetailId);
				Double planShiftQty = shiftDetail.getShiftQty();
				String packId = shiftDetail.getPackId();
				InvStockVO stockVo = new InvStockVO();
				InvStock stock = new InvStock();
				String skuId = shiftDetail.getSkuId();
				MetaSku sku = skuService.get(skuId);
				stock.setSkuId(skuId);
				String outLocation = shiftDetail.getOutLocation();
				if (StringUtil.equals(inLocationId,outLocation)) {
					throw new BizException("err_shift_location_same");
				}
				stock.setLocationId(outLocation);
				stock.setBatchNo(shiftDetail.getBatchNo());
				stock.setOrgId(orgId);
				stock.setWarehouseId(warehouseId);
				stock.setPackId(packId);
				stockVo.setInvStock(stock);
				stockVo.setFindNum(planShiftQty);
				//解锁预分配库存
				this.stockService.unlockOutStock(stockVo);
				//减少预分配库容
				this.locationService.addPreusedCapacity(skuId, oldInLocation, packId, new BigDecimal(-planShiftQty * sku.getPerVolume()));
				//预设日志对象
				InvLog invLog = new InvLog();
				stockVo.setInvLog(invLog);
				stockVo.setFindNum(actShiftQty);
				invLog.setInvoiceBill(shiftNo);
				invLog.setOpPerson(opPerson);
				invLog.setLogType(Constant.STOCK_LOG_TYPE_SHIFT);
				invLog.setSkuId(skuId);
				//出库-库存减少（出库位已设置，不需重复设置）
				stockVo.setFindNum(actShiftQty);
				invLog.setOpType(Constant.STOCK_LOG_OP_TYPE_OUT);
				invLog.setQty(-actShiftQty);
				//设置重量和体积
				invLog.setVolume(NumberUtil.mul(-actShiftQty, sku.getPerVolume() == null ? 0 : sku.getPerVolume()));
    			invLog.setWeight(NumberUtil.mul(-actShiftQty, sku.getPerWeight() == null ? 0 : sku.getPerWeight()));
    			
				invLog.setLocationId(outLocation);
				this.stockService.outStock(stockVo);
				//入库-库存增加
				stock.setLocationId(inLocationId);
				invLog.setOpType(Constant.STOCK_LOG_OP_TYPE_IN);
				invLog.setQty(actShiftQty);
				//设置重量和体积
				invLog.setVolume(NumberUtil.mul(actShiftQty, sku.getPerVolume() == null ? 0 : sku.getPerVolume()));
    			invLog.setWeight(NumberUtil.mul(actShiftQty, sku.getPerWeight() == null ? 0 : sku.getPerWeight()));
    			
				invLog.setLocationId(inLocationId);
				this.stockService.inStock(stockVo);
				
				//发送辅助系统
				//出库
				MetaLocation out = FqDataUtils.getLocById(locationService, outLocation);
				MessageData data1 = new MessageData(out.getLocationNo(), out.getLocationColumn(), sku.getSkuNo(), NumberUtil.rounded(actShiftQty, 0), sku.getMeasureUnit(), "E");
//				context.getStrategy4Assis().request(orgId, IAssisService.BILL_TYPE_SHIFT, shiftId, true, data1);
				//入库
				MetaLocation in = FqDataUtils.getLocById(locationService, inLocationId);
				MessageData data2 = new MessageData(in.getLocationNo(), in.getLocationColumn(), sku.getSkuNo(), NumberUtil.rounded(actShiftQty, 0), sku.getMeasureUnit(), "I");
//				context.getStrategy4Assis().request(orgId, IAssisService.BILL_TYPE_SHIFT, shiftId, false, data2);
			}
		} else {
			throw new BizException("err_shift_detail_null");
		}
		InvShift shiftRecord = new InvShift();
		shiftRecord.setShiftId(shiftId);
		shiftRecord.setShiftStatus(Constant.SHIFT_STATUS_FINISH);
		shiftRecord.setOpPerson(opPerson);
		shiftRecord.setOpTime(new Date());
		this.dao.updateByPrimaryKeySelective(shiftRecord);
		//完成任务单
		this.taskService.finishByOpId(shiftId, loginUser.getUserId());
	}

	/**
	 * @param id
	 * @throws Exception
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月7日 下午5:29:48<br/>
	 * @author 王通<br/>
	 */
	@Override
	@Transactional(readOnly=false)
	public void print(String id) throws Exception {
		Principal loginUser = LoginUtil.getLoginUser();
    	// 验证用户是否登录
    	if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
    		throw new BizException("valid_common_user_no_login");
    	}
		InvShiftVO shiftVo = this.view(id);
		if (shiftVo == null) {
			throw new BizException("err_shift_null");
		}
		InvShift shift = shiftVo.getInvShift();
		if (shift.getShiftStatus() != Constant.STATUS_ACTIVE) {
			throw new BizException("err_shift_status_not_active");
		}
		InvShift shiftRecord = new InvShift();
		shiftRecord.setShiftId(id);
		shiftRecord.setShiftStatus(Constant.SHIFT_STATUS_WORKING);
		this.dao.updateByPrimaryKeySelective(shiftRecord);
	}

	/**
	 * 不做任何校验，直接保存入库，参数自填，系统内调用
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月8日 下午5:24:24<br/>
	 * @author 王通<br/>
	 */
	@Override
	@Transactional(readOnly=false)
	public void quickSave(InvShiftVO shiftVo) throws Exception {
			this.dao.insertSelective(shiftVo.getInvShift());
			for (InvShiftDetailVO detailVo : shiftVo.getListInvShiftDetailVO()) {
				this.detailDao.insertSelective(detailVo.getInvShiftDetail());
			}
	}

	/**
	 * 内部服务，查询库存对应的拣货预分配数量
	 * @param locationId
	 * @param skuId
	 * @param batchNo
	 * @return
	 * @throws Exception
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月20日 下午3:24:23<br/>
	 * @author 王通<br/>
	 */
	@Override
	public List<InvOutLockDetailVO> getOutLockDetail(String locationId, String skuId, String batchNo) throws Exception {
		List<InvOutLockDetailVO> listInvOutLockDetailVO = new ArrayList<InvOutLockDetailVO>();
		InvShiftVO vo = new InvShiftVO();
		List<Integer> statusList = new ArrayList<Integer>();
		statusList.add(Constant.STATUS_ACTIVE);
		statusList.add(Constant.STATUS_WORKING);
		vo.setListInvShiftStatus(statusList);
		List<InvShiftDetailVO> listInvShiftDetailVO = new ArrayList<InvShiftDetailVO>();
		InvShiftDetailVO invShiftDetailVO = new InvShiftDetailVO();
		InvShiftDetail invShiftDetail = new InvShiftDetail();
    	// 获取当前用户信息
		Principal loginUser = LoginUtil.getLoginUser();
		// 验证用户是否登录
    	if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
    		throw new BizException("valid_common_user_no_login");
    	}
		invShiftDetail.setOrgId(loginUser.getOrgId());
		invShiftDetail.setWarehouseId(LoginUtil.getWareHouseId());
		invShiftDetailVO.setInvShiftDetail(invShiftDetail);
		invShiftDetail.setBatchNo(batchNo);
		invShiftDetail.setOutLocation(locationId);
		invShiftDetail.setSkuId(skuId);
		String measureUnit = skuService.get(skuId).getMeasureUnit();
		listInvShiftDetailVO.add(invShiftDetailVO);
		vo.setListInvShiftDetailVO(listInvShiftDetailVO);
		List<InvShiftVO> shiftList = this.dao.getOutLockDetail(vo);
		for (InvShiftVO shiftVo : shiftList) {
			if (shiftVo == null) {
				continue;
			}
			InvOutLockDetailVO invOutLockDetailVO = new InvOutLockDetailVO();
			listInvOutLockDetailVO.add(invOutLockDetailVO);
			invOutLockDetailVO.setBill(shiftVo.getShiftNo());
			invOutLockDetailVO.setBillType("移位单");
			invOutLockDetailVO.setBillStatus(paramService.getValue(CacheName.STATUS,shiftVo.getShiftStatus()));
			invOutLockDetailVO.setOutLockQty(shiftVo.getTotalQty());
			invOutLockDetailVO.setMeasureUnit(measureUnit);
		}
		return listInvOutLockDetailVO;
	}
	

	/**
	 * 内部服务，查询预占用库容
	 * @param locationId
	 * @param skuId
	 * @param batchNo
	 * @return
	 * @throws Exception
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月20日 下午3:24:23<br/>
	 * @author 王通<br/>
	 */
	@Override
	public List<InvShiftDetail> getInLocationOccupy(String inLocation, Integer... status) throws Exception {
		InvShiftVO vo = new InvShiftVO();
		List<Integer> statusList = new ArrayList<Integer>();
		for (Integer stat : status) {
			statusList.add(stat);
		}
		vo.setListInvShiftStatus(statusList);
		List<InvShiftDetailVO> listInvShiftDetailVO = new ArrayList<InvShiftDetailVO>();
		InvShiftDetailVO invShiftDetailVO = new InvShiftDetailVO();
		InvShiftDetail invShiftDetail = new InvShiftDetail();
    	// 获取当前用户信息
		Principal loginUser = LoginUtil.getLoginUser();
		// 验证用户是否登录
    	if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
    		throw new BizException("valid_common_user_no_login");
    	}
		invShiftDetailVO.setInvShiftDetail(invShiftDetail);
		invShiftDetail.setOrgId(loginUser.getOrgId());
		invShiftDetail.setWarehouseId(LoginUtil.getWareHouseId());
		invShiftDetail.setInLocation(inLocation);
		listInvShiftDetailVO.add(invShiftDetailVO);
		vo.setListInvShiftDetailVO(listInvShiftDetailVO);
		List<InvShiftDetail> retList = this.detailDao.getInLocationOccupy(vo);
		return retList;
	}
	
	

	/**
	 * 内部调用服务
	 * @param vo
	 * @return
	 * @throws Exception
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年4月19日 下午1:23:56<br/>
	 * @author 王通<br/>
	 */
	@Override
	@Transactional(readOnly = false)
	public InvShiftVO saveAndEnable(InvShiftVO vo) throws Exception {
		InvShiftVO shiftVo = this.saveOrUpdate(vo);
    	String shiftId = vo.getInvShift().getShiftId();
        List<String> idList = new ArrayList<String>();
        idList.add(shiftId);
        vo.setShiftIdList(idList);
        this.enable(vo);
		return shiftVo;
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
	public Integer countByExample(InvShiftVO vo) throws Exception {
		Principal loginUser = LoginUtil.getLoginUser();
    	// 验证用户是否登录
    	if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
    		throw new BizException("valid_common_user_no_login");
    	}
    	vo.getInvShift().setWarehouseId(LoginUtil.getWareHouseId());
    	vo.getInvShift().setOrgId(loginUser.getOrgId());
    	return dao.selectCountByExample(vo.getExample());
	}
	@Override
	@Transactional(readOnly = true)
	public Integer countByExample2(InvShiftVO vo) throws Exception {
    	return dao.selectCountByExample(vo.getExample());
	}
	@Override
	public List<String>getTask(String orgId){
		return dao.getTask(orgId);
	}
	/**
	 * 快速作业确认，用于手持终端
	 * @param shiftVo
	 * @throws Exception
	 * @required 
	 * @optional  
	 * @Description 
	 * 1、校验库存，无库存报异常
	 * 2、新增移位单，状态为完成
	 * 3、入库库存及占用库容，出库库存及释放库容
	 * @version 2017年7月20日 下午4:18:24<br/>
	 * @author 王通<br/>
	 */
	@Override
	@Transactional(readOnly = false)
	public void quickConfirm(InvShiftVO shiftVo) throws Exception {
		// 校验字段是否为空
		Principal loginUser = LoginUtil.getLoginUser();
    	// 验证用户是否登录
    	if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
    		throw new BizException("valid_common_user_no_login");
    	}
		//校验参数不为空
		if ( shiftVo == null || shiftVo.getListInvShiftDetailVO() == null ) {
    		throw new BizException("valid_common_data_empty");
    	}
		// 准备移位单字段，状态为完成
		InvShift shift = new InvShift();
		shift.setShiftStatus(Constant.STATUS_FINISH);
		// 设置uuid
		String shiftId = IdUtil.getUUID();
    	shift.setShiftId(shiftId);
    	// 设置创建人/修改人/企业/仓库
    	String orgId = loginUser.getOrgId();
    	String warehouseId = LoginUtil.getWareHouseId();
    	Date now = new Date();
    	shift.setCreatePerson(loginUser.getUserId());
    	shift.setCreateTime(now);
    	shift.setUpdatePerson(loginUser.getUserId());
    	shift.setUpdateTime(now);
    	shift.setOrgId(orgId);
    	shift.setWarehouseId(warehouseId);
    	shift.setOpPerson(loginUser.getUserId());
    	shift.setOpTime(now);
    	// 设置移位单号
    	String shiftNo = context.getStrategy4No(orgId, LoginUtil.getWareHouseId()).getShiftInternalNo(orgId);
    	shift.setShiftNo(shiftNo);
    	shift.setShiftType(Constant.SHIFT_TYPE_INWAREHOUSE);
    	// 保存移位单
    	this.dao.insertSelective(shift);
		List<MessageData> outList = new ArrayList<MessageData>();
		List<MessageData> inList = new ArrayList<MessageData>();
    	// --移位单详情循环
		List<InvShiftDetailVO> shiftDetailVoList = shiftVo.getListInvShiftDetailVO();
		for (int i = 0; i < shiftDetailVoList.size(); i++) {
			InvShiftDetailVO shiftDetailVo = shiftDetailVoList.get(i);
			InvShiftDetail invShiftDetail = shiftDetailVo.getInvShiftDetail();
			String outLocationNo = shiftDetailVo.getOutLocationNo();
			String inLocationNo = shiftDetailVo.getInLocationNo();
			String skuNo = shiftDetailVo.getSkuNo();
//			String skuBarCode = shiftDetailVo.getSkuBarCode();
			String batchNo = invShiftDetail.getBatchNo();
			Double shiftQty = invShiftDetail.getRealShiftQty();
			if (StringUtil.equals(outLocationNo,inLocationNo)) {
				throw new BizException("err_shift_location_same");
			}
			// --校验字段
			String outLocationId = null;
			if (outLocationNo == null) {
				throw new BizException("err_shift_out_location_null");
			} else {
				MetaLocation location = locationService.findLocByNo(outLocationNo, null);
				if (location == null) {
					throw new BizException("err_shift_out_location_not_exists", outLocationNo);
				}
				if (location.getLocationType() == Constant.LOCATION_TYPE_TEMPSTORAGE) {
					throw new BizException("err_shift_out_location_temp", outLocationNo);
				}
				outLocationId = location.getLocationId();
			}
			String inLocationId = null;
			if (inLocationNo == null) {
				throw new BizException("err_shift_in_location_null");
			} else {
				MetaLocation location = locationService.findLocByNo(inLocationNo, null);
				if (location == null) {
					throw new BizException("err_shift_in_location_not_exists", inLocationNo);
				}
				if (location.getLocationType() == Constant.LOCATION_TYPE_TEMPSTORAGE) {
					throw new BizException("err_shift_in_location_temp", inLocationNo);
				}
				inLocationId = location.getLocationId();
			}
			String skuId = null;
			if (skuNo == null) {
				throw new BizException("err_shift_sku_null", skuNo);
			} else {
				MetaSku queryEntity = new MetaSku();
//				queryEntity.setSkuBarCode(skuBarCode);
				queryEntity.setSkuNo(skuNo);
				MetaSku sku = skuService.query(queryEntity);
				if (sku == null) {
					throw new BizException("err_shift_sku_not_exists", skuNo);
				}
				skuId = sku.getSkuId();
			}
			if (shiftQty == null || shiftQty < 1) {
				throw new BizException("err_shift_qty_min", skuNo);
			}
			// --检测重复
			for (int j = i+1; j < shiftDetailVoList.size(); j++) {
				InvShiftDetailVO invShiftDetailVO2 = shiftDetailVoList.get(j);
				String outLocationNo2 = invShiftDetailVO2.getOutLocationNo();
				String inLocationNo2 = invShiftDetailVO2.getInLocationNo();
				String skuNo2 = invShiftDetailVO2.getSkuNo();
				String batchNo2 = invShiftDetailVO2.getInvShiftDetail().getBatchNo();
				if (StringUtil.equals(inLocationId, inLocationNo2) 
						&& StringUtil.equals(outLocationId, outLocationNo2) 
						&& StringUtil.equals(skuNo, skuNo2) 
						&& StringUtil.equals(batchNo, batchNo2)) {
					throw new BizException("err_common_repeat");
				}
			}
			// 组装字段
			invShiftDetail.setShiftId(shiftId);
			invShiftDetail.setShiftDetailId(IdUtil.getUUID());
			invShiftDetail.setOutLocation(outLocationId);
			invShiftDetail.setInLocation(inLocationId);
			invShiftDetail.setSkuId(skuId);
			invShiftDetail.setRealShiftQty(shiftQty);
			invShiftDetail.setCreatePerson(loginUser.getUserId());
			invShiftDetail.setCreateTime(now);
			invShiftDetail.setUpdatePerson(loginUser.getUserId());
			invShiftDetail.setUpdateTime(now);
			invShiftDetail.setOrgId(orgId);
			invShiftDetail.setWarehouseId(warehouseId);
			// 保存移位单详情
			this.detailDao.insertSelective(invShiftDetail);
			// --开始准备库存字段
			InvStockVO stockVo = new InvStockVO();
			InvStock stock = new InvStock();
			stock.setSkuId(skuId);
			stock.setLocationId(outLocationId);
			stock.setBatchNo(batchNo);
			stock.setOrgId(orgId);
			stock.setWarehouseId(warehouseId);
			stockVo.setInvStock(stock);
			stockVo.setFindNum(shiftQty);
			//预设日志对象
			InvLog invLog = new InvLog();
			stockVo.setInvLog(invLog);
			invLog.setInvoiceBill(shiftNo);
			invLog.setOpPerson(loginUser.getUserId());
			invLog.setLogType(Constant.STOCK_LOG_TYPE_SHIFT);
			invLog.setSkuId(skuId);
			//出库-库存减少（出库位已设置，不需重复设置）
			invLog.setOpType(Constant.STOCK_LOG_OP_TYPE_OUT);
			invLog.setQty(-shiftQty);
			//设置重量和体积
			MetaSku sku = skuService.get(skuId);
			invLog.setVolume(NumberUtil.mul(-shiftQty, sku.getPerVolume() == null ? 0 : sku.getPerVolume()));
			invLog.setWeight(NumberUtil.mul(-shiftQty, sku.getPerWeight() == null ? 0 : sku.getPerWeight()));
			invLog.setLocationId(outLocationId);
			this.stockService.outStock(stockVo);
			//入库-库存增加
			stock.setLocationId(inLocationId);
			invLog.setOpType(Constant.STOCK_LOG_OP_TYPE_IN);
			invLog.setQty(shiftQty);
			//设置重量和体积
			invLog.setVolume(NumberUtil.mul(shiftQty, sku.getPerVolume() == null ? 0 : sku.getPerVolume()));
			invLog.setWeight(NumberUtil.mul(shiftQty, sku.getPerWeight() == null ? 0 : sku.getPerWeight()));
			invLog.setLocationId(inLocationId);
			this.stockService.inStock(stockVo);

			//辅助系统
			MetaLocation in = FqDataUtils.getLocById(locationService, inLocationId);
			MessageData data1 = new MessageData(in.getLocationNo(), in.getLocationColumn(), sku.getSkuNo(), NumberUtil.rounded(Math.abs(shiftQty), 0), sku.getMeasureUnit(), "I");
			inList.add(data1);

			MetaLocation out = FqDataUtils.getLocById(locationService, outLocationId);
			MessageData data2 = new MessageData(out.getLocationNo(), out.getLocationColumn(), sku.getSkuNo(), NumberUtil.rounded(Math.abs(shiftQty), 0), sku.getMeasureUnit(), "E");
			outList.add(data2);
			}
			//发送辅助系统
			//出库
//			context.getStrategy4Assis().request(orgId, IAssisService.BILL_TYPE_SHIFT, shiftId, true, outList);
			//入库
//			context.getStrategy4Assis().request(orgId, IAssisService.BILL_TYPE_SHIFT, shiftId, false, inList);
		}
	@Override
	public InvShiftVO importShift(InvShiftVO paramVo) throws Exception {
		InvShiftVO retVo = this.saveOrUpdate(paramVo);
		return retVo;
	}
	
	@Override
	@Transactional(readOnly=false)
	public void confirmBatch (InvShiftVO vo) throws Exception {
		Principal loginUser = LoginUtil.getLoginUser();
    	// 验证用户是否登录
    	if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
    		throw new BizException("valid_common_user_no_login");
    	}
		if (vo == null) {
			throw new BizException("err_shift_null");
		}
		List<String> shiftIdList = vo.getShiftIdList();
		if (shiftIdList == null || shiftIdList.isEmpty()) {
			throw new BizException("err_shift_list_null");
		}
    	//获取登录信息中的企业和仓库
    	String orgId = loginUser.getOrgId();
    	String warehouseId = LoginUtil.getWareHouseId();
		String opPerson = vo.getInvShift().getOpPerson();
		for (String shiftId : shiftIdList) {
			InvShift shift = this.dao.selectByPrimaryKey(shiftId);
			if (shift == null) {
				throw new BizException("err_shift_null");
			}
			if (shift.getShiftStatus() != Constant.SHIFT_STATUS_ACTIVE  && shift.getShiftStatus() != Constant.SHIFT_STATUS_WORKING) {
				throw new BizException("err_shift_status_cannot_finish");
			}
			String shiftNo = shift.getShiftNo();
			
			InvShiftVO oldShiftVo = this.view(shiftId);
			Map<String, String> map = new HashMap<String, String>();
			for (InvShiftDetailVO detailVo :  oldShiftVo.getListInvShiftDetailVO()) {
				map.put(detailVo.getInvShiftDetail().getShiftDetailId(), detailVo.getInvShiftDetail().getInLocation());
			}
			
			List<InvShiftDetailVO> shiftDetailVoList = oldShiftVo.getListInvShiftDetailVO();
			if (shiftDetailVoList != null && !shiftDetailVoList.isEmpty()) {
				for (InvShiftDetailVO shiftDetailVo : shiftDetailVoList) {
					InvShiftDetail shiftDetailCon = shiftDetailVo.getInvShiftDetail();
					//校验字段
					String shiftDetailId = shiftDetailCon.getShiftDetailId();
					Double actShiftQty = shiftDetailCon.getShiftQty();
					String inLocationId = shiftDetailCon.getInLocation();
					String oldInLocation = map.get(shiftDetailId);
					Double planShiftQty = shiftDetailCon.getShiftQty();
					String packId = shiftDetailCon.getPackId();
					InvStockVO stockVo = new InvStockVO();
					InvStock stock = new InvStock();
					String skuId = shiftDetailCon.getSkuId();
					MetaSku sku = skuService.get(skuId);
					stock.setSkuId(skuId);
					String outLocation = shiftDetailCon.getOutLocation();
					stock.setLocationId(outLocation);
					stock.setBatchNo(shiftDetailCon.getBatchNo());
					stock.setOrgId(orgId);
					stock.setWarehouseId(warehouseId);
					stock.setPackId(packId);
					stockVo.setInvStock(stock);
					stockVo.setFindNum(planShiftQty);
					//解锁预分配库存
					this.stockService.unlockOutStock(stockVo);
					//减少预分配库容
					this.locationService.addPreusedCapacity(skuId, oldInLocation, packId, new BigDecimal(-planShiftQty * sku.getPerVolume()));
					//预设日志对象
					InvLog invLog = new InvLog();
					stockVo.setInvLog(invLog);
					stockVo.setFindNum(actShiftQty);
					invLog.setInvoiceBill(shiftNo);
					invLog.setOpPerson(opPerson);
					invLog.setLogType(Constant.STOCK_LOG_TYPE_SHIFT);
					invLog.setSkuId(skuId);
					//出库-库存减少（出库位已设置，不需重复设置）
					stockVo.setFindNum(actShiftQty);
					invLog.setOpType(Constant.STOCK_LOG_OP_TYPE_OUT);
					invLog.setQty(-actShiftQty);
					//设置重量和体积
					invLog.setVolume(NumberUtil.mul(-actShiftQty, sku.getPerVolume() == null ? 0 : sku.getPerVolume()));
	    			invLog.setWeight(NumberUtil.mul(-actShiftQty, sku.getPerWeight() == null ? 0 : sku.getPerWeight()));
	    			
					invLog.setLocationId(outLocation);
					this.stockService.outStock(stockVo);
					//入库-库存增加
					stock.setLocationId(inLocationId);
					invLog.setOpType(Constant.STOCK_LOG_OP_TYPE_IN);
					invLog.setQty(actShiftQty);
					//设置重量和体积
					invLog.setVolume(NumberUtil.mul(actShiftQty, sku.getPerVolume() == null ? 0 : sku.getPerVolume()));
	    			invLog.setWeight(NumberUtil.mul(actShiftQty, sku.getPerWeight() == null ? 0 : sku.getPerWeight()));
	    			
					invLog.setLocationId(inLocationId);
					this.stockService.inStock(stockVo);
					
					//发送辅助系统
					//出库
					MetaLocation out = FqDataUtils.getLocById(locationService, outLocation);
					MessageData data1 = new MessageData(out.getLocationNo(), out.getLocationColumn(), sku.getSkuNo(), NumberUtil.rounded(actShiftQty, 0), sku.getMeasureUnit(), "E");
//					context.getStrategy4Assis().request(orgId, IAssisService.BILL_TYPE_SHIFT, shiftId, true, data1);
					//入库
					MetaLocation in = FqDataUtils.getLocById(locationService, inLocationId);
					MessageData data2 = new MessageData(in.getLocationNo(), in.getLocationColumn(), sku.getSkuNo(), NumberUtil.rounded(actShiftQty, 0), sku.getMeasureUnit(), "I");
//					context.getStrategy4Assis().request(orgId, IAssisService.BILL_TYPE_SHIFT, shiftId, false, data2);
				}
			} else {
				throw new BizException("err_shift_detail_null");
			}
			InvShift shiftRecord = new InvShift();
			shiftRecord.setShiftId(shiftId);
			shiftRecord.setShiftStatus(Constant.SHIFT_STATUS_FINISH);
			shiftRecord.setOpPerson(opPerson);
			shiftRecord.setOpTime(new Date());
			this.dao.updateByPrimaryKeySelective(shiftRecord);
			//完成任务单
			this.taskService.finishByOpId(shiftId, loginUser.getUserId());
		}
	}
}