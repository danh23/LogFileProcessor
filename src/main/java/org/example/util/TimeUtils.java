package org.example.util;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class TimeUtils {

    public static LocalTime parseTimestamp(String timestamp) throws DateTimeParseException {
        DateTimeFormatter.ofPattern("HH:mm:ss");
        return LocalTime.parse(timestamp);
    }

    public static int getSecondsFromTime(LocalTime localTime) {
        return localTime.toSecondOfDay();
    }
}
