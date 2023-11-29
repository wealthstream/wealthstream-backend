package com.bank.wealthstream.service.impl;

import com.bank.wealthstream.controller.CustomerController;
import com.bank.wealthstream.model.Customer;
import com.bank.wealthstream.model.Person;
import com.bank.wealthstream.repository.CustomerRepository;
import com.bank.wealthstream.repository.PersonRepository;
import com.bank.wealthstream.security.Encryption;
import com.bank.wealthstream.service.CustomerService;
import com.bank.wealthstream.service.PersonService;
import com.bank.wealthstream.service.dto.CustomerDto;
import com.bank.wealthstream.service.dto.LoginDto;
import com.bank.wealthstream.service.dto.PersonDto;
import com.bank.wealthstream.service.mapper.CustomerMapper;
import com.bank.wealthstream.service.mapper.PersonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CustomerServiceImpl implements CustomerService {
    private CustomerMapper customerMapper;
    private CustomerRepository customerRepository;

    private PersonMapper personMapper;
    private PersonRepository personRepository;

    public CustomerServiceImpl(CustomerMapper customerMapper, CustomerRepository customerRepository, PersonMapper personMapper, PersonRepository personRepository) {
        this.customerMapper = customerMapper;
        this.customerRepository = customerRepository;

        this.personMapper = personMapper;
        this.personRepository = personRepository;
    }

    @Override
    public CustomerDto createCustomer(CustomerDto customerDto) {
        Person person = personMapper.personDtoToPerson(customerDto.getPerson());
        if (person.getIdPer().equals("")) {
            person.setIdPer(null);
        }

        Optional<Customer> optionalCustomer = customerRepository.getCustomerByIdentificationOptional(customerDto.getPerson().getIdentification());

        if (optionalCustomer.isPresent()) {
            return null;
        }

        Person personSave = personRepository.save(person);
        customerDto.setIdCus(personSave.getIdPer());
        customerDto.setPassword(Encryption.aesEncrypt(customerDto.getPassword(), false));
        customerDto.setPerson(null);
        CustomerDto customer = customerMapper.customerToCustomerDto(customerRepository.save(customerMapper.customerDtoToCustomer(customerDto)));
        customer.setPerson(personMapper.personToPersonDto(person));
        
        return customer;
    }

    @Override
    public CustomerDto updateCustomer(CustomerDto customerDto) {
         Person person = personMapper.personDtoToPerson(customerDto.getPerson());
        if (!personRepository.findById(person.getIdPer()).isPresent()) {
            return null;
        }

        Person personSave = personRepository.save(person);
        customerDto.setIdCus(personSave.getIdPer());
        customerDto.setPassword(Encryption.aesEncrypt(customerDto.getPassword(), false));

        return customerMapper.customerToCustomerDto(customerRepository.save(customerMapper.customerDtoToCustomer(customerDto)));
    }

    @Override
    public CustomerDto getCustomerByIdentification(String identification) {
        Customer customer = customerRepository.getCustomerByIdentification(identification);

        if (!(Objects.isNull(customer))) {
            return customerMapper.customerToCustomerDto(customer);
        } else {
            return new CustomerDto();
        }
    }


    @Override
    public List<CustomerDto> getCustomers() {
        return StreamSupport.stream(customerRepository.findAll().spliterator(), false)
                .map(customerMapper::customerToCustomerDto)
                .collect(Collectors.toList());
    }

    @Override
    public LoginDto logIn(LoginDto loginDto) {
        Optional<Customer> optionalCustomer = customerRepository.getCredentials(loginDto.getEmail(),Encryption.aesEncrypt(loginDto.getPassword(), false));
        if (optionalCustomer.isEmpty()) {
            return null;
        }
        return loginDto;
    }
}
