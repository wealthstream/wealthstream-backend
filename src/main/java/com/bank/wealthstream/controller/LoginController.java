package com.bank.wealthstream.controller;

import com.bank.wealthstream.model.Customer;
import com.bank.wealthstream.security.JwtTokenUtil;
import com.bank.wealthstream.service.CustomerService;
import com.bank.wealthstream.service.dto.AuthResponse;
import com.bank.wealthstream.service.dto.LoginDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class LoginController {

    private final JwtTokenUtil jwtTokenUtil;
    private CustomerService customerService;

    public LoginController(JwtTokenUtil jwtTokenUtil, CustomerService customerService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.customerService = customerService;
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<?> logIng(@RequestBody LoginDto loginDto) {
        LoginDto login = customerService.logIn(loginDto);

        if (login == null) {
            return null;
        }

        String accessToken = jwtTokenUtil.generateAccessToken(loginDto);
        AuthResponse response = new AuthResponse(loginDto.getEmail(), accessToken);
        return ResponseEntity.ok().body(response);
    }
}
