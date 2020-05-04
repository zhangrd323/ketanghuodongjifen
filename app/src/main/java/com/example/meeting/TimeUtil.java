package com.example.meeting;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {

	//日期转字符串
	public static String DateToStr(Date date) {
	SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss");
	String str = format.format(date);
	return str;
	}
	public static String DateToStr2(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str = format.format(date);
		return str;
		}
	public static String DateToStr7(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
		String str = format.format(date);
		return str;
	}
	public static String DateToStr8(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String str = format.format(date);
		return str;
	}
	public static String DateToStr3(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String str = format.format(date);
		return str;
	}
	public static String DateToStr4(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm");
		String str = format.format(date);
		return str;
	}

    //获取当前时间
	public static String getCurrentTime(long date) {
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	String str = format.format(new Date(date));
	return str;
	}

	//获取当前年月
	public static String getCurrentYm(long date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
		String str = format.format(new Date(date));
		return str;
	}

   //字符串转日期
	public static Date StrToDate(String str) {
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Date date = null;
	try {
	date = format.parse(str);
	} catch (ParseException e) {
	e.printStackTrace();
	}
	return date;
	}

	public static  Date str2Data(String str){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = format.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	//字符串转日期
	public static Date StrToDateYm(String str) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = format.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 判断2个时间大小
	 * yyyy-MM-dd HH:mm 格式（自己可以修改成想要的时间格式）
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static int getTimeCompareSize(String startTime, String endTime){
		int i=0;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");//年-月-日 时-分
		try {
			Date date1 = dateFormat.parse(startTime);//开始时间
			Date date2 = dateFormat.parse(endTime);//结束时间
			// 1 结束时间小于开始时间 2 开始时间与结束时间相同 3 结束时间大于开始时间
			if (date2.getTime()<date1.getTime()){
				i= 1;
			}else if (date2.getTime()==date1.getTime()){
				i= 2;
			}else if (date2.getTime()>date1.getTime()){
				//正常情况下的逻辑操作.
				i= 3;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return  i;
	}

}
