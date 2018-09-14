package com.yunkouan.wms.modules.inv.dao;

import java.util.List;

import tk.mybatis.mapper.common.Mapper;
import com.yunkouan.wms.modules.inv.entity.InvCountDetail;
import com.yunkouan.wms.modules.inv.vo.InvCountVO;

/**
 * 盘点明细数据层接口
 */
public interface ICountDetailDao  extends Mapper<InvCountDetail> {

	/**
	 * @param listCountDetail
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月14日 下午3:06:41<br/>
	 * @author 王通<br/>
	 */
	void add(List<InvCountDetail> listCountDetail);

	/**
	 * @param countVo
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月31日 上午10:32:51<br/>
	 * @author 王通<br/>
	 */
	Integer selectLocationCount(InvCountVO countVo);

	/**
	 * @param id
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年5月12日 下午5:26:50<br/>
	 * @author 王通<br/>
	 */
	List<InvCountDetail> selectCountDetail(String id);


}