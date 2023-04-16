package com.atm.simulator.services;

import com.atm.simulator.ApplicationProperties;
import com.atm.simulator.CardValidation;
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

    @Autowired
    BankCardStorage bankCardStorage;

    @Autowired
    CardValidation cardValidation;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    ApplicationProperties applicationProperties;

    public ResponseEntity checkCard(Card card) throws HttpServerErrorException {

        if(cardValidation.isValid(card)) {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            HttpEntity<Card> entity = new HttpEntity<Card>(card,headers);

            ResponseEntity response = restTemplate.exchange(applicationProperties.BANK_URL.concat(applicationProperties.CHECK_CARD),
                  HttpMethod.POST, entity, String.class);

            if(response.getStatusCode() == HttpStatus.OK) {
                bankCardStorage.putCard(card);
                return new ResponseEntity(response.getBody(), HttpStatus.OK);
            }else {
                throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, applicationProperties.WRONG_CARD);
            }
        }else {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, applicationProperties.WRONG_CARD);
        }
    }

    private ResponseEntity check(AtmData atmData) throws HttpServerErrorException, NullCardException {

        if (atmData.getPinOrFingerprint()=="PIN" && atmData.getPinOrFingerprint().length() != LENGTH_OF_PIN_CODE){
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, applicationProperties.WRONG_PIN);
        }

            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

            Map<String, String> param = new HashMap<>();
            param.put("authenticationType", atmData.getAuthenticationType());
            param.put("pinOrFingerprint", atmData.getPinOrFingerprint());
            param.put("pan", atmData.getPan());
            HttpEntity<Map> entity = new HttpEntity<Map>(param, headers);


            ResponseEntity response = restTemplate.exchange(
                    applicationProperties.BANK_URL.concat(applicationProperties.CHECK_PIN), HttpMethod.POST, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                Card card = bankCardStorage.checkCardExistence(atmData.getPan());
                if (card != null) {
                    return new ResponseEntity(response.getBody(), HttpStatus.OK);
                }
            } else {
                throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, applicationProperties.WRONG_PIN);
            }

        throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, applicationProperties.WRONG_PIN);
    }


    public ResponseEntity checkPinCode(AtmData atmData) throws HttpServerErrorException, NullCardException {

        String authType = AuthType.valueOf(atmData.getAuthenticationType()).toString();

        ResponseEntity result= null;

        boolean isOk = false;

        switch (authType){
            case "PIN": result = check(atmData); isOk = true; break;
            case "FINGERPRINT": result = check(atmData); isOk = true; break;
        }

        if(isOk){
            return result;
        }

        throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, applicationProperties.WRONG_FINGERPRINT);
    }

    public ResponseEntity checkBalance(String token) throws HttpServerErrorException {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        Map<String, String> param = new HashMap<>();
        param.put("token", token);
        HttpEntity<Map> entity = new HttpEntity<Map>(param, headers);

        ResponseEntity response = restTemplate.exchange(
                applicationProperties.BANK_URL.concat(applicationProperties.CHECK_BALANCE), HttpMethod.POST, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return new ResponseEntity(response.getBody(), HttpStatus.OK);
        }

        throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, applicationProperties.FAIL_GET_BALANCE);
    }

    public ResponseEntity withdrawMoney(String token, Integer amount) throws HttpServerErrorException {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        Map<String, String> param = new HashMap<>();
        param.put("token", token);
        param.put("amount", amount.toString());
        HttpEntity<Map> entity = new HttpEntity<Map>(param, headers);

        ResponseEntity<JsonObject> response = restTemplate.exchange(
                applicationProperties.BANK_URL.concat(applicationProperties.CASH_OUT), HttpMethod.POST, entity, JsonObject.class);

        if (response.getStatusCode() == HttpStatus.OK) {
           // removeCardFromAtm(response.getBody());
//            Gson gson = new Gson();
//            String content = gson.toJson(response.getBody());
//            System.out.println(content);
            return new ResponseEntity(response.getBody(), HttpStatus.OK);
        }

        //removeCardFromAtm(response.getHeaders().get("pan").get(0));
        throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, applicationProperties.FAIL_GET_MONEY);
    }

    public ResponseEntity removeCardFromAtm(String pan) {
        bankCardStorage.removeCard(pan);

        //return new ResponseEntity(pan + messageSource.getMessage("removedCard",null, locale), HttpStatus.OK);
        return new ResponseEntity(pan, HttpStatus.OK);
    }
}
