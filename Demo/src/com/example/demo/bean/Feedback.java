package com.example.demo.bean;

import cn.bmob.v3.BmobObject;

public class Feedback extends BmobObject{
	//��������
    private String content;
    //��ϵ��ʽ
    private String contacts;
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getContacts() {
		return contacts;
	}
	public void setContacts(String contacts) {
		this.contacts = contacts;
	}
	@Override
	public String toString() {
		return "Feedback [content=" + content + ", contacts=" + contacts + "]";
	}
    
}
