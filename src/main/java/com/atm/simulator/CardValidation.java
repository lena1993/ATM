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

    @Autowired
    MessagesProperties messagesProperties;

    private static Logger log = LoggerFactory.getLogger(CardValidation.class);

    public boolean isValid(Card bankCard) {

        try {
            validatePan(bankCard);
            expirationValidationDate(bankCard);
            ValidateCardHolder(bankCard);

            log.info(messagesProperties.VALID_CARD, CardValidation.class);

        } catch (Exception e) {

            log.error("notValidCard", e);

            return false;
        }
        return true;
    }

    private void validatePan(Card bankCard) throws InvalidPanException {

        if (!(bankCard.getPan().matches("[0-9]+") && bankCard.getPan().length() == 16)) {
            log.info(messagesProperties.NOT_VALID_PAN, CardValidation.class);
            throw new InvalidPanException(messagesProperties.NOT_VALID_PAN);
        }
    }

    private void expirationValidationDate(Card bankCard) throws ExpiredCardException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");

        LocalDate date = LocalDate.now();
        String text = date.format(formatter);
        YearMonth cardDate = YearMonth.parse(text, formatter);

        YearMonth bankCardDate = YearMonth.parse(bankCard.getExpiryDate(), formatter);

        int checkExpDate = bankCardDate.compareTo(cardDate);

        if (checkExpDate < 0) {

            log.info(messagesProperties.NOT_VALID_DATA, CardValidation.class);
            throw new ExpiredCardException(messagesProperties.NOT_VALID_DATA);
        }

    }

    private void ValidateCardHolder(Card bankCard) throws InvalidCardHolder {

        if (!(bankCard.getCardHolder().matches("^[a-zA-Z^w+( +w+)]*$") && bankCard.getCardHolder() != null)) {
            log.info(messagesProperties.NOT_VALID_CARD_HOLDER, CardValidation.class);
            throw new InvalidCardHolder(messagesProperties.NOT_VALID_CARD_HOLDER);
        }
    }

}
