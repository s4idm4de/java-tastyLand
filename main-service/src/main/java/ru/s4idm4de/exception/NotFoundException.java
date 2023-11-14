package ru.s4idm4de.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NotFoundException extends Exception {
    public NotFoundException(String message) {
        super(message);
        log.error("Ой-ой, NotFoundException {}", message);
    }
}
