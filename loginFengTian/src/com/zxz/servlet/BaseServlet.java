package com.zxz.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.zxz.util.PageUtil;
import com.zxz.util.StringUtil;

public abstract class BaseServlet extends HttpServlet implements CallBack,DoExcepion{

	private static Logger logger = Logger.getLogger(BaseServlet.class);  
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	HttpServletRequest request;
	HttpServletResponse response;
	HttpSession session;
	String method;//方法名
    String serviceStr;
	
	public BaseServlet() {
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.request = req;
		this.response = resp;
		this.session = getSession();
		try {
			String method = req.getParameter("method");
			if(StringUtil.isNull(method)){
				return;
			}
			this.method = method;
			String serviceStr = request.getParameter("serviceStr");
			this.serviceStr = serviceStr;
			receiveMessage(req, resp);
		} catch (Exception e) {
			doWithException(e);
		}
	}

	
	
	/**返回
	 * @return
	 */
	public JSONObject getServiceStr(){
		JSONObject jsonObject = new JSONObject(this.serviceStr);
		return jsonObject;
	}
	
	
	public HttpServletRequest getRequest() {
		return request;
	}

	public BaseServlet setRequest(HttpServletRequest request) {
		this.request = request;
		return this;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}
	
	/**重定向
	 * @param path
	 */
	public void sendRedirect(String path){
		try {
			response.sendRedirect(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**转发
	 * @param path
	 */
	public void forward(String path){
		try {
			request.getRequestDispatcher(path).forward(request, response);
		} catch (ServletException | IOException e) {
			e.printStackTrace();
		}
	}

	
	private HttpSession getSession(){
		HttpSession session = request.getSession();
		return session;
	}
	
	@Override
	public void doWithException(Exception exception) {
		exception.printStackTrace();
		StringBuffer requestURL = request.getRequestURL();
		System.out.println("requestURL:"+requestURL);
		Map<String, String[]> parameterMap = request.getParameterMap();
		Iterator<String> iterator = parameterMap.keySet().iterator();
		int begin=1;
		while(iterator.hasNext()){
			String key = iterator.next();
			String[] strings = parameterMap.get(key);
			if(begin==1){
				requestURL.append("?");
			}
			for(int i=0;i<strings.length;i++){
				if(begin==1){
					requestURL.append(key+"="+strings[i]);
				}else{
					requestURL.append("&"+key+"="+strings[i]);
				}
				System.out.println("key:"+key+" "+strings[i]);
				begin++;
			}
		}
		logger.fatal("出错的请求地址是:"+requestURL);
		//sendRedirect("/MJ/errorPage/error.html");
	}
	
	
	/**得到路径
	 * @return
	 */
	public String getContextPath(){
		return request.getServletContext().getContextPath();
	}
	
	
	/**得到当前页
	 * @return
	 */
	public int getIndex(){
		String sindex = request.getParameter("index");
		if(StringUtil.isNull(sindex)){
			return 1;
		}else{
			return Integer.parseInt(sindex);
		}
	}
	
	/**得到每页大小
	 * @return
	 */
	public int getPageSize(){
		return 10;
	}
	
	
	/**得到参数
	 * @param parameter
	 * @return
	 */
	public String getParameter(String parameter){
		String str = request.getParameter(parameter);
		if(!StringUtil.isNull(str)){
			return str.trim();
		}
		return null;
	}
	
	public void setAttribute(String name, Object o){
		if(!StringUtil.isNull(name)){
			request.setAttribute(name, o);
		}
	}
	
	/**得到一个pageUtil，已经指定好了页数
	 * @return
	 */
	public PageUtil<Map<String,Object>> getObjectPageUtil(){
		PageUtil<Map<String,Object>> pageUtil = new PageUtil<>();
		pageUtil.setIndex(getIndex());
		return pageUtil;
	}
	
	
	/**向输出流里面写信息
	 * @param str
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public void write(Object str){
		try {
			response.getOutputStream().write(str.toString().getBytes("UTF-8"));
		} catch (Exception e) {
			logger.error(str);
			e.printStackTrace();
			doWithException(e);
		}
	}
	
}
