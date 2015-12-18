package com.library_app.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by ahmed on 12/18/2015.
 */
public class TimeFormatUtils
{
    public static String foramtDate(Calendar calendar)
    {
        SimpleDateFormat f = new SimpleDateFormat("MM-dd-yyyy");
        return f.format(calendar.getTime());
    }
}
