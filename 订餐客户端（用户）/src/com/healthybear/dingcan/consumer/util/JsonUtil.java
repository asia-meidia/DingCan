package com.healthybear.dingcan.consumer.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.healthybear.dingcan.consumer.bean.Food;


public class JsonUtil {

	public static List<Food> getFoodList(String json) {
		try {
			List<Food> list = new ArrayList<Food>();
			JSONObject obj = new JSONObject(json);
			JSONArray array = obj.getJSONArray("data");
			for(int i=0;i<array.length();i++){
				JSONObject jo = array.getJSONObject(i);
				Food food = new Food();
				food.setFoodname(jo.getString("foodName"));
				food.setFoodprice(jo.getInt("foodprice"));
				list.add(food);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("json×Ö·û´®¸ñÊ½´íÎó");
		}
	}

}
