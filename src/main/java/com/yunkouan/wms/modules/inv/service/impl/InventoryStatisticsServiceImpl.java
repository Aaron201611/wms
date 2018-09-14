package com.yunkouan.wms.modules.inv.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yunkouan.excel.impt.ExcelExport;
import com.yunkouan.exception.BizException;
import com.yunkouan.exception.DaoException;
import com.yunkouan.exception.ServiceException;
import com.yunkouan.saas.modules.sys.service.IUserService;
import com.yunkouan.util.DateUtil;
import com.yunkouan.util.FileUtil;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.common.service.BaseService;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.inv.dao.IInventoryStatisticsDao;
import com.yunkouan.wms.modules.inv.entity.InvInventoryStatistics;
import com.yunkouan.wms.modules.inv.service.IInventoryStatisticsService;
import com.yunkouan.wms.modules.inv.vo.InvInventoryStatisticsVO;
import com.yunkouan.wms.modules.inv.vo.InvInventoryStatisticsVO4Excel;
import com.yunkouan.wms.modules.meta.service.ISkuService;
import com.yunkouan.wms.modules.meta.vo.SkuVo;

/**
 * 在库管理服务实现类
 */
@Service
public class InventoryStatisticsServiceImpl extends BaseService implements IInventoryStatisticsService {
	
	/**
	 * 外部服务-用户
	 */
	@Autowired
	IUserService userService;
	/**
	 * 外部服务-货品
	 */
	@Autowired
	ISkuService skuService;
	@Autowired
	IInventoryStatisticsDao statisticsDao;

    /**
     * Default constructor
     */
    public InventoryStatisticsServiceImpl() {}

    /**
	 * 查询sku库存列表(未冻结)
	 * @Description 
	 * 1.          手动点击“自动分配”时先删除拣货单明细，系统根据发货单明细触发自动分配。
2.          拣货单自动分配库位后，拣货单明细中相应货品的“分配数量”即产生数值（分配数量=已分配拣货库位的SKU对应的数量）。
3.          系统初始拣货规则：排除收货区暂存库位库存，对正常库位库存按先进先出FIFO处理。
4.          系统按照拣货策略分配拣货库位，默认不分配暂存区库位库存，人工可指定从暂存区库位拣货，但保存时，系统需做提醒，告知有暂存区某库位参与拣选。
5.          拣货规则，在v2.0或后续版本中，将在规则管理模块与其他规则一起，集中进行管理、配置。
6.    拣货单状态：打开、生效、作业中（打印拣货单触发）、作业完成（作业确认时触发，作业确认时，作业人员可以根据实际拣货情况进行拣货库位、拣货数量信息的更新，包括删增拣货单明细信息）。
	 * @version 2017年2月14日 下午1:40:04<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
    @Override
    @Transactional(readOnly=false)
	public Page<InvInventoryStatisticsVO> listByPage(InvInventoryStatisticsVO statisticsVo) throws Exception {
		Principal loginUser = LoginUtil.getLoginUser();
    	// 验证用户是否登录
    	if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
    		throw new BizException("valid_common_user_no_login");
    	}
    	Page<InvInventoryStatisticsVO> page = null;
    	if (statisticsVo == null) {
			throw new BizException("err_stock_param_null");
		}
    	// 设置创建人/修改人/企业/仓库
    	String orgId = loginUser.getOrgId();
    	if (statisticsVo.getInventoryStatistics() == null) {
    		statisticsVo.setInventoryStatistics(new InvInventoryStatistics());
    	}
    	statisticsVo.getInventoryStatistics().setOrgId(orgId);
    	statisticsVo.getInventoryStatistics().setWarehouseId(LoginUtil.getWareHouseId());
    	// 包装查询条件
    	statisticsVo.setOwnerNameLike(StringUtil.likeEscapeH(statisticsVo.getOwnerNameLike()));
    	statisticsVo.setSkuNameLike(StringUtil.likeEscapeH(statisticsVo.getSkuNameLike()));
    	statisticsVo.setSkuNoLike(StringUtil.likeEscapeH(statisticsVo.getSkuNoLike()));
		// 查询库存单列表 
    	Integer circle = statisticsVo.getCircle();
    	if (circle == null) {
			circle = 1;
			statisticsVo.setCircle(1);
		}
    	if (statisticsVo.getInventoryStatistics().getStatisticsDate() == null) {
    		statisticsVo.getInventoryStatistics().setStatisticsDate(DateUtil.getStartDateOnMonth(new Date()));
    	}
		Integer nowCircle = circle;
    	if (circle > 1) {
    		if (DateUtil.addMonths(statisticsVo.getInventoryStatistics().getStatisticsDate(), circle - 1).after(new Date())) {
    			nowCircle = DateUtil.getIntervalMonths(statisticsVo.getInventoryStatistics().getStatisticsDate(), new Date());
    		}
    		//循环大于一个月的情况下，pagesize要翻倍
    		statisticsVo.setPageSize(nowCircle * statisticsVo.getPageSize());
		}
    	page = PageHelper.startPage(statisticsVo.getCurrentPage()+1, statisticsVo.getPageSize());
    	
		List<InvInventoryStatistics> list = this.statisticsDao.list(statisticsVo);
		List<InvInventoryStatisticsVO> listVo = new ArrayList<InvInventoryStatisticsVO>();
		if (list != null && !list.isEmpty()) {
			//查看第一条记录的统计日期，如果大于统计起始月份，则报无结果异常
			if (statisticsVo.getInventoryStatistics().getStatisticsDate().before(list.get(0).getStatisticsDate())) {
				throw new BizException("inventory_statistics_no_data");
			}
			nowCircle = circle;
			InvInventoryStatisticsVO circleVo = null;
			for (InvInventoryStatistics entity : list) {
				if (circle > 1) {
					//进入周期统计方法
					if (circle == nowCircle) {
						//第一次进来时，赋值
						circleVo = chg(entity, circle);
						//如果日期大于今天，则实际循环次数要小于提供的值
						if (DateUtil.addMonths(entity.getStatisticsDate(), circle - 1).after(new Date())) {
							nowCircle = DateUtil.getIntervalMonths(entity.getStatisticsDate(), new Date());
						}
					} else {
						//后续次数进来时，累加进出盈亏数量
						circleVo.circleAdd(entity);
					}
					nowCircle--;
					if (nowCircle == 0) {
						//循环完毕，下次开始新循环
						nowCircle = circle;
						//判断查询条件是否包含当前月，若是，则查询当前月的进出盈亏数量
						if (DateUtil.addMonths(entity.getStatisticsDate(), circle).after(new Date())) {
							InvInventoryStatistics iis = statisticsDao.countCurrentMonth(entity);
							circleVo.circleAdd(iis);
						}
						listVo.add(circleVo);
					}
				} else {
					//判断查询条件是否包含当前月，若是，则查询当前月的进出盈亏数量
					if (DateUtil.addMonths(entity.getStatisticsDate(), circle).after(new Date())) {
						InvInventoryStatistics iis = statisticsDao.countCurrentMonth(entity);
						listVo.add(chg(iis, circle));
						listVo.add(circleVo);
					} else {
						circleVo = chg(entity, circle);
						listVo.add(circleVo);
					}
				}
			}
		} else {
			//无历史记录，若期初日期小于当前月份，则报错
			if (statisticsVo.getInventoryStatistics().getStatisticsDate().before(DateUtil.getStartDateOnMonth(new Date()))) {
				throw new BizException("inventory_statistics_no_data");
			}
			
			//无历史记录，需要查询当前月记录，只有期末数据没有期初数据
			//此处需要重置分页查询
			page = PageHelper.startPage(statisticsVo.getCurrentPage()+1, statisticsVo.getPageSize());
			//判断查询条件是否包含当前月，若是，则查询当前月的进出盈亏数量
			if (DateUtil.addMonths(statisticsVo.getInventoryStatistics().getStatisticsDate(), circle).after(new Date())) {
//				InvInventoryStatistics iis = statisticsDao.countCurrentMonth(statisticsVo.getInventoryStatistics());
//				listVo.add(chg(iis, circle));
				List<InvInventoryStatistics> countList = statisticsDao.countThisMonth(statisticsVo);
				for (InvInventoryStatistics entity : countList) {
					listVo.add(chg(entity, circle));
				}
			}
		}
		if (DateUtil.addMonths(statisticsVo.getInventoryStatistics().getStatisticsDate(), circle - 1).after(new Date())) {
			nowCircle = DateUtil.getIntervalMonths(statisticsVo.getInventoryStatistics().getStatisticsDate(), new Date());
		}
		page.clear();
		page.setPageSize(statisticsVo.getPageSize() / nowCircle);
		page.setTotal(page.getTotal() / nowCircle);
		page.addAll(listVo);
		return page;
	}
    


    private InvInventoryStatisticsVO chg(InvInventoryStatistics entity, Integer circle) throws DaoException, ServiceException {
    	InvInventoryStatisticsVO vo = new InvInventoryStatisticsVO(entity);
		vo.setStatisticsEndDate(DateUtil.addMonths(entity.getStatisticsDate(), circle - 1));
		vo.setCircle(circle);
		vo.setSkuVo((SkuVo)skuService.view(entity.getSkuId(), LoginUtil.getLoginUser()).getObj());
		return vo;
	}

	/**
	 * @param string
	 * @param file
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年4月21日 下午6:14:23<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
	public ResponseEntity<byte[]> download(String fileName, File file) throws Exception {
		return FileUtil.excel2Stream(file.getPath(), fileName);
	}
	/**
	 * @param string
	 * @param file
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年4月21日 下午6:14:23<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
	@Override
	public ResponseEntity<byte[]> downloadExcel(InvInventoryStatisticsVO vo) throws Exception {
	  // 导出库存  
      ExcelExport<InvInventoryStatisticsVO4Excel> ex = new ExcelExport<InvInventoryStatisticsVO4Excel>();  
      String[] headers = { "序号","起始月份",
    		  "统计周期","货主简称","货品名称",
    		  "货品条码","计量单位","期初",
    		  "进","出","盈亏","期末"}; 
      List<InvInventoryStatisticsVO4Excel> dataset = vo2excelVo(listByPage(vo));
      ResponseEntity<byte[]> bytes = ex.exportExcel2Array("进销存统计清单", headers, dataset, "yyyy-MM-dd", "进销存统计清单.xls");
      return bytes;
	}

	/**
	 * 普通vo转换为excel格式化后vo
	 * @param listByPage
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年10月23日 下午1:40:02<br/>
	 * @author 王通<br/>
	 */
	private List<InvInventoryStatisticsVO4Excel> vo2excelVo(Page<InvInventoryStatisticsVO> list) {
		List<InvInventoryStatisticsVO4Excel> retList = new ArrayList<InvInventoryStatisticsVO4Excel>();
		for (int i = 0; i < list.size(); i++) {
			InvInventoryStatisticsVO4Excel vo4excel = new InvInventoryStatisticsVO4Excel(list.get(i));
			vo4excel.setIndex(String.valueOf(i+1));
			retList.add(vo4excel);
		}
		return retList;
	}
}