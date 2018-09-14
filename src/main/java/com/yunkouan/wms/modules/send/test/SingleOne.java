package com.yunkouan.wms.modules.send.test;


public class SingleOne {

	private String s1;
	
	private String s2;
	
	public SingleOne(String s1,String s2){
		this.s1 = s1;
		this.s2 = s2;
	}
	
	public void test(String s){
		s = "good";
		System.out.println(s);
	}
	
	public static  void test2(int t1,int t2){
		int tmp = t1;
		t1 = t2;
		t2 = tmp;
		System.out.println(t1+"====="+t2);
	}
	
	public static void main(String[] args) {
		int a=1;
		int b=2;
		test2(a,b);
		System.out.println(a +"-----"+ b);
	}
	
}
