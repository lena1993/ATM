package com.atm.simulator.model;

public class AtmData {
    private String authenticationType;
    private String pinOrFingerprint;
    private String pan;

    public String getPinOrFingerprint() {
        return pinOrFingerprint;
    }

    public String getAuthenticationType() {
        return authenticationType;
    }

    public void setAuthenticationType(String authenticationType) {
        this.authenticationType = authenticationType;
    }

    public void setPinOrFingerprint(String pinOrFingerprint) {
        this.pinOrFingerprint = pinOrFingerprint;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }
}
