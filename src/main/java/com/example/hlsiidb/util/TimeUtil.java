package com.example.hlsiidb.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 为了解决下载文件时出现的内存泄漏问题，将下载所选时间段进行分段处理。
 * @author xie
 */
public class TimeUtil {

    public static List<String> splitTime(String startTime, String endTime) throws ParseException {
        List<String> timeList = new ArrayList<>();
        SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
        Date date1;
        Date date2;
        Calendar right1 = Calendar.getInstance();
        Calendar right2 = Calendar.getInstance();
        date1 = sdf.parse(startTime);
        date2 = sdf.parse(endTime);
        if(date1.before(date2)) {
            right1.setTime(date1);
            right2.setTime(date2);
            while (right1.before(right2)) {
                timeList.add(sdf.format(right1.getTime()));
                right1.add(Calendar.HOUR_OF_DAY, 12);   //按12个小时的间隔划分
            }
            timeList.add(sdf.format(right2.getTime()));
        }
        return timeList;
    }
}
