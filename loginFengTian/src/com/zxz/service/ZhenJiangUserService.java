package com.zxz.service;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
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
import com.zxz.dao.OperateUserDao;
import com.zxz.dao.UserMapper;
import com.zxz.dao.UserScoreDao;
import com.zxz.dao.VedioDao;
import com.zxz.dao.ZjcardDao;
import com.zxz.dao.impl.OperateUserDaoImpl;
import com.zxz.dao.impl.UserMapperImpl;
import com.zxz.dao.impl.UsersMapperDaoImpl;
import com.zxz.dao.impl.VedioDaoImpl;
import com.zxz.dao.impl.ZjcardDaoImpl;
import com.zxz.domain.OperateUser;
import com.zxz.domain.User;
import com.zxz.domain.UserScore;
import com.zxz.domain.UsersMapper;
import com.zxz.domain.Vedio;
import com.zxz.domain.Zjcard;
import com.zxz.redis.RedisUtil;
import com.zxz.rpcclient.RpcClient;
import com.zxz.util.DateUtils;
import com.zxz.util.EmojiUtil;
import com.zxz.util.RoomCardUtils;
import com.zxz.util.Server;
import com.zxz.util.StringUtil;
import com.zxz.util.XmlUtils;
import com.zxz.util.ZhenJiangWeiXinUtil;

import config.Constants;

public class ZhenJiangUserService implements Constants,RoomCardUtils{

	private static Logger logger = Logger.getLogger(ZhenJiangUserService.class);  
	
	UserMapper userDao = new UserMapperImpl();
	UserScoreDao userScoreDao = UserScoreDao.getInstance();//单局结算成绩
	VedioDao vedioDao = new VedioDaoImpl();
	ZjcardDao zjcardDao = new ZjcardDaoImpl();
	UsersMapperDaoImpl userMapperDao = new UsersMapperDaoImpl();
	OperateUserDao operateUserDao = new OperateUserDaoImpl();
	
	
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
			JSONObject accessTokenJson = ZhenJiangWeiXinUtil.getAccessTokenJson(code);
			String refreshToken = accessTokenJson.getString("refresh_token");
			String openid = accessTokenJson.getString("openid");
			String accesstoken = accessTokenJson.getString("access_token");
			JSONObject userInfoJson = ZhenJiangWeiXinUtil.getUserInfo(accesstoken, openid);
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
				findUser.setId(user.getId());
				isRegistZhenJiang(findUser);
				findUser.setRoomid(user.getRoomid());
				findUser.setIsDaiLi(user.getIsDaiLi());
				isFirstLogin = false;//不是第一次登陆
				//修改用户的头像和昵称
				User modifyUser = new User();
				modifyUser.setId(user.getId());
				//modifyUser.setRefreshtoken(refreshToken);
				modifyUser.setHeadimgurl(findUser.getHeadimgurl());//修改用户的头像
				userDao.modifyUser(modifyUser);
			}
			ZjcardDao zjcardDao = new ZjcardDaoImpl();
			Zjcard zjcard = new Zjcard();
			zjcard.setUserid(findUser.getId());
			zjcard.setRefreshToken(refreshToken);
			zjcardDao.updateByPrimaryKeySelective(zjcard);
			notifyUserLoginSuccess(findUser,out,isFirstLogin,ip);
		} catch (Exception e) {
			logger.warn(getClass().getName(), e);
			logger.info("微信登录失败");
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
		}finally{
			isRegistZhenJiang(findUser);
		}
	}
	
	
	
	/**是否需要注册镇江
	 * @param user
	 * @return
	 */
	public boolean isRegistZhenJiang(User user){
		ZjcardDao zjcardDao = new ZjcardDaoImpl();
		Zjcard userZjCard = zjcardDao.selectByPrimaryKey(user.getId());
		if(userZjCard==null){
			Zjcard zjcard = new Zjcard();
			zjcard.setUserid(user.getId());
			zjcard.setRoomcard(DEFAULT_USER_REGIST_ROOMCARD_ZHEN_JIANG);
			zjcard.setRefreshToken(user.getRefreshtoken());
			zjcardDao.insert(zjcard);
			user.setRoomCard(DEFAULT_USER_REGIST_ROOMCARD_ZHEN_JIANG);
			return true;
		}
		user.setRoomCard(userZjCard.getRoomcard());
		return false;
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
		if(user!=null){//用户存在
			Zjcard zjcard = zjcardDao.selectByPrimaryKey(user.getId());
			if(zjcard==null){
				notifyUserLoginFail(out,"errorRefreshToken","微信refreshToken过期请重新授权");
				return false;
			}
			String refreshToken = zjcard.getRefreshToken();
			try {
//				String accesstoekn = ZhenJiangWeiXinUtil.getAccessTokenWithRefreshToken(refreshToken);
//				JSONObject userInfo = ZhenJiangWeiXinUtil.getUserInfo(accesstoekn, user.getOpenid());
//				setUserWithUserInfoJson(userInfo, user);//封装用户的信息
				user.setRoomCard(zjcard.getRoomcard());
				notifyUserLoginSuccess(user, out,false,ip);
				return true;
			} catch (Exception e) {
				logger.warn(getClass().getName(), e);
				logger.fatal("微信refreshToken过期");
				notifyUserLoginFail(out,"errorRefreshToken","微信refreshToken过期请重新授权");
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
		outJsonObjectPutBase(findUser, isFirstLogin, ip, outJsonObject);//存放基础的信息
		String server = "";//掉线的服务器地址
		int port = 0;
		//检测用户是否掉线
		boolean isUserIsDropLine = false;//用户是否掉线
		String roomId = RedisUtil.getKey("usRoomId"+findUser.getId(), REDIS_DB_SANGUO);
		if(!StringUtils.isNullOrEmpty(roomId)){
			//redis中得到用户的信息
			String roomInfo = RedisUtil.getKey("ftRoomId"+roomId,REDIS_DB_SANGUO);
			if(!StringUtils.isNullOrEmpty(roomInfo)){
				JSONObject roomInfoJsonObject = new JSONObject(roomInfo);
				int rpcPort = roomInfoJsonObject.getInt("localRPCPort");
				String rpcServer = roomInfoJsonObject.getString("bestServer");
				try {
					DateService dateService = RpcClient.getService(DateService.class,"com.zxz.service.DateServiceImpl", rpcServer,rpcPort);
					String roomInfos  = dateService.isUserInRoomWithRoomId(roomId,findUser.getId()+"");
					JSONObject jsonObject = new JSONObject(roomInfos);
					boolean isUserInRoom = jsonObject.getBoolean("isUserInRoom");
					if(isUserInRoom){
						server = roomInfoJsonObject.getString("bestServer");
						isUserIsDropLine = true;
						port = jsonObject.getInt("port");
					}
					
				} catch (Exception e) {
					e.printStackTrace();
					logger.warn(e.getClass().getName(), e);
				}
			}
		}
		outJsonObject.put("isUserIsDropLine", isUserIsDropLine);
		if(isUserIsDropLine){
			outJsonObject.put("dropLineServer", server);//掉线的IP地址
			outJsonObject.put("port", port);//掉线的IP地址的端口号
		}
		outJsonObject.put("isDaiLi", findUser.getIsDaiLi()==0?false:true);
		logger.info("登录返回:"+outJsonObject.toString());
		out.write(outJsonObject.toString().getBytes("UTF-8"));
		outJsonObject.put("refreshToken", findUser.getRefreshtoken());
		HashMap<String, String> hashMap = new HashMap<>();
		hashMap.put("baseInfo", outJsonObject.toString());
		hashMap.put("roomId", (roomId==null?0+"":roomId));
		RedisUtil.setHashMap("uid"+findUser.getId(),hashMap,REDIS_DB_SANGUO,EXPIRE_USER_LOGIN);
	}

	
	/**存放基础的信息
	 * @param user
	 * @param isFirstLogin
	 * @param ip
	 * @param outJsonObject
	 */
	private void outJsonObjectPutBase(User user, boolean isFirstLogin,
			String ip, JSONObject outJsonObject) {
		outJsonObject.put("method", "login");
		outJsonObject.put("login", true);
		outJsonObject.put("userId",user.getId());
		outJsonObject.put("userName",user.getNickName());
		outJsonObject.put("headimgurl",user.getHeadimgurl());
		outJsonObject.put("unionid",user.getUnionid());//唯一的unionid
		outJsonObject.put("isFirstLogin",isFirstLogin);//是否第一次登陆，也就是注册
		outJsonObject.put("sex",user.getSex());//性别
		outJsonObject.put("description", "登录成功!");
		outJsonObject.put("ip", ip.replaceAll("/", ""));//ip地址
		outJsonObject.put("roomCard",user.getRoomCard());//剩余的房卡数
	}
	
	
	
	/**封装用户的信息
	 * @param userInfoJson
	 * @param findUser
	 */
	private void setUserWithUserInfoJson(JSONObject userInfoJson, User findUser) {
		String nickName = userInfoJson.getString("nickname");//昵称
		String unionid = userInfoJson.getString("unionid");
		String city = userInfoJson.getString("city");//城市
		String headimgurl = userInfoJson.getString("headimgurl");//头像
		String province = userInfoJson.getString("province");//省份
		String openid = userInfoJson.getString("openid");//省份
		int sex = userInfoJson.getInt("sex");//性别
		findUser.setCity(city);
		findUser.setHeadimgurl(headimgurl);
		findUser.setUnionid(unionid);
		findUser.setNickName(EmojiUtil.resolveToByteFromEmoji(nickName)); //过滤掉特殊字符
		findUser.setProvince(province);
		findUser.setOpenid(openid);
		if(sex==1){
			findUser.setSex("0");
		}else if(sex==2){
			findUser.setSex("1");
		}else{
			findUser.setSex("0");
		}
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
				Zjcard zjcard = zjcardDao.selectByPrimaryKey(user.getId());
				if(zjcard==null){
					notifyUserLoginFail(out,"errorRefreshToken","微信refreshToken过期请重新授权");
					return;
				}
				user.setRoomCard(zjcard.getRoomcard());
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
		List<Map<String, Object>> userScoreList = userScoreDao.findUserSumScore(map);
		int totalUser = userScoreList.size();
		int sumScoreId = 0;
		if(userScoreList.size()>0){
			JSONObject outJsonObject = new JSONObject();
			JSONArray userScores = new JSONArray();
			JSONArray oneGame = new JSONArray();
			JSONObject object = new JSONObject();
			object.put("total", Integer.parseInt(userScoreList.get(0).get("total").toString()));
			object.put("roomNumber", Integer.parseInt(userScoreList.get(0).get("roomNumber").toString()));
			object.put("createDate", DateUtils.getFormatDate((Date)userScoreList.get(0).get("createDate"), "yyyy/MM/dd HH:mm:ss"));
			for (Map<String, Object> sumscore : userScoreList) {
				int userid = Integer.parseInt(sumscore.get("userid").toString());
				int finalScore = Integer.parseInt(sumscore.get("finalScore").toString());
				if(userid==user.getId()){
					sumScoreId = Integer.parseInt(sumscore.get("id").toString());
				}
				JSONObject everyScore = new JSONObject();
				everyScore.put("userid", userid);//用户id
				if(sumscore.get("nickName")!=null){
					everyScore.put("nickName", sumscore.get("nickName").toString());//昵称
				}else{
					everyScore.put("nickName", "");//昵称
				}
				everyScore.put("finalScore", finalScore);//总成绩
				everyScore.put("totalUser", totalUser);//房间人数
				oneGame.put(everyScore);
			}
			object.put("sumScoreId", sumScoreId);
			object.put("oneGame", oneGame);
			userScores.put(object);
			outJsonObject.put("userScores", userScores);
			outJsonObject.put("method", "getUserSumScore");
			outJsonObject.put("discription", "得到用户总结算的战绩");
			outJsonObject.put("type", "sumScore");
			logger.info(outJsonObject.toString());
			return outJsonObject;
		}else{
			logger.info("没有该用户的战绩");
			return null;
		}	
	}
	

	private void removeJsonkey(JSONObject p1) {
		p1.remove("total");
		p1.remove("roomNumber");
		p1.remove("createDate");
	}

	/**得到创建房间的最优服务器
	 * @param out
	 * @param userId 
	 * @param haoKa 
	 * @throws IOException 
	 * @throws DocumentException 
	 */
	public void beforeCreateRoom(ServletOutputStream out,String path, int userId, int haoKa) throws Exception {
		LinkedHashMap<Integer, Server> serverMaps = new LinkedHashMap<>();
		List<Server> serverList = XmlUtils.getServerList(path,zhenJiangServer);
		int all = 0;
		for(int i=0;i<serverList.size();i++){
			Server server = serverList.get(i);
			try {
				DateService dateService = RpcClient.getService(DateService.class, "com.zxz.service.DateServiceImpl",server.getIp(),server.getRpcPort());
				String totalOnlineUserAndTotalRoom = dateService.getTotalOnlineUserAndTotalRoom();
				int checkCard = dateService.checkCard(userId);
				all = all + checkCard;
				JSONObject jsonObject = new JSONObject(totalOnlineUserAndTotalRoom);
				int totalLoginUser = jsonObject.getInt("totalLoginUser");
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
		Map<Object, Object> findUser = userDao.findZjUserByUserId(userId);
		int roomCard = (int) findUser.get("roomCard");// 房卡的数量
		int zong = haoKa+all;
		if(roomCard>=zong){
			//可以创建房间
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
			outJsonObject.put("isSuccess", true);
			outJsonObject.put("description", "可以创建房间!");
			outJsonObject.put("bestServer", bestServer.getIp());
			outJsonObject.put("port", bestServer.getGamePort());
			out.write(outJsonObject.toString().getBytes("UTF-8"));
		}else{
			JSONObject outJsonObject = new JSONObject();
			outJsonObject.put("isSuccess", false);
			outJsonObject.put("description", "房卡不足,创建失败!");
			out.write(outJsonObject.toString().getBytes("UTF-8"));
		}
		
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
		int userId = serviceStr.getInt("userId");
		String server = null;
		int port = 0;//游戏服务器的端口号
		JSONObject outJsonObject = null;
		String roomInfo = RedisUtil.getKey("ftRoomId"+roomId,REDIS_DB_SANGUO);
		if(StringUtil.isNull(roomInfo)){//房间不存在
			//如果redis中没有房间信息则遍历所有的游戏服务器获取数据
			List<Server> serverList = XmlUtils.getServerList(realPath,zhenJiangServer);
			for(int i=0;i<serverList.size();i++){
				Server ser = serverList.get(i);
				DateService dateService = RpcClient.getService(DateService.class, "com.zxz.service.DateServiceImpl",ser.getIp(),ser.getRpcPort());
				boolean isHave = dateService.isRoomsHaveThisUser(userId);
				if(!isHave){
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
				}else{
					outJsonObject = new JSONObject();
					outJsonObject.put("code", "error");
					outJsonObject.put("method", "beforeEnterRoom");
					outJsonObject.put("description", "您已经在房间内了");
					return outJsonObject.toString();
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
			return outJsonObject2.toString();
		}else{
			return result;
		}
	}

	/**得到用户单局的战绩
	 * @param user
	 * @param serviceStr
	 * @return
	 */
	public JSONObject getUserScore(JSONObject serviceStr) {
		int roomId = serviceStr.getInt("roomId");
		int userid = serviceStr.getInt("userid");
		Map<String, Object> map = new HashMap<>();
		map.put("roomid", roomId);
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
			everyScore.put("roomid", roomid);
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
		JSONObject outJsonObject = new JSONObject();
		if(vedio==null){
			String createDate = DateUtils.getFormatDate(new Date(), "yyyy/MM/dd hh:mm:ss");
			outJsonObject.put("record", "");
			outJsonObject.put("createDate", createDate);
			return outJsonObject; 
		}
		String createDate = DateUtils.getFormatDate(vedio.getCreatedate(), "yyyy/MM/dd hh:mm:ss");
		outJsonObject.put("record", vedio.getRecord());
		outJsonObject.put("createDate", createDate);
		return outJsonObject;
	}

	/**检查用户是否绑定了代理
	 * @param serviceStr
	 * @return
	 */
	public Map<String, Object> checkIsBind(JSONObject serviceStr) {
		int userId = serviceStr.getInt("userid");
		UsersMapper usersMapper = new UsersMapper();
		usersMapper.setUserid(userId);
		Map<String, Object> map = new HashMap<>();
		map.put("userId",userId);
		Map<String, Object> queryMyClub = userMapperDao.queryMyClub(map);
		return queryMyClub;
	}

	public int bindOperateUser(JSONObject serviceStr) {
		int userid = serviceStr.getInt("userid");
		int clubId = serviceStr.getInt("clubId");
		//检测用户是否存在
		Zjcard zjcard = zjcardDao.selectByPrimaryKey(userid);
		if(zjcard==null){
			return -3;
		}
		//检测用户是否已经绑定
		UsersMapper usersMapper = new UsersMapper();
		usersMapper.setUserid(userid);
		UsersMapper userMapper = userMapperDao.findUsersMapperByCondition(usersMapper);
		if(userMapper!=null){
			return  -1;
		}
		//检测俱乐部ID是否存在
		OperateUser operateUser = operateUserDao.selectByPrimaryKey(clubId);
		if(operateUser==null){
			return -2;
		}
		usersMapper.setOperateUserId(clubId);
		usersMapper.setCreateDate(new Date());
		int insert = userMapperDao.insert(usersMapper);
		//修改用户的房卡数
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userid);
		int addUserCardByBangDingJuLeBu = userMapperDao.addUserCardByBangDingJuLeBu(map);
		return addUserCardByBangDingJuLeBu;
	}

	/**
	 * @param serviceStr
	 * @return
	 */
	public JSONObject getTimeElse(JSONObject serviceStr) {
		Map<String, Object> map = new HashMap<>();
		int userId = serviceStr.getInt("userid");
		map.put("userId",userId);
		Map<String, Object> queryMyClub = zjcardDao.getTimeElse(map);
		
		if(queryMyClub==null){
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("hElse", 0);
			jsonObject.put("mElse", 0);
			jsonObject.put("srElse", 0);
			return jsonObject;
		}else{
			String sdate = queryMyClub.get("createdate").toString();
			String time = sdate.substring(0, sdate.length()-2);
			
			DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");		
			Date date =null;
			try {
				date = sdf.parse(time);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			long last = date.getTime();
			if(System.currentTimeMillis()-last>24*3600*1000*2){
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("hElse", 0);
				jsonObject.put("mElse", 0);
				jsonObject.put("srElse", 0);
				return jsonObject;
			}else{
				Mydates dateInfo = getDateInfo(last);
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("hElse", dateInfo.getDays()*24+dateInfo.getHours());
				jsonObject.put("mElse", dateInfo.getMinutes());
				jsonObject.put("srElse", dateInfo.getSeconds());
				return jsonObject;
			}
		}
	}
	
	private Mydates getDateInfo(long milliseconds) {
        try {
            Date currDate = new Date(System.currentTimeMillis());
            Date endDate = new Date(milliseconds);
            long diff = endDate.getTime() - (currDate.getTime() - 24*3600*1000*2); // 得到的差值是微秒级别，可以忽略
            long days = diff / (1000 * 60 * 60 * 24);
            long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
            long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
            long seconds = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60) - minutes * (1000 * 60)) / (1000);
            Mydates mydates = new Mydates(days, hours, minutes, seconds);
            return mydates;
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return null;
    }
	
	class Mydates{
		long days;
		long hours;
		long minutes;
		long seconds;
		public Mydates(long days, long hours, long minutes, long seconds) {
			super();
			this.days = days;
			this.hours = hours;
			this.minutes = minutes;
			this.seconds = seconds;
		}
		public long getDays() {
			return days;
		}
		public void setDays(long days) {
			this.days = days;
		}
		public long getHours() {
			return hours;
		}
		public void setHours(long hours) {
			this.hours = hours;
		}
		public long getMinutes() {
			return minutes;
		}
		public void setMinutes(long minutes) {
			this.minutes = minutes;
		}
		public long getSeconds() {
			return seconds;
		}
		public void setSeconds(long seconds) {
			this.seconds = seconds;
		}
	}

	public JSONObject fenxiangchenggong(JSONObject serviceStr) {
		Map<String, Object> map = new HashMap<>();
		int userId = serviceStr.getInt("userid");
		map.put("userId",userId);
		int nowCard = zjcardDao.fenxiangchenggong(map);
		JSONObject outJsonObject = new JSONObject();
		outJsonObject.put("sgbNum", nowCard);
		return outJsonObject;
	}
	/**
	 * 得到房间列表
	 * @param path
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public JSONObject getRoomList(String path,int userId) throws Exception {
		JSONObject outJson = new JSONObject();
		outJson.put("method", "getRoomList"); 
		getRoomList(path, userId, outJson);
		return outJson;
	}
	/**
	 * 得到房间信息
	 * @param path
	 * @param userId
	 * @param outJson
	 * @throws DocumentException
	 * @throws ParseException
	 */
	private void getRoomList(String path, int userId, JSONObject outJson)
			throws DocumentException, ParseException {
		List<Server> serverList = XmlUtils.getServerList(path,zhenJiangServer);
		List<Room> roomLists = new ArrayList<>();
		for(int i=0;i<serverList.size();i++){
			Server server = serverList.get(i);
			DateService dateService = RpcClient.getService(DateService.class, "com.zxz.service.DateServiceImpl",server.getIp(),server.getRpcPort());
			String serverRoomList = dateService.getRoomList(userId);
			JSONArray roomArray = new JSONArray(serverRoomList);
			for(int j=0;j<roomArray.length();j++){
				String object = roomArray.get(j).toString();
				JSONObject oneRoomJson = new JSONObject(object);
				int roomId = oneRoomJson.getInt("roomId");//房间号
				int total = oneRoomJson.getInt("total");//房间的总人数
				String createDate = oneRoomJson.getString("createDate");//创建时间
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");  
				Date date = sdf.parse(createDate);
				int now = oneRoomJson.getInt("now");//现在的人数 
				Room room = new Room(roomId, date, now, total);
				roomLists.add(room);
			}
		}
		Collections.sort(roomLists);
		DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");	
		JSONArray roomArray = new JSONArray();
		for(int i=0;i<roomLists.size();i++){
			Room room = roomLists.get(i);
			JSONObject oneRoom = new JSONObject();
			oneRoom.put("roomId",room.roomId); 
			oneRoom.put("now",room.now); 
			oneRoom.put("total",room.total); 
			Date remainTime = room.remainTime;
			String format = sdf.format(remainTime);
			System.out.println(format);
			long time =1000*60*30 - (new Date().getTime() - room.remainTime.getTime());
            if(time>0&&room.now<room.total){
            	long days = time / (1000 * 60 * 60 * 24);
            	long hours = (time - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
            	long minutes = (time - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
            	long seconds = (time - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60) - minutes * (1000 * 60)) / (1000);
            	oneRoom.put("remainTimeH",0); 
            	oneRoom.put("remainTimeM",minutes); 
            	oneRoom.put("remainTimeS",seconds); 
            }else{
            	oneRoom.put("remainTimeH",0); 
            	oneRoom.put("remainTimeM",0); 
            	oneRoom.put("remainTimeS",0); 
            }
            
            roomArray.put(oneRoom);
		}
		outJson.put("rooms", roomArray);
	}

	
	public static class Room implements Comparable<Room>{
		public int roomId;
		public Date remainTime;
		public int now;
		public int total;
		public Room() {
		}
		
		public Room(int roomId, Date remainTime, int now, int total) {
			super();
			this.roomId = roomId;
			this.remainTime = remainTime;
			this.now = now;
			this.total = total;
		}

		@Override
		public int compareTo(Room o) {

			return 0;
		}
	}
	/**
	 * 创建房间
	 * @param realPath
	 * @param userId
	 * @return
	 * @throws DocumentException
	 * @throws ParseException
	 */
	public JSONObject checkCard(String realPath, int userId) throws DocumentException, ParseException {
		JSONObject outJson = new JSONObject();
		outJson.put("method", "checkCard"); 
		List<Server> serverList = XmlUtils.getServerList(realPath,zhenJiangServer);
		int all = 0 ;
		for(int i=0;i<serverList.size();i++){
			Server server = serverList.get(i);
			DateService dateService = RpcClient.getService(DateService.class, "com.zxz.service.DateServiceImpl",server.getIp(),server.getRpcPort());
			int checkCard = dateService.checkCard(userId);
			all = all +checkCard;
		}

		ZjcardDao zjcardDao = new ZjcardDaoImpl();
		Zjcard zjcard = zjcardDao.selectByPrimaryKey(new Integer(userId));
		Integer roomcard = zjcard.getRoomcard();
		roomcard = roomcard - all;
		outJson.put("roomCard", roomcard); 
		return outJson;
	}
	/**
	 * 代理创建房间
	 * @param realPath
	 * @param userId
	 * @param serviceStr 
	 * @return
	 * @throws DocumentException 
	 * @throws ParseException 
	 */
	public JSONObject daiLiCreateRoom(String realPath, int userId, JSONObject serviceStr) throws DocumentException, ParseException {
		JSONObject outJson = new JSONObject();
		outJson.put("m", "daiLiCreateRoom");
		List<Server> serverList = XmlUtils.getServerList(realPath,zhenJiangServer);
		int all = 0 ;
		for(int i=0;i<serverList.size();i++){
			Server server = serverList.get(i);
			DateService dateService = RpcClient.getService(DateService.class, "com.zxz.service.DateServiceImpl",server.getIp(),server.getRpcPort());
			int checkCard = dateService.checkCard(userId);
			all = all + checkCard;
		}
		ZjcardDao zjcardDao = new ZjcardDaoImpl();
		Zjcard zjcard = zjcardDao.selectByPrimaryKey(new Integer(userId));
		Integer roomcard = zjcard.getRoomcard();
		roomcard = roomcard - all;//减去空房间需要的房卡之后剩余的房卡数
		int needRoomCard = MINCARD;
		int quanNum = serviceStr.getInt("quanNum");//圈数
		if(quanNum>=16){
			needRoomCard = MAXCARD;
		}
		if(roomcard-needRoomCard<0){
			outJson.put("isSuccess", false);
			outJson.put("description", "房卡不足,创建空房间失败");
			return outJson;
		}
		Server bastServer = getBastServer(realPath);//得到最好的服务器
		DateService dateService = RpcClient.getService(DateService.class, "com.zxz.service.DateServiceImpl",bastServer.getIp(),bastServer.getRpcPort());
		int su = dateService.daiLiCreateRoom(serviceStr.toString());
		if(su==1){//创建成功
			outJson.put("isSuccess", true);
			outJson.put("description", "创建空房间成功");
			getRoomList(realPath, userId, outJson);
			return outJson;
		}else{
			outJson.put("isSuccess", false);
			outJson.put("description", "创建空房间失败");
			return outJson;
		}
	}
	
	/**
	 * 得到最好的服务器
	 * @param realPath
	 * @return
	 * @throws DocumentException
	 */
	private Server getBastServer(String realPath) throws DocumentException {
		LinkedHashMap<Integer, Server> serverMaps = new LinkedHashMap<>();
		List<Server> serverList = XmlUtils.getServerList(realPath,zhenJiangServer);
		for(int i=0;i<serverList.size();i++){
			Server server = serverList.get(i);
			try {
				DateService dateService = RpcClient.getService(DateService.class, "com.zxz.service.DateServiceImpl",server.getIp(),server.getRpcPort());
				String totalOnlineUserAndTotalRoom = dateService.getTotalOnlineUserAndTotalRoom();
				JSONObject jsonObject = new JSONObject(totalOnlineUserAndTotalRoom);
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
		Server bestServer = serverMaps.get(beserServerId);//得到最优服务器
		return bestServer;
	}
}
