package com.atm.simulator.services;

import com.atm.simulator.ApplicationProperties;
import com.atm.simulator.CardValidation;
import com.atm.simulator.domain.BankCardStorage;
import com.atm.simulator.model.Card;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({MockitoExtension.class})
class AtmServiceTest {

    private final String PAN = "pan";

    private final String PAN_VALUE = "1234567891234567";
    private final String EXPIRY_DATE = "02/24";
    private final String CARD_HOLDER = "Lena Sargsyan";
    private final String CARD_TYPE = "VISA";
    private final String ISSUER = "HSBC";

    @InjectMocks
    AtmService atmService;

    @Mock
   private  RestTemplate restTemplate;

    @Mock
    CardValidation cardValidation;

    @Mock
    BankCardStorage bankCardStorage;

    @Mock
    ApplicationProperties applicationProperties;

    @Spy
    private RestTemplate restT = new RestTemplate();

    @Test
    void checkCard() {

        Card card = new Card(PAN_VALUE, EXPIRY_DATE, CARD_HOLDER, CARD_TYPE, ISSUER);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<Card> entity = new HttpEntity<Card>(card,headers);

        HashMap<Object, Object> h =  new HashMap<>();
        h.put(PAN, card.getPan());

        Mockito.when(cardValidation.isValid(card)).thenReturn(true);

      //  Mockito.when(applicationProperties.BANK_URL).thenReturn("http://localhost:8088/bank/");
      //  Mockito.when(applicationProperties.CHECK_CARD).thenReturn("checkCard");

        ResponseEntity<Object> myEntity = new ResponseEntity<Object>(HttpStatus.OK);


//        Mockito.when(restTemplate.exchange("http://localhost:8088/bank/".concat("checkCard"),
//               HttpMethod.POST, entity, Object.class)).thenReturn(new ResponseEntity<>(h, headers, HttpStatus.OK));

    Mockito.when(restTemplate.exchange("http://localhost:8088/bank/".concat("checkCard"),
               HttpMethod.POST, entity, Object.class)).thenReturn(myEntity);


//        Mockito.when(restTemplate.exchange(Mockito.endsWith("http://localhost:8088/bank/"), Mockito.eq(HttpMethod.POST),
//                Mockito.any(), Mockito.eq(Object.class)))
//                .thenReturn(myEntity);


       /* ResponseEntity<Object> resp = new ResponseEntity<>(h, headers, HttpStatus.OK);


        Mockito.when(restTemplate.exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<Object>>any()))
        .thenReturn(resp);*/



        Mockito.when(bankCardStorage.putCard(card)).thenReturn(true);

        Object responseEntity = atmService.checkCard(card);
        assertThat(responseEntity).isNotNull();
        //assertThat(responseEntity.).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity).isEqualTo(h);

    }

    @Test
    void checkPinCode() {
    }

    @Test
    void checkBalance() {
    }

    @Test
    void withdrawMoney() {
    }

    @Test
    void removeCardFromAtm() {
    }
}