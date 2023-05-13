package com.atm.simulator.controllers;

import com.atm.simulator.MessagesProperties;
import com.atm.simulator.exception.NullCardException;
import com.atm.simulator.model.AtmData;
import com.atm.simulator.model.Card;
import com.atm.simulator.services.AtmService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({MockitoExtension.class})
class AtmControllerTest {

    private final String PAN = "pan";
    private final String TOKEN = "token";
    private final String BALANCE = "balance";

    private final String PAN_VALUE = "1234567891234567";
    private final String PAN_VALUE_NOT_VALID = "1234567891234568";
    private final String PIN_VALUE = "1234";
    private final String EXPIRY_DATE = "02/24";
    private final String CARD_HOLDER = "Lena Sargsyan";
    private final String CARD_TYPE = "VISA";
    private final String ISSUER = "HSBC";
    private final String AUTHENTICATION_TYPE = "PIN";
    private final Integer BALANCE_VALUE = 100;
    private final Integer AMOUNT = 10;

    private final String CARD = "card";
    private final String CARD_UPPERCASE = "Card";
    private final String CARD_DETAILS = "Card Details";


    @InjectMocks
    AtmController atmController;

    @Mock
    AtmService atmService;

    @Autowired
    MessagesProperties messagesProperties;


    @Test
    void insertCard() {
        Card card = new Card(PAN_VALUE, EXPIRY_DATE, CARD_HOLDER,
                CARD_TYPE, ISSUER);

        HashMap<Object, Object> h =  new HashMap<>();
        h.put(PAN, card.getPan());

        Mockito.when(atmService.checkCard(card)).thenReturn(h);

        ResponseEntity responseEntity = atmController.insertCard(card);
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(h);
    }

    @Test
    void insertCardNotValid() {
        Card card = new Card(PAN_VALUE_NOT_VALID, EXPIRY_DATE, CARD_HOLDER,
                CARD_TYPE, ISSUER);

       Mockito.when(atmService.checkCard(card)).thenReturn(null);

        ResponseEntity responseEntity = atmController.insertCard(card);
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(responseEntity.getStatusCode().getReasonPhrase()).isEqualTo("Internal Server Error");
    }


    @Test
    void checkPin() throws NullCardException {
        AtmData atmData = new AtmData(AUTHENTICATION_TYPE, PIN_VALUE, PAN_VALUE);

        String generatedToken = generateToken(atmData.getPan());

        Map<String, String> param = new HashMap<>();
        param.put(PAN, atmData.getAuthenticationType());
        param.put(TOKEN, generatedToken);

        Mockito.when(atmService.checkPinCode(atmData)).thenReturn(param);

        ResponseEntity responseEntity = atmController.checkPin(atmData);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(param);

    }

    @Test
    void checkPinNotValid() throws NullCardException {
        AtmData atmData = new AtmData(AUTHENTICATION_TYPE, PIN_VALUE, PAN_VALUE_NOT_VALID);

        Mockito.when(atmService.checkPinCode(atmData)).thenReturn(null);

       /* assertThrows(HttpServerErrorException.class, () -> {
            atmController.checkPin(atmData);
        });*/

        ResponseEntity responseEntity = atmController.checkPin(atmData);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(responseEntity.getStatusCode().getReasonPhrase()).isEqualTo("Internal Server Error");

    }

    @Test
    void balance() {
        String generatedToken = generateToken(PAN_VALUE);

        Map<String, String> param = new HashMap<>();
        param.put(PAN, PAN_VALUE);
        param.put(BALANCE, BALANCE_VALUE.toString());
        param.put(TOKEN, generatedToken);

        Mockito.when(atmService.checkBalance(generatedToken)).thenReturn(param);

        ResponseEntity responseEntity = atmController.balance(generatedToken);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(param);
    }

    @Test
    void balanceNotValid() {
        String generatedToken = generateToken(PAN_VALUE);

        Mockito.when(atmService.checkBalance(generatedToken)).thenReturn(null);

        ResponseEntity responseEntity = atmController.balance(generatedToken);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(responseEntity.getStatusCode().getReasonPhrase()).isEqualTo("Internal Server Error");

    }

    @Test
    void cashOut() {
        String generatedToken = generateToken(PAN_VALUE);

        Map<String, String> param = new HashMap<>();
        param.put(PAN, PAN_VALUE);
        param.put(BALANCE, BALANCE_VALUE.toString());
        param.put(TOKEN, null);

        Mockito.when(atmService.withdrawMoney(generatedToken, AMOUNT)).thenReturn(param);

        ResponseEntity responseEntity = atmController.cashOut(generatedToken, AMOUNT);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(param);

    }

    @Test
    void cashOutNotValid() {
        String generatedToken = generateToken(PAN_VALUE);

        Mockito.when(atmService.withdrawMoney(generatedToken, AMOUNT)).thenReturn(null);

        ResponseEntity responseEntity = atmController.cashOut(generatedToken, AMOUNT);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(responseEntity.getStatusCode().getReasonPhrase()).isEqualTo("Internal Server Error");

    }

    @Test
    void removeCard() {

        Mockito.when(atmService.removeCardFromAtm(PAN_VALUE)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity responseEntity = atmController.removeCard(PAN_VALUE);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(new ResponseEntity<>(HttpStatus.OK));

    }

    @Test
    void removeCardNotValid() {

        Mockito.when(atmService.removeCardFromAtm(PAN_VALUE_NOT_VALID)).thenThrow(HttpServerErrorException.class);

        assertThrows(HttpServerErrorException.class, () -> {
            atmController.removeCard(PAN_VALUE_NOT_VALID);
        });
    }

    private String generateToken(String pan) {
        Algorithm algorithm = Algorithm.HMAC256(CARD);

        Instant now = Instant.now();
        String jwtToken = JWT.create()
                .withIssuer(CARD_UPPERCASE)
                .withSubject(CARD_DETAILS)
                .withClaim(PAN, pan)
                .withIssuedAt(now)
                .withExpiresAt(now.plusSeconds(120))
                .sign(algorithm);

        return jwtToken;
    }
}