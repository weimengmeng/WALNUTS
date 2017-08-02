package com.example.retrofit.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

public class StringUtil {
	/**
	 * 判断是否是手机号码
	 * 
	 * @param s
	 * @return
	 */

	public static boolean isPhoneNumber(String s) {
		return s != null && s.matches("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
	}

	/**
	 * 判断email格式
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isEmail(String s) {
		return s != null
				&& s.matches("^[a-z0-9-_]{1,30}@[a-z0-9-]{1,65}.[a-z]{2,3}(.[a-z]{2})?$");
	}

	/**
	 * 判断qq格式
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isQQ(String s) {
		return s != null && s.matches("[1-9][0-9]{4,}");
	}

	/**
	 * 判断用户名格式
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isName(String s) {
		return s != null && s.matches("^[a-zA-Z0-9_][a-zA-Z0-9_]{0,15}$");
	}

	/**
	 * 判断姓名格式
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isZName(String s) {
		return s != null && s.matches("^[\u4E00-\u9FA5]{2,4}$");
	}

	/**
	 * 判断密码格式
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isPass(String s) {
		return s != null && s.matches("^[a-zA-Z0-9_][a-zA-Z0-9_]{3,15}$");
	}

	/**
	 * 判断身份证格式
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isSfz(String s) {
		// return s != null
		// &&
		// s.matches("[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|[X|x])");
		boolean result = false;
		try {
			if (CheckIDCard.IDCardValidate(s.toUpperCase()).equals("该身份证有效！"))
				result = true;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public static boolean isCarNo(String s) {
		// return s != null &&
		// s.matches("[\u4e00-\u9fa5]{1}[A-Z]{1}[A-Z_0-9]{5}");
		return s != null
				&& s.matches("[京|沪|津|渝|冀|晋|蒙|辽|吉|黑|苏|浙|皖|闽|赣|鲁|豫|鄂|湘|粤|桂|琼|川|贵|滇|藏|陕|甘|宁|青|新|港|澳|台]{1}[A-Z]{1}[A-Z_0-9]{5}");
	}

	/**
	 * 判断字符串是否给空
	 */
	public static boolean isEmpty(String s) {
		return s == null || s.trim().length() <= 0;
	}

	/**
	 * 返回有效的字符串
	 * 
	 * @param s
	 * @param placeholder
	 * @return
	 */
	public static String getValid(String s, String placeholder) {
		return (s == null ? placeholder : s);
	}

	public static String secTostr(String secStr) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		SimpleDateFormat weekFormat = new SimpleDateFormat("EEEE");
		String result = "";
		long sec = Long.valueOf(secStr);
		Date date = new Date(sec);
		Date curDate = new Date();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(curDate);
		calendar.add(calendar.DATE, 1);
		Date tom1 = calendar.getTime();
		calendar.add(calendar.DATE, 1);
		Date tom2 = calendar.getTime();
		if (format.format(curDate).substring(0, 10)
				.equals(format.format(date).substring(0, 10))) {
			result = "今天(周" + weekFormat.format(curDate).replaceAll("星期", "")
					+ ")" + format.format(date).substring(11);
		} else if (format.format(tom1).substring(0, 10)
				.equals(format.format(date).substring(0, 10))) {
			result = "明天(周" + weekFormat.format(tom1).replaceAll("星期", "")
					+ ")" + format.format(date).substring(11);
		} else if (format.format(tom2).substring(0, 10)
				.equals(format.format(date).substring(0, 10))) {
			result = "后天(周" + weekFormat.format(tom2).replaceAll("星期", "")
					+ ")" + format.format(date).substring(11);
		} else {
			result = format.format(date);
		}
		return result;
	}

	public static long strTosec(String str) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		long result = 0;
		Date curDate = new Date();
		Calendar calendar = new GregorianCalendar();
		calendar.add(calendar.DATE, 1);
		Date tom1 = calendar.getTime();
		calendar.add(calendar.DATE, 1);
		Date tom2 = calendar.getTime();
		try {
			if (str.contains("今天")) {
				String temp = format.format(curDate).substring(0, 10) + " "
						+ str.substring(2);
				result = format.parse(temp).getTime();
			} else if (str.contains("明天")) {
				String temp = format.format(tom1).substring(0, 10) + " "
						+ str.substring(2);
				result = format.parse(temp).getTime();
			} else if (str.contains("后天")) {
				String temp = format.format(tom2).substring(0, 10) + " "
						+ str.substring(2);
				result = format.parse(temp).getTime();
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i("tiem", result + "");
		return result;
	}

	public static String getShortCarno(String carno) {
		return carno.subSequence(0, 2) + "***"
				+ carno.substring(carno.length() - 2, carno.length());
	}

	public static String getShortPhone(String phone) {
		if (phone.length() > 0)
			return phone.subSequence(0, 3) + "***"
					+ phone.substring(phone.length() - 3, phone.length());
		else
			return "";
	}

	public static String getNextHour() {
		// Date date = new Date();
		// int next_hour = (date.getHours() + 1) % 24;
		// return next_hour == 0 ? "明天" : "今天" + (next_hour < 10 ? "0" : "")
		// + next_hour + ":00";
		return "明天12:30";
	}

	public static String outputStr(float output) {
		if (output == 1.0)
			return "经济";
		else if (output == 1.5)
			return "舒适";
		else if (output == 2.0)
			return "商务";
		else if (output == 2.5)
			return "豪华";
		else
			return "经济";
	}

	public static String getMD5(String info) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(info.getBytes("UTF-8"));
			byte[] encryption = md5.digest();

			StringBuffer strBuf = new StringBuffer();
			for (int i = 0; i < encryption.length; i++) {
				if (Integer.toHexString(0xff & encryption[i]).length() == 1) {
					strBuf.append("0").append(
							Integer.toHexString(0xff & encryption[i]));
				} else {
					strBuf.append(Integer.toHexString(0xff & encryption[i]));
				}
			}

			return strBuf.toString();
		} catch (NoSuchAlgorithmException e) {
			return "";
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}
	 /** 
     * 电话号码验证 
     *  
     * @param  str 
     * @return 验证通过返回true 
     */  
    public static boolean isPhone(String str) {   
        Pattern p1 = null,p2 = null;  
        Matcher m = null;  
        boolean b = false;    
        p1 = Pattern.compile("^[0][1-9]{2,3}-[0-9]{5,10}$");  // 验证带区号的  
        p2 = Pattern.compile("^[1-9]{1}[0-9]{5,8}$");         // 验证没有区号的  
        if(str.length() >9)  
        {   m = p1.matcher(str);  
            b = m.matches();    
        }else{  
            m = p2.matcher(str);  
            b = m.matches();   
        }    
        return b;  
    }  
}
