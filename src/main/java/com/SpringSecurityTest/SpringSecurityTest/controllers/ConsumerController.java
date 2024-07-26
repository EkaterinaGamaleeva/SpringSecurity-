package com.SpringSecurityTest.SpringSecurityTest.controllers;


import com.SpringSecurityTest.SpringSecurityTest.DTO.ConsumerDTO;
import com.SpringSecurityTest.SpringSecurityTest.models.Consumer;
import com.SpringSecurityTest.SpringSecurityTest.response.ConsumerNotFoundException;
import com.SpringSecurityTest.SpringSecurityTest.services.ConsumerService;
import com.SpringSecurityTest.SpringSecurityTest.sucurity.ConsumerDetails;
import com.SpringSecurityTest.SpringSecurityTest.sucurity.JwtUtil;
import com.SpringSecurityTest.SpringSecurityTest.util.ConsumerValidation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("")
public class ConsumerController {
    private final ConsumerService service;
    private final ConsumerValidation consumerValidation;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

//    private  final ModelMapper modelMapper;

    @Autowired
    public ConsumerController(ConsumerService service, ConsumerValidation consumerValidation, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.service = service;
        this.consumerValidation = consumerValidation;
        this.jwtUtil = jwtUtil;
//        this.modelMapper = modelMapper;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/consumers")
    public ResponseEntity getConsumer() {
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/consumer/{id}")
    public ResponseEntity getConsumerById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody ConsumerDTO consumerDTO) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    consumerDTO.getUsername(),
                    consumerDTO.getPassword()
            ));
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>("Не верный логин или пароль", HttpStatus.NOT_FOUND);
        }
        System.out.println("я прошел");
        Consumer consumer = service.findByUsername(consumerDTO.getUsername());
        String token = jwtUtil.generateToken(new ConsumerDetails(consumer));
        return new ResponseEntity<>(Map.of("jwtToken", token), HttpStatus.OK);
    }

    @PostMapping("/registration")
    public ResponseEntity registration(@RequestBody @Valid Consumer consumer, BindingResult bindingResult) {
        consumerValidation.validate(consumer, bindingResult);
        if (bindingResult.hasErrors()) {
            StringBuilder builder = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                builder.append(error.getField()).append("-").append(error.getDefaultMessage()).append(";");
                throw new ConsumerNotFoundException();
            }
        }
        service.save(consumer);
        String token = jwtUtil.generateToken(new ConsumerDetails(consumer));

        return new ResponseEntity(Map.of("jwtToken", token), HttpStatus.CREATED);
    }

}