/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * @version 2017年2月15日 下午3:06:38<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.rec.service;

import java.util.List;
import java.util.Map;

import com.yunkouan.wms.modules.rec.entity.RecAsnDetail;
import com.yunkouan.wms.modules.rec.vo.RecAsnDetailVO;

/**
 * ASN单明细外部调用业务类接口<br/><br/>
 * @version 2017年2月15日 下午3:06:38<br/>
 * @author andy wang<br/>
 */
public interface IASNDetailExtlService {
	
	/**
	 * 根据Asn单id集合，查询Asn单明细，并使用Map返回
	 * @param asnId Asn单id
	 * @return 	key Asn单明细id<br/>
	 * 			value Asn单明细
	 * @return
	 * @throws Exception
	 * @version 2017年5月4日 下午3:56:38<br/>
	 * @author andy wang<br/>
	 */
	public Map<String,RecAsnDetail> mapAsnDetailByAsnIds ( List<String> asnIds ) throws Exception;
	
	/**
	 * 根据ASN单id集合，查询ASN单明细集合
	 * @param asnIds ASN单id集合
	 * @return ASN单明细集合
	 * @throws Exception
	 * @version 2017年5月4日 下午3:33:03<br/>
	 * @author andy wang<br/>
	 */
	public List<RecAsnDetail> listAsnDetailByAsnIds ( List<String> asnIds ) throws Exception;
	
	/**
	 * 根据条件，查询ASN单明细
	 * @param recAsnDetailVO ASN单明细
	 * @return ASN单明细集合
	 * @throws Exception
	 * @version 2017年3月20日 下午8:12:36<br/>
	 * @author andy wang<br/>
	 */
	public List<RecAsnDetail> listAsnDetailByExample ( RecAsnDetailVO recAsnDetailVO ) throws Exception ;
	
	/**
	 * 根据Asn单明细id，查询Asn单
	 * @param asnDetailId Asn单明细id
	 * @return Asn单
	 * @throws Exception
	 * @version 2017年2月21日 上午10:36:58<br/>
	 * @author andy wang<br/>
	 */
	public RecAsnDetail findByDetailId ( String asnDetailId ) throws Exception;
	
	/**
	 * 批量保存Asn单明细
	 * @param listRecAsnDetail Asn单明细集合
	 * @return 保存后的Asn单名字集合
	 * @throws Exception
	 * @version 2017年2月20日 下午3:20:28<br/>
	 * @author andy wang<br/>
	 */
	public List<RecAsnDetail> insertAsnDetail ( List<RecAsnDetail> listRecAsnDetail ) throws Exception;
	
	/**
	 * 根据Asn单Id，删除Asn单明细
	 * @param listAsnDetailId Asn单明细Id集合
	 * @return 删除的数量
	 * @throws Exception
	 * @version 2017年2月16日 下午2:51:00<br/>
	 * @author andy wang<br/>
	 * @return 
	 */
	public int deleteListByAsnDetailId ( List<String> listAsnDetailId ) throws Exception;
	
	/**
	 * 更新Asn单明细
	 * @param recAsnDetail Asn单明细
	 * @return 更新的数量
	 * @throws Exception
	 * @version 2017年2月19日 上午6:31:13<br/>
	 * @author andy wang<br/>
	 */
	public int updateAsnDetail ( RecAsnDetail recAsnDetail ) throws Exception;
	
	/**
	 * 根据Asn单id，查询Asn单明细
	 * @param asnId Asn单id
	 * @return
	 * @throws Exception
	 * @version 2017年2月17日 下午5:36:27<br/>
	 * @author andy wang<br/>
	 */
	public List<RecAsnDetail> listSkuByAsnId ( String asnId ) throws Exception;
	
	/**
	 * 保存Asn单明细（只适用于页面第一次保存Asn单）<br/>
	 *  —— 方法内对一些字段进行拦截，不能保存到数据库，如：parentAsnDeailId，receiveQty等;
	 *  —— 创建人、仓库、企业，自动使用当前登录人信息填充
	 * @param recAsnDetail Asn单明细
	 * @return 带有id的Asn单明细
	 * @throws Exception
	 * @version 2017年2月17日 下午4:29:35<br/>
	 * @author andy wang<br/>
	 */
	public RecAsnDetail insertAsnDetail ( RecAsnDetail recAsnDetail ) throws Exception;
	
	/**
	 * 更新Asn单明细的收货确认信息
	 * @param recAsnDetail Asn单明细
	 * @return 更新的数量
	 * @throws Exception
	 * @version 2017年2月17日 下午3:13:33<br/>
	 * @author andy wang<br/>
	 */
	public int confirmAsnDetail ( RecAsnDetail recAsnDetail ) throws Exception;
	
	
	/**
	 * 根据Asn单明细id集合，查询Asn单明细
	 * @param asnDetailId Asn单明细id集合
	 * @return 	key Asn单明细id<br/>
	 * 			value Asn单明细
	 * @throws Exception
	 * @version 2017年2月17日 上午11:43:25<br/>
	 * @author andy wang<br/>
	 */
	public Map<String,RecAsnDetail> mapAsnDetailByDetailId ( List<String> listAsnDetailId ) throws Exception;
	
	/**
	 * 根据Asn单Id，删除Asn单明细
	 * @param listAsnDetailId Asn单明细Id集合
	 * @return 删除的数量
	 * @throws Exception
	 * @version 2017年2月16日 下午2:51:00<br/>
	 * @author andy wang<br/>
	 * @return 
	 */
	public int deleteListByAsnId ( List<String> listAsnDetailId ) throws Exception;
	
	/**
	 * 根据Asn单id，查询Asn单明细，并使用Map返回
	 * @param asnId Asn单id
	 * @return 	key Asn单明细id<br/>
	 * 			value Asn单明细
	 * @throws Exception
	 * @version 2017年2月15日 下午3:09:12<br/>
	 * @author andy wang<br/>
	 */
	public Map<String,RecAsnDetail> mapAsnDetailByAsnId ( String asnId ) throws Exception;
	
	
	/**
	 * 根据Asn单id，查询Asn单明细
	 * @param asnId Asn单id
	 * @return
	 * @throws Exception
	 * @version 2017年2月15日 下午5:01:22<br/>
	 * @author andy wang<br/>
	 */
	public List<RecAsnDetail> listAsnDetailByAsnId ( String asnId ) throws Exception;

	public List<RecAsnDetail> insertAsnDetailComfirm(List<RecAsnDetail> newDetailList) throws Exception;
	
}
