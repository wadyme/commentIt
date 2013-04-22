package cn.ls.integrator.components.utils;

import java.util.Date;
/**
 * 
* @ClassName: CommonHelper 
* @Description: 公共方法工具类
* @author wanl
* @date 2011-4-28 上午09:45:10 
* @version V1.0
 */
public class CommonHelper {
	/**
	 * 
	 * @Title: Null2String
	 * @Description: 非空字符串处理
	 * @param value
	 *            值
	 * @return String 返回类型
	 * @throws
	 */
	public static String Null2String(Object value) {
		return (value == null || String.valueOf(value).equalsIgnoreCase("NULL")) ? ""
				: String.valueOf(value);
	}
	
	/**
	 * 
	 * @Title: Null2String
	 * @Description: 非空长整型处理
	 * @param value
	 *            值
	 * @return Long 返回类型
	 * @throws
	 */
	public static Long Null2Long(Object value) {
		long v = 0L;
		try {
			v = Long.parseLong(Null2String(value));
		} catch (Exception e) {
		}
		return v;
	}
	
	/**
	 * 
	 * @Title: Null2String
	 * @Description: 非空整型处理
	 * @param value
	 *            值
	 * @return int 返回类型
	 * @throws
	 */
	public static int Null2Int(Object value) {
		int v = 0;
		try {
			v = Integer.parseInt(Null2String(value));
		} catch (Exception e) {
		}
		return v;
	}
	
	/**
	 * 
	 * @Title: Null2String
	 * @Description: 非空布尔处理
	 * @param value
	 *            值
	 * @return boolean 返回类型
	 * @throws
	 */
	public static boolean Null2Boolean(Object value) {
		boolean v = false;
		try {
			v = Boolean.parseBoolean(Null2String(value));
		} catch (Exception e) {
		}
		return v;
	}
	
	/**
	 * 
	 * @Title: Null2Date
	 * @Description: 非空时间处理
	 * @param value
	 *            值
	 * @return Date 返回类型
	 * @throws
	 */
	public static Date Null2Date(Object value) {
		Date d = new Date();
		try {
			d = (Date) value;
		} catch (Exception e) {
		}
		return d;
	}
}
