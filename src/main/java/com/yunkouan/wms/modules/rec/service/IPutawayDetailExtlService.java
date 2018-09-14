/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * @version 2017年2月22日 上午10:54:05<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.rec.service;

import java.util.List;
import java.util.Map;

import com.yunkouan.wms.modules.rec.entity.RecPutawayDetail;
import com.yunkouan.wms.modules.rec.vo.RecPutawayDetailVO;

/**
 * 上架单明细外部调用业务类<br/><br/>
 * @version 2017年2月22日 上午10:54:05<br/>
 * @author andy wang<br/>
 */
public interface IPutawayDetailExtlService {
	
	/**
	 * 根据上架单明细id，进行确认上架操作
	 * @param recPtwDetailVO 上架单明细
	 * @return 更新的数量
	 * @throws Exception
	 * @version 2017年5月2日 下午3:45:33<br/>
	 * @author andy wang<br/>
	 */
	public int confirmPtwDetail ( RecPutawayDetailVO recPtwDetailVO ) throws Exception;
	
	/**
	 * 根据上架单明细id，更新上架单明细
	 * @param recPtwDetailVO 上架单明细
	 * @return 更新的数量
	 * @throws Exception
	 * @version 2017年3月25日 上午11:10:48<br/>
	 * @author andy wang<br/>
	 */
	public int updatePtwDetail ( RecPutawayDetailVO recPtwDetailVO ) throws Exception ;
	
	/**
	 * 根据ASN单id集合，查询上架单明细
	 * @param listAsnId ASN单id集合
	 * @return 上架单明细集合
	 * @throws Exception
	 * @version 2017年3月23日 下午3:00:33<br/>
	 * @author andy wang<br/>
	 */
	public List<RecPutawayDetail> listPtwDetailByAsnId ( List<String> listAsnId ) throws Exception;
	
	/**
	 * 统计上架数/重/体
	 * @param asnId ASN单id
	 * @return 统计结果
	 * @throws Exception
	 * @version 2017年3月16日 上午11:13:06<br/>
	 * @author andy wang<br/>
	 */
	public RecPutawayDetailVO sumPtwDetailNumByAsnId ( String asnId ) throws Exception;
	
	/**
	 * 根据Asn单id，查询上架明细
	 * @param asnId Asn单id
	 * @return Asn单对应的上架单明细
	 * @throws Exception
	 * @version 2017年3月9日 下午7:59:22<br/>
	 * @author andy wang<br/>
	 */
	public List<RecPutawayDetail> listPtwDetailByAsnId ( String asnId ) throws Exception;
	
	/**
	 * 根据上架单id，查询上架单明细集合
	 * @param recPutawayDetailVO 查询条件
	 * @return 上架单明细Map集合
	 * @throws Exception
	 * @version 2017年2月22日 下午9:34:00<br/>
	 * @author andy wang<br/>
	 */
	public Map<String,RecPutawayDetail> mapPutawayDetailByPtwId ( String putawayId ) throws Exception;
	
	/**
	 * 根据上架单id集合，删除上架单明细
	 * @param listPutawayId 上架单id集合
	 * @return 删除的数量
	 * @throws Exception
	 * @version 2017年3月2日 下午7:58:02<br/>
	 * @author andy wang<br/>
	 */
	public int deletePutawayDetailByPtwId ( List<String> listPutawayId ) throws Exception ;
	
	/**
	 * 根据上架单id，删除上架单明细
	 * @param putawayId 上架单id
	 * @return 删除的数量
	 * @throws Exception
	 * @version 2017年3月2日 下午7:57:33<br/>
	 * @author andy wang<br/>
	 */
	public int deletePutawayDetailByPtwId ( String putawayId ) throws Exception ;
	
	/**
	 * 根据上架单明细id，删除上架单明细
	 * @param putawayDetailId 上架单明细id
	 * @return 删除的数量
	 * @throws Exception
	 * @version 2017年2月27日 下午9:03:05<br/>
	 * @author andy wang<br/>
	 */
	public int deletePutawayDetailById ( String putawayDetailId ) throws Exception ;
	
	/**
	 * 根据上架单id，查询上架单明细集合
	 * @param recPutawayDetailVO 查询条件
	 * @return 上架单明细集合
	 * @throws Exception
	 * @version 2017年2月22日 下午9:34:00<br/>
	 * @author andy wang<br/>
	 */
	public List<RecPutawayDetail> listPutawayDetailByPtwId ( String putawayId ) throws Exception;
	
	/**
	 * 根据id，查询上架单明细
	 * @param recPutawayDetailVO 查询条件
	 * @return 上架单明细
	 * @throws Exception
	 * @version 2017年2月22日 下午9:26:20<br/>
	 * @author andy wang<br/>
	 */
	public RecPutawayDetail findPtwDetailById ( String putawayDetailId ) throws Exception ;
	
	/**
	 * 查询上架单明细
	 * @param recPutawayDetailVO 查询条件
	 * @return 上架单明细
	 * @throws Exception
	 * @version 2017年2月22日 下午9:25:14<br/>
	 * @author andy wang<br/>
	 */
	public RecPutawayDetail findPutawayDetailByExample ( RecPutawayDetailVO recPutawayDetailVO ) throws Exception;
	
	/**
	 * 查询上架单明细集合
	 * @param recPutawayDetailVO 查询条件
	 * @return 上架单明细集合
	 * @throws Exception
	 * @version 2017年2月22日 下午9:22:53<br/>
	 * @author andy wang<br/>
	 */
	public List<RecPutawayDetail> listPutawayDetailByExample ( RecPutawayDetailVO recPutawayDetailVO ) throws Exception;
	
	/**
	 * 保存上架单明细集合
	 * @param listRecPutawayDetail 要保存的上架单明细集合
	 * @return 保存后的上架单明细集合
	 * @throws Exception
	 * @version 2017年2月22日 下午1:22:18<br/>
	 * @author andy wang<br/>
	 */
	public List<RecPutawayDetail> insertPutawayDetail ( List<RecPutawayDetail> listRecPutawayDetail ) throws Exception;
	
	
	/**
	 * 保存上架单明细
	 * @param recPutawayDetail 要保存上架单明细
	 * @return 保存后的上架单明细
	 * @throws Exception
	 * @version 2017年2月22日 下午1:19:58<br/>
	 * @author andy wang<br/>
	 */
	public RecPutawayDetail insertPutawayDetail ( RecPutawayDetail recPutawayDetail ) throws Exception;
}
