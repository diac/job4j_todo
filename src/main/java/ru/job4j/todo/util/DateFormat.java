package ru.job4j.todo.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

public final class DateFormat {

    private static final String DEFAULT_FORMAT = "dd-MM-yyyy HH:mm";

    public static Function<LocalDateTime, String> defaultFormatter() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DEFAULT_FORMAT);
        return localDateTime -> localDateTime.format(formatter);
    }
}