package org.meetcute.view.apaters;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TimeUtils {
    public static String parseTimeFormat(String time) {
        String inputPattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        String outputPattern = "hh:mm a";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
        inputFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date;
        String str = null;
        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }


    public static String dateHeader(String time){
        String inputPattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        String outputPattern = "dd MMM yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
        inputFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date;
        String str = null;
        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }
    public static Date getDate(String time){
        String inputPattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            return inputFormat.parse(time);
        } catch (ParseException e) {
            return Calendar.getInstance().getTime();
        }
    }

    public static String parseTimeFormat(Date date) {
        String outputPattern = "HH:mm";
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
        outputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return outputFormat.format(date);
    }

    public static String convertDateToString(Date date){
        String outputPattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
        outputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return outputFormat.format(date);
    }




}
