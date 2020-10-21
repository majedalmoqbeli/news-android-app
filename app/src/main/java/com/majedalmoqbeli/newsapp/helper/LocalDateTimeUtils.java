package com.majedalmoqbeli.newsapp.helper;

import org.joda.time.LocalDateTime;
import org.joda.time.format.ISODateTimeFormat;

import java.util.Locale;

/**
 * Helper class to parse {@link LocalDateTime} and convert it into a string.
 * The patterns are:
 * PATTERN_DATE                 => i.e: 1990/04/20
 * PATTERN_DATE_TIME_24_HOURS   => i.e: 13:22 1990/04/20
 * PATTERN_DATE_TIME_12_HOURS   => i.e: 01:22 pm 1990/04/20
 * PATTERN_TIME_24_HOURS        => i.e: 13:22
 * PATTERN_TIME_12_HOURS        => i.e: 01:22 pm
 */
public class LocalDateTimeUtils {

    public static final String PATTERN_DATE = "MMMM dd,yyyy";

    public static final String PATTERN_DATE_TIME_24_HOURS = "HH:mm yyyy/MM/dd";
    public static final String PATTERN_DATE_TIME_12_HOURS = "hh:mm a yyyy/MM/dd";

    public static final String PATTERN_TIME_24_HOURS = "HH:mm";
    public static final String PATTERN_TIME_12_HOURS = "hh:mm a";

    private static Locale locale;

    public static Locale getLocale() {
        return locale;
    }

    public static void setLocale(Locale locale) {
        LocalDateTimeUtils.locale = locale;
    }

    public static String convertToString(LocalDateTime localDateTime, String pattern) {
        Locale locale = LocalDateTimeUtils.getLocale();
        if (locale == null) {
            return localDateTime.toString(pattern);
        } else {
            return localDateTime.toString(pattern, locale);
        }
    }

    /**
     * Return a string of the current local date and time.
     *
     * @return
     */
    public static String getCurrentDateTime() {
        return LocalDateTime.now().toString();
    }


    /**
     * this is for return the
     *
     * @param dataUTS
     * @return
     */
    public static String getDataToShow(String dataUTS) {
        try {
            LocalDateTime dateTime = ISODateTimeFormat.dateTimeParser()
                    .parseDateTime(dataUTS)
                    .toLocalDateTime();
            String date = LocalDateTimeUtils.convertToString(dateTime, LocalDateTimeUtils.PATTERN_DATE);
            String time = LocalDateTimeUtils.convertToString(dateTime, LocalDateTimeUtils.PATTERN_TIME_12_HOURS);

            return date + "   " + time;
        } catch (Exception ex) {
            return dataUTS;
        }

    }


    public static String getOnlyDataToShow(String dataUTS) {
        try {
            LocalDateTime dateTime = ISODateTimeFormat.dateTimeParser()
                    .parseDateTime(dataUTS)
                    .toLocalDateTime();
            String date = LocalDateTimeUtils.convertToString(dateTime, LocalDateTimeUtils.PATTERN_DATE);

            return date;
        } catch (Exception ex) {
            return dataUTS;
        }

    }

    public static String getOnlyTimeToShow(String dataUTS) {
        try {
            LocalDateTime dateTime = ISODateTimeFormat.dateTimeParser()
                    .parseDateTime(dataUTS)
                    .toLocalDateTime();
            String time = LocalDateTimeUtils.convertToString(dateTime, LocalDateTimeUtils.PATTERN_TIME_12_HOURS);
            return time;
        } catch (Exception ex) {
            return dataUTS;
        }

    }

}
