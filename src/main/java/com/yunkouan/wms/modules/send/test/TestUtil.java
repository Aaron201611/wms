package com.yunkouan.wms.modules.send.test;

import java.util.Calendar;

public class TestUtil {
	
	public static void print(int i) throws Exception{
		int x  = 0;
		x = x + i;
		if(i % 2 == 0){
			System.out.println("print==========i>>>"+i+"   time====start>>"+Calendar.getInstance().getTimeInMillis()+"  x==="+x);
			Thread.sleep(500000);
			System.out.println("print==========i>>>"+i+"   time=====end>>"+Calendar.getInstance().getTimeInMillis()+"  x==="+x);
		}else{
			System.out.println("print==========i>>>"+i+"   time======="+Calendar.getInstance().getTimeInMillis()+"  x==="+x);
		}
	}
	
	public void print2(int i) throws Exception{
		int x  = 0;
		x = x + i;
		if(i % 2 == 0){
			System.out.println("print==========i>>>"+i+"   time====start>>"+Calendar.getInstance().getTimeInMillis()+"  x==="+x);
			Thread.sleep(500000);
			System.out.println("print==========i>>>"+i+"   time=====end>>"+Calendar.getInstance().getTimeInMillis()+"  x==="+x);
		}else{
			System.out.println("print==========i>>>"+i+"   time======="+Calendar.getInstance().getTimeInMillis()+"  x==="+x);
		}
	}
	
	public static void main(String[] args) throws Exception{
		for(int i = 0;i<5;i++){
			final int t = i;
			final TestUtil util = new TestUtil();
			new Thread(new Runnable() {
				public void run() {
					try {
						TestUtil.print(t);
//						util.print2(t);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
	}
}
