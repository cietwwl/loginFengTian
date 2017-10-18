package com.zxz.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.zxz.domain.AppVersion;
import com.zxz.redis.RedisUtil;
import com.zxz.service.AppService;
import com.zxz.util.StringUtil;

import config.Constants;

public class AppInFoServlet extends HttpServlet implements Constants{

	AppService appService = new AppService();
	
	
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
		if(method.equals("getVersionInfo")){
			getVersionInfo(request,response);//得到游戏的版本信息
		}else if(method.equals("getVersionInfoZhenJiang")){
			getVersionInfoZhenJiang(request,response);//得到镇江的版本信息
		}else if(method.equals("getVersionInfoPaoDeKuai")){
			getVersionInfoPaoDeKuai(request,response);//得到镇江的版本信息
		}
	}

	
	/**向输出流里面写信息
	 * @param str
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public void write(Object str,HttpServletResponse response){
		try {
			response.getOutputStream().write(str.toString().getBytes("UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void getVersionInfoPaoDeKuai(HttpServletRequest request,
			HttpServletResponse response) {
		String majiangVersion = RedisUtil.getKey("pdkVersion",REDIS_DB_PDK);
		if(StringUtil.isNull(majiangVersion)){
			AppVersion appVersion = appService.getAppVersionPdk();
			JSONObject outJsonObject = new JSONObject();
			//安卓
			outJsonObject.put("versionNumber", appVersion.getVersionNumber());//版本号
			outJsonObject.put("discription", appVersion.getDiscription());//描述
			outJsonObject.put("downloadUrl", appVersion.getDownloadUrl());//下载地址
			outJsonObject.put("force", appVersion.getForce());//是否强制更新
			//苹果
			outJsonObject.put("versionNumberApple", appVersion.getVersionNumberApple());
			outJsonObject.put("discriptionApple", appVersion.getDiscriptionApple());
			outJsonObject.put("downloadUrlApple", appVersion.getDownloadUrlApple());
			outJsonObject.put("forceApple", appVersion.getForceApple());
			
			outJsonObject.put("mcString", appVersion.getMcString());//魔窗
			write(outJsonObject.toString(),response);
			RedisUtil.setKey("pdkVersion", outJsonObject.toString(),REDIS_DB_PDK);
		}else{
			write(majiangVersion,response);
		}
	}


	/**得到镇江的版本信息
	 * @param request
	 * @param response
	 */
	private void getVersionInfoZhenJiang(HttpServletRequest request,
			HttpServletResponse response) {
		String majiangVersion = RedisUtil.getKey("majiangVersion",REDIS_DB_SANGUO);
		if(StringUtil.isNull(majiangVersion)){
			AppVersion appVersion = appService.getAppVersionZhenJaing();
			JSONObject outJsonObject = new JSONObject();
			//安卓
			outJsonObject.put("versionNumber", appVersion.getVersionNumber());//版本号
			outJsonObject.put("discription", appVersion.getDiscription());//描述
			outJsonObject.put("downloadUrl", appVersion.getDownloadUrl());//下载地址
			outJsonObject.put("force", appVersion.getForce());//是否强制更新
			//苹果
			outJsonObject.put("versionNumberApple", appVersion.getVersionNumberApple());
			outJsonObject.put("discriptionApple", appVersion.getDiscriptionApple());
			outJsonObject.put("downloadUrlApple", appVersion.getDownloadUrlApple());
			outJsonObject.put("forceApple", appVersion.getForceApple());
			outJsonObject.put("mcString", appVersion.getMcString());//魔窗
			write(outJsonObject.toString(),response);
			RedisUtil.setKey("majiangVersion", outJsonObject.toString(),REDIS_DB_SANGUO);
		}else{
			write(majiangVersion,response);
		}
	}


	/**得到游戏的版本信息
	 * @param request
	 * @param response
	 */
	private void getVersionInfo(HttpServletRequest request,
			HttpServletResponse response) {
		String majiangVersion = RedisUtil.getKey("majiangVersion");
		if(StringUtil.isNull(majiangVersion)){
			AppVersion appVersion = appService.getAppVersion();
			JSONObject outJsonObject = new JSONObject();
			//安卓
			outJsonObject.put("versionNumber", appVersion.getVersionNumber());//版本号
			outJsonObject.put("discription", appVersion.getDiscription());//描述
			outJsonObject.put("downloadUrl", appVersion.getDownloadUrl());//下载地址
			outJsonObject.put("force", appVersion.getForce());//是否强制更新
			//苹果
			outJsonObject.put("versionNumberApple", appVersion.getVersionNumberApple());
			outJsonObject.put("discriptionApple", appVersion.getDiscriptionApple());
			outJsonObject.put("downloadUrlApple", appVersion.getDownloadUrlApple());
			outJsonObject.put("forceApple", appVersion.getForceApple());
			outJsonObject.put("mcString", appVersion.getMcString());//魔窗
			write(outJsonObject.toString(),response);
			RedisUtil.setKey("majiangVersion", outJsonObject.toString(),REDIS_DB_HONGZHONG);
		}else{
			write(majiangVersion,response);
		}
		//System.out.println(outJsonObject.toString());
	}
}
