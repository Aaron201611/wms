package com.yunkouan.wms.modules.send.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.BeanUtils;

import com.yunkouan.exception.BizException;
import com.yunkouan.util.NumberUtil;
import com.yunkouan.wms.common.constant.BizStatus;
import com.yunkouan.wms.common.util.IdUtil;
import com.yunkouan.wms.modules.send.entity.SendPick;
import com.yunkouan.wms.modules.send.entity.SendPickDetail;
import com.yunkouan.wms.modules.send.service.impl.SplitContext;
import com.yunkouan.wms.modules.send.vo.SendPickDetailVo;
import com.yunkouan.wms.modules.send.vo.SendPickVo;

public class PickSplitUtil {

	private SendPickVo pickVo_1;

	private SendPickVo pickVo_2;

	private SendPickVo parentVo;

	private Map<String, SendPickDetail> subDetailMap = new HashMap<String, SendPickDetail>();

	private SplitContext splitContext = null; // 拆分规则

	public void split(SendPickVo subVo, String operator) throws Exception {

		if (parentVo == null)
			throw new BizException(BizStatus.DATA_IS_NULL.getReasonPhrase());
		//创建子拣货单vo
		pickVo_1 = newSubPick(parentVo, 1, operator);
		pickVo_2 = newSubPick(parentVo, 2, operator);
		//根据上送的拣货单明细，按规则拆分原拣货单明细
		splitDetails(subVo, operator);
	}

	public void splitDetails(SendPickVo subVo, String operator) throws Exception {
		double rate = 0.0;
		double zero = 0.0;
		Map<String, SendPickDetailVo> subDetailMap = subVo.listToMap2();
		for (SendPickDetailVo parentDetailVo : parentVo.getSendPickDetailVoList()) {
			// 子拣货单明细vo
			SendPickDetailVo subDetailVo = subDetailMap.get(parentDetailVo.getSendPickDetail().getPickDetailId());
			// 原拣货单明细
			SendPickDetail sourceDetail = parentDetailVo.getSendPickDetail();
			// 没有拆分则直接生成子明细
			if (subDetailVo == null) {
				splitDetail(sourceDetail, sourceDetail.getOrderQty(), sourceDetail.getOrderWeight(),
						sourceDetail.getOrderVolume(), operator, pickVo_2);
				continue;
			}
			// 子拣货单明细
			SendPickDetail subDetail = subDetailVo.getSendPickDetail();
			// 数据校验
			check(sourceDetail, subDetail);
			// 按规则拆分
			// 若重量或体积不为空，则按实际填写的重量体积拆分
			// 若若重量或体积为空，则按数量比例拆分重量和体积
			double qty_1 = subDetail.getOrderQty();
			double weight_1 = 0.0;
			double volume_1 = 0.0;
			if ((subDetail.getOrderWeight() != null && subDetail.getOrderWeight().compareTo(zero) != 0)
					|| (subDetail.getOrderVolume() != null && subDetail.getOrderVolume().compareTo(zero) != 0)) {
				// 拆分规则
				this.splitContext = SplitContext.createInstance(SplitContext.STRATEGE_QUAN);
				weight_1 = subDetail.getOrderWeight();
				volume_1 = subDetail.getOrderVolume();
			} else {
				// 拆分规则
				this.splitContext = SplitContext.createInstance(SplitContext.STRATEGE_RATE);
				rate = (double) subDetail.getOrderQty() / sourceDetail.getOrderQty();
				weight_1 = splitContext.quoteWeigh(sourceDetail.getOrderWeight(), subDetail.getOrderWeight(), rate);
				volume_1 = splitContext.quoteVolume(sourceDetail.getOrderVolume(), subDetail.getOrderVolume(), rate);
			}
			if (qty_1 > 0) {
				splitDetail(sourceDetail, qty_1, weight_1, volume_1, operator, pickVo_1);
			}
			// 创建子拣货单02,保存拆分数量大于0的明细
			double qty_2 = splitContext.quoteQty(sourceDetail.getOrderQty(), subDetail.getOrderQty());
			if (qty_2 > 0) {
				double weigh2 = NumberUtil.sub(sourceDetail.getOrderWeight(),weight_1);
				double volume2 = NumberUtil.sub(sourceDetail.getOrderVolume(),volume_1);
				splitDetail(sourceDetail, qty_2, weigh2, volume2, operator, pickVo_2);
			}
		}
	}

	// 校验是否拆分数量是否超出订单数量
	public void check(SendPickDetail org, SendPickDetail sub) {
		// 检查数量，重量，体积是否为空
		qtyIsNull(org);
		qtyIsNull(sub);
		// 校验是否拆分数量是否超出订单数量
		if ((sub.getOrderQty().doubleValue() > org.getOrderQty().doubleValue())
				|| (sub.getOrderWeight().compareTo(org.getOrderWeight()) > 0)
				|| (sub.getOrderVolume().compareTo(org.getOrderVolume()) > 0)) {

			throw new BizException(BizStatus.SEND_PICK_SPLIT_ERROR.getReasonPhrase());
		}
	}

	/**
	 * 检查数量，重量，体积是否为空
	 * 
	 * @param detail
	 * @version 2017年2月28日 下午2:15:51<br/>
	 * @author Aaron He<br/>
	 */
	public void qtyIsNull(SendPickDetail detail) {
		if (detail.getOrderQty() == null || detail.getOrderWeight() == null || detail.getOrderVolume() == null)
			throw new BizException(BizStatus.DATA_IS_NULL.getReasonPhrase());
	}

	/**
	 * 创建子拣货单明细
	 * 
	 * @param orlRecord
	 * @param qty
	 * @param weigh
	 * @param volume
	 * @return
	 * @throws Exception
	 * @version 2017年2月16日 下午6:12:52<br/>
	 * @author Aaron He<br/>
	 */
	public void splitDetail(SendPickDetail orlRecord, double qty, double weigh, double volume, String operator,
			SendPickVo subPickVo) throws Exception {
		SendPickDetail newDetail = (SendPickDetail) BeanUtils.cloneBean(orlRecord);
		newDetail.setPickDetailId(IdUtil.getUUID());
		newDetail.setPickId(subPickVo.getSendPick().getPickId());
		newDetail.setOrderQty(qty);
		newDetail.setOrderWeight(weigh);
		newDetail.setOrderVolume(volume);
		newDetail.setCreateTime(new Date());
		newDetail.setCreatePerson(operator);
		newDetail.setUpdateTime(new Date());
		newDetail.setUpdatePerson(operator);

		SendPickDetailVo vo = new SendPickDetailVo();
		vo.setSendPickDetail(newDetail);
		subPickVo.getSendPickDetailVoList().add(vo);

	}

	/**
	 * 创建子拣货单
	 * 
	 * @param parentVo
	 * @param no
	 * @return
	 * @throws Exception
	 * @version 2017年2月16日 下午6:12:32<br/>
	 * @author Aaron He<br/>
	 */
	public SendPickVo newSubPick(SendPickVo parentVo, int no, String operator) throws Exception {
		SendPickVo subPickVo = new SendPickVo();
		// 根据系统规则生成子拣货单号
		String pickNo = newSubPickNo(parentVo.getSendPick().getPickNo(), no);
		// 创建子发货单
		SendPick pick = new SendPick();
		pick = (SendPick) BeanUtils.cloneBean(parentVo.getSendPick());
		pick.setPickId(IdUtil.getUUID());
		pick.setPickNo(pickNo);
		pick.setParentId(parentVo.getSendPick().getPickId());
		pick.setCreateTime(new Date());
		pick.setCreatePerson(operator);
		pick.setUpdatePerson(operator);
		pick.setUpdateTime(new Date());
		subPickVo.setSendPick(pick);

		return subPickVo;
	}

	/**
	 * 创建新拣货子单号
	 * 
	 * @param oriDeliveryId
	 * @return
	 * @version 2017年2月15日 下午2:04:47<br/>
	 * @author Aaron He<br/>
	 */
	public String newSubPickNo(String orgPickNo, int no) {
		String subPickNo = "";
		if (no == 1)
			subPickNo = orgPickNo + "-01";
		else if (no == 2)
			subPickNo = orgPickNo + "-02";
		return subPickNo;
	}

	public SendPickVo getPickVo_1() {
		return pickVo_1;
	}

	public void setPickVo_1(SendPickVo pickVo_1) {
		this.pickVo_1 = pickVo_1;
	}

	public SendPickVo getPickVo_2() {
		return pickVo_2;
	}

	public void setPickVo_2(SendPickVo pickVo_2) {
		this.pickVo_2 = pickVo_2;
	}

	public SendPickVo getParentVo() {
		return parentVo;
	}

	public void setParentVo(SendPickVo parentVo) {
		this.parentVo = parentVo;
	}

	public SplitContext getSplitContext() {
		return splitContext;
	}

	public void setSplitContext(SplitContext splitContext) {
		this.splitContext = splitContext;
	}

}
