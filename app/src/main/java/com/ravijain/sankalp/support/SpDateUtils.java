package com.ravijain.sankalp.support;

import android.provider.CalendarContract;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created by ravijain on 7/4/2016.
 */
public class SpDateUtils {

    /**
     * Given a date, a proper TimeZone, return the beginning date of the specified
     * date and TimeZone. If TimeZone is null, meaning use Defult TimeZone of the
     * JVM.
     */
    public static Date beginOfDate(Calendar cal) {
        // will reset the TimeZone.
        final int day = cal.get(Calendar.DAY_OF_MONTH);
        final int year = cal.get(Calendar.YEAR);
        final int month = cal.get(Calendar.MONTH);
        cal.clear();
        cal.set(year, month, day);
        return cal.getTime();
    }

    public static Date beginOfDate(Date d) {
        if (d == null) return null;
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        return beginOfDate(c);
    }

    public static Date endOfDate(Date d) {
        if (d == null) return null;
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        return endOfDate(c);
    }

    /**
     * Given a date, a proper TimeZone, return the last millisecond date of the
     * specified date and TimeZone. If TimeZone is null, meaning use Defult
     * TimeZone of the JVM.
     */
    public static Date endOfDate(Calendar cal) {
        final int day = cal.get(Calendar.DAY_OF_MONTH);
        final int year = cal.get(Calendar.YEAR);
        final int month = cal.get(Calendar.MONTH);
        cal.clear();
        cal.set(year, month, day + 1);
        cal.setTimeInMillis(cal.getTimeInMillis() - 1);
        return cal.getTime();
    }

    public static Calendar previousDate(Calendar cal) {
        cal.add(Calendar.DAY_OF_YEAR, -1);
        return cal;
    }

    public static Calendar nextDate(Calendar cal) {
        cal.add(Calendar.DAY_OF_YEAR, 1);
        return cal;
    }

    public static String[] getMonthStrings() {
        String[] months = new String[12];
        Calendar c = Calendar.getInstance();
        for (int i = 0; i < 12; i++) {
            c.set(Calendar.MONTH, i);
            months[i] = new SimpleDateFormat("MMMM").format(c.getTime());
        }
        return months;
    }

    public static String getDayString(Calendar cal) {
        SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEEE MMMM dd, yyyy");
        return shortenedDateFormat.format(cal.getTime());
    }

    public static String getNumericalDayString(Calendar cal) {
        return getNumericalDayString(cal.getTime());
    }

    public static String getNumericalDayString(Date d) {
        SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("d/M/yyyy");
        return shortenedDateFormat.format(d);
    }

    public static String getMonthString(Calendar cal) {
        SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("MMMM, yyyy");
        return shortenedDateFormat.format(cal.getTime());
    }

    public static String getYearString(Calendar cal) {
        SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("yyyy");
        return shortenedDateFormat.format(cal.getTime());
    }

    public static Date beginOfMonth(Calendar cal) {

        final int year = cal.get(Calendar.YEAR);
        final int month = cal.get(Calendar.MONTH);
        cal.clear();
        cal.set(year, month, 1);
        return cal.getTime();
    }

    /**
     * Given a date, a proper TimeZone, return the ending date of the month of the
     * specified date and TimeZone. If TimeZone is null, meaning use default
     * TimeZone of the JVM.
     */
    public static Date endOfMonth(Calendar cal) {

        final int year = cal.get(Calendar.YEAR);
        final int month = cal.get(Calendar.MONTH);
        final int monthDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        cal.clear();
        cal.set(year, month, monthDays + 1);
        cal.setTimeInMillis(cal.getTimeInMillis() - 1);
        return cal.getTime();
    }

    /**
     * Given a date, a proper TimeZone, return the beginning date of the month of
     * the specified date and TimeZone. If TimeZone is null, meaning use default
     * TimeZone of the JVM.
     */
    public static Date beginOfYear(Calendar cal) {

        final int year = cal.get(Calendar.YEAR);
        cal.clear();
        cal.set(year, Calendar.JANUARY, 1);
        return cal.getTime();
    }

    /**
     * Given a date, a proper TimeZone, return the ending date of the month of the
     * specified date and TimeZone. If TimeZone is null, meaning use default
     * TimeZone of the JVM.
     */
    public static Date endOfYear(Calendar cal) {

        final int year = cal.get(Calendar.YEAR);
        cal.clear();
        cal.set(year + 1, Calendar.JANUARY, 1);
        cal.setTimeInMillis(cal.getTimeInMillis() - 1);
        return cal.getTime();
    }

    public static String yearOfDate(Calendar cal) {

        return String.valueOf(cal.get(Calendar.YEAR));
    }

    public static String getFriendlyDateString(Date date) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("MMMM dd, yyyy", Locale.US);
        return dateFormatter.format(date);
    }

    public static String getFriendlyDateShortString(Date date) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
        return dateFormatter.format(date);
    }

    public static String getFriendlyDateString(long milliSeconds) {
        Date date = new Date(milliSeconds);
        return getFriendlyDateString(date);
    }

    public static boolean isCurrentDate(Date fromDate, Date toDate) {
        Date now = new Date();
        return fromDate.before(now) && (toDate == null || toDate.after(now));
    }

    public static boolean isUpcomingDate(Date fromDate) {
        Date now = new Date();
        return fromDate.after(now);
    }

    public static String getFriendlyPeriodString(Date fromDate, Date toDate, boolean shortRepresentation) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(fromDate);

        Calendar c2 = Calendar.getInstance();
        c2.setTime(toDate);

        int year1 = c1.get(Calendar.YEAR);
        int year2 = c2.get(Calendar.YEAR);
        if (year1 == year2) {
            int month1 = c1.get(Calendar.MONTH);
            int month2 = c2.get(Calendar.MONTH);

            if (month1 == month2) {
                if (isBeginOfMonth(c1) && isEndOfMonth(c2)) {
                    return getMonthString(c1);
                } else {
                    if (isSameDay(c1, c2)) {
                        if (isToday(c1)) {
                            return "Today";
                        } else if (isSameDay(c1, nextDate(Calendar.getInstance()))) {
                            return "Tomorrow";
                        } else {
                            return shortRepresentation ? getNumericalDayString(c1) : getDayString(c1);
                        }

                    }
                }
            } else {
                if (month1 == Calendar.JANUARY && month2 == Calendar.DECEMBER
                        && isBeginOfMonth(c1) && isEndOfMonth(c2)) {
                    return String.valueOf(year1);
                }
            }
        }

        return getNumericalDayString(fromDate) + " - " + getNumericalDayString(toDate);

    }

    /**
     * <p>Checks if two calendars represent the same day ignoring time.</p>
     *
     * @param cal1 the first calendar, not altered, not null
     * @param cal2 the second calendar, not altered, not null
     * @return true if they represent the same day
     * @throws IllegalArgumentException if either calendar is <code>null</code>
     */
    public static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }

    /**
     * <p>Checks if a calendar date is today.</p>
     *
     * @param cal the calendar, not altered, not null
     * @return true if cal date is today
     * @throws IllegalArgumentException if the calendar is <code>null</code>
     */
    public static boolean isToday(Calendar cal) {
        return isSameDay(cal, Calendar.getInstance());
    }

    public static boolean isToday(Date d) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        return isToday(c);
    }

    public static boolean isTomorrow(Date d) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        return isSameDay(c, nextDate(Calendar.getInstance()));
    }

    public static boolean isCurrentMonth(Date d) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(d);

        Calendar c2 = Calendar.getInstance();

        int year1 = c1.get(Calendar.YEAR);
        int year2 = c2.get(Calendar.YEAR);
        if (year1 == year2) {
            int month1 = c1.get(Calendar.MONTH);
            int month2 = c2.get(Calendar.MONTH);

            if (month1 == month2) {
                return true;
            }
        }
        return false;
    }

    public static boolean isCurrentYear(Date d) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(d);

        Calendar c2 = Calendar.getInstance();

        int year1 = c1.get(Calendar.YEAR);
        int year2 = c2.get(Calendar.YEAR);
        if (year1 == year2) {
            return true;
        }
        return false;
    }

    public static boolean isBeginOfMonth(Calendar c) {
        return c.get(Calendar.DAY_OF_MONTH) == 1;
    }

    public static boolean isEndOfMonth(Calendar c) {
        int day = c.get(Calendar.DAY_OF_MONTH);
        int maxDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        return day == maxDay;
    }

    /**
     * Date Arithmetic function (date2 - date1). subtract a date from another
     * date, return the difference as the required fields. E.g. if specified
     * Calendar.Date, the smaller range of fields is ignored and this method
     * return the difference of days.
     *
     * @param date2 The date2.
     * @param field The time field; e.g., Calendar.DATE, Calendar.YEAR, it's default
     *              value is Calendar.DATE
     * @param date1 The date1.
     */
    public static final long subtract(Date date2, Date date1, int field) {

        boolean negative = false;
        if (date1.after(date2)) {
            negative = true;
            final Date d = date1;
            date1 = date2;
            date2 = d;
        }

        final Calendar cal1 = Calendar.getInstance();
        cal1.setTimeInMillis(date1.getTime());// don't call cal.setTime(Date) which
        // will reset the TimeZone.

        final Calendar cal2 = Calendar.getInstance();
        cal2.setTimeInMillis(date2.getTime());// don't call cal.setTime(Date) which
        // will reset the TimeZone.

        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);

        switch (field) {
            case Calendar.YEAR: {
                return negative ? (year1 - year2) : (year2 - year1);
            }
            case Calendar.MONTH: {
                int month1 = cal1.get(Calendar.MONTH);
                int month2 = cal2.get(Calendar.MONTH);
                int months = 12 * (year2 - year1) + month2 - month1;
                return negative ? -months : months;
            }
            case Calendar.HOUR: {
                long time1 = date1.getTime();
                long time2 = date2.getTime();
                long min1 = (time1 < 0 ? (time1 - (1000 * 60 * 60 - 1)) : time1) / (1000 * 60 * 60);
                long min2 = (time2 < 0 ? (time2 - (1000 * 60 * 60 - 1)) : time2) / (1000 * 60 * 60);
                return negative ? (min1 - min2) : (min2 - min1);
            }
            case Calendar.MINUTE: {
                long time1 = date1.getTime();
                long time2 = date2.getTime();
                long min1 = (time1 < 0 ? (time1 - (1000 * 60 - 1)) : time1) / (1000 * 60);
                long min2 = (time2 < 0 ? (time2 - (1000 * 60 - 1)) : time2) / (1000 * 60);
                return negative ? (min1 - min2) : (min2 - min1);
            }
            case Calendar.SECOND: {
                long time1 = date1.getTime();
                long time2 = date2.getTime();
                long sec1 = (time1 < 0 ? (time1 - (1000 - 1)) : time1) / 1000;
                long sec2 = (time2 < 0 ? (time2 - (1000 - 1)) : time2) / 1000;

                return negative ? (sec1 - sec2) : (sec2 - sec1);
            }
            case Calendar.MILLISECOND: {
                return negative ? (date1.getTime() - date2.getTime()) : (date2.getTime() - date1.getTime());
            }
            case Calendar.DATE:
            default: /* default, like -1 */ {
                int day1 = cal1.get(Calendar.DAY_OF_YEAR);
                int day2 = cal2.get(Calendar.DAY_OF_YEAR);

                int maxDay1 = year1 == year2 ? 0 : cal1.getActualMaximum(Calendar.DAY_OF_YEAR);
                int days = maxDay1 - day1 + day2;

                final Calendar cal = Calendar.getInstance();
                for (int year = year1 + 1; year < year2; year++) {
                    cal.set(Calendar.YEAR, year);
                    days += cal.getActualMaximum(Calendar.DAY_OF_YEAR);
                }
                return negative ? -days : days;
            }
        }
    }
}
