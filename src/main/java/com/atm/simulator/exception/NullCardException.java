package com.atm.simulator.exception;

public class NullCardException extends NotEnoughMoneyException {
    public NullCardException(String message) {
        super(message);
    }
}
