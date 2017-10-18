package com.zxz.config.utils;

import java.util.ResourceBundle;

public class PropertiesUtil {

	public static void main(String[] args) {
		try {
			ResourceBundle resource = ResourceBundle.getBundle("config/redisconfig");// testΪ�����ļ�����ڰ�com.mmq�£�����Ƿ���src�£�ֱ����test����
//			ResourceBundle resource = ResourceBundle.getBundle("com/zxz/config/utils/config");// testΪ�����ļ�����ڰ�com.mmq�£�����Ƿ���src�£�ֱ����test����
			String redispwd = resource.getString("redispwd");
			System.out.println(redispwd);
			String host = resource.getString("host");
			System.out.println(host);
			String port = resource.getString("port");
			System.out.println(port);
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	
	
	

}
