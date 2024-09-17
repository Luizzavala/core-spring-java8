package com.core.api.infrastructure.servicesImpl;

import com.core.api.application.services.CustomTimeService;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;

@Service
public class CustomTimeServiceImpl implements CustomTimeService {
    final ZoneId zoneId = ZoneId.of("America/Mazatlan");

    @Override
    public LocalDateTime getLocalDataTime() {
        return ZonedDateTime.now(zoneId).toLocalDateTime();
    }

    @Override
    public LocalDate getLocalDate() {
        return ZonedDateTime.now(zoneId).toLocalDateTime().toLocalDate();
    }

    @Override
    public LocalTime getLocalTime() {
        return LocalTime.parse(ZonedDateTime.now(zoneId)
                .toLocalDateTime()
                .toLocalTime()
                .format(DateTimeFormatter.ofPattern("HH:mm:ss")));
    }

    @Override
    public Long getMillis() {
        return ZonedDateTime.now(zoneId).toInstant().toEpochMilli();
    }
}
