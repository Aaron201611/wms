package com.yunkouan.wms.modules.inv.service;

import java.util.List;

import com.github.pagehelper.Page;
import com.yunkouan.wms.modules.inv.entity.InvShiftDetail;
import com.yunkouan.wms.modules.inv.vo.InvOutLockDetailVO;
import com.yunkouan.wms.modules.inv.vo.InvShiftVO;

/**
 * 移位业务接口
 */
public interface IShiftService {


    /**
     * 查看移位单列表
     * @param shiftVO 
     * @param page 
     * @return
     */
    public Page<InvShiftVO> list(InvShiftVO vo) throws Exception ;

    /**
     * @param id 
     * @return
     * @throws Exception 
     */
    public InvShiftVO view(String id) throws Exception;

    /**
     * @param shift 
     * @return
     * @throws Exception 
     */
    public InvShiftVO saveOrUpdate(InvShiftVO vo) throws Exception;

    /**
     * 生效移位单
     * @param vo
     * @throws Exception
     * @required 
     * @optional  
     * @Description 
	 * 1、移出库位预分配
	 * 2、库存预占用
	 * 3、更新移位单状态
     * @version 2017年3月7日 下午4:34:40<br/>
     * @author 王通<br/>
     */
    public void enable(InvShiftVO vo) throws Exception;

	/**
	 * 确认移位
	 * @param vo
	 * @return
	 * @required shiftId,shiftDetailId,shiftDetail.shiftDetail.shiftQty,shiftDetail.realShiftQty
	 * @optional  
	 * @Description 
     * 1、解除锁定-预分配   
     * 2、解除锁定-预占用    
     * 3、入库-库存增加			
     * 4、出库-库存减少
	 * @version 2017年3月7日 下午4:46:25<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
    public void confirm(InvShiftVO shiftVo) throws Exception;

	/**
	 * 失效移位计划
	 * @param vo
	 * @required 
	 * @optional  
	 * @Description 
	 * 1、解锁移出库位预分配
	 * 2、解锁库存预占用
	 * 3、更新移位单状态
	 * @version 2017年3月7日 下午4:33:54<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
	public void disable(InvShiftVO vo) throws Exception;

	/**
	 * 打印移位单
	 * @param id
	 * @required 
	 * @optional  
	 * @Description 修改移位单状态为“作业中”
	 * @version 2017年3月7日 下午5:29:12<br/>
	 * @author 王通<br/>
	 */
	public void print(String id) throws Exception;

	/**
	 * 不做任何校验，直接保存入库，参数自填
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月8日 下午5:24:24<br/>
	 * @author 王通<br/>
	 */
	public void quickSave(InvShiftVO shiftVo) throws Exception;

	/**
	 * 检查移位单预分配详情
	 * @param locationId
	 * @param skuId
	 * @param batchNo
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月20日 下午3:24:01<br/>
	 * @author 王通<br/>
	 */
	public List<InvOutLockDetailVO> getOutLockDetail(String locationId, String skuId, String batchNo) throws Exception;

    /**
     * @param shift 
     * @return
     * @throws Exception 
     */
    public InvShiftVO saveAndEnable(InvShiftVO vo) throws Exception;

	/**
	 * @param vo
	 * @return
	 * @throws Exception
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年5月15日 上午11:10:11<br/>
	 * @author 王通<br/>
	 */
	Integer countByExample(InvShiftVO vo) throws Exception;
	
	Integer countByExample2(InvShiftVO vo) throws Exception;
	
	/**
	 * 内部服务，查询预占用库容
	 * @param locationId
	 * @param status
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年5月15日 下午4:10:59<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
	List<InvShiftDetail> getInLocationOccupy(String locationId, Integer... status) throws Exception;

	/**
	 * 快速作业确认，用于手持终端
	 * @param shiftVo
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年7月20日 下午4:18:02<br/>
	 * @author 王通<br/>
	 */
	public void quickConfirm(InvShiftVO shiftVo) throws Exception;

	/**
	 * @param vo
	 * @throws Exception
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年9月2日 下午4:01:57<br/>
	 * @author 王通<br/>
	 */
	public void cancel(InvShiftVO vo) throws Exception;

	/**
	 * @param vo
	 * @return
	 * @throws Exception
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年9月11日 上午10:30:40<br/>
	 * @author 王通<br/>
	 */
	Page<InvShiftVO> list4Print(InvShiftVO vo) throws Exception;
	List<String>getTask(String orgId);

	public InvShiftVO importShift(InvShiftVO paramVo) throws Exception;

	public void confirmBatch(InvShiftVO shiftVo) throws Exception;
}