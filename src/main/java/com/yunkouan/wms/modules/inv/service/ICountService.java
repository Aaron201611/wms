package com.yunkouan.wms.modules.inv.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.github.pagehelper.Page;
import com.yunkouan.wms.modules.inv.vo.InvCountVO;

/**
 * 盘点业务接口
 */
public interface ICountService {

	/**
	 * 增加盘点单
	 * @param vo
	 * @return
	 * @required countType,isBlockLocation
	 * @optional count_status
	 * @Description 
	 * @version 2017年3月14日 下午2:49:22<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
	InvCountVO add(InvCountVO vo) throws Exception;

	/**
	 * 分页获取盘点单列表
	 * @param countVO
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月14日 下午3:14:45<br/>
	 * @author 王通<br/>
	 */
	Page<InvCountVO> list(InvCountVO countVO) throws Exception;

	/**
	 * 获取盘点单详情
	 * @param id
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月14日 下午3:15:32<br/>
	 * @author 王通<br/>
	 */
	InvCountVO view(String id) throws Exception;

	/**
	 * 更新盘点单
	 * @param vo
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月14日 下午3:15:45<br/>
	 * @author 王通<br/>
	 */
	InvCountVO update(InvCountVO vo) throws Exception;

	/**
	 * 生效盘点单
	 * @param vo
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月14日 下午3:15:56<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
	void enable(InvCountVO vo) throws Exception;

	/**
	 * 失效盘点单
	 * @param vo
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月14日 下午3:16:09<br/>
	 * @author 王通<br/>
	 */
	void disable(InvCountVO vo) throws Exception;

	/**
	 * 盘点确认，可逐条确认也可批量确认
	 * @param countVo
	 * @required count.countId,countDetail.stockQty,countDetail.realCountQty
	 * @optional countDetail.countDetailId,
	 * @Description 
	 * @version 2017年3月14日 下午3:16:18<br/>
	 * @author 王通<br/>
	 */
	void confirm(InvCountVO countVo) throws Exception;

	/**
	 * 盘点单打印，盘点单状态改为作业中
	 * @param id
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月14日 下午3:17:02<br/>
	 * @author 王通<br/>
	 */
	void print(String id) throws Exception;

	/**
	 * @param countIdList
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月14日 下午4:30:30<br/>
	 * @author 王通<br/>
	 */
	void cancel(List<String> countIdList) throws Exception;

	/**
	 * 保存生效盘点单
	 * @param vo
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月14日 下午3:15:45<br/>
	 * @author 王通<br/>
	 */
	InvCountVO saveAndEnable(InvCountVO vo) throws Exception;

	/**
	 * @param vo
	 * @throws Exception
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年5月15日 上午10:53:12<br/>
	 * @author 王通<br/>
	 */
	Integer countByExample(InvCountVO vo) throws Exception;
	/**
	 * @param vo
	 * @throws Exception
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年5月15日 上午10:53:12<br/>
	 * @author zwb<br/>
	 */
	Integer countByExample2(InvCountVO vo) throws Exception;
	
	public List<String>getTask(String orgId);

	/**
	 * 快速作业确认，用于手持终端
	 * @param countVo
	 * @required 
	 * @optional  
	 * @Description 
	 * 1、校验库存，无库存报异常
	 * 2、新增盘点单，状态为完成
	 * 3、如有变动，生成调账单
	 * @version 2017年7月20日 上午11:15:13<br/>
	 * @author 王通<br/>
	 */
	void quickConfirm(InvCountVO countVo) throws Exception;

	/**
	 * @param vo
	 * @return
	 * @throws Exception
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年9月11日 上午11:20:58<br/>
	 * @author 王通<br/>
	 */
	Page<InvCountVO> list4Print(InvCountVO vo) throws Exception;
	
	/**
	 * 传送期末库存数据对接仓储企业联网监管系统
	 * @throws Exception
	 */
	void transmitStocksXML(String id) throws Exception;

	ResponseEntity<byte[]> downloadExcel(InvCountVO vo) throws Exception;

	String getApplyNo();
	
	
}