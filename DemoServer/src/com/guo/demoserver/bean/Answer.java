package com.guo.demoserver.bean;

import cn.bmob.v3.BmobObject;

public class Answer extends BmobObject{
	private String answername;
	private String content;
	public String getAnswername() {
		return answername;
	}
	public void setAnswername(String answername) {
		this.answername = answername;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	@Override
	public String toString() {
		return "Answer [answername=" + answername + ", content=" + content + "]";
	}
	 
}
