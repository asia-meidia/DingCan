package com.example.demo.bean;

public class BmobFile {
	private String filename;
	private String group;
	private String url;

	/**  
	 * @param fileName �ļ���(����)
	 * @param group ������ѡ�
	 * @param url  ����url��ַ�����
	 * ע������Ҫ���ļ������ļ�������url��ַ��group��Ϊ��
	 */
	public BmobFile(String fileName,String group,String url){
	    this.filename = fileName;
	    this.group=group;
	    this.url = url;
	}
}
