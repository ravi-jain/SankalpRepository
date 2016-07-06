package com.ravijain.sankalp.data;

import android.provider.CalendarContract;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

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

    public static String getDayString(Calendar cal)
    {
        SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEE MMM dd");
        return shortenedDateFormat.format(cal.getTime());
    }

    public static String getMonthString(Calendar cal)
    {
        SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("MMMM, yyyy");
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

    public static String getFriendlyDateString(Date date)
    {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("MMMM dd, yyyy", Locale.US);
        return dateFormatter.format(date);
    }

    public static String getFriendlyDateString(long milliSeconds)
    {
        Date date = new Date(milliSeconds);
        return getFriendlyDateString(date);
    }

    public static boolean isCurrentDate(Date fromDate, Date toDate)
    {
        Date now = new Date();
        return fromDate.before(now) && (toDate == null || toDate.after(now));
    }

    public static boolean isUpcomingDate(Date fromDate)
    {
        Date now = new Date();
        return fromDate.after(now);
    }

}
