package com.atm.simulator.allException;

public class NullCardException extends NotEnoughMoneyException {
    public NullCardException(String message) {
        super(message);
    }
}
