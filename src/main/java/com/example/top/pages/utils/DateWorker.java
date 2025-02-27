package com.example.top.pages.utils;

import org.springframework.stereotype.Component;

import java.text.ParseException;
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

    public boolean isStringValidData(String checkedData) {
        boolean isStringValid = false;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(formatData);
            sdf.parse(checkedData);
            isStringValid = true;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return isStringValid;
    }
}
