package com.example.demo.bean;

import android.view.View.OnClickListener;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.listener.SaveListener;

public class Order extends BmobObject{
	private String name;
	private String price;
	private String phone;
	private String address;
	private String instal;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getInstal() {
		return instal;
	}
	public void setInstal(String instal) {
		this.instal = instal;
	}
	@Override
	public String toString() {
		return "Order [name=" + name + ", price=" + price + ", phone=" + phone + ", address=" + address + ", instal="
				+ instal + "]";
	}
	
}
