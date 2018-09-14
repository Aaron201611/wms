package com.yunkouan.wms.modules.inv.service;

import java.util.List;

import com.github.pagehelper.Page;
import com.yunkouan.wms.modules.inv.entity.InvRejectsDestoryDetail;
import com.yunkouan.wms.modules.inv.vo.InvRejectsDestoryVO;

/**
 * 移位业务接口
 */
public interface IRejectsDestoryService {


    /**
     * 查看移位单列表
     * @param shiftVO 
     * @param page 
     * @return
     */
    public Page<InvRejectsDestoryVO> list(InvRejectsDestoryVO vo) throws Exception ;

    /**
     * @param id 
     * @return
     * @throws Exception 
     */
    public InvRejectsDestoryVO view(String id) throws Exception;

    /**
     * @param shift 
     * @return
     * @throws Exception 
     */
    public InvRejectsDestoryVO saveOrUpdate(InvRejectsDestoryVO vo) throws Exception;

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
    public void enable(InvRejectsDestoryVO vo) throws Exception;

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
    public void confirm(InvRejectsDestoryVO shiftVo) throws Exception;

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
	public void disable(InvRejectsDestoryVO vo) throws Exception;

	/**
	 * 打印移位单
	 * @param id
	 * @required 
	 * @optional  
	 * @Description 修改移位单状态为“作业中”
	 * @version 2017年3月7日 下午5:29:12<br/>
	 * @author 王通<br/>
	 * @return 
	 */
	public InvRejectsDestoryVO print(String id) throws Exception;

    /**
     * @param shift 
     * @return
     * @throws Exception 
     */
    public InvRejectsDestoryVO saveAndEnable(InvRejectsDestoryVO vo) throws Exception;

	/**
	 * @param vo
	 * @throws Exception
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年9月2日 下午4:01:57<br/>
	 * @author 王通<br/>
	 */
	public void cancel(InvRejectsDestoryVO vo) throws Exception;

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
	Page<InvRejectsDestoryVO> list4Print(InvRejectsDestoryVO vo) throws Exception;

}