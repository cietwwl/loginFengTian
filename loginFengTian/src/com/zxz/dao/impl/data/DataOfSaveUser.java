package com.zxz.dao.impl.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.io.Writer;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Pattern;

import org.junit.Test;

import com.zxz.dao.impl.UserMapperImpl;
import com.zxz.domain.User;


public class DataOfSaveUser {
	
	static UserMapperImpl userMapperImpl = new UserMapperImpl();
	public static void main(String[] args) throws Exception{
		Pattern pattern = Pattern.compile("\\{^*}$");
		File readFile = new File("E:/日志分析/weiXinMing.txt");
		Reader fr = new FileReader(readFile);
		File writeFile = new File("E:/日志分析/resultWrite.txt");
		if (!writeFile.exists()) {
			writeFile.createNewFile();
		}
		Writer fw = new FileWriter(writeFile);
		BufferedReader br = new BufferedReader(fr);
		BufferedWriter bw = new BufferedWriter(fw);
		String str = br.readLine();
		int totalRegist = 50;
		for(int i=0;i<totalRegist;i++){
			bw.write(str);
			bw.newLine();
			str = br.readLine();
			System.out.println(str);
			User user= new User();
			user.setOpenid("odfaww"+getOrderIdByUUId());
			user.setNickName(str);
			user.setSex(getSex());
			user.setProvince("Hunan");
			user.setCity("Yueyang");
			user.setUnionid("obhqFx"+getOrderIdByUUId());
			user.setRefreshtoken(getOrderIdByUUId()+getOrderIdByUUId());
			user.setCreatedate(getRegistDate(totalRegist));
			user.setRoomCard((int)(Math.random()*10));
			user.setUt(1);
			user.setHeadimgurl(UserImages.getHeadImage());
			userMapperImpl.insert(user);
			str = br.readLine();
		}
		br.close();
		bw.close();
		fr.close();
		fw.close();
	}

	
	/**
	 * @param sbeginDate ��ʼʱ��
	 * @param totalRegist �ܵ�ע������
	 * @return
	 */
	public static Date getRegistDate(int totalRegist){
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
		try {
			User selectLastRegistUser = userMapperImpl.selectLastRegist();
			Date begeinData = selectLastRegistUser.getCreatedate();
			System.out.println("最后注册的用户是:"+selectLastRegistUser.getNickName()+"注册时间:"+selectLastRegistUser.getCreatedate());
			long time = begeinData.getTime();
			int registDataInterval = getRegistDataInterval(totalRegist);
			Date nextRegistDate= new Date(time+registDataInterval);
			return nextRegistDate;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	/**注册时间间隔
	 * @param hours
	 * @param totalRegist
	 * @return
	 */
	public static int getRegistDataInterval(int totalRegist){
		int i = (int)((totalRegist/24));
		int evey = 3600/i;
		int interval =evey-100+(int)(Math.random()*100);
		return interval*1000;
	}
	
	
	public static String getOrderIdByUUId() {
        return  UUID.randomUUID().toString();
    }
	
	
	/**�õ��Ա�
	 * @return
	 */
	public static String getSex(){
		Random r = new Random();
		int nextInt = r.nextInt(2);
		return nextInt+"";
	}
	
	@Test
	public  void testSex() {
		for(int i=0;i<100;i++){
			String sex = getSex();
			System.out.println(sex);
		}
	}
	
	
	@Test
	public  void testUUID() {
		String orderIdByUUId = getOrderIdByUUId();
		System.out.println(orderIdByUUId);
	}
	
	
	@Test
	public  void testSave() {
		UserMapperImpl mapperImpl = new UserMapperImpl();
		User u= new User();
		u.setUserName("t");
		u.setPassword("dddd");
//		User findUser = findUser(u);
//		u.setRoomcard(10000);
		mapperImpl.insert(u);
	}
	
	@Test
	public void save() throws Exception{
		Pattern pattern = Pattern.compile("\\{^*}$");
		// File readFile = new File("E:/��־����/��������.txt");
		File readFile = new File("E:/��־����/΢���ǳ�.txt");
		Reader fr = new FileReader(readFile);
		File writeFile = new File("E:/��־����/weiXinMing.txt");
		if (!writeFile.exists()) {
			writeFile.createNewFile();
		}
		Writer fw = new FileWriter(writeFile);
		BufferedReader br = new BufferedReader(fr);
		BufferedWriter bw = new BufferedWriter(fw);
		String str = br.readLine();
		while (str != null) {
			if (!str.trim().equals("")&&!str.contains("tr")&&!str.contains("td")&&!str.contains("</")) {
					String[] strings = str.split(" ");
					System.out.println(strings[0]);
					bw.write(strings[0]);
					bw.newLine();
			}
			// �ٶ�һ��
			str = br.readLine();
		}
		// �ر��ַ����������ַ������
		br.close();
		bw.close();
		fr.close();
		fw.close();
	}
	
	
}
