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

        ResponseEntity response = atmService.checkCard(card);

        if(response.getStatusCode() == HttpStatus.OK) {
            return new ResponseEntity(response, HttpStatus.OK);
        }
        return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @PostMapping(ENTER_PIN)
    public ResponseEntity checkPin(@RequestBody AtmData atmData) throws NullCardException {
        ResponseEntity response = atmService.checkPinCode(atmData);

        if(response.getStatusCode() == HttpStatus.OK) {
            return new ResponseEntity(response, HttpStatus.OK);
        }
        return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping(BALANCE)
    public ResponseEntity balance(@RequestParam(TOKEN) String token) {
        ResponseEntity response = atmService.checkBalance(token);

        if(response.getStatusCode() == HttpStatus.OK) {
            return new ResponseEntity(response, HttpStatus.OK);
        }
        return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping(CASH_OUT)
    public ResponseEntity cashOut(@RequestParam(TOKEN) String token, @RequestParam(AMOUNT) Integer amount){
        ResponseEntity response = atmService.withdrawMoney(token, amount);

        if(response.getStatusCode() == HttpStatus.OK) {
            return new ResponseEntity(response, HttpStatus.OK);
        }
        return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @PostMapping("REMOVE_CARD")
    public ResponseEntity removeCard(@RequestParam(PAN) String pan) {
        return new ResponseEntity(atmService.removeCardFromAtm(pan), HttpStatus.OK);
    }

}
