package com.example.top.pages.utils;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@Component
public class DateWorker {
    private final String formatData = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    public String getCurrentTimeISO() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat(formatData);
        sdf.setTimeZone(TimeZone.getTimeZone("CET"));
        return sdf.format(date);
    }
}
