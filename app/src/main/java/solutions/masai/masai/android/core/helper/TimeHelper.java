package solutions.masai.masai.android.core.helper;

import android.content.Context;

import solutions.masai.masai.android.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by Semko on 2016-12-08.
 */

public class TimeHelper {
    private static final int SECOND_MILLIS = 1000;
    public static final int SECONDS_THREE = 3000;
    public static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 86400000;


    public static String getTimeAgo(Context context, long time) {
        if (time < 1000000000000L) {
            time *= 1000;
        }

        long now = System.currentTimeMillis();

        final long diff = now - time;
        if (diff < MINUTE_MILLIS || time > now || time <= 0) {
            return context.getString(R.string.just_now);
        } else if (diff < 2 * MINUTE_MILLIS) {
            return context.getString(R.string.a_minute_ago);
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + context.getString(R.string.minutes_ago);
        } else if (diff < 90 * MINUTE_MILLIS) {
            return context.getString(R.string.an_hour_ago);
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + context.getString(R.string.hours_ago);
        } else if (diff < 48 * HOUR_MILLIS) {
            return context.getString(R.string.yesterday);
        } else {
            return diff / DAY_MILLIS + context.getString(R.string.days_ago);
        }
    }

    public static String getTimeOfDay(Context context, Calendar calendar) {
        String outTime;
        String dateFormat = context.getString(R.string.recent_date_format);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.getDefault());
        outTime = simpleDateFormat.format(calendar.getTime());
        return outTime;
    }

    public static String getHumanReadableDate(Context context, long date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        return TimeHelper.getHumanReadableDate(context, calendar);
    }

    public static String getHumanReadableDate(Context context, Calendar calendar) {
        Calendar today = Calendar.getInstance();
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE, -1);
        String dayName;
        String outTime;
        String dateFormat;
        if (calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {
            dateFormat = context.getString(R.string.recent_date_format);
            dayName = String.format(context.getString(R.string.day_format), context.getString(R.string.today));
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.getDefault());
            outTime = dayName.concat(simpleDateFormat.format(calendar.getTime()));
        } else if (calendar.get(Calendar.YEAR) == yesterday.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) == yesterday.get(Calendar.DAY_OF_YEAR)) {
            dateFormat = context.getString(R.string.recent_date_format);
            dayName = String.format(context.getString(R.string.day_format), context.getString(R.string.yesterday));
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.getDefault());
            outTime = dayName.concat(simpleDateFormat.format(calendar.getTime()));
        } else if (calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) && calendar.get(Calendar.WEEK_OF_YEAR) == today.get(Calendar.WEEK_OF_MONTH)) {
            dateFormat = context.getString(R.string.recent_date_format);
            dayName = String.format(context.getString(R.string.day_format), Calendar.getInstance().getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()));
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.getDefault());
            outTime = dayName.concat(simpleDateFormat.format(calendar.getTime()));
        } else if (calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR)) {
            dateFormat = context.getString(R.string.month_date_format);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.getDefault());
            outTime = simpleDateFormat.format(calendar.getTime());
        } else {
            dateFormat = context.getString(R.string.full_date_format);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.getDefault());
            outTime = simpleDateFormat.format(calendar.getTime());
        }
        return outTime;
    }


    public static String getDurationFromMinutes(Context context, int minutes) {
        int hours = minutes / 60;
        int minutesRest = minutes % 60;
        String out = String.format(context.getString(R.string.flight_duration_format), hours, minutesRest);
        return out;
    }

    public static int getTravelDurationFromDepartureArrivalTimes(String departureTime, String arrivalTime) {
        Calendar departure = getCalendarFromStringYMDHMS(departureTime);
        Calendar arrival = getCalendarFromStringYMDHMS(arrivalTime);
        long durationInMilliseconds = arrival.getTimeInMillis() - departure.getTimeInMillis();
        return (int) (durationInMilliseconds / MINUTE_MILLIS);
    }

    public static Calendar getCalendarFromStringYMD(String dateString) {
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd'Z'", Locale.getDefault()).parse(dateString);
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            return calendar;
        } catch (ParseException | NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Calendar getCalendarFromStringYMDHMS(String dateString) {
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ss.SSS'Z'", Locale.getDefault()).parse(dateString);
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            return calendar;
        } catch (ParseException | NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getDateHM(Calendar calendar) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm aa", Locale.getDefault());
        String time = simpleDateFormat.format(calendar.getTime());
        return time;
    }

    public static String getDateDayNrMonth(Context context, Calendar calendar) {
        String day = getDayString(context, calendar).substring(0, 3);
        String dayNumber = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
        String month = getMonthString(context, calendar).substring(0, 3);
        String out = day.concat("., ").concat(dayNumber).concat(". ").concat(month).concat(".");
        return out;
    }

    public static String getFormattedDateYMD(Context context, String dateString) {
        Calendar calendar = getCalendarFromStringYMD(dateString);
        return getFormattedDate(context, calendar);
    }

    public static String getFormattedDateYMDHMS(Context context, String dateString) {
        Calendar calendar = getCalendarFromStringYMDHMS(dateString);
        return getFormattedDate(context, calendar);
    }

    private static String getMonthString(Context context, Calendar calendar) {
        String monthString = "";
        int monthCode = calendar.get(Calendar.MONTH);
        switch (monthCode) {
            case Calendar.JANUARY:
                monthString = context.getString(R.string.january);
                break;
            case Calendar.FEBRUARY:
                monthString = context.getString(R.string.february);
                break;
            case Calendar.MARCH:
                monthString = context.getString(R.string.march);
                break;
            case Calendar.APRIL:
                monthString = context.getString(R.string.april);
                break;
            case Calendar.MAY:
                monthString = context.getString(R.string.may);
                break;
            case Calendar.JUNE:
                monthString = context.getString(R.string.june);
                break;
            case Calendar.JULY:
                monthString = context.getString(R.string.july);
                break;
            case Calendar.SEPTEMBER:
                monthString = context.getString(R.string.september);
                break;
            case Calendar.OCTOBER:
                monthString = context.getString(R.string.october);
                break;
            case Calendar.NOVEMBER:
                monthString = context.getString(R.string.november);
                break;
            case Calendar.DECEMBER:
                monthString = context.getString(R.string.december);
                break;
        }
        return monthString;
    }

    private static String getDayString(Context context, Calendar calendar) {
        String dayString = "";
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        switch (dayOfWeek) {
            case Calendar.MONDAY:
                dayString = context.getString(R.string.monday);
                break;
            case Calendar.TUESDAY:
                dayString = context.getString(R.string.tuesday);
                break;
            case Calendar.WEDNESDAY:
                dayString = context.getString(R.string.wednesday);
                break;
            case Calendar.THURSDAY:
                dayString = context.getString(R.string.thursday);
                break;
            case Calendar.FRIDAY:
                dayString = context.getString(R.string.friday);
                break;
            case Calendar.SATURDAY:
                dayString = context.getString(R.string.saturday);
                break;
            case Calendar.SUNDAY:
                dayString = context.getString(R.string.sunday);
                break;
        }
        return dayString;
    }

    private static String getFormattedDate(Context context, Calendar calendar) {
        String dayString = getDayString(context, calendar);
        String monthString = getMonthString(context, calendar);
        monthString = monthString.substring(0, 3);
        dayString = dayString.substring(0, 3);
        String outDate = dayString.concat(",").concat(Integer.toString(calendar.get(Calendar.DAY_OF_MONTH))).concat(".").concat(monthString).concat(".").concat(Integer.toString(calendar.get(Calendar.YEAR)));
        return outDate;
    }

    public static int getStayDuration(String dateStringStart, String dateStringEnd) {
        Calendar calendarStart = getCalendarFromStringYMD(dateStringStart);
        Calendar calendarEnd = getCalendarFromStringYMD(dateStringEnd);
        long daysInMili = calendarEnd.getTimeInMillis() - calendarStart.getTimeInMillis();
        int days = (int) (daysInMili / DAY_MILLIS);
        return days;
    }
}
