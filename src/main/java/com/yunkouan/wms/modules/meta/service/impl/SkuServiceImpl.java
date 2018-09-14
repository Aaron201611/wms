package com.yunkouan.wms.modules.meta.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yunkouan.entity.ResultModel;
import com.yunkouan.excel.ExcelUtil;
import com.yunkouan.excel.impt.ExcelExport;
import com.yunkouan.exception.BizException;
import com.yunkouan.exception.DaoException;
import com.yunkouan.exception.ServiceException;
import com.yunkouan.saas.modules.sys.entity.MetaWarehouse;
import com.yunkouan.saas.modules.sys.entity.SysOrg;
import com.yunkouan.saas.modules.sys.service.IOrgService;
import com.yunkouan.saas.modules.sys.service.ISysParamService;
import com.yunkouan.saas.modules.sys.service.IWarehouseExtlService;
import com.yunkouan.util.FileUtil;
import com.yunkouan.util.NumberUtil;
import com.yunkouan.util.PubUtil;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.common.constant.CacheName;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.common.constant.ErrorCode;
import com.yunkouan.wms.common.service.BaseService;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.IdUtil;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.inv.entity.InvStock;
import com.yunkouan.wms.modules.inv.service.IStockService;
import com.yunkouan.wms.modules.inv.vo.InvStockVO;
import com.yunkouan.wms.modules.inv.vo.SkuExportExcel;
import com.yunkouan.wms.modules.meta.dao.ISkuDao;
import com.yunkouan.wms.modules.meta.dao.ISkuPackDao;
import com.yunkouan.wms.modules.meta.entity.MetaMerchant;
import com.yunkouan.wms.modules.meta.entity.MetaPack;
import com.yunkouan.wms.modules.meta.entity.MetaSku;
import com.yunkouan.wms.modules.meta.entity.MetaSkuPack;
import com.yunkouan.wms.modules.meta.entity.MetaSkuType;
import com.yunkouan.wms.modules.meta.service.IMerchantService;
import com.yunkouan.wms.modules.meta.service.ISkuService;
import com.yunkouan.wms.modules.meta.service.ISkuTypeService;
import com.yunkouan.wms.modules.meta.service.IWarehouseSettingService;
import com.yunkouan.wms.modules.meta.vo.MerchantVo;
import com.yunkouan.wms.modules.meta.vo.MetaWarehouseSettingVo;
import com.yunkouan.wms.modules.meta.vo.SkuTypeVo;
import com.yunkouan.wms.modules.meta.vo.SkuVo;
import com.yunkouan.wms.modules.rec.entity.RecAsn;
import com.yunkouan.wms.modules.rec.entity.RecAsnDetail;
import com.yunkouan.wms.modules.rec.service.IASNDetailExtlService;
import com.yunkouan.wms.modules.rec.service.IASNExtlService;
import com.yunkouan.wms.modules.rec.vo.RecAsnDetailVO;
import com.yunkouan.wms.modules.rec.vo.RecAsnVO;

/**
 * 货品服务实现类
 * @author tphe06 2017年2月14日
 */
@Service
@Transactional(readOnly=true)
public class SkuServiceImpl extends BaseService implements ISkuService {
	private static Log log = LogFactory.getLog(SkuServiceImpl.class);

	@Autowired
	private ISysParamService paramService;

	/**
	 * 外部服务-库存
	 */
	@Autowired
	IStockService stockService;
	/**
	 * 外部服务-收货
	 */
	@Autowired
	IASNExtlService asnService;
	/**
	 * 外部服务-收货
	 */
	@Autowired
	IASNDetailExtlService asnDetailService;

	@Autowired
	private IWarehouseSettingService warehouseSettingService;

	/**货品数据层接口*/
	@Autowired
	private ISkuDao dao;
	/**货品包装数据层接口**/
	@Autowired
	private ISkuPackDao skuPackDao;
	@Autowired
	private IMerchantService service;
	@Autowired
	private ISkuTypeService type;
	/**warehouseService:仓库服务**/
	@Autowired
	private IWarehouseExtlService warehouseService;
	/**orgServie:企业服务**/
	@Autowired
	private IOrgService orgServie;

	/**
	 * 货品列表数据查询
	 * @param vo 
	 * @param page 
	 * @return
	 */
	public ResultModel list(SkuVo vo, Principal p) throws DaoException, ServiceException {
		vo.getEntity().setOrgId(p.getOrgId());
		vo.getEntity().setWarehouseId(LoginUtil.getWareHouseId());
		Page<MetaSku> page = PageHelper.startPage(vo.getCurrentPage()+1, vo.getPageSize());
		if (vo.getMerchant() != null && !StringUtil.isTrimEmpty(vo.getMerchant().getMerchantName())) {
			vo.getMerchant().setMerchantName(StringUtil.likeEscapeH(vo.getMerchant().getMerchantName()));
		}
		vo.setSkuNoLike(StringUtil.likeEscapeH(vo.getSkuNoLike()));
		vo.setSkuNameLike(StringUtil.likeEscapeH(vo.getSkuNameLike()));
		vo.setMeasureUnitLike(StringUtil.likeEscapeH(vo.getMeasureUnitLike()));
		// 返回查询结果
		List<MetaSku> list = dao.list(vo);
		return new ResultModel().setPage(page).setList(fill(list));
	}

	/**
	 * 查询所有货品列表
	 * @param vo
	 * @param p
	 * @return
	 * @throws Exception
	 */
	public List<MetaSku> qryAllList(SkuVo vo,Principal p) throws Exception{
		vo.getEntity().setOrgId(p.getOrgId());
		vo.getEntity().setWarehouseId(LoginUtil.getWareHouseId());
		if (vo.getMerchant() != null && !StringUtil.isTrimEmpty(vo.getMerchant().getMerchantName())) {
			vo.getMerchant().setMerchantName(StringUtil.likeEscapeH(vo.getMerchant().getMerchantName()));
		}
		vo.setSkuNoLike(StringUtil.likeEscapeH(vo.getSkuNoLike()));
		vo.setSkuNameLike(StringUtil.likeEscapeH(vo.getSkuNameLike()));
		vo.setMeasureUnitLike(StringUtil.likeEscapeH(vo.getMeasureUnitLike()));
		List<MetaSku> list = dao.list(vo);
		return list;

	}

	/**
	 * 查询货品详情
	 * @param id 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ResultModel view(String id, Principal p) throws DaoException, ServiceException {
		SkuVo vo = new SkuVo();
		/**查询货品信息**/
		MetaSku entity =  dao.selectByPrimaryKey(id);
		if(entity == null) throw new ServiceException(ErrorCode.DATA_EMPTY);
		vo.setEntity(entity);
		/**查询货品包装列表**/
		List<MetaPack> list = skuPackDao.list(new MetaSkuPack().setSkuId(id));
		vo.setList(list);
		//查询客商信息
		if(StringUtils.isNoneBlank(entity.getOwner())) vo.setMerchant(service.get(entity.getOwner()));
		//查询货品类型名称
		if(StringUtils.isNoneBlank(entity.getSkuTypeId())) {
			MetaSkuType t = type.get(entity.getSkuTypeId());
			if(t != null) {
				vo.setSkuTypeId(t.getSkuTypeId());
				vo.setSkuTypeName(t.getSkuTypeName());
				vo.setParent(type.get(t.getParentId()));
				SkuTypeVo t1 = new SkuTypeVo(t);
				vo.setTypeList(type.selectList(t1, p).getList());
			}
		}
		return new ResultModel().setObj(vo);
	}

	/**
	 * 添加货品
	 * @param vo 
	 * @return
	 */
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public ResultModel add(SkuVo vo, Principal p) throws DaoException, ServiceException {
		/**添加货品信息**/
		MetaSku entity = vo.getEntity();
		if(entity == null) throw new ServiceException(ErrorCode.DATA_EMPTY);
		entity.clear();
		//普通仓货品代码等于货品条码
		String warehouseId = LoginUtil.getWareHouseId();
		MetaWarehouse warehouse = warehouseService.findWareHouseById(warehouseId);
		String setSkuNo = entity.getSkuNo();
		String skuNo = null;
		if (warehouse != null) {
			if (warehouse.getWarehouseType().intValue() == Constant.WAREHOUSE_TYPE_NOTCUSTOMS) {
				skuNo = entity.getSkuBarCode();
				entity.setSkuNo(skuNo);
			} else {
				//货品代码生成规则：3位货主编号+6位自动生成的编号
				String no = context.getStrategy4No(p.getOrgId(), LoginUtil.getWareHouseId()).getSkuPrefixNo(p.getOrgId());
				skuNo = vo.getMerchant().getMerchantNo() + no;
				entity.setSkuNo(skuNo);
			}
		}
		//查询仓库配置
		MetaWarehouseSettingVo settingVo = warehouseSettingService.findByWarehouseId(LoginUtil.getWareHouseId());
		if (settingVo != null && settingVo.getEntity().getInputSkuNo()!= null && settingVo.getEntity().getInputSkuNo() == 1) {
			if (StringUtil.isNoneBlank(setSkuNo)) {
				entity.setSkuNo(setSkuNo);
			} else {
				throw new BizException("err_skuNo_null");
			}
		}
		//若海关货号为空，则复制货品代码--20171024hgx
		if(StringUtil.isTrimEmpty(entity.getHgGoodsNo())){
			//如果是普通仓，就按货品代码生成规则，生成海关货号
			if (warehouse.getWarehouseType().intValue() == Constant.WAREHOUSE_TYPE_NOTCUSTOMS) {
				String no = context.getStrategy4No(p.getOrgId(), LoginUtil.getWareHouseId()).getSkuPrefixNo(p.getOrgId());
				String hgGoodsNo = vo.getMerchant().getMerchantNo() + no;
				entity.setHgGoodsNo(hgGoodsNo);
			}else{
				entity.setHgGoodsNo(skuNo);
			}
		}else{
			//检验海关货号唯一性
			MetaSku skuParam = new MetaSku();
			skuParam.setHgGoodsNo(entity.getHgGoodsNo());
			int num = dao.selectCount(skuParam);
			if(num > 0){
				throw new BizException("hg_goods_no_has_exists");
			}
		}
		//同一仓库同一货主货品名称不允许重复
		MetaSku su = new MetaSku().setSkuName(entity.getSkuName()).setOrgId(p.getOrgId()).setWarehouseId(LoginUtil.getWareHouseId()).setOwner(entity.getOwner());
		MetaSku old = dao.selectOne(su);
		if(old != null) throw new ServiceException("err_sku_rep");
		//同一仓库同一货主货品代码不能重复
		su = new MetaSku().setSkuNo(entity.getSkuNo()).setOrgId(p.getOrgId()).setWarehouseId(LoginUtil.getWareHouseId()).setOwner(entity.getOwner());
		old = dao.selectOne(su);
		if(old != null) throw new ServiceException("err_skuNo_rep");
		//同一仓库同一货主货品条码不能重复
		su = new MetaSku().setSkuBarCode(entity.getSkuBarCode()).setOrgId(p.getOrgId()).setWarehouseId(LoginUtil.getWareHouseId()).setOwner(entity.getOwner());
		old = dao.selectOne(su);
		if(old != null) throw new ServiceException("err_skuBarCode_rep");
		//计量单位转换
		if (!StringUtil.isTrimEmpty(entity.getMeasureUnitCode())) {
			if (StringUtil.isTrimEmpty(entity.getMeasureUnit())) {
				String measure = paramService.getValue(CacheName.MEASURE_UNIT, entity.getMeasureUnitCode());
				entity.setMeasureUnit(measure);
			}
		} else {
			String measure = entity.getMeasureUnit();
			String measureUnitCode = paramService.getValue(CacheName.MEASURE_UNIT_REVERSE, measure);
			entity.setMeasureUnitCode(measureUnitCode);
		}
		if (entity.getSkuStatus() == null) {
			entity.setSkuStatus(Constant.STATUS_OPEN);
		}
		String id = IdUtil.getUUID();
		entity.setSkuId(id);
		entity.setOrgId(p.getOrgId());
		entity.setWarehouseId(LoginUtil.getWareHouseId());
		entity.setCreatePerson(p.getUserId());
		if(StringUtils.isBlank(entity.getSkuTypeId())) entity.setSkuTypeId(null);
		entity.setSkuId2(context.getStrategy4Id().getSkuSeq());
		int r = dao.insertSelective(entity);
		if(r == 0) throw new DaoException(ErrorCode.DB_EXCEPTION);
		/**批量添加货品包装信息**/
		List<MetaPack> list = vo.getList();
		if(list != null) for(int i=0; i<list.size(); ++i) {
			MetaPack pack = list.get(i);
			if(pack == null || StringUtil.isTrimEmpty(pack.getPackId())) continue;
			MetaSkuPack child = new MetaSkuPack();
			child.setCreatePerson(p.getUserId());
			child.setOrgId(p.getOrgId());
			child.setWarehouseId(LoginUtil.getWareHouseId());
			child.setPackId(pack.getPackId());
			child.setPackPercent(pack.getPackPercent());
			child.setSkuId(id);
			child.setSkuPackId(IdUtil.getUUID());
			child.setUpdatePerson(p.getUserId());
			child.setSkuPackId2(context.getStrategy4Id().getSkuPackSeq());
			r = skuPackDao.insertSelective(child);
			if(r == 0) throw new DaoException(ErrorCode.DB_EXCEPTION);
		}
		/**返回货品信息**/
		return view(id, p);
	}

	/**
	 * 修改货品
	 * @param vo 
	 * @return
	 */
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public ResultModel update(SkuVo vo, Principal p) throws DaoException, ServiceException {
		/**修改货品信息**/
		MetaSku entity = vo.getEntity();
		if(entity == null) throw new ServiceException(ErrorCode.DATA_EMPTY);
		entity.clear();
		//同一仓库同一货主货品代码不能重复
		MetaSku old = dao.selectOne(new MetaSku().setSkuNo(entity.getSkuNo()).setOrgId(p.getOrgId()).setWarehouseId(LoginUtil.getWareHouseId()).setOwner(entity.getOwner()));
		if(old != null && !old.getSkuId().equals(entity.getSkuId())) throw new ServiceException(ErrorCode.NO_NOT_UNIQUE);
		//同一仓库同一货主货品名称不允许重复
		old = dao.selectOne(new MetaSku().setSkuName(entity.getSkuName()).setOrgId(p.getOrgId()).setWarehouseId(LoginUtil.getWareHouseId()).setOwner(entity.getOwner()));
		if(old != null && !old.getSkuId().equals(entity.getSkuId())) throw new ServiceException("err_sku_rep");
		//同一仓库同一货主货品条码不允许重复
		old = dao.selectOne(new MetaSku().setSkuBarCode(entity.getSkuBarCode()).setOrgId(p.getOrgId()).setWarehouseId(LoginUtil.getWareHouseId()).setOwner(entity.getOwner()));
		if(old != null && !old.getSkuId().equals(entity.getSkuId())) throw new ServiceException("err_skuBarCode_rep");


		//    	SkuVo param = new SkuVo();
		//    	param.setEntity(new MetaSku().setSkuName(entity.getSkuName()).setOrgId(p.getOrgId()).setWarehouseId(LoginUtil.getWareHouseId()));
		//    	List<MetaSku> list0 = dao.list(param);
		//    	if(list0.size() > 1) throw new ServiceException("err_sku_rep");
		//海关货号不能重复
		//若海关货号为空，则复制货品代码--20171024hgx 2018-5-9 11:36:31王通
		if(!StringUtil.isTrimEmpty(entity.getHgGoodsNo())){
			//检验海关货号唯一性
			old = dao.selectOne(new MetaSku().setHgGoodsNo(entity.getHgGoodsNo()));
			if(old != null && !old.getSkuId().equals(entity.getSkuId())) throw new ServiceException("hg_goods_no_has_exists");
		} else {
			entity.setHgGoodsNo(entity.getSkuNo());
		}
		//计量单位转换
		String measure = paramService.getValue(CacheName.MEASURE_UNIT, entity.getMeasureUnitCode());
		entity.setMeasureUnit(measure);
		entity.setOrgId(p.getOrgId());
		entity.setWarehouseId(LoginUtil.getWareHouseId());
		entity.setUpdatePerson(p.getUserId());
		if(StringUtils.isBlank(entity.getSkuTypeId())) entity.setSkuTypeId(null);
		int r = dao.updateByPrimaryKeySelective(entity);
		if(r == 0) throw new DaoException(ErrorCode.DB_EXCEPTION);
		/**批量修改货品包装信息，先删除后添加**/
		r = skuPackDao.delete(new MetaSkuPack().setSkuId(entity.getSkuId()));
		List<MetaPack> list = vo.getList();
		if(list != null) for(int i=0; i<list.size(); ++i) {
			MetaPack pack = list.get(i);
			if(pack == null || StringUtil.isTrimEmpty(pack.getPackId())) continue;
			MetaSkuPack child = new MetaSkuPack();
			child.setCreatePerson(p.getUserId());
			child.setOrgId(p.getOrgId());
			child.setWarehouseId(LoginUtil.getWareHouseId());
			child.setPackId(pack.getPackId());
			child.setPackPercent(pack.getPackPercent());
			child.setSkuId(entity.getSkuId());
			child.setSkuPackId(IdUtil.getUUID());
			child.setUpdatePerson(p.getUserId());
			child.setSkuPackId2(context.getStrategy4Id().getSkuPackSeq());
			r = skuPackDao.insertSelective(child);
			if(r == 0) throw new DaoException(ErrorCode.DB_EXCEPTION);
		}
		/**返回货品信息**/
		return view(entity.getSkuId(), p);
	}

	/**
	 * 修改货品
	 * @param vo 
	 * @return
	 */
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	@Override
	public ResultModel change(SkuVo vo, Principal p) throws DaoException, ServiceException {
		/**修改货品信息**/
		MetaSku entity = vo.getEntity();
		if(entity == null) throw new ServiceException(ErrorCode.DATA_EMPTY);
		entity.clear();
		MetaSku old = dao.selectOne(new MetaSku().setSkuNo(entity.getSkuNo()).setOrgId(p.getOrgId()));
		if(old != null && !old.getSkuId().equals(entity.getSkuId())) throw new ServiceException(ErrorCode.NO_NOT_UNIQUE);
		//计量单位转换
		String measure = paramService.getValue(CacheName.MEASURE_UNIT, entity.getMeasureUnitCode());
		entity.setMeasureUnit(measure);
		entity.setOrgId(p.getOrgId());
		entity.setUpdatePerson(p.getUserId());
		if(StringUtils.isBlank(entity.getSkuTypeId())) entity.setSkuTypeId(null);
		int r = dao.updateByPrimaryKeySelective(entity);
		if(r == 0) throw new DaoException(ErrorCode.DB_EXCEPTION);
		/**批量修改货品包装信息，先删除后添加**/
		r = skuPackDao.delete(new MetaSkuPack().setSkuId(entity.getSkuId()));
		List<MetaPack> list = vo.getList();
		if(list != null) for(int i=0; i<list.size(); ++i) {
			MetaPack pack = list.get(i);
			if(pack == null || StringUtil.isTrimEmpty(pack.getPackId())) continue;
			MetaSkuPack child = new MetaSkuPack();
			child.setCreatePerson(p.getUserId());
			child.setOrgId(p.getOrgId());
			child.setPackId(pack.getPackId());
			child.setPackPercent(pack.getPackPercent());
			child.setSkuId(entity.getSkuId());
			child.setSkuPackId(IdUtil.getUUID());
			child.setUpdatePerson(p.getUserId());
			child.setSkuPackId2(context.getStrategy4Id().getSkuPackSeq());
			r = skuPackDao.insertSelective(child);
			if(r == 0) throw new DaoException(ErrorCode.DB_EXCEPTION);
		}
		/**返回货品信息**/
		return view(entity.getSkuId(), p);
	}
	/**
	 * 生效货品
	 * @param id 
	 * @return
	 */
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public ResultModel enable(List<String> idList) throws DaoException, ServiceException {
		for (String id : idList) {
			MetaSku old =  dao.selectByPrimaryKey(id);
			if(old == null) throw new ServiceException(ErrorCode.DATA_EMPTY);
			if(old.getSkuStatus() != Constant.STATUS_OPEN) throw new ServiceException(ErrorCode.STATUS_NO_RIGHT);
			MetaSku obj = new MetaSku();
			obj.setSkuId(id);
			obj.setSkuStatus(Constant.STATUS_ACTIVE);
			int r = dao.updateByPrimaryKeySelective(obj);
			if(r == 0) throw new DaoException(ErrorCode.DB_EXCEPTION);
		}
		ResultModel m = new ResultModel();
		return m;
	}

	/**
	 * 同步更新erp商品信息
	 * @param idList
	 * @throws Exception
	 */
	//    public void updateErpSku(int status,List<String> idList) throws Exception{
	//    	List<Map<String,String>> goodsList = new ArrayList<Map<String,String>>();
	//    	for (String id : idList) {
	//	    	//查询货品信息
	//    		MetaSku sku = dao.selectByPrimaryKey(id);
	//    		//同步更新ERP
	//    		Map<String,String> map = new HashMap<String,String>();
	//    		map.put("goods_no", sku.getSkuId());
	//			map.put("hg_goods_no", sku.getSkuNo());
	//			map.put("status", status+"");
	//    		if(status == 1){
	////    			MetaWarehouse wareHouse = warehouseService.findWareHouseById(sku.getWarehouseId());
	//    			
	//    			map.put("goods_name", sku.getSkuName());
	//    			map.put("bar_code", sku.getSkuBarCode());
	////    			map.put("warehouse_code", wareHouse.getWarehouseNo());
	//    			map.put("warehouse_code", "");
	//    			map.put("unit", sku.getMeasureUnit());
	//    			
	//    			map.put("price", NumberUtil.rounded(sku.getPerPrice()==null?0:sku.getPerPrice(), 2));
	//    			map.put("goods_weight", NumberUtil.rounded(sku.getPerWeight(),1));
	//    		}
	//    		goodsList.add(map);
	//    	}
	//    	String data = JsonUtil.toJson(goodsList);
	//    	Map<String,String> paramMap = new HashMap<String,String>();
	//    	paramMap.put("goods", data);
	//    	paramMap.put("notify_time", new Date().getTime()+"");
	//		eRPService.doInvoke(Constant.ERP_UPDATEGOODS,paramMap);
	//    }
	//    

	/**
	 * 失效货品 在有库存和未完成收货单的情况下不能失效
	 * @param id 
	 * @return
	 * @throws Exception 
	 */
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public ResultModel disable(List<String> idList) throws Exception {
		//获取当前打开生效状态的收货单
		RecAsnVO recAsnVO = new RecAsnVO();
		List<Integer> listAsnStatus = new ArrayList<Integer>();
		recAsnVO.setListAsnStatus(listAsnStatus);
		listAsnStatus.add(Constant.ASN_STATUS_ACTIVE);
		listAsnStatus.add(Constant.ASN_STATUS_OPEN);
		List<RecAsn> listAsn = asnService.listAsnByExample(recAsnVO);
		List<String> listAsnId = new ArrayList<String>();
		if (listAsn != null&&listAsn.size()>0) {
			for (RecAsn asn : listAsn) {
				listAsnId.add(asn.getAsnId());
			}
		}

		for (String id : idList) {
			MetaSku old =  dao.selectByPrimaryKey(id);
			if(old == null) throw new ServiceException(ErrorCode.DATA_EMPTY);
			if(old.getSkuStatus() != Constant.STATUS_ACTIVE) throw new ServiceException(ErrorCode.STATUS_NO_RIGHT);
			//判断有无库存
			InvStockVO stockVo = new InvStockVO();
			InvStock stock = new InvStock();
			stockVo.setInvStock(stock);
			stock.setSkuId(id);
			List<InvStock> listStock = stockService.list(stockVo);
			if (listStock != null && listStock.size() > 0) {
				throw new BizException("err_sku_disable_stock");
			};
			//（在打开生效的ASN单中判断有无该货品）判断有无未完成收货单
			if(listAsnId.size() > 0){
				RecAsnDetailVO recAsnDetailVO = new RecAsnDetailVO();
				recAsnDetailVO.setListAsnId(listAsnId);
				RecAsnDetail recAsnDetail = new RecAsnDetail();
				recAsnDetail.setSkuId(id);
				recAsnDetailVO.setAsnDetail(recAsnDetail);
				List<RecAsnDetail> listAsnDetail = asnDetailService.listAsnDetailByExample(recAsnDetailVO);
				//List<RecAsn>listAsn=asnService.listAsnByExample(recAsnVO);
				if (listAsnDetail != null && listAsnDetail.size() > 0) {
					throw new BizException("err_sku_disable_asn");
				}
			}
			//校验通过
			MetaSku obj = new MetaSku();
			obj.setSkuId(id);
			obj.setSkuStatus(Constant.STATUS_OPEN);
			int r = dao.updateByPrimaryKeySelective(obj);
			if(r == 0) throw new DaoException(ErrorCode.DB_EXCEPTION);
		}
		ResultModel m = new ResultModel();
		return m;
	}

	/**
	 * @Description: 取消货品
	 * @param id
	 * @return
	 * @throws DaoException
	 * @throws ServiceException
	 */
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public ResultModel cancel(List<String> idList) throws DaoException, ServiceException {
		for (String id : idList) {
			MetaSku old =  dao.selectByPrimaryKey(id);
			if(old == null) throw new ServiceException(ErrorCode.DATA_EMPTY);
			if(old.getSkuStatus() != Constant.STATUS_OPEN) throw new ServiceException(ErrorCode.STATUS_NO_RIGHT);
			MetaSku obj = new MetaSku();
			obj.setSkuId(id);
			obj.setSkuStatus(Constant.STATUS_CANCEL);
			int r = dao.updateByPrimaryKeySelective(obj);
			if(r == 0) throw new DaoException(ErrorCode.DB_EXCEPTION);
		}
		ResultModel m = new ResultModel();
		return m;
	}

	@Override
	public MetaSku get(String id) {
		MetaSku obj =  dao.selectByPrimaryKey(id);
		return obj;
	}

	/**
	 * 根据货品包装实体类属性查询一个货品信息
	 */
	@Override
	public MetaSkuPack querySkuPack(MetaSkuPack entity) {
		MetaSkuPack o = skuPackDao.selectOne(entity);
		return o;
	}

	@Override
	public MetaSku query(MetaSku entity) {
		return dao.selectOne(entity);
	}

	/**
	 * @Description: 分页查询安全库存信息（限设置了最大安全库存或者最小安全库存记录）
	 * @param vo：查询参数，可选：
	 * 货主名称：merchant.merchantName
	 * 货品代码：entity.skuNo
	 * 当前页：currentPage
	 * 每页显示数量：pageSize
	 * @throws DaoException
	 * @throws ServiceException
	 * @return SkuVo
	 * @throws
	 */
	@Override
	public Page<MetaSku> list4stock(SkuVo vo, Principal p) throws DaoException, ServiceException {
		Page<MetaSku> page = PageHelper.startPage(vo.getCurrentPage()+1, vo.getPageSize());
		//		if(vo.getEntity() != null && StringUtils.isNotEmpty(vo.getEntity().getSkuNo())) 
		//			vo.getEntity().setSkuNo("%".concat(vo.getEntity().getSkuNo()).concat("%"));
		//		if(vo.getMerchant() != null && StringUtils.isNotEmpty(vo.getMerchant().getMerchantName())) 
		//			vo.getMerchant().setMerchantName("%".concat(vo.getMerchant().getMerchantName()).concat("%"));
		List<MetaSku> list = dao.list4stock(vo);
		if(log.isInfoEnabled()) log.info(list.size());
		return page;
	}

	/**
	 * @param vo
	 * @return
	 * @throws DaoException
	 * @throws ServiceException
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年6月19日 下午2:57:02<br/>
	 * @author 王通<br/>
	 */
	@Override
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public ResultModel saveAndEnable(SkuVo vo) throws DaoException, ServiceException {
		Principal p = LoginUtil.getLoginUser();
		ResultModel r = null;
		if (StringUtil.isTrimEmpty(vo.getEntity().getSkuId())) {
			r = this.add(vo, p);
			String skuId = vo.getEntity().getSkuId();
			List<String> skuList = new ArrayList<String>();
			skuList.add(skuId);
			this.enable(skuList);
		} else {
			r = this.update(vo, p);
			String skuId = vo.getEntity().getSkuId();
			List<String> skuList = new ArrayList<String>();
			skuList.add(skuId);
			this.enable(skuList);
		}
		return r;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public ResultModel list4Page(SkuVo vo) throws DaoException, ServiceException {
		Page<MetaSku> page = PageHelper.startPage(vo.getCurrentPage(), vo.getPageSize());
		List<MetaSku> list = dao.selectByExample(vo.getExample());
		if(vo.getCurrentPage() > page.getPages()) return new ResultModel().setPage(page).setList(new ArrayList());
		List<SkuVo> voList = fill(list);
		return new ResultModel().setPage(page).setList(voList);
	}

	/**
	 * 批量把货品实体类转化成VO对象
	 * @param list
	 * @return
	 * @throws ServiceException
	 * @throws DaoException
	 */
	private List<SkuVo> fill(List<MetaSku> list) throws ServiceException, DaoException {
		List<SkuVo> r = new ArrayList<SkuVo>();
		if(list != null) for(MetaSku entity : list) {
			r.add(fill(entity));
		}
		return r;
	}
	/**
	 * 把货品实体类转化成VO对象，同时补充一些扩展属性
	 * @param entity
	 * @return
	 * @throws DaoException 
	 * @throws ServiceException 
	 * @throws Exception 
	 */
	private SkuVo fill(MetaSku entity) throws ServiceException, DaoException {
		SkuVo vo = new SkuVo(entity);
		//查询客商信息
		if(StringUtils.isNoneBlank(entity.getOwner())) vo.setMerchant(service.get(entity.getOwner()));
		//查询货品类型名称
		if(StringUtils.isNoneBlank(entity.getSkuTypeId())) {
			MetaSkuType t = type.get(entity.getSkuTypeId());
			if(t != null) {
				vo.setSkuTypeId(t.getSkuTypeName());
				vo.setSkuTypeName(t.getSkuTypeName());
			}
		}
		//仓库
		if(StringUtils.isNoneBlank(entity.getWarehouseId())) vo.setWarehouse(warehouseService.findWareHouseById(entity.getWarehouseId()));
		//企业
		if(StringUtils.isNoneBlank(entity.getOrgId())) {
			SysOrg org = new SysOrg();
			org.setOrgId(entity.getOrgId());
			vo.setOrg(orgServie.query(org));
		}
		return vo;
	}

	/**
	 * @param merchantIdList
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年9月11日 下午7:53:16<br/>
	 * @author 王通<br/>
	 */
	@Override
	public List<String> getSkuIdList(List<String> merchantIdList) {
		SkuVo skuVo = new SkuVo();
		skuVo.setMerchantIdList(merchantIdList);
		List<MetaSku> skuList = this.dao.selectByExample(skuVo.getExample());
		List<String> skuIdList = new ArrayList<String>();
		for (MetaSku sku : skuList) {
			skuIdList.add(sku.getSkuId());
		}
		return skuIdList;
	}

	/**
	 * 导入货品
	 * @param stockVo
	 * @return
	 * @throws Exception
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年2月27日 下午4:37:04<br/>
	 * @author 王通<br/>
	 */
	@Override
	@Transactional(readOnly=false)
	public void importSku(MultipartFile fileLicense) throws Exception {
		Principal loginUser = LoginUtil.getLoginUser();
		// 验证用户是否登录
		if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
			throw new BizException("valid_common_user_no_login");
		}
		if (fileLicense == null) {
			throw new BizException("parameter_is_null");
		}
		File newFile = FileUtil.createNewFile(fileLicense);
		List<SkuVo> skuVoList = null;
		if ( newFile != null ) {
			try {
				skuVoList = new ArrayList<SkuVo>();
				List<Object> listImport = ExcelUtil.parseExcel("sku", newFile.getAbsolutePath());
				for (int i = 0; i < listImport.size(); i++) {
					Object obj = listImport.get(i);
					if (obj != null) {
						skuVoList.add((SkuVo) obj);
					} else {
						SkuVo skuVo = new SkuVo();
						skuVo.setEntity(new MetaSku());
						skuVoList.add(skuVo);
					}
				}
			} catch (Exception e) {
				throw e;
			} finally {
				// 删除文件
				newFile.delete();
			}
		} else {
			throw new BizException("err_stock_imp_no_file");
		}
		String ret = "";
		if ( PubUtil.isEmpty(skuVoList) ) {
			throw new BizException("err_stock_imp_no_date");
		}

		List<Integer> repeatIndexList = new ArrayList<Integer>();
		for (int i = 0; i < skuVoList.size(); i++) {
			if (repeatIndexList.contains(i)) {
				continue;
			}
			boolean currect = true;
			String lineResult = "";
			SkuVo skuVo = skuVoList.get(i);
			MetaSku sku = skuVo.getEntity();
			if (sku == null) {
				currect = false;
				lineResult = "行" + (i + 2) + "无数据!\r\n<br />";
				ret += lineResult;
				continue;
			}
			//获取当前行数据，带$为必填项
			String merchantName$ = skuVo.getMerchant().getMerchantName();
			String skuName$ = sku.getSkuName();
			String skuBarCode$ = sku.getSkuBarCode();
			String measureUnit$ = sku.getMeasureUnit();
			//			String specModel = sku.getSpecModel();
			Double perWeight$ = sku.getPerWeight();
			Double length$ = sku.getLength();
			Double width$ = sku.getWidth();
			Double height$ = sku.getHeight();
			String skuTypeName$ = skuVo.getSkuTypeName();
			Integer minSafetyStock = sku.getMinSafetyStock();
			Integer maxSafetyStock = sku.getMaxSafetyStock();
			Integer minReplenishStock = sku.getMinReplenishStock();
			Integer maxReplenishStock = sku.getMaxReplenishStock();
			//			String hsCode$ = sku.getHsCode();
			String gNo$ = sku.getgNo();
			String goodsNatureComment$ = skuVo.getGoodsNatureComment();
			String currComment$ = skuVo.getCurrComment();
			String originCountryName = skuVo.getOriginCountryName();
			//			Double perPrice = sku.getPerPrice();
			//			Double declarePrice = sku.getDeclarePrice();
			//			String brand = sku.getBrand();
			//			String attribute1 = sku.getAttribute1();
			//			String attribute2 = sku.getAttribute2();
			//			String attribute3 = sku.getAttribute3();
			//    		String attribute4 = sku.getAttribute4();
			//    		Integer shelflife = sku.getShelflife();
			//    		Integer overdueWarningDays = sku.getOverdueWarningDays();
			//    		String note = sku.getNote();
			String skuStatusName = skuVo.getSkuStatus();
			String skuNo = skuVo.getEntity().getSkuNo();
			//检测是否信息不全，即无效
			//查询仓库配置
			MetaWarehouseSettingVo settingVo = warehouseSettingService.findByWarehouseId(LoginUtil.getWareHouseId());
			if (settingVo != null && settingVo.getEntity().getInputSkuNo() != null && settingVo.getEntity().getInputSkuNo() == 1) {
				if (!StringUtil.isNoneBlank(skuNo)) {
					currect = false;
					lineResult += "配置了手工输入,'货品代码'不能为空！";
				}
			}
			if (StringUtil.isTrimEmpty(merchantName$)) {
				currect = false;
				lineResult += "'货主名称'不能为空！";
			}
			if (StringUtil.isTrimEmpty(skuName$)) {
				currect = false;
				lineResult += "'货品名称'不能为空！";
			}
			if (StringUtil.isTrimEmpty(skuBarCode$)) {
				currect = false;
				lineResult += "'货品条码'不能为空！";
			}
			else if (skuBarCode$.indexOf(".") != -1) {
				currect = false;
				lineResult += "'货品条码'不能有小数点！";
			}
			else if (skuBarCode$.length() < 6 || skuBarCode$.length() > 64) {
				currect = false;
				lineResult += "'货品条码'长度不能超出范围6~64！";
			}

			if (StringUtil.isTrimEmpty(measureUnit$)) {
				currect = false;
				lineResult += "'计量单位'不能为空！";
			}
			if (perWeight$ == null) {
				currect = false;
				lineResult += "'单重'不能为空！";
			}
			else if(perWeight$ <=0 || perWeight$ > 100){
				currect = false;
				lineResult += "'单重'不能超出0~100！";
			}else{
				sku.setPerWeight(NumberUtil.subScale(perWeight$, 0d, 4));
			}
			if (length$ == null) {
				currect = false;
				lineResult += "'长'不能为空！";
			}
			else if(length$ <=0 || length$ > 100){
				currect = false;
				lineResult += "'长'不能超出范围0-100！";
			}
			if (width$ == null) {
				currect = false;
				lineResult += "'宽'不能为空！";
			}
			else if (width$ <= 0 || width$ > 100) {
				currect = false;
				lineResult += "'宽'不能超出范围0-100！";
			}
			if (height$ == null) {
				currect = false;
				lineResult += "'高'不能为空！";
			}
			else if(height$ <= 0 || height$ > 100) {
				currect = false;
				lineResult += "'高'不能超出范围0-100！";
			}
			if (StringUtil.isTrimEmpty(skuTypeName$)) {
				currect = false;
				lineResult += "'货品子类型名称'不能为空！";
			}
			//    		if (StringUtil.isTrimEmpty(hsCode$)) {
			//    			currect = false;
			//    			lineResult += "'海关归类税号'不能为空！";
			//    		}
			if (StringUtil.isTrimEmpty(gNo$)) {
				currect = false;
				lineResult += "'项号'不能为空！";
			}
			if (StringUtil.isTrimEmpty(goodsNatureComment$)) {
				currect = false;
				lineResult += "'料件性质'不能为空！";
			}
			if (StringUtil.isTrimEmpty(currComment$)) {
				currect = false;
				lineResult += "'币制'不能为空！";
			}
			if (minSafetyStock != null && maxSafetyStock != null && minSafetyStock.intValue() > maxSafetyStock.intValue()) {
				currect = false;
				lineResult += "'安全库存下限'不能大于'安全库存上限'！";
			}
			if (minReplenishStock != null) {
				if (maxReplenishStock == null) {
					currect = false;
					lineResult += "'补货警戒数量'和'补货上限数量'必须同时存在！";
				} else {
					if(minReplenishStock.intValue() > maxReplenishStock.intValue()) {
						currect = false;
						lineResult += "'补货警戒数量'不能大于'补货上限数量'！";
					}
				}
			} else {
				if (maxReplenishStock != null) {
					currect = false;
					lineResult += "'补货警戒数量'和'补货上限数量'必须同时存在！";
				}
			}
			String owner = null;
			String skuTypeId = null;
			String merchantNo = null;
			try {
				//校验关联是否存在
				MerchantVo chantVo = new MerchantVo(new MetaMerchant());
				chantVo.getEntity().setMerchantName(merchantName$);
				List<MetaMerchant> mmList = service.listByParam(chantVo,loginUser);
				if (mmList == null || mmList.isEmpty()) {
					currect = false;
					lineResult += "'货主'查无结果！";
				}else if(mmList.size() > 1){
					currect = false;
					lineResult += "'货主'有多条记录！";
				}else {
					owner = mmList.get(0).getMerchantId();
					merchantNo = mmList.get(0).getMerchantNo();
				}
			} catch (Exception e) {
				e.printStackTrace();
				currect = false;
				lineResult += "'货主'有多条记录！";
			}
			try {
				MetaSkuType mst = type.selectChild(skuTypeName$,20);
				if (mst == null || mst.getParentId() == null) {
					currect = false;
					lineResult += "'货品子类型'查无结果！";
				} else {
					skuTypeId = mst.getSkuTypeId();
				}
			} catch (Exception e) {
				e.printStackTrace();
				currect = false;
				lineResult += "'货品子类型'有多条记录！";
			}
			try {
				//校验货品名称和货品条码是否存在
				SkuVo reqSkuVo = new SkuVo();
				MetaSku reqSku =  new MetaSku().setSkuName(skuName$).setOrgId(loginUser.getOrgId()).setWarehouseId(LoginUtil.getWareHouseId());
				reqSkuVo.setEntity(reqSku);
				List<MetaSku> retSkuList = this.dao.selectByExample(reqSkuVo.getExample());
				if (retSkuList != null && !retSkuList.isEmpty()) {
					currect = false;
					lineResult += "'货品名称'已有重复记录！";
				}
			} catch (Exception e) {
				currect = false;
				e.printStackTrace();
				lineResult += "'货品名称'有多条记录！";
			}
			try {
				SkuVo reqSkuVo = new SkuVo();
				MetaSku reqSku =  new MetaSku().setSkuBarCode(skuBarCode$);
				reqSkuVo.setEntity(reqSku);
				List<MetaSku> retSkuList = this.dao.selectByExample(reqSkuVo.getExample());
				if (retSkuList != null && !retSkuList.isEmpty()) {
					currect = false;
					lineResult += "'货品条码'已有重复记录！";
				}
			} catch (Exception e) {
				currect = false;
				e.printStackTrace();
				lineResult += "'货品条码'有多条记录！";
			}
			if (skuStatusName != null) {
				String skuStatus = paramService.getValue(CacheName.STATUS_REVERSE, skuStatusName);
				if (skuStatus == null) {
					currect = false;
					lineResult += "根据'状态'查询状态代码失败！";
				} else {
					//找到货品状态编码，根据货品状态中文
					sku.setSkuStatus(Integer.parseInt(skuStatus));
				}
			}
			String measureUnitCode = paramService.getValue(CacheName.MEASURE_UNIT_REVERSE, measureUnit$);
			if (measureUnitCode == null) {
				currect = false;
				lineResult += "根据'计量单位'查询计量单位代码失败！";
			} else {
				sku.setMeasureUnitCode(measureUnitCode);
			}
			String curr = paramService.getValue(CacheName.CURR_CODE_REVERSE, currComment$);
			if(curr == null || curr.isEmpty()){
				currect = false;
				lineResult += "无此币制！";
			}else{
				sku.setCurr(curr);
			}
			String goodsNature = paramService.getValue(CacheName.GOODS_NATURE_REVERSE, goodsNatureComment$);
			if(goodsNature == null || goodsNature.isEmpty()){
				currect = false;
				lineResult += "无此料件性质！";
			}else{
				sku.setGoodsNature(Integer.parseInt(goodsNature));
			}
			String originCountry = paramService.getValue(CacheName.COUNTORY_CODE_REVERSE, originCountryName);
			sku.setOriginCountry(originCountry);
			//判断校验结果
			if (!currect) {
				lineResult = "行" + (i + 2) + ":" + lineResult + "\r\n<br />";
				ret += lineResult;
				continue;
			}
			//检查是否有无效或重复数据，并提示
			//依次查找后续对象是否有重复
			for (int j = i+1; j < skuVoList.size(); j++) {
				SkuVo skuVo2 = skuVoList.get(j);
				MetaSku sku2 = skuVo2.getEntity();
				if (sku2 == null) {
					continue;
				}
				String skuName$2 = sku2.getSkuName();
				String skuBarCode$2 = sku2.getSkuBarCode();
				//判断重复
				if (StringUtil.equals(skuName$,skuName$2) || StringUtil.equals(skuBarCode$,skuBarCode$2)) {
					currect = false;
					lineResult += "与行" + (j + 2) + "数据重复！";
					repeatIndexList.add(j);
				}
			}
			//开始货品导入
			//设置体积
			Double perVolume = NumberUtil.mul(NumberUtil.mul(height$, width$),length$, 6);
			sku.setPerVolume(perVolume);
			skuVo.getMerchant().setMerchantNo(merchantNo);
			//    		sku.setSkuNo(merchantNo + skuBarCode$);
			sku.setOwner(owner);
			sku.setSkuTypeId(skuTypeId);
			if (currect) {
				//字段校验完毕，开始导入操作
				this.add(skuVo, loginUser);
			} else {
				lineResult = "行" + (i + 2) + "：" + lineResult + "\n\r<br />";
				ret += lineResult;
			}
		}
		if (!StringUtil.isTrimEmpty(ret)) {
			//这里是为了回滚事物，不提交有失败的数据
			throw new BizException("err_import_stock_msg", ret);
		}
	}

	/**
	 * 从外部接口导入货品
	 * @param skuVoList
	 * @throws Exception
	 */
	@Transactional(readOnly=false)
	public List<SkuVo> importSkuForExtInface(List<SkuVo> skuVoList) throws Exception{
		Principal loginUser = LoginUtil.getLoginUser();
		// 验证用户是否登录
		if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
			throw new BizException("valid_common_user_no_login");
		}
		if(skuVoList == null || skuVoList.isEmpty()) throw new BizException(ErrorCode.DATA_EMPTY) ;

		List<SkuVo> resultList = new ArrayList<SkuVo>();
		String ret = "";
		List<Integer> repeatIndexList = new ArrayList<Integer>();
		for (int i = 0; i < skuVoList.size(); i++) {
			if (repeatIndexList.contains(i)) {
				continue;
			}
			boolean currect = true;
			String lineResult = "";
			SkuVo skuVo = skuVoList.get(i);
			MetaSku sku = skuVo.getEntity();
			//			if (sku == null) {
			//				currect = false;
			//				lineResult = "行" + (i + 2) + "无数据!\r\n<br />";
			//    			ret += lineResult;
			//    			continue;
			//			}
			//获取当前行数据，带$为必填项
			String merchantName$ = skuVo.getMerchant().getMerchantName();
			String skuName$ = sku.getSkuName();
			String skuBarCode$ = sku.getSkuBarCode();
			String measureUnit$ = sku.getMeasureUnit();
			//			String specModel = sku.getSpecModel();
			Double perWeight$ = sku.getPerWeight();
			Double length$ = sku.getLength();
			Double width$ = sku.getWidth();
			Double height$ = sku.getHeight();
			String skuTypeName$ = skuVo.getSkuTypeName();
			Integer minSafetyStock = sku.getMinSafetyStock();
			Integer maxSafetyStock = sku.getMaxSafetyStock();
			Integer minReplenishStock = sku.getMinReplenishStock();
			Integer maxReplenishStock = sku.getMaxReplenishStock();
			//			String hsCode$ = sku.getHsCode();
			//			String gNo$ = sku.getgNo();
			//			String goodsNatureComment$ = skuVo.getGoodsNatureComment();
			//			String currComment$ = skuVo.getCurrComment();
			//			String originCountryName = skuVo.getOriginCountryName();
			//    		String skuStatusName = skuVo.getSkuStatus();
			String skuNo = skuVo.getEntity().getSkuNo();
			//			String hgGoodsNo = skuVo.getEntity().getHgGoodsNo();
			//检测是否信息不全，即无效

			//查询仓库配置
			MetaWarehouseSettingVo settingVo = warehouseSettingService.findByWarehouseId(LoginUtil.getWareHouseId());
			if (settingVo != null && settingVo.getEntity().getInputSkuNo() == 1) {
				if (!StringUtil.isNoneBlank(skuNo)) {
					currect = false;
					lineResult += "配置了手工输入,'货品代码'不能为空！";
				}
			}
			if (StringUtil.isTrimEmpty(merchantName$)) {
				currect = false;
				lineResult += "'货主名称'不能为空！";
			}
			if (StringUtil.isTrimEmpty(skuName$)) {
				currect = false;
				lineResult += "'货品名称'不能为空！";
			}
			if (StringUtil.isTrimEmpty(skuBarCode$)) {
				currect = false;
				lineResult += "'货品条码'不能为空！";
			}
			if (StringUtil.isTrimEmpty(measureUnit$)) {
				currect = false;
				lineResult += "'计量单位'不能为空！";
			}
			if (perWeight$ == null) {
				currect = false;
				lineResult += "'单重'不能为空！";
			}
			if (length$ == null) {
				currect = false;
				lineResult += "'长'不能为空！";
			}
			if (width$ == null) {
				currect = false;
				lineResult += "'宽'不能为空！";
			}
			if (height$ == null) {
				currect = false;
				lineResult += "'高'不能为空！";
			}
			if (StringUtil.isTrimEmpty(skuTypeName$)) {
				currect = false;
				lineResult += "'货品子类型名称'不能为空！";
			}
			//    		if (StringUtil.isTrimEmpty(gNo$)) {
			//    			currect = false;
			//    			lineResult += "'项号'不能为空！";
			//    		}
			//    		if (StringUtil.isTrimEmpty(goodsNatureComment$)) {
			//    			currect = false;
			//    			lineResult += "'料件性质'不能为空！";
			//    		}
			//    		if (StringUtil.isTrimEmpty(currComment$)) {
			//    			currect = false;
			//    			lineResult += "'币制'不能为空！";
			//    		}
			if (minSafetyStock != null && maxSafetyStock != null && minSafetyStock.intValue() > maxSafetyStock.intValue()) {
				currect = false;
				lineResult += "'安全库存下限'不能大于'安全库存上限'！";
			}
			if (minReplenishStock != null) {
				if (maxReplenishStock == null) {
					currect = false;
					lineResult += "'补货警戒数量'和'补货上限数量'必须同时存在！";
				} else {
					if(minReplenishStock.intValue() > maxReplenishStock.intValue()) {
						currect = false;
						lineResult += "'补货警戒数量'不能大于'补货上限数量'！";
					}
				}
			} else {
				if (maxReplenishStock != null) {
					currect = false;
					lineResult += "'补货警戒数量'和'补货上限数量'必须同时存在！";
				}
			}
			String owner = null;
			String skuTypeId = null;
			String merchantNo = null;
			try {
				//校验关联是否存在
				MetaMerchant mm = service.selectOne(merchantName$);
				if (mm == null) {
					currect = false;
					lineResult += "'货主'查无结果！";
				} else {
					owner = mm.getMerchantId();
					merchantNo = mm.getMerchantNo();
				}
			} catch (Exception e) {
				e.printStackTrace();
				currect = false;
				lineResult += "'货主'有多条记录！";
			}
			try {
				MetaSkuType mst = type.selectChild(skuTypeName$,20);
				if (mst == null || mst.getParentId() == null) {
					currect = false;
					lineResult += "'货品子类型'查无结果！";
				} else {
					skuTypeId = mst.getSkuTypeId();
				}
			} catch (Exception e) {
				e.printStackTrace();
				currect = false;
				lineResult += "'货品子类型'有多条记录！";
			}
			try {
				//校验货品名称和货品条码是否存在
				SkuVo reqSkuVo = new SkuVo();
				MetaSku reqSku =  new MetaSku().setSkuName(skuName$).setOrgId(loginUser.getOrgId()).setWarehouseId(LoginUtil.getWareHouseId());
				reqSkuVo.setEntity(reqSku);
				List<MetaSku> retSkuList = this.dao.selectByExample(reqSkuVo.getExample());
				if (retSkuList != null && !retSkuList.isEmpty()) {
					currect = false;
					lineResult += "'货品名称'"+skuName$+"已有重复记录！";
				}
			} catch (Exception e) {
				currect = false;
				e.printStackTrace();
				lineResult += "'货品名称'"+skuName$+"有多条记录！";
			}
			try {
				SkuVo reqSkuVo = new SkuVo();
				MetaSku reqSku =  new MetaSku().setSkuBarCode(skuBarCode$);
				reqSkuVo.setEntity(reqSku);
				List<MetaSku> retSkuList = this.dao.selectByExample(reqSkuVo.getExample());
				if (retSkuList != null && !retSkuList.isEmpty()) {
					currect = false;
					lineResult += "'货品条码'"+skuBarCode$+"已有重复记录！";
				}
			} catch (Exception e) {
				currect = false;
				e.printStackTrace();
				lineResult += "'货品条码'"+skuBarCode$+"有多条记录！";
			}
			//    		if (skuStatusName != null) {
			//    			String skuStatus = paramService.getValue(CacheName.STATUS_REVERSE, skuStatusName);
			//    			if (skuStatus == null) {
			//        			currect = false;
			//        			lineResult += "根据'状态'查询状态代码失败！";
			//    			} else {
			//            		//找到货品状态编码，根据货品状态中文
			//        			sku.setSkuStatus(Integer.parseInt(skuStatus));
			//    			}
			//    		}
			String measureUnitCode = paramService.getValue(CacheName.MEASURE_UNIT_REVERSE, measureUnit$);
			if (measureUnitCode == null) {
				currect = false;
				lineResult += "根据'计量单位'"+measureUnit$+"查询计量单位代码失败！";
			} else {
				sku.setMeasureUnitCode(measureUnitCode);
			}
			//    		String curr = paramService.getValue(CacheName.CURR_CODE_REVERSE, currComment$);
			//    		if(curr == null){
			//    			currect = false;
			//    			lineResult += "无此币制！";
			//    		}else{
			//    			sku.setCurr(curr);
			//    		}
			//    		String goodsNature = paramService.getValue(CacheName.GOODS_NATURE_REVERSE, goodsNatureComment$);
			//    		if(goodsNature == null){
			//    			currect = false;
			//    			lineResult += "无此料件性质！";
			//    		}else{
			//    			sku.setGoodsNature(Integer.parseInt(goodsNature));
			//    		}
			//    		String originCountry = paramService.getValue(CacheName.COUNTORY_CODE_REVERSE, originCountryName);
			//    		sku.setOriginCountry(originCountry);
			//判断校验结果
			if (!currect) {
				lineResult = "第" + (i + 1) + ":" + lineResult + " ";
				ret += lineResult;
				continue;
			}
			//检查是否有无效或重复数据，并提示
			//依次查找后续对象是否有重复
			for (int j = i+1; j < skuVoList.size(); j++) {
				SkuVo skuVo2 = skuVoList.get(j);
				MetaSku sku2 = skuVo2.getEntity();
				if (sku2 == null) {
					continue;
				}
				String skuName$2 = sku2.getSkuName();
				String skuBarCode$2 = sku2.getSkuBarCode();
				//判断重复
				if (StringUtil.equals(skuName$,skuName$2) || StringUtil.equals(skuBarCode$,skuBarCode$2)) {
					currect = false;
					lineResult += "与第" + (j + 1) + "数据重复！";
					repeatIndexList.add(j);
				}
			}
			//开始货品导入
			//设置体积
			Double perVolume = NumberUtil.mul(NumberUtil.mul(height$, width$),length$, 6);
			sku.setPerVolume(perVolume);
			skuVo.getMerchant().setMerchantNo(merchantNo);
			//    		sku.setSkuNo(merchantNo + skuBarCode$);
			sku.setOwner(owner);
			sku.setSkuTypeId(skuTypeId);
			if (currect) {
				//字段校验完毕，开始导入操作
				ResultModel rm = this.add(skuVo, loginUser);
				SkuVo vo = (SkuVo)rm.getObj();
				SkuVo record = new SkuVo();
				record.setEntity(vo.getEntity());
				resultList.add(record);
			} else {
				lineResult = "第" + (i + 1) + "：" + lineResult + " ";
				ret += lineResult;
			}
		}
		if (!StringUtil.isTrimEmpty(ret)) {
			//这里是为了回滚事物，不提交有失败的数据
			throw new BizException("err_import_stock_msg",ret);
		}
		return resultList;
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
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年4月21日 下午6:20:51<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
	@Override
	public ResponseEntity<byte[]> downloadSkuDemo() throws Exception {
		File file = new File(Thread.currentThread().getContextClassLoader()
				.getResource("excel/template/sku_template.xls").getPath());
		return download("sku_template.xls", file);
	}
	@Override
	public ResponseEntity<byte[]> downloadBatchAjdustmentDemo() throws Exception {
		File file = new File(Thread.currentThread().getContextClassLoader()
				.getResource("excel/template/sku_batch_adjustment.xlsx").getPath());
		return download("sku_batch_adjustment.xlsx", file);
	}
	/**
	 * @Description: 恢复货品
	 * @param id
	 * @return
	 * @throws DaoException
	 * @throws ServiceException
	 */
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public ResultModel recovery(List<String> idList) throws DaoException, ServiceException {
		for (String id : idList) {
			MetaSku old =  dao.selectByPrimaryKey(id);
			if(old == null) throw new ServiceException(ErrorCode.DATA_EMPTY);
			if(old.getSkuStatus() != Constant.STATUS_CANCEL) throw new ServiceException(ErrorCode.STATUS_NO_RIGHT);
			MetaSku obj = new MetaSku();
			obj.setSkuId(id);
			obj.setSkuStatus(Constant.STATUS_OPEN);
			int r = dao.updateByPrimaryKeySelective(obj);
			if(r == 0) throw new DaoException(ErrorCode.DB_EXCEPTION);
		}
		ResultModel m = new ResultModel();
		return m;
	}
	/**
	 * 批量调整货品
	 * @param fileLicense
	 * @throws Exception
	 * zwb
	 */
	@Override
	@Transactional(readOnly=false)
	public void batchAdjustmentSku(MultipartFile fileLicense) throws Exception {

		Principal loginUser = LoginUtil.getLoginUser();
		// 验证用户是否登录
		if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
			throw new BizException("valid_common_user_no_login");
		}
		if (fileLicense == null) {
			throw new BizException("parameter_is_null");
		}
		File newFile = FileUtil.createNewFile(fileLicense);
		List<SkuVo> skuVoList = null;
		if ( newFile != null ) {
			try {
				skuVoList = new ArrayList<SkuVo>();
				List<Object> listImport = ExcelUtil.parseExcel("sku_batch_adjustment", newFile.getAbsolutePath());
				for (int i = 0; i < listImport.size(); i++) {
					Object obj = listImport.get(i);
					if (obj != null) {
						skuVoList.add((SkuVo) obj);
					}
					else {
						SkuVo skuVo = new SkuVo();
						skuVo.setEntity(new MetaSku());
						skuVoList.add(skuVo);
					}
				}
			} catch (Exception e) {
				throw e;
			} finally {
				// 删除文件
				newFile.delete();
			}
		} else {
			throw new BizException("err_stock_imp_no_file");
		}
		String ret = "";
		if ( PubUtil.isEmpty(skuVoList) ) {
			throw new BizException("err_stock_imp_no_date");
		}

		List<Integer> repeatIndexList = new ArrayList<Integer>();
		for (int i = 0; i < skuVoList.size(); i++) {
			String lineResult = "";
			if (repeatIndexList.contains(i)) {
				continue;
			}
			SkuVo skuVo = skuVoList.get(i);
			MetaSku sku = skuVo.getEntity();
			if (StringUtils.isBlank(sku.getSkuNo())) {
				lineResult = "行" + (i + 2) + "无数据!\r\n<br />";
				ret += lineResult;
				continue;
			}
			//检查根据（货主，货品代码）货品是否存在且为生效状态  并检查货品所属仓库 和登录用户是否是一个仓库
			lineResult=findByOwnerSkuno(skuVo,i+2,lineResult);
			//将不能修改的字段置为null 防止误更新  excle中显示只是为了方便客户看

			sku.setSkuName(null);
			sku.setPerWeight(null);
			sku.setMeasureUnit(null);
			sku.setSpecModel(null);
			sku.setLength(null);
			sku.setHeight(null);
			sku.setWidth(null);
			skuVo.setSkuTypeName(null);
			sku.setHsCode(null);

			Double perWeight$ = sku.getPerWeight();
			String skuTypeName$ = skuVo.getSkuTypeName();
			Integer minSafetyStock = sku.getMinSafetyStock();
			Integer maxSafetyStock = sku.getMaxSafetyStock();
			Integer minReplenishStock = sku.getMinReplenishStock();
			Integer maxReplenishStock = sku.getMaxReplenishStock();
			String goodsNatureComment$ = skuVo.getGoodsNatureComment();
			String currComment$ = skuVo.getCurrComment();
			String originCountryName = skuVo.getOriginCountryName();
			//检测是否信息不全，即无效
			if(perWeight$!=null&&(perWeight$ <=0 || perWeight$ > 100)){
				lineResult += "'单重'不能超出0~100！";
			}else if(perWeight$!=null&&(perWeight$ >=0 && perWeight$ < 100)){
				sku.setPerWeight(NumberUtil.subScale(perWeight$, 0d, 4));
			}
			if (minSafetyStock != null && maxSafetyStock != null && minSafetyStock.intValue() > maxSafetyStock.intValue()) {
				lineResult += "'安全库存下限'不能大于'安全库存上限'！";
			}
			if (minReplenishStock != null) {
				if (maxReplenishStock == null) {
					lineResult += "'补货警戒数量'和'补货上限数量'必须同时存在！";
				} else {
					if(minReplenishStock.intValue() > maxReplenishStock.intValue()) {
						lineResult += "'补货警戒数量'不能大于'补货上限数量'！";
					}
				}
			} else {
				if (maxReplenishStock != null) {
					lineResult += "'补货警戒数量'和'补货上限数量'必须同时存在！";
				}
			}

			//设置货品值类型 zwb
			if(StringUtils.isNotBlank(skuTypeName$)){
				String skuTypeId = null;
				try {
					MetaSkuType mst = type.selectChild(skuTypeName$,20);
					if (mst == null || mst.getParentId() == null) {
						lineResult += "'货品子类型'查无结果！";
					} else {
						skuTypeId = mst.getSkuTypeId();
						skuVo.getEntity().setSkuTypeId(skuTypeId);
					}
				} catch (Exception e) {
					e.printStackTrace();
					lineResult += "'货品子类型'有多条记录！";
				}
			}

			//币制 zwb
			if(StringUtils.isNotBlank(currComment$)){
				String curr = paramService.getValue(CacheName.CURR_CODE_REVERSE, currComment$);
				if(curr == null || curr.isEmpty()){
					lineResult += "无此币制！";
				}else{
					sku.setCurr(curr);
				}
			}

			// 材料性质 zwb
			if(StringUtils.isNotBlank(goodsNatureComment$)){
				String goodsNature = paramService.getValue(CacheName.GOODS_NATURE_REVERSE, goodsNatureComment$);
				if(goodsNature == null || goodsNature.isEmpty()){
					lineResult += "无此料件性质！";
				}else{
					sku.setGoodsNature(Integer.parseInt(goodsNature));
				}
			}

			// 原产国 zwb
			if(StringUtils.isNotBlank(originCountryName)){
				String originCountry = paramService.getValue(CacheName.COUNTORY_CODE_REVERSE, originCountryName);
				if(originCountry==null){
					lineResult += "无此国家！";
				}
				sku.setOriginCountry(originCountry);
			}

			//判断校验结果
			if (!StringUtils.isBlank(lineResult)) {
				lineResult = "行" + (i + 2) + ":" + lineResult + "\r\n<br />";
				ret += lineResult;
				continue;
			}else{
				//字段校验完毕，更新
				dao.updateByExampleSelective(sku, skuVo.getExample());
			} 
		}
		if (!StringUtil.isTrimEmpty(ret)) {
			//这里是为了回滚事物，不提交有失败的数据
			throw new BizException("err_import_stock_msg", ret);
		}
	}
	private String findByOwnerSkuno(SkuVo skuVo,int index,String lineResult){
		String skuNo=skuVo.getEntity().getSkuNo();
		String owner=skuVo.getMerchant().getMerchantName();
		String warehouseId=LoginUtil.getWareHouseId();
		skuVo.getEntity().setWarehouseId(warehouseId);
		if(StringUtils.isBlank(skuNo)){
			lineResult+="第"+index+"行："+"货品代码不许为空";
		}
		if(StringUtils.isBlank(owner)){
			lineResult+="第"+index+"行："+"货主不许为空";
		}
		List<MetaSku>list=dao.findByOwnerSkuno(skuVo);
		if(list.isEmpty()){
			lineResult+="此货品不存在";
		}else {
			if(Constant.STATUS_ACTIVE!=list.get(0).getSkuStatus()){
				lineResult+="货品不在生效状态不允许调整";
			}
		}
		return lineResult;
	}

	@Override
	public MetaSku getBySkuNo(String skuNo) {
		Principal p = LoginUtil.getLoginUser();
		String warehouseId = LoginUtil.getWareHouseId();
		SkuVo skuVo = new SkuVo();
		skuVo.getEntity().setSkuNo(skuNo);
		skuVo.getEntity().setWarehouseId(warehouseId);
		skuVo.getEntity().setOrgId(p.getOrgId());
		List<MetaSku> obj = dao.selectByExample(skuVo.getExample());
		if (obj == null || obj.isEmpty()) {
			throw new BizException("err_stock_imp_sku", skuNo);
		}
		return obj.get(0);
	}
	@Override
	public ResponseEntity<byte[]> skuExport(List<String>ids) throws Exception {
		SkuVo skuVo=new SkuVo();
		skuVo.setIds(ids);
		List<MetaSku> list = dao.selectByExample(skuVo.getExample());
		List<SkuVo>voList=fill(list);
		// 导出入库清单  
		List<SkuExportExcel>eList=new ArrayList<>();
		if(voList!=null&&voList.size()>0){
			for(SkuVo vo:voList){
				eList.add(chVo2SkuExportExcel(vo));
			}
		}
		ExcelExport<SkuExportExcel> ex = new ExcelExport<SkuExportExcel>();  
		String[] headers = { 
				"货主简称","货品名称","货品代码","计量单位",
				"规格型号","单重（kg）","长(M)","宽(M)",
				"高(M)","货品分类2","安全库存下限","安全库存上限",
				"补货库存下限","补货库存上限","海关货号","项号","料件性质",
				"币制","原产国","海关税号","第一(法定)单位","第二单位","单价（元）",
				"申报价（元）","品牌","属性1","属性2","属性3","属性4","保质期（天）",
				"提前预警期（天）","备注","状态"
		};  

		ResponseEntity<byte[]> bytes = ex.exportExcel2Array("货品明细", headers, eList, "yyyy-MM-dd", "货品明细.xls");
		return bytes;
	}
	private SkuExportExcel chVo2SkuExportExcel(SkuVo vo){
		SkuExportExcel skuExportExcel=new SkuExportExcel();
		skuExportExcel.setMerchantName(vo.getMerchant().getMerchantShortName());
		skuExportExcel.setSkuName(vo.getEntity().getSkuName());
		skuExportExcel.setSkuNo(vo.getEntity().getSkuNo());
		skuExportExcel.setMeasureUnit(vo.getEntity().getMeasureUnit());
		skuExportExcel.setSpecModel(vo.getEntity().getSpecModel());
		skuExportExcel.setPerWeight(vo.getEntity().getPerWeight());
		skuExportExcel.setLength(vo.getEntity().getLength());
		skuExportExcel.setWidth(vo.getEntity().getWidth());
		skuExportExcel.setHeight(vo.getEntity().getHeight());
		skuExportExcel.setSkuTypeName(vo.getSkuTypeName());
		skuExportExcel.setMinSafetyStock(vo.getEntity().getMinSafetyStock());
		skuExportExcel.setMaxSafetyStock(vo.getEntity().getMaxSafetyStock());
		skuExportExcel.setMinReplenishStock(vo.getEntity().getMinReplenishStock());
		skuExportExcel.setMaxReplenishStock(vo.getEntity().getMaxReplenishStock());
		skuExportExcel.setHsCode(vo.getEntity().getHsCode());
		skuExportExcel.setGNo(vo.getEntity().getgNo());
		skuExportExcel.setGoodsNatureComment(vo.getGoodsNatureComment());
		skuExportExcel.setCurrComment(vo.getCurrComment());
		skuExportExcel.setOriginCountryName(vo.getOriginCountryName());
		skuExportExcel.setHgGoodsNo(vo.getEntity().getHgGoodsNo());
		skuExportExcel.setUnit1(vo.getEntity().getUnit1());
		skuExportExcel.setUnit2(vo.getEntity().getUnit2());
		skuExportExcel.setPerPrice(vo.getEntity().getPerPrice());
		skuExportExcel.setDeclarePrice(vo.getEntity().getDeclarePrice());
		skuExportExcel.setBrand(vo.getEntity().getBrand());
		skuExportExcel.setAttribute1(vo.getEntity().getAttribute1());
		skuExportExcel.setAttribute2(vo.getEntity().getAttribute2());
		skuExportExcel.setAttribute3(vo.getEntity().getAttribute3());
		skuExportExcel.setAttribute4(vo.getEntity().getAttribute4());
		skuExportExcel.setShelflife(vo.getEntity().getShelflife());
		skuExportExcel.setOverdueWarningDays(vo.getEntity().getOverdueWarningDays());
		skuExportExcel.setNote(vo.getEntity().getNote());
		skuExportExcel.setSkuStatus(vo.getSkuStatus());
		return skuExportExcel;
		
	}

}