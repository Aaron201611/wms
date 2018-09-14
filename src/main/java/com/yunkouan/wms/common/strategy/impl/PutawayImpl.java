package com.yunkouan.wms.common.strategy.impl;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunkouan.exception.BizException;
import com.yunkouan.util.PubUtil;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.common.strategy.IPutawayRule;
import com.yunkouan.wms.modules.rec.util.flow.autoptw.AutoPutawayFlow;
import com.yunkouan.wms.modules.rec.vo.RecPutawayDetailVO;
import com.yunkouan.wms.modules.rec.vo.RecPutawayLocationVO;
import com.yunkouan.wms.modules.rec.vo.RecPutawayVO;

/**
 * @function 上架规则的实现：相同货品邻近摆放
 * @author tphe06
 */
@Service(value="putawayRule")
@Transactional(readOnly=false,rollbackFor=Exception.class)
public class PutawayImpl extends IPutawayRule {
	private Logger log = LoggerFactory.getLogger(PutawayImpl.class);

	/**
	 * 自动分配上架库位
	 * @param param_recPutawayVO 上架信息
	 * @return 包含分配好上架库位的上架信息
	 * @throws Exception
	 * @version 2017年3月14日 下午6:12:56<br/>
	 * @author andy wang<br/>
	 */
	public RecPutawayVO auto ( RecPutawayVO param_recPutawayVO ) throws Exception {
		List<RecPutawayDetailVO> listSavePutawayDetailVO = param_recPutawayVO.getListSavePutawayDetailVO();
		if ( param_recPutawayVO == null || PubUtil.isEmpty(listSavePutawayDetailVO) ) {
			log.error("autoPutaway --> param_recPutawayVO or listSavePutawayDetailVO is null!");
    		throw new BizException("err_sku_detail_is_null");
		}
		for (RecPutawayDetailVO recPutawayDetailVO : listSavePutawayDetailVO) {
			recPutawayDetailVO.setListPlanLocationVO(new ArrayList<RecPutawayLocationVO>());
		}
		AutoPutawayFlow flow = new AutoPutawayFlow(listSavePutawayDetailVO, param_recPutawayVO.getPutaway());
		flow.startFlow();
		List<RecPutawayDetailVO> result = flow.getResult();
		param_recPutawayVO.setListPutawayDetailVO(result);
		param_recPutawayVO.setMessage("分配完毕");
		param_recPutawayVO.setSuccess(true);
		for (RecPutawayDetailVO recPutawayDetailVO : result) {
			List<RecPutawayLocationVO> listPlanLocationVO = recPutawayDetailVO.getListPlanLocationVO();
			boolean isBreak = false;
			for (RecPutawayLocationVO recPutawayLocationVO : listPlanLocationVO) {
				if ( StringUtil.isTrimEmpty(recPutawayLocationVO.getPutawayLocation().getLocationId()) ) {
					isBreak = true;
					break;
				}
			}
			if ( isBreak ) {
				param_recPutawayVO.setSuccess(false);
				param_recPutawayVO.setMessage("系统无法全部自动分配，请手工分配");
				break;
			}
		}
		return param_recPutawayVO;
	}
}