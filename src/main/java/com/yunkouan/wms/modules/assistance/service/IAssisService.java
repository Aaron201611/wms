package com.yunkouan.wms.modules.assistance.service;

import java.io.IOException;
import java.util.List;

import com.yunkouan.wms.modules.assistance.vo.MessageData;
import com.yunkouan.wms.modules.rec.vo.RecPutawayVO;
import com.yunkouan.wms.modules.send.vo.SendDeliveryVo;
import com.yunkouan.wms.modules.send.vo.SendPickVo;

/** 
* @Description: 海关辅助系统接口
* @author tphe06
* @date 2018年7月25日 下午3:52:57  
*/
public interface IAssisService {
	/**上架单**/
	public static final String BILL_TYPE_PUTAWAY = "0";
	/**拣货单**/
	public static final String BILL_TYPE_PICKUP = "1";
	/**移位单**/
	public static final String BILL_TYPE_SHIFT = "2";
	/**盈亏调整单**/
	public static final String BILL_TYPE_ADJUST = "3";
	/** 
	* @Title: request 
	* @Description: 通用出入库请求接口（单条）
	* @auth tphe06
	* @time 2018 2018年8月10日 上午10:16:16
	* @param orgid
	* @param billType
	* @param billId
	* @param isOut
	* @param data
	* @throws IOException
	* void
	*/
	public void request(String orgid, String billType, String billId, boolean isOut, MessageData data) throws IOException;
	/** 
	* @Title: send 
	* @Description: 通用出入库请求接口（批量）
	* 1.封装XML报文
	* 2.写入文件
	* @auth tphe06
	* @time 2018 2018年7月26日 上午9:56:28
	* @param list
	* void
	 * @throws IOException 
	*/
	public void request(String orgid, String billType, String billId, boolean isOut, List<MessageData> list) throws IOException;
	/** 
	* @Title: request 
	* @Description: 发送入库单（收货单+上架单）
	* 对应IASNService.findAndSendInStockData
	* @auth tphe06
	* @time 2018 2018年7月26日 上午10:50:56
	* @param vo
	* @throws IOException
	* void
	*/
	public void request(RecPutawayVO vo) throws IOException;
	/** 
	* @Title: send 
	* @Description: 发送出库单（发货单+拣货单）
	* @auth tphe06
	* @time 2018 2018年7月25日 下午3:53:27
	* @param vo
	* void
	 * @throws IOException 
	*/
	@Deprecated
	public void request(SendDeliveryVo vo) throws IOException;
	/** 
	* @Title: request 
	* @Description: 发送出库单（发货单+拣货单）
	* 对应IDeliveryService.findAndSendOutStockData方法
	* @auth tphe06
	* @time 2018 2018年7月26日 上午11:01:16
	* @param vo
	* @throws IOException
	* void
	*/
	public void request(SendPickVo vo) throws IOException;
	/** 
	* @throws IOException 
	 * @Title: response 
	* @Description: 出入库回执接口
	* 1.文件读取
	* 2.解析XML报文
	* 3.更新处理结果
	* @auth tphe06
	* @time 2018 2018年7月25日 下午3:53:51
	* void
	*/
	public void response() throws IOException;
}