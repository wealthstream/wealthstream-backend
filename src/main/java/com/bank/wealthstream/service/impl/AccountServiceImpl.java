package com.bank.wealthstream.service.impl;

import com.bank.wealthstream.model.Account;
import com.bank.wealthstream.model.enums.AccountType;
import com.bank.wealthstream.repository.AccountRepository;
import com.bank.wealthstream.service.AccountService;
import com.bank.wealthstream.service.dto.AccountDto;
import com.bank.wealthstream.service.mapper.AccountMapper;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

@Service
public class AccountServiceImpl implements AccountService {
    private AccountRepository accountRepository;
    private AccountMapper accountMapper;

    public AccountServiceImpl(AccountRepository accountRepository, AccountMapper accountMapper) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
    }

    @Override
    public AccountDto createAccount(AccountDto accountDto) {
        Optional<Account> account = accountRepository.ifExistAccount(
                accountDto.getIdCus().getIdCus(),
                accountDto.getAccountNumber());
        if (account.isPresent()) {
            return null;
        }

        if (accountDto.getAccountType().trim().toUpperCase().equals(AccountType.AHORROS.toString())) {
            accountDto.setAccountType(AccountType.AHORROS.toString());
            accountDto.setAccountNumber(accountGenerateNumber(AccountType.AHORROS.toString()));
        }

        if (accountDto.getAccountType().trim().toUpperCase().equals(AccountType.CORRIENTE.toString())) {
            accountDto.setAccountType(AccountType.CORRIENTE.toString());
            accountDto.setAccountNumber(accountGenerateNumber(AccountType.CORRIENTE.toString()));
        }

        return accountMapper.accountToAccountDto(accountRepository.save(accountMapper.accountDtoToAccount(accountDto)));
    }

    @Override
    public List<AccountDto> getAccountByIdentification(String identification) {
        System.out.println("CEDULA "+ identification);
        return accountRepository.getAccountByIdentification(identification).stream()
                .map(accountMapper:: accountToAccountDto)
                .collect(Collectors.toList());
    }

    private String accountGenerateNumber(String accountType) {
        Map<String, Integer> numberGenerate = new HashMap<>();
        String accountNumber = "";
        Random randNumber = new Random();

        do {
            int randomNumber = 1000 + randNumber.nextInt(9999);
            if (accountType.trim().toUpperCase().equals(AccountType.AHORROS.toString())) {
                accountNumber = accountCode(AccountType.AHORROS.toString()) + branchCode(AccountType.AHORROS.toString()) + String.format("%04d", randomNumber);
            }

            if (accountType.trim().toUpperCase().equals(AccountType.CORRIENTE.toString())) {
                accountNumber = accountCode(AccountType.CORRIENTE.toString()) + branchCode(AccountType.CORRIENTE.toString()) + String.format("%04d", randomNumber);
            }
        } while (numberGenerate.containsKey(accountNumber));

        return changePositionNumber(accountNumber);
    }

    private String changePositionNumber(String accountNumber) {
        if (accountNumber.length() != 10) {
            return "Account number must be 10 digits";
        }

        char account[] = accountNumber.toCharArray();
        int start = account.length / 2 - 3;
        int end = account.length / 2 + 2;

        IntStream.range(0, 3)
                .forEach(i -> {
                    char temp = account[start + i];
                    account[start + i] = account[end + i];
                    account[end + i] = temp;
                });
        return new String(account);
    }

    private String branchCode(String accountType) {
        String branchCode = "";
        if (accountType.trim().toUpperCase().equals(AccountType.AHORROS.toString())) {
            branchCode = "0022";
        }

        if (accountType.trim().toUpperCase().equals(AccountType.CORRIENTE.toString())) {
            branchCode = "0055";
        }

        return branchCode;
    }

    private String accountCode(String accountType) {
        String accountCode = "";
        if (accountType.trim().toUpperCase().equals(AccountType.AHORROS.toString())) {
            accountCode = "22";
        }

        if (accountType.trim().toUpperCase().equals(AccountType.CORRIENTE.toString())) {
            accountCode = "55";
        }

        return accountCode;
    }
}
