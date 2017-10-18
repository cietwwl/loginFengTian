package com.zxz.service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.dom4j.DocumentException;
import org.json.JSONArray;
import org.json.JSONObject;






import com.mysql.jdbc.StringUtils;
import com.zxz.dao.UserMapper;
import com.zxz.dao.UserScoreDao;
import com.zxz.dao.VedioDao;
import com.zxz.dao.impl.UserMapperImpl;
import com.zxz.dao.impl.VedioDaoImpl;
import com.zxz.domain.User;
import com.zxz.domain.UserScore;
import com.zxz.domain.Vedio;
import com.zxz.redis.RedisUtil;
import com.zxz.rpcclient.RpcClient;
import com.zxz.util.DateUtils;
import com.zxz.util.EmojiUtil;
import com.zxz.util.Server;
import com.zxz.util.StringUtil;
import com.zxz.util.WeiXinUtil;
import com.zxz.util.XmlUtils;

import config.Constants;

public class UserService implements Constants{

	private static Logger logger = Logger.getLogger(UserService.class);  
	
	UserMapper userDao = new UserMapperImpl();
	UserScoreDao userScoreDao = UserScoreDao.getInstance();//单局结算成绩
	VedioDao vedioDao = new VedioDaoImpl();
	
	/**根据用户ID查询用户的信息
	 * @param userId
	 * @return
	 */
	public User findUserById(int userId){
		User user = userDao.selectByPrimaryKey(userId);
		return user;
	}

	/**得到房间的详细信息
	 * @param roomId
	 */
	public String findRoomDetail1(String roomId) {
		DateService dateService = RpcClient.getService(DateService.class,"com.zxz.service.DateServiceImpl","101.201.115.208",1);
		String roomDetail = dateService.getRoomDetail(roomId);
		logger.info("调用房间的详细信息,服务器返回的是:"+roomDetail);
		return roomDetail;
	}
	
	/**得到房间的详细信息
	 * @param roomId
	 */
	public String findRoomDetail2(String roomId) {
		DateService dateService = RpcClient.getService(DateService.class,"com.zxz.service.DateServiceImpl","123.57.185.23",1);
		String roomDetail = dateService.getRoomDetail(roomId);
		logger.info("调用房间的详细信息,服务器返回的是:"+roomDetail);
		return roomDetail;
	}


	/**登录
	 * @param jsonObject
	 * @param session
	 * @return
	 * @throws IOException 
	 */
	public void login(JSONObject jsonObject, HttpSession session,ServletOutputStream out,String ip) throws IOException {
		boolean hasUnionid = jsonObject.has("unionid");//是否含有hasUnionid
		boolean loginResult = false;
		if(hasUnionid){//unionid登录
			loginResult = loginWithUnionid(jsonObject,out,ip,session);
		}else{//code登录
			loginWithCode(jsonObject,out,ip,session);
		}
	}
	
	


	/**用code登录
	 * @param jsonObject
	 * @param session
	 * @return
	 * @throws IOException 
	 */
	private boolean loginWithCode(JSONObject jsonObject, ServletOutputStream out,String ip,HttpSession session) throws IOException {
		String code = jsonObject.getString("code");
		try {
			JSONObject accessTokenJson = WeiXinUtil.getAccessTokenJson(code);
			String refreshToken = accessTokenJson.getString("refresh_token");
			String openid = accessTokenJson.getString("openid");
			String accesstoken = accessTokenJson.getString("access_token");
			JSONObject userInfoJson = WeiXinUtil.getUserInfo(accesstoken, openid);
			String unionid  = userInfoJson.getString("unionid");
			User findUser = new User();
			findUser.setUnionid(unionid);
			User user = userDao.findUser(findUser);
			findUser.setRefreshtoken(refreshToken);
			setUserWithUserInfoJson(userInfoJson, findUser);//封装用户的信息
			boolean isFirstLogin = true;
			if(user==null){//没有注册
				registUser(userInfoJson, findUser);
			}else{//已经注册获取用户的房卡数量
				//修改用户的refreshToken
				User modifyUser = new User();
				modifyUser.setId(user.getId());
				modifyUser.setRefreshtoken(refreshToken);
				userDao.modifyUser(modifyUser);
				findUser.setId(user.getId());
				findUser.setRoomCard(user.getRoomCard());
				findUser.setRoomid(user.getRoomid());
				isFirstLogin = false;//不是第一次登陆
				session.setAttribute("user", user);
			}
			notifyUserLoginSuccess(findUser,out,isFirstLogin,ip);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("微信登录失败");
			logger.fatal(e);
			notifyUserLoginFail(out,"errorCode","CODE传递不正确");
			return false;
		}
		return true;
	}
	
	

	
	
	/**注册用户
	 * @param userInfoJson
	 * @param findUser
	 */
	private void registUser(JSONObject userInfoJson, User findUser) {
		//findUser.setNickName(""); //过滤掉特殊字符
		//设置用户默认的房卡数量
		findUser.setRoomCard(DEFAULT_USER_REGIST_ROOMCARD);
		findUser.setCreatedate(new Date());
		try {
			userDao.insert(findUser);
		} catch (Exception e) {
			logger.error(e);
			logger.info("注册的时候出错,昵称是:"+findUser.getNickName());
			findUser.setNickName("");
			userDao.insert(findUser);
		}
	}
	
	
	
	private boolean loginWithUnionid(JSONObject jsonObject,ServletOutputStream out,String ip,HttpSession session) throws IOException {
		String unionid =  jsonObject.getString("unionid");
		User user = new User();
		user.setUnionid(unionid);
		switch (unionid) {
			case "obhqFxAmLRLMv1njQnWFsl_npjPw"://顾双
			case "obhqFxIRabSd9B2qhT_ThzsXMU58"://周益雄
			case "obhqFxCzFVH5UkKJRIH-AqePEnZ8"://张森
			case "obhqFxHtB3emb506Q-FsZwW4_Py4"://尤海涛s
				testLogin(unionid,out,ip,session);
			return true;
		}
		user = userDao.findUser(user);
		if(user!=null){//用户不存在
			String refreshToken = user.getRefreshtoken();
			try {
				String accesstoekn = WeiXinUtil.getAccessTokenWithRefreshToken(refreshToken);
				JSONObject userInfo = WeiXinUtil.getUserInfo(accesstoekn, user.getOpenid());
				setUserWithUserInfoJson(userInfo, user);//封装用户的信息
				session.setAttribute("user", user);
				notifyUserLoginSuccess(user, out,false,ip);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				notifyUserLoginFail(out,"errorRefreshToken","微信refreshToken过期请重新授权");
				logger.fatal("微信refreshToken过期");
				return false;
			}
		}else{
			notifyUserLoginFail(out,"errorUnionId","unionId不存在");
			return false;
		}
	}
	
	
	
	
	
	/**通知用户登录成功
	 * @param findUser
	 * @param session 
	 * @param isFirstLogin 是否第一次注册
	 * @throws IOException 
	 */
	private void notifyUserLoginSuccess(User findUser, ServletOutputStream out,boolean isFirstLogin,String ip) throws IOException {
		JSONObject outJsonObject = new JSONObject();
		outJsonObject.put("method", "login");
		outJsonObject.put("login", true);
		outJsonObject.put("userId",findUser.getId());
		outJsonObject.put("userName",findUser.getNickName());
		outJsonObject.put("headimgurl",findUser.getHeadimgurl());
		outJsonObject.put("roomCard",findUser.getRoomCard());//剩余的房卡数
		outJsonObject.put("unionid",findUser.getUnionid());//唯一的unionid
		outJsonObject.put("isFirstLogin",isFirstLogin);//是否第一次登陆，也就是注册
		outJsonObject.put("sex",findUser.getSex());//性别
		outJsonObject.put("description", "登录成功!");
		outJsonObject.put("ip", ip.replaceAll("/", ""));//ip地址
		String server = "";//掉线的服务器地址
		int port = 0;
		//检测用户是否掉线
		boolean isUserIsDropLine = false;//用户是否掉线
		String roomId = findUser.getRoomid();
		if(!StringUtils.isNullOrEmpty(roomId)){
			//redis中得到用户的信息
			String roomInfo = RedisUtil.getKey(roomId);
			if(!StringUtils.isNullOrEmpty(roomInfo)){
				JSONObject roomInfoJsonObject = new JSONObject(roomInfo);
				int rpcPort = roomInfoJsonObject.getInt("localRPCPort");
				String rpcServer = roomInfoJsonObject.getString("bestServer");
				try {
					DateService dateService = RpcClient.getService(
							DateService.class,
							"com.zxz.service.DateServiceImpl", rpcServer,
							rpcPort);
					String roomInfos  = dateService.isUserInRoomWithRoomId(roomId);
					JSONObject jsonObject = new JSONObject(roomInfos);
					boolean isUserInRoom = jsonObject.getBoolean("isUserInRoom");
					if(isUserInRoom){
						server = roomInfoJsonObject.getString("bestServer");
						isUserIsDropLine = true;
						port = jsonObject.getInt("port");
					}
				} catch (Exception e) {
					logger.error(e);
				}
			}
		}
		outJsonObject.put("isUserIsDropLine", isUserIsDropLine);
		if(isUserIsDropLine){
			outJsonObject.put("dropLineServer", server);//掉线的IP地址
			outJsonObject.put("port", port);//掉线的IP地址的端口号
		}
//		ResponseUtil.write(out,outJsonObject.toString());
		out.write(outJsonObject.toString().getBytes("UTF-8"));
		outJsonObject.put("refreshToken", findUser.getRefreshtoken());
		HashMap<String, String> hashMap = new HashMap<>();
		hashMap.put("baseInfo", outJsonObject.toString());
		hashMap.put("roomId", (roomId==null?0+"":roomId));
		RedisUtil.setHashMap(findUser.getUnionid(), hashMap);
//		logger.info(findUser.getNickName()+"登录成功");
	}
	
	
	
	
	/**封装用户的信息
	 * @param userInfoJson
	 * @param findUser
	 */
	private void setUserWithUserInfoJson(JSONObject userInfoJson, User findUser) {
		logger.info("userInfoJson:"+userInfoJson);
		String nickName = userInfoJson.getString("nickname");//昵称
		String unionid = userInfoJson.getString("unionid");
		String city = userInfoJson.getString("city");//城市
		String headimgurl = userInfoJson.getString("headimgurl");//头像
		String province = userInfoJson.getString("province");//省份
		String openid = userInfoJson.getString("openid");//省份
		int sex = userInfoJson.getInt("sex");//性别
//		String refreshToken = userInfoJson.getString("refresh_token");
		findUser.setCity(city);
		findUser.setHeadimgurl(headimgurl);
		findUser.setUnionid(unionid);
//		findUser.setNickName(nickName);
		findUser.setNickName(EmojiUtil.resolveToByteFromEmoji(nickName)); //过滤掉特殊字符
		findUser.setProvince(province);
		findUser.setOpenid(openid);
		findUser.setSex(sex+"");
//		findUser.setRefreshToken(refreshToken);
	}
	
	

	/**通知用户登录失败
	 * @param session
	 * @throws IOException 
	 */
	private void notifyUserLoginFail(ServletOutputStream out,String errorCode,String discription) throws IOException {
		JSONObject outJsonObject = new JSONObject();
		outJsonObject.put("login", false);
		outJsonObject.put("method", "login");
		outJsonObject.put("errorCode", errorCode);
		outJsonObject.put("discription", discription);
		out.write(outJsonObject.toString().getBytes("UTF-8"));
	}
	
	/**测试的人登录
	 * @param unionid
	 * @return
	 * @throws IOException 
	 */
	private void testLogin(String unionid,ServletOutputStream out,String ip,HttpSession session) throws IOException {
		User user = new User();
		user.setUnionid(unionid);;
		user = userDao.findUser(user);
		if(user!=null){
			try {
				JSONObject userInfo = new JSONObject();
				userInfo.put("userId",user.getId());
				user.setHeadimgurl(user.getHeadimgurl());//头像
				userInfo.put("userName",user.getNickName());//昵称
				userInfo.put("headimgurl",user.getHeadimgurl());//头像
				userInfo.put("roomCard",user.getRoomCard());//剩余的房卡数
				userInfo.put("unionid",user.getUnionid());//唯一的unionid
				userInfo.put("nickName",user.getUserName());//用户名 
				userInfo.put("sex",user.getSex());//性别
				session.setAttribute("user", user);
				notifyUserLoginSuccess(user, out,false,ip);
			} catch (Exception e) {
				e.printStackTrace();
				logger.fatal("微信refreshToken过期");
			}
		}else{
			notifyUserLoginFail(out,"errorUnionId","unionId不存在");
		}
	}

	/**得到用户的成绩
	 * @param user
	 * @return
	 */
	public JSONObject getUserSumScore(User user,JSONObject jsonObject) {
		
		
		int index = jsonObject.getInt("index");
		int pageSzie = 1;
		Map<String, Object> map = new HashMap<>();
		map.put("userid", user.getId());
		map.put("pageIndex", (index-1)*pageSzie);
		map.put("pageSize", pageSzie);
		map.put("totalSize", pageSzie*4);
		List<Map<String, Object>> userScoreList = userScoreDao.findUserSumScore(map);
		JSONObject outJsonObject = new JSONObject();
		JSONArray scoreArray = new JSONArray();
		for(int i=0;i<userScoreList.size();i++){
			Map<String, Object> oneMap = userScoreList.get(i);
			int userid = Integer.parseInt(oneMap.get("userid")+"");
			int finalScore = Integer.parseInt(oneMap.get("finalScore")+"");
			int roomNumber = Integer.parseInt(oneMap.get("roomNumber")+"");
			String createDate = oneMap.get("createDate")+"";
			String nickName = oneMap.get("nickName")+"";
			int total = Integer.parseInt(oneMap.get("total")+"");
			createDate = createDate.substring(0, createDate.length()-2);
			JSONObject everyScore = new JSONObject();
			everyScore.put("userid", userid);//用户id
			everyScore.put("finalScore", finalScore);//总成绩
			everyScore.put("roomNumber", roomNumber);//房号
			everyScore.put("createDate", createDate);//创建时间
			everyScore.put("nickName", nickName);//昵称
			everyScore.put("total", total);//局数
			scoreArray.put(everyScore);
		}
		JSONArray personArray = new JSONArray();
		for(int i=0;i<scoreArray.length();){
			JSONObject object = new JSONObject();
			JSONObject baseScore = scoreArray.getJSONObject(i);
			object.put("total", baseScore.get("total"));
			object.put("roomNumber", baseScore.get("roomNumber"));
			object.put("createDate", baseScore.get("createDate"));
			JSONArray oneGame = new JSONArray();
			if(i>scoreArray.length()){
				break;
			}
			if(i+1<scoreArray.length()){
				JSONObject p1 = scoreArray.getJSONObject(i);
				removeJsonkey(p1);
				oneGame.put(p1);
			}else{
				break;
			}
			if(i+1<scoreArray.length()){
				JSONObject p2 = scoreArray.getJSONObject(i+1);
				removeJsonkey(p2);
				oneGame.put(p2);
			}else{
				break;
			}
			if(i+1<scoreArray.length()){
				JSONObject p3 = scoreArray.getJSONObject(i+2);
				removeJsonkey(p3);
				oneGame.put(p3);
			}else{
				break;
			}
			if(i+1<scoreArray.length()){
				JSONObject p4 = scoreArray.getJSONObject(i+3);
				removeJsonkey(p4);
				oneGame.put(p4);
			}else{
				break;
			}
			object.put("oneGame", oneGame);
			personArray.put(object);
			i=i+4;
		}
		outJsonObject.put("userScores", personArray);
		outJsonObject.put("method", "getUserSumScore");
		outJsonObject.put("discription", "得到用户总结算的战绩");
		outJsonObject.put("type", "sumScore");
		return outJsonObject;
		
	}
	

	private void removeJsonkey(JSONObject p1) {
		p1.remove("total");
		p1.remove("roomNumber");
		p1.remove("createDate");
	}

	/**得到创建房间的最优服务器
	 * @param out
	 * @throws IOException 
	 * @throws DocumentException 
	 */
	public void beforeCreateRoom(ServletOutputStream out,String path) throws Exception {
		LinkedHashMap<Integer, Server> serverMaps = new LinkedHashMap<>();
		List<Server> serverList = XmlUtils.getServerList(path,honZhongServer);
		for(int i=0;i<serverList.size();i++){
			Server server = serverList.get(i);
			try {
				DateService dateService = RpcClient.getService(DateService.class, "com.zxz.service.DateServiceImpl",server.getIp(),server.getRpcPort());
				String totalOnlineUserAndTotalRoom = dateService.getTotalOnlineUserAndTotalRoom();
				JSONObject jsonObject = new JSONObject(totalOnlineUserAndTotalRoom);
//				int totalLoginUser = jsonObject.getInt("totalLoginUser");
				int totalRoom = jsonObject.getInt("totalRoom");//总的房间数
				int port = jsonObject.getInt("port");//服务器的端口
				boolean isWaitRestart = jsonObject.getBoolean("isWaitRestart");//是否等待重新启动
				if (!isWaitRestart) {//不是等待重启
					server.setTotalRoom(totalRoom);//总的房间数
					server.setGamePort(port);
					serverMaps.put(server.getId(), server);
				}
				logger.info("游戏服务器当前的信息是" + totalOnlineUserAndTotalRoom);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e);
			}
		}

		int beserServerId = 1;
		int maxRoom= 1000;
		Iterator<Integer> iterator = serverMaps.keySet().iterator();
		while(iterator.hasNext()){
			Integer next = iterator.next();
			Server server = serverMaps.get(next);
			int nowRoomTotal = server.getTotalRoom();
			if(server.getTotalRoom()<maxRoom){
				beserServerId = server.getId();
				maxRoom = nowRoomTotal;
			}
		}
		Server bestServer = serverMaps.get(beserServerId);
		logger.info("创建房间当前最好的服务器是:"+bestServer);
		JSONObject outJsonObject = new JSONObject();
		outJsonObject.put("bestServer", bestServer.getIp());
		outJsonObject.put("port", bestServer.getGamePort());
		out.write(outJsonObject.toString().getBytes("UTF-8"));
	}

	
	/**在进入房间之前
	 * @param serviceStr
	 * @param out
	 * @param realPath 
	 * @throws IOException 
	 * @throws DocumentException 
	 */
	public String beforeEnterRoom(JSONObject serviceStr, String realPath) throws Exception {
		int roomId = serviceStr.getInt("roomId");
		String server = null;
		int port = 0;//游戏服务器的端口号
		JSONObject outJsonObject = null;
		String roomInfo = RedisUtil.getKey(roomId+"");
		if(StringUtil.isNull(roomInfo)){//房间不存在
			//如果redis中没有房间信息则遍历所有的游戏服务器获取数据
			List<Server> serverList = XmlUtils.getServerList(realPath,honZhongServer);
			for(int i=0;i<serverList.size();i++){
				Server ser = serverList.get(i);
				DateService dateService = RpcClient.getService(DateService.class, "com.zxz.service.DateServiceImpl",ser.getIp(),ser.getRpcPort());
				String result = dateService.isHaveRoomWithRoomId(roomId+"");
				JSONObject resultJsonObject =  new JSONObject(result);
				String code = resultJsonObject.getString("code");
				if(code.equals("success")){
					port = resultJsonObject.getInt("port");
					JSONObject outJsonObject2 = new JSONObject();
					outJsonObject2.put("code", "success");
					outJsonObject2.put("server", ser.getIp());
					outJsonObject2.put("port", port);
					outJsonObject2.put("method", "beforeEnterRoom");
					outJsonObject2.put("description", "房间地址是");
					return outJsonObject2.toString();
				}
			}
			outJsonObject = new JSONObject();
			outJsonObject.put("code", "error");
			outJsonObject.put("method", "beforeEnterRoom");
			outJsonObject.put("description", "房间不存在");
			return outJsonObject.toString();
		}
		JSONObject roomJsonObject = new JSONObject(roomInfo);
		String bestServer = roomJsonObject.getString("bestServer");
		int rpcPort = roomJsonObject.getInt("localRPCPort");
		DateService dateService = RpcClient.getService(DateService.class, "com.zxz.service.DateServiceImpl",bestServer,rpcPort);
		String result = dateService.isHaveRoomWithRoomId(roomId+"");
		JSONObject resultJsonObject =  new JSONObject(result);
		String code = resultJsonObject.getString("code");
		if(code.equals("success")){
			server = bestServer;
			port = resultJsonObject.getInt("port");
			JSONObject outJsonObject2 = new JSONObject();
			outJsonObject2.put("code", "success");
			outJsonObject2.put("server", server);
			outJsonObject2.put("port", port);
			outJsonObject2.put("method", "beforeEnterRoom");
			outJsonObject2.put("description", "房间地址是");
//			out.write(outJsonObject2.toString().getBytes("UTF-8"));
			return outJsonObject2.toString();
		}else{
//			out.write(result.getBytes("UTF-8"));
			return result;
		}
	}

	/**得到用户单局的战绩
	 * @param user
	 * @param serviceStr
	 * @return
	 */
	public JSONObject getUserScore(JSONObject serviceStr) {
		int sumScoreId = serviceStr.getInt("sumScoreId");
		int userid = serviceStr.getInt("userid");
		Map<String, Object> map = new HashMap<>();
		map.put("sumScoreId", sumScoreId);
		map.put("userid", userid);
		List<UserScore> userScoreList = userScoreDao.findUserScore(map);
		JSONObject outJsonObject = new JSONObject();
		JSONArray scoreArray = new JSONArray();
		for(int i=0;i<userScoreList.size();i++){
			UserScore userScore = userScoreList.get(i);
			int roomid = userScore.getRoomid();
			int currentGame = userScore.getCurrentGame();
			int score = userScore.getScore();
			int vedioid = userScore.getVedioid();
			String createDate = DateUtils.getFormatDate(userScore.getCreateDate(), "yyyy/MM/dd hh:mm:ss");
			JSONObject everyScore = new JSONObject();
			everyScore.put("roomId", roomid);
			everyScore.put("currentGame", currentGame);
			everyScore.put("score", score);
			everyScore.put("createDate", createDate);
			everyScore.put("vedioid", vedioid);
			scoreArray.put(everyScore);
		}
		outJsonObject.put("userScores", scoreArray);
		outJsonObject.put("method", "getUserScore");
		outJsonObject.put("discription", "得到单局用户的战绩");
		outJsonObject.put("type", "everyScore");
		logger.info(outJsonObject);
		return outJsonObject;
	}

	/**得到录像
	 * @param user
	 * @param serviceStr
	 * @return
	 */
	public JSONObject getVedio(JSONObject serviceStr) {
		int roomId = serviceStr.getInt("vedioId");
		Vedio vedio = vedioDao.selectByPrimaryKey(roomId);
		System.out.println(vedio);
		JSONObject outJsonObject = new JSONObject();
		if(vedio==null){
			String createDate = DateUtils.getFormatDate(new Date(), "yyyy/MM/dd hh:mm:ss");
			outJsonObject.put("record", vedio.getRecord());
			outJsonObject.put("createDate", createDate);
			return outJsonObject; 
		}
		String createDate = DateUtils.getFormatDate(vedio.getCreatedate(), "yyyy/MM/dd hh:mm:ss");
		outJsonObject.put("record", vedio.getRecord());
		outJsonObject.put("createDate", createDate);
		return outJsonObject;
	}
	/**
	 * 判断该人是不是代理 查询agent
	 * @param unionid
	 * @return
	 */
	public boolean isDaiLi(String unionid) {
		Map<Object,Object> hashMap = new HashMap<>();
		hashMap.put("unionid", unionid);
		Map<Object,Object> map = userDao.findAgentOrder(hashMap);
		if(map==null){
			return false;
		}else{
			return true;
		}
	}
}
