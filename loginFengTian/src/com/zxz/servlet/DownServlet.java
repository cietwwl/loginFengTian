package com.zxz.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

public class DownServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//按照学生id查询指定学生信息
		//使用IO流完成文件复制
		//2.1创建输入流和输出流
		String realPath = this.getServletContext().getRealPath("/download");
		File file = new File(realPath,"ZZMJ.plist");
		InputStream is = new FileInputStream(file);//photoName
		OutputStream os = response.getOutputStream();
		//解决表单get请求的中文乱码问题 http协议使用iso-8859-1协议传输数据
//		String name = request.getParameter("name");
//		name = new String(name.getBytes("iso-8859-1"),"utf-8");
		//解决服务器端文件名到客户端的乱码问题
//		photoName = new String(photoName.getBytes("utf-8"),"iso-8859-1");
		//为response添加三个头信息,用来实现弹框下载
		response.setHeader("Content-Disposition","attachment;filename=ZZMJ.plist");//附件
		response.setContentLength((int)file.length());
		//2.2使用输入流和输出流完成文件复制(服务器上文件复制到客户端)
		IOUtils.copy(is,os);
		//2.3关闭输入流和输出流
		is.close();
		os.close();
	}
}
