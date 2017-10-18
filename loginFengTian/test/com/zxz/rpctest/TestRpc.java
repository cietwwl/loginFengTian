package com.zxz.rpctest;

import org.junit.Test;

import com.zxz.rpcclient.RpcClient;
import com.zxz.service.DateService;
import com.zxz.service.HelloService;

public class TestRpc {

	public static void main(String[] args) throws Exception {
//		//testService();
//		DateService service = RpcClient.refer(DateService.class,"com.zxz.service.DateServiceImpl","");
//		int totalOneLineUser = service.getTotalOneLineUser();
//		System.out.println(":" + totalOneLineUser);
	}

	@Test
	private static void testService() throws Exception {
//		HelloService service = RpcClient.refer(HelloService.class,"com.zxz.service.HelloServiceImpl","");
//		String result = service.hello("gushuang");
//		System.out.println("result:" + result);
	}

}
