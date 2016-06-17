package com.example.demo.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class Food extends BmobObject{
	private String foodname;
	private BmobFile foodpic;
	private Integer foodprice;
	private String fooddetailed;
	public String getFoodname() {
		return foodname;
	}
	public void setFoodname(String foodname) {
		this.foodname = foodname;
	}
	public BmobFile getFoodpic() {
		return foodpic;
	}
	public void setFoodpic(BmobFile foodpic) {
		this.foodpic = foodpic;
	}
	public Integer getFoodprice() {
		return foodprice;
	}
	public void setFoodprice(Integer foodprice) {
		this.foodprice = foodprice;
	}
	public String getFooddetailed() {
		return fooddetailed;
	}
	public void setFooddetailed(String fooddetailed) {
		this.fooddetailed = fooddetailed;
	}
	
	
	
	
}
