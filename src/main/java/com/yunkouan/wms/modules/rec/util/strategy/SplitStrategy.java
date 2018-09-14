/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * @version 2017年2月16日 下午5:38:09<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.rec.util.strategy;

import java.util.List;
import java.util.Map;

import com.yunkouan.exception.BizException;
import com.yunkouan.util.NumberUtil;
import com.yunkouan.util.PubUtil;

/**
 * 拆分策略<br/><br/>
 * @version 2017年2月16日 下午5:38:09<br/>
 * @author andy wang<br/>
 */
public abstract class SplitStrategy<T,K> {
	
	
	/**
	 * 构造方法
	 * @param source 拆分源
	 * @param listSourceDetail 拆分源明细
	 * @param listSavetDetail 输入的拆分明细
	 * @version 2017年2月16日 下午7:39:46<br/>
	 * @author andy wang<br/>
	 */
	public SplitStrategy(T source, List<K> listSourceDetail, List<K> listSavetDetail , String msgModel ) {
		this.source = source;
		this.listSourceDetail = listSourceDetail;
		this.listSavetDetail = listSavetDetail;
		this.msgModel = msgModel;
	}

	/** 原单 <br/> add by andy */
	protected T source;
	
	/** 原单明细 <br/> add by andy */
	protected List<K> listSourceDetail;
	
	/** 输入的单明细 <br/> add by andy */
	protected List<K> listSavetDetail;
	
	/** 组装的输入的单明细 <br/> add by andy */
	protected Map<String,K> mapSaveDetail;
	
	/** 拆分的列表 <br/> add by andy */
	protected List<K> listSplitDetail;
	
	/** 剩余的列表 <br/> add by andy */
	protected List<K> listSurplusDetail;
	
	/** 拆分对象 <br/> add by andy */
	protected T splitObj;
	
	/** 剩余对象 <br/> add by andy */
	protected T surplusObj;
	
	/** 信息前缀 <br/> add by andy */
	private String msgModel;
	
	/**
	 * 扣减方式计算
	 */
	private static final Integer COUNTMODE_MINUS = 1;
	private static final Integer COUNTMODE_RATE = 2;
	
	/* getset **********************************************/
	/**
	 * 属性 source getter方法
	 * @return 属性source
	 * @author andy wang<br/>
	 */
	public T getSource() {
		return source;
	}

	/**
	 * 属性 source setter方法
	 * @param source 设置属性source的值
	 * @author andy wang<br/>
	 */
	public void setSource(T source) {
		this.source = source;
	}

	/**
	 * 属性 listSourceDetail getter方法
	 * @return 属性listSourceDetail
	 * @author andy wang<br/>
	 */
	public List<K> getListSourceDetail() {
		return listSourceDetail;
	}

	/**
	 * 属性 listSourceDetail setter方法
	 * @param listSourceDetail 设置属性listSourceDetail的值
	 * @author andy wang<br/>
	 */
	public void setListSourceDetail(List<K> listSourceDetail) {
		this.listSourceDetail = listSourceDetail;
	}

	/**
	 * 属性 listSavetDetail getter方法
	 * @return 属性listSavetDetail
	 * @author andy wang<br/>
	 */
	public List<K> getListSavetDetail() {
		return listSavetDetail;
	}

	/**
	 * 属性 listSavetDetail setter方法
	 * @param listSavetDetail 设置属性listSavetDetail的值
	 * @author andy wang<br/>
	 */
	public void setListSavetDetail(List<K> listSavetDetail) {
		this.listSavetDetail = listSavetDetail;
	}

	/**
	 * 属性 mapSaveDetail getter方法
	 * @return 属性mapSaveDetail
	 * @author andy wang<br/>
	 */
	public Map<String, K> getMapSaveDetail() {
		return mapSaveDetail;
	}

	/**
	 * 属性 mapSaveDetail setter方法
	 * @param mapSaveDetail 设置属性mapSaveDetail的值
	 * @author andy wang<br/>
	 */
	public void setMapSaveDetail(Map<String, K> mapSaveDetail) {
		this.mapSaveDetail = mapSaveDetail;
	}

	/**
	 * 属性 listSplitDetail getter方法
	 * @return 属性listSplitDetail
	 * @author andy wang<br/>
	 */
	public List<K> getListSplitDetail() {
		return listSplitDetail;
	}

	/**
	 * 属性 listSplitDetail setter方法
	 * @param listSplitDetail 设置属性listSplitDetail的值
	 * @author andy wang<br/>
	 */
	public void setListSplitDetail(List<K> listSplitDetail) {
		this.listSplitDetail = listSplitDetail;
	}

	/**
	 * 属性 listSurplusDetail getter方法
	 * @return 属性listSurplusDetail
	 * @author andy wang<br/>
	 */
	public List<K> getListSurplusDetail() {
		return listSurplusDetail;
	}

	/**
	 * 属性 listSurplusDetail setter方法
	 * @param listSurplusDetail 设置属性listSurplusDetail的值
	 * @author andy wang<br/>
	 */
	public void setListSurplusDetail(List<K> listSurplusDetail) {
		this.listSurplusDetail = listSurplusDetail;
	}

	/**
	 * 属性 splitObj getter方法
	 * @return 属性splitObj
	 * @author andy wang<br/>
	 */
	public T getSplitObj() {
		return splitObj;
	}

	/**
	 * 属性 splitObj setter方法
	 * @param splitObj 设置属性splitObj的值
	 * @author andy wang<br/>
	 */
	public void setSplitObj(T splitObj) {
		this.splitObj = splitObj;
	}

	/**
	 * 属性 surplusObj getter方法
	 * @return 属性surplusObj
	 * @author andy wang<br/>
	 */
	public T getSurplusObj() {
		return surplusObj;
	}

	/**
	 * 属性 surplusObj setter方法
	 * @param surplusObj 设置属性surplusObj的值
	 * @author andy wang<br/>
	 */
	public void setSurplusObj(T surplusObj) {
		this.surplusObj = surplusObj;
	}
	
	
	/* method ********************************************************/
	
	/**
	 * 验证主单状态是否能进行拆分
	 * —— true 能进行拆分
	 * —— false 不能进行拆分，系统报错
	 * @param t 原主单
	 * @return 是否能进行拆分
	 * @version 2017年3月7日 下午6:17:56<br/>
	 * @author andy wang<br/>
	 */
	protected abstract boolean checkStatus( T t );
	
	/**
	 * 获取明细单id
	 * @param k 明细单对象
	 * @return 明细单id
	 * @version 2017年3月7日 下午6:19:00<br/>
	 * @author andy wang<br/>
	 */
	protected abstract String getDetailId( K k );
	
	/**
	 * 获取明细单数量
	 * @param k 明细单对象
	 * @return 明细单数量
	 * @version 2017年3月7日 下午6:19:20<br/>
	 * @author andy wang<br/>
	 */
	protected abstract Double getDetailQty( K k );
	
	/**
	 * 获取明细单体积
	 * @param k 明细单对象
	 * @return 明细单体积
	 * @version 2017年3月7日 下午6:19:54<br/>
	 * @author andy wang<br/>
	 */
	protected abstract Double getDetailVolume( K k );
	
	/**
	 * 获取明细单重量
	 * @param k 明细单对象
	 * @return 明细单重量
	 * @version 2017年3月7日 下午6:20:09<br/>
	 * @author andy wang<br/>
	 */
	protected abstract Double getDetailWeight( K k );
	
	/**
	 * 设置明细单中的数量/重量/体积默认值
	 * @param k 明细单对象
	 * @version 2017年3月7日 下午6:20:27<br/>
	 * @author andy wang<br/>
	 */
	protected abstract void defDetail( K k );
	
	/**
	 * 获取主单id
	 * @param t 主单对象
	 * @return 主单id
	 * @version 2017年3月7日 下午6:20:55<br/>
	 * @author andy wang<br/>
	 */
	protected abstract String getId(T t) ;
	
	/**
	 * 设置主单的数量
	 * @param t 主单对象
	 * @param qty 主单的数量
	 * @version 2017年3月22日 下午5:36:42<br/>
	 * @author andy wang<br/>
	 */
	protected abstract void setQty( T t , Double qty) ;
	
	/**
	 * 设置主单的重量
	 * @param t 主单对象
	 * @param weight 主单的重量
	 * @version 2017年3月22日 下午5:37:00<br/>
	 * @author andy wang<br/>
	 */
	protected abstract void setWeight( T t , Double weight) ;
	
	/**
	 * 设置主单体积
	 * @param t 主单的对象
	 * @param volume 主单的体积
	 * @version 2017年3月22日 下午5:37:13<br/>
	 * @author andy wang<br/>
	 */
	protected abstract void setVolume( T t , Double volume) ;
	
	/**
	 * 设置明细单的数量
	 * @param k 明细单对象
	 * @param qty 明细单的数量
	 * @version 2017年3月7日 下午6:21:16<br/>
	 * @author andy wang<br/>
	 */
	protected abstract void setDetailQty( K k , Double qty) ;
	
	/**
	 * 设置明细单的体积
	 * @param k 明细单对象
	 * @param volume 明细单的体积
	 * @version 2017年3月7日 下午6:21:41<br/>
	 * @author andy wang<br/>
	 */
	protected abstract void setDetailVolume( K k , Double volume );
	
	/**
	 * 设置明细单的重量
	 * @param k 明细单对象
	 * @param weight 明细单的重量
	 * @version 2017年3月7日 下午6:21:57<br/>
	 * @author andy wang<br/>
	 */
	protected abstract void setDetailWeight( K k , Double weight );
	
	
	/**
	 * 业务校验
	 * @throws Exception
	 * @version 2017年2月16日 下午5:47:30<br/>
	 * @author andy wang<br/>
	 */
	protected void check() throws Exception {
		if ( source == null ) {
    		throw new BizException(msgModel + "_null");
    	}
    	if ( checkStatus(source) ) {
    		throw new BizException(msgModel + "_splitFail_statusNotOpen");
    	}
    	if ( listSourceDetail == null || listSourceDetail.isEmpty() ) {
    		throw new BizException(msgModel + "_splitFail_detailEmpty");
    	}
    	
    	for (int i = 0; i < listSavetDetail.size(); i++) {
    		K saveDetail = listSavetDetail.get(i);
    		if ( saveDetail == null ) {
    			throw new NullPointerException("detail is null!");
    		}
    		String detailId = getDetailId(saveDetail);
    		if ( detailId == null ) {
    			throw new NullPointerException("detail id is null!");
    		}
    		this.defDetail(saveDetail);
    		if ( this.getDetailQty(saveDetail) < 0 || this.getDetailWeight(saveDetail) < 0 || this.getDetailVolume(saveDetail) < 0 ) {
    			throw new BizException(msgModel + "_splitFail_negative");
    		}
    		mapSaveDetail.put(detailId, saveDetail);
		}
    	
    	for (int i = 0; i < listSourceDetail.size(); i++) {
    		K sourceAsnDetail = listSourceDetail.get(i);
    		K saveDetail = mapSaveDetail.get(this.getDetailId(sourceAsnDetail));
    		if ( saveDetail == null ) {
    			continue;
    		}
    		if ( getDetailQty(saveDetail) > getDetailQty(sourceAsnDetail) ) {
    			throw new BizException("err_rec_asn_splitFail_overQty");
    		} else if ( getDetailQty(saveDetail) == 0 ) {
    			// 数量为0的情况，重量/体积必须为0
    			if ( getDetailVolume(saveDetail) > 0 ) {
    				throw new BizException(msgModel + "_splitFail_VolumeSameZero");
    			} else if ( getDetailWeight(saveDetail) > 0 ) {
    				throw new BizException(msgModel + "_splitFail_WeightSameZero");
    			}
    		}
    		// 设置体积
    		if ( getDetailVolume(saveDetail) > getDetailVolume(sourceAsnDetail) ) {
    			throw new BizException(msgModel + "_splitFail_overVolume");
    		}
    		// 设置重量
    		if ( getDetailWeight(saveDetail) > getDetailWeight(sourceAsnDetail) ) {
    			throw new BizException(msgModel + "_splitFail_overWeight");
    		}
		}
	}
	
	/**
	 * 生成拆分对象
	 * @throws Exception
	 * @version 2017年2月16日 下午5:45:54<br/>
	 * @author andy wang<br/>
	 */
	protected abstract void create() throws Exception;
	
	/**
	 * 拆分逻辑
	 * 
	 * 拆分数量/重量/体积规则
	 * 在上架单拆分页面，输入拆分数量、拆分重量、拆分体积（拆分数量必填，拆分重量或拆分体积，非必填；
	 * 若拆分重量/拆分体积未填写，则上架单拆分时根据数量分配，按比例拆分其重量、体积）
	 * 
	 * @throws Exception
	 * @version 2017年2月16日 下午5:47:24<br/>
	 * @author andy wang<br/>
	 */
	protected void split() throws Exception {
		Double surplusTotalQty = 0d;
		Double splitTotalQty =0d;
		Double surplusTotalWeight = 0d;
		Double splitTotalWeight =0d;
		Double surplusTotalVolume = 0d;
		Double splitTotalVolume =0d;
		for (int i = 0; i < this.listSourceDetail.size(); i++) {
    		K sourceDetail = this.listSourceDetail.get(i);
    		K saveDetail = this.mapSaveDetail.get(getDetailId(sourceDetail));
    		K splitDetail = this.createSplitDetail(getId(this.splitObj), sourceDetail);
    		K surplusDetail = this.createSplitDetail(getId(this.surplusObj), sourceDetail);
    		if ( saveDetail == null ) {
    			this.listSurplusDetail.add(surplusDetail);
    			continue;
    		}
    		Integer mode = COUNTMODE_RATE;
    		// 判断计算方式
    		if ( getDetailVolume(saveDetail) > 0 
    				|| getDetailWeight(saveDetail) > 0 ) {
    			mode = COUNTMODE_MINUS;
    		}
    		// 拆分
    		Double scale = 0d;
    		scale = PubUtil.round(getDetailQty(saveDetail).doubleValue() / getDetailQty(sourceDetail),2);
    		if ( getDetailQty(saveDetail) == 0 ) {
    			this.listSurplusDetail.add(surplusDetail);
    		} else if ( getDetailQty(saveDetail).doubleValue() == getDetailQty(sourceDetail).doubleValue() ) {
    			this.listSplitDetail.add(splitDetail);
    		} else {
    			this.listSplitDetail.add(splitDetail);
    			this.listSurplusDetail.add(surplusDetail);
    		}
    		this.setDetailQty(splitDetail, getDetailQty(saveDetail));
    		Double surplusQty = getDetailQty(sourceDetail) - getDetailQty(saveDetail);
    		this.setDetailQty(surplusDetail, surplusQty);
    		splitTotalQty += getDetailQty(saveDetail);
    		surplusTotalQty += surplusQty;
    		// 计算
    		if ( COUNTMODE_RATE == mode ) {
    			// 比率拆分
    			Double splitVolume = this.countRate(getDetailVolume(sourceDetail), scale);
    			setDetailVolume(splitDetail, splitVolume);
    			Double surplusVolume = this.countSurplus(getDetailVolume(sourceDetail), splitVolume);
    			setDetailVolume(surplusDetail, surplusVolume);
    			
    			Double splitWeight = this.countRate(getDetailWeight(sourceDetail), scale);
    			setDetailWeight(splitDetail, splitWeight);
    			Double surplusWeight =  this.countSurplus(getDetailWeight(sourceDetail), splitWeight);
    			setDetailWeight(surplusDetail, surplusWeight);
        		splitTotalWeight = NumberUtil.add(splitTotalWeight , splitWeight);
        		surplusTotalWeight = NumberUtil.add(surplusTotalWeight , surplusWeight);
        		splitTotalVolume = NumberUtil.add(splitTotalVolume , splitVolume);
        		surplusTotalVolume = NumberUtil.add(surplusTotalVolume , surplusVolume);
    		} else {
    			// 扣减拆分
    			this.setDetailVolume(splitDetail, getDetailVolume(saveDetail));
    			Double surplusVolume = this.countMinus(getDetailVolume(sourceDetail), getDetailVolume(saveDetail));
    			this.setDetailVolume(surplusDetail, surplusVolume);

    			this.setDetailWeight(splitDetail, getDetailWeight(saveDetail));
    			Double surplusWeight = this.countMinus(getDetailWeight(sourceDetail), getDetailWeight(saveDetail));
    			this.setDetailWeight(surplusDetail, surplusWeight);
        		splitTotalWeight = NumberUtil.add(splitTotalWeight , getDetailWeight(saveDetail));
        		surplusTotalWeight = NumberUtil.add( surplusTotalWeight , surplusWeight);
        		splitTotalVolume = NumberUtil.add( splitTotalVolume , getDetailVolume(saveDetail));
        		surplusTotalVolume = NumberUtil.add( surplusTotalVolume , surplusVolume);
    		}
		}
		this.setQty(this.splitObj, splitTotalQty);
		this.setQty(this.surplusObj, surplusTotalQty);
		this.setWeight(this.splitObj, splitTotalWeight);
		this.setWeight(this.surplusObj, surplusTotalWeight);
		this.setVolume(this.splitObj, splitTotalVolume);
		this.setVolume(this.surplusObj, surplusTotalVolume);
	}
	
	/**
	 * @param id
	 * @param sourceDetail
	 * @return
	 * @version 2017年3月7日 下午5:51:38<br/>
	 * @author andy wang<br/>
	 */
	protected abstract K createSplitDetail(String id, K sourceDetail) throws Exception ;

	/**
	 * 执行规则
	 * @throws Exception
	 * @version 2017年2月16日 下午7:42:51<br/>
	 * @author andy wang<br/>
	 */
	public SplitStrategy<T,K> execute() throws Exception {
		this.check();
		this.create();
		this.split();
		return this;
	}
	

	/**
	 * 比率计算
	 * @param source 计算源
	 * @param scale 比率
	 * @return
	 * @version 2017年2月16日 下午7:34:47<br/>
	 * @author andy wang<br/>
	 * @throws Exception 
	 */
	private Double countRate( double source , double scale ) throws Exception {
//		return Math.ceil(source * scale);
		// 解决相乘精度问题 ： http://www.jb51.net/article/103894.htm
		return NumberUtil.mul(source, scale,2);
	}
	
	/**
	 * 扣减计算
	 * @param source 计算源
	 * @param split 拆分数量
	 * @return
	 * @version 2017年2月16日 下午7:35:09<br/>
	 * @author andy wang<br/>
	 */
	private Double countMinus( double source , double split ) {
		return NumberUtil.sub(source, split);
	}
	
	/**
	 * 计算剩余
	 * @param source 计算源
	 * @param split 拆分数量
	 * @return
	 * @version 2017年2月16日 下午7:35:38<br/>
	 * @author andy wang<br/>
	 */
	private Double countSurplus ( double source , double split ) {
		return NumberUtil.sub(source, split);
	}
}
