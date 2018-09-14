package com.yunkouan.wms.modules.inv.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yunkouan.exception.BizException;
import com.yunkouan.saas.modules.sys.service.ISysParamService;
import com.yunkouan.saas.modules.sys.service.IUserService;
import com.yunkouan.saas.modules.sys.service.IWarehouseExtlService;
import com.yunkouan.util.NumberUtil;

import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.common.service.BaseService;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.IdUtil;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.inv.dao.IStockDao;
import com.yunkouan.wms.modules.inv.dao.InvRejectsDestoryDao;
import com.yunkouan.wms.modules.inv.dao.InvRejectsDestoryDetailDao;
import com.yunkouan.wms.modules.inv.entity.InvLog;
import com.yunkouan.wms.modules.inv.entity.InvRejectsDestory;
import com.yunkouan.wms.modules.inv.entity.InvRejectsDestoryDetail;
import com.yunkouan.wms.modules.inv.entity.InvStock;
import com.yunkouan.wms.modules.inv.service.IRejectsDestoryDetailService;
import com.yunkouan.wms.modules.inv.service.IRejectsDestoryService;
import com.yunkouan.wms.modules.inv.service.IStockService;
import com.yunkouan.wms.modules.inv.vo.InvRejectsDestoryDetailVO;
import com.yunkouan.wms.modules.inv.vo.InvRejectsDestoryVO;
import com.yunkouan.wms.modules.meta.entity.MetaSku;
import com.yunkouan.wms.modules.meta.service.IAreaService;
import com.yunkouan.wms.modules.meta.service.ILocationExtlService;
import com.yunkouan.wms.modules.meta.service.IPackService;
import com.yunkouan.wms.modules.meta.service.ISkuService;
import com.yunkouan.wms.modules.rec.service.IASNExtlService;

/**
 * 移位服务实现类
 */
@Service
public class RejectsDestoryServiceImpl extends BaseService implements IRejectsDestoryService {
	/**
     * 数据层接口
     */
	@Autowired
    private InvRejectsDestoryDao dao;
	/**
     * 数据层接口
     */
	@Autowired
    private InvRejectsDestoryDetailDao detailDao;
	@Autowired
    private IRejectsDestoryDetailService detailService;
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
	@Autowired
    private IStockDao stockDao;
	
	@Autowired
	private ISysParamService paramService;

	/**
     * Default constructor
     */
    public RejectsDestoryServiceImpl() {
    }

	/**
	 * 日志对象
	 */
	private Logger log = LoggerFactory.getLogger(getClass());
    /**
     * 查询不良品销毁单列表
     * @param vo
     * @return
     * @Description 
     * @version 2017年2月16日 下午1:24:22<br/>
     * @author 王通<br/>
     * @throws Exception 
     */
	@Override
	@Transactional(readOnly=false)
    public Page<InvRejectsDestoryVO> list(InvRejectsDestoryVO vo) throws Exception {
		Principal loginUser = LoginUtil.getLoginUser();
    	// 验证用户是否登录
    	if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
    		throw new BizException("valid_common_user_no_login");
    	}
    	if ( vo == null ) {
    		vo = new InvRejectsDestoryVO();
		}
		if ( vo.getInvRejectsDestory() == null ) {
			vo.setInvRejectsDestory(new InvRejectsDestory());
		}
		Page<InvRejectsDestoryVO> page = null;
		// 查询不良品销毁单列表 
		page = PageHelper.startPage(vo.getCurrentPage()+1, vo.getPageSize());
		List<InvRejectsDestory> listInvRejectsDestory = this.dao.selectByExample(vo.getExample());
		if ( listInvRejectsDestory == null || listInvRejectsDestory.isEmpty() ) {
			return null;
		}
		// 组装信息
		List<InvRejectsDestoryVO> listVO = chg(listInvRejectsDestory);
		page.clear();
		page.addAll(listVO);
		return page;
    }
	
	private List<InvRejectsDestoryVO> chg(List<InvRejectsDestory> list) {
		List<InvRejectsDestoryVO> voList = new ArrayList<InvRejectsDestoryVO>();
		for (InvRejectsDestory entity: list) {
			voList.add(this.chg(entity));
		}
		return voList;
	}
	
	private InvRejectsDestoryVO chg(InvRejectsDestory entity) {
		InvRejectsDestoryVO vo = new InvRejectsDestoryVO(entity);
		List<InvRejectsDestoryDetailVO> detailList = null;
		try {
			detailList = detailService.listByParentId(entity.getRejectsDestoryId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		vo.setListInvRejectsDestoryDetailVO(detailList);
		vo.setApplyPersonName(userService.get(entity.getApplyPerson()).getUserName());
		vo.setUpdateUserName(userService.get(entity.getUpdatePerson()).getUserName());
		vo.setStatusName(paramService.getValue("REJECTS_STATUS", entity.getStatus()));
		return vo;
	}
	
	/**
     * 查看不良品销毁单详情
     * @param vo
     * @return 
     * @Description 
     * @version 2017年2月16日 下午1:58:31<br/>
     * @author 王通<br/>
     */
    @Override
	@Transactional(readOnly=false)
    public InvRejectsDestoryVO view(String id) throws Exception {
		Principal loginUser = LoginUtil.getLoginUser();
    	// 验证用户是否登录
    	if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
    		throw new BizException("valid_common_user_no_login");
    	}
		if ( id == null ) {
			throw new BizException("err_reject_null");
		}
		// 查询不良品销毁单详情 
		InvRejectsDestory reject = this.dao.selectByPrimaryKey(id);
		if ( reject == null ) {
			throw new BizException("err_reject_null");
		}
		// 组装信息
		InvRejectsDestoryVO rejectVo = chg(reject);
		return rejectVo;
    }

    /**
     * 保存或修改不良品销毁单
     * @param vo
     * @return
     * @Description 
     * @version 2017年2月16日 下午5:46:50<br/>
     * @author 王通<br/>
     * @throws Exception 
     */
	@Override
	@Transactional(readOnly = false)
	public InvRejectsDestoryVO saveOrUpdate(InvRejectsDestoryVO vo) throws Exception {
		Principal loginUser = LoginUtil.getLoginUser();
    	String orgId = loginUser.getOrgId();
    	// 验证用户是否登录
    	if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
    		throw new BizException("valid_common_user_no_login");
    	}
		if ( vo == null || vo.getInvRejectsDestory() == null ) {
    		log.error("%s.saveOrUpdate --> param is null!", getClass().getName());
    		throw new BizException("err_reject_null");
    	}
		InvRejectsDestoryVO invRejectVo = new InvRejectsDestoryVO();
    	InvRejectsDestory reject = vo.getInvRejectsDestory();
    	String rejectId = reject.getRejectsDestoryId();
    	if (StringUtil.isEmpty(rejectId)) {
    		// 新增方法
        	// 设置uuid
    		rejectId = IdUtil.getUUID();
        	reject.setRejectsDestoryId(rejectId);
        	// 设置创建人/修改人/企业/仓库
        	reject.setCreatePerson(loginUser.getUserId());
        	reject.setCreateTime(new Date());
        	reject.setUpdatePerson(loginUser.getUserId());
        	reject.setUpdateTime(new Date());
        	reject.setOrgId(orgId);
        	reject.setWarehouseId(LoginUtil.getWareHouseId());
        	// 设置不良品销毁单号
        	reject.setFormNo(context.getStrategy4No(orgId, LoginUtil.getWareHouseId()).getApplyRejectsNo(orgId));
        	// 设置状态
        	reject.setStatus(Constant.STATUS_OPEN);
        	invRejectVo.setInvRejectsDestory(reject);
        	//先保存主单
        	this.dao.insertSelective(reject);
        	List<InvRejectsDestoryDetailVO> listRejectDetailVo = vo.getListInvRejectsDestoryDetailVO();
        	if ( listRejectDetailVo != null && !listRejectDetailVo.isEmpty() ) {
//        		List<InvRejectsDestoryDetail> listRejectDetail = new ArrayList<InvRejectsDestoryDetail>();
        		//增加不良品销毁单明细
        		for (int i = 0; i < listRejectDetailVo.size(); i++) {
        			InvRejectsDestoryDetailVO invRejectDetailVO = listRejectDetailVo.get(i);
        			InvRejectsDestoryDetail invRejectDetail = invRejectDetailVO.getInvRejectsDestoryDetail();
        			String stockId = invRejectDetail.getStockId();
        			//验证库存id是否存在
        			InvStock stock = this.stockService.view(stockId);
        			if (stock == null) {
        				throw new BizException("err_reject_stock_null");
        			}
        			Double opNumber = invRejectDetail.getOpNumber();
        			if (opNumber < 0) {
                		throw new BizException("err_reject_qty_min");
        			} else if (opNumber > NumberUtil.sub(stock.getStockQty(),stock.getPickQty())) {
        				throw new BizException("err_reject_qty_canuse");
        			}
        			//设置主单号
        			invRejectDetail.setRejectsDestoryId(rejectId);
        			//设置自身唯一单号
        			invRejectDetail.setRejectsDestoryDetailId(IdUtil.getUUID());
        			//其他设置
        			invRejectDetail.setOrgId(loginUser.getOrgId());
        			invRejectDetail.setWarehouseId(LoginUtil.getWareHouseId());
        			invRejectDetail.setCreatePerson(loginUser.getUserId());
        			invRejectDetail.setCreateTime(new Date());
        			invRejectDetail.setUpdatePerson(loginUser.getUserId());
        			invRejectDetail.setUpdateTime(new Date());
//        			listRejectDetail.add(invRejectDetail);
                	this.detailDao.insertSelective(invRejectDetail);
        		}
//            	invRejectVo.setListInvRejectsDestoryDetailVO(listRejectDetailVo);
    		}
    	} else {
    		// 修改方法
    		// 检查不良品销毁单是否存在
    		InvRejectsDestoryVO findReject = this.view(rejectId);
    		if (findReject == null || StringUtil.isEmpty(findReject.getInvRejectsDestory().getRejectsDestoryId())) {
    			throw new BizException("err_reject_null");
    		} else if (!findReject.getInvRejectsDestory().getStatus().equals(Constant.STATUS_OPEN)) {
    			throw new BizException("err_reject_status_not_open");
    		}
    		if (invRejectVo.getChangeFormNo()) {
            	reject.setFormNo(context.getStrategy4No(orgId, LoginUtil.getWareHouseId()).getApplyRejectsNo(orgId));
    		}
    		// 设置修改人/企业/仓库
        	reject.setUpdatePerson(loginUser.getUserId());
        	reject.setUpdateTime(new Date());
        	invRejectVo.setInvRejectsDestory(reject);
        	this.dao.updateByPrimaryKeySelective(reject);
        	List<InvRejectsDestoryDetailVO> listRejectDetailVo = vo.getListInvRejectsDestoryDetailVO();
    		//清空不良品销毁单明细
    		this.detailDao.emptyDetail(rejectId);
        	if ( listRejectDetailVo != null && !listRejectDetailVo.isEmpty() ) {
        		//添加不良品销毁单明细
        		for (int i = 0; i < listRejectDetailVo.size(); i++) {
        			InvRejectsDestoryDetailVO invRejectDetailVO = listRejectDetailVo.get(i);
        			InvRejectsDestoryDetail invRejectDetail = invRejectDetailVO.getInvRejectsDestoryDetail();
        			String stockId = invRejectDetail.getStockId();
        			//验证库存id是否存在
        			InvStock stock = this.stockService.view(stockId);
        			if (stock == null) {
        				throw new BizException("err_reject_stock_null");
        			}
        			Double opNumber = invRejectDetail.getOpNumber();
        			if (opNumber < 0) {
                		throw new BizException("err_reject_qty_min");
        			} else if (opNumber > NumberUtil.add(stock.getStockQty(),stock.getPickQty())) {
        				throw new BizException("err_reject_qty_canuse");
        			}
        			//设置主单号
        			invRejectDetail.setRejectsDestoryId(rejectId);
        			//设置自身唯一单号
        			invRejectDetail.setRejectsDestoryDetailId(IdUtil.getUUID());
        			//其他设置
        			invRejectDetail.setOrgId(loginUser.getOrgId());
        			invRejectDetail.setWarehouseId(LoginUtil.getWareHouseId());
        			invRejectDetail.setCreatePerson(loginUser.getUserId());
        			invRejectDetail.setCreateTime(new Date());
        			invRejectDetail.setUpdatePerson(loginUser.getUserId());
        			invRejectDetail.setUpdateTime(new Date());
//        			listRejectDetail.add(invRejectDetail);
                	this.detailDao.insertSelective(invRejectDetail);
        		}
//            	this.detailDao.add(listRejectDetail);
//            	invRejectVo.setListInvRejectsDestoryDetailVO(listRejectDetailVo);
    		}
    	}
		return invRejectVo;
	}
	/**
	 * 生效不良品销毁单
	 * @param rejectIdList
	 * @throws Exception
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月2日 上午9:43:05<br/>
	 * @author 王通<br/>
	 */
	@Override
	@Transactional(readOnly=false)
	public void enable( InvRejectsDestoryVO vo ) throws Exception {
		Principal loginUser = LoginUtil.getLoginUser();
    	// 验证用户是否登录
    	if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
    		throw new BizException("valid_common_user_no_login");
    	}
		if (vo == null || vo.getIdList() == null || vo.getIdList().isEmpty()) {
			throw new BizException("err_reject_null");
		}
		List<String> rejectIdList = vo.getIdList();
    	//获取登录信息中的企业和仓库
		for (String rejectId : rejectIdList) {
			InvRejectsDestoryVO rejectVo = this.view(rejectId);
			if (rejectVo == null) {
				throw new BizException("err_reject_null");
			}
			InvRejectsDestory reject = rejectVo.getInvRejectsDestory();
			if (reject.getStatus() != Constant.STATUS_OPEN) {
				throw new BizException("err_reject_status_not_open");
			}
			List<InvRejectsDestoryDetailVO> rejectDetailVoList = rejectVo.getListInvRejectsDestoryDetailVO();
			if (rejectDetailVoList != null && !rejectDetailVoList.isEmpty()) {
				for (InvRejectsDestoryDetailVO rejectDetailVo : rejectDetailVoList) {
					InvRejectsDestoryDetail rejectDetail = rejectDetailVo.getInvRejectsDestoryDetail();
	    			//验证移位数量出库库存--生效时验证
        			InvStock stock = this.stockService.view(rejectDetail.getStockId());
        			if (stock == null) {
        				throw new BizException("err_reject_stock_null");
        			}
        			Double opNumber = rejectDetail.getOpNumber();
        			if (opNumber < 0) {
                		throw new BizException("err_reject_qty_min");
        			} else if (opNumber > NumberUtil.sub(stock.getStockQty() ,stock.getPickQty())) {
        				throw new BizException("err_reject_qty_canuse");
        			}
        			stock.setPickQty(NumberUtil.add(stock.getPickQty() , opNumber));
        			this.stockDao.updateByPrimaryKeySelective(stock);
				}
			} else {
				throw new BizException("err_reject_detail_null");
			}
			InvRejectsDestory rejectRecord = new InvRejectsDestory();
			rejectRecord.setRejectsDestoryId(rejectId);
			rejectRecord.setUpdatePerson(loginUser.getUserId());
			rejectRecord.setStatus(Constant.STATUS_ACTIVE);
			this.dao.updateByPrimaryKeySelective(rejectRecord);
		}
	}

	/**
	 * @param rejectIdList
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月7日 下午4:46:25<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
	@Override
	@Transactional(readOnly=false)
	public void disable(InvRejectsDestoryVO vo) throws Exception {
		Principal loginUser = LoginUtil.getLoginUser();
    	// 验证用户是否登录
    	if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
    		throw new BizException("valid_common_user_no_login");
    	}
		if (vo == null || vo.getIdList() == null || vo.getIdList().isEmpty()) {
			throw new BizException("err_reject_null");
		}
		List<String> rejectIdList = vo.getIdList();
		for (String rejectId : rejectIdList) {
			InvRejectsDestoryVO rejectVo = this.view(rejectId);
			if (rejectVo == null) {
				throw new BizException("err_reject_null");
			}
			InvRejectsDestory reject = rejectVo.getInvRejectsDestory();
			if (reject.getStatus() != Constant.STATUS_ACTIVE) {
				throw new BizException("err_reject_status_not_active");
			}
			List<InvRejectsDestoryDetailVO> rejectDetailVoList = rejectVo.getListInvRejectsDestoryDetailVO();
			if (rejectDetailVoList != null && !rejectDetailVoList.isEmpty()) {
				for (InvRejectsDestoryDetailVO rejectDetailVo : rejectDetailVoList) {
					InvRejectsDestoryDetail rejectDetail = rejectDetailVo.getInvRejectsDestoryDetail();
	    			//验证移位数量出库库存--生效时验证
	    			InvStock stock = this.stockService.view(rejectDetail.getStockId());
	    			Double opNumber = rejectDetail.getOpNumber();
        			if (opNumber < 0) {
                		throw new BizException("err_reject_qty_min");
        			} else if (opNumber > stock.getPickQty()) {
        				throw new BizException("err_reject_pickqty_canuse");
        			}
	    			//解锁预分配库存
        			stock.setPickQty(NumberUtil.sub(stock.getPickQty(),opNumber));
        			this.stockDao.updateByPrimaryKeySelective(stock);
				}
			}
			InvRejectsDestory rejectRecord = new InvRejectsDestory();
			rejectRecord.setRejectsDestoryId(rejectId);
			rejectRecord.setUpdatePerson(loginUser.getUserId());
			rejectRecord.setUpdateTime(new Date());
			rejectRecord.setStatus(Constant.STATUS_OPEN);
			this.dao.updateByPrimaryKeySelective(rejectRecord);
		}
	}
	
	/**
	 * @param rejectIdList
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月7日 下午4:46:25<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
	@Override
	@Transactional(readOnly=false)
	public void cancel(InvRejectsDestoryVO vo) throws Exception {
		Principal loginUser = LoginUtil.getLoginUser();
    	// 验证用户是否登录
    	if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
    		throw new BizException("valid_common_user_no_login");
    	}
		if (vo == null || vo.getIdList() == null || vo.getIdList().isEmpty()) {
			throw new BizException("err_reject_null");
		}
		List<String> rejectIdList = vo.getIdList();
    	//获取登录信息中的企业和仓库
		for (String rejectId : rejectIdList) {
			InvRejectsDestoryVO rejectVo = this.view(rejectId);
			if (rejectVo == null) {
				throw new BizException("err_reject_null");
			}
			InvRejectsDestory reject = rejectVo.getInvRejectsDestory();
			if (reject.getStatus() != Constant.STATUS_OPEN) {
				throw new BizException("err_reject_status_cannot_cancel");
			}
			InvRejectsDestory rejectRecord = new InvRejectsDestory();
			rejectRecord.setRejectsDestoryId(rejectId);
			rejectRecord.setStatus(Constant.STATUS_CANCEL);
			this.dao.updateByPrimaryKeySelective(rejectRecord);
		}
	}
	/**
	 * @param vo
	 * @return
	 * @required rejectId,rejectDetail.rejectDetail.rejectQty,rejectDetail.realRejectQty
	 * @optional  
	 * @Description 
     * 1、解除锁定-预分配   
     * 2、创建调账完毕的盈亏调账单			
     * 3、出库-库存减少
	 * @version 2017年3月7日 下午4:46:25<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
	@Override
	@Transactional(readOnly=false)
	public void confirm (InvRejectsDestoryVO rejectVo) throws Exception {
		Principal loginUser = LoginUtil.getLoginUser();
    	// 验证用户是否登录
    	if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
    		throw new BizException("valid_common_user_no_login");
    	}
		if (rejectVo == null) {
			throw new BizException("err_reject_null");
		}
		String rejectId = rejectVo.getInvRejectsDestory().getRejectsDestoryId();
		if (rejectId == null) {
			throw new BizException("err_reject_null");
		}
		rejectVo = this.view(rejectId);
		if (rejectVo.getInvRejectsDestory().getStatus() != Constant.STATUS_ACTIVE) {
			throw new BizException("err_reject_status_cannot_finish");
		}
		String formNo = rejectVo.getInvRejectsDestory().getFormNo();
		
		List<InvRejectsDestoryDetailVO> rejectDetailVoList = rejectVo.getListInvRejectsDestoryDetailVO();
		if (rejectDetailVoList != null && !rejectDetailVoList.isEmpty()) {
			for (InvRejectsDestoryDetailVO rejectDetailVo : rejectDetailVoList) {
				InvRejectsDestoryDetail rejectDetailCon = rejectDetailVo.getInvRejectsDestoryDetail();
				String stockId = rejectDetailCon.getStockId();
				//解锁预分配库存
				InvStock stock = this.stockService.view(stockId);
				String skuId = stock.getSkuId();
				MetaSku sku = skuService.get(skuId);
    			Double opNumber = rejectDetailCon.getOpNumber();
    			if (opNumber < 0) {
            		throw new BizException("err_reject_qty_min");
    			} else if (opNumber > stock.getPickQty()) {
    				throw new BizException("err_reject_pickqty_canuse");
    			}
    			//解锁预分配库存,减少库存
    			stock.setPickQty(NumberUtil.sub(stock.getPickQty(), opNumber));
    			stock.setStockQty(NumberUtil.sub(stock.getStockQty(), opNumber));
    			this.stockDao.updateByPrimaryKeySelective(stock);
				//预设日志对象
				InvLog invLog = new InvLog();
				invLog.setInvoiceBill(formNo);
				invLog.setOpPerson(loginUser.getUserId());
				invLog.setLogType(Constant.STOCK_LOG_TYPE_BAD_DESTORY);
				invLog.setSkuId(stock.getSkuId());
				//出库-库存减少（出库位已设置，不需重复设置）
				invLog.setOpType(Constant.STOCK_LOG_OP_TYPE_OUT);
				invLog.setQty(-opNumber);
				//设置重量和体积
				invLog.setVolume(NumberUtil.mul(-opNumber, sku.getPerVolume() == null ? 0 : sku.getPerVolume()));
    			invLog.setWeight(NumberUtil.mul(-opNumber, sku.getPerWeight() == null ? 0 : sku.getPerWeight()));
				invLog.setLocationId(stock.getLocationId());
				this.stockService.insertLog(invLog, loginUser);
			}
		} else {
			throw new BizException("err_reject_detail_null");
		}
		InvRejectsDestory rejectRecord = new InvRejectsDestory();
		rejectRecord.setRejectsDestoryId(rejectId);
		rejectRecord.setStatus(Constant.STATUS_FINISH);
		rejectRecord.setUpdatePerson(loginUser.getUserId());
		rejectRecord.setUpdateTime(new Date());
		rejectRecord.setApplyPerson(loginUser.getUserId());
		rejectRecord.setApplyDate(new Date());
		this.dao.updateByPrimaryKeySelective(rejectRecord);
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
	public InvRejectsDestoryVO saveAndEnable(InvRejectsDestoryVO vo) throws Exception {
		InvRejectsDestoryVO rejectVo = this.saveOrUpdate(vo);
    	String rejectId = vo.getInvRejectsDestory().getRejectsDestoryId();
        List<String> idList = new ArrayList<String>();
        idList.add(rejectId);
        vo.setIdList(idList);
        this.enable(vo);
		return rejectVo;
	}
	
	@Override
	public InvRejectsDestoryVO print(String id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<InvRejectsDestoryVO> list4Print(InvRejectsDestoryVO vo) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}