package com.yunkouan.wms.modules.inv.service;

import java.util.List;

import com.github.pagehelper.Page;
import com.yunkouan.wms.modules.inv.vo.InvAdjustVO;

/**
 * 盈亏调整业务接口
 */
public interface IAdjustService {


    /**
     * 获取调账单详情
     * @param id
     * @return
     * @required 
     * @optional  
     * @Description 
     * @version 2017年3月11日 下午1:14:31<br/>
     * @author 王通<br/>
     * @throws Exception 
     */
    public InvAdjustVO view(String id) throws Exception;

    /**
     * 调账单新增
     * @param adjust
     * @required 
     * @optional  
     * @Description 
     * @version 2017年3月11日 下午1:14:48<br/>
     * @author 王通<br/>
     * @return 
     * @throws Exception 
     */
    public InvAdjustVO add(InvAdjustVO adjust) throws Exception;

    /**
     * 调账单保存并生效
     * @param adjust
     * @required 
     * @optional  
     * @Description 
     * @version 2017年3月11日 下午1:14:48<br/>
     * @author 王通<br/>
     * @return 
     * @throws Exception 
     */
    public InvAdjustVO saveAndEnable(InvAdjustVO adjust) throws Exception;
    /**
     * 调账单修改
     * @param adjust
     * @required 
     * @optional  
     * @Description 
     * @version 2017年3月11日 下午1:14:48<br/>
     * @author 王通<br/>
     * @return 
     * @throws Exception 
     */
    public InvAdjustVO update(InvAdjustVO adjust) throws Exception;

    /**
     * 调账单生效
     * @param adjustIdList
     * @return
     * @required 
     * @optional  
     * @Description 
     * 1、获取当前库存
     * 2、判断调整类型
     * @version 2017年3月11日 下午1:15:04<br/>
     * @author 王通<br/>
     * @throws Exception 
     */
    public void enable(List<String> adjustIdList) throws Exception;

    /**
     * 调账单取消
     * @param adjustIdList
     * @return
     * @required 
     * @optional  
     * @Description 
     * @version 2017年3月11日 下午1:15:20<br/>
     * @author 王通<br/>
     * @throws Exception 
     */
    public void cancel(List<String> adjustIdList) throws Exception;

	/**
     * 调账单分页列表
	 * @param adjVO
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月11日 下午2:13:16<br/>
	 * @author 王通<br/>
	 */
	public Page<InvAdjustVO> listByPage(InvAdjustVO adjVO);

	/**
	 * @param vo
	 * @return
	 * @throws Exception
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年5月15日 上午11:18:44<br/>
	 * @author 王通<br/>
	 */
	Integer countByExample(InvAdjustVO vo) throws Exception;
	Integer countByExample2(InvAdjustVO vo) throws Exception;

	public List<String>getTask(String orgId);
}