package com.atm.simulator;

import com.atm.simulator.exception.NullCardException;
import com.atm.simulator.model.AtmData;
import com.atm.simulator.model.Card;
import com.atm.simulator.services.AtmService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.time.Instant;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
//@RunWith(SpringRunner.class)
//@AutoConfigureMockMvc
@SpringBootTest
public class AtmServiceTest {

    private final String PAN = "pan";
    private final String PIN = "PIN";
    private final String PIN_OR_FINGERPRINT = "1234";
    private final String TOKEN = "token";
    private final String CARD_PAN = "1234567891234567";
    private final Integer BALANCE = 10;
    private final String CARD_BALANCE = "cardBalance";
    private final String PAN_OF_CARD = "cardPan";
    private final String CARD = "card";
    private final String CARD_UPPERCASE = "Card";

    Card card = new Card("1234567891234567", "02/24", "Lena Sargsyan",
            "VISA", "HSBC");

    @Autowired
    AtmService atmService;

    @Test
    public void testCheckCard(){

        HashMap<String, String> h =  new LinkedHashMap<>();
        h.put(PAN, card.getPan());

        ResponseEntity expected =
                ResponseEntity.status(HttpStatus.OK).header(null).body(h);

        ResponseEntity actual = atmService.checkCard(card);

        assertEquals(expected,actual);
    }

    /**
     *
     * @throws NullCardException
     */

    @Test
    public void testCheckPin() throws NullCardException {

        atmService.checkCard(card);
        // checkCardTest();

        AtmData atmData = new AtmData(PIN, PIN_OR_FINGERPRINT, CARD_PAN);

        /*HashMap<String, String> h =  new LinkedHashMap<>();
        h.put(PAN, card.getPan());

        ResponseEntity expected =
              ResponseEntity.status(HttpStatus.OK).header(null).body(h);*/

       // checkCardTest();

        ResponseEntity actual = atmService.checkPinCode(atmData);

        String token = ((LinkedHashMap) actual.getBody()).get(TOKEN).toString();

        String panFromToken = getPanFromToken(token);

        //assertEquals(((LinkedHashMap) expected.getBody()).get("pan"),((LinkedHashMap) actual.getBody()).get("pan"));
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(card.getPan(), panFromToken);
    }

    @Test
    public void testCheckBalance() throws NullCardException {
        atmService.checkCard(card);

        AtmData atmData = new AtmData(PIN, PIN_OR_FINGERPRINT, CARD_PAN);
        ResponseEntity checkPinCodeResponse = atmService.checkPinCode(atmData);

        String token = ((LinkedHashMap) checkPinCodeResponse.getBody()).get(TOKEN).toString();

        ResponseEntity actual = atmService.checkBalance(token);

        String newToken = ((LinkedHashMap) checkPinCodeResponse.getBody()).get(TOKEN).toString();

        String panFromToken = getPanFromToken(newToken);

        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(card.getPan(), panFromToken);
    }

    @Test
    public void withdrawMoney() throws NullCardException {
        atmService.checkCard(card);

        AtmData atmData = new AtmData(PIN, PIN_OR_FINGERPRINT, CARD_PAN);
        ResponseEntity checkPinCodeResponse = atmService.checkPinCode(atmData);

        String token = ((LinkedHashMap) checkPinCodeResponse.getBody()).get(TOKEN).toString();

        ResponseEntity checkCardBalance = atmService.checkBalance(token);
        Integer cardBalance = (Integer) ((LinkedHashMap) checkCardBalance.getBody()).get(CARD_BALANCE);

        Integer b = cardBalance - BALANCE;

        HashMap<String, Object> h =  new LinkedHashMap<>();
        h.put(PAN_OF_CARD, CARD_PAN);
        h.put(CARD_BALANCE, b);

        ResponseEntity expected =
                ResponseEntity.status(HttpStatus.OK).header(null).body(h);

        ResponseEntity actual = atmService.withdrawMoney(token, BALANCE);

        //String cardPan = ((LinkedHashMap) checkPinCodeResponse.getBody()).get(TOKEN).toString();

        //assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(expected, actual);
    }



    private String getPanFromToken(String token){
        Algorithm algorithm = Algorithm.HMAC256(CARD);

        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(CARD_UPPERCASE)
                .build();
        DecodedJWT decodedJWT = verifier.verify(token);

        String cardPan = ((Claim) decodedJWT.getClaim(PAN)).asString();

        return cardPan;
    }

}

