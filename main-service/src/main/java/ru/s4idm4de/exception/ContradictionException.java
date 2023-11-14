package ru.s4idm4de.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ContradictionException extends Exception {
    public ContradictionException(String message) {
        super(message);
        log.error("Ой-ой, NotFoundException {}", message);
    }
}
