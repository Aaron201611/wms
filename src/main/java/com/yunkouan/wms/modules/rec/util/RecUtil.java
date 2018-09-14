/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * 创建日期:<br/> 2017年2月10日 下午3:40:17<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.rec.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.yunkouan.wms.common.constant.BillPrefix;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.inv.entity.InvLog;
import com.yunkouan.wms.modules.inv.entity.InvStock;
import com.yunkouan.wms.modules.inv.vo.InvStockVO;
import com.yunkouan.wms.modules.rec.entity.RecAsn;
import com.yunkouan.wms.modules.rec.entity.RecAsnDetail;
import com.yunkouan.wms.modules.rec.entity.RecPutaway;
import com.yunkouan.wms.modules.rec.entity.RecPutawayDetail;
import com.yunkouan.wms.modules.rec.entity.RecPutawayLocation;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 收货工具类<br/><br/>
 * <b>创建日期</b>:<br/> 2017年2月10日 下午3:40:17<br/>
 * @author andy wang<br/>
 */
public class RecUtil {
	
	/**
	 * 设置上架单对象默认计划/实际上架数量、重量、体积
	 * @param ptw 上架单对象
	 * @throws Exception
	 * @version 2017年4月19日 上午9:54:19<br/>
	 * @author andy wang<br/>
	 */
	public static void defPtwQWV ( RecPutaway ptw ) throws Exception {
		if ( ptw == null ) {
			return;
		}
		if ( ptw.getPlanQty() == null ) {
			ptw.setPlanQty(0d);
		}
		if ( ptw.getRealQty() == null ) {
			ptw.setRealQty(0d);
		}
		if ( ptw.getPlanWeight() == null ) {
			ptw.setPlanWeight(0d);
		}
		if ( ptw.getRealWeight() == null ) {
			ptw.setRealWeight(0d);
		}
		if ( ptw.getPlanVolume() == null ) {
			ptw.setPlanVolume(0d);
		}
		if ( ptw.getRealVolume() == null ) {
			ptw.setRealVolume(0d);
		}
	}
	
	
	/**
	 * 根据上架单单，创建库存对象
	 * @param recPutaway ASN单对象
	 * @param recPutawayDetail ASN单明细对象
	 * @param locationId 库位id
	 * @param qty 上架数量
	 * @param opType 操作类型
	 * <br/>—— Constant.STOCK_LOG_OP_TYPE_IN 入库
	 * <br/>—— Constant.STOCK_LOG_OP_TYPE_OUT 出库
	 * @return 库存对象
	 * @version 2017年3月3日 下午1:39:49<br/>
	 * @author andy wang<br/>
	 */
	public static InvStockVO createStock ( RecPutaway recPutaway, RecPutawayDetail recPutawayDetail 
			, String locationId , Double qty , Integer opType ) {
		Principal loginUser = LoginUtil.getLoginUser();
		InvStockVO invStockVO = new InvStockVO();
		if ( invStockVO.getInvStock() == null ) {
			invStockVO.setInvStock(new InvStock());
		}
		InvStock invStock = invStockVO.getInvStock();
		invStockVO.setFindNum(qty);
		invStock.setLocationId(locationId);
		invStock.setSkuId(recPutawayDetail.getSkuId());
		invStock.setPackId(recPutawayDetail.getPackId());
		invStock.setBatchNo(recPutawayDetail.getBatchNo());
		invStock.setInDate(new Date());
		invStock.setSkuStatus(Constant.STOCK_SKU_STATUS_NORMAL);
		invStock.setIsBlock(Constant.STOCK_BLOCK_FALSE);
		invStock.setCreatePerson(loginUser.getUserId());
		invStock.setUpdatePerson(loginUser.getUserId());
		invStock.setCreateTime(new Date());
		invStock.setUpdateTime(new Date());
		invStock.setPickQty(0d);
		invStock.setAsnId(recPutawayDetail.getAsnId());
		invStock.setAsnDetailId(recPutawayDetail.getAsnDetailId());
		invStock.setOwner(recPutawayDetail.getOwner());
		// 创建日志
		InvLog invLog = new InvLog();
		invLog.setOpPerson(loginUser.getUserId());
		invLog.setOpType(opType);
		invLog.setInvoiceBill(recPutaway.getPutawayNo());
		invLog.setLogType(Constant.STOCK_LOG_TYPE_PUTAWAY);
		invLog.setLocationId(locationId);
		invLog.setSkuId(recPutawayDetail.getSkuId());
		invLog.setQty(qty);
		invLog.setBatchNo(recPutawayDetail.getBatchNo());
		invLog.setSkuStatus(recPutawayDetail.getSkuStatus());
		invStockVO.setInvLog(invLog);
		return invStockVO;
	}
	
	
	/**
	 * 获取拆分上架单号
	 * @param putawayNo 上架单号
	 * @return 集合包含拆分后的两个单号
	 * @version 2017年3月2日 上午9:49:08<br/>
	 * @author andy wang<br/>
	 */
	public static List<String> createSplitPutawayNo( String putawayNo ) {
		List<String> listSplitNo = new ArrayList<String>();
		listSplitNo.add(putawayNo+"-01");
		listSplitNo.add(putawayNo+"-02");
		return listSplitNo;
	}
	
	
	/**
	 * 根据单据类型，获取编号前缀
	 * @param docType 单据类型
	 * @return 编号前缀
	 * @version 2017年2月22日 下午5:34:34<br/>
	 * @author andy wang<br/>
	 */
	public static BillPrefix type2NoPrefix( Integer docType ) {
		BillPrefix prefix = BillPrefix.DOCUMENT_PREFIX_INCOMING_NORMAL;
		if ( Constant.ASN_DOCTYPE_NORMAL == docType ) {
			prefix = BillPrefix.DOCUMENT_PREFIX_INCOMING_NORMAL;
		} else if ( Constant.ASN_DOCTYPE_OTHER == docType ) {
			prefix = BillPrefix.DOCUMENT_PREFIX_INCOMING_OTHER;
		} else if ( Constant.ASN_DOCTYPE_REJECT == docType ) {
			prefix = BillPrefix.DOCUMENT_PREFIX_INCOMING_REJECTION;
		} else if ( Constant.ASN_DOCTYPE_RETURNED == docType ) {
			prefix = BillPrefix.DOCUMENT_PREFIX_INCOMING_BACK;
		} else if ( Constant.ASN_DOCTYPE_TRANSFER == docType ) {
			prefix = BillPrefix.DOCUMENT_PREFIX_INCOMING_TRANSFER;
		}
		return prefix;
	}
	
	/**
	 * 创建上架单操作明细对象，并把上架单明细对象中的属性复制到上架单操作明细对象
	 * @param recPutawayLocation
	 * @param recPutawayDetail
	 * @return
	 * @version 2017年2月22日 下午4:52:23<br/>
	 * @author andy wang<br/>
	 */
	public static RecPutawayLocation createPtwLocation ( RecPutawayLocation recPutawayLocation , RecPutawayDetail recPutawayDetail ) {
		if ( recPutawayLocation == null || recPutawayDetail == null ) {
			return null;
		}
		recPutawayLocation.setPutawayDetailId(recPutawayDetail.getPutawayDetailId());
		recPutawayLocation.setPackId(recPutawayDetail.getPackId());
		recPutawayLocation.setMeasureUnit(recPutawayDetail.getMeasureUnit());
		return recPutawayLocation;
	}
	
	/**
	 * 创建上架单明细对象，并把Asn单明细对象中的属性复制到上架单明细对象
	 * @param recPutawayDetail 上架单明细对象
	 * @param recAsnDetail Asn单明细
	 * @return 上架单明细对象
	 * @version 2017年2月22日 下午4:43:02<br/>
	 * @author andy wang<br/>
	 */
	public static RecPutawayDetail createPtwDetail ( RecPutawayDetail recPutawayDetail , RecAsnDetail recAsnDetail ) {
		if ( recPutawayDetail == null || recAsnDetail == null ) {
			return null;
		}
		recPutawayDetail.setAsnId(recAsnDetail.getAsnId());
		recPutawayDetail.setAsnDetailId(recAsnDetail.getAsnDetailId());
		recPutawayDetail.setSkuId(recAsnDetail.getSkuId());
		recPutawayDetail.setOrgId(recAsnDetail.getOrgId());
		recPutawayDetail.setWarehouseId(recAsnDetail.getWarehouseId());
		recPutawayDetail.setPackId(recAsnDetail.getPackId());
		recPutawayDetail.setBatchNo(recAsnDetail.getBatchNo());
		recPutawayDetail.setMeasureUnit(recAsnDetail.getMeasureUnit());
		return recPutawayDetail;
	}
	
	/**
	 * 根据ASN单，创建库存对象
	 * @param recAsn ASN单对象
	 * @param recAsnDetail ASN单明细对象
	 * @param qty 数量
	 * @param locationId 库位id
	 * @param opType 操作类型
	 * <br/>—— Constant.STOCK_LOG_OP_TYPE_IN 入库
	 * <br/>—— Constant.STOCK_LOG_OP_TYPE_OUT 出库
	 * @return 库存对象
	 * @version 2017年2月22日 上午11:22:20<br/>
	 * @author andy wang<br/>
	 */
	public static InvStockVO createStock ( RecAsn recAsn , RecAsnDetail recAsnDetail 
			, Double qty , String locationId , Integer opType ) {
		Principal loginUser = LoginUtil.getLoginUser();
		InvStock invStock = new InvStock();
		InvStockVO invStockVO = new InvStockVO(invStock);
		invStockVO.setFindNum(qty);
		invStock.setLocationId(locationId);
		invStock.setAsnId(recAsnDetail.getAsnId());
		invStock.setAsnDetailId(recAsnDetail.getAsnDetailId());
		invStock.setOwner(recAsn.getOwner());
		invStock.setSkuId(recAsnDetail.getSkuId());
		invStock.setPackId(recAsnDetail.getPackId());
		invStock.setBatchNo(recAsnDetail.getBatchNo());
		invStock.setInDate(new Date());
		invStock.setSkuStatus(Constant.STOCK_SKU_STATUS_NORMAL);
		invStock.setOrgId(loginUser.getOrgId());
		invStock.setWarehouseId(LoginUtil.getWareHouseId());
		invStock.setIsBlock(0);
		invStock.setCreatePerson(loginUser.getUserId());
		invStock.setUpdatePerson(loginUser.getUserId());
		invStock.setCreateTime(new Date());
		invStock.setUpdateTime(new Date());
		invStock.setPickQty(0d);
		invStock.setProduceDate(recAsnDetail.getProduceDate());
		invStock.setExpiredDate(recAsnDetail.getExpiredDate());
		invStock.setOwner(recAsn.getOwner());
		// 创建日志
		InvLog invLog = new InvLog();
		invLog.setBatchNo(recAsnDetail.getBatchNo());
		invLog.setOpPerson(loginUser.getUserId());
		invLog.setOpType(opType);
		invLog.setInvoiceBill(recAsn.getAsnNo());
		invLog.setLogType(Constant.STOCK_LOG_TYPE_RECEIPT);
		invLog.setLocationId(locationId);
		invLog.setSkuId(recAsnDetail.getSkuId());
		invLog.setQty(qty);
		invStockVO.setInvLog(invLog);
		return invStockVO;
	}
	
	/**
	 * 设置上架单操作明细的实际上架数量/重量/体积默认值
	 * @param recPutawayDetail 上架单明细
	 * @version 2017年2月21日 上午11:03:30<br/>
	 * @author andy wang<br/>
	 */
	public static void defPutawayDetailRealQWV ( RecPutawayDetail recPutawayDetail ) {
		if ( recPutawayDetail == null ) {
			return;
		}
		if ( recPutawayDetail.getRealPutawayQty() == null ) {
			recPutawayDetail.setRealPutawayQty(0d);
		}
		if ( recPutawayDetail.getRealPutawayWeight() == null ) {
			recPutawayDetail.setRealPutawayWeight(0d);
		}
		if ( recPutawayDetail.getRealPutawayVolume() == null ) {
			recPutawayDetail.setRealPutawayVolume(0d);
		}
	}
	
	/**
	 * 设置上架单操作明细的计划上架数量/重量/体积默认值
	 * @param recPutawayDetail 上架单明细
	 * @version 2017年2月21日 上午11:01:28<br/>
	 * @author andy wang<br/>
	 */
	public static void defPutawayDetailPlanQWV ( RecPutawayDetail recPutawayDetail ) {
		if ( recPutawayDetail == null ) {
			return;
		}
		if ( recPutawayDetail.getPlanPutawayQty() == null ) {
			recPutawayDetail.setPlanPutawayQty(0d);
		}
		if ( recPutawayDetail.getPlanPutawayWeight() == null ) {
			recPutawayDetail.setPlanPutawayWeight(0d);
		}
		if ( recPutawayDetail.getPlanPutawayVolume() == null ) {
			recPutawayDetail.setPlanPutawayVolume(0d);
		}
	}
	
	/**
	 * 设置上架单操作明细的上架数量/重量/体积默认值
	 * @param putawayLocation 上架单操作明细
	 * @version 2017年2月21日 上午11:01:33<br/>
	 * @author andy wang<br/>
	 */
	public static void defLocationQWV( RecPutawayLocation putawayLocation ) {
		if ( putawayLocation == null ) {
			return;
		}
		if ( putawayLocation.getPutawayQty() == null ) {
			putawayLocation.setPutawayQty(0d);
		}
		if ( putawayLocation.getPutawayWeight() == null ) {
			putawayLocation.setPutawayWeight(0d);
		}
		if ( putawayLocation.getPutawayVolume() == null ) {
			putawayLocation.setPutawayVolume(0d);
		}
	}
	
	/**
	 * 设置Asn单的收货数量/重量/体积默认值
	 * @param asn Asn单
	 * @version 2017年2月18日 下午12:50:05<br/>
	 * @author andy wang<br/>
	 */
	public static void defReceiveQWV( RecAsn asn ) {
		if ( asn == null ) {
			return;
		}
		if ( asn.getReceiveQty() == null ) {
			asn.setReceiveQty(0d);
		}
		if ( asn.getReceiveWeight() == null ) {
			asn.setReceiveWeight(0d);
		}
		if ( asn.getReceiveVolume() == null ) {
			asn.setReceiveVolume(0d);
		}
	}
	
	/**
	 * 设置Asn单明细的收货数量/重量/体积默认值
	 * @param detail 
	 * @version 2017年2月18日 下午12:47:42<br/>
	 * @author andy wang<br/>
	 */
	public static void defReceiveQWV( RecAsnDetail detail ) {
		if ( detail == null ) {
			return;
		}
		if ( detail.getReceiveQty() == null ) {
			detail.setReceiveQty(0d);
		}
		if ( detail.getReceiveWeight() == null ) {
			detail.setReceiveWeight(0d);
		}
		if ( detail.getReceiveVolume() == null ) {
			detail.setReceiveVolume(0d);
		}
	}
	
	
	/**
	 * 设置Asn单的订单数量/重量/体积默认值
	 * @param asn Asn单
	 * @version 2017年2月18日 下午12:50:05<br/>
	 * @author andy wang<br/>
	 */
	public static void defOrderQWV( RecAsn asn ) {
		if ( asn == null ) {
			return;
		}
		if ( asn.getOrderQty() == null ) {
			asn.setOrderQty(0d);
		}
		if ( asn.getOrderWeight() == null ) {
			asn.setOrderWeight(0d);
		}
		if ( asn.getOrderVolume() == null ) {
			asn.setOrderVolume(0d);
		}
	}
	
	/**
	 * 设置Asn单明细的订单数量/重量/体积默认值
	 * @param detail 
	 * @version 2017年2月18日 下午12:47:42<br/>
	 * @author andy wang<br/>
	 */
	public static void defOrderQWV( RecAsnDetail detail ) {
		if ( detail == null ) {
			return;
		}
		if ( detail.getOrderQty() == null ) {
			detail.setOrderQty(0d);
		}
		if ( detail.getOrderWeight() == null ) {
			detail.setOrderWeight(0d);
		}
		if ( detail.getOrderVolume() == null ) {
			detail.setOrderVolume(0d);
		}
	}
	
	/**
	 * 默认Where条件
	 * @param criteria
	 * @version 2017年2月15日 下午6:19:56<br/>
	 * @author andy wang<br/>
	 */
	public static Criteria defWhere( Example example ) {
		if ( example == null || example.getOredCriteria() == null || example.getOredCriteria().isEmpty() ) {
			return null;
		}
		return RecUtil.defWhere(example.getOredCriteria().get(0));
	}
	
	/**
	 * 默认Where条件
	 * @param criteria
	 * @version 2017年2月15日 下午6:19:56<br/>
	 * @author andy wang<br/>
	 */
	public static Criteria defWhere(Criteria criteria) {
		Principal loginUser = LoginUtil.getLoginUser();
		criteria.andEqualTo("orgId", loginUser.getOrgId());
		return criteria;
	}
	
	/**
	 * 获取拆分Asn单号
	 * @param asnNo Asn单号
	 * @return 集合包含拆分后的两个单号
	 * @version 2017年2月15日 下午4:30:19<br/>
	 * @author andy wang<br/>
	 */
	public static List<String> createSplitAsnNo( String asnNo ) {
		List<String> listSplitNo = new ArrayList<String>();
		listSplitNo.add(asnNo+"-01");
		listSplitNo.add(asnNo+"-02");
		return listSplitNo;
	}
	
}
