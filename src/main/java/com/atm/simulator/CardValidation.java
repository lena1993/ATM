package com.atm.simulator;


import com.atm.simulator.exception.ExpiredCardException;
import com.atm.simulator.exception.InvalidCardHolder;
import com.atm.simulator.exception.InvalidPanException;
import com.atm.simulator.model.Card;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;


@Component
public class CardValidation {

    @Autowired
    MessageSource messageSource;

    private static Logger log = LoggerFactory.getLogger(CardValidation.class);

    public boolean isValid(Card bankCard) {
        //Locale locale = LocaleContextHolder.getLocale();

        try {
            validatePan(bankCard);
            expirationValidationDate(bankCard);
            ValidateCardHolder(bankCard);

            //log.info( messageSource.getMessage("validCard",null, locale), CardValidation.class);
            log.info("validCard", CardValidation.class);

        } catch (Exception e) {

            log.error("notValidCard", e);

            return false;
        }
        return true;
    }

    private void validatePan(Card bankCard) throws InvalidPanException {
        //Locale locale = LocaleContextHolder.getLocale();

        if (!(bankCard.getPan().matches("[0-9]+") && bankCard.getPan().length() == 16)) {
//            log.info(messageSource.getMessage("notValidPAN",null, locale), CardValidation.class);
//            throw new InvalidPanException(messageSource.getMessage("notValidPAN",null, locale));
            log.info("notValidPAN", CardValidation.class);
            throw new InvalidPanException("notValidPAN");
        }
    }

    private void expirationValidationDate(Card bankCard) throws ExpiredCardException {

        //Locale locale = LocaleContextHolder.getLocale();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");

        LocalDate date = LocalDate.now();
        String text = date.format(formatter);
        YearMonth cardDate = YearMonth.parse(text, formatter);

        YearMonth bankCardDate = YearMonth.parse(bankCard.getExpiryDate(), formatter);

        int a = bankCardDate.compareTo(cardDate);

        if (a < 0) {
//            log.info(messageSource.getMessage("notValidDate",null, locale), CardValidation.class);
//            throw new ExpiredCardException(messageSource.getMessage("notValidDate",null, locale));
            log.info("notValidDate", CardValidation.class);
            throw new ExpiredCardException("notValidDate");
        }

    }

    private void ValidateCardHolder(Card bankCard) throws InvalidCardHolder {
        //Locale locale = LocaleContextHolder.getLocale();

        if (!(bankCard.getCardHolder().matches("^[a-zA-Z^w+( +w+)]*$") && bankCard.getCardHolder() != null)) {
//            log.info(messageSource.getMessage("notValidCardHolder",null, locale), CardValidation.class);
//            throw new InvalidCardHolder(messageSource.getMessage("notValidCardHolder",null, locale));
            log.info("notValidCardHolder", CardValidation.class);
            throw new InvalidCardHolder("notValidCardHolder");
        }
    }

}
