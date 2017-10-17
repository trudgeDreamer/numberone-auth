package com.numberONe.util;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;


public class OrderUtil {

	private OrderUtil() {

	}

	/**
	 * 返回指定时间字符串格式：yyyy-MM-dd
	 * 
	 * @param dateDate
	 * @return
	 */
	public static String dateToStr(Date dateDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(dateDate);
		return dateString;
	}

	/**
	 * 返回当前时间字符串格式：yyyy-MM-dd HH:mm:ss
	 * 
	 * @return
	 */
	public static String dateToStr() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(new Date());
		return dateString;
	}

	public static String dateToStr(Date dateDate, String dateFormat) {
		if (null == dateFormat || dateFormat.length() == 0)
			dateFormat = "yyyy-MM-dd HH:mm:ss";
		if (dateDate == null)
			dateDate = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
		String dateString = formatter.format(dateDate);
		return dateString;
	}

	public static String dateToStr(String dateFormat) {
		if (null == dateFormat || dateFormat.length() == 0)
			dateFormat = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
		String dateString = formatter.format(new Date());
		return dateString;
	}


	/**
	 * 获取不为空的字符串
	 * 
	 * @param src
	 * @param trim
	 * @return
	 */
	public static String getNotNullStr(Object src, boolean trim) {
		if (null == src) {
			return "";
		} else {
			if (trim) {
				return src.toString().trim();
			} else {
				return src.toString();
			}
		}
	}

	public final static int stringToInt(String str, int defaultValue) {
		if (str != null) {
			try {
				return Integer.parseInt(str);
			} catch (Exception e) {
				return defaultValue;
			}
		} else {
			return defaultValue;
		}
	}

	/**
	 * 得到现在小时分钟
	 */
	public static int getHhMm() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		int hhmm = Integer.parseInt(dateString.substring(11, 16).replace(":",
				""));
		return hhmm;
	}

	/**
	 * 前一天时间
	 * 
	 * @param datetime
	 * @return
	 */
	public static String getStepDate(String datetime) {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			c.setTime(dateFormat.parse(datetime));
			int day = c.get(Calendar.DATE);
			c.set(Calendar.DATE, day - 1);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dateFormat.format(c.getTime());
	}

	/**
	 * 后一天时间
	 * 
	 * @param datetime
	 * @return
	 */
	public static String getNextDate(String datetime) {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			c.setTime(dateFormat.parse(datetime));
			int day = c.get(Calendar.DATE);
			c.set(Calendar.DATE, day + 1);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dateFormat.format(c.getTime());
	}

	public static String getNotNullStr(String src, boolean trim) {
		if (null == src || src.trim().equals("") || src.trim().equals("null")) {
			return "";
		} else {
			if (trim) {
				return src.trim();
			} else {
				return src;
			}
		}
	}

	/**
	 * 整理时间(补全缺少的0)
	 * 
	 * @param formatStr
	 * @return
	 */
	public static String tidyTime(String formatStr) {
		String dateString = null;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		if (null != formatStr && !"".equals(formatStr)) {
			try {
				Date form = formatter.parse(formatStr);
				dateString = formatter.format(form);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return dateString;

	}

	public static int getStringtoInt(String str) {
		int in = 0;
		try {
			in = Integer.parseInt(str);
			return in;
		} catch (Exception e) {
			return in;
		}
	}

	public static int getObjecttoInt(Object str) {
		int in = 0;
		try {
			in = Integer.parseInt(str + "");
			return in;
		} catch (Exception e) {
			return in;
		}
	}

	public static String getRemortIP(HttpServletRequest request) {
		if (request.getHeader("x-forwarded-for") == null) {
			return request.getRemoteAddr();
		}
		return request.getHeader("x-forwarded-for");
	}

	/**
	 * 读取文件
	 * 
	 * @param url
	 * @return
	 */
	public static String ReadDate(String url) {
		StringBuffer sb = new StringBuffer();
		FileInputStream fr = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader read = null;
		try {
			fr = new FileInputStream(url);
			inputStreamReader = new InputStreamReader(fr, "UTF-8");
			read = new BufferedReader(inputStreamReader);
			char ch[] = new char[1024];
			int d = read.read(ch);
			while (d != -1) {
				String str = new String(ch, 0, d);
				sb.append(str);
				d = read.read(ch);
			}
			return sb.toString();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (read != null) {
					read.close();
				}
				if (inputStreamReader != null) {
					inputStreamReader.close();
				}
				if (fr != null) {
					fr.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 写入文件
	 * 
	 * @param url
	 * @param content
	 */
	public static int WriteDate(String url, String content) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(url, false);// 两个参数，true表示在文件末尾追加
			fos.write(content.getBytes());
			return 0;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if(fos != null){
					fos.close();// 流要及时关闭
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return -1;
	}

	/**
	 * 获取19位订单号
	 * 
	 * @return
	 */
	public static String getOrderId() {
		StringBuffer orderId = new StringBuffer();
		Calendar Cld = Calendar.getInstance();
		int YY = Cld.get(Calendar.YEAR);
		int MM = Cld.get(Calendar.MONTH) + 1;
		int DD = Cld.get(Calendar.DATE);
		int HH = Cld.get(Calendar.HOUR_OF_DAY);
		int mm = Cld.get(Calendar.MINUTE);
		int SS = Cld.get(Calendar.SECOND);
		int MI = Cld.get(Calendar.MILLISECOND);

		orderId.append(YY);
		if (MM < 10)
			orderId.append("0" + MM);
		else
			orderId.append(MM);
		if (DD < 10)
			orderId.append("0" + DD);
		else
			orderId.append(DD);
		if (HH < 10)
			orderId.append("0" + HH);
		else
			orderId.append(HH);
		if (mm < 10)
			orderId.append("0" + mm);
		else
			orderId.append(mm);
		if (SS < 10)
			orderId.append("0" + SS);
		else
			orderId.append(SS);
		orderId.append(MI);
		orderId.append(getRandom(6));
		return orderId.toString();
	}

	/**
	 * 获取随机数
	 * 
	 * @param digit
	 * @return
	 */
	public static String getRandom(int digit) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < digit; i++) {
			int random = new Random().nextInt(10);
			sb.append(random);
		}
		return sb.toString();
	}

	/**
	 * 判断字符串是否为空，null或者""都会返回false，其他情况返回true
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		if (str != null && str.trim().length() > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 返回指定日期的开始时间:例如2014-12-12 的开始时间为:2014-12-12 00:00:00 000
	 * 
	 * @param date
	 * @return
	 */
	public static Date getDateBeginTime(Date date) {
		if (date == null) {
			return null;
		}
		Calendar beginTime = Calendar.getInstance();
		beginTime.setTime(date);
		beginTime.set(Calendar.HOUR_OF_DAY, 0);
		beginTime.set(Calendar.MINUTE, 0);
		beginTime.set(Calendar.SECOND, 0);
		beginTime.set(Calendar.MILLISECOND, 0);
		return beginTime.getTime();
	}

	/**
	 * 返回指定日期的结束时间:例如2014-12-12 的结束时间为:2014-12-12 23:59:59 999
	 * 
	 * @param date
	 * @return
	 */
	public static Date getDateEndTime(Date date) {
		if (date == null) {
			return null;
		}
		Calendar endTime = Calendar.getInstance();
		endTime.setTime(date);
		endTime.set(Calendar.HOUR_OF_DAY, 23);
		endTime.set(Calendar.MINUTE, 59);
		endTime.set(Calendar.SECOND, 59);
		endTime.set(Calendar.MILLISECOND, 999);
		return endTime.getTime();
	}

	/**
	 * 获得昨日的日期
	 * 
	 * @return
	 */
	public static Date getYestday() {
		Calendar today = Calendar.getInstance();
		int day_of_year = today.get(Calendar.DAY_OF_YEAR);
		int yesterday_of_year = day_of_year - 1;
		today.set(Calendar.DAY_OF_YEAR, yesterday_of_year);
		Date yesterday = today.getTime();
		return yesterday;
	}

	public static String getCustomDate(int num) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, num);
		String dateString = dateFormat.format(c.getTime());
		return dateString;
	}

	public static void main(String[] args) {
		// String date =
		// ReadDate("F:\\resin-3.0.26\\webapps\\filmCritics\\upload\\o-w-txt.txt");
		// System.out.println(date);
		// String[] str = date.split(";");
		// for (int i = 0; i < str.length; i++) {
		// System.out.println(str[i]);
		// }
		System.out.println(getOrderId());


	}
}
