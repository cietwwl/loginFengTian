package com.zxz.dao.fatherAndChild;

import org.json.JSONObject;

public class Child extends Father{
	
	
	public static void main(String[] args) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("appVersion", 1.0);
		jsonObject.put("force", false);
		jsonObject.put("discription", "鏂扮増鏈笂绾匡紝bug娴嬭瘯涓�");
		System.out.println(jsonObject);
	}

	private static void a() {
		Child child = new Child();
		child.setName("gushuang");
		child.showName();
	}

	
}
