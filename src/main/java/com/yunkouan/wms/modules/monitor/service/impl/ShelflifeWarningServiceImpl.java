package com.yunkouan.wms.modules.monitor.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yunkouan.exception.BizException;
import com.yunkouan.saas.modules.sys.entity.MetaArea;
import com.yunkouan.saas.modules.sys.entity.MetaLocation;
import com.yunkouan.saas.modules.sys.entity.SysUser;
import com.yunkouan.saas.modules.sys.service.ISysParamService;
import com.yunkouan.saas.modules.sys.service.IUserService;

import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.common.constant.CacheName;
import com.yunkouan.wms.common.service.BaseService;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.inv.entity.InvStock;
import com.yunkouan.wms.modules.inv.service.IStockService;
import com.yunkouan.wms.modules.inv.vo.InvStockVO;
import com.yunkouan.wms.modules.meta.entity.MetaSku;
import com.yunkouan.wms.modules.meta.service.IAreaExtlService;
import com.yunkouan.wms.modules.meta.service.ILocationExtlService;
import com.yunkouan.wms.modules.meta.service.IMerchantService;
import com.yunkouan.wms.modules.meta.service.ISkuService;
import com.yunkouan.wms.modules.monitor.dao.IWarningHandlerDao;
import com.yunkouan.wms.modules.monitor.entity.WarningHandler;
import com.yunkouan.wms.modules.monitor.service.IShelflifeWarningService;
import com.yunkouan.wms.modules.monitor.vo.ShelflifeWarningVO;
import com.yunkouan.wms.modules.monitor.vo.WarningHandlerVO;

/**
 * 保质期预警业务实现类
 */
@Service
public class ShelflifeWarningServiceImpl extends BaseService implements IShelflifeWarningService {

	@Autowired
	private ISysParamService paramService;

	/**
	 * Default constructor
	 */
	public ShelflifeWarningServiceImpl() {
	}

	/**
	 * dao对象
	 */
	@Autowired
	private IWarningHandlerDao dao;
	/**
	 * 外部服务-用户
	 */
	@Autowired
	IUserService userService;
	/**
	 * 外部服务-库存
	 */
	@Autowired
	IStockService stockService;
	/**
	 * 外部服务-货商
	 */
	@Autowired
	IMerchantService merchantService;
	/**
	 * 外部服务-库位
	 */
	@Autowired
	ILocationExtlService locationService;
	/**
	 * 外部服务-库区
	 */
	@Autowired
	IAreaExtlService areaService;
	/**
	 * 外部服务-货品
	 */
	@Autowired
	ISkuService skuService;



	/**
	 * @param shelfLifeWarningVO
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月13日 下午2:11:14<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
	@Override
	public Page<ShelflifeWarningVO> listByPage(ShelflifeWarningVO vo) throws Exception {
		// 获取当前用户信息
		Principal loginUser = LoginUtil.getLoginUser();
		if (loginUser == null) {
			throw new BizException("valid_common_user_no_login");
		}
		if ( vo == null ) {
    		vo = new ShelflifeWarningVO();
		}
		Page<ShelflifeWarningVO> page = null;
		//组装信息
		vo.setWarehouseId(LoginUtil.getWareHouseId());
		vo.setOrgId(loginUser.getOrgId());
		vo.setBatchNoLike(StringUtil.likeEscapeH(vo.getBatchNoLike()));
		vo.setOwnerLike(StringUtil.likeEscapeH(vo.getOwnerLike()));
		vo.setSkuNoLike(StringUtil.likeEscapeH(vo.getSkuNoLike()));
		// 查询保质期预警列表 
		page = PageHelper.startPage(vo.getCurrentPage()+1, vo.getPageSize());
		List<ShelflifeWarningVO> listVO = this.dao.findShelflifeList(vo);
		if ( listVO == null || listVO.isEmpty() ) {
			return null;
		}
		// 组装信息
		for (int i = 0; i < listVO.size(); i++) {
			ShelflifeWarningVO shelfLifeWarningVO = listVO.get(i);
			String skuId = shelfLifeWarningVO.getSkuId();
			String batchNo = shelfLifeWarningVO.getBatchNo();
			String warningHandlerId = shelfLifeWarningVO.getWarningHandlerId();
			WarningHandlerVO whVo = new WarningHandlerVO();
			WarningHandler wh = null;
			if (warningHandlerId != null) {
				wh = dao.selectByPrimaryKey(warningHandlerId);
				whVo.setWarningHandler(wh);
			}
			if (wh != null) {
				whVo.setWarningHandler(wh);
				SysUser createPerson = userService.get(wh.getCreatePerson());
				if ( createPerson != null ) {
					whVo.setCreatePersonName(createPerson.getUserName());
				}
				whVo.setHandlerStatusName("已处理");
			} else {
				wh = new WarningHandler(shelfLifeWarningVO);
				whVo.setWarningHandler(wh);
				whVo.setHandlerStatusName("未处理");
			}
			//开始查找库存详情
			InvStockVO stockVoParam = new InvStockVO();
			InvStock stockParam = new InvStock();
			stockParam.setSkuId(skuId);
			stockParam.setBatchNo(batchNo);
			stockVoParam.setInvStock(stockParam);
			List<InvStock> stockList = stockService.list(stockVoParam);
			List<InvStockVO> stockVoList = new ArrayList<InvStockVO>();
			for (InvStock stock : stockList) {
				InvStockVO entityVo = new InvStockVO(stock);
				String locationId = stock.getLocationId();
				MetaLocation location = this.locationService.findLocById(locationId);
				if (location != null) {
					entityVo.setLocationNo(location.getLocationNo());
					entityVo.setLocationName(location.getLocationName());
					entityVo.setLocationTypeName(paramService.getValue(CacheName.AREA_TYPE, location.getLocationType()));
					MetaArea area = this.areaService.findAreaById(location.getAreaId());
					if (area != null) {
						entityVo.setAreaName(area.getAreaName());
					}
				}
				MetaSku sku = this.skuService.get(stock.getSkuId());
				if (sku != null) {
					entityVo.setSkuNo(sku.getSkuNo());
					entityVo.setSkuName(sku.getSkuName());
					entityVo.setSpecModel(sku.getSpecModel());
					entityVo.setMeasureUnit(sku.getMeasureUnit());
//					MetaMerchant m =merchantService.get(sku.getOwner());
//					if (m != null) {
//						entityVo.setOwnerName(m.getMerchantName());
//					}
				}		
				stockVoList.add(entityVo);
			}
			whVo.setStockCount(shelfLifeWarningVO.getStockCount());
			whVo.setStockVoList(stockVoList);
			shelfLifeWarningVO.setWarningHandlerVo(whVo);
		}
//		page.clear();
//		page.addAll(listVO);
		return page;
	}

	/**
	 * @param handler
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年9月5日 下午3:47:39<br/>
	 * @author 王通<br/>
	 */
	@Override
	@Transactional(readOnly = false)
	public void handler(WarningHandler handler) {
		// 设置创建人/修改人/企业/仓库
		Principal loginUser = LoginUtil.getLoginUser();
		// 验证用户是否登录
		if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
			throw new BizException("valid_common_user_no_login");
		}
		// 字段校验
		if (handler == null) {
			throw new BizException("err_handler_param_null");
		}
		Date now = new Date();
		String orgId = loginUser.getOrgId();
		handler.setWarningHandlerId(UUID.randomUUID().toString());
		handler.setCreatePerson(loginUser.getUserId());
		handler.setCreateTime(now);
		handler.setUpdatePerson(loginUser.getUserId());
		handler.setUpdateTime(now);
		handler.setOrgId(orgId);
		handler.setWarehouseId(LoginUtil.getWareHouseId());
		String handlerMsg = handler.getHandleMsg();
		//防止内部调用时参数不全
		if (StringUtil.isTrimEmpty(handlerMsg)) {
			throw new BizException("err_exp_param_required_null");
		}
		this.dao.insertSelective(handler);
	}

	/**
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年9月7日 上午10:51:20<br/>
	 * @author 王通<br/>
	 */
	@Override
	public Integer count() {
		// 获取当前用户信息
		Principal loginUser = LoginUtil.getLoginUser();
		if (loginUser == null) {
			throw new BizException("valid_common_user_no_login");
		}
		ShelflifeWarningVO vo = new ShelflifeWarningVO();
		//组装信息
		vo.setHandleStatus(1);
		vo.setWarehouseId(LoginUtil.getWareHouseId());
		vo.setOrgId(loginUser.getOrgId());
		// 查询保质期预警列表 
		List<ShelflifeWarningVO> listVO = this.dao.findShelflifeList(vo);
		if ( listVO == null || listVO.isEmpty() ) {
			return 0;
		} else {
			return listVO.size();
		}
	}
	@Override
	public List<ShelflifeWarningVO> count(String orgId){
		ShelflifeWarningVO vo = new ShelflifeWarningVO();
		//组装信息
		vo.setHandleStatus(1);
		vo.setOrgId(orgId);
		// 查询保质期预警列表 
		List<ShelflifeWarningVO> listVO = this.dao.findShelflifeList(vo);
		return listVO;
	}

}