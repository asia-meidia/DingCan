package com.guo.demoserver.bean;

import cn.bmob.v3.BmobObject;

public class Food extends BmobObject{
	private String name;
	private int count;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	@Override
	public String toString() {
		return "Food [name=" + name + ", count=" + count + "]";
	}
	
}
