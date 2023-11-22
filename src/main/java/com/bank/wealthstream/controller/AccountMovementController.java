package com.bank.wealthstream.controller;

import com.bank.wealthstream.service.AccountMovementService;
import com.bank.wealthstream.service.dto.AccountMovementDto;
import com.bank.wealthstream.service.dto.TransferDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account-movement/movement")
@CrossOrigin(origins = "*")
public class AccountMovementController {
    private AccountMovementService accountMovementService;

    public AccountMovementController(AccountMovementService accountMovementService) {
        this.accountMovementService = accountMovementService;
    }

    @PostMapping("/deposit")
    @ResponseBody
    public ResponseEntity<?> makeDeposit(@RequestBody AccountMovementDto accountMovementDto) {
        return new ResponseEntity<>(accountMovementService.makeDeposit(accountMovementDto), HttpStatus.CREATED);
    }

    @PostMapping("/withdrawal")
    @ResponseBody
    public ResponseEntity<?> makeWithdrawal(@RequestBody AccountMovementDto accountMovementDto) {
        return new ResponseEntity<>(accountMovementService.makeWithdrawal(accountMovementDto), HttpStatus.CREATED);
    }

    @PostMapping("/transfer")
    @ResponseBody
    public ResponseEntity<?> maketransfer(@RequestBody TransferDto transferDto) {
        return new ResponseEntity<>(accountMovementService.makeTransfer(transferDto), HttpStatus.CREATED);
    }

}
