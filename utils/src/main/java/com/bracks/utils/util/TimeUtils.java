package com.bracks.utils.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * good programmer.
 *
 * @date : 2019-03-01 下午 06:27
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class TimeUtils {

    /******************************************************自定义************************************************************/

    /**
     * @return 判断一个时间是不是上午
     */
    public static boolean isMorning(long when) {
        android.text.format.Time time = new android.text.format.Time();
        time.set(when);

        int hour = time.hour;
        return (hour >= 0) && (hour < 12);
    }

    /**
     * @return 判断一个时间是不是昨天
     */
    public static boolean isYesterday(long when) {
        android.text.format.Time time = new android.text.format.Time();
        time.set(when);

        int thenYear = time.year;
        int thenMonth = time.month;
        int thenMonthDay = time.monthDay;

        time.set(System.currentTimeMillis());
        return (thenYear == time.year)
                && (thenMonth == time.month)
                && (time.monthDay - thenMonthDay == 1);
    }

    /**
     * @return 判断一个时间是不是今年
     */
    public static boolean isCurrentYear(long when) {
        android.text.format.Time time = new android.text.format.Time();
        time.set(when);

        int thenYear = time.year;

        time.set(System.currentTimeMillis());
        return (thenYear == time.year);
    }

    /**
     * 改变日期格式:例如输入"yyyy-MM-dd",和"MM-dd"
     * 表示去除年份
     *
     * @param time
     * @return
     */
    public static String changeDate(String time, String pattern1, String pattern2) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern1, Locale.getDefault());
        SimpleDateFormat sdf_ = new SimpleDateFormat(pattern2, Locale.getDefault());
        try {
            Date date = sdf.parse(time);
            return sdf_.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 测试运行时时间
     *
     * @return
     */
    public static String testRunTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd_HH:mm:ss", Locale.getDefault());
        String curTime = format.format(System.currentTimeMillis());
        System.out.println(curTime);
        return curTime;
    }
}
