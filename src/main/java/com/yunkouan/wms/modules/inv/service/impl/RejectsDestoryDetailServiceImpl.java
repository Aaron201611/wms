package com.yunkouan.wms.modules.inv.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunkouan.exception.BizException;
import com.yunkouan.saas.modules.sys.service.IUserService;
import com.yunkouan.saas.modules.sys.service.IWarehouseExtlService;

import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.common.service.BaseService;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.inv.dao.InvRejectsDestoryDetailDao;
import com.yunkouan.wms.modules.inv.entity.InvRejectsDestoryDetail;
import com.yunkouan.wms.modules.inv.entity.InvStock;
import com.yunkouan.wms.modules.inv.service.IRejectsDestoryDetailService;
import com.yunkouan.wms.modules.inv.service.IStockService;
import com.yunkouan.wms.modules.inv.vo.InvRejectsDestoryDetailVO;
import com.yunkouan.wms.modules.meta.service.IAreaService;
import com.yunkouan.wms.modules.meta.service.ILocationExtlService;
import com.yunkouan.wms.modules.meta.service.IMerchantService;
import com.yunkouan.wms.modules.meta.service.ISkuService;

/**
 * 移位服务实现类
 */
@Service
public class RejectsDestoryDetailServiceImpl extends BaseService implements IRejectsDestoryDetailService {
	/**
     * 数据层接口
     */
	@Autowired
    private InvRejectsDestoryDetailDao detailDao;

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
	 * 外部服务-客商
	 */
	@Autowired
	IMerchantService merchantService;
	/**
	 * 外部服务-库区
	 */
	@Autowired
	IAreaService areaService;
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
     * Default constructor
     */
    public RejectsDestoryDetailServiceImpl() {
    }

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
    public List<InvRejectsDestoryDetailVO> listByParentId(String id) throws Exception {
		Principal loginUser = LoginUtil.getLoginUser();
    	// 验证用户是否登录
    	if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
    		throw new BizException("valid_common_user_no_login");
    	}
    	InvRejectsDestoryDetailVO paramVo = new InvRejectsDestoryDetailVO();
    	paramVo.getInvRejectsDestoryDetail().setRejectsDestoryId(id);
    	List<InvRejectsDestoryDetail> list = this.detailDao.selectByExample(paramVo.getExample());
    	return chg(list);
    }
	
	private List<InvRejectsDestoryDetailVO> chg(List<InvRejectsDestoryDetail> list) {
		List<InvRejectsDestoryDetailVO> voList = new ArrayList<InvRejectsDestoryDetailVO>();
		for (InvRejectsDestoryDetail entity: list) {
			voList.add(this.chg(entity));
		}
		return voList;
	}
	
	private InvRejectsDestoryDetailVO chg(InvRejectsDestoryDetail entity) {
		InvRejectsDestoryDetailVO vo = new InvRejectsDestoryDetailVO(entity);
		vo.setInvRejectsDestoryDetail(entity);
		InvStock stock = stockService.view(entity.getStockId());
		vo.setStock(stock);
		vo.setSku(skuService.get(stock.getSkuId()));
		try {
			vo.setLocation(locationService.findLocById(stock.getLocationId()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		vo.setOwner(merchantService.get(stock.getOwner()).getMerchantShortName());
		try {
			vo.setAreaName(areaService.viewArea(vo.getLocation().getAreaId()).getArea().getAreaName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return vo;
	}
//    /**
//     * 查看不良品销毁单详情
//     * @param vo
//     * @return 
//     * @Description 
//     * @version 2017年2月16日 下午1:58:31<br/>
//     * @author 王通<br/>
//     */
//    @Override
//	@Transactional(readOnly=false)
//    public InvRejectsDestoryVO view(String id) throws Exception {
//		Principal loginUser = LoginUtil.getLoginUser();
//    	// 验证用户是否登录
//    	if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
//    		throw new BizException("valid_common_user_no_login");
//    	}
//		if ( id == null ) {
//			throw new BizException("err_reject_null");
//		}
//		// 查询不良品销毁单详情 
//		InvRejectsDestory reject = this.dao.selectByPrimaryKey(id);
//		if ( reject == null ) {
//			throw new BizException("err_reject_null");
//		}
//		// 组装信息
//		InvRejectsDestoryVO rejectVo = chg(reject);
//		return rejectVo;
//    }
}