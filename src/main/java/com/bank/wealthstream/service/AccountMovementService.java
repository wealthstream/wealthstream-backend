package com.bank.wealthstream.service;

import com.bank.wealthstream.model.AccountMovement;
import com.bank.wealthstream.service.dto.AccountMovementDto;
import com.bank.wealthstream.service.dto.CustomerDto;
import com.bank.wealthstream.service.dto.TransferDto;

public interface AccountMovementService {
    //AccountMovementDto createAccountMovement(AccountMovementDto accountMovementDto);
    AccountMovementDto makeDeposit(AccountMovementDto accountMovementDto);
    AccountMovementDto makeWithdrawal(AccountMovementDto accountMovementDto);
    AccountMovementDto makeTransfer(TransferDto transferDto);
}
