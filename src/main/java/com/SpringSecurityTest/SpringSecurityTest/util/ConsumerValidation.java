package com.SpringSecurityTest.SpringSecurityTest.util;


import com.SpringSecurityTest.SpringSecurityTest.models.Consumer;
import com.SpringSecurityTest.SpringSecurityTest.services.ConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ConsumerValidation implements Validator {
    private final ConsumerService consumerService;

    @Autowired
    public ConsumerValidation(ConsumerService consumerService) {
        this.consumerService = consumerService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Consumer.class.equals(clazz);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Consumer consumer = (Consumer) o;
        if (consumerService.getConsumerLogin(((Consumer) o).getUsername()).isPresent()) {
            errors.rejectValue("login", "", "Человек с таким логином уже существует");

        }

        if (consumerService.getConsumerEmail(consumer.getEmail()).isPresent()) {
            errors.rejectValue("email", "", "Человек с таким Email уже существует");
        }
    }

}
