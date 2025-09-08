package br.com.desenvolvimento.logica.estoque_service.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DataUtil {

    public static final String PATTERN_DDMMYYYYHHMMSS = "dd/MM/yyyy HH:mm:ss";

    public static String LocalDateTimeToString(LocalDateTime date, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return date.format(formatter);
    }
}
