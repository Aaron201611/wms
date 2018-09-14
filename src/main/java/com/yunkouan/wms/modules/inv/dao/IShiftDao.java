package com.yunkouan.wms.modules.inv.dao;

import java.util.List;

import com.yunkouan.wms.modules.inv.entity.InvShift;
import com.yunkouan.wms.modules.inv.vo.InvShiftVO;

import tk.mybatis.mapper.common.Mapper;

/**
 * 移位数据层接口
 */
public interface IShiftDao  extends Mapper<InvShift> {

	/**
	 * @param param
	 * @return
	 * @Description 
	 * @version 2017年2月16日 上午10:40:46<br/>
	 * @author 王通<br/>
	 */
	List<InvShift> list(InvShiftVO vo);

	/**
	 * @param id
	 * @return
	 * @Description 
	 * @version 2017年2月16日 下午1:53:00<br/>
	 * @author 王通<br/>
	 */
	InvShift find(String id);

	/**
	 * @param shift
	 * @Description 
	 * @version 2017年2月17日 下午1:13:35<br/>
	 * @author 王通<br/>
	 */
	void add(InvShift shift);

	/**
	 * @param invShiftVo
	 * @Description 
	 * @version 2017年2月20日 下午2:33:31<br/>
	 * @author 王通<br/>
	 */
	void update(InvShiftVO invShiftVo);

	/**
	 * @param shift
	 * @Description 
	 * @version 2017年2月22日 下午3:27:03<br/>
	 * @author 王通<br/>
	 */
	void enable(InvShift shift);

	/**
	 * @param vo
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月20日 下午4:19:46<br/>
	 * @author 王通<br/>
	 */
	List<InvShiftVO> getOutLockDetail(InvShiftVO vo);
	
	List<String>getTask(String orgId);
}