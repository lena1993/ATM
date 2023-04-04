package com.atm.simulator.allException;

public class InvalidCardHolder extends Exception {
    public InvalidCardHolder(String message) {
        super(message);
    }
}
