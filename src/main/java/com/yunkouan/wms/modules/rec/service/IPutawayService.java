/**
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * 创建日期:<br/> 2017年2月8日 下午3:59:52<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.rec.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.yunkouan.wms.modules.rec.vo.RecPutawayVO;

/**
 * 上架单服务接口<br/><br/>
 * <b>创建日期</b>:<br/> 2017年2月8日 下午3:59:52<br/>
 * @author andy wang<br/>
 */
public interface IPutawayService {
	/**
	 * 检查库位所属库区的货品类型跟货品所属一级货品类型是否一致
	 * @param skuId
	 * @param locationId
	 * @return
	 */
	public boolean isSameSkuType(String skuId, String locationId);

	/**
	 * 收货确认后，根据ASN单id，创建上架单 TODO
	 * @param listAsnId ASN单id
	 * @return 上架单
	 * @throws Exception
	 * @version 2017年5月4日 下午3:21:33<br/>
	 * @author andy wang<br/>
	 */
	public List<RecPutawayVO> createPutaway ( List<String> listAsnId ) throws Exception;
	
	/**
	 * 更新并生效上架单
	 * @param param_recPutawayVO 需要更新的上架单
	 * @throws Exception
	 * @version 2017年5月2日 下午4:16:16<br/>
	 * @author andy wang<br/>
	 */
	public void updateAndEnable ( RecPutawayVO param_recPutawayVO ) throws Exception;
	
	/**
	 * 保存并生效上架单
	 * @param param_recPutawayVO 需要保存的上架单
	 * @throws Exception
	 * @version 2017年5月2日 下午4:14:25<br/>
	 * @author andy wang<br/>
	 */
	public void addAndEnable ( RecPutawayVO param_recPutawayVO ) throws Exception;
	
	/**
	 * 自动分配上架库位
	 * @param param_recPutawayVO 上架信息
	 * @return 包含分配好上架库位的上架信息
	 * @throws Exception
	 * @version 2017年3月14日 下午6:12:56<br/>
	 * @author andy wang<br/>
	 */
//	public RecPutawayVO auto( RecPutawayVO param_recPutawayVO ) throws Exception;
	
	
	/**
	 * 上架单批量确认
	 * @param param_recPutawayVO 上架单对象
	 * @throws Exception
	 * @version 2017年3月5日 上午7:56:48<br/>
	 * @author andy wang<br/>
	 */
	public void batchConfirmPutaway ( RecPutawayVO param_recPutawayVO ) throws Exception ;
	
	/**
	 * 上架单确认
	 * @param recPutaway 上架单
	 * @throws Exception
	 * @version 2017年3月2日 下午8:45:53<br/>
	 * @author andy wang<br/>
	 */
	public void complete( RecPutawayVO param_recPutawayVO ) throws Exception;
	
	/**
	 * 取消拆分上架单
	 * @param param_listPutawayId 上架单id集合
	 * @throws Exception
	 * @version 2017年3月2日 上午11:53:32<br/>
	 * @author andy wang<br/>
	 */
	public void unsplit ( List<String> param_listPutawayId ) throws Exception;
	
	/**
	 * 拆分上架单
	 * @param putawayId 上架单id
	 * @throws Exception
	 * @version 2017年3月2日 上午8:42:13<br/>
	 * @author andy wang<br/>
	 */
	public void split ( RecPutawayVO param_recPutawayVO ) throws Exception;
	
	/**
	 * 打印上架单
	 * @param param_putawayId 上架单id
	 * @throws Exception
	 * @version 2017年3月1日 下午8:37:46<br/>
	 * @author andy wang<br/>
	 */
	public void printPutaway ( String param_putawayId ) throws Exception;
	
	/**
	 * 根据id，显示上架单信息
	 * @param param_putawayId 上架单id
	 * @return 上架单信息
	 * @throws Exception
	 * @version 2017年3月1日 下午2:16:41<br/>
	 * @author andy wang<br/>
	 */
	public RecPutawayVO view ( String param_putawayId ) throws Exception ;
	
	/**
	 * 根据条件，分页查询上架单
	 * @param param_recPutawayVO 查询条件
	 * @return 上架单集合
	 * @throws Exception
	 * @version 2017年2月28日 下午1:48:21<br/>
	 * @author andy wang<br/>
	 */
	public Page<RecPutawayVO> list ( RecPutawayVO param_recPutawayVO ) throws Exception;
	public List<RecPutawayVO> list4print ( RecPutawayVO param_recPutawayVO ) throws Exception;
	
	/**
	 * 取消上架单
	 * @param param_listPutawayId 上架单id集合
	 * @throws Exception
	 * @version 2017年2月28日 上午10:10:58<br/>
	 * @author andy wang<br/>
	 */
	public void cancel ( List<String> param_listPutawayId ) throws Exception ;
	
	/**
	 * 更新上架单
	 * @param recPutawayVO 上架单VO
	 * @throws Exception
	 * @version 2017年2月27日 下午5:04:37<br/>
	 * @author andy wang<br/>
	 */
	@Transactional(readOnly=false,rollbackFor=Exception.class)
	public void update ( RecPutawayVO param_recPutawayVO ) throws Exception;
	
	/**
	 * 失效上架单
	 * @param listPutawayId 上架单id集合
	 * @throws Exception
	 * @version 2017年2月26日 下午6:04:52<br/>
	 * @author andy wang<br/>
	 */
	public void disable ( List<String> listPutawayId ) throws Exception;
	
	/**
	 * 生效上架单
	 * @param param_putawayVO 上架单VO
	 * @version 2017年2月22日 下午5:45:23<br/>
	 * @author andy wang<br/>
	 * @throws Exception 
	 */
	public void enable ( RecPutawayVO param_putawayVO ) throws Exception;
	
	/**
	 * 保存上架单
	 * @param param_recPutawayVO 要保存的上架单
	 * @return 保存后的上架单对象
	 * @throws Exception
	 * @version 2017年2月20日 下午3:08:04<br/>
	 * @author andy wang<br/>
	 */
	public RecPutawayVO add ( RecPutawayVO param_recPutawayVO ) throws Exception;

//	public ResponseEntity<byte[]> downloadExcel(InStockVO vo) throws Exception;
	
	/**
	 * 传送入库数据对接仓储企业联网监管系统
	 * @throws Exception
	 */
//	public void transmitInstockXML(String putawayId) throws Exception;

	public static final String ASSIS_STATUS_INIT = "0";
	public static final String ASSIS_STATUS_SENDED = "1";
	public static final String ASSIS_STATUS_SUCCESS = "2";
	public static final String ASSIS_STATUS_FAILED = "3";

	/** 
	* @Title: updateAssisStatus 
	* @Description: 更新发送辅助系统状态
	* @auth tphe06
	* @time 2018 2018年7月30日 下午2:14:26
	* @param putawayId
	* @param status 发送辅助系统状态，null未发送，1已经发送，2辅助系统返回成功，3辅助系统返回失败
	* @return
	* @throws Exception
	* int
	*/
	public int updateAssisStatus(String putawayId, String status) ;
}