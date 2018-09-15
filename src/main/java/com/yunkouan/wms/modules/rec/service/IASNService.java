/**
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * 创建日期:<br/> 2017年2月8日 下午3:47:30<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.rec.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.github.pagehelper.Page;
import com.yunkouan.wms.modules.rec.vo.RecAsnVO;
import com.yunkouan.wms.modules.send.vo.ems.ErpResult;

/**
 * ASN单服务接口<br/>
 * 创建日期:<br/> 2017年2月8日 下午3:47:30<br/>
 * @author andy wang<br/>
 */
public interface IASNService {
	
	/**
	 * 更新并生效仓库
	 * @param param_warehouseVO 仓库对象
	 * @return 生效后的仓库
	 * @throws Exception
	 * @version 2017年6月19日 下午2:09:06<br/>
	 * @author andy wang<br/>
	 */
	public RecAsnVO updateAndEnable ( RecAsnVO param_asnVO ) throws Exception;
	
	
	/**
	 * 保存并生效仓库
	 * @param param_asnVO 仓库对象
	 * @return 生效后的仓库
	 * @throws Exception
	 * @version 2017年6月19日 下午2:09:06<br/>
	 * @author andy wang<br/>
	 */
	public RecAsnVO addAndEnable ( RecAsnVO param_asnVO ) throws Exception;
	
	
	/**
	 * 查询暂存区的ASN单列表
	 * @return 暂存区的ASN单列表
	 * @throws Exception
	 * @version 2017年3月30日 下午2:18:01<br/>
	 * @author andy wang<br/>
	 * @param docType 
	 */
	public List<RecAsnVO> listStock (Integer docType) throws Exception;
	
	
	/**
     * 导入ASN单
     * @param param_listRecAsnVO 导入的asn单集合
     * @throws Exception
     * @version 2017年2月20日 上午11:46:18<br/>
     * @author andy wang<br/>
     */
    public String upload ( List<RecAsnVO> param_listRecAsnVO ) throws Exception;
	
	/**
     * 更新Asn单
     * @param param_recAsnVO Asn单信息
     * @throws Exception
     * @version 2017年2月18日 下午2:44:59<br/>
     * @author andy wang<br/>
	 * @return 
     */
    public RecAsnVO update ( RecAsnVO param_recAsnVO ) throws Exception;
	
	/**
     * 批量收货确认
     * @param param_recAsnVO 收货确认数据
     * —— 提供listAsnId，locationId
     * @throws Exception
     * @version 2017年2月18日 下午2:08:49<br/>
     * @author andy wang<br/>
     */
	public void batch( RecAsnVO param_recAsnVO ) throws Exception;
	/**
	 * 批量收货确认并自动生成上架单
	 * @param param_recAsnVO
	 * @throws Exception
	 */
	public void batchAndAddPtw( RecAsnVO param_recAsnVO ) throws Exception;

	/**
     * 收货确认 
     * @param param_recAsnVO 确认收货的Asn单（asnId,listSavetAsnDetail不能为空）
     * @throws Exception
     * @version 2017年2月16日 下午4:10:04<br/>
     * @author andy wang<br/>
     */
	public void complete( RecAsnVO param_recAsnVO ) throws Exception;
	
	/**
	 * 更新ERP入库单
	 * @param _recAsnVo
	 * @throws Exception
	 */
//	public ErpResult updateERPInventory(RecAsnVO _recAsnVo) throws Exception;
	
	/**
	 * 收货确认和自动生成上架单 TODO
	 * @param param_recAsnVO 确认收货的Asn单（asn,listSavetAsnDetail/listAsnDetailVO不能为空）
	 * @throws Exception
	 */
	public void completeAndPutaway( RecAsnVO param_recAsnVO ) throws Exception;

	/**
     * 取消拆分Asn单
     * @param param_listAsnId 要被恢复的Asn单 子单id
     * @throws Exception
     * @version 2017年2月16日 下午1:44:58<br/>
     * @author andy wang<br/>
     */
    public void unsplit ( List<String> param_listAsnId ) throws Exception;
	
    /**
     * 拆分ASN单
     * @param param_recAsnVO 需要拆分的ASN单
     * @throws Exception
     * @version 2017年2月15日 下午2:00:11<br/>
     * @author andy wang<br/>
     */
    public void split ( RecAsnVO param_recAsnVO ) throws Exception;
    
    /**
     * 取消ASN单
     * @param param_listAsnId Asn单id集合
     * @throws Exception
     * @version 2017年2月15日 下午2:00:11<br/>
     * @author andy wang<br/>
     */
    public void cancel ( List<String> param_listAsnId ) throws Exception;
    
    /**
     * 通过单号取消收货单
     * @param asnVo
     * @throws Exception
     */
    public void cancelByNo(RecAsnVO asnVo)throws Exception;
    
    /**
     * 失效Asn单
     * @param param_listAsnId Asn单id集合
     * @throws Exception
     * @version 2017年2月15日 下午1:32:50<br/>
     * @author andy wang<br/>
     */
    public void disable ( List<String> param_listAsnId ) throws Exception;
    
    /**
     * 生效Asn单
     * @param param_asnVO Asn单VO
     * @throws Exception
     * @version 2017年2月14日 下午6:03:51<br/>
     * @author andy wang<br/>
     */
    public void enable ( RecAsnVO param_asnVO ) throws Exception ;
    /**
     * 新增Asn单
     * @param asnVO Asn单VO
     * @return
     * @throws Exception
     * @version 2017年2月14日 上午11:06:22<br/>
     * @author andy wang<br/>
     */
    public RecAsnVO add ( RecAsnVO param_recAsnVO ) throws Exception;
    
    /**
     * 导入Asn单
     * @param _recAsnVO
     * @return
     * @throws Exception
     */
    public RecAsnVO importAsn(RecAsnVO _recAsnVO ) throws Exception;
    
    /**
	 * 查询ASN单列表
	 * @param param_recAsnVO 查询条件
	 * @return 返回对象
	 * @throws Exception
	 * 创建日期:<br/> 2017年2月9日 下午1:44:07<br/>
	 * @author andy wang<br/>
	 */
	public Page<RecAsnVO> list( RecAsnVO param_recAsnVO) throws Exception;
	/**
	 * 查询ASN单列表和货品详情
	 * @param param_recAsnVO listAsnId 待打印ASN单id列表
	 * @return
	 * @throws Exception
	 */
	public List<RecAsnVO> list4print( RecAsnVO param_recAsnVO) throws Exception;

	/**
	 * 根据asn单id查询asn信息
	 * @param asnId asn单id
	 * @return asn单信息
	 * @throws Exception
	 * 创建日期:<br/> 2017年2月10日 下午2:39:51<br/>
	 * @author andy wang<br/>
	 */
    public RecAsnVO view ( String param_asnId ) throws Exception ;


	/**
	 * 下载excel
	 * @param vo
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年10月24日 下午1:23:53<br/>
	 * @author 王通<br/>
	 */
	public ResponseEntity<byte[]> downloadExcel(RecAsnVO vo) throws Exception;
	
	/**
	 * 将所有仓库的入库数据发送到海关
	 * @param warehouseIdList
	 * @throws Exception
	 */
//	public void findAndSendInStockData(List<String> warehouseIdList)throws Exception;
	
	/**
	 * 批量同步入库数据
	 * @param idList
	 * @throws Exception
	 */
//	public void batchSyncInstockData(List<String> idList) throws Exception;
	
	/**
	 * 传送入库数据对接仓储企业联网监管系统
	 * @throws Exception
	 */
//	public void transmitInstockXML(String asnId) throws Exception;
	
}