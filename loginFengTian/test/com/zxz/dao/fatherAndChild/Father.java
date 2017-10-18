package com.zxz.dao.fatherAndChild;

public abstract class Father {

	String name;
	
	public Father() {
	}
	
	public void showName(){
		System.out.println("名字:"+name);
	}
	
	public void setName(String name){
		this.name = name;
	}
}
