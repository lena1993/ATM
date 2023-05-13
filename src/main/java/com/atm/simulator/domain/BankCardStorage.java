package com.atm.simulator.domain;

import com.atm.simulator.MessagesProperties;
import com.atm.simulator.exception.NullCardException;
import com.atm.simulator.model.Card;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
public class BankCardStorage {

    @Autowired
    MessagesProperties messagesProperties;
    
    private Map<String, Card> validatedCard;

    public BankCardStorage() {
        validatedCard = Collections.synchronizedMap(new HashMap<>());
    }


    public synchronized Boolean putCard(Card card) {
        validatedCard.put(card.getPan(), card);

        if(validatedCard.get(card.getPan()) == null){
            return false;
        }

        return true;

    }

    public boolean removeCard(String pan){
        validatedCard.remove(pan);

        if(validatedCard.get(pan)==null){
            return true;
        }
        return false;
    }

    public Card checkCardExistence(String cardPan) throws NullCardException {
        Card card = validatedCard.get(cardPan);

        if(!card.equals(null)) {
            return card;
        } else {
            throw new NullCardException(messagesProperties.NOT_VALID_PAN);
        }
    }

    //removeCard
}
