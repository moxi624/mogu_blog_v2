package com.moxi.mogublog.utils;
/**
 * 时间操作工具类
 *
 * @author 陌溪
 * 2018年5月8日 上午9:20:33
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

public class DateUtils {

    public static final String STARTTIME = " 00:00:00";
    public static final String ENDTIME = " 23:59:59";
    public final static String FORMAT_STRING = "yyyy-MM-dd HH:mm:ss";
    public final static String[] REPLACE_STRING = new String[]{"GMT+0800", "GMT+08:00"};
    public final static String SPLIT_STRING = "(中国标准时间)";
    public static Logger log = LoggerFactory.getLogger(DateUtils.class);

    public static String YYYY = "yyyy";

    public static String YYYY_MM = "yyyy-MM";

    public static String YYYY_MM_DD = "yyyy-MM-dd";

    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    private DateUtils() {
    }

    /**
     * 获取现在的时间 yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String getNowTime() {
        SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        return formate.format(date);
    }

    /**
     * 获取当前Date型日期
     *
     * @return Date() 当前日期
     */
    public static Date getNowDate() {
        return new Date();
    }

    /**
     * @return
     * @author 陌溪
     * @date 2018年6月14日
     */
    public static String getNowTimeFormat() {
        SimpleDateFormat formate = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        Date date = new Date(System.currentTimeMillis());
        return formate.format(date);
    }


    public static Date str2Date(String dateString) {
        try {
            dateString = dateString.split(Pattern.quote(SPLIT_STRING))[0].replace(REPLACE_STRING[0], REPLACE_STRING[1]);
            SimpleDateFormat sf1 = new SimpleDateFormat("E MMM dd yyyy HH:mm:ss z", Locale.US);
            Date date = sf1.parse(dateString);
            return date;
        } catch (Exception e) {
            throw new RuntimeException("时间转化格式错误" + "[dateString=" + dateString + "]" + "[FORMAT_STRING=" + FORMAT_STRING + "]");
        }
    }

    /**
     * 获取今天开始的时间
     *
     * @return
     */
    public static String getToDayStartTime() {
        SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        Date date = new Date(System.currentTimeMillis());
        return formate.format(date);
    }

    /**
     * 获取今天结束的时间
     *
     * @return
     */
    public static String getToDayEndTime() {
        SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        Date date = new Date(System.currentTimeMillis());
        return formate.format(date);
    }

    /**
     * 获取昨天开始的时间
     *
     * @return
     */
    public static String getYestodayStartTime() {
        SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        Date date = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000L);
        return formate.format(date);
    }

    /**
     * 获取昨天结束的时间
     *
     * @return
     */
    public static String getYestodayEndTime() {
        SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        Date date = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000L);
        return formate.format(date);
    }

    /**
     * 获取某天开始的时间
     *
     * @return
     */
    public static String getOneDayStartTime(String oneDay) {
        SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        Date date = new Date(oneDay);
        return formate.format(date);
    }

    /**
     * 获取某天开始的日期
     *
     * @param oneDay
     * @return
     */
    public static String getOneDayStartTime(Date oneDay) {
        SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        return formate.format(oneDay);
    }

    /**
     * 获取某天结束的时间
     *
     * @return
     */
    public static String getOneDayEndTime(String oneDay) {
        SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        Date date = new Date(oneDay);
        return formate.format(date);
    }

    /**
     * 获取某天结束的日期
     *
     * @param oneDay
     * @return
     */
    public static String getOneDayEndTime(Date oneDay) {
        SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        return formate.format(oneDay);
    }


    /**
     * 获取本周开始的时间
     *
     * @return
     */
    public static Date getWeekStartTime() {
        // 获得本周一0点时间
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return cal.getTime();
    }

    /**
     * 将  String 转换成  date
     *
     * @param dateTime
     * @return
     */
    public static Date strToDateTime(String dateTime) {
        Date date = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = format.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 将  date 转换成  时间戳
     *
     * @return
     */
    public static Long dateToStamp(String s) throws ParseException {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        return ts;
    }

    /**
     * Date 转换成  String
     *
     * @param dateTime
     * @return
     */
    public static String dateTimeToStr(Date dateTime) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(dateTime);
    }

    /**
     * 获取本周开始的时间的字符串
     *
     * @return
     */
    public static String getWeekStartTimeStr() {
        // 获得本周一0点时间
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        return formate.format(cal.getTime());
    }

    /**
     * 获取本周结束的时间
     *
     * @return
     */
    public static Date getWeekEndTime() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getWeekStartTime());
        cal.add(Calendar.DAY_OF_WEEK, 7);
        return cal.getTime();
    }

    /**
     * 获取本周结束的时间的字符串
     *
     * @return
     */
    public static String getWeekEndTimeStr() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getWeekStartTime());
        cal.add(Calendar.DAY_OF_WEEK, 7);
        SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        return formate.format(cal.getTime());
    }

    /**
     * 获取上周开始的时间的字符串
     *
     * @return
     */
    public static String getLastWeekStartTimeStr() {
        int weeks = -1;
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 * weeks);
        Date monday = currentDate.getTime();
        SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        return formate.format(monday);
    }

    /**
     * 获取本月开始的时间
     *
     * @return
     */
    public static Date getMonthStartTime() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }

    /**
     * 获取本月开始的时间的字符串
     *
     * @return
     */
    public static String getMonthStartTimeStr() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        return formate.format(cal.getTime());
    }

    /**
     * 获取本月结束的时间
     *
     * @return
     */
    public static Date getMonthEndTime() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 24);
        return cal.getTime();
    }

    /**
     * 获取本月结束的时间的字符串
     *
     * @return
     */
    public static String getMonthEndTimeStr() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 24);
        SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        return formate.format(cal.getTime());
    }

    /**
     * 获取当月的 天数
     */
    public static int getCurrentMonthDay() {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    /**
     * 得到二个日期间的间隔天数
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int getDayByTwoDay(String date1, String date2) {
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
        Long day = 0L;
        try {
            java.util.Date date = myFormatter.parse(date1);
            java.util.Date mydate = myFormatter.parse(date2);
            day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
        } catch (Exception e) {
            return 0;
        }
        return day.intValue();
    }

    /**
     * 得到两个日期相差的秒数
     *
     * @param lastDate
     * @param date
     * @return
     */
    public static int getSecondByTwoDay(Date lastDate, Date date) {
        Long second = 0L;
        try {
            second = (lastDate.getTime() - date.getTime()) / 1000;
        } catch (Exception e) {
            return 0;
        }
        return second.intValue();
    }

    /**
     * 判断某个日期属于本周的第几天 (星期一代表第一天)
     *
     * @param dateTime
     * @return
     * @throws ParseException
     */
    public static int getDaysByWeek(String dateTime) throws ParseException {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = dateFormat.parse(dateTime);
        cal.setTime(date);
        int day = cal.get(Calendar.DAY_OF_WEEK);
        day = day - 1;
        if (day == 0) {
            day = 7;
        }
        return day;
    }

    /**
     * 判断某个日期属于本月的第几天
     *
     * @param dateTime
     * @return
     * @throws ParseException
     */
    public static int getDaysByMonth(String dateTime) throws ParseException {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = dateFormat.parse(dateTime);
        cal.setTime(date);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return day;
    }

    /**
     * 根据年 月 获取对应的月份 天数
     */
    public static int getDaysByYearMonth(int year, int month) {

        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }


    /**
     * 获取当前的年
     *
     * @return
     */
    public static Integer getYears() {
        Calendar calendar = new GregorianCalendar(TimeZone
                .getDefault());
        return calendar.get(Calendar.YEAR);

    }

    /**
     * 获取当前的月
     *
     * @return
     */
    public static Integer getMonth() {
        Calendar calendar = new GregorianCalendar(TimeZone
                .getDefault());
        return calendar.get(Calendar.MONTH) + 1;

    }

    /**
     * 获取当前天
     *
     * @return
     */
    public static Integer getDay() {
        Calendar calendar = new GregorianCalendar(TimeZone
                .getDefault());
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * wx支付的过期时间
     *
     * @param hour
     * @return
     */
    @SuppressWarnings("unused")
    public static String getTime(double hour) {
        Calendar calendar = new GregorianCalendar(TimeZone.getDefault());
        long time = (long) (System.currentTimeMillis() + hour * 60 * 60 * 1000L);
        Date date = new Date(time);
        SimpleDateFormat formate = new SimpleDateFormat("yyyyMMddHHmmss");
        String format = formate.format(date);
        return format;
    }

    /**
     * 获得当前日期与本周日相差的天数
     *
     * @return
     */
    private static int getMondayPlus() {
        Calendar cd = Calendar.getInstance();
        // 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
        // 因为按中国礼拜一作为第一天所以这里减1
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayOfWeek == 1) {
            return 0;
        } else {
            return 1 - dayOfWeek;
        }
    }

    /**
     * 获取几天之后的日期
     *
     * @param date yyyy-MM-dd HH:mm:ss
     * @param day  加减的天数
     * @return
     */
    public static Date getDate(String date, int day) {
        SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        try {
            Date beforeDate = formate.parse(date);
            cal.setTime(beforeDate);
            cal.add(Calendar.DAY_OF_MONTH, day);
            //cal.set(beforeDate.getYear(), beforeDate.getMonth(), beforeDate.getDay()+day, beforeDate.getHours(),beforeDate.getSeconds(), beforeDate.getMinutes());
            Date newDate = cal.getTime();
            return newDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取某个日期 在加上 秒数的时间
     *
     * @param beforeDate yyyy-MM-dd HH:mm:ss
     * @param timeSecond 加减的秒数
     * @return
     */
    public static String getDateStr(Date beforeDate, Long timeSecond) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            // 返回毫秒数 + 添加的毫秒数
            Long time = beforeDate.getTime() + timeSecond * 1000;
            return format.format(time);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return "";
    }

    /**
     * 把date转换成字符串
     *
     * @param date
     * @param code 例如  yyyy-MM-dd 00:00:00
     * @return
     */
    public static String formateDate(Date date, String code) {
        SimpleDateFormat formate = new SimpleDateFormat(code);
        return formate.format(date);

    }

    /**
     * 获取过去N天内的日期数组
     *
     * @param intervals intervals天内
     * @param formatStr 格式化字符串   yyyy-MM-dd
     * @return 日期数组
     */
    public static ArrayList<String> getDaysByN(int intervals, String formatStr) {
        ArrayList<String> pastDaysList = new ArrayList<>();
        for (int i = intervals - 1; i >= 0; i--) {
            pastDaysList.add(getPastDate(i, formatStr));
        }
        return pastDaysList;
    }

    /**
     * 获取过去第几天的日期
     *
     * @param past
     * @return
     */
    public static String getPastDate(int past, String formatStr) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        String result = format.format(today);
        return result;
    }

    /**
     * 获取某个时间段内所有日期
     *
     * @param begin
     * @param end
     * @return
     */
    public static List<String> getDayBetweenDates(String begin, String end) {
        Date dBegin = strToDateTime(begin);
        Date dEnd = strToDateTime(end);
        List<String> lDate = new ArrayList<>();
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        lDate.add(sd.format(dBegin));
        Calendar calBegin = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calBegin.setTime(dBegin);
        Calendar calEnd = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calEnd.setTime(dEnd);
        // 测试此日期是否在指定日期之后
        while (dEnd.after(calBegin.getTime())) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            lDate.add(sd.format(calBegin.getTime()));
        }
        return lDate;
    }

    /**
     * 获取服务器启动时间
     */
    public static Date getServerStartDate() {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    /**
     * 计算两个时间差
     */
    public static String getDatePoor(Date endDate, Date nowDate) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }
}
