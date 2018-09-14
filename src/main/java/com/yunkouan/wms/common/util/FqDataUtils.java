package com.yunkouan.wms.common.util;

import java.util.List;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.yunkouan.entity.ResultModel;
import com.yunkouan.exception.ServiceException;
import com.yunkouan.saas.modules.sys.entity.MetaArea;
import com.yunkouan.saas.modules.sys.entity.MetaLocation;
import com.yunkouan.saas.modules.sys.entity.MetaWarehouse;
import com.yunkouan.saas.modules.sys.entity.SysOrg;
import com.yunkouan.saas.modules.sys.entity.SysUser;
import com.yunkouan.saas.modules.sys.service.IOrgService;
import com.yunkouan.saas.modules.sys.service.IUserService;
import com.yunkouan.saas.modules.sys.service.IWarehouseExtlService;
import com.yunkouan.util.PubUtil;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.modules.meta.entity.MetaMerchant;
import com.yunkouan.wms.modules.meta.entity.MetaPack;
import com.yunkouan.wms.modules.meta.entity.MetaSku;
import com.yunkouan.wms.modules.meta.entity.MetaSkuType;
import com.yunkouan.wms.modules.meta.service.IAreaExtlService;
import com.yunkouan.wms.modules.meta.service.ILocationExtlService;
import com.yunkouan.wms.modules.meta.service.IMerchantService;
import com.yunkouan.wms.modules.meta.service.IPackService;
import com.yunkouan.wms.modules.meta.service.ISkuService;
import com.yunkouan.wms.modules.meta.service.ISkuTypeService;
import com.yunkouan.wms.modules.meta.vo.MerchantVo;
import com.yunkouan.wms.modules.rec.entity.RecAsn;
import com.yunkouan.wms.modules.rec.entity.RecAsnDetail;
import com.yunkouan.wms.modules.rec.entity.RecPutawayDetail;
import com.yunkouan.wms.modules.rec.service.IASNDetailExtlService;
import com.yunkouan.wms.modules.rec.service.IASNExtlService;
import com.yunkouan.wms.modules.rec.service.IPutawayDetailExtlService;
import com.yunkouan.wms.modules.send.entity.SendWave;
import com.yunkouan.wms.modules.send.service.IDeliveryService;
import com.yunkouan.wms.modules.send.service.IWaveService;
import com.yunkouan.wms.modules.send.vo.SendDeliveryVo;
import com.yunkouan.wms.modules.send.vo.SendWaveVo;

/**
 * 常用数据查询
 * 
 * Note：此类提供了常用数据的查询方法。
 * 为了避免在查找相同数据时，反复执行查询数据库的操作，常用的数据会在内存中保存起来，查找会优先在内存中获取。
 * @author Aaron
 *
 */
public class FqDataUtils {
	public static String getOrgNameById(IOrgService orgService,String id){
		// 缓存导致名称修改后没变化 hetianping 20170927
//		SysOrg record = null;
//		if(orgMap.containsKey(id)){
//			record = orgMap.get(id);
//		}else{
//			SysOrg i_org = new SysOrg();
//			i_org.setOrgId(id);
//			record = orgService.query(i_org);
//			if(record != null) orgMap.put(id, record);
//		}
//		if(record == null) return null;
//		return record.getOrgName();
		if(StringUtils.isBlank(id)) return null;
		SysOrg org = orgService.query(new SysOrg().setOrgId(id));
		if(org == null) return null;
		return org.getOrgName();
	}

	public static MetaMerchant getMerchantById(IMerchantService merchantService,String id){
		// 缓存导致名称修改后没变化 hetianping 20170927
//		if ( StringUtil.isTrimEmpty(id) ) {
//			return null;
//		}
//		MetaMerchant record = null;
//		if(merchantMap.containsKey(id)){
//			record = merchantMap.get(id);
//		}else{
//			record = merchantService.get(id);
//			if(record != null) merchantMap.put(id, record);
//		}
//		if(record == null) return null;
//		return record;
		if(StringUtils.isBlank(id)) return null;
		MetaMerchant entity = merchantService.get(id);
		return entity;
	}

	public static String getMerchantNameById(IMerchantService merchantService,String id){
		// 缓存导致名称修改后没变化 hetianping 20170927
//		if ( StringUtil.isTrimEmpty(id) ) {
//			return null;
//		}
//		MetaMerchant record = null;
//		if(merchantMap.containsKey(id)){
//			record = merchantMap.get(id);
//		}else{
//			record = merchantService.get(id);
//			if(record != null) merchantMap.put(id, record);
//		}
//		if(record == null) return null;
//		return record.getMerchantName();
		if(StringUtils.isBlank(id)) return "";
		MetaMerchant entity = merchantService.get(id);
		if(entity == null) return "";
		return entity.getMerchantName();
	}
	
	/**
	 * 根据客商编号，查询客商
	 * @param merchantService 客商业务类
	 * @param no 客商编号
	 * @return
	 * @version 2017年3月23日 上午11:36:35<br/>
	 * @author andy wang<br/>
	 * @throws ServiceException 
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public static MetaMerchant getMerchantByNo(IMerchantService merchantService,String no) throws Exception {
		if ( StringUtil.isTrimEmpty(no) ) {
			return null;
		}
		MetaMerchant record = null;
		MerchantVo vo = new MerchantVo(new MetaMerchant());
		vo.getEntity().setMerchantNo(no);
		Principal p = LoginUtil.getLoginUser();
		ResultModel rm = merchantService.list(vo, p);
		List<MerchantVo> listMerchant = rm.getList();
		if ( !PubUtil.isEmpty(listMerchant) ) {
			record = listMerchant.get(0).getEntity();
		}
		return record;
	}
	
	public static String getWarehouseNameById(IWarehouseExtlService warehouseExtlService,String id) throws Exception{
		if ( StringUtil.isTrimEmpty(id) ) {
			return null;
		}
		MetaWarehouse record = warehouseExtlService.findWareHouseById(id);
		if(record == null) return null;
		return record.getWarehouseName();
	}
	
	public static String getSkuNameById(ISkuService skuService,String id) throws Exception{
		MetaSku record = skuService.get(id);
		if(record == null) return null;
		return record.getSkuName();
	}
	
	public static String getPackUnit(IPackService packService,String id){
		if ( StringUtil.isTrimEmpty(id) ) {
			return null;
		}
		MetaPack record = packService.get(id);
		if(record == null) return null;
		return record.getPackUnit();
	}
	
	
	/**
	 * 根据库位id，查询库位名
	 * @param locExtlService 库位业务类
	 * @param id 库位id
	 * @return 对应的库位名
	 * @throws Exception
	 * @version 2017年3月14日 下午5:43:28<br/>
	 * @author andy wang<br/>
	 */
	public static String getLocNameById(ILocationExtlService locExtlService,String id) throws Exception {
		MetaLocation record = getLocById(locExtlService, id);
		return record == null ? null : record.getLocationName();
	}
	
	/**
	 * 根据库位编号，查询库位
	 * @param locExtlService 库位业务类 
	 * @param no 库位编号
	 * @return 库位对象
	 * @throws Exception
	 * @version 2017年7月17日 上午11:41:29<br/>
	 * @author andy wang<br/>
	 */
	public static MetaLocation getLocByNo(ILocationExtlService locExtlService,String no) throws Exception {
		if ( StringUtil.isTrimEmpty(no) ) {
			return null;
		}
		MetaLocation record = locExtlService.findLocByNo(no, null);
		return record;
	}
	
	/**
	 * 根据库位id，查询库位
	 * @param locExtlService 库位业务类
	 * @param id 库位id
	 * @return 库位
	 * @throws Exception
	 * @version 2017年3月16日 下午9:20:39<br/>
	 * @author andy wang<br/>
	 */
	public static MetaLocation getLocById ( ILocationExtlService locExtlService , String id ) throws Exception {
		if ( StringUtil.isTrimEmpty(id) ) {
			return null;
		}
		MetaLocation record = locExtlService.findLocById(id);
		return record;
	}
	
	
	/**
	 * 根据用户id，查询用户名
	 * @param userService 用户业务类
	 * @param id 用户id
	 * @return 对应用户名
	 * @throws Exception
	 * @version 2017年3月15日 上午10:42:28<br/>
	 * @author andy wang<br/>
	 */
	public static String getUserNameById ( IUserService userService , String id ) throws Exception {
		if ( StringUtil.isTrimEmpty(id) ) {
			return null;
		}
		SysUser record = userService.get(id);
		return record!= null ? record.getUserName() : null;
	}
	
	
	/**
	 * 根据货品id，查询货品规格型号
	 * @param skuService 货品业务类
	 * @param id 货品id
	 * @return 货品规格型号
	 * @throws Exception
	 * @version 2017年3月16日 上午11:40:28<br/>
	 * @author andy wang<br/>
	 */
	public static String getSkuSpecById(ISkuService skuService,String id) throws Exception {
		if ( StringUtil.isTrimEmpty(id) ) {
			return null;
		}
		MetaSku record = skuService.get(id);
		return record == null ? null : record.getSpecModel();
	}
	
	public static String getDeliveryNoById(IDeliveryService deliveryService,String id){
		if ( StringUtil.isTrimEmpty(id) ) {
			return null;
		}
		SendDeliveryVo record = deliveryService.getDeliveryById(id);
		return record == null ? null : record.getSendDelivery().getDeliveryNo();
	}
	
	/**
	 * 根据
	 * @param waveService
	 * @param id
	 * @return
	 */
	public static String getWaveNoById(IWaveService waveService,String id){
		if ( StringUtil.isTrimEmpty(id) ) {
			return null;
		}
		SendWave record = waveService.getEntityById(id);
		return record == null ? null : record.getWaveNo();
	}
	
	public static MetaSku getSkuById(ISkuService skuService,String id) throws Exception{
		MetaSku record = skuService.get(id);
		return record;
	}
	
	public static MetaSkuType getSkuTypeById(ISkuTypeService skuTypeService,String id) throws Exception{
		MetaSkuType record = skuTypeService.get(id);
		return record;
	}
	/**
	 * 根据Asn单id，查询Asn单
	 * @param asnExtlService Asn单外调业务类
	 * @param id Asn单id
	 * @return 对应的Asn单
	 * @throws Exception
	 * @version 2017年3月24日 下午2:31:10<br/>
	 * @author andy wang<br/>
	 */
	public static RecAsn getAsnById (IASNExtlService asnExtlService ,String id ) throws Exception {
		RecAsn record = asnExtlService.findAsnById(id);
		return record;
	}
	
	
	/**
	 * 根据id，查询ASN单明细
	 * @param asnDetailExtlService ASN单明细外调业务类
	 * @param id ASN单明细id
	 * @return ASN单明细
	 * @throws Exception
	 * @version 2017年3月25日 上午10:31:34<br/>
	 * @author andy wang<br/>
	 */
	public static RecAsnDetail getAsnDetailById (IASNDetailExtlService asnDetailExtlService ,String id ) throws Exception {
		RecAsnDetail record = asnDetailExtlService.findByDetailId(id);
		return record;
	}
	
	/**
	 * 根据上架单明细id，查找上架单明细
	 * @param ptwDetailExtlService 上架单明细外调业务类
	 * @param id 上架单明细id
	 * @return 上架单明细
	 * @throws Exception
	 * @version 2017年3月25日 上午10:38:27<br/>
	 * @author andy wang<br/>
	 */
	public static RecPutawayDetail getPtwDetailById (IPutawayDetailExtlService ptwDetailExtlService ,String id ) throws Exception {
		RecPutawayDetail record = ptwDetailExtlService.findPtwDetailById(id);
		return record;
	}
	
	/**
	 * 添加上架单明细
	 * @param ptwDetailId 上架单明细id
	 * @param ptwDetail 上架单明细对象
	 * @throws Exception
	 * @version 2017年3月26日 下午6:21:51<br/>
	 * @author andy wang<br/>
	 */
//	public static void putPtwDetail ( String ptwDetailId , RecPutawayDetail ptwDetail ) throws Exception {
//		if ( this.mapPtwDetail == null ) {
//			this.mapPtwDetail = new HashMap<String,RecPutawayDetail>();
//		}
//		this.mapPtwDetail.put(ptwDetailId, ptwDetail);
//	}
	
	/**
	 * 根据id查询库区名称
	 * @param areaExtlService 库区外调接口
	 * @param id 库区id
	 * @return 库区名
	 * @throws Exception
	 * @version 2017年4月4日 下午3:28:03<br/>
	 * @author andy wang<br/>
	 */
	public static String getAreaNameById ( IAreaExtlService areaExtlService , String id  ) throws Exception {
		MetaArea record = getAreaById(areaExtlService, id);
		if(record == null) return null;
		return record.getAreaName();
	}
	
	/**
	 * 根据id查询库区名称
	 * @param areaExtlService 库区外调接口
	 * @param id 库区id
	 * @return 库区名
	 * @throws Exception
	 * @version 2017年5月12日 下午5:23:24<br/>
	 * @author andy wang<br/>
	 */
	public static MetaArea getAreaById ( IAreaExtlService areaExtlService , String id  ) throws Exception {
		MetaArea record = areaExtlService.findAreaById(id);
		return record;
	}
}