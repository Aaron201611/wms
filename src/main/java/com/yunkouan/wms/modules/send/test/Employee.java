package com.yunkouan.wms.modules.send.test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Constructor;

public class Employee implements Cloneable,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;
	
	private String department;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		System.out.println("set name");
		this.name = name;
	}
	
	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		System.out.println("setDepartment");
		this.department = department;
	}
	

	public Employee(){
		System.out.println("Construct......default");
	}
	
	public Employee(String name,String department){
		this.name = name;
		this.department = department;
		System.out.println("Construct......has name");
	}
	
	public static void main(String[] args) throws Exception{
		//1、new
		Employee employee = new Employee();
		//2、使用Class类的newInstance方法
		Employee employee2 = (Employee)Class.forName("com.yunkouan.wms.modules.send.test.Employee").newInstance();		
		//Employee employee3 = Employee.class.newInstance();
		//3、使用Constructor类的newInstance方法
		Constructor<Employee> constructor =  Employee.class.getConstructor(String.class,String.class);
		Employee employee3 =  constructor.newInstance("123","abc");
		//4、使用Clone的方法
		Employee employee4 = (Employee)employee3.clone();
		System.out.println(employee3 == employee4);
		//5、使用反序列化
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("data.obj"));
		out.writeObject(employee3);
		out.close();
		
		ObjectInputStream in = new ObjectInputStream(new FileInputStream("data.obj"));
		Employee employee5 = (Employee)in.readObject();
		System.out.println(employee3 == employee5);
		
		
	}
	
	
}
