package com.healthybear.dingcan.consumer.bean;

import cn.bmob.v3.BmobObject;

public class Food extends BmobObject{
	private String foodname;		//菜品名字
	private String foodpic;			//菜品图片URL
	private Integer foodprice;		//菜品价格
	private String fooddetailed;	//菜品描述
	private int shopNumber;			//菜品数量
	private boolean isChoosed;		//菜品是否在购物车中被选中
	
	public String getFoodname() {
		return foodname;
	}
	public void setFoodname(String foodname) {
		this.foodname = foodname;
	}
	public String getFoodpic() {
		return foodpic;
	}
	public void setFoodpic(String foodpic) {
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
	public int getShopNumber() {
		return shopNumber;
	}
	public void setShopNumber(int shopNumber) {
		this.shopNumber = shopNumber;
	}
	public boolean isChoosed() {
		return isChoosed;
	}
	public void setChoosed(boolean isChoosed) {
		this.isChoosed = isChoosed;
	}
	
	
	@Override
	public String toString() {
		return "Food [foodname=" + foodname + ", foodpic=" + foodpic
				+ ", foodprice=" + foodprice + ", fooddetailed=" + fooddetailed
				+ ", shopNumber=" + shopNumber + ", isChoosed=" + isChoosed
				+ "]";
	}

}
