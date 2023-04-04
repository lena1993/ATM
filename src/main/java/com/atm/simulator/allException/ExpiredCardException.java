package com.atm.simulator.allException;

public class ExpiredCardException extends Exception {
    public ExpiredCardException(String message) {
        super(message);
    }
}
