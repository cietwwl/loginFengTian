package com.zxz.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

	/**寰楀埌鎸囧畾鏍煎紡鐨勬椂闂�
	 * @param date
	 * @param pattern  yyyy/MM/dd hh:mm:ss
	 * @return
	 */
	public static String getFormatDate(Date date,String pattern) {
		DateFormat sdf2 = new SimpleDateFormat(pattern);		
		String sdate = sdf2.format(date);
		return sdate;
	}

	
	/**
	 * @param sdate
	 * @param pattern yyyy/MM/dd HH:mm:ss
	 * @return
	 */
	public static Date stringToDate(String sdate,String pattern){
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Date date = null;
		try {
			date = sdf.parse(sdate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	/**得到明天
	 * @return
	 */
	public static Date getTommorrow(){
		Date date = new Date();
		long time = date.getTime();
		Date tom = new Date(time+3600*24*1000);
		return tom;
	}
	/**
	 * 得到游戏持续时间
	 * @param date
	 * @return
	 */
	public static String getTimeLast(Date dateStart,Date dateEnd) {
		 long time = dateEnd.getTime() - dateStart.getTime();
//		 long time = 1000;
	     long days = time / (1000 * 60 * 60 * 24);
	     long hours = (time - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
	     long minutes = (time - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
	     long seconds = (time - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60) - minutes * (1000 * 60)) / (1000);
         
		return days +"天 "+hours+":"+minutes+":"+seconds;
	}
}
