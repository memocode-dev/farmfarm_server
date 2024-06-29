package dev.memocode.farmfarm_server.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

public class InstantUtils {

    public static Instant getStartOfToday(ZoneOffset zoneOffset) {
        LocalDate today = LocalDate.now(zoneOffset);
        return today.atStartOfDay().toInstant(zoneOffset);
    }

    public static Instant getStartOfNextDay(ZoneOffset zoneOffset) {
        LocalDate today = LocalDate.now(zoneOffset);
        LocalDate nextDay = today.plusDays(1);
        return nextDay.atStartOfDay().toInstant(zoneOffset);
    }
}
