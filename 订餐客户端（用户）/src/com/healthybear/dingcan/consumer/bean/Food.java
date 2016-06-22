package com.healthybear.dingcan.consumer.bean;

import cn.bmob.v3.BmobObject;

public class Food extends BmobObject{
	private String foodname;		//��Ʒ����
	private String foodpic;			//��ƷͼƬURL
	private Integer foodprice;		//��Ʒ�۸�
	private String fooddetailed;	//��Ʒ����
	private int shopNumber;			//��Ʒ����
	private boolean isChoosed;		//��Ʒ�Ƿ��ڹ��ﳵ�б�ѡ��
	
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
