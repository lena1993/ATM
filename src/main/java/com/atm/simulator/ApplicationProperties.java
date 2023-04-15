package com.atm.simulator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:messages.properties")
public class ApplicationProperties {


    @Value("${BANK_URL}")
    public String BANK_URL;

    @Value("${CHECK_CARD}")
    public String CHECK_CARD;

    @Value("${CHECK_PIN}")
    public String CHECK_PIN;

    @Value("${CHECK_FINGERPRINT}")
    public String CHECK_FINGERPRINT;

    @Value("${CHECK_BALANCE}")
    public String CHECK_BALANCE;

    @Value("${CASH_OUT}")
    public String CASH_OUT;

    @Value("${WRONG_CARD}")
    public String WRONG_CARD;

    @Value("${WRONG_PIN}")
    public String WRONG_PIN;

    @Value("${WRONG_FINGERPRINT}")
    public String WRONG_FINGERPRINT;

    @Value("${FAIL_GET_MONEY}")
    public String FAIL_GET_MONEY;

    @Value("${FAIL_GET_BALANCE}")
    public String FAIL_GET_BALANCE;

}
