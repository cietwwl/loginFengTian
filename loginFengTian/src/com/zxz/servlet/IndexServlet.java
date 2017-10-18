package com.zxz.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.zxz.domain.OperateUser;
import com.zxz.domain.Room;
import com.zxz.rpcclient.RpcClient;
import com.zxz.service.DateService;
import com.zxz.service.IndexService;

public class IndexServlet extends HttpServlet {

	IndexService indexService = new IndexService();
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//forwordOrld(request, response);
		OperateUser operateUser  = (OperateUser) request.getSession().getAttribute("user");
		if(operateUser.getPid()==1){//管理员登录
			setData(request);
			request.getRequestDispatcher("/user/index.jsp").forward(request, response);
		}else if(operateUser.getPid()==0){//代理登录
			request.getRequestDispatcher("/center/index.jsp").forward(request, response);
		}
	}

	private void setData(HttpServletRequest request)  {
		setFenQu1(request);
		setFenQu2(request);
	}

	private void setFenQu2(HttpServletRequest request) {
		int totalOneLineUser2 = indexService.getTotalOneLineUser2();
		int totalLoginLineUser2 = indexService.getTotalLoginLineUser2();
		List<Room> roomList2 = indexService.getRoomList2();
		request.setAttribute("totalOneLineUser2", totalOneLineUser2);
		request.setAttribute("totalLoginLineUser2", totalLoginLineUser2);
		request.setAttribute("roomList2", roomList2);
	}

	private void setFenQu1(HttpServletRequest request) {
		int totalOneLineUser = indexService.getTotalOneLineUser1();
		int totalLoginLineUser = indexService.getTotalLoginLineUser1();
		List<Room> roomList1 = indexService.getRoomList1();
		int todayTotal = indexService.getTodayTotalRegist();//得到今日注册数
		int consumeTotal = indexService.getTodayConsumeTotal();//得到今日房卡消耗数
	/*	List<Room> falseRoomList = indexService.getFalseRoomList();
		request.setAttribute("falseRoomList", falseRoomList);*/
		request.setAttribute("consumeTotal", consumeTotal);
		request.setAttribute("todayTotal", todayTotal);
		request.setAttribute("totalOneLineUser", totalOneLineUser);
		request.setAttribute("totalLoginLineUser", totalLoginLineUser);
		request.setAttribute("roomList", roomList1);
	}

}
