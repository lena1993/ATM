package com.atm.simulator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:messages.properties")
public class MessagesProperties {

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

    @Value("${VALID_CARD}")
    public String VALID_CARD;

    @Value("${NOT_VALID_CARD}")
    public String NOT_VALID_CARD;

    @Value("${NOT_VALID_PAN}")
    public String NOT_VALID_PAN;

    @Value("${NOT_VALID_DATA}")
    public String NOT_VALID_DATA;

    @Value("${NOT_VALID_CARD_HOLDER}")
    public String NOT_VALID_CARD_HOLDER;

}
