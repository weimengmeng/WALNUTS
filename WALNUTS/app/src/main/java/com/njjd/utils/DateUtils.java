package com.njjd.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mrwim on 17/8/10.
 */

public class DateUtils {
    public  static String formationDate(Date date) {
        String dateString = "";
        // 获取系统当前时间
        Date now = new Date();
        try {
            long endTime = now.getTime();
            long currentTime= date.getTime();
            // 计算两个时间点相差的秒数
            long seconds = (endTime - currentTime);
            if (seconds<10*1000) {
                dateString ="刚刚";
            }else if (seconds<60*1000) {
                dateString = seconds/1000+"秒前";
            }else if (seconds<60*60*1000) {
                dateString = seconds/1000/60+"分钟前";
            }else if (seconds<60*60*24*1000) {
                dateString = seconds/1000/60/60+"小时前";
            }else if (seconds<60*60*24*1000*30L) {
                dateString =seconds/1000/60/60/24+ "天前";
            }else if (date.getYear()==now.getYear()) {//今年并且大于30天显示具体月日
                dateString = new SimpleDateFormat("MM-dd").format(date.getTime());
            }else if (date.getYear()!=now.getYear()) {//大于今年显示年月日
                dateString =  new SimpleDateFormat("yyyy-MM-dd").format(date.getTime());
            }else{
                dateString =  new SimpleDateFormat("yyyy-MM-dd").format(date);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dateString;

    }
}
