package com.hy.java.date;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * HyDate
 *
 * @author Jie.Hu
 * @date 6/14/21 10:14 AM
 */
public class HyDate {
    public static void main(String[] args) {

        String format = DateFormat.getDateInstance().format(new Date());
        String format1 = DateFormat.getInstance().format(new Date());
        String format2 = DateFormat.getTimeInstance().format(new Date());
        System.out.println(format2);
        System.out.println(format1);
        System.out.println(format);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String format3 = simpleDateFormat.format(new Date());
        System.out.println(format3);

        System.out.println("==============");

        LocalDateTime now = LocalDateTime.now();
        String date = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        System.out.println(date);

        Date date1 = new Date();
    }
}
