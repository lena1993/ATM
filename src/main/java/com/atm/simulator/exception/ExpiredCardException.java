package com.atm.simulator.exception;

public class ExpiredCardException extends Exception {
    public ExpiredCardException(String message) {
        super(message);
    }
}
