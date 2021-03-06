package org.jujubeframework.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * 日期工具类
 *
 * @author John Li
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Dates {
    public static final int THIRTEEN = 13;
    public static final int TEN = 10;
    public static final int SEVEN = 7;
    private static Logger logger = LoggerFactory.getLogger(Dates.class);

    private static final String[] DEFAULT_PATTERNS = {"yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM-dd HH", "yyyy-MM-dd"};

    /**
     * 格式化时间
     *
     * @param date    待格式化的时间
     * @param pattern 格式化规则
     */
    public static String formatDate(Date date, String pattern) {
        if (date == null) {
            return "";
        }

        String thisPattern = DEFAULT_PATTERNS[0];
        if (StringUtils.isNotBlank(pattern)) {
            thisPattern = pattern;
        }
        return DateFormatUtils.format(date, thisPattern);
    }

    /**
     * 格式化时间
     *
     * @param date    待格式化的时间
     * @param pattern 格式化规则
     * @param zone    时区
     */
    public static String formatDate(Date date, String pattern, TimeZone zone) {
        if (date == null) {
            return "";
        }
        String thisPattern = DEFAULT_PATTERNS[0];
        if (StringUtils.isNotBlank(pattern)) {
            thisPattern = pattern;
        }
        return DateFormatUtils.format(date, thisPattern, zone);
    }

    /**
     * 格式化时间
     *
     * @param time    待格式化的时间
     * @param pattern 格式化规则
     */
    public static String formatTimeMillis(Long time, String pattern) {
        time = time == null ? 0L : time;
        int len = time.toString().length();
        if (!(len == THIRTEEN || len == TEN)) {
            return "";
        }

        Date date = null;
        // 毫秒
        if (len == THIRTEEN) {
            date = new Date(time);
        }
        // 秒
        else if (len == TEN) {
            date = new Date(time * 1000);
        }
        return formatDate(date, pattern);
    }

    /**
     * 按照{yyyy-MM-dd}格式化时间
     *
     * @param times epoch的秒值或毫秒值
     */
    public static String formatTimeMillisByDatePattern(long times) {
        return formatTimeMillis(times, DEFAULT_PATTERNS[3]);
    }

    /**
     * 按照{yyyy-MM-dd HH:mm:ss}格式化时间
     *
     * @param times epoch的秒值或毫秒值
     */
    public static String formatTimeMillisByFullDatePattern(long times) {
        return formatTimeMillis(times, DEFAULT_PATTERNS[0]);
    }

    /**
     * 根据pattern规则转换字符串为Date
     */
    public static Date parse(String source, String pattern) {
        try {
            return new SimpleDateFormat(pattern).parse(source);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据pattern和时区转换字符串为Date
     */
    public static Date parse(String source, String pattern, TimeZone timeZone) {
        try {
            DateFormat dateFormat = new SimpleDateFormat(pattern);
            dateFormat.setTimeZone(timeZone);
            return dateFormat.parse(source);
        } catch (ParseException e) {
            logger.error("dates", e);
        }
        return null;
    }

    /**
     * 根据{@link Dates#DEFAULT_PATTERNS}转换字符串为Date
     */
    public static Date parse(String source) {
        for (String pattern : DEFAULT_PATTERNS) {
            try {
                return new SimpleDateFormat(pattern).parse(source);
            } catch (ParseException e) {
                continue;
            }
        }
        throw new RuntimeException("找不到适合的pattern");
    }

    /**
     * @see Dates#parse(String, String)
     */
    public static long parseToTimeMillis(String source, String pattern) {
        return parse(source, pattern).getTime();
    }

    /**
     * @see Dates#parse(String)
     */
    public static long parseToTimeMillis(String source) {
        return parse(source).getTime();
    }

    /**
     * 根据时间返回当前是星期几
     *
     * @return 0周日 1周一 2周二 3周三 4周四 5周五 6周六
     */
    public static int getWeekMark(Date date) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        // 获取当前日期是周几
        int week = 0;
        switch (cal.get(GregorianCalendar.DAY_OF_WEEK)) {
            case GregorianCalendar.SUNDAY:
                week = 0;
                break;
            case GregorianCalendar.MONDAY:
                week = 1;
                break;
            case GregorianCalendar.TUESDAY:
                week = 2;
                break;
            case GregorianCalendar.WEDNESDAY:
                week = 3;
                break;
            case GregorianCalendar.THURSDAY:
                week = 4;
                break;
            case GregorianCalendar.FRIDAY:
                week = 5;
                break;
            case GregorianCalendar.SATURDAY:
                week = 6;
                break;

            default:
                break;
        }
        return week;
    }

    /**
     * 获得当前时间的epoch秒值
     */
    public static long now() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 获取当前日期前一个月日期
     */
    public static Date getBeforeByMonth() {
        // 当前日期
        Date date = new Date();
        // 格式化对象
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 日历对象
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // 月份减一
        calendar.add(Calendar.MONTH, -1);
        // 输出格式化的日期
        return parse(sdf.format(calendar.getTime()));
    }

    /**
     * 获得指定日期前(后)x天的日期
     *
     * @param time epoch格式的当前日期
     * @param day  天数（如果day数为负数,说明是此日期前的天数）
     */
    public static Date beforNumDay(long time, int day) {
        Calendar c = Calendar.getInstance();
        c.setTime(parse(Dates.formatTimeMillis(time, "yyyy-MM-dd HH:mm:ss")));
        c.add(Calendar.DAY_OF_YEAR, day);
        return parse(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(c.getTime()));
    }

    /**
     * 获取当前月份的最后一天
     */
    public static int getLastDayOfMonth(Date date) {
        LocalDateTime localDate = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        return localDate.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth();
    }

    /**
     * 获取当前月份的最后一天
     */
    public static int getLastDayOfMonth() {
        return getLastDayOfMonth(new Date());
    }

    /**
     * 获得针对目标时间的倒计时
     *
     * @param descTime epoch格式的目标时间
     * @return 数组中四个元素，依次是：日、时、分、秒
     */
    public static long[] countDown(long descTime) {
        return endDown(now(), descTime);
    }

    /**
     * 计算两个日期之间的差
     *
     * @param startTime epoch格式的开始时间
     * @param endTime   epoch格式的结束时间
     * @return 数组中四个元素，依次是：日、时、分、秒
     */
    public static long[] endDown(long startTime, long endTime) {
        String strTime = String.valueOf(endTime);
        int len = strTime.length();
        Validate.isTrue(len == THIRTEEN || len == TEN, "endTime必须为秒或毫秒");

        String strNow = String.valueOf(startTime);
        int len2 = strNow.length();
        Validate.isTrue(len2 == THIRTEEN || len2 == TEN, "startTime必须为秒或毫秒");

        if (len == THIRTEEN) {
            endTime = endTime / 1000;
        }
        if (len2 == THIRTEEN) {
            startTime = startTime / 1000;
        }

        long[] arr = new long[4];
        long second = endTime - startTime;
        if (second < 0) {
            return null;
        }

        long minite = second / 60;
        long hour = minite / 60;
        long day = hour / 24;

        second = second % 60;
        minite = minite % 60;
        hour = hour % 24;

        arr[0] = day;
        arr[1] = hour;
        arr[2] = minite;
        arr[3] = second % 60;
        return arr;
    }

    /**
     * 据今天结束还有多少秒
     */
    public static long endOfToday() {
        return (maximumTimeMillisOfToday() - System.currentTimeMillis()) / 1000;
    }

    /**
     * 今天的结束时间
     *
     * @return 返回millis值
     */
    public static long maximumTimeMillisOfToday() {
        LocalDateTime localDateTime = LocalDateTime.now();
        return Date.from(localDateTime.with(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant()).getTime();
    }

    /**
     * 今天开始的时间
     *
     * @return 返回millis值
     */
    public static long minimumTimeMillisOfToday() {
        LocalDateTime localDateTime = LocalDateTime.now();
        return Date.from(localDateTime.with(LocalTime.MIN).atZone(ZoneId.systemDefault()).toInstant()).getTime();
    }

    /**
     * 以‘yyyy-MM-dd HH:mm:ss’格式化当前日期
     */
    public static String formatNow() {
        return formatDate(new Date(), null);
    }

    /**
     * 以指定的pattern格式化当前日期
     */
    public static String formatNow(String pattern) {
        return formatDate(new Date(), pattern);
    }

}
