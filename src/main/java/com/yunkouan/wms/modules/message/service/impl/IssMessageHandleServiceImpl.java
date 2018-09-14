package com.yunkouan.wms.modules.message.service.impl;

import java.io.File;
import java.io.IOException;

import org.springframework.stereotype.Service;

import com.yunkouan.util.SpringContextHolder;
import com.yunkouan.wms.modules.message.service.IMessageHandleService;
import com.yunkouan.wms.modules.message.service.IMessageService;
import com.yunkouan.wms.modules.message.util.IssFileUtil;
import com.yunkouan.wms.modules.send.util.ThreadPoolUtils;

@Service
public class IssMessageHandleServiceImpl implements IMessageHandleService{

	@Override
	public void receiveAndHandle(IMessageService service) throws Exception {
		IssFileUtil issFileUtil = SpringContextHolder.getBean("issFileUtil");
		File[] files = issFileUtil.getFileList();
		for(File file:files){
			
			Thread thread = new Thread(new Runnable(){
				public void run(){
					try {
						String xmlStr = issFileUtil.read(file);
						service.handleData(xmlStr);
						issFileUtil.deleteFile(file);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
			ThreadPoolUtils.getInstance().addThreadItem(thread);
			
		}
		
	}

}
