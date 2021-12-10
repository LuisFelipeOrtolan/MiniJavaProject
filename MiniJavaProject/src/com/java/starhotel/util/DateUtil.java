package com.java.starhotel.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    public static Date convertToDate(String date) {
        Date d = null;
        try {
            d = new SimpleDateFormat("dd/MM/yyyy").parse(date);
        } catch (ParseException e) {
            System.out.println("Format not valid.");
            e.printStackTrace();
        }
        return d;
    }
}
