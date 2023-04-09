package com.atm.simulator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.validation.beanvalidation.MessageSourceResourceBundleLocator;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import java.util.Locale;

@Component
public class ApplicationProperties {


    public static final String BANK_URL = "http://localhost:8088/bank/";
    public static final String CHECK_CARD = "checkCard";
    public static final String CHECK_PIN = "checkPin";
    public static final String CHECK_FINGERPRINT = "checkFingerprint";
    public static final String CHECK_BALANCE = "checkBalance";
    public static final String CASH_OUT = "cashOut";

    public static final String WRONG_CARD = "The Card is not valid";
    public static final String WRONG_PIN = "Wrong Pin code";
    public static final String WRONG_FINGERPRINT = "Wrong Fingerprint code";
    public static final String FAIL_GET_MONEY = "Fail to get money";
    public static final String FAIL_GET_BALANCE = "Fail to show balance";

//    private static MessageSourceResourceBundleLocator messageSource;

    /*@Autowired
    public ApplicationProperties(@NonNull MessageSource messageSource) {
        this.messageSource = messageSource;
    }*/

    /*@Autowired
    public static MessageSource messageSource;
*/




  /*  static ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();

    public static Locale locale = LocaleContextHolder.getLocale();
    //messageSource.setBasenames("lang/res");
    //messageSource.getMessage("hello", null, Locale.ITALIAN);

    public static final String BANK_URL = messageSource.getMessage("BANK_URL",null, locale);
    //public static final String CHECK_CARD = messageSource.getMessage("CHECK_CARD",null, locale);
*/

}
