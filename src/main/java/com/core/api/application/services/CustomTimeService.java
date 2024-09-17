package com.core.api.application.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public interface CustomTimeService {
    LocalDateTime getLocalDataTime();
    LocalDate getLocalDate();
    LocalTime getLocalTime();
    Long getMillis();
}
