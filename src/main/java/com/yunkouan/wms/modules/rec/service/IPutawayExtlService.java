/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * @version 2017年2月22日 上午10:54:59<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.rec.service;

import java.util.List;

import com.github.pagehelper.Page;
import com.yunkouan.wms.modules.rec.entity.RecPutaway;
import com.yunkouan.wms.modules.rec.vo.RecPutawayVO;

/**
 * 上架单外部调用业务类<br/><br/>
 * @version 2017年2月22日 上午10:54:59<br/>
 * @author andy wang<br/>
 */
public interface IPutawayExtlService {
	
	/**
	 * 根据上架单id，确认上架单
	 * @param recPtwVO 上架单对象
	 * @return 更新的数量
	 * @throws Exception
	 * @version 2017年5月2日 下午3:57:07<br/>
	 * @author andy wang<br/>
	 */
	public int confirmPtw ( RecPutawayVO recPtwVO ) throws Exception;
	
	
	/**
	 * 更新上架单
	 * @param recPtwVO 上架单VO
	 * @return 更新的数量
	 * @throws Exception
	 * @version 2017年3月25日 上午11:04:41<br/>
	 * @author andy wang<br/>
	 */
	public int updatePtw ( RecPutawayVO recPtwVO ) throws Exception;
	
	
	/**
	  * 根据ASN单号、PO单号，查询上架单id集合
	  * @param asnNo - ASN单号
	  * @param poNo - PO单号
	  * @param ownerComment - 货主名称
	  * @return 上架单id集合
	  * @throws Exception
	  * @version 2017年3月23日 下午2:53:29<br/>
	  * @author andy wang<br/>
	  */
	public List<String> listPtwByAsnPoNo ( String asnNo , String poNo , String ownerComment ) throws Exception;
	
	/**
	 * 根据库位、货品、批次，查询对应状态的上架单（库存模块使用）
	 * @param locationId 库位id
	 * @param skuId 货品id
	 * @param batchNo 货品批次
	 * @param status 上架单的状态
	 * @return 对应条件的上架单
	 * —— sumQty表示对应的数量
	 * @throws Exception
	 * @version 2017年3月20日 下午4:19:36<br/>
	 * @author andy wang<br/>
	 */
	public List<RecPutawayVO> listPtwByStock ( String locationId , String skuId , String batchNo , Integer... status ) throws Exception ;
	
	/**
	 * 根据上架单id，删除上架单
	 * @param listPutawayId 上架单id
	 * @return 删除的数量
	 * @throws Exception
	 * @version 2017年3月2日 下午8:08:12<br/>
	 * @author andy wang<br/>
	 */
	public int deletePutawayById ( List<String> listPutawayId ) throws Exception;
	
	/**
	 * 根据父上架单id，查询拆分的上架单
	 * @param parentPutawayId 父上架单id
	 * @return 拆分的子单集合
	 * @throws Exception
	 * @version 2017年3月2日 下午7:35:41<br/>
	 * @author andy wang<br/>
	 */
	public List<RecPutaway> listSplitPutaway ( String parentPutawayId ) throws Exception;
	
	/**
	 * 根据条件，分页查询上架单信息
	 * @param recPutawayVO 查询条件
	 * @return 上架单集合
	 * @throws Exception
	 * @version 2017年2月28日 下午1:53:50<br/>
	 * @author andy wang<br/>
	 */
	public Page<RecPutaway> listPutawayByPage ( RecPutawayVO recPutawayVO ) throws Exception;
	
	/**
	 * 根据条件，查询上架单信息
	 * @param recPutawayVO 查询条件
	 * @return 上架单集合
	 * @throws Exception
	 * @version 2017年2月28日 下午1:53:50<br/>
	 * @author andy wang<br/>
	 */
	public List<RecPutaway> selectPutaway ( RecPutawayVO recPutawayVO ) throws Exception ;
	
	/**
	 * 更新上架单状态
	 * @param putawayId 上架单id
	 * @param status 上架单状态
	 * @return 更新的数量
	 * @throws Exception
	 * @version 2017年2月23日 下午1:37:01<br/>
	 * @author andy wang<br/>
	 */
	public int updatePutawayStatus ( String putawayId , Integer status ) throws Exception;
	
	/**
	 * 根据上架单id，查询上架单
	 * @param putawayId 上架单id
	 * @return 上架单
	 * @throws Exception
	 * @version 2017年2月22日 下午6:02:20<br/>
	 * @author andy wang<br/>
	 */
	public RecPutaway findPutawayById ( String putawayId ) throws Exception ;
	
	/**
	 * 根据条件查询上架单
	 * @param recPutawayVO 查询条件
	 * @return 上架单
	 * @throws Exception
	 * @version 2017年2月22日 下午6:01:50<br/>
	 * @author andy wang<br/>
	 */
	public RecPutaway findPutawayByExample ( RecPutawayVO recPutawayVO ) throws Exception;
	
	/**
	 * 保存上架单
	 * @param recPutaway 要保存的上架单
	 * @return 保存后的上架单
	 * @throws Exception
	 * @version 2017年2月22日 上午11:53:13<br/>
	 * @author andy wang<br/>
	 */
	public RecPutaway insertPutaway ( RecPutaway recPutaway ) throws Exception;
	
	
	/**
	 * 根据条件，查询上架单集合
	 * @param recPutawayVO 上架单查询条件
	 * @return 上架单集合
	 * @throws Exception
	 * @version 2017年2月22日 上午11:46:15<br/>
	 * @author andy wang<br/>
	 */
	public List<RecPutaway> listPutawayByExample ( RecPutawayVO recPutawayVO ) throws Exception;
	
	/**
	 * 根据条件，查询上架单集合
	 * @param recPutawayVO 上架单查询条件
	 * @return 上架单集合
	 * @throws Exception
	 * @version 2017年2月22日 上午11:46:15<br/>
	 * @author zwb<br/>
	 */
	public List<RecPutaway> listPutawayByExample2 ( RecPutawayVO recPutawayVO ) throws Exception;
	
	List<String>getTask(String orgId);
}
