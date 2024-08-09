package com.ahsan.scrap.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a");
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter stringDateFormat = DateTimeFormatter.ofPattern("ddMMyyyy");


    public static String formatDate(LocalDateTime date) {
        return date.format(formatter);
    }
    public static String formatDate(LocalDate date) {
    	return date.format(dateFormatter);
    }
    public static String stringDateFormat(LocalDate date) {
    	return date.format(stringDateFormat);
    }
}
