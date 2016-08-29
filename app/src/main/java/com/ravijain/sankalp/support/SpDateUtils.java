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

    public static String getDayString(Calendar cal) {
        SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEEE MMMM dd, yyyy");
        return shortenedDateFormat.format(cal.getTime());
    }

    public static String getNumericalDayString(Calendar cal) {
        SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("d/M/yyyy");
        return shortenedDateFormat.format(cal.getTime());
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

        return getFriendlyDateString(fromDate) + " - " + getFriendlyDateString(toDate);

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
}
