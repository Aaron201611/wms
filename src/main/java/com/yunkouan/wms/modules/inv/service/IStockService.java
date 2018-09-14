package com.yunkouan.wms.modules.inv.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.Page;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.modules.inv.entity.InvLog;
import com.yunkouan.wms.modules.inv.entity.InvStock;
import com.yunkouan.wms.modules.inv.vo.InStockVO;
import com.yunkouan.wms.modules.inv.vo.InvCountVO;
import com.yunkouan.wms.modules.inv.vo.InvLogVO;
import com.yunkouan.wms.modules.inv.vo.InvSkuStockVO;
import com.yunkouan.wms.modules.inv.vo.InvStockVO;
import com.yunkouan.wms.modules.inv.vo.InvWarnVO;
import com.yunkouan.wms.modules.send.vo.TotalVo;

/**
 * 在库业务接口
 */
public interface IStockService {

    /**
     * 库存冻结
     * @param idList
     * @required 
     * @optional  
     * @Description 
     * @version 2017年3月2日 上午10:30:50<br/>
     * @author 王通<br/>
     */
    public void freezeList(List<String> idList);

    /**
     * 库存解冻
     * @param idList
     * @required 
     * @optional  
     * @Description 
     * @version 2017年3月2日 上午10:31:18<br/>
     * @author 王通<br/>
     */
    public void unfreezeList(List<String> idList);

    /**
	 * 快速移位
	 * @param vo
	 * @required outStockVo.findNum, outStockVo.invStock.stockId,
	 * inStockVo.invStock.locationId
	 * @optional
	 * @Description 
	 * 1、获取库存信息
	 * 2、比较库存数量
	 * 3、出库存（包括生成日志）
	 * 4、入库存（包括生成日志）
	 * 5、生成移位单
	 * @version 2017年2月24日 下午5:59:35<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
    public void shift(InvStockVO outStockVo)  throws Exception ;

    /**
     * 库存日志列表数据查询
     * @param page 
     * @return
     */
    public Page<InvLogVO> logList(InvLogVO logVO)  throws Exception;

	/**
	 * 检查是否有库存
	 * @param stockVo
	 * @return
	 * @throws Exception
	 * @Description 
	 * @version 2017年2月17日 下午1:42:13<br/>
	 * @author 王通<br/>
	 */
    public void checkStock(InvStockVO stockVo) throws Exception;
//	/**
//	 * 检查库存是否冻结
//	 * @param stockVo
//	 * @return
//	 * @throws Exception
//	 * @Description 
//	 * @version 2017年2月17日 下午1:42:13<br/>
//	 * @author 王通<br/>
//	 */
//    public void checkStockFreeze(InvStockVO stockVo) throws Exception;

	/**
	 * 查询库存列表
	 * @param skuVo
	 * @return
	 * @Description 
	 * @version 2017年2月17日 下午1:42:22<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
	public List<InvStock> list(InvStockVO stockVo) throws Exception;

	/**
	 * 锁定出库库存
	 * @param stockVo
	 * @return
	 * @Description 
	 * @version 2017年2月17日 下午1:42:30<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
	public void lockOutStock(InvStockVO stockVo) throws Exception;
	/**
	 * 解锁出库库存
	 * @param stockVo
	 * @return
	 * @Description 
	 * @version 2017年2月17日 下午1:42:30<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
	public void unlockOutStock(InvStockVO stockVo) throws Exception;

//	/**
//	 * 增加库存
//	 * @param skuVo
//	 * 必填：库位id，货品id，数量，操作类型，日志类型，单据号，企业id，仓库id
//	 * @return
//	 * @Description 
//	 * @version 2017年2月17日 下午1:42:35<br/>
//	 * @author 王通<br/>
//	 */
//	public void addStock(InvStock stock);

//	/**
//	 * 更新库存信息，适用于各种修改库存操作，请将不需要更新的字段置为空
//	 * @param stock
//	 * @return
//	 * @Description 
//	 * @version 2017年2月22日 下午3:05:47<br/>
//	 * @author 王通<br/>
//	 */
//	public void updateStock(InvStock stock);
//	/**
//	 * 删除库存信息，用于删除库存操作
//	 * @param stock
//	 * @return
//	 * @Description 
//	 * @version 2017年2月22日 下午3:05:47<br/>
//	 * @author 王通<br/>
//	 */
//	public void deleteStock(String id);
//	/**
//	 * 保存库存日志
//	 * @param skuVo
//	 * @return
//	 * @Description 
//	 * @version 2017年2月17日 下午3:40:11<br/>
//	 * @author 王通<br/>
//	 */
//	public InvLog addStockLog(InvLog invLog);
	
	/**
	 * 快速修改库存
	 * @param stockVo
	 * @throws Exception
	 * @required 库存ID:invStock.stockId，调整原因:invLog.note,批次号:invStock.batchNo,调整后数量:findNum
	 * @optional  
	 * @Description 在库容满足的同时进行入库(新增或修改)，避免异步操作
	 * @version 2017年3月28日 下午3:53:24<br/>
	 * @author 王通<br/>
	 */
	public void changeStock(InvStockVO stockVo) throws Exception;
	
	/**
	 * 导入库存
	 * @param fileLicense
	 * @Description 在库容满足的同时进行入库(新增或修改)，避免异步操作
	 * @version 2017年2月23日16:52:08<br/>
	 * @author 王通<br/>
	 * @return 库存录入条数
	 * @throws Exception 
	 */
	public void importStock(MultipartFile fileLicense) throws Exception;
	
	/**
	 * 库存入库
	 * @param stockVo
	 * @return
	 * @throws Exception
	 * @required invStock.skuId,invStock.locationId,invStock.findNum,
	 * invLog.opPerson,invLog.opType,invLog.invoiceBill,
	 * invLog.logType,invLog.localtionId,invLog.skuId,invLog.qty
	 * @optional invStock.packId,invStock.batchNo,invStock.asnDetailId,
	 *  invStock.proDate,invLog.note,invLog.batchNo
	 * @Description 
	 * 1、新增库位使用库容(同时不释放预分配库容,判断库容是否足够,判断库位是否冻结)	
	 * 2、判断是否有相同库存，是则更新库存数量
	 * 3、否则直接新增库存
	 * 4、保存库存日志
	 * @version 2017年2月28日 下午3:16:03<br/>
	 * @author 王通<br/>
	 */
	public InvStock inStock(InvStockVO stockVo) throws Exception;

	/**
	 * 库存出库
	 * @param stockVo
	 * @throws Exception
	 * @required invStock.skuId,invStock.locationId,invStock.findNum,
	 * invLog.opPerson,invLog.opType,invLog.invoiceBill,
	 * invLog.logType,invLog.localtionId,invLog.skuId,invLog.qty
	 * @optional invStock.packId,invStock.batchNo,invStock.asnDetailId,
	 *  invLog.note,invLog.batchNo
	 * @Description 
	 * 1、不释放预分配库存
	 * 2、减少库位使用库容(判断库容是否足够,判断库位是否冻结)	
	 * 3、判断是否有库存，数量小于库存总数则更新库存数量
	 * 4、数量等于库存总数则删除库存
	 * 5、大于则报错
	 * 6、保存库存日志
	 */
	public void outStock(InvStockVO stockVo) throws Exception;


	/**
	 * @param vo
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月6日 下午1:43:44<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
	public Page<InvStockVO> listByPage(InvStockVO vo) throws Exception;

	/** 
	* @Title: listByT 
	* @Description: 退货库存列表查询
	* @auth tphe06
	* @time 2018 2018年8月29日 上午11:12:50
	* @param vo
	* @return
	* @throws Exception
	* Page<InvStockVO>
	*/
	public List<InvStockVO> listByT(InvStockVO vo, Principal p) throws Exception;

	/**
	 * 查看库存详情
	 * @param id
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月7日 上午11:23:42<br/>
	 * @author 王通<br/>
	 */
	public InvStock view(String id);

	/**
	 * 获取库位动碰次数
	 * @param locationId
	 * @return
	 * @throws Exception
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月13日 上午9:29:08<br/>
	 * @author 王通<br/>
	 */
	public Integer countLocationTouch(String locationId, Date startDate, Date endDate) throws Exception;

	/**
	 * 库存告警列表
	 * @param stockVo
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月13日 下午4:20:54<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
	public Page<InvWarnVO> warnList(InvWarnVO vo) throws Exception;

	/**
	 * 获取库存预分配详情
	 * @param id
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * 1、检查拣货单预分配详情
	 * 2、检查移位单预分配详情
	 * 3、检查上架单预分配详情
	 * @version 2017年3月20日 下午2:35:21<br/>
	 * @author 王通<br/>
	 */
	public InvStockVO outLockDetail(String id) throws Exception;

	/**
	 * 预分配重算
	 * @param id
	 * @required 
	 * @optional  
	 * @Description 
	 * 1、检查拣货单预分配详情
	 * 2、检查移位单预分配详情
	 * 3、检查上架单预分配详情
	 * 4、更新预分配数量
	 * @version 2017年3月20日 下午2:49:32<br/>
	 * @author 王通<br/>
	 * @return 
	 * @throws Exception 
	 */
	public Double refreshOutLock(String id) throws Exception;

	/**
	 * @param vo
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月29日 上午11:05:59<br/>
	 * @author 王通<br/>
	 */
	public void freeze(InvStockVO vo) throws Exception ;

	/**
	 * @param vo
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月29日 上午11:06:41<br/>
	 * @author 王通<br/>
	 */
	public void unfreeze(InvStockVO vo) throws Exception ;

	/**
	 * 根据盘点条件查询库存
	 * @param vo
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年4月1日 上午9:44:35<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
	public List<InvStockVO> listByCount(InvCountVO vo) throws Exception;


	/**
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年4月21日 下午6:20:25<br/>
	 * @author 王通<br/>
	 * @throws IOException 
	 * @throws Exception 
	 */
	public ResponseEntity<byte[]> downloadStockDemo() throws Exception;

	/**
	 * @param vo
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年6月28日 下午1:58:02<br/>
	 * @author 王通<br/>
	 */
	public Page<InvStockVO> listStockSku(InvStockVO vo) throws Exception;

	/**
	 * @param stockVo
	 * @return
	 * @throws Exception
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年8月24日 上午10:51:30<br/>
	 * @author 王通<br/>
	 */
	public boolean checkStockRet(InvStockVO stockVo) throws Exception;

	/**
	 * 移位至发货区
	 * @param outStockVo
	 * @throws Exception
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年8月24日 上午11:29:06<br/>
	 * @author 王通<br/>
	 */
	public void shiftToSend(String deliveryId) throws Exception;
//	public void shiftToSend(InvStockVO outStockVo) throws Exception;
	/**
	 * @param inStockVo
	 * @throws Exception
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年8月24日 下午2:49:30<br/>
	 * @author 王通<br/>
	 */
//	public void shiftToPick(InvStockVO inStockVo) throws Exception;

	/**
	 * @param skuId
	 * @throws Exception
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年8月24日 下午4:48:31<br/>
	 * @author 王通<br/>
	 */
	public void repPickSku(Set<String> skuIdSet, String involveBill) throws Exception;

	/**
	 * @param skuId
	 * @param sendQty
	 * @return
	 * @throws Exception
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年8月24日 下午5:28:30<br/>
	 * @author 王通<br/>
	 */
//	public void repSendSku(Set<String> skuIdSet, int sendQty, String involveBill) throws Exception;

	/**
	 * @param skuNoList
	 * @return
	 * @throws Exception
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年8月25日 上午11:48:06<br/>
	 * @author 王通<br/>
	 */
	public List<InvSkuStockVO> stockSkuCount(List<String> skuNoList) throws Exception;


	/**
	 * @param stockVo
	 * @return
	 * @throws Exception
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年8月25日 下午6:09:30<br/>
	 * @author 王通<br/>
	 */
	public Page<InvStockVO> listByPageNoLogin(InvStockVO stockVo) throws Exception;

	/**
	 * @param deliveryId
	 * @throws Exception
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年8月31日 下午2:11:30<br/>
	 * @author 王通<br/>
	 */
	public void shiftToPick(String deliveryId) throws Exception;

	/**
	 * @param deliveryId
	 * @throws Exception
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年8月31日 下午2:43:38<br/>
	 * @author 王通<br/>
	 */
	public void outStock(String deliveryId) throws Exception;

	/**
	 * @param paramVo
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年9月7日 上午10:46:15<br/>
	 * @author 王通<br/>
	 */
	public Integer countWarnByExample(InvWarnVO paramVo);
	
	public List<InvWarnVO> countWarnByExample2(InvWarnVO paramVo);

	/**
	 * @param deliveryId
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年9月8日 上午9:37:11<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
	public void destory(String deliveryId) throws Exception;

	/**
	 * 下载excel
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年10月23日 上午10:06:04<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
	public ResponseEntity<byte[]> downloadExcel(InvStockVO vo) throws Exception;

	Page<InStockVO> listInStock(InStockVO inStockVO) throws Exception;

	ResponseEntity<byte[]> downloadInStockExcel(InStockVO vo) throws Exception;

	public TotalVo totalInStock(InStockVO inStockVO);
	
	public ResponseEntity<byte[]> adjustmentLogExcel(Page<InvLogVO> vo) throws Exception;

	public void insertLog(InvLog invLog, Principal loginUser);

	public Map<String,Object> stockList2ERP(InvStockVO vo,Principal p) throws Exception;

	void repSkuInterface(String skuId, Double number) throws Exception;
}