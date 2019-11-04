package com.che.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * 日期,时间工具类
 */
public class MyDateUtils {

	public final static String DATE_FORMAT = "yyyy-MM-dd";
	
	public final static String DATE_FORMAT_NUMB = "yyyyMMdd";
	
	public final static String DATETIME_FORMAT = "yyyy-MM-dd HH:mm";
	public static String DATETIME_LONG = "yyyyMMddHHmmss";
	/** 日期 */
	public final static String DEFAILT_DATE_PATTERN = "yyyy-MM-dd";

	/** 日期时间 */
	public final static String DEFAILT_DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

	/** 时间 */
	public final static String DEFAULT_TIME_PATTERN = "HH:mm:ss";
	
	public final static String DATETIME_FORMAT_YYYYMMDDHHMMSSSSS = "yyyyMMddHHmmssSSS";
	
	public final static String DATETIME_FORMAT_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
	
	public final static String DATETIME_FORMAT_CHINA = "yyyy年MM月dd日";

	/**
	 * 每天的毫秒数
	 */
	public final static long MILLIS_IN_DAY = 1000 * 60 * 60 * 24;

	/**
	 * 获取当前时间到毫秒
	 * @return
	 */
	public static Long getTimeMillis(){
		return Long.parseLong(new SimpleDateFormat(DATETIME_FORMAT_YYYYMMDDHHMMSSSSS).format(new Date()));
	}
	
	
	public static String trans(String data,String patternSource,String patternTarget){
		String trans = null;
		SimpleDateFormat source = new SimpleDateFormat(patternSource);
		try {
			Date day = source.parse(data);
			SimpleDateFormat format = new SimpleDateFormat(patternTarget);
			trans = format.format(day);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return trans;
	}
	
	/**
	 * 加减天数
	 * 
	 * @param num
	 * @param Date
	 * @return
	 */
	public static Date addDay(int num, Date Date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(Date);
		calendar.add(Calendar.DATE, num);// 把日期往后增加 num 天.整数往后推,负数往前移动
		return calendar.getTime(); // 这个时间就是日期往后推一天的结果
	}

	/**
	 * 比较时间date1是否在时间date2之前 时间格式 2008-11-25 16:30:10
	 * 
	 * @param date1
	 * @param date2
	 * @return boolean; true:在date2之前
	 * @author 胡建国
	 */
	public static boolean beforeDate(String date1, String date2) {
		try {
			Date d1 = convertString2Date(DEFAILT_DATE_PATTERN, date1);
			Date d2 = convertString2Date(DEFAILT_DATE_PATTERN, date2);
			return d1.before(d2);
		} catch (ParseException e) {
			return false;
		}
	}
	/**
	 * 比较日期大小
	 * 
	 * @param src
	 * @param src
	 * @return int; 1:DATE1>DATE2;
	 */
	public static int compare_date(Date src, Date src1) {

		String date1 = convertDate2String(DEFAILT_DATE_TIME_PATTERN, src);
		String date2 = convertDate2String(DEFAILT_DATE_TIME_PATTERN, src1);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date dt1 = df.parse(date1);
			Date dt2 = df.parse(date2);
			if (dt1.getTime() > dt2.getTime()) {
				return 1;
			} else if (dt1.getTime() < dt2.getTime()) {
				return -1;
			} else {
				return 0;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return 0;
	}
	/**
	 * 转换日期,得到默认日期格式字符串
	 * 
	 * @param targetDate
	 * @return
	 */
	public static String convertDate2String(Date targetDate) {
		return convertDate2String(DEFAILT_DATE_PATTERN, targetDate);
	}
	/**
	 * 转换日期得到指定格式的日期字符串
	 * 
	 * @param formatString
	 *            需要把目标日期格式化什么样子的格式。例如,yyyy-MM-dd HH:mm:ss
	 * @param targetDate
	 *            目标日期
	 * @return
	 */
	public static String convertDate2String(String formatString, Date targetDate) {
		SimpleDateFormat format = null;
		String result = null;
		if (targetDate != null) {
			format = new SimpleDateFormat(formatString);
			result = format.format(targetDate);
		} else {
			return null;
		}
		return result;
	}

	/**
	 * 转换字符串得到默认格式的日期类型
	 * 
	 * @param strDate
	 * @return
	 * @throws ParseException
	 */
	public static Date convertString2Date(String strDate) throws ParseException {
		Date result = null;
		try {
			result = convertString2Date(DEFAILT_DATE_PATTERN, strDate);
		} catch (ParseException pe) {
			throw new ParseException(pe.getMessage(), pe.getErrorOffset());
		}
		return result;
	}

	/**
	 * 转换日期字符串得到指定格式的日期类型
	 * 
	 * @param formatString
	 *            需要转换的格式字符串
	 * @param targetDate
	 *            需要转换的时间
	 * @return
	 * @throws ParseException
	 */
	public static final Date convertString2Date(String formatString,
			String targetDate) throws ParseException {
		if (StringUtils.isBlank(targetDate))
			return null;
		SimpleDateFormat format = null;
		Date result = null;
		format = new SimpleDateFormat(formatString);
		try {
			result = format.parse(targetDate);
		} catch (ParseException pe) {
			throw new ParseException(pe.getMessage(), pe.getErrorOffset());
		}
		return result;
	}

	public static final Date convertString2Date(String[] formatString,
			String targetDate) throws ParseException {
		if (StringUtils.isBlank(targetDate)) {
			return null;
		}
		SimpleDateFormat format = null;
		Date result = null;
		String errorMessage = null;
		Integer errorOffset = null;
		for (String dateFormat : formatString) {
			try {
				format = new SimpleDateFormat(dateFormat);
				result = format.parse(targetDate);
			} catch (ParseException pe) {
				result = null;
				errorMessage = pe.getMessage();
				errorOffset = pe.getErrorOffset();
			} finally {
				if (result != null && result.getTime() > 1) {
					break;
				}
			}
		}
		if (result == null) {
			throw new ParseException(errorMessage, errorOffset);
		}
		return result;
	}

	/**
	 * 格式化日期
	 */
	public static String dateFormat(Date date) {
		if (date == null) {
			return null;
		}
		return DateFormat.getDateInstance().format(date);
	}

	/**
	 * 比较当前时间与时间date2的天相等 时间格式 2008-11-25 16:30:10 如:当前时间是2008-11-25
	 * 16:30:10与传入时间2008-11-25 15:31:20 相比较,返回true即相等
	 * 
	 * @param date1
	 * @param date2
	 * @return boolean; true:相等
	 * @author zhangjl
	 */
	public static boolean equalDate(String date2) {
		try {
			String date1 = convertDate2String(DEFAILT_DATE_TIME_PATTERN,
					new Date());
			date1.equals(date2);
			Date d1 = convertString2Date(DEFAILT_DATE_PATTERN, date1);
			Date d2 = convertString2Date(DEFAILT_DATE_PATTERN, date2);
			return d1.equals(d2);
		} catch (ParseException e) {
			System.out.println(e.getMessage());
			return false;
		}
	}

	/**
	 * 比较时间date1与时间date2的天相等 时间格式 2008-11-25 16:30:10
	 * 
	 * @param date1
	 * @param date2
	 * @return boolean; true:相等
	 * @author zhangjl
	 */
	public static boolean equalDate(String date1, String date2) {
		try {

			Date d1 = convertString2Date(DEFAILT_DATE_PATTERN, date1);
			Date d2 = convertString2Date(DEFAILT_DATE_PATTERN, date2);

			return d1.equals(d2);
		} catch (ParseException e) {
			return false;
		}
	}

	/**
	 * 格式化日期 yyyy-MM-dd
	 * 
	 * @throws ParseException
	 *             例： DateUtils.formatDate(new Date()) "yyyy-MM-dd 00:00:00"
	 */
	public static Date formatDate(Date date) throws ParseException {
		date = formatDate(MyDateUtils.DEFAILT_DATE_PATTERN, date);
		return date;
	}

	/**
	 * 格式化日期
	 * 
	 * @throws ParseException
	 * 
	 *             例: DateUtils.formatDate("yyyy-MM-dd HH",new Date())
	 *             "yyyy-MM-dd HH:00:00"
	 */
	public static Date formatDate(String formatString, Date date)
			throws ParseException {
		if (date == null) {
			date = new Date();
		}
		if (StringUtils.isBlank(formatString))
			formatString = MyDateUtils.DEFAILT_DATE_PATTERN;

		date = MyDateUtils.convertString2Date(formatString,
				MyDateUtils.convertDate2String(formatString, date));

		return date;
	}

	/**
	 * Convert date and time to string like "yyyy-MM-dd HH:mm".
	 */
	public static String formatDateTime(Date d) {
		return new SimpleDateFormat(DATETIME_FORMAT).format(d);
	}

	/**
	 * Convert date and time to string like "yyyy-MM-dd HH:mm".
	 */
	public static String formatDateTime(long d) {
		return new SimpleDateFormat(DATETIME_FORMAT).format(d);
	}

	/**
	 * 获取指定月份的第一天
	 * 
	 * @param currentDate
	 * @return
	 */
	public static Date getBeginDate(Calendar currentDate) {
		Calendar result = Calendar.getInstance();
		result.set(currentDate.get(Calendar.YEAR),
				(currentDate.get(Calendar.MONTH)),
				result.getActualMinimum(Calendar.DATE));
		return result.getTime();
	}

	/**
	 * 获取上个月开始时间
	 * 
	 * @param currentDate
	 *            当前时间
	 * @return 上个月的第一天
	 */
	public static Date getBoferBeginDate(Calendar currentDate) {
		Calendar result = Calendar.getInstance();
		result.set(currentDate.get(Calendar.YEAR),
				(currentDate.get(Calendar.MONTH)) - 1,
				result.getActualMinimum(Calendar.DATE), 0, 0, 0);
		return result.getTime();
	}

	/**
	 * 获取上个月结束时间
	 * 
	 * @param currentDate
	 *            当前时间
	 * @return 上个月最后一天
	 */
	public static Date getBoferEndDate(Calendar currentDate) {
		Calendar result = currentDate;
		// result.set(currentDate.get(Calendar.YEAR), currentDate
		// .get(Calendar.MONTH) - 1);
		result.set(Calendar.DATE, 1);
		result.add(Calendar.DATE, -1);
		return result.getTime();
	}

	/**
	 * 根据当前给定的日期获取当前天是星期几(中国版的)
	 * 
	 * @param date
	 *            任意时间
	 * @return
	 */
	public static String getChineseWeek(Calendar date) {
		final String dayNames[] = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五",
				"星期六" };
		int dayOfWeek = date.get(Calendar.DAY_OF_WEEK);
		return dayNames[dayOfWeek - 1];

	}

	public static Date getCountMaxDate(int year, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		Calendar calendar2 = Calendar.getInstance();
		calendar2.set(calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH), 0);
		return calendar2.getTime();
	}

	public static Date getCountMinDate(int year, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month - 1, calendar.getActualMinimum(Calendar.DATE));
		return calendar.getTime();
	}

	public static Calendar getDate(int year, int month) {
		return getDate(year, month, 0);
	}

	public static Calendar getDate(int year, int month, int date) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, date);
		return calendar;
	}

	/**
	 * 获取两个时间的时间间隔
	 * 
	 * @param beginDate
	 *            开始时间
	 * @param endDate
	 *            结束时间
	 * @return
	 */
	public static int getDaysBetween(Calendar beginDate, Calendar endDate) {
		if (beginDate.after(endDate)) {
			Calendar swap = beginDate;
			beginDate = endDate;
			endDate = swap;
		}
		int days = endDate.get(Calendar.DAY_OF_YEAR)
				- beginDate.get(Calendar.DAY_OF_YEAR) + 1;
		int year = endDate.get(Calendar.YEAR);
		if (beginDate.get(Calendar.YEAR) != year) {
			beginDate = (Calendar) beginDate.clone();
			do {
				days += beginDate.getActualMaximum(Calendar.DAY_OF_YEAR);
				beginDate.add(Calendar.YEAR, 1);
			} while (beginDate.get(Calendar.YEAR) != year);
		}
		return days;
	}

	/**
	 * 获取发生日期的结束时间 根据用户设置的日期天数来判定这这个日期是什么(例如 (getHappenMinDate = 2008-10-1) 的话
	 * 那么 (getHappenMaxDate = 2008-11-1) 号)
	 * 
	 * @return
	 */
	public static Date getHappenMaxDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
		return calendar.getTime();
	}

	/**
	 * 获取两个日期之间的休息时间
	 * 
	 * @param beginDate
	 *            开始时间
	 * @param endDate
	 *            结束时间
	 * @return
	 */
	public static int getHolidays(Calendar beginDate, Calendar endDate) {
		return getDaysBetween(beginDate, endDate)
				- getWorkingDay(beginDate, endDate);

	}

	/**
	 * 计算两端时间的小时差
	 * 
	 * @param begin
	 * @param end
	 * @return
	 */
	public static int getHour(Date begin, Date end) {
		Calendar c1 = Calendar.getInstance();
		c1.setTime(begin);
		Calendar c2 = Calendar.getInstance();
		c2.setTime(end);
		Long millisecond = c2.getTimeInMillis() - c1.getTimeInMillis();
		Long hour = millisecond / 1000 / 60 / 60;
		Long minute = (millisecond / 1000 / 60) % 60;
		if (minute >= 30) {
			hour++;
		}

		return hour.intValue();
	}

	/**
	 * 获取当前月分的最大天数
	 * 
	 * @param currentDate
	 *            当前时间
	 * @return
	 */
	public static Date getMaxDate() {
		Calendar currentDate = Calendar.getInstance();
		return getMaxDate(currentDate);
	}

	/**
	 * 获取当前月分的最大天数
	 * 
	 * @param currentDate
	 *            当前时间
	 * @return
	 */
	public static Date getMaxDate(Calendar currentDate) {
		Calendar result = Calendar.getInstance();
		result.set(currentDate.get(Calendar.YEAR),
				currentDate.get(Calendar.MONTH),
				currentDate.getActualMaximum(Calendar.DATE));
		return result.getTime();
	}

	/**
	 * 获取今天最大的时间
	 * 
	 * @return
	 */
	public static String getMaxDateTimeForToDay() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY,
				calendar.getMaximum(Calendar.HOUR_OF_DAY));
		calendar.set(Calendar.MINUTE, calendar.getMaximum(Calendar.MINUTE));
		calendar.set(Calendar.SECOND, calendar.getMaximum(Calendar.SECOND));
		return convertDate2String(DEFAILT_DATE_TIME_PATTERN, calendar.getTime());
	}

	/**
	 * 获取日期最大的时间
	 * 
	 * @return
	 */
	public static Date getMaxDateTimeForToDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY,
				calendar.getMaximum(Calendar.HOUR_OF_DAY));
		calendar.set(Calendar.MINUTE, calendar.getMaximum(Calendar.MINUTE));
		calendar.set(Calendar.SECOND, calendar.getMaximum(Calendar.SECOND));
		return calendar.getTime();
	}

	/**
	 * 获取当前月份的第一天
	 * 
	 * @param currtenDate
	 *            当前时间
	 * @return
	 */
	public static Date getMinDate() {
		Calendar currentDate = Calendar.getInstance();
		return getMinDate(currentDate);
	}

	/**
	 * 获取当前月份的第一天
	 * 
	 * @param currtenDate
	 *            当前时间
	 * @return
	 */
	public static Date getMinDate(Calendar currentDate) {
		Calendar result = Calendar.getInstance();
		result.set(currentDate.get(Calendar.YEAR),
				currentDate.get(Calendar.MONTH),
				currentDate.getActualMinimum(Calendar.DATE));
		return result.getTime();
	}

	/**
	 * 获取今天最小时间
	 * 
	 * @return
	 */
	public static String getMinDateTimeForToDay() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY,
				calendar.getMinimum(Calendar.HOUR_OF_DAY));
		calendar.set(Calendar.MINUTE, calendar.getMinimum(Calendar.MINUTE));
		calendar.set(Calendar.SECOND, calendar.getMinimum(Calendar.SECOND));
		return convertDate2String(DEFAILT_DATE_TIME_PATTERN, calendar.getTime());
	}

	/**
	 * 获取 date 最小时间
	 * 
	 * @return
	 */
	public static Date getMinDateTimeForToDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY,
				calendar.getMinimum(Calendar.HOUR_OF_DAY));
		calendar.set(Calendar.MINUTE, calendar.getMinimum(Calendar.MINUTE));
		calendar.set(Calendar.SECOND, calendar.getMinimum(Calendar.SECOND));
		return calendar.getTime();
	}

	/**
	 * @throws ParseException
	 *             根据日期获得 星期一的日期
	 * 
	 */
	public static Date getMonDay(Date date) throws ParseException {

		Calendar cal = Calendar.getInstance();
		if (date == null)
			date = new Date();
		cal.setTime(date);
		if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
			cal.add(Calendar.WEEK_OF_YEAR, -1);

		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

		date = formatDate(cal.getTime());

		return date;
	}

	/**
	 * 获取两个时间的时间间隔(月份)
	 * 
	 * @param beginDate
	 *            开始时间
	 * @param endDate
	 *            结束时间
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static int getMonthsBetween(Date beginDate, Date endDate) {
		if (beginDate.after(endDate)) {
			Date swap = beginDate;
			beginDate = endDate;
			endDate = swap;
		}
		int months = endDate.getMonth() - beginDate.getMonth();
		int years = endDate.getYear() - beginDate.getYear();

		months += years * 12;

		return months;
	}

	/**
	 * 获得 下个月的第一天
	 * 
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static Date getNextDay(Date date) throws ParseException {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, 1);
		cal.set(Calendar.DATE, 1);
		return formatDate(cal.getTime());
	}

	/**
	 * 获得日期的下一个星期一的日期
	 * 
	 * @param date
	 *            任意时间
	 * @return
	 */
	public static Calendar getNextMonday(Calendar date) {
		Calendar result = null;
		result = date;
		do {
			result = (Calendar) result.clone();
			result.add(Calendar.DATE, 1);
		} while (result.get(Calendar.DAY_OF_WEEK) != 2);
		return result;
	}

	/**
	 * @throws ParseException
	 *             根据日期获得 星期日 的日期
	 * 
	 */
	public static Date getSunDay(Date date) throws ParseException {

		Calendar cal = Calendar.getInstance();
		if (date == null)
			date = new Date();
		cal.setTime(date);
		if (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY)
			cal.add(Calendar.WEEK_OF_YEAR, 1);

		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

		date = formatDate(cal.getTime());
		return date;
	}

	/*
	 * public static void main(String[] args) {
	 * System.out.println(getMaxDateTimeForToDay());
	 * System.out.println(getMinDateTimeForToDay()); }
	 */

	/**
	 * 获取两个时间内的工作日
	 * 
	 * @param beginDate
	 *            开始时间
	 * @param endDate
	 *            结束时间
	 * @return
	 */
	public static int getWorkingDay(Calendar beginDate, Calendar endDate) {
		int result = -1;
		if (beginDate.after(endDate)) {
			Calendar swap = beginDate;
			beginDate = endDate;
			endDate = swap;
		}
		int charge_start_date = 0;
		int charge_end_date = 0;
		int stmp;
		int etmp;
		stmp = 7 - beginDate.get(Calendar.DAY_OF_WEEK);
		etmp = 7 - endDate.get(Calendar.DAY_OF_WEEK);
		if (stmp != 0 && stmp != 6) {
			charge_start_date = stmp - 1;
		}
		if (etmp != 0 && etmp != 6) {
			charge_end_date = etmp - 1;
		}
		result = (getDaysBetween(getNextMonday(beginDate),
				getNextMonday(endDate)) / 7)
				* 5
				+ charge_start_date
				- charge_end_date;
		return result;
	}

	/*****************************************
	 * @功能 计算某年某月的结束日期
	 * @return interger
	 * @throws ParseException
	 ****************************************/
	public static String getYearMonthEndDay(int yearNum, int monthNum)
			throws ParseException {

		// 分别取得当前日期的年、月、日
		String tempYear = Integer.toString(yearNum);
		String tempMonth = Integer.toString(monthNum);
		String tempDay = "31";
		if (tempMonth.equals("1") || tempMonth.equals("3")
				|| tempMonth.equals("5") || tempMonth.equals("7")
				|| tempMonth.equals("8") || tempMonth.equals("10")
				|| tempMonth.equals("12")) {
			tempDay = "31";
		}
		if (tempMonth.equals("4") || tempMonth.equals("6")
				|| tempMonth.equals("9") || tempMonth.equals("11")) {
			tempDay = "30";
		}
		if (tempMonth.equals("2")) {
			if (isLeapYear(yearNum)) {
				tempDay = "29";
			} else {
				tempDay = "28";
			}
		}
		String tempDate = tempYear + "-" + tempMonth + "-" + tempDay;
		return tempDate;// setDateFormat(tempDate,"yyyy-MM-dd");
	}

	/**
	 * 日期比较
	 * 
	 * 判断当前时间是否在时间date2之前 <br/>
	 * 时间格式 2005-4-21 16:16:34 <br/>
	 * 添加人：胡建国
	 * 
	 * @param targetDate
	 * @return
	 */
	public static boolean isDateBefore(String date2) {
		if (date2 == null) {
			return false;
		}
		try {
			Date date1 = new Date();
			DateFormat df = DateFormat.getDateTimeInstance();
			return date1.before(df.parse(date2));
		} catch (ParseException e) {
			return false;
		}
	}

	/**
	 * 日期比较
	 * 
	 * 判断时间date1是否在时间date2之前 <br/>
	 * 时间格式 2005-4-21 16:16:34 <br/>
	 * 添加人：胡建国
	 * 
	 * @param targetDate
	 * @return
	 */
	public static boolean isDateBefore(String date1, String date2) {
		try {
			DateFormat df = DateFormat.getDateTimeInstance();
			return df.parse(date1).before(df.parse(date2));
		} catch (ParseException e) {
			return false;
		}
	}

	public static boolean isDateEnable(Date beginDate, Date endDate,
			Date currentDate) {
		// 开始日期
		long beginDateLong = beginDate.getTime();
		// 结束日期
		long endDateLong = endDate.getTime();
		// 当前日期
		long currentDateLong = currentDate.getTime();
		if (currentDateLong >= beginDateLong && currentDateLong <= endDateLong) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	/*****************************************
	 * @功能 判断某年是否为闰年
	 * @return boolean
	 * @throws ParseException
	 ****************************************/
	public static boolean isLeapYear(int yearNum) {
		boolean isLeep = false;
		/** 判断是否为闰年，赋值给一标识符flag */
		if ((yearNum % 4 == 0) && (yearNum % 100 != 0)) {
			isLeep = true;
		} else if (yearNum % 400 == 0) {
			isLeep = true;
		} else {
			isLeep = false;
		}
		return isLeep;
	}

	/**
	 * Parse date like "yyyy-MM-dd".
	 */
	public static Date parseDate(String d) {
		try {
			return new SimpleDateFormat(DATE_FORMAT).parse(d);
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * Parse date and time like "yyyy-MM-dd hh:mm".
	 */
	public static Date parseDateTime(String dt) {
		try {
			return new SimpleDateFormat(DATETIME_FORMAT).parse(dt);
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * Get the previous time, from how many days to now.
	 * 
	 * @param days
	 *            How many days.
	 * @return The new previous time.
	 */
	public static Date previous(int days) {
		return new Date(System.currentTimeMillis() - days * 3600000L * 24L);
	}

	/**
	 * @see 取得指定时间的给定格式()
	 * @return String
	 * @throws ParseException
	 */
	public static String setDateFormat(Date myDate, String strFormat)
			throws ParseException {
		if (myDate == null) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(strFormat);
		String sDate = sdf.format(myDate);
		return sDate;
	}

	public static String setDateFormat(String myDate, String strFormat)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(strFormat);
		String sDate = sdf.format(myDate);

		return sDate;
	}

	public static Long getCurrentDay0Time(){
		Calendar currentDate = new GregorianCalendar(); 
		currentDate.set(Calendar.HOUR_OF_DAY, 0);
		currentDate.set(Calendar.MINUTE, 0);
		currentDate.set(Calendar.SECOND, 0);
		currentDate.set(Calendar.MILLISECOND,0);
		return  currentDate.getTimeInMillis();
	}
	
	/**
	 * 获取xxxx年 第x周 x月xx日-x月xx日
	 * @param date
	 * @return
	 */
	public static String getFewWeek(long millis) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(millis);
		StringBuffer ret = new StringBuffer();
		ret.append(c.get(Calendar.YEAR)).append("年 第").append(c.get(Calendar.WEEK_OF_YEAR)).append("周  ");
		
		int year 	  = c.get(Calendar.YEAR);
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		int weekStart = dayOfWeek == 1 ? 0 : dayOfWeek - 1;
		int weekEnd   = dayOfWeek == 7 ? 7 : 7 - dayOfWeek;
		if(weekStart > 0) {
			c.add(Calendar.DAY_OF_YEAR, -weekStart);
		}
		if(year != c.get(Calendar.YEAR)) {
			year = c.get(Calendar.YEAR);
			ret.append(year).append("年");
		}
		ret.append(c.get(Calendar.MONTH) + 1).append("月").append(c.get(Calendar.DAY_OF_MONTH)).append("-");
		c.add(Calendar.DAY_OF_YEAR, weekStart);
		if(weekEnd < 7) {
			c.add(Calendar.DAY_OF_YEAR, weekEnd);
		}
		if(year != c.get(Calendar.YEAR)) {
			ret.append(c.get(Calendar.YEAR)).append("年");
		}
		ret.append(c.get(Calendar.MONTH) + 1).append("月").append(c.get(Calendar.DAY_OF_MONTH));
		return ret.toString();
	}
	
	/*
	 * 判断 day1 是否比 day2 多2天
	 * @param Long day1,Long day2
	 * @return
	*/
	public static boolean overTwoDays(Date day1,Date day2){
		
		Long hour = (day1.getTime() - day2.getTime())/3600000;
		 
		if(hour.longValue()<48)
			return false; 
		
		return true; 
		
	}
	
	/*
	 * 判断day1 是否比 day2 多一年
	 * @param Long day 
	 * @return
	*/
	public static boolean overOneYear(Date day1,Date day2){
		
		Calendar c = Calendar.getInstance();
		c.setTime(day1);
		c.add(Calendar.YEAR, 1);
		if(c.getTime().compareTo(day2)>1)
			return false; 
		
		return true; 
		
	}

	public static void main(String[] args) {
//		String dateString = "2015-01-23";
//		DateFormat format = new SimpleDateFormat(MyDateUtils.DEFAILT_DATE_PATTERN);
//		Date date = null;
//		try {
//			date = format.parse(dateString);
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//		System.out.println(MyDateUtils.getFewWeek(date.getTime()));
//		System.out.println(MyDateUtils.getFewMonth(date));
//		跨年
//		2015年 第1周  2014年12月28-2015年1月3
//		2015年1月
//		不跨年
//		2015年 第5周  1月25-1月31
//		2015年1月
		
//		System.out.println(MyDateUtils.getTimeMillis());
		
//		System.out.println(trans("2015年09月11日",DATETIME_FORMAT_CHINA,DATE_FORMAT));
		//System.out.println(trans("20151019202600",DATETIME_FORMAT_YYYYMMDDHHMMSS,DEFAILT_DATE_TIME_PATTERN));
		Date a = new Date();
		System.out.println(a);
		try {
			Date b = convertString2Date(DEFAILT_DATE_TIME_PATTERN, "2016-05-11 14:20:00");
			System.out.println( b);
			System.out.println(a.before(b));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 Calendar calendar = Calendar.getInstance();
		 System.out.println(calendar.getTime());
		
	}
	
	
}
