package br.com.desenvolvimento.logica.estoque_service.util;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class DataUtil {

    public static final String PATTERN_DDMMYYYYHHMMSS = "dd/MM/yyyy HH:mm:ss";
    public static final String PATTERN_DDMMYYYY = "dd/MM/yyyy";

    public static String LocalDateTimeToString(LocalDateTime date, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return date.format(formatter);
    }

    public static String OffsetDateTimeToString(OffsetDateTime dateTime, String pattern) {
        if (dateTime == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return dateTime.format(formatter);
    }
}
