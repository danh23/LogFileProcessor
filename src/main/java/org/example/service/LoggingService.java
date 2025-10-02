package org.example.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

public class LoggingService {

    private static final Logger log = LoggerFactory.getLogger(LoggingService.class);

    public void log(Level level, String message) {
        log.atLevel(level).log(message);
    }
}
