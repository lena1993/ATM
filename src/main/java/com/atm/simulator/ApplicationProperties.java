package com.atm.simulator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class ApplicationProperties {


    @Value("${BANK_URL}")
    public String BANK_URL;

    @Value("${CHECK_CARD}")
    public String CHECK_CARD;

    @Value("${CHECK_PIN}")
    public String CHECK_PIN;

    @Value("${CHECK_BALANCE}")
    public String CHECK_BALANCE;

    @Value("${CASH_OUT}")
    public String CASH_OUT;


}
