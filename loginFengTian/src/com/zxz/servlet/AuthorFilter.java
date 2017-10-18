package com.zxz.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.zxz.domain.OperateUser;

/**验证
 * @author Administrator
 *
 */
public class AuthorFilter implements Filter{

	
	public void destroy() {
		
	}

	public void doFilter(ServletRequest servletrequest,
			ServletResponse servletresponse, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)servletrequest;
		HttpServletResponse response = (HttpServletResponse)servletresponse;
		OperateUser user = (OperateUser) request.getSession().getAttribute("user");
		String url = request.getRequestURL().toString();
		if(url.endsWith(".css")||url.endsWith(".js")){
			chain.doFilter(request, response);
			return;
		}
		if(user!=null){
			String method = request.getParameter("method");
			request.getSession().setAttribute("method", method);
			chain.doFilter(servletrequest, servletresponse);
			return;
		}else if(url.endsWith("regist.jsp")){
			//将来这里可以改写出一个白名单
			chain.doFilter(servletrequest, servletresponse);
			return;
		}else{
			if(url.contains("UserServlet")){
				String method = request.getParameter("method");
				if(method.equals("login")||//登录
				   method.equals("regist")||//注册
				   method.equals("toLogin")||//跳转到注册 
				   method.equals("toFindPwd")||//跳转到找回密码
				   method.equals("getVerifyCode")||//得到验证码
				   method.equals("getDynamic")||//得到短信验证码
				   method.equals("findPwd")//找回密码
				   ){
				   chain.doFilter(servletrequest, servletresponse);
				}else{
				   request.getRequestDispatcher("/user/login.jsp").forward(request, response);
				}
			}else if(url.contains("ReceiveMessageServlet")||url.contains("Image")||url.contains("AppInFoServlet")){//接收信息或者验证码
				chain.doFilter(request, response);
			}else{
				request.getRequestDispatcher("/user/login.jsp").forward(request, response);
			}
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		
	}
	
}
