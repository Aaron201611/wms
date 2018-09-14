/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * @version 2017年2月15日 下午6:24:38<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.rec.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.Page;
import com.yunkouan.wms.modules.rec.entity.RecAsn;
import com.yunkouan.wms.modules.rec.vo.RecAsnVO;

/**
 * ASN单外部调用业务类接口<br/><br/>
 * @version 2017年2月15日 下午6:24:38<br/>
 * @author andy wang<br/>
 */
public interface IASNExtlService {
	
	/**
	 * 根据ASN单号、PO单号(<font color="red"><strong>模糊</strong></font>)，查询ASN单集合
	 * @param asnNo - ASN单号
	 * @param poNo - PO单号
	 * @return ASN单集合
	 * @throws Exception
	 * @version 2017年3月23日 下午2:50:24<br/>
	 * @author andy wang<br/>
	 */
	public List<String> listAsnIdByAsnPoNo ( String asnNo , String poNo ) throws Exception;
	
	
	/**
	 * 根据条件，统计数据数量
	 * @param recAsnVO 查询条件
	 * @return 数据数量
	 * @throws Exception
	 * @version 2017年3月19日 下午5:41:49<br/>
	 * @author andy wang<br/>
	 */
	public Integer countAsnByExample ( RecAsnVO recAsnVO ) throws Exception;
	
	/**
	 * 根据条件，统计数据数量
	 * @param recAsnVO 查询条件
	 * @return 数据数量
	 * @throws Exception
	 * @version 2017年3月19日 下午5:41:49<br/>
	 * @author zwb<br/>
	 */
	public Integer countAsnByExample2 ( RecAsnVO recAsnVO ) throws Exception;
	
	List<String>getTask(String orgId);
	/**
	 * 根据Asn单id，查询Asn单信息（注意：锁行）
	 * @param asnId Asn单id
	 * @return Asn单信息
	 * @throws Exception
	 * @version 2017年3月10日 上午11:11:47<br/>
	 * @author andy wang<br/>
	 */
	public RecAsn findAsnById4Lock ( String asnId ) throws Exception;
	
	/**
	 * 根据条件，分页查询ASN单<br/>
	 * —— 自带条件当前登录用户的企业id、仓库id
	 * @param recAsnVO 查询条件
	 * @return ASN单集合
	 * @throws Exception
	 * @version 2017年2月28日 下午5:35:56<br/>
	 * @author andy wang<br/>
	 */
	@SuppressWarnings("rawtypes")
	public Page listAsnByPage ( RecAsnVO recAsnVO ) throws Exception ;
	
	
	/**
	 * 根据Asn单VO生成条件，获取Asn单<br/>
	 * —— 自带条件当前登录用户的企业id、仓库id
	 * @param recAsnVO Asn单查询条件
	 * @return Asn单集合
	 * @throws Exception
	 * @version 2017年2月21日 下午2:16:32<br/>
	 * @author andy wang<br/>
	 */
	public RecAsn findAsnByExample ( RecAsnVO recAsnVO ) throws Exception;
	
	/**
	 * 根据Asn单VO生成条件，获取Asn单集合<br/>
	 * —— 自带条件当前登录用户的企业id、仓库id
	 * @param recAsnVO Asn单查询条件
	 * @return Asn单集合
	 * @throws Exception
	 * @version 2017年2月15日 下午6:22:52<br/>
	 * @author andy wang<br/>
	 */
	public List<RecAsn> listAsnByExample ( RecAsnVO recAsnVO ) throws Exception;
	
	/**
	 * 更新Asn单(接口默认设置当前登录人为更新人，使用数据库时间为更新时间)
	 * @param asnVO Asn单
	 * @return 更新的数量
	 * @throws Exception
	 * @version 2017年2月18日 下午11:25:18<br/>
	 * @author andy wang<br/>
	 */
	public int updateAsn ( RecAsnVO asnVO ) throws Exception ;
	
	/**
	 * 查看Asn单信息（查看Asn单页面调用）<br/>
	 *  —— 有字段进行拦截
	 * @param asnId Asn单id<br/>
	 * @return
	 * @throws Exception
	 * @version 2017年2月17日 下午5:14:16<br/>
	 * @author andy wang<br/>
	 */
	public RecAsn viewById ( String asnId ) throws Exception;

	/**
	 * 查看Asn单信息（查看Asn单页面调用）<br/>
	 *  —— 有字段进行拦截
	 * @param asnId Asn单id<br/>
	 * @return
	 * @throws Exception
	 * @version 2017年2月17日 下午5:14:16<br/>
	 * @author andy wang<br/>
	 */
	public RecAsn findByAsnNo ( String asnNo ) throws Exception;
	
	/**
	 * 保存Asn单（只适用于页面第一次保存Asn单）<br/>
	 *  —— 方法内对一些字段进行拦截，不能保存到数据库，如：parentAsnId，receiveQty等;
	 * @param recAsn 要保存的Asn单
	 * @return 带有id的Asn单
	 * @throws Exception
	 * @version 2017年2月17日 下午4:21:33<br/>
	 * @author andy wang<br/>
	 */
	public RecAsn insertAsn( RecAsn recAsn ) throws Exception;
	
	/**
	 * 更新Asn单的收货确认信息
	 * @param recAsn Asn单
	 * @return 更新的数量
	 * @throws Exception
	 * @version 2017年2月17日 下午3:30:15<br/>
	 * @author andy wang<br/>
	 */
	public int confirmAsn ( RecAsn recAsn ) throws Exception ;
	
	/**
	 * 根据Asn单id集合，查询Asn单
	 * @param listAsnId Asn单id集合
	 * @return 	key Asn单id<br/>
	 * 			value Asn单
	 * @throws Exception
	 * @version 2017年2月17日 下午1:48:26<br/>
	 * @author andy wang<br/>
	 */
    public Map<String,RecAsn> mapAsnByIds ( List<String> listAsnId ) throws Exception;
	
	/**
	 * 根据Asn单id，更新状态
	 * @param asnId Asn单id
	 * @param status 状态 - 输入常量中 【Constant.ASN_STATUS】 的值
	 * @return 更新的数量
	 * @throws Exception
	 * @version 2017年2月16日 下午3:29:55<br/>
	 * @author andy wang<br/>
	 * @return 
	 */
	public int updateAsnStatusById ( String asnId , Integer status ) throws Exception;
	
	/**
	 * 删除Asn单集合
	 * @param listAsnId Asn单Id集合
	 * @return 删除的数量
	 * @throws Exception
	 * @version 2017年2月16日 下午2:51:00<br/>
	 * @author andy wang<br/>
	 * @return 
	 */
	public int deleteAsnById ( List<String> listAsnId ) throws Exception;
	
	/**
	 * 根据Asn单id，获取Asn单
	 * @param asnId Asn单id
	 * @return
	 * @throws Exception
	 * @version 2017年2月15日 下午6:22:52<br/>
	 * @author andy wang<br/>
	 */
	public RecAsn findAsnById ( String asnId ) throws Exception;
	
	/**
	 * 根据父Asn单id，查询拆分的Asn单
	 * @param parentAsnId 父Asn单id
	 * @return 拆分的子单集合
	 * @throws Exception
	 * @version 2017年2月16日 下午1:51:20<br/>
	 * @author andy wang<br/>
	 */
	public List<RecAsn> listSplitAsn ( String parentAsnId ) throws Exception;
	
}
