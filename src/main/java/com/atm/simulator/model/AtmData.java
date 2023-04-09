package com.atm.simulator.model;

public class AtmData {
    private int authenticationType;
    private String pin;
    private String pan;

    public String getPin() {
        return pin;
    }

    public int getAuthenticationType() {
        return authenticationType;
    }

    public void setAuthenticationType(int authenticationType) {
        this.authenticationType = authenticationType;
    }

    public void setPin(String pinOrFingerprint) {
        this.pin = pin;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }
}
