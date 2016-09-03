package com.ravijain.sankalp.support;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by ravijain on 7/4/2016.
 */
public class DateUtility {

    /**
     * Returns the current time without rounding.
     */
    public static final Date now() {
        return new Date();
    }

    /**
     * Returns today by setting time to 0:0:0.
     */
    public static final Date today() {
        return beginOfDate(new Date(), null);
    }

    /**
     * Given a date, return the previouse date of the given date (24 hrs before).
     */
    final public static Date previousDate(Date when) {
        long time = when.getTime() - 24 * 60 * 60 * 1000;
        return new Date(time);
    }

    /**
     * Return the beginning date of this month.
     */
    final public static Date beginOfMonth() {
        return beginOfMonth(new Date(), null);
    }

    /**
     * Given a date, a proper TimeZone, return the beginning date of the month of
     * the specified date and TimeZone. If TimeZone is null, meaning use default
     * TimeZone of the JVM.
     */
    final public static Date beginOfMonth(Date when, TimeZone tz) {
//        if (tz == null)
//            tz = TimeZones.getCurrent();
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(when.getTime()); // don't call cal.setTime(Date) which
        // will reset the TimeZone.

        final int year = cal.get(Calendar.YEAR);
        final int month = cal.get(Calendar.MONTH);
        cal.clear();
        cal.set(year, month, 1);
        return cal.getTime();
    }

    /**
     * Return the ending date of this month.
     */
    final public static Date endOfMonth() {
        return endOfMonth(new Date(), null);
    }

    /**
     * Given a date, a proper TimeZone, return the ending date of the month of the
     * specified date and TimeZone. If TimeZone is null, meaning use default
     * TimeZone of the JVM.
     */
    final public static Date endOfMonth(Date when, TimeZone tz) {
//        if (tz == null)
//            tz = TimeZones.getCurrent();
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(when.getTime()); // don't call cal.setTime(Date) which
        // will reset the TimeZone.

        final int year = cal.get(Calendar.YEAR);
        final int month = cal.get(Calendar.MONTH);
        final int monthDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        cal.clear();
        cal.set(year, month, monthDays + 1);
        cal.setTimeInMillis(cal.getTimeInMillis() - 1);
        return cal.getTime();
    }

    /**
     * Whether the given date in the specified TimeZone is the last day of that
     * month. If TimeZone is null, meaning use default TimeZone of the JVM.
     */
    final public static boolean isEndOfMonth(Date when, TimeZone tz) {
//        if (tz == null)
//            tz = TimeZones.getCurrent();
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(when.getTime()); // don't call cal.setTime(Date) which
        // will reset the TimeZone.

        final int day = cal.get(Calendar.DAY_OF_MONTH);
        final int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        return day == maxDay;
    }

    /**
     * Whether the given date in the specified TimeZone is the first day of that
     * month. If TimeZone is null, meaning use default TimeZone of the JVM.
     */
    final public static boolean isBeginOfMonth(Date when, TimeZone tz) {
//        if (tz == null)
//            tz = TimeZones.getCurrent();
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(when.getTime()); // don't call cal.setTime(Date) which
        // will reset the TimeZone.

        final int day = cal.get(Calendar.DAY_OF_MONTH);
        return day == 1;
    }

    /**
     * Given a date, a proper TimeZone, return the beginning date of the specified
     * date and TimeZone. If TimeZone is null, meaning use Defult TimeZone of the
     * JVM.
     */
    final public static Date beginOfDate(Date when, TimeZone tz) {
//        if (tz == null)
//            tz = TimeZones.getCurrent();

        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(when.getTime());// don't call cal.setTime(Date) which
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
    final public static Date endOfDate(Date when, TimeZone tz) {
//        if (tz == null)
//            tz = TimeZones.getCurrent();

        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(when.getTime());// don't call cal.setTime(Date) which
        // will reset the TimeZone.
        final int day = cal.get(Calendar.DAY_OF_MONTH);
        final int year = cal.get(Calendar.YEAR);
        final int month = cal.get(Calendar.MONTH);
        cal.clear();
        cal.set(year, month, day + 1);
        cal.setTimeInMillis(cal.getTimeInMillis() - 1);

        return cal.getTime();
    }

    /**
     * Return the beginning date of this year.
     */
    final public static Date beginOfYear() {
        return beginOfYear(new Date(), null);
    }

    /**
     * Given a date, a proper TimeZone, return the beginning date of the month of
     * the specified date and TimeZone. If TimeZone is null, meaning use default
     * TimeZone of the JVM.
     */
    final public static Date beginOfYear(Date when, TimeZone tz) {
//        if (tz == null)
//            tz = TimeZones.getCurrent();
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(when.getTime()); // don't call cal.setTime(Date) which
        // will reset the TimeZone.

        final int year = cal.get(Calendar.YEAR);
        cal.clear();
        cal.set(year, Calendar.JANUARY, 1);
        return cal.getTime();
    }

    /**
     * Return the ending date of this year.
     */
    final public static Date endOfYear() {
        return endOfYear(new Date(), null);
    }

    /**
     * Given a date, a proper TimeZone, return the ending date of the month of the
     * specified date and TimeZone. If TimeZone is null, meaning use default
     * TimeZone of the JVM.
     */
    final public static Date endOfYear(Date when, TimeZone tz) {
//        if (tz == null)
//            tz = TimeZones.getCurrent();
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(when.getTime()); // don't call cal.setTime(Date) which
        // will reset the TimeZone.

        final int year = cal.get(Calendar.YEAR);
        cal.clear();
        cal.set(year + 1, Calendar.JANUARY, 1);
        cal.setTimeInMillis(cal.getTimeInMillis() - 1);
        return cal.getTime();
    }

//    /**
//     * Return the ending date of this year.
//     */
//    final public static short twoMonthShort() {
//        return twoMonthShort(new Date(), null);
//    }
//
//    /**
//     * Given a date, a proper TimeZone, return the two month int. eg. 1, 3, 5, 7,
//     * 9, 11. If TimeZone is null, meaning use default TimeZone of the JVM.
//     */
//    final public static short twoMonthShort(Date when, TimeZone tz) {
//        if (tz == null)
//            tz = TimeZones.getCurrent();
//        final Calendar cal = Calendar.getInstance(tz);
//        cal.setTimeInMillis(when.getTime()); // don't call cal.setTime(Date) which
//        // will reset the TimeZone.
//
//        final int month = (cal.get(Calendar.MONTH) / 2) * 2 + 1;
//        return (short) month;
//    }

    /**
     * Get the year of a date.
     *
     * @param when
     *          The date.
     * @param tz
     *          The time zone; if null, the current time zone is assumed.
     * @see
     */
    public static final int yearOfDate(Date when, TimeZone tz) {
//        if (tz == null)
//            tz = TimeZones.getCurrent();
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(when.getTime()); // don't call cal.setTime(Date) which
        // will reset the TimeZone.

        return cal.get(Calendar.YEAR);
    }

    /**
     * Get the year of a date in the specified locale.
     *
     * <p>
     * Currenty, only Locale.ZH_TW is supported, i.e., "year - 1911" and it's may
     * be less than 0. Otherwise, it is the same as {@link #yearOfDate}.
     *
     * @param when
     *          The date.
     * @param locale
     *          the locale; if null, the current locale is assumed.
     * @param tz
     *          The time zone; if null, the current time zone is assumed.
     * @see #yearOfDate
     */
//    public static final int localizedYearOfDate(Date when, Locale locale, TimeZone tz) {
//        final int year = yearOfDate(when, tz);
//        if (locale.equals(Locale.TAIWAN))
//            return year - 1911;
//        return year;
//    }

    /**
     * Get the month of a date. The first month of the year is JANUARY which is 0.
     *
     * @param when
     *          The date.
     * @param tz
     *          The time zone; if null, the current time zone is assumed.
     */
    public static final int monthOfDate(Date when, TimeZone tz) {
//        if (tz == null)
//            tz = TimeZones.getCurrent();
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(when.getTime()); // don't call cal.setTime(Date) which
        // will reset the TimeZone.

        return cal.get(Calendar.MONTH);
    }

    /**
     * Get the month of a date. The first month of the year is JANUARY which is 1.
     *
     * @param when
     *          The date.
     * @param tz
     *          The time zone; if null, the current time zone is assumed.
     */
//    public static final int monthOfDatePlus1(Date when, TimeZone tz) {
//        if (tz == null)
//            tz = TimeZones.getCurrent();
//        final Calendar cal = Calendar.getInstance(tz);
//        cal.setTimeInMillis(when.getTime()); // don't call cal.setTime(Date) which
//        // will reset the TimeZone.
//
//        return cal.get(Calendar.MONTH) + 1;
//    }

    /**
     * Get the day of month of a date. The first day of the month has value 1.
     *
     * @param when
     *          The date.
     * @param tz
     *          The time zone; if null, the current time zone is assumed.
     */
    public static final int dayMonthOfDate(Date when, TimeZone tz) {
//        if (tz == null)
//            tz = TimeZones.getCurrent();
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(when.getTime()); // don't call cal.setTime(Date) which
        // will reset the TimeZone.

        return cal.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Date Arithmetic function. Adds the specified (signed) amount of time to the
     * given date, based on the calendar's rules.
     *
     * @param when
     *          The based date.
     * @param tz
     *          The time zone; if null, the current time zone is assumed.
     * @param field
     *          The time field.
     * @param amount
     *          The amount of date or time to be added to the field.
     */
    public static final Date add(Date when, TimeZone tz, int field, int amount) {
//        if (tz == null)
//            tz = TimeZones.getCurrent();

        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(when.getTime());// don't call cal.setTime(Date) which
        // will reset the TimeZone.

        cal.add(field, amount);
        return cal.getTime();
    }

    /**
     * Date Arithmetic function (date2 - date1). subtract a date from another
     * date, return the difference as the required fields. E.g. if specified
     * Calendar.Date, the smaller range of fields is ignored and this method
     * return the difference of days.
     *
     * @param date2
     *          The date2.
     * @param tz
     *          The time zone.
     * @param field
     *          The time field; e.g., Calendar.DATE, Calendar.YEAR, it's default
     *          value is Calendar.DATE
     * @param date1
     *          The date1.
     */
    public static final long subtract(Date date2, TimeZone tz, int field, Date date1) {
//        if (tz == null)
//            tz = TimeZones.getCurrent();

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
            default: /* default, like -1 */
            {
                int day1 = cal1.get(Calendar.DAY_OF_YEAR);
                int day2 = cal2.get(Calendar.DAY_OF_YEAR);

                int maxDay1 = year1 == year2 ? 0 : cal1.getActualMaximum(Calendar.DAY_OF_YEAR);
                int days = maxDay1 - day1 + day2;

                final Calendar cal = Calendar.getInstance(tz);
                for (int year = year1 + 1; year < year2; year++) {
                    cal.set(Calendar.YEAR, year);
                    days += cal.getActualMaximum(Calendar.DAY_OF_YEAR);
                }
                return negative ? -days : days;
            }
        }
    }

    /**
     * merge the date part and time part of two specified dates into a date.
     *
     * @param datePart
     *          The date part date.
     * @param timePart
     *          The time part date.
     * @param tz
     *          The time zone.
     */
//    public static final Date merge(Date datePart, Date timePart, TimeZone tz) {
//        if (tz == null)
//            tz = TimeZones.getCurrent();
//
//        final Calendar dateCal = Calendar.getInstance(tz);
//        dateCal.setTimeInMillis(datePart.getTime());// don't call cal.setTime(Date)
//        // which will reset the
//        // TimeZone.
//
//        final Calendar timeCal = Calendar.getInstance(tz);
//        timeCal.setTimeInMillis(timePart.getTime());// don't call cal.setTime(Date)
//        // which will reset the
//        // TimeZone.
//
//        final int hour = timeCal.get(Calendar.HOUR);
//        final int minute = timeCal.get(Calendar.MINUTE);
//        final int second = timeCal.get(Calendar.SECOND);
//        final int msillisecond = timeCal.get(Calendar.MILLISECOND);
//
//        dateCal.set(Calendar.HOUR, hour);
//        dateCal.set(Calendar.MINUTE, minute);
//        dateCal.set(Calendar.SECOND, second);
//        dateCal.set(Calendar.MILLISECOND, msillisecond);
//
//        return dateCal.getTime();
//    }
}