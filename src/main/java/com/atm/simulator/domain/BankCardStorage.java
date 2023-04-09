package com.atm.simulator.domain;

import com.atm.simulator.exception.NullCardException;
import com.atm.simulator.model.Card;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
public class BankCardStorage {
    
    private Map<String, Card> validatedCard;

    public BankCardStorage() {
        validatedCard = Collections.synchronizedMap(new HashMap<>());
    }


    public synchronized void putCard(Card card) {
        validatedCard.put(card.getPan(), card);
    }

    public void removeCard(String pan){
        validatedCard.remove(pan);
    }

    public Card checkCardExistence(String cardPan) throws NullCardException {
        Card card = validatedCard.get(cardPan);

        if(!card.equals(null)) {
            return card;
        } else {
            throw new NullCardException("notValidPAN");
        }
    }

    //removeCard
}
