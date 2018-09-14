package com.yunkouan.wms.common.strategy.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunkouan.exception.BizException;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.common.constant.BillPrefix;
import com.yunkouan.wms.common.constant.BizStatus;
import com.yunkouan.wms.common.dao.ICommonDao;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.strategy.INoRule;
import com.yunkouan.wms.common.util.IdUtil;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.common.vo.CommonVo;
import com.yunkouan.wms.modules.rec.util.RecUtil;

/**
 * @function 单据编号生成规则实现（ORACLE，使用数据库脚本实现）
 * @author tphe06
 */
@Service(value="noRule4Oracle")
@Transactional(readOnly=false,rollbackFor=Exception.class)
public class NoImpl4Oracle extends INoRule {
	private static Log log = LogFactory.getLog(NoImpl4Oracle.class);

	/** 通用接口类 <br/> add by andy wang */
	@Autowired
	private ICommonDao dao;

	@Override
	public String getAsnNo(String orgid, Integer docType) {
		if ( docType == null ) {
			return null;
		}
		// 设置asn单号
		CommonVo p_commonVO = new CommonVo(orgid, RecUtil.type2NoPrefix(docType));
		return this.getNo(p_commonVO);
	}

	@Override
	public String getPutawayNo(String orgid) {
		CommonVo p_commonVO = new CommonVo(orgid, BillPrefix.DOCUMENT_PREFIX_PUTAWAY_RECEIPT );
		return this.getNo(p_commonVO);
	}

	@Override
	public String getDeliveryNo(String orgid, Integer docType) {
		BillPrefix field = NoImpl4Mysql.toFieldName(docType);
		if(StringUtils.isEmpty(orgid) || field == null)
			throw new BizException(BizStatus.DELIVERY_NO_CREATE_ERROR.getReasonPhrase());
		CommonVo common = new CommonVo(orgid, field); 
		return this.getNo(common);
	}

	@Override
	public String getPickNo(String orgid, Integer docType) {
		BillPrefix field = NoImpl4Mysql.toFieldName4pick(docType);
		if(StringUtils.isEmpty(orgid) || field == null) 
			throw new BizException(BizStatus.PICK_NO_CREATE_ERROR.getReasonPhrase());
		CommonVo common = new CommonVo(orgid, field);
		return this.getNo(common);
	}

	@Override
	public String getWaveNo(String orgid) {
		CommonVo common = new CommonVo(orgid, BillPrefix.DOCUMENT_PREFIX_WAVE_DOCUMENT);
		return this.getNo(common);
	}

	@Override
	public String getCountNo(String orgid) {
		return this.getNo(new CommonVo(orgid, BillPrefix.DOCUMENT_COUNT));
	}

	@Override
	public String getAdjustNo(String orgid) {
		return this.getNo(new CommonVo(orgid, BillPrefix.DOCUMENT_ADJUST_BILL));
	}

	@Override
	public String getExceptionNo(String orgid) {
		return this.getNo(new CommonVo(orgid, BillPrefix.DOCUMENT_EXCEPTION));
	}

	@Override
	public String getLocationSpecNo(String orgid) {
		return this.getNo(new CommonVo(orgid, BillPrefix.DOCUMENT_LOCATION_SPEC));
	}

	public String getApplicationNo(String orgId){
		return this.getNo(new CommonVo(orgId, BillPrefix.APPLICATION_NO,false));
	}
	/**
	 * 生成核放单流水号
	 * @param orgId
	 * @return
	 */
	public  String getExamineNo(String orgId){
		return this.getNo(new CommonVo(orgId, BillPrefix.EXAMINE_NO,false));
	}

    /**
     * 生成编号
     * @param vo
     * @return
     */
    private String getNo(CommonVo vo) {
    	if ( vo == null ) {
    		return null;
		}
		// 获取当前用户信息
		Principal loginUser = LoginUtil.getLoginUser();
		if (loginUser == null) {
			throw new BizException("valid_common_user_no_login");
		}
		String id = IdUtil.getUUID();
		vo.setId(id);
		vo.setWarehouseId(LoginUtil.getWareHouseId());
		vo.setOrgId(loginUser.getOrgId());
		String fieldName = vo.getFieldName();
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
		String dateString = sdf.format(new Date());
		Integer seq = this.dao.getSequence(vo);
		if (seq == null) {
			this.dao.addSequence(vo);
			seq = this.dao.getSequence(vo);
		}
		this.dao.updateSequence(vo);
		String seqStr = StringUtil.getString(new BigDecimal(seq), 6);
		if(log.isInfoEnabled()) log.info(seqStr);
		if (vo.getContainDate()) {
			return fieldName + dateString + seqStr;
		} else {
			return fieldName + seqStr;
		}
    }

	/**
	 * @param orgid
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年8月24日 上午11:31:00<br/>
	 * @author 王通<br/>
	 */
	@Override
	public String getShiftReplenishmentNo(String orgid) {
		CommonVo p_commonVO = new CommonVo(orgid, BillPrefix.DOCUMENT_PREFIX_MOVE_REPLENISHMENT );
		return this.getNo(p_commonVO);
	}

	/**
	 * @param orgid
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年8月24日 上午11:31:00<br/>
	 * @author 王通<br/>
	 */
	@Override
	public String getShiftRejectNo(String orgid) {
		CommonVo p_commonVO = new CommonVo(orgid, BillPrefix.DOCUMENT_PREFIX_MOVE_REJECT );
		return this.getNo(p_commonVO);
	}

	/**
	 * @param orgid
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年8月24日 上午11:31:00<br/>
	 * @author 王通<br/>
	 */
	@Override
	public String getShiftPickNo(String orgid) {
		CommonVo p_commonVO = new CommonVo(orgid, BillPrefix.DOCUMENT_PREFIX_MOVE_PICK );
		return this.getNo(p_commonVO);
	}

	/**
	 * @param orgid
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年8月24日 上午11:31:00<br/>
	 * @author 王通<br/>
	 */
	@Override
	public String getShiftInternalNo(String orgid) {
		CommonVo p_commonVO = new CommonVo(orgid, BillPrefix.DOCUMENT_PREFIX_MOVE_INTERNAL );
		return this.getNo(p_commonVO);
	}

	/**
	 * @param orgid
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年8月29日 上午11:34:10<br/>
	 * @author 王通<br/>
	 */
	@Override
	public String getMaterialNo(String orgid) {
		CommonVo p_commonVO = new CommonVo(orgid, BillPrefix.DOCUMENT_MATERIAL );
		return this.getNo(p_commonVO);
	}

	@Override
	public String getSkuPrefixNo(String orgid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getSkuSuffixNo(String orgId, int sum) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMerchantNo(String orgid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSkuTypeNo(String orgid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getApplyRejectsNo(String orgid) {
		return this.getNo(new CommonVo(orgid, BillPrefix.DOCUMENT_REJECTS_CHANGE));
	}
}