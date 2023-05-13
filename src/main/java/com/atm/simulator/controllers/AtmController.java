package com.atm.simulator.controllers;

import com.atm.simulator.exception.NullCardException;
import com.atm.simulator.model.AtmData;
import com.atm.simulator.model.Card;
import com.atm.simulator.services.AtmService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/atm")
@Api(value = "atm")
public class AtmController {

    private final String PAN = "pan";
    private final String TOKEN = "token";
    private final String AMOUNT = "amount";

    private final String INSERT_CARD = "/insertCard";
    private final String ENTER_PIN = "/enterPin";
    private final String BALANCE = "/balance";
    private final String CASH_OUT = "/cashOut";
    private final String REMOVE_CARD = "/removeCard";

    @Autowired
    AtmService atmService;

    @PostMapping(INSERT_CARD)
    public ResponseEntity insertCard(@RequestBody Card card) {

        Object response = atmService.checkCard(card);

        /*if(((ResponseEntity) response).getStatusCode() == HttpStatus.OK) {
            return new ResponseEntity((ResponseEntity) response, HttpStatus.OK);
        }*/

        if(response != null) {
            return new ResponseEntity(response, HttpStatus.OK);
        }
        return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @PostMapping(ENTER_PIN)
    public ResponseEntity checkPin(@RequestBody AtmData atmData) throws NullCardException {
        Object response = atmService.checkPinCode(atmData);

        if(response != null) {
            return new ResponseEntity(response, HttpStatus.OK);
        }
        return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping(BALANCE)
    public ResponseEntity balance(@RequestParam(TOKEN) String token) {
        Object response = atmService.checkBalance(token);

        if(response != null) {
            return new ResponseEntity(response, HttpStatus.OK);
        }
        return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping(CASH_OUT)
    public ResponseEntity cashOut(@RequestParam(TOKEN) String token, @RequestParam(AMOUNT) Integer amount){
        Object response = atmService.withdrawMoney(token, amount);

        if(response != null) {
            return new ResponseEntity(response, HttpStatus.OK);
        }
        return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @PostMapping(REMOVE_CARD)
    public ResponseEntity removeCard(@RequestParam(PAN) String pan) {

        if(atmService.removeCardFromAtm(pan) != null) {
            return new ResponseEntity(atmService.removeCardFromAtm(pan), HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);

        //return new ResponseEntity(atmService.removeCardFromAtm(pan), HttpStatus.OK);
    }

}
