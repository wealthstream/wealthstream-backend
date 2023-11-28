package com.bank.wealthstream.service;

import com.bank.wealthstream.model.Customer;
import com.bank.wealthstream.service.dto.CustomerDto;
import com.bank.wealthstream.service.dto.LoginDto;

import java.util.List;

public interface CustomerService {
    CustomerDto createCustomer(CustomerDto customerDto);
    CustomerDto updateCustomer(CustomerDto customerDto);
    CustomerDto getCustomerByIdentification(String identification);
    List<CustomerDto> getCustomers();
    LoginDto logIn(LoginDto loginDto);
}
