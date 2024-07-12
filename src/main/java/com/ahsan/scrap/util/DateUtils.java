package com.ahsan.scrap.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a");

    public static String formatDate(LocalDateTime date) {
        return date.format(formatter);
    }
}
