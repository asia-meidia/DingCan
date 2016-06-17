package com.example.demo.bean;

public class Bread {
	private String name;
	private int price;
	private String pic;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	@Override
	public String toString() {
		return "Bread [name=" + name + ", price=" + price + ", pic=" + pic + "]";
	}
	
}
