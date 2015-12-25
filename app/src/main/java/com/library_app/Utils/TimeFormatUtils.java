package com.library_app.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Created by ahmed on 12/18/2015.
 */
public class TimeFormatUtils
{
    public static String foramtDate(Calendar calendar)
    {
        SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy");
        return f.format(calendar.getTime());
    }

    /**
     * converts a string like "2014-12-11T12:30:12.33Z" to a calendar
     */
    public static Calendar parseCalendar(String dateStr)
    {
        try
        {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            Date date = dateFormat.parse(dateStr);
            Calendar c = new GregorianCalendar(TimeZone.getDefault());
            c.setTime(date);
            return c;
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return new GregorianCalendar();
    }
}
