package com.zxz.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.zxz.domain.User;
import com.zxz.service.UserService;
import com.zxz.service.ZhenJiangUserService;
import com.zxz.util.RoomCardUtils;

public class ZhenJiangUserServlet extends HttpServlet implements RoomCardUtils{

	private static final long serialVersionUID = 1L;
	
	private static Logger logger = Logger.getLogger(ZhenJiangUserServlet.class);  
	ZhenJiangUserService zhenJiangUserService = new ZhenJiangUserService();
	
	public ZhenJiangUserServlet() {
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String method = request.getParameter("method");
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
		}else if (method.equals("bindOperateUser")){
			bindOperateUser(request, response);//绑定代理(俱乐部)
		}else if (method.equals("checkIsBind")){
			checkIsBind(request, response);//绑定代理(俱乐部)
		}else if (method.equals("getTimeElse")){
			getTimeElse(request, response);//分享得奖
		}else if (method.equals("fenxiangchenggong")){
			fenxiangchenggong(request, response);//分享成功
		}else if(method.equals("getRoomList")){
			getRoomList(request, response);//得到房间列表
		}else if(method.equals("checkCard")){
			checkCard(request, response);//分享成功
		}else if(method.equals("daiLiCreateRoom")){//代理创建房间
			daiLiCreateRoom(request, response);
		}
	}
	
	/**
	 * 代理创建房间
	 * @param request
	 * @param response
	 */
	private void daiLiCreateRoom(HttpServletRequest request,
			HttpServletResponse response) {
		String str= request.getParameter("serviceStr");
		JSONObject serviceStr = new JSONObject(str);
//		logger.info("传来的信息:"+serviceStr);
		int userId = serviceStr.getInt("userId");
		String realPath = request.getSession().getServletContext().getRealPath("/");
		JSONObject outJson;
		try {
			outJson = zhenJiangUserService.daiLiCreateRoom(realPath, userId,serviceStr);
			logger.info("房间列表返回："+outJson);
			write(response, outJson);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**创建房间
	 * @param request
	 * @param response
	 */
	private void checkCard(HttpServletRequest request,
			HttpServletResponse response) {
		String str= request.getParameter("serviceStr");
		JSONObject serviceStr = new JSONObject(str);
		int userId = serviceStr.getInt("userId");
		String realPath = request.getSession().getServletContext().getRealPath("/");
		JSONObject outJson;
		try {
			outJson = zhenJiangUserService.checkCard(realPath, userId);
//			logger.info("房间列表返回："+outJson);
			write(response, outJson);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**得到房间列表
	 * @param request
	 * @param response
	 */
	private void getRoomList(HttpServletRequest request,
			HttpServletResponse response) {
		String str= request.getParameter("serviceStr");
		JSONObject serviceStr = new JSONObject(str);
		int userId = serviceStr.getInt("userId");
		String realPath = request.getSession().getServletContext().getRealPath("/");
		JSONObject outJson;
		try {
			outJson = zhenJiangUserService.getRoomList(realPath, userId);
//			logger.info("房间列表返回："+outJson);
			write(response, outJson);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**分享成功
	 * @param request
	 * @param response
	 */
	private void fenxiangchenggong(HttpServletRequest request,
			HttpServletResponse response) {
		String str= request.getParameter("serviceStr");
		JSONObject serviceStr = new JSONObject(str);
		JSONObject outJson = zhenJiangUserService.fenxiangchenggong(serviceStr);
		write(response, outJson);
	}

	/**分享得奖
	 * @param request
	 * @param response
	 */
	private void getTimeElse(HttpServletRequest request,
			HttpServletResponse response) {
		String str= request.getParameter("serviceStr");
		JSONObject serviceStr = new JSONObject(str);
		JSONObject outJson = zhenJiangUserService.getTimeElse(serviceStr);
		write(response, outJson);
	}

	/**检查用户是否绑定了代理
	 * @param request
	 * @param response
	 */
	private void checkIsBind(HttpServletRequest request,
			HttpServletResponse response) {
		String str= request.getParameter("serviceStr");
		JSONObject serviceStr = new JSONObject(str);
		Map<String, Object> checkIsBind = zhenJiangUserService.checkIsBind(serviceStr);
		if(checkIsBind!=null){
			JSONObject outJsonObject = new JSONObject();
			outJsonObject.put("result", true);
			outJsonObject.put("clubId", checkIsBind.get("operateUserId"));
			outJsonObject.put("clubName", checkIsBind.get("userName")); //俱乐部名字
			outJsonObject.put("clubCreate", checkIsBind.get("userName"));//俱乐部创建者
			outJsonObject.put("clubNowNum", checkIsBind.get("total"));//俱乐部当前人数
			outJsonObject.put("clubNotice", checkIsBind.get("notice"));//俱乐部公告
			outJsonObject.put("clubTotalNum", 1000); //俱乐部总人数
			write(response, outJsonObject);
		}else{
			JSONObject outJsonObject = new JSONObject();
			outJsonObject.put("result", false);
			write(response, outJsonObject);
		}
	}

	/**绑定代理(俱乐部)
	 * @param request
	 * @param response
	 */
	private void bindOperateUser(HttpServletRequest request,
			HttpServletResponse response) {
		String str= request.getParameter("serviceStr");
		JSONObject serviceStr = new JSONObject(str);
		int result = zhenJiangUserService.bindOperateUser(serviceStr);
		if(result==-1){
			JSONObject outJsonObject = new JSONObject();
			outJsonObject.put("result", false);
			outJsonObject.put("discription", "您已经绑定");
			write(response, outJsonObject);
		}else if(result==-2 ){
			JSONObject outJsonObject = new JSONObject();
			outJsonObject.put("result", false);
			outJsonObject.put("discription", "俱乐部ID不存在");
			write(response, outJsonObject);
		}else if(result==-3){
			JSONObject outJsonObject = new JSONObject();
			outJsonObject.put("result", false);
			outJsonObject.put("discription", "用户不存在");
			write(response, outJsonObject);
		}else{
			if(result>=0){
				JSONObject outJsonObject = new JSONObject();
				outJsonObject.put("result", true);
				outJsonObject.put("discription", "绑定成功!");
				write(response, outJsonObject);
			}else{
				JSONObject outJsonObject = new JSONObject();
				outJsonObject.put("result", false);
				outJsonObject.put("discription", "绑定失败!");
				write(response, outJsonObject);
			}
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
		UserService userService = new UserService();
		JSONObject jsonObject = userService.getVedio(serviceStr);
		logger.info(jsonObject.toString());
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
		UserService userService = new UserService();
		JSONObject jsonObject = userService.getUserScore(serviceStr);
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
			String result = zhenJiangUserService.beforeEnterRoom(serviceStr,realPath);
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
			String serviceStr = request.getParameter("serviceStr");
			JSONObject jsonString = new JSONObject(serviceStr);
			if(!jsonString.has("userId")){
				JSONObject outJsonObject = new JSONObject();
				outJsonObject.put("isSuccess", false);
				outJsonObject.put("description", "没有用户ID,创建失败!");
				out.write(outJsonObject.toString().getBytes("UTF-8"));
				return;
			}
			int userId = jsonString.getInt("userId");
			if(!jsonString.has("juNum")){
				JSONObject outJsonObject = new JSONObject();
				outJsonObject.put("isSuccess", false);
				outJsonObject.put("description", "没有局数,创建失败!");
				out.write(outJsonObject.toString().getBytes("UTF-8"));
				return;
			}
			int juNum = jsonString.getInt("juNum");
			int haoKa = MINCARD;
			if(juNum>8){
				haoKa = MAXCARD;
			}
			zhenJiangUserService.beforeCreateRoom(out,realPath,userId,haoKa);
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
		int userId = serviceStr.getInt("userId");
		user.setId(userId);
		JSONObject jsonObject = zhenJiangUserService.getUserSumScore(user,serviceStr);
		if(jsonObject==null){
			return;
		}
		write(response,jsonObject);
	}
	
	/**用户登录
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	private void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String str= request.getParameter("serviceStr");
		logger.info("登录请求:"+str);
		JSONObject serviceStr = new JSONObject(str);
		ServletOutputStream out = response.getOutputStream();
		String remoteAddr = "";
		HttpSession session = request.getSession();
		zhenJiangUserService.login(serviceStr, session,out,remoteAddr);
	}
}