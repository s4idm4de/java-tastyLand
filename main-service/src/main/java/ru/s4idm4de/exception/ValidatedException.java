package ru.s4idm4de.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ValidatedException extends Exception {
    public ValidatedException(String message) {
        super(message);
        log.error("oh-oh, ValidatedException {}", message);
    }
}
