package com.itblare.itools.date;
/*
 * Copyright (C), 2020-2021, 逝者如斯夫，不舍昼夜
 * PackageName: com.itblare.itools.date
 * ClassName:   DateProcessor
 * Author:   Blare
 * Date:     Created in 2021/4/17 21:49
 * Description:    时间处理器
 * History:
 * <author>        <time>             <version>          <desc>
 * 作者姓名         修改时间             版本号              描述
 * Blare           2021/4/17 21:49    1.0.0         时间处理器
 */

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

/**
 * 一句话功能简述：时间处理器
 *
 * @author Blare
 * @create 2021/4/17 21:49
 * @since 1.0.0
 */
public class DateProcessor {

    /**
     * 功能描述: 日期转换
     *
     * @param time 时间戳
     * @param fmt  格式，eg：yyyy-MM-dd HH:mm:ss
     * @return {@link String}
     * @method formatTime
     * @date 2021/4/18 14:40
     */
    public static String formatTime(Timestamp time, String fmt) {
        if (time == null) {
            return "";
        }
        SimpleDateFormat myFormat = new SimpleDateFormat(fmt);
        return myFormat.format(time);
    }

    /**
     * 功能描述: 获取当前时间戳(毫秒)
     *
     * @return {@link Long}
     * @method getCurrentTimeMills
     * @date 20WW21/4/18 14:41
     */
    public static Long getCurrentTimeMills() {
        return System.currentTimeMillis();
    }

    /**
     * 功能描述: 获取系统当前时间（秒）
     *
     * @return {@link Timestamp}
     * @method getTime
     * @date 2021/4/18 14:41
     */
    public static Timestamp getTime() {
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        String mystrdate = myFormat.format(calendar.getTime());
        return Timestamp.valueOf(mystrdate);
    }

    /**
     * 功能描述: 获取当前日期开始(时间 00:00:00)
     *
     * @return {@link Timestamp}
     * @method getDateFirst
     * @date 2021/4/18 14:41
     */
    public static Timestamp getDateFirst() {
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        Calendar calendar = Calendar.getInstance();
        String masticate = myFormat.format(calendar.getTime());
        return Timestamp.valueOf(masticate);
    }

    /**
     * 功能描述: 获取当前日期结束(时间 23:59:59)
     *
     * @return {@link Timestamp}
     * @method getDateLast
     * @date 2021/4/18 14:41
     */
    public static Timestamp getDateLast() {
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        Calendar calendar = Calendar.getInstance();
        String mystrdate = myFormat.format(calendar.getTime());
        return Timestamp.valueOf(mystrdate);
    }

    /**
     * 功能描述: 获取昨日开始时间 00:00:00
     *
     * @return {@link Timestamp}
     * @method getYesterdayBegin
     * @date 2021/4/18 14:41
     */
    public static Timestamp getYesterdayBegin() {
        long today = getDateFirst().getTime();
        Long yesterday = today - (24 * 60 * 60 * 1000 - 1);
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String result = myFormat.format(yesterday);
        return Timestamp.valueOf(result);
    }

    /**
     * 功能描述: 获取昨日最后时间 23:59:59
     *
     * @return {@link Timestamp}
     * @method getYesterdayEnd
     * @date 2021/4/18 14:42
     */
    public static Timestamp getYesterdayEnd() {
        long today = getDateLast().getTime();
        Long yesterday = today - (24 * 60 * 60 * 1000 - 1);
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String result = myFormat.format(yesterday);
        return Timestamp.valueOf(result);
    }

    /**
     * 功能描述: 获取当前日期
     *
     * @return {@link Date}
     * @method getDate
     * @date 2021/4/18 14:42
     */
    public static Date getDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    /**
     * 功能描述: 格式（yyyy-MM-dd HH:mm:ss）转换成 Timestamp
     *
     * @param timeString 格式时间
     * @return {@link Timestamp}
     * @method getTime
     * @date 2021/4/18 14:42
     */
    public static Timestamp getTime(String timeString) {
        return Timestamp.valueOf(timeString);
    }

    /**
     * 功能描述: 自定义格式的字符串转换成日期
     *
     * @param timeString 日期时间字符串
     * @param fmt        格式，eg：yyyy-MM-dd HH:mm:ss
     * @return {@link Timestamp}
     * @method getTime
     * @date 2021/4/18 14:43
     */
    public static Timestamp getTime(String timeString, String fmt) throws Exception {
        SimpleDateFormat myFormat = new SimpleDateFormat(fmt);
        Date date = myFormat.parse(timeString);
        myFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return getTime(myFormat.format(date));
    }

    /**
     * 功能描述: 格式化日期
     *
     * @param date 日期
     * @param fmt  自定义格式，eg：yyyy-MM-dd HH:mm:ss
     * @return {@link String}
     * @method formatDate
     * @date 2021/4/18 14:44
     */
    public static String formatDate(Date date, String fmt) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat myFormat = new SimpleDateFormat(fmt);
        return myFormat.format(date);
    }

    /**
     * 功能描述: 格式化日期
     *
     * @param timeString    时间字符串
     * @param timeStringFmt 时间字符串格式
     * @param fmt           解析为格式
     * @return {@link String}
     * @method formatDate
     * @date 2021/4/18 14:44
     */
    public static String formatDate(String timeString, String timeStringFmt, String fmt) throws Exception {
        SimpleDateFormat myFormat = new SimpleDateFormat(timeStringFmt);
        Date date = myFormat.parse(timeString);
        myFormat = new SimpleDateFormat(fmt);
        return myFormat.format(date);
    }

    /**
     * 功能描述: 返回日期或者时间，如果传入的是日期，返回日期的 00:00:00 时间
     *
     * @param timeString 时间字符串
     * @return {@link Timestamp}
     * @method getDateFirst
     * @date 2021/4/18 14:44
     */
    public static Timestamp getDateFirst(String timeString) throws Exception {

        if (timeString == null || "".equals(timeString)) {
            return null;
        }
        if (timeString.length() > 10) {
            return getTime(timeString, "yyyy-MM-dd HH:mm:ss");
        } else {
            return getTime(timeString, "yyyy-MM-dd");
        }
    }

    /**
     * 功能描述: 返回日期或者时间，如果传入的是日期，返回日期的 23:59:59 时间
     *
     * @param timeString 时间字符串
     * @return {@link Timestamp}
     * @method getDateLast
     * @date 2021/4/18 14:45
     */
    public static Timestamp getDateLast(String timeString) throws Exception {
        if (timeString == null || "".equals(timeString)) {
            return null;
        }
        if (timeString.length() > 10) {
            return getTime(timeString, "yyyy-MM-dd HH:mm:ss");
        } else {
            return getTime(timeString + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
        }
    }

    /**
     * 功能描述: 获取本周周一时间，返回格式 yyyy-MM-dd 00:00:00
     *
     * @return {@link Timestamp}
     * @method getMonday
     * @date 2021/4/18 14:45
     */
    public static Timestamp getMonday() {
        Calendar calendar = Calendar.getInstance();
        int dayofweek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayofweek == 0) {
            dayofweek = 7;
        }
        calendar.add(Calendar.DATE, -dayofweek + 1);
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        String mystrdate = myFormat.format(calendar.getTime());
        return Timestamp.valueOf(mystrdate);
    }

    /**
     * 功能描述: 获取本周周日时间，返回格式 yyyy-MM-dd 23:59:59
     *
     * @return {@link Timestamp}
     * @method getSunday
     * @date 2021/4/18 14:45
     */
    public static Timestamp getSunday() {
        Calendar calendar = Calendar.getInstance();
        int dayofweek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayofweek == 0) {
            dayofweek = 7;
        }
        calendar.add(Calendar.DATE, -dayofweek + 7);
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        String mystrdate = myFormat.format(calendar.getTime());
        return Timestamp.valueOf(mystrdate);
    }

    /**
     * 功能描述: 增加天数
     *
     * @param time 时间戳
     * @param day  增加的天数
     * @return {@link Timestamp}
     * @method addDay
     * @date 2021/4/18 14:46
     */
    public static Timestamp addDay(Timestamp time, Long day) {
        return new Timestamp(time.getTime() + day * 1000 * 60 * 60 * 24);
    }

    /**
     * 功能描述: 比较 2 个日期格式的字符串
     *
     * @param str1 格式 ：yyyyMMdd
     * @param str2 格式 ：yyyyMMdd
     * @return {@link Integer}
     * @method compareDate
     * @date 2021/4/18 14:46
     */
    public static Integer compareDate(String str1, String str2) {
        return Integer.parseInt(str1) - Integer.parseInt(str2);
    }

    /**
     * 功能描述: 2 个时间的相差天数
     *
     * @param time1 时间1
     * @param time2 时间2
     * @return {@link Integer}
     * @method getDay
     * @date 2021/4/18 14:46
     */
    public static Integer getDay(Timestamp time1, Timestamp time2) {
        long dayTime = (time1.getTime() - time2.getTime()) / (1000 * 60 * 60 * 24);
        return (int) dayTime;
    }

    /**
     * 功能描述: 获取系统当前时间（分）
     *
     * @return {@link String}
     * @method getMinute
     * @date 2021/4/18 14:47
     */
    public static String getMinute() {
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyyMMddHHmm");
        return myFormat.format(new Date());
    }

    /**
     * 功能描述: 获取系统当前时间
     *
     * @return {@link String}
     * @method getDateTime
     * @date 2021/4/18 14:47
     */
    public static String getDateTime() {
        return DateProcessor.getDateTime("yyyyMMddHHmmss");
    }

    /**
     * 功能描述: 获取系统当前日期
     *
     * @return {@link String}
     * @method getDateTimeDay
     * @date 2021/4/18 14:47
     */
    public static String getDateTimeDay() {
        return DateProcessor.getDateTime("yyyyMMdd");
    }

    /**
     * 功能描述: 获取系统当前时间
     *
     * @param formatStyle 返回时间格式
     * @return {@link String}
     * @method getDateTime
     * @date 2021/4/18 14:47
     */
    public static String getDateTime(String formatStyle) {
        if (formatStyle == null || "".equals(formatStyle)) {
            formatStyle = "yyyyMMddHHmmss";
        }
        SimpleDateFormat myFormat = new SimpleDateFormat(formatStyle);
        return myFormat.format(new Date());
    }

    /**
     * 功能描述: 格式化时间成yyyy-MM-dd HH:mm:ss
     *
     * @param time 待格式化时间
     * @return {@link String}
     * @method formatDateTime
     * @date 2021/4/18 14:48
     */
    public static String formatDateTime(String time) throws ParseException {
        return DateProcessor.formatDateTime(time, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 功能描述: 格式化时间成yyyy-MM-dd HH:mm:ss
     *
     * @param time    待格式化时间
     * @param timeStr 时间格式
     * @return {@link String}
     * @method formatDateTime
     * @date 2021/4/18 14:48
     */
    public static String formatDateTime(String time, String timeStr) throws ParseException {
        if (timeStr == null || "".equals(timeStr)) {
            timeStr = "yyyy-MM-dd HH:mm:ss";
        }
        Date date = new SimpleDateFormat("yyyyMMddHHmmss").parse(time);
        SimpleDateFormat myFormat = new SimpleDateFormat(timeStr);
        return myFormat.format(date);
    }

    /**
     * 功能描述: 转换成时间 字符串格式必须为 yyyy-MM-dd HH:mm:ss 或 yyyy-MM-dd
     *
     * @param val 待转换时间
     * @return {@link Date}
     * @method parseToDate
     * @date 2021/4/18 14:49
     */
    public static Date parseToDate(String val) throws ParseException {
        Date date = null;
        if (val != null && val.trim().length() != 0 && !val.trim().toLowerCase().equals("null")) {
            val = val.trim();
            if (val.length() > 10) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                date = sdf.parse(val);
            }
            if (val.length() <= 10) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                date = sdf.parse(val);
            }
        }
        return date;
    }

    /**
     * 功能描述: 获取上月的第一天 yyyy-MM-dd 00:00:00 和最后一天 yyyy-MM-dd 23:59:59
     *
     * @return {@link Map}
     * @method getPreMonth
     * @date 2021/4/18 14:50
     */
    public static Map<String, String> getPreMonth() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        calendar.add(Calendar.MONTH, -1);
        Date theDate = calendar.getTime();
        gcLast.setTime(theDate);
        gcLast.set(Calendar.DAY_OF_MONTH, 1);
        String day_first_prevM = df.format(gcLast.getTime());
        //上月第一天
        day_first_prevM = day_first_prevM + " 00:00:00";

        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DATE, 1);
        calendar.add(Calendar.DATE, -1);
        String day_end_prevM = df.format(calendar.getTime());
        //上月最后一天
        day_end_prevM = day_end_prevM + " 23:59:59";

        Map<String, String> map = new HashMap<>();
        map.put("prevMonthFD", day_first_prevM);
        map.put("prevMonthPD", day_end_prevM);
        return map;
    }

    /**
     * 功能描述: 获取本月的第一天 yyyy-MM-dd 00:00:00
     *
     * @return {@link String}
     * @method getNowMonth
     * @date 2021/4/18 14:50
     */
    public static String getNowMonth() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        calendar.add(Calendar.MONTH, 0);
        Date theDate = calendar.getTime();
        gcLast.setTime(theDate);
        gcLast.set(Calendar.DAY_OF_MONTH, 1);
        String day_first_prevM = df.format(gcLast.getTime());
        //本月第一天
        day_first_prevM = day_first_prevM + " 00:00:00";
        return day_first_prevM;
    }

    /**
     * 功能描述:  获取上周周一时间，返回格式 yyyy-MM-dd 00:00:00
     *
     * @return {@link Timestamp}
     * @method getPreMonday
     * @date 2021/4/18 14:51
     */
    public static Timestamp getPreMonday() {
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        System.out.println(dayOfWeek);
        if (dayOfWeek == 1) {
            calendar.add(Calendar.WEEK_OF_MONTH, -1);
        }

        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        calendar.add(Calendar.WEEK_OF_MONTH, -1);

        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        String mystrdate = myFormat.format(calendar.getTime());
        return Timestamp.valueOf(mystrdate);
    }

    /**
     * 功能描述: 获取上周周日时间，返回格式 yyyy-MM-dd 23:59:59
     *
     * @return {@link Timestamp}
     * @method getPreSunday
     * @date 2021/4/18 14:51
     */
    public static Timestamp getPreSunday() {
        Calendar calendar = Calendar.getInstance();
        int dayofweek = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayofweek != 1) {
            calendar.add(Calendar.WEEK_OF_MONTH, +1);
        }

        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        calendar.add(Calendar.WEEK_OF_MONTH, -1);

        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        String mystrdate = myFormat.format(calendar.getTime());
        return Timestamp.valueOf(mystrdate);
    }

    /**
     * 功能描述: 字符串日期加n天
     *
     * @param date       日期
     * @param days       加的天数
     * @param dateFormat 字符串日期格式
     * @return {@link String}
     * @method addDay
     * @date 2021/4/18 14:51
     */
    public static String addDay(String date, int days, String dateFormat) {
        if (dateFormat == null || "".equals(dateFormat)) {
            dateFormat = "yyyyMMdd";
        }
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
            Calendar cd = Calendar.getInstance();
            cd.setTime(formatter.parse(date));
            cd.add(Calendar.DATE, days);
            return formatter.format(cd.getTime());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 功能描述: 计算两个字符串的时间相差的时间
     *
     * @param beforeDate 开始时间
     * @param afterDate  结束时间
     * @return {@link Long}
     * @method getDayNum
     * @date 2021/4/18 14:52
     */
    public static Long getDayNum(String beforeDate, String afterDate) {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        long result = 0;
        try {
            long to = df.parse(afterDate).getTime();
            long from = df.parse(beforeDate).getTime();
            result = (to - from) / (1000 * 60 * 60 * 24);
        } catch (ParseException e) {

            e.printStackTrace();
        }
        return result;
    }

    /**
     * 功能描述: 计算日期两个差的月份
     *
     * @param beforeDate 格式 yyyyMM
     * @param afterDate  格式 yyyyMM
     * @return {@link int}
     * @method getMonthNum
     * @date 2021/4/18 14:52
     */
    public static int getMonthNum(String beforeDate, String afterDate) {

        int yearNum = Integer.parseInt(afterDate.substring(0, 4))
            - Integer.parseInt(beforeDate.substring(0, 4));

        int resultMonthNum = 0;

        if (yearNum > 0) {
            resultMonthNum = Integer.parseInt(afterDate.substring(4)) +
                (12 - Integer.parseInt(beforeDate.substring(4))) + 12 * (yearNum - 1);
        } else if (yearNum == 0) {
            resultMonthNum = Integer.parseInt(afterDate.substring(4))
                - Integer.parseInt(beforeDate.substring(4));
        }
        return resultMonthNum;

    }

    /**
     * 功能描述: 获取日期的周
     *
     * @param dt 日期
     * @return {@link String}
     * @method getWeekOfDate
     * @date 2021/4/18 14:53
     */
    public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"7", "1", "2", "3", "4", "5", "6"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);

        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return weekDays[w];
    }

    public static class Java8 {

        // 时间元素
        private static final String YEAR = "year";
        private static final String MONTH = "month";
        private static final String WEEK = "week";
        private static final String DAY = "day";
        private static final String HOUR = "hour";
        private static final String MINUTE = "minute";
        private static final String SECOND = "second";

        // 星期元素
        private static final String MONDAY = "MONDAY";// 星期一
        private static final String TUESDAY = "TUESDAY";// 星期二
        private static final String WEDNESDAY = "WEDNESDAY";// 星期三
        private static final String THURSDAY = "THURSDAY";// 星期四
        private static final String FRIDAY = "FRIDAY";// 星期五
        private static final String SATURDAY = "SATURDAY";// 星期六
        private static final String SUNDAY = "SUNDAY";// 星期日

        // 根据指定格式显示日期和时间
        /**
         * yyyy-MM-dd
         */
        private static final DateTimeFormatter yyyyMMdd_EN = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        /**
         * yyyy-MM-dd HH
         */
        private static final DateTimeFormatter yyyyMMddHH_EN = DateTimeFormatter.ofPattern("yyyy-MM-dd HH");
        /**
         * yyyy-MM-dd HH:mm
         */
        private static final DateTimeFormatter yyyyMMddHHmm_EN = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        /**
         * yyyy-MM-dd HH:mm:ss
         */
        private static final DateTimeFormatter yyyyMMddHHmmss_EN = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        /**
         * HH:mm:ss
         */
        private static final DateTimeFormatter HHmmss_EN = DateTimeFormatter.ofPattern("HH:mm:ss");
        /**
         * yyyy年MM月dd日
         */
        private static final DateTimeFormatter yyyyMMdd_CN = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
        /**
         * yyyy年MM月dd日HH时
         */
        private static final DateTimeFormatter yyyyMMddHH_CN = DateTimeFormatter.ofPattern("yyyy年MM月dd日HH时");
        /**
         * yyyy年MM月dd日HH时mm分
         */
        private static final DateTimeFormatter yyyyMMddHHmm_CN = DateTimeFormatter.ofPattern("yyyy年MM月dd日HH时mm分");
        /**
         * yyyy年MM月dd日HH时mm分ss秒
         */
        private static final DateTimeFormatter yyyyMMddHHmmss_CN = DateTimeFormatter.ofPattern("yyyy年MM月dd日HH时mm分ss秒");
        /**
         * HH时mm分ss秒
         */
        private static final DateTimeFormatter HHmmss_CN = DateTimeFormatter.ofPattern("HH时mm分ss秒");

        // 本地时间显示格式：区分中文和外文显示
        private static final DateTimeFormatter shotDate = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);
        private static final DateTimeFormatter fullDate = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL);
        private static final DateTimeFormatter longDate = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG);
        private static final DateTimeFormatter mediumDate = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);

        /**
         * 功能描述: 获取当前日期（yyyy-MM-dd）
         *
         * @return {@link String}
         * @method getNowDate_EN
         * @date 20W21/4/18 14:54
         */
        public static String getNowDate_EN() {
            return String.valueOf(LocalDate.now());
        }

        /**
         * 功能描述: 获取当前日期时间（yyyy-MM-dd HH:mm:ss）
         *
         * @return {@link String}
         * @method getNowTime_EN
         * @date 2021/4/18 14:54
         */
        public static String getNowTime_EN() {
            return LocalDateTime.now().format(yyyyMMddHHmmss_EN);
        }

        /**
         * 获取当前时间（yyyy-MM-dd HH）
         */
        public static String getNowTime_EN_yMdH() {
            return LocalDateTime.now().format(yyyyMMddHH_EN);
        }

        /**
         * 功能描述: 获取当前时间（yyyy年MM月dd日）
         *
         * @return {@link String}
         * @method getNowTime_CN_yMdH
         * @date 2021/4/18 14:54
         */
        public static String getNowTime_CN_yMdH() {
            return LocalDateTime.now().format(yyyyMMddHH_CN);
        }

        /**
         * 功能描述: 获取当前时间（yyyy-MM-dd HH:mm）
         *
         * @return {@link String}
         * @method getNowTime_EN_yMdHm
         * @date 2021/4/18 14:55
         */
        public static String getNowTime_EN_yMdHm() {
            return LocalDateTime.now().format(yyyyMMddHHmm_EN);
        }

        /**
         * 功能描述: 获取当前时间（yyyy年MM月dd日HH时mm分）
         *
         * @return {@link String}
         * @method getNowTime_CN_yMdHm
         * @date 2021/4/18 14:55
         */
        public static String getNowTime_CN_yMdHm() {
            return LocalDateTime.now().format(yyyyMMddHHmm_CN);
        }

        /**
         * 功能描述: 获取当前时间（HH时mm分ss秒）
         *
         * @return {@link String}
         * @method getNowTime_CN_HHmmss
         * @date 2021/4/18 14:55
         */
        public static String getNowTime_CN_HHmmss() {
            return LocalDateTime.now().format(HHmmss_CN);
        }

        /**
         * 功能描述: 根据日期格式，获取当前时间
         *
         * @param formatStr 日期格式：yyyy，yyyy-MM-dd，yyyy-MM-dd HH:mm:ss，HH:mm:ss
         * @return {@link String}
         * @method getTime
         * @date 2021/4/18 14:56
         */
        public static String getTime(String formatStr) {
            return LocalDateTime.now().format(DateTimeFormatter.ofPattern(formatStr));
        }

        /**
         * 功能描述: 获取中文的当前日期（yyyy年MM月dd日）
         *
         * @return {@link String}
         * @method getNowDate_CN
         * @date 2021/4/18 14:56
         */
        public static String getNowDate_CN() {
            return LocalDate.now().format(yyyyMMdd_CN);
        }

        /**
         * 功能描述: 获取中文当前时间（yyyy年MM月dd日HH时mm分ss秒）
         *
         * @return {@link String}
         * @method getNowTime_CN
         * @date 2021/4/18 14:56
         */
        public static String getNowTime_CN() {
            return LocalDateTime.now().format(yyyyMMddHHmmss_CN);
        }

        /**
         * 功能描述: 简写本地当前日期：yy-M-dd，例如：19-3-30为2019年3月30日
         *
         * @return {@link String}
         * @method getNowLocalTime_shot
         * @date 2021/4/18 14:57
         */
        public static String getNowLocalTime_shot() {
            return LocalDateTime.now().format(shotDate);
        }

        /**
         * 功能描述: 根据当地日期显示格式：yyyy年M月dd日 星期？（中国），形如：2019年3月30日 星期六
         *
         * @return {@link String}
         * @method getNowLocalTime_full
         * @date 2021/4/18 14:57
         */
        public static String getNowLocalTime_full() {
            return LocalDateTime.now().format(fullDate);
        }

        /**
         * 功能描述: 根据当地显示日期格式：yyyy年M月dd日（中国），形如 2019年3月30日
         *
         * @return {@link String}
         * @method getNowLocalTime_long
         * @date 2021/4/18 14:57
         */
        public static String getNowLocalTime_long() {
            return LocalDateTime.now().format(longDate);
        }

        /**
         * 功能描述: 根据当地显示日期格式：yyyy-M-dd（中国），形如：2019-3-30
         *
         * @return {@link String}
         * @method getNowLocalTime_medium
         * @date 2021/4/18 14:57
         */
        public static String getNowLocalTime_medium() {
            return LocalDateTime.now().format(mediumDate);
        }

        /**
         * 功能描述: 获取当前日期的节点时间（年，月，周，日，时，分，秒）,节点数字，如创建此方法的时间：年 2019，月 3，日 30，周 6
         *
         * @param node 日期中的节点元素（年，月，周，日，时，分，秒）
         * @return {@link Integer}
         * @method getNodeTime
         * @date 2021/4/18 14:57
         */
        public static Integer getNodeTime(String node) {
            LocalDateTime today = LocalDateTime.now();
            int resultNode;
            switch (node) {
                case YEAR:
                    resultNode = today.getYear();
                    break;
                case MONTH:
                    resultNode = today.getMonthValue();
                    break;
                case WEEK:
                    resultNode = transformWeekEN2Num(String.valueOf(today.getDayOfWeek()));
                    break;
                case DAY:
                    resultNode = today.getDayOfMonth();
                    break;
                case HOUR:
                    resultNode = today.getHour();
                    break;
                case MINUTE:
                    resultNode = today.getMinute();
                    break;
                case SECOND:
                    resultNode = today.getSecond();
                    break;
                default:
                    // 当前日期是当前年的第几天。例如：2019/1/3是2019年的第三天
                    resultNode = today.getDayOfYear();
                    break;
            }
            return resultNode;
        }

        /**
         * 功能描述: 将英文星期转换成数字
         *
         * @param enWeek 英文星期
         * @return {@link int}
         * @method transformWeekEN2Num
         * @date 2021/4/18 14:58
         */
        public static int transformWeekEN2Num(String enWeek) {
            if (MONDAY.equals(enWeek)) {
                return 1;
            } else if (TUESDAY.equals(enWeek)) {
                return 2;
            } else if (WEDNESDAY.equals(enWeek)) {
                return 3;
            } else if (THURSDAY.equals(enWeek)) {
                return 4;
            } else if (FRIDAY.equals(enWeek)) {
                return 5;
            } else if (SATURDAY.equals(enWeek)) {
                return 6;
            } else if (SUNDAY.equals(enWeek)) {
                return 7;
            } else {
                return -1;
            }
        }

        /**
         * 功能描述: 获取当前日期之后（之后）的节点事件。
         * 比如当前时间为：2019-03-30 10:20:30
         * node="hour",num=5L:2019-03-30 15:20:30
         * node="day",num=1L:2019-03-31 10:20:30
         * node="year",num=1L:2020-03-30 10:20:30
         *
         * @param node 节点元素（“year”,"month","week","day","huor","minute","second"）
         * @param num  第几天（+：之后，-：之前）
         * @return {@link String}
         * @method getAfterOrPreNowTime
         * @date 2021/4/18 14:58
         */
        public static String getAfterOrPreNowTime(String node, Long num) {
            return getString(node, num, yyyyMMddHHmmss_EN);
        }

        /**
         * 功能描述: 获取日期时间变得结果
         *
         * @param node              节点元素（“year”,"month","week","day","huor","minute","second"）
         * @param num               第几天（+：之后，-：之前）
         * @param yyyyMMddHHmmss_en 返回格式
         * @return {@link String}
         * @method getString
         * @date 2021/4/18 14:59
         */
        private static String getString(String node, Long num, DateTimeFormatter yyyyMMddHHmmss_en) {
            LocalDateTime now = LocalDateTime.now();
            if (HOUR.equals(node)) {
                return now.plusHours(num).format(yyyyMMddHHmmss_en);
            } else if (DAY.equals(node)) {
                return now.plusDays(num).format(yyyyMMddHHmmss_en);
            } else if (WEEK.equals(node)) {
                return now.plusWeeks(num).format(yyyyMMddHHmmss_en);
            } else if (MONTH.equals(node)) {
                return now.plusMonths(num).format(yyyyMMddHHmmss_en);
            } else if (YEAR.equals(node)) {
                return now.plusYears(num).format(yyyyMMddHHmmss_en);
            } else if (MINUTE.equals(node)) {
                return now.plusMinutes(num).format(yyyyMMddHHmmss_en);
            } else if (SECOND.equals(node)) {
                return now.plusSeconds(num).format(yyyyMMddHHmmss_en);
            } else {
                return "Node is Error!";
            }
        }

        /**
         * 功能描述: 获取与当前日期相距num个之后（之前）的日期。
         * 比如当前时间为：2019-03-30 10:20:30的格式日期
         * node="hour",num=5L:2019-03-30 15:20:30
         * node="day",num=1L:2019-03-31 10:20:30
         * node="year",num=1L:2020-03-30 10:20:30
         *
         * @param dtf  格式化当前时间格式（dtf = yyyyMMddHHmmss_EN）
         * @param node 节点元素（“year”,"month","week","day","hour","minute","second"）
         * @param num  （+：之后，-：之前）
         * @return {@link String}
         * @method getAfterOrPreNowTimePlus
         * @date 2021/4/18 15:00
         */
        public static String getAfterOrPreNowTimePlus(DateTimeFormatter dtf, String node, Long num) {
            return getString(node, num, dtf);
        }

        /**
         * 功能描述: 当前时间的hour，minute，second之后（之前）的时刻，返回HH:mm:ss 字符串
         *
         * @param node 时间节点元素（hour，minute，second）
         * @param num  之后（之后）多久时，分，秒（+：之后，-：之前）
         * @return {@link String}
         * @method getAfterOrPreNowTimeSimp
         * @date 2021/4/18 15:01
         */
        public static String getAfterOrPreNowTimeSimp(String node, Long num) {
            LocalTime now = LocalTime.now();
            if (HOUR.equals(node)) {
                return now.plusHours(num).format(HHmmss_EN);
            } else if (MINUTE.equals(node)) {
                return now.plusMinutes(num).format(HHmmss_EN);
            } else if (SECOND.equals(node)) {
                return now.plusSeconds(num).format(HHmmss_EN);
            } else {
                return "Node is Error!";
            }
        }

        /**
         * 功能描述: 检查重复事件，比如生日。
         *
         * @param month      月份
         * @param dayOfMonth 天号
         * @return {@link boolean}
         * @method isBirthday
         * @date 2021/4/18 15:02
         */
        public static boolean isBirthday(int month, int dayOfMonth) {
            MonthDay birthDay = MonthDay.of(month, dayOfMonth);
            MonthDay curMonthDay = MonthDay.from(LocalDate.now());// MonthDay只存储了月、日。
            return birthDay.equals(curMonthDay);
        }

        /**
         * 功能描述: 获取当前日期第index日之后(之前)的日期（yyyy-MM-dd）
         *
         * @param index 第index天
         * @return {@link String}
         * @method getAfterOrPreDayDate
         * @date 2021/4/18 15:02
         */
        public static String getAfterOrPreDayDate(int index) {
            return LocalDate.now().plus(index, ChronoUnit.DAYS).format(yyyyMMdd_EN);
        }

        /**
         * 功能描述: 获取当前日期第index周之前（之后）的日期（yyyy-MM-dd）
         *
         * @param index 第index周（+：之后，-：之前）
         * @return {@link String}
         * @method getAfterOrPreWeekDate
         * @date 2021/4/18 15:02
         */
        public static String getAfterOrPreWeekDate(int index) {
            return LocalDate.now().plus(index, ChronoUnit.WEEKS).format(yyyyMMdd_EN);
        }

        /**
         * 功能描述: 获取当前日期第index月之前（之后）的日期（yyyy-MM-dd）
         *
         * @param index 第index月（+：之后，-：之前）
         * @return {@link String}
         * @method getAfterOrPreMonthDate
         * @date 2021/4/18 15:03
         */
        public static String getAfterOrPreMonthDate(int index) {
            return LocalDate.now().plus(index, ChronoUnit.MONTHS).format(yyyyMMdd_EN);
        }

        /**
         * 功能描述: 获取当前日期第index年之前（之后）的日期（yyyy-MM-dd）
         *
         * @param index 第index年（+：之后，-：之前）
         * @return {@link String}
         * @method getAfterOrPreYearDate
         * @date 2021/4/18 15:03
         */
        public static String getAfterOrPreYearDate(int index) {
            return LocalDate.now().plus(index, ChronoUnit.YEARS).format(yyyyMMdd_EN);
        }

        /**
         * 功能描述: 获取指定日期之前之后的第index的日，周，月，年的日期，返回yyyy-MM-dd 日期字符串
         *
         * @param date  指定日期格式：yyyy-MM-dd
         * @param node  时间节点元素（日周月年）
         * @param index 之前之后第index个日期
         * @return {@link String}
         * @method getAfterOrPreDate
         * @date 2021/4/18 15:03
         */
        public static String getAfterOrPreDate(String date, String node, int index) {
            date = date.trim();
            if (DAY.equals(node)) {
                return LocalDate.parse(date).plus(index, ChronoUnit.DAYS).format(yyyyMMdd_EN);
            } else if (WEEK.equals(node)) {
                return LocalDate.parse(date).plus(index, ChronoUnit.WEEKS).format(yyyyMMdd_EN);
            } else if (MONTH.equals(node)) {
                return LocalDate.parse(date).plus(index, ChronoUnit.MONTHS).format(yyyyMMdd_EN);
            } else if (YEAR.equals(node)) {
                return LocalDate.parse(date).plus(index, ChronoUnit.YEARS).format(yyyyMMdd_EN);
            } else {
                return "Wrong date format!";
            }
        }

        /**
         * 功能描述: 检测：输入年份是否是闰年？返回：true：闰年，false：平年
         *
         * @param date 日期格式：yyyy-MM-dd
         * @return {@link boolean}
         * @method isLeapYear
         * @date 2021/4/18 15:04
         */
        public static boolean isLeapYear(String date) {
            return LocalDate.parse(date.trim()).isLeapYear();
        }

        /**
         * 功能描述: 计算两个日期字符串之间相差多少个周期（天，月，年）
         *
         * @param date1 yyyy-MM-dd
         * @param date2 yyyy-MM-dd
         * @param node  三者之一:(day，month,year)
         * @return {@link int}
         * @method peridCount
         * @date 2021/4/18 15:04
         */
        public static int peridCount(String date1, String date2, String node) {
            date1 = date1.trim();
            date2 = date2.trim();
            if (DAY.equals(node)) {
                return Period.between(LocalDate.parse(date1), LocalDate.parse(date2)).getDays();
            } else if (MONTH.equals(node)) {
                return Period.between(LocalDate.parse(date1), LocalDate.parse(date2)).getMonths();
            } else if (YEAR.equals(node)) {
                return Period.between(LocalDate.parse(date1), LocalDate.parse(date2)).getYears();
            } else {
                return 0;
            }
        }

        /**
         * 功能描述: 切割日期。按照周期切割成小段日期段。
         * 示例1：startDate="2019-02-28",endDate="2019-03-05",period="day"
         * 结果为：[2019-02-28, 2019-03-01, 2019-03-02, 2019-03-03, 2019-03-04, 2019-03-05]
         * 示例2：startDate="2019-02-28",endDate="2019-03-25",period="week"
         * 结果为：[2019-02-28,2019-03-06, 2019-03-07,2019-03-13, 2019-03-14,2019-03-20,2019-03-21,2019-03-25]
         * 示例3：startDate="2019-02-28",endDate="2019-05-25",period="month"
         * 结果为：[2019-02-28,2019-02-28, 2019-03-01,2019-03-31, 2019-04-01,2019-04-30,2019-05-01,2019-05-25]
         * 示例4：startDate="2019-02-28",endDate="2020-05-25",period="year"
         * 结果为：[2019-02-28,2019-12-31, 2020-01-01,2020-05-25]
         *
         * @param startDate 开始日期（yyyy-MM-dd）
         * @param endDate   结束日期（yyyy-MM-dd）
         * @param period    周期（天，周，月，年）
         * @return {@link List<String>}
         * @method getPieDateRange
         * @date 2021/4/18 15:05
         */
        public static List<String> getPieDateRange(String startDate, String endDate, String period) {
            List<String> result = new ArrayList<>();
            LocalDate end = LocalDate.parse(endDate, yyyyMMdd_EN);
            LocalDate start = LocalDate.parse(startDate, yyyyMMdd_EN);
            LocalDate tmp = start;
            switch (period) {
                case DAY:
                    while (start.isBefore(end) || start.isEqual(end)) {
                        result.add(start.toString());
                        start = start.plusDays(1);
                    }
                    break;
                case WEEK:
                    while (tmp.isBefore(end) || tmp.isEqual(end)) {
                        if (tmp.plusDays(6).isAfter(end)) {
                            result.add(tmp.toString() + "," + end);
                        } else {
                            result.add(tmp.toString() + "," + tmp.plusDays(6));
                        }
                        tmp = tmp.plusDays(7);
                    }
                    break;
                case MONTH:
                    while (tmp.isBefore(end) || tmp.isEqual(end)) {
                        LocalDate lastDayOfMonth = tmp.with(TemporalAdjusters.lastDayOfMonth());
                        if (lastDayOfMonth.isAfter(end)) {
                            result.add(tmp.toString() + "," + end);
                        } else {
                            result.add(tmp.toString() + "," + lastDayOfMonth);
                        }
                        tmp = lastDayOfMonth.plusDays(1);
                    }
                    break;
                case YEAR:
                    while (tmp.isBefore(end) || tmp.isEqual(end)) {
                        LocalDate lastDayOfYear = tmp.with(TemporalAdjusters.lastDayOfYear());
                        if (lastDayOfYear.isAfter(end)) {
                            result.add(tmp.toString() + "," + end);
                        } else {
                            result.add(tmp.toString() + "," + lastDayOfYear);
                        }
                        tmp = lastDayOfYear.plusDays(1);
                    }
                    break;
                default:
                    break;
            }
            return result;
        }

        /**
         * 功能描述: 指定日期月的最后一天（yyyy-MM-dd）
         *
         * @param curDate     日期格式（yyyy-MM-dd）
         * @param firstOrLast true：第一天，false：最后一天
         * @return {@link String}
         * @method getLastDayOfMonth
         * @date 2021/4/18 15:06
         */
        public static String getLastDayOfMonth(String curDate, boolean firstOrLast) {
            if (firstOrLast) {
                return LocalDate.parse(curDate, yyyyMMdd_EN).with(TemporalAdjusters.firstDayOfMonth()).toString();
            } else {
                return LocalDate.parse(curDate, yyyyMMdd_EN).with(TemporalAdjusters.lastDayOfMonth()).toString();
            }
        }

        /**
         * 功能描述: 指定日期年的最后一天（yyyy-MM-dd）
         *
         * @param curDate     指定日期（格式：yyyy-MM-dd）
         * @param firstOrLast true:第一天，false:最后一天
         * @return {@link String}
         * @method getLastDayOfYear
         * @date 2021/4/18 15:06
         */
        public static String getLastDayOfYear(String curDate, boolean firstOrLast) {
            if (firstOrLast) {
                return LocalDate.parse(curDate, yyyyMMdd_EN).with(TemporalAdjusters.firstDayOfYear()).toString();
            } else {
                return LocalDate.parse(curDate, yyyyMMdd_EN).with(TemporalAdjusters.lastDayOfYear()).toString();
            }
        }

        /**
         * 功能描述: 获取下一个星期的日期（yyyy-MM-dd）
         *
         * @param curDay          yyyy-MM-dd
         * @param dayOfWeek       monday:1~sunday:7
         * @param isContainCurDay 是否包含当天，true：是，false：不包含
         * @return {@link String}
         * @method getNextWeekDate
         * @date 2021/4/18 15:06
         */
        public static String getNextWeekDate(String curDay, int dayOfWeek, boolean isContainCurDay) {
            dayOfWeek = dayOfWeek < 1 || dayOfWeek > 7 ? 1 : dayOfWeek;
            if (isContainCurDay) {
                return LocalDate.parse(curDay).with(TemporalAdjusters.nextOrSame(DayOfWeek.of(dayOfWeek))).toString();
            } else {
                return LocalDate.parse(curDay).with(TemporalAdjusters.next(DayOfWeek.of(dayOfWeek))).toString();
            }
        }

        /**
         * 功能描述: 获取上一个星期的日期（yyyy-MM-dd）
         *
         * @param curDay    指定日期（yyyy-MM-dd）
         * @param dayOfWeek 数字范围（monday:1~sunday:7）
         * @param isCurDay  是否包含当天，true：是，false：不包含
         * @return {@link String}
         * @method getPreWeekDate
         * @date 2021/4/18 15:07
         */
        public static String getPreWeekDate(String curDay, int dayOfWeek, boolean isCurDay) {
            dayOfWeek = dayOfWeek < 1 || dayOfWeek > 7 ? 1 : dayOfWeek;
            if (isCurDay) {
                return LocalDate.parse(curDay).with(TemporalAdjusters.previousOrSame(DayOfWeek.of(dayOfWeek))).toString();
            } else {
                return LocalDate.parse(curDay).with(TemporalAdjusters.previous(DayOfWeek.of(dayOfWeek))).toString();
            }
        }

        /**
         * 功能描述: 获取指定日期当月的最后或第一个星期日期（yyyy-MM-dd）
         *
         * @param curDay      指定日期（yyyy-MM-dd）
         * @param dayOfWeek   周几（1~7）
         * @param lastOrFirst true：最后一个，false本月第一个
         * @return {@link String}
         * @method getFirstOrLastWeekDate
         * @date 2021/4/18 15:07
         */
        public static String getFirstOrLastWeekDate(String curDay, int dayOfWeek, boolean lastOrFirst) {
            dayOfWeek = dayOfWeek < 1 || dayOfWeek > 7 ? 1 : dayOfWeek;
            if (lastOrFirst) {
                return LocalDate.parse(curDay).with(TemporalAdjusters.lastInMonth(DayOfWeek.of(dayOfWeek))).toString();
            } else {
                return LocalDate.parse(curDay).with(TemporalAdjusters.firstInMonth(DayOfWeek.of(dayOfWeek))).toString();
            }
        }
    }
}