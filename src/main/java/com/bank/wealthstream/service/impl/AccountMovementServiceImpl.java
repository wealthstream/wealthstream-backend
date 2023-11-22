package com.bank.wealthstream.service.impl;

import com.bank.wealthstream.model.Account;
import com.bank.wealthstream.model.Customer;
import com.bank.wealthstream.model.enums.AccountType;
import com.bank.wealthstream.model.enums.TypeAccountMovement;
import com.bank.wealthstream.repository.AccountMovementRepository;
import com.bank.wealthstream.repository.AccountRepository;
import com.bank.wealthstream.repository.CustomerRepository;
import com.bank.wealthstream.service.AccountMovementService;
import com.bank.wealthstream.service.dto.AccountMovementDto;
import com.bank.wealthstream.service.dto.TransferDto;
import com.bank.wealthstream.service.mapper.AccountMapper;
import com.bank.wealthstream.service.mapper.AccountMovementMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AccountMovementServiceImpl implements AccountMovementService {
    private AccountMapper accountMapper;
    private AccountMovementRepository accountMovementRepository;
    private AccountMovementMapper accountMovementMapper;
    private CustomerRepository customerRepository;

    private AccountRepository accountRepository;

    public AccountMovementServiceImpl(AccountMapper accountMapper, AccountRepository accountRepository,
                                      AccountMovementMapper accountMovementMapper, AccountMovementRepository accountMovementRepository,
                                      CustomerRepository customerRepository) {
        this.accountMapper = accountMapper;
        this.accountRepository = accountRepository;
        this.accountMovementMapper = accountMovementMapper;
        this.accountMovementRepository = accountMovementRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public AccountMovementDto makeDeposit(AccountMovementDto accountMovementDto) {
        if (!(accountMovementDto.getMovementType().trim().toUpperCase().equals(TypeAccountMovement.DEPOSITO.toString()))) {
            return null;
        }
        return processTransaction(accountMovementDto, accountMovementDto.getMovementType().trim().toUpperCase());
    }

    @Override
    public AccountMovementDto makeWithdrawal(AccountMovementDto accountMovementDto) {
        if (!(accountMovementDto.getMovementType().trim().toUpperCase().equals(TypeAccountMovement.RETIRO.toString()))) {
            return null;
        }
        return processTransaction(accountMovementDto, accountMovementDto.getMovementType().trim().toUpperCase());
    }

    @Override
    public AccountMovementDto makeTransfer(TransferDto transferDto) {
        AccountMovementDto accountMovementDto = new AccountMovementDto();
        Account originAccount = accountRepository.getAccount(transferDto.getOriginAccount());
        accountMovementDto.setIdAcc(accountMapper.accountToAccountDto(originAccount));
        accountMovementDto.setValue(transferDto.getValue());

        processTransaction(accountMovementDto, TypeAccountMovement.RETIRO.toString());
        Account destinationAccount = accountRepository.getAccount(transferDto.getDestinationAccount());
        accountMovementDto.setIdAcc(accountMapper.accountToAccountDto(destinationAccount));

        return processTransaction(accountMovementDto, TypeAccountMovement.DEPOSITO.toString());
    }

    private AccountMovementDto processTransaction(AccountMovementDto accountMovementDto, String transactionType) {
        Account account = accountRepository.getAccount(accountMovementDto.getIdAcc().getAccountNumber());
        String accountType = account.getAccountType();
        double initialBalance = account.getInitialBalance();
        double transactionValue = accountMovementDto.getValue();

        if (isValidTransaction(accountType, initialBalance, transactionValue, transactionType)) {
            updateAccountAndMovement(account, accountMovementDto, initialBalance, transactionValue, transactionType);
            accountMovementDto.setDate(LocalDateTime.now());
        }

        accountMovementDto.setMovementType(transactionType);

        return accountMovementMapper.accountMovementToAccountMovementDto(accountMovementRepository.save(accountMovementMapper.accountMovementDtoTAccountMovement(accountMovementDto)));
    }

    private boolean isValidTransaction(String accountType, double initialBalance, double transactionValue, String transactionType) {
        return (accountType.trim().toUpperCase().equals(AccountType.AHORROS.toString()) || accountType.trim().toUpperCase().equals(AccountType.CORRIENTE.toString())) &&
                (initialBalance >= transactionValue && transactionType.trim().toUpperCase() != TypeAccountMovement.RETIRO.toString() || initialBalance >= 0);
    }

    private void updateAccountAndMovement(Account account, AccountMovementDto accountMovementDto,
                                     double initialBalance, double transactionValue, String transactionType) {
        accountMovementDto.setIdAcc(accountMapper.accountToAccountDto(account));
        account.getIdCus().getIdPer().setIdentification(accountMovementDto.getIdAcc().getIdCus().getPerson().getIdentification());

        double newBalance = 0.0;

        if (transactionType.trim().toUpperCase().equals(TypeAccountMovement.DEPOSITO.toString())) {
            newBalance = initialBalance + transactionValue;
        }

        if (transactionType.trim().toUpperCase().equals(TypeAccountMovement.RETIRO.toString())){
            newBalance = initialBalance - transactionValue;
        }

        account.setInitialBalance(newBalance);
        accountMovementDto.setValue(transactionValue);
        accountMovementDto.setBalance(newBalance);
    }
}
