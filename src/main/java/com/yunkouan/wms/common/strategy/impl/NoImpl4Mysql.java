package com.yunkouan.wms.common.strategy.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.yunkouan.exception.BizException;
import com.yunkouan.util.RedisTool;
import com.yunkouan.wms.common.config.RedisConfig;
import com.yunkouan.wms.common.constant.BillPrefix;
import com.yunkouan.wms.common.constant.BizStatus;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.common.dao.INoDao;
import com.yunkouan.wms.common.entity.Sequence;
import com.yunkouan.wms.common.service.ICommonService;
import com.yunkouan.wms.common.strategy.INoRule;
import com.yunkouan.wms.common.util.IdUtil;
import com.yunkouan.wms.common.vo.CommonVo;
import com.yunkouan.wms.modules.rec.util.RecUtil;

/**
 * @function 单据编号生成规则实现（MYSQL，使用数据库函数）
 * @author tphe06
 */
@Service(value="noRule")
@Transactional(readOnly=false,rollbackFor=Exception.class)
public class NoImpl4Mysql extends INoRule {
	/** 通用接口类 <br/> add by andy wang */
	@Autowired
	private ICommonService commonService;
	@Autowired
	private INoDao dao;

	@Override
	public String getAsnNo(String orgid, Integer docType) {
		if ( docType == null ) {
			return null;
		}
		// 设置asn单号
		CommonVo p_commonVO = new CommonVo(orgid, RecUtil.type2NoPrefix(docType));
		return this.commonService.getNo(p_commonVO);
	}

	@Override
	public String getPutawayNo(String orgid) {
		CommonVo p_commonVO = new CommonVo(orgid, BillPrefix.DOCUMENT_PREFIX_PUTAWAY_RECEIPT );
		return this.commonService.getNo(p_commonVO);
	}

	@Override
	public String getDeliveryNo(String orgid, Integer docType) {
		BillPrefix field = toFieldName(docType);
		if(StringUtils.isEmpty(orgid) || field == null)
			throw new BizException(BizStatus.DELIVERY_NO_CREATE_ERROR.getReasonPhrase());
		CommonVo common = new CommonVo(orgid, field); 
		return this.commonService.getNo(common);
	}
	public static BillPrefix toFieldName(Integer docType) {
		if(docType == null) return null;
		//普通出仓
		if(docType.intValue() == Constant.DELIVERY_TYPE_NORMAL_OUT){
			return BillPrefix.DOCUMENT_PREFIX_OUTGOING_NORMAL;
		}
		//转仓出库
		else if(docType.intValue() == Constant.DELIVERY_TYPE_SWITCH_OUT){
			return BillPrefix.DOCUMENT_PREFIX_OUTGOING_SWITCH;
		}
		//其他出库
		else if(docType.intValue() == Constant.DELIVERY_TYPE_ORTHER_OUT){
			return BillPrefix.DOCUMENT_PREFIX_OUTGOING_OTHER;
		}
		//领用出库
		else if(docType.intValue() == Constant.DELIVERY_TYPE_TAKE_OUT){
			return BillPrefix.DOCUMENT_PREFIX_OUTGOING_TAKE;
		}
		//样品出库
		else if(docType.intValue() == Constant.DELIVERY_TYPE_SAMPLE_OUT){
			return BillPrefix.DOCUMENT_PREFIX_OUTGOING_SAMPLE;
		}
		//调拨出库
		else if(docType.intValue() == Constant.DELIVERY_TYPE_TRANSFER_OUT){
			return BillPrefix.DOCUMENT_PREFIX_OUTGOING_TRANSFER;
		}
		//调拨出库
		else if(docType.intValue() == Constant.DELIVERY_TYPE_BURST_OUT){
			return BillPrefix.DELIVERY_TYPE_BURST_OUT;
		}
		return null;
	}

	@Override
	public String getPickNo(String orgid, Integer docType) {
		BillPrefix field = toFieldName4pick(docType);
		if(StringUtils.isEmpty(orgid) || field == null) 
			throw new BizException(BizStatus.PICK_NO_CREATE_ERROR.getReasonPhrase());
		CommonVo common = new CommonVo(orgid, field);
		return commonService.getNo(common);
	}
	public static BillPrefix toFieldName4pick(Integer docType){
		if(docType == null) return null;
		//发货单拣货
		if(docType.intValue() == Constant.PICKTYPE_FORM_DELIVERY){
			return BillPrefix.DOCUMENT_PREFIX_PICKING_OUTGOING;
		}
		//波次拣货单
		else if(docType.intValue() == Constant.PICKTYPE_FORM_WAVE){
			return BillPrefix.DOCUMENT_PREFIX_PICKING_WAVE;
		}
		//加工拣货单
		else if(docType.intValue() == Constant.PICKTYPE_FORM_HANDLE){
			return BillPrefix.DOCUMENT_PREFIX_PICKING_MACHINING;
		}
		return null;
	}

	@Override
	public String getWaveNo(String orgid) {
		CommonVo common = new CommonVo(orgid, BillPrefix.DOCUMENT_PREFIX_WAVE_DOCUMENT);
		return commonService.getNo(common);
	}

	@Override
	public String getCountNo(String orgid) {
		return commonService.getNo(new CommonVo(orgid, BillPrefix.DOCUMENT_COUNT));
	}

	@Override
	public String getAdjustNo(String orgid) {
		return commonService.getNo(new CommonVo(orgid, BillPrefix.DOCUMENT_ADJUST_BILL));
	}

	@Override
	public String getShiftInternalNo(String orgid) {
		return commonService.getNo(new CommonVo(orgid, BillPrefix.DOCUMENT_PREFIX_MOVE_INTERNAL));
	}

	@Override
	public String getShiftReplenishmentNo(String orgid) {
		return commonService.getNo(new CommonVo(orgid, BillPrefix.DOCUMENT_PREFIX_MOVE_REPLENISHMENT));
	}

	@Override
	public String getShiftRejectNo(String orgid) {
		return commonService.getNo(new CommonVo(orgid, BillPrefix.DOCUMENT_PREFIX_MOVE_REJECT));
	}
	@Override
	public String getShiftPickNo(String orgid) {
		return commonService.getNo(new CommonVo(orgid, BillPrefix.DOCUMENT_PREFIX_MOVE_PICK));
	}
	@Override
	public String getExceptionNo(String orgid) {
		return commonService.getNo(new CommonVo(orgid, BillPrefix.DOCUMENT_EXCEPTION));
	}

	@Override
	public String getLocationSpecNo(String orgid) {
		return commonService.getNo(new CommonVo(orgid, BillPrefix.DOCUMENT_LOCATION_SPEC));
	}

	/**
	 * @param orgid
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年8月29日 上午11:32:35<br/>
	 * @author 王通<br/>
	 */
	@Override
	public String getMaterialNo(String orgid) {
		return commonService.getNo(new CommonVo(orgid, BillPrefix.DOCUMENT_MATERIAL));
	}
	
	/**
	 * 生成申报单流水号
	 * @param orgId
	 * @return
	 */
	public String getApplicationNo(String orgId){
		return commonService.getNo(new CommonVo(orgId, BillPrefix.APPLICATION_NO,false));
	}
	
	/**
	 * 生成核放单流水号
	 * @param orgId
	 * @return
	 */
	public  String getExamineNo(String orgId){
		return commonService.getNo(new CommonVo(orgId, BillPrefix.EXAMINE_NO,false));
	}

	@Override
	public String getSkuPrefixNo(String orgid) {
		if(StringUtils.isBlank(orgid)) return "";
		String val = IdUtil.INIT6;
		String seq = BillPrefix.DOCUMENT_PREFIX_SKU.getPrefix();
		RedissonClient client = RedisTool.lock(RedisConfig.host, RedisConfig.port, RedisConfig.password, Integer.parseInt(RedisConfig.db), "lock_"+seq);
		try {
			/**1查询当前数值**/
			Sequence current = dao.selectOne(new Sequence().setOrgId(orgid).setSeqName(seq));
			if(current != null) {
				/**2“加一”**/
				val = IdUtil.addOne(current.getCurrentValueTxt(), IdUtil.INIT6);
				/**3更新数据库**/
				current.setCurrentValueTxt(val);
				dao.updateByPrimaryKeySelective(current);
			} else {
				/**3更新数据库**/
				Sequence s = new Sequence();
				s.setOrgId(orgid);
				s.setSeqName(seq);
				s.setsIncrement(1);
				s.setCurrentValueTxt(val);
				dao.insertSelective(s);
			}
		} finally {
			RedisTool.unLock(client, "lock_"+seq);
		}
		/**4返回当前数值**/
		return val;
	}

	@Override
	public List<String> getSkuSuffixNo(String orgId, int sum) {
		if(sum == 0) return null;
		List<String> val = new ArrayList<String>();
		/**1查询当前数值**/
		String seq = BillPrefix.DOCUMENT_SUFFIX_SKU.getPrefix();
		RedissonClient client = RedisTool.lock(RedisConfig.host, RedisConfig.port, RedisConfig.password, Integer.parseInt(RedisConfig.db), "lock_"+seq);
		try {
			Sequence current = dao.selectOne(new Sequence().setOrgId(orgId).setSeqName(seq));
			if(current != null) {
				/**2“加一”**/
				String max = current.getCurrentValueTxt();
				for(int i=0; i<sum; ++i) {
					String next = IdUtil.addOne(max, IdUtil.INIT4);
					max = next;
					val.add(next);
				}
				/**3更新数据库**/
				current.setCurrentValueTxt(max);
				dao.updateByPrimaryKeySelective(current);
			} else {
				/**2“加一”**/
				String max = "";
				for(int i=0; i<sum; ++i) {
					String next = IdUtil.addOne(max, IdUtil.INIT4);
					max = next;
					val.add(next);
				}
				/**3更新数据库**/
				Sequence s = new Sequence();
				s.setOrgId(orgId);
				s.setSeqName(seq);
				s.setsIncrement(1);
				s.setCurrentValueTxt(max);
				dao.insertSelective(s);
			}
		} finally {
			RedisTool.unLock(client, "lock_"+seq);
		}
		/**4返回当前数值**/
		return val;
	}

	@Override
	public String getMerchantNo(String orgid) {
		//BillPrefix.DOCUMENT_MERCHANT
		return null;
	}

	@Override
	public String getSkuTypeNo(String orgid) {
		//BillPrefix.DOCUMENT_SKU_TYPE
		return null;
	}

	@Override
	public String getApplyRejectsNo(String orgid) {
		return commonService.getNo(new CommonVo(orgid, BillPrefix.DOCUMENT_REJECTS_CHANGE));
	}
}