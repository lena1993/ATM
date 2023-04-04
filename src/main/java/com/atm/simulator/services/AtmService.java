package com.atm.simulator.services;

//import com.atm.simulator.ApplicationProperties;
import com.atm.simulator.CardValidation;
import com.atm.simulator.allException.NullCardException;
import com.atm.simulator.domain.BankCardStorage;
import com.atm.simulator.model.ATM;
import com.atm.simulator.model.Card;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
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

    /*@Autowired
    MessageSource messageSource;*/

   // ApplicationProperties applicationProperties = new ApplicationProperties(messageSource);

//    @Autowired
//    ApplicationProperties applicationProperties;

    public ResponseEntity checkCard(Card card) throws HttpServerErrorException {
//        System.out.println(applicationProperties.BANK_URL);
        /*Locale locale = LocaleContextHolder.getLocale();
        messageSource.getMessage("BANK_URL",null, locale);*/

        if(cardValidation.isValid(card)){

            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            HttpEntity<Card> entity = new HttpEntity<Card>(card,headers);


          /*ResponseEntity response = restTemplate.exchange(
                  applicationProperties.BANK_URL.concat(applicationProperties.CHECK_CARD),
                  HttpMethod.POST, entity, String.class);*/

            ResponseEntity response = restTemplate.exchange("http://localhost:8088/bank/checkCard",
                  HttpMethod.POST, entity, String.class);

            if(response.getStatusCode() == HttpStatus.OK){
                bankCardStorage.putCard(card);

                return new ResponseEntity(response.getBody(), HttpStatus.OK);
            }else{
                //return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);

                throw new HttpServerErrorException(response.getStatusCode(), "message1");
            }
        }else{
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "message2");
        }
        //throw new NoSuchElementException();

        /*HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<Card> entity = new HttpEntity<Card>(card,headers);

        ResponseEntity response = restTemplate.exchange(
                "http://localhost:8080/actuator/shutdown", HttpMethod.POST, entity, String.class);*/

       /* String command =
                "curl -X POST http://localhost:8080/actuator/shutdown";
        ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
*/
        //return new ResponseEntity(messageSource.getMessage("notValidCard",null, locale), HttpStatus.INTERNAL_SERVER_ERROR);

        //return new ResponseEntity("notValidCard", HttpStatus.INTERNAL_SERVER_ERROR);



    }

    public ResponseEntity checkPinCode(ATM atm) throws HttpServerErrorException, NullCardException {

       // Locale locale = LocaleContextHolder.getLocale();

        if(atm.getPin().length() == LENGTH_OF_PIN_CODE) {

            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            HttpEntity<ATM> entity = new HttpEntity<ATM>(atm, headers);

            ResponseEntity response = restTemplate.exchange(
                    "http://localhost:8088/bank/checkPin", HttpMethod.POST, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                Card card = bankCardStorage.checkCardExistence(atm.getPan());
                if(card != null){

                    //String msg = messageSource.getMessage("CheckBalance",null, locale);

                    return new ResponseEntity(response.getBody(), HttpStatus.OK);
                }
            }else{
                //return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);

                throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "message2");
            }

        }

        //String msg = messageSource.getMessage("IncorrectPinCode",null, locale);

        //return new ResponseEntity(msg, HttpStatus.BAD_REQUEST);

        throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "message2");

    }

    public ResponseEntity checkBalance(String token) throws HttpServerErrorException{

        //Locale locale = LocaleContextHolder.getLocale();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        Map<String, String> param = new HashMap<>();
        param.put("token", token);
        HttpEntity<Map> entity = new HttpEntity<Map>(param, headers);

        ResponseEntity response = restTemplate.exchange(
                "http://localhost:8088/bank/checkBalance", HttpMethod.POST, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {

           /* headers.add(messageSource.getMessage("takeMoney",null, locale), response.getHeaders().get("cardToken").get(0));
            headers.add(messageSource.getMessage("balance",null, locale), response.getBody().toString());

            System.out.println(headers);*/

            return new ResponseEntity(response.getBody(), HttpStatus.OK);
        }


        //return new ResponseEntity(messageSource.getMessage("incorrectToken",null, locale), HttpStatus.BAD_REQUEST);

        throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "message2");

    }


    public ResponseEntity withdrawMoney(String token, Integer amount) throws HttpServerErrorException{
        //Locale locale = LocaleContextHolder.getLocale();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        Map<String, String> param = new HashMap<>();
        param.put("token", token);
        param.put("amount", amount.toString());
        HttpEntity<Map> entity = new HttpEntity<Map>(param, headers);

        ResponseEntity response = restTemplate.exchange(
                "http://localhost:8088/bank/cashOut", HttpMethod.POST, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
           // removeCardFromAtm(response.getBody());
//            Gson gson = new Gson();
//            String content = gson.toJson(response.getBody());
//            System.out.println(content);


            return new ResponseEntity(response.getBody(), HttpStatus.OK);
        }

        //removeCardFromAtm(response.getHeaders().get("pan").get(0));
        //return new ResponseEntity(messageSource.getMessage("incorrectToken",null, locale), HttpStatus.BAD_REQUEST);

        throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "message2");
    }


    public ResponseEntity removeCardFromAtm(String pan){
        bankCardStorage.removeCard(pan);

        //Locale locale = LocaleContextHolder.getLocale();

        //return new ResponseEntity(pan + messageSource.getMessage("removedCard",null, locale), HttpStatus.OK);
        return new ResponseEntity(pan, HttpStatus.OK);

    }

}
