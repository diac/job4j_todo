package ru.job4j.todo.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.TimeZone;

public final class DateAdjustment {

    public static LocalDateTime adjustByZoneId(LocalDateTime localDateTime, ZoneId zoneId) {
        return localDateTime
                .atZone(TimeZone.getDefault().toZoneId())
                .withZoneSameInstant(zoneId)
                .toLocalDateTime();
    }
}