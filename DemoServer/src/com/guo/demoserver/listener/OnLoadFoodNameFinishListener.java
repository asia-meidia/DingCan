package com.guo.demoserver.listener;

import java.util.List;

import com.guo.demoserver.bean.Food;
import com.guo.demoserver.bean.Message;

public interface OnLoadFoodNameFinishListener {
	void OnLoadFoodNameFinish(List<Food> list);
}
