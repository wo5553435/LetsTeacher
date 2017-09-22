package com.example.sinner.letsteacher.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	
	/**
	 * 接受yyyy年MM月dd日的日期字符串参数,返回两个日期相差的天数
	 * @param start
	 * @param end
	 * @return
	 * @throws ParseException
	 */
	public static long getDistDates(String start, String end){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
		Date startDate;
		Date endDate;
		try {
			startDate = sdf.parse(start);
			endDate = sdf.parse(end);
			return getDistDates(startDate, endDate);
		} catch (ParseException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * 返回两个日期相差的天数
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws ParseException
	 */
	public static long getDistDates(Date startDate, Date endDate) {
		long totalDate = 0;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDate);
		long timestart = calendar.getTimeInMillis();
		calendar.setTime(endDate);
		long timeend = calendar.getTimeInMillis();
		totalDate = Math.abs((timeend - timestart)) / (1000 * 60 * 60 * 24);
		return totalDate;
	}
	/**
	 * 接受yyyy年MM月dd日的日期字符串参数
	 * @param start
	 * @param end
	 * @return
	 * @throws ParseException
	 */
	public static int compareTo(String start, String end){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
		Date startDate;
		Date endDate;
		try {
			startDate = sdf.parse(start);
			endDate = sdf.parse(end);
			return startDate.compareTo(endDate);
		} catch (ParseException e) {
			e.printStackTrace();
			return -1;
		}
	}

	/**
	 * 智能化计算任意两天相距天数 ，已每天的凌晨0:点为单位为一天
	 * @param start 开始时间
	 * @param end 结束时间
     * @return 天数
     */
	public static int comareToDaydiff(String start,String end ){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar=Calendar.getInstance();
		Date startDate = null;
		Date RealstartDate;
		Date endDate;
		try {
			startDate = sdf.parse(start);
			endDate = sdf.parse(end);
			calendar.setTime(startDate);
			calendar.add(Calendar.DAY_OF_MONTH,1);
			RealstartDate=calendar.getTime();
			String RealtimeStr=sdf.format(RealstartDate);
			RealstartDate=sdf.parse(RealtimeStr);
			calendar.setTime(RealstartDate);
			long starttime=calendar.getTimeInMillis();
			calendar.setTime(endDate);
			long endttime=calendar.getTimeInMillis();
			double totalDate = Math.ceil(Math.abs((endttime - starttime)) / (1000.0f * 60 * 60 * 24));
			return (int)totalDate+1;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}
}
