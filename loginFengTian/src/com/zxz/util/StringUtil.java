package com.zxz.util;

import java.util.regex.Pattern;

public class StringUtil {

	/**判断字符串是否为空
	 * @param str
	 * @return
	 */
	public static boolean isNull(String str){
		if(str==null||"".equals(str)){
			return true;
		}
		return false;
	}
	
	
	/**得到int
	 * @param str
	 * @return
	 */
	public static int getInt(String str){
		try {
			return Integer.parseInt(str);
		} catch (NumberFormatException e) {
			return -1;
		}
	}
	
	/**判断数字是否为空
	 * @return
	 */
	public static boolean integerIsEmpty(Integer integer){
		if(integer==null){
			return true;
		}
		
		if(integer == 0){
			return true;
		}
		
		return false;
	}
	
	
	/**判断是否有一个不为空
	 * @param strings
	 * @return
	 */
	public static boolean isNotAllEmpty(String ...strings){
		for(int i=0;i<strings.length;i++){
			boolean isNull = isNull(strings[i]);
			if(!isNull){
				return true;
			}
		}
		return false;
	}
	
	
	/**判断是否是数字
	 * @param str
	 * @return
	 */
	public static boolean isNumber(String str){
		boolean isNumber = Pattern.matches(RegexUtil.NUMBERS, str);//判读是否是数字
		if(isNumber){
			return true;
		}else{
			return false;
		}
	}
	
}
