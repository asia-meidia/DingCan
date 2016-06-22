package com.healthybear.dingcan.consumer.bean;

import cn.bmob.v3.BmobUser;

public class User extends BmobUser{
	private String installId;

	public String getInstallId() {
		return installId;
	}

	public void setInstallId(String installId) {
		this.installId = installId;
	}

	@Override
	public String toString() {
		return "User [installId=" + installId + "]";
	}
	
}
