package com.yunkouan.wms.modules.redis;

import java.util.HashSet;
import java.util.Set;
import org.junit.Test;
import com.yunkouan.util.SpringContextHolder;
import com.yunkouan.wms.common.BaseJunitTest;
import com.yunkouan.wms.common.strategy.impl.NoImpl4Mysql;

public class RedisTest extends BaseJunitTest {
	@Test
	public void test() throws InterruptedException {
		Set<String> set = new HashSet<String>();
		for(int i=0; i<10; ++i) {
	        Thread t = new Thread() {
	            public void run() {
	            	try {
		            	NoImpl4Mysql service = SpringContextHolder.getBean("noRule");
		        		String no = service.getSkuPrefixNo("1");
		        		if(set.contains(no)) {
		        			System.err.print(no);
		        		} else {
		        			System.out.print(no);
		        		}
		        		set.add(no);
	            	} catch(Throwable e) {
	            		e.printStackTrace();
	            	}
	            };
	        };
	        t.start();
		}
		Thread.sleep(60*60*1000);
	}
}