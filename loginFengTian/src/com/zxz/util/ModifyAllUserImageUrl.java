package com.zxz.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.zxz.dao.UserMapper;
import com.zxz.dao.impl.UserMapperImpl;
import com.zxz.domain.User;

public class ModifyAllUserImageUrl {

	
	public static void main(String[] args) {
		UserMapper mapper = new UserMapperImpl();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rowStart", 0);
		map.put("pageSize", 100);
		List<User> userList = mapper.selectUserList(map);
		for (User user : userList) {
			try {
				System.out.println(user.getNickName()+"\t" +user.getRefreshtoken());
				String accessToken = WeiXinUtil.getAccessTokenWithRefreshToken(user.getRefreshtoken());
				//System.out.println(accessToken);
				JSONObject userInfo = WeiXinUtil.getUserInfo(accessToken, user.getOpenid());
//				if(user.getHeadimgurl()==null||"".equals(user.getHeadimgurl())){
//					User modifyUser = new User();
//					modifyUser.setId(user.getId());
//					modifyUser.setHeadimgurl(userInfo.getString("headimgurl"));
//					int updateByPrimaryKeySelective = mapper.updateByPrimaryKeySelective(modifyUser);
//					System.out.println("updateByPrimaryKeySelective:"+updateByPrimaryKeySelective);
//				}
				System.out.println(userInfo);
			} catch (Exception e) {
				System.err.println(user.getId()+" "+user.getUserName());//输出红色的字
				//e.printStackTrace();
			}
		}
		//System.out.println(userList.size());
	}
	
}
