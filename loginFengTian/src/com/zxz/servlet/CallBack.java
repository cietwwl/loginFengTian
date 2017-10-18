package com.zxz.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface CallBack {

	/**
	 * 设置request,response
	 * @throws IOException 
	 * @throws ServletException 
	 */
	void receiveMessage(HttpServletRequest request,HttpServletResponse response) throws Exception;
	
}
