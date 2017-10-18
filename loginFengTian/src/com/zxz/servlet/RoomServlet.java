package com.zxz.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zxz.service.RoomService;
import com.zxz.util.PageUtil;
import com.zxz.util.StringUtil;

public class RoomServlet extends BaseServlet {

	RoomService roomService = new RoomService();
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		super.doPost(request, response);
	}

	@Override
	public void receiveMessage(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		switch (method) {
		case "roomList":
			roomList(request,response);//房间列表
			break;
		case "createBuyMessage":
			createBuyMessage(request,response);//跳转创建购买房卡的通知
			break;
		case "createBuyCardMessage":
			createBuyCardMessage(request,response);//创建购买房卡的通知
			break;
		case "buyMessageList":
			buyMessageList(request,response);//购买房卡的通知的列表
			break;
		case "statics":
			statics(request,response);//购买房卡的通知的列表
			break;
		case "disbanRoom":
			disbanRoom(request,response);//购买房卡的通知的列表
			break;
		case "toDisbanRoom":
			toDisbanRoom(request,response);//跳转到去解散房间的界面
			break;
		default:
			break;
		}
	}

	/**去解散房间
	 * @param request
	 * @param response
	 */
	private void toDisbanRoom(HttpServletRequest request,
			HttpServletResponse response) {
		forward("/opreate/disbanRoom.jsp");
	}

	/**解散房间
	 * @param request
	 * @param response
	 */
	private void disbanRoom(HttpServletRequest request,
			HttpServletResponse response) {
		String roomNumber = getParameter("roomNumber");
		roomService.disbanRoom(roomNumber);
		forward("/opreate/disbanRoom.jsp");
	}

	/**房卡统计信息 当月所有代理充值卡数 、当月所有代理余卡 、当月所有赠送卡数 、当月实际消费总卡数 、当月所有用户余卡
	 * @param request
	 * @param response
	 */
	private void statics(HttpServletRequest request,
			HttpServletResponse response) {
		int chargeTotal = roomService.findAllOperateChargeTotal();//充值总数
		int remainCardTotal = roomService.findAllOperateRemainCardTotal();//余卡总数 
		int sendCardTotal = roomService.findAllOperateSendCardTotal();//赠送总数 
		int consumeTotal = roomService.findAllOperateConsumeTotal();//当月实际消费总卡数
		int userCardTotal = roomService.findAllUserCardTotal();//用户余卡数
		setAttribute("chargeTotal", chargeTotal);
		setAttribute("remainCardTotal", remainCardTotal);
		setAttribute("sendCardTotal", sendCardTotal);
		setAttribute("consumeTotal", consumeTotal);
		setAttribute("userCardTotal", userCardTotal);
		forward("/manager/roomCardStatics.jsp");
	}

	/**购买房卡通知的列表
	 * @param request
	 * @param response
	 */
	private void buyMessageList(HttpServletRequest request,
			HttpServletResponse response) {
		PageUtil<Map<String,Object>> pageUtil = getObjectPageUtil();
		roomService.buyMessageList(pageUtil);
		setAttribute("pageUtil", pageUtil);
		forward("/user/messgaeList.jsp");
	}

	/**创建购买房卡的通知
	 * @param request
	 * @param response
	 */
	private void createBuyCardMessage(HttpServletRequest request,
			HttpServletResponse response) {
		String message = getParameter("message");
		if(StringUtil.isNull(message)){
			forward("/user/createMessage.jsp");
			setAttribute("error", "消息不能为空");
			return;
		}
		roomService.saveMessage(message);
		//sendRedirect("/user/messgaeList.jsp");
	}

	/**跳转创建购买房卡的消息
	 * @param request
	 * @param response
	 */
	private void createBuyMessage(HttpServletRequest request,
			HttpServletResponse response) {
		forward("/user/createMessage.jsp");
	}

	/**查询房间
	 * @param request
	 * @param response
	 */
	private void roomList(HttpServletRequest request,
			HttpServletResponse response) {
		int index = getIndex();
		PageUtil<Map<String,Object>> pageUtil = new PageUtil<>();
		pageUtil.setIndex(index);
		String userIdOrNick = getParameter("userIdOrNick");
		String roomNumber = getParameter("roomNumber");
		String beginDate = getParameter("beginDate");
		String endDate = getParameter("endDate");
		String createDate = getParameter("createDate");//创建排序
		if(StringUtil.isNull(createDate)){
			createDate = "desc";
		}
		roomService.roomListMap(pageUtil,userIdOrNick,roomNumber,beginDate,endDate,createDate);
		setAttribute("pageUtil", pageUtil);
		setAttribute("roomNumber", roomNumber);
		setAttribute("beginDate", beginDate);
		setAttribute("endDate", endDate);
		setAttribute("createDate", createDate);
		setAttribute("userIdOrNick", userIdOrNick);
		forward("/opreate/roomList.jsp");
	}

}
