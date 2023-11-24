package com.bank.wealthstream.service;

import com.bank.wealthstream.service.dto.AccountMovementDto;
import com.bank.wealthstream.service.dto.TransferDto;

import java.util.List;

public interface AccountMovementService {
    List<AccountMovementDto> getAccountMovementByIdentification(String identification);
    AccountMovementDto makeDeposit(AccountMovementDto accountMovementDto);
    AccountMovementDto makeWithdrawal(AccountMovementDto accountMovementDto);
    AccountMovementDto makeTransfer(TransferDto transferDto);
}
