package com.atm.simulator.domain;

import com.atm.simulator.allException.InvalidPanException;
import com.atm.simulator.allException.NullCardException;
import com.atm.simulator.model.Card;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class BankCardStorage {

    private Map<String, Card> keepCard;

    public BankCardStorage() {
        keepCard = new HashMap<>();
    }


    public synchronized void putCard(Card card){
        //BankCard bankCard = new BankCard();
        //bankCard.setCard(card);
        keepCard.put(card.getPan(), card);
    }

    public void removeCard(String pan){
        keepCard.remove(pan);
    }

    public Card checkCardExistence(String cardPan) throws NullCardException{
        Card card = keepCard.get(cardPan);

        if(!card.equals(null)){
            return card;
        } else {
            throw new NullCardException("notValidPAN");
        }


       /* BankCard bankCard = new BankCard();
        keepCard.forEach((key,value)-> {
            if(key.equals(cardPan)){
                bankCard.setPin(pinCode);
                bankCard.setCard(new Card(value.getCard().getPan(), value.getCard().getExpiryDate(),
                        value.getCard().getCardHolder(), value.getCard().getCardType(),value.getCard().getIssuer()));

            }
        });*/

        //return card;
    }

    //removeCard
}
