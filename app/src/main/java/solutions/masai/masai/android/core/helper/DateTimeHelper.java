package solutions.masai.masai.android.core.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static solutions.masai.masai.android.core.helper.DateTimeHelper.DateFormat.ACTIVITY_DATE;
import static solutions.masai.masai.android.core.helper.DateTimeHelper.DateFormat.DEFAULT_DATE;
import static solutions.masai.masai.android.core.helper.DateTimeHelper.DateFormat.HOTEL_DATE;
import static solutions.masai.masai.android.core.helper.DateTimeHelper.RegexDateFormat.ACTIVITY_REGEX;
import static solutions.masai.masai.android.core.helper.DateTimeHelper.RegexDateFormat.HOTEL_REGEX;
import static solutions.masai.masai.android.core.helper.TimeHelper.MINUTE_MILLIS;

/**
 * @author Sebastian Tanzer
 * @version 1.0
 *          created on 01/09/2017
 */

public class DateTimeHelper {

    public static enum DateFormat {
        HOTEL_DATE("yyyy-MM-dd"),
        ACTIVITY_DATE("yyyy-MM-dd HH:mm:ss"),
        DEFAULT_DATE("yyyy-MM-dd'T'HH:mm:ss");

        private String stringValue;
        private DateFormat(String toString) {
            stringValue = toString;
        }
        @Override
        public String toString() {
            return stringValue;
        }
    }

    public static enum RegexDateFormat {
        HOTEL_REGEX("\\d{4}-\\d{2}-\\d{2}"),
        ACTIVITY_REGEX("\\d{4}-\\d{2}-\\d{2}\\s{1}\\d{2}:\\d{2}:\\d{2}"),
        DEFAULT_REGEX("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}");

        private String stringValue;
        private RegexDateFormat(String toString) {
            stringValue = toString;
        }
        @Override
        public String toString() {
            return stringValue;
        }
    }

    public static String checkForFormat(String date){

        if (date.matches(HOTEL_REGEX.toString()))
            return HOTEL_DATE.toString();
        else if(date.matches(ACTIVITY_REGEX.toString()))
            return ACTIVITY_DATE.toString();
        else return DEFAULT_DATE.toString();

    }

    public static String getTime(String datetime){
        SimpleDateFormat sdf = new SimpleDateFormat();

        sdf.applyPattern(checkForFormat(datetime));

        try {
            Date d = sdf.parse(datetime);
            sdf.applyPattern("HH:mm");
            return sdf.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return datetime;
    }

    public static String getDateTime(String datetime){
        SimpleDateFormat sdf = new SimpleDateFormat();

        sdf.applyPattern(checkForFormat(datetime));

        try {
            Date d = sdf.parse(datetime);
            sdf.applyPattern("dd.MM.YY HH:mm");
            return sdf.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return datetime;
    }

    public static String getDate(String datetime){
        SimpleDateFormat sdf = new SimpleDateFormat();

        sdf.applyPattern(checkForFormat(datetime));

        try {
            Date d = sdf.parse(datetime);
            sdf.applyPattern("dd.MM.yy");
            return sdf.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return datetime;
    }

    public static int getTravelDurationFromDepartureArrivalTimes(String departureTime, String arrivalTime, DateFormat format) {
        SimpleDateFormat sdf = new SimpleDateFormat();
        if (format == null) {
            sdf.applyPattern(DateFormat.DEFAULT_DATE.toString());
        } else {
            sdf.applyPattern(format.toString());
        }
        try {
            Date start = sdf.parse(departureTime);
            Date end = sdf.parse(arrivalTime);

            long durationInMilliseconds = end.getTime() - start.getTime();
            return (int) (durationInMilliseconds / MINUTE_MILLIS);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
