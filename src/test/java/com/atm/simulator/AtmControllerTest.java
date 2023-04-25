package com.atm.simulator;


import com.atm.simulator.controllers.AtmController;
import com.atm.simulator.model.Card;

import com.atm.simulator.services.AtmService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashMap;
import java.util.LinkedHashMap;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@RunWith(SpringJUnit4ClassRunner.class)
//@RunWith(SpringRunner.class)
//@AutoConfigureMockMvc
//@WebMvcTest(AtmController.class)
//@SpringBootTest
//@SpringBootApplication
//@ContextConfiguration(locations = {"file:src/main/resources/webapp/WEB-INF/app.xml"})
//@ContextConfiguration(locations = {"file:src/main/resources/application.properties"})
//@ExtendWith(SpringExtension.class)
//@ExtendWith(MockitoExtension.class)

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AtmControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AtmService atmService;

    @Test
    public void testIinsertCard() throws Exception{
        Card card = new Card("1234567891234567", "02/24", "Lena Sargsyan",
                "VISA", "HSBC");

        /*HashMap<String, String> h =  new LinkedHashMap<>();
        h.put("pan", card.getPan());

        ResponseEntity expected =
                ResponseEntity.status(HttpStatus.OK).header(null).body(h);
    //    Mockito.when(atmService.checkCard(card)).thenReturn(expected);
        Mockito.when(atmService.checkCard(card)).thenReturn(expected);*/

        /*mockMvc.perform(post("http://localhost:8088/bank/insertCard").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tutorial)))
                .andExpect(status().isCreated());*/

        mockMvc.perform(MockMvcRequestBuilders
                .post("http://localhost:8088/bank/insertCard")
                .content(asJsonString(card))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        /*mockMvc.perform(post("http://localhost:8088/bank/insertCard")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));*/
    }


    @Test
    public void checkCardTest1() throws Exception
    {
        Card card = new Card("1234567891234567", "02/24", "Lena Sargsyan",
                "VISA", "HSBC");

        HashMap<String, String> h =  new LinkedHashMap<>();
        h.put("pan", "1234567891234567");

        ResponseEntity expected =
                ResponseEntity.status(HttpStatus.OK).header(null).body(h);

        ResponseEntity actual = (ResponseEntity) mockMvc.perform(post("http://localhost:8088/bank/insertCard")
                .content(asJsonString(card))
                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
//                .andExpect(body("$[0].name", is("pan")));

/*        ResponseEntity actual = (ResponseEntity) mockMvc.perform( MockMvcRequestBuilders
                .post("http://localhost:8088/bank/insertCard")
                //.content(asJsonString(new Card("1234567891234567", "02/24", "Lena Sargsyan", "VISA", "HSBC")))
                .content(asJsonString(card))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));*/
        assertEquals(expected,actual);
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
