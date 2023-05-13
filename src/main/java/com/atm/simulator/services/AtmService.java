package com.atm.simulator.services;

import com.atm.simulator.ApplicationProperties;
import com.atm.simulator.CardValidation;
import com.atm.simulator.MessagesProperties;
import com.atm.simulator.exception.NullCardException;
import com.atm.simulator.domain.BankCardStorage;
import com.atm.simulator.model.AtmData;
import com.atm.simulator.model.AuthType;
import com.atm.simulator.model.Card;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class AtmService {

    private final int LENGTH_OF_PIN_CODE = 4;
    private final String AUTHENTICATION_TYPE = "authenticationType";
    private final String PIN_OR_FINGER_PRINT = "pinOrFingerprint";
    private final String PAN = "pan";
    private final String PIN = "pin";
    private final String TOKEN = "token";
    private final String AMOUNT = "amount";
    private final String CARD_PAN = "cardPan";

    @Autowired
    BankCardStorage bankCardStorage;

    @Autowired
    CardValidation cardValidation;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    ApplicationProperties applicationProperties;

    @Autowired
    MessagesProperties messagesProperties;

    public Object checkCard(Card card) throws HttpServerErrorException  {

        if(cardValidation.isValid(card)) {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            HttpEntity<Card> entity = new HttpEntity<Card>(card,headers);

            ResponseEntity<Object> response = restTemplate.exchange(applicationProperties.BANK_URL.concat(applicationProperties.CHECK_CARD),
                  HttpMethod.POST, entity, Object.class);

            if(response == null){
                return response;
            }else if(response.getStatusCode() == HttpStatus.OK && bankCardStorage.putCard(card)) {
                return response.getBody();
            }else {
                throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, messagesProperties.WRONG_CARD);
            }

        }else {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, messagesProperties.WRONG_CARD);
        }
    }

    private Object check(AtmData atmData) throws HttpServerErrorException, NullCardException {

        if (atmData.getPinOrFingerprint().equals(PIN) && atmData.getPinOrFingerprint().length() != LENGTH_OF_PIN_CODE){
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, messagesProperties.WRONG_PIN);
        }

            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

            Map<String, String> param = new HashMap<>();
            param.put(AUTHENTICATION_TYPE, atmData.getAuthenticationType());
            param.put(PIN_OR_FINGER_PRINT, atmData.getPinOrFingerprint());
            param.put(PAN, atmData.getPan());
            HttpEntity<Map> entity = new HttpEntity<Map>(param, headers);


            ResponseEntity<Object> response = restTemplate.exchange(
                    applicationProperties.BANK_URL.concat(applicationProperties.CHECK_PIN), HttpMethod.POST, entity, Object.class);

        if(response == null){

            return response;

        } else if (response.getStatusCode() == HttpStatus.OK) {
            Card card = bankCardStorage.checkCardExistence(atmData.getPan());
            if (card != null) {
                return response.getBody();
            }
        } else {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, messagesProperties.WRONG_PIN);
        }

        throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, messagesProperties.WRONG_PIN);
    }


    public Object checkPinCode(AtmData atmData) throws HttpServerErrorException, NullCardException {

        AuthType authType = AuthType.valueOf(atmData.getAuthenticationType());

        Object result = null;

        switch (authType){
            case PIN:
            case FINGERPRINT:
                result = check(atmData);
                break;
            default:
                    throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, messagesProperties.WRONG_FINGERPRINT);
        }

        return result;
    }

    public Object checkBalance(String token) throws HttpServerErrorException {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        Map<String, String> param = new HashMap<>();
        param.put(TOKEN, token);
        HttpEntity<Map> entity = new HttpEntity<Map>(param, headers);

        ResponseEntity<Object> response = restTemplate.exchange(
                applicationProperties.BANK_URL.concat(applicationProperties.CHECK_BALANCE), HttpMethod.POST, entity, Object.class);

        if(response == null){
            return response;
        }else if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        }

        throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, messagesProperties.FAIL_GET_BALANCE);
    }

    public Object withdrawMoney(String token, Integer amount) throws HttpServerErrorException {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        Map<String, String> param = new HashMap<>();
        param.put(TOKEN, token);
        param.put(AMOUNT, amount.toString());
        HttpEntity<Map> entity = new HttpEntity<Map>(param, headers);

        ResponseEntity<Object> response = restTemplate.exchange(
                applicationProperties.BANK_URL.concat(applicationProperties.CASH_OUT), HttpMethod.POST, entity, Object.class);

        if(response == null){
            return response;
        }if (response.getStatusCode() == HttpStatus.OK) {

            String pan = ((LinkedHashMap) response.getBody()).get(CARD_PAN).toString();
            removeCardFromAtm(pan);

            return response.getBody();
        }

        throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, messagesProperties.FAIL_GET_MONEY);
    }

    public ResponseEntity removeCardFromAtm(String pan) {
        if(bankCardStorage.removeCard(pan)){
            return new ResponseEntity(HttpStatus.OK);
        }

        throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, messagesProperties.FAIL_TO_TAKE_CARD);
    }
}
