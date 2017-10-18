package com.zxz.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.zxz.domain.User;
import com.zxz.service.PaoDeKuaiUserService;
import com.zxz.service.UserService;

public class PaoDeKuaiUserServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	
	private static Logger logger = Logger.getLogger(ZhenJiangUserServlet.class);  
	PaoDeKuaiUserService paoDeKuaiUserService = new PaoDeKuaiUserService();
	UserService userService = new UserService();

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String method = request.getParameter("method");
		if(method==null){
			return;
		}
		if (method.equals("login")) {
			login(request, response);// 登录
		}else if(method.equals("getUserSumScore")){
			getUserSumScore(request,response);//得到用户的战绩
		}else if(method.equals("beforeCreateRoom")){
			beforeCreateRoom(request,response);//在创建房间直接点击
		}else if(method.equals("beforeEnterRoom")){
			beforeEnterRoom(request,response);//在进入房间之前点击
		}else if(method.equals("getUserScore")){
			getUserScore(request,response);//得到
		}else if (method.equals("getVedio")) {
			getVedio(request, response);//得到录像
		}
	}

	
	private void write(HttpServletResponse response, JSONObject jsonObject){
		try {
			ServletOutputStream out = response.getOutputStream();
			out.write(jsonObject.toString().getBytes("utf-8"));
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	
	/**得到录像
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws UnsupportedEncodingException 
	 */
	private void getVedio(HttpServletRequest request,
			HttpServletResponse response)  {
		String str= request.getParameter("serviceStr");
		JSONObject serviceStr = new JSONObject(str);
		JSONObject jsonObject = userService.getVedio(serviceStr);
		write(response,jsonObject);
	}

	/**得到用户的成绩
	 * @param request
	 * @param response
	 */
	private void getUserScore(HttpServletRequest request,
			HttpServletResponse response) {
		String str= request.getParameter("serviceStr");
		JSONObject serviceStr = new JSONObject(str);
		JSONObject jsonObject = paoDeKuaiUserService.getUserScore(serviceStr);
		write(response,jsonObject);
	}

	/**在进入房间之前点击
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	private void beforeEnterRoom(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String str= request.getParameter("serviceStr");
		JSONObject serviceStr = new JSONObject(str);
		try {
			ServletOutputStream out = response.getOutputStream();
			String realPath = request.getSession().getServletContext().getRealPath("/");
			String result = paoDeKuaiUserService.beforeEnterRoom(serviceStr,realPath);
			logger.info("receive:"+str+",请求创建房间返回:"+result);
			out.write(result.getBytes("utf-8"));
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**在创建房间之前点击
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	private void beforeCreateRoom(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		ServletOutputStream out = response.getOutputStream();
		try {
			String realPath = request.getSession().getServletContext().getRealPath("/");
			paoDeKuaiUserService.beforeCreateRoom(out,realPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**得到用户的战绩
	 * @param request
	 * @param response
	 */
	private void getUserSumScore(HttpServletRequest request,
			HttpServletResponse response) {
		User user = new User();
		String str= request.getParameter("serviceStr");
		JSONObject serviceStr = new JSONObject(str);
		String suserId = serviceStr.getString("userId");
		int userId = Integer.parseInt(suserId);
		user.setId(userId);
		JSONObject jsonObject = paoDeKuaiUserService.getUserSumScore(user,serviceStr);
		write(response,jsonObject);
	}
	
	/**用户登录
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	private void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String str= request.getParameter("serviceStr");
		JSONObject serviceStr = new JSONObject(str);
		ServletOutputStream out = response.getOutputStream();
		String remoteAddr = request.getRemoteAddr();
		HttpSession session = request.getSession();
		paoDeKuaiUserService.login(serviceStr, session,out,remoteAddr);
	}
}