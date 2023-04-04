package com.atm.simulator.controllers;

import com.atm.simulator.allException.NullCardException;
import com.atm.simulator.model.ATM;
import com.atm.simulator.model.Card;
import com.atm.simulator.services.AtmService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/atm")
@Api(value = "hello atm")
public class AtmController {

    @Autowired
    AtmService atmService;

    @PostMapping("/insertCard")
    public ResponseEntity insertCard(@RequestBody Card card){
        ResponseEntity response = atmService.checkCard(card);

        if(response.getStatusCode() == HttpStatus.OK){
            return new ResponseEntity(response, HttpStatus.OK);
        }
        return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @PostMapping("/enterPin")
    public ResponseEntity enterPin(@RequestBody ATM atm) throws NullCardException {
        ResponseEntity response = atmService.checkPinCode(atm);

        if(response.getStatusCode() == HttpStatus.OK){
            return new ResponseEntity(response, HttpStatus.OK);
        }
        return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/balance")
    public ResponseEntity balance(@RequestParam("token") String token){
        ResponseEntity response = atmService.checkBalance(token);

        if(response.getStatusCode() == HttpStatus.OK){
            return new ResponseEntity(response, HttpStatus.OK);
        }
        return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/cashOut")
    public ResponseEntity cashOut(@RequestParam("token") String token, @RequestParam("amount") Integer amount){
        ResponseEntity response = atmService.withdrawMoney(token, amount);

        if(response.getStatusCode() == HttpStatus.OK){
            return new ResponseEntity(response, HttpStatus.OK);
        }
        return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @PostMapping("/removeCard")
    public ResponseEntity removeCard(@RequestParam("pan") String pan){
        return new ResponseEntity(atmService.removeCardFromAtm(pan), HttpStatus.OK);
    }

}
