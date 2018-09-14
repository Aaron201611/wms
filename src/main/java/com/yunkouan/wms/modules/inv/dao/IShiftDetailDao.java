package com.yunkouan.wms.modules.inv.dao;

import java.util.List;

import tk.mybatis.mapper.common.Mapper;
import com.yunkouan.wms.modules.inv.entity.InvShiftDetail;
import com.yunkouan.wms.modules.inv.vo.InvShiftVO;

/**
 * 移位明细数据层接口
 */
public interface IShiftDetailDao  extends Mapper<InvShiftDetail> {

	/**
	 * @param shiftId
	 * @return
	 * @Description 
	 * @version 2017年2月16日 下午2:26:56<br/>
	 * @author 王通<br/>
	 */
	List<InvShiftDetail> list(String shiftId);

	/**
	 * @param listShiftDetail
	 * @Description 
	 * @version 2017年2月17日 下午1:23:14<br/>
	 * @author 王通<br/>
	 */
	void add(List<InvShiftDetail> listShiftDetail);

	/**
	 * 
	 * @Description 
	 * @version 2017年2月20日 下午3:17:49<br/>
	 * @author 王通<br/>
	 * @param shiftId 
	 */
	void emptyDetail(String shiftId);

	/**
	 * @param vo
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年5月15日 下午4:29:08<br/>
	 * @author 王通<br/>
	 */
	List<InvShiftDetail> getInLocationOccupy(InvShiftVO vo);

}