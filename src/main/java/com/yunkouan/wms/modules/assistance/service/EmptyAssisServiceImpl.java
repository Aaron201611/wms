package com.yunkouan.wms.modules.assistance.service;

import java.io.IOException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.yunkouan.wms.modules.assistance.vo.MessageData;
import com.yunkouan.wms.modules.rec.vo.RecPutawayVO;
import com.yunkouan.wms.modules.send.vo.SendDeliveryVo;
import com.yunkouan.wms.modules.send.vo.SendPickVo;

@Service(value="emptyAssisService")
public class EmptyAssisServiceImpl implements IAssisService {
	private static Log log = LogFactory.getLog(EmptyAssisServiceImpl.class);

	public void request(String orgid, String billType, String billId, boolean isOut, MessageData data)
			throws IOException {
		
	}

	public void request(String orgid, String billType, String billId, boolean isOut, List<MessageData> list)
			throws IOException {
		
	}

	public void request(RecPutawayVO vo) throws IOException {
		
	}

	public void request(SendDeliveryVo vo) throws IOException {
		
	}

	public void request(SendPickVo vo) throws IOException {
		
	}

	public void response() throws IOException {
		if(log.isDebugEnabled()) log.debug("empty assis service");
	}
}