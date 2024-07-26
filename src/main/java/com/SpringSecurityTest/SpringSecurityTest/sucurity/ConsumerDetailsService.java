package com.SpringSecurityTest.SpringSecurityTest.sucurity;

import com.SpringSecurityTest.SpringSecurityTest.models.Consumer;
import com.SpringSecurityTest.SpringSecurityTest.repositories.ConsumerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ConsumerDetailsService implements UserDetailsService {
    private final ConsumerRepository consumerRepository;

    @Autowired
    public ConsumerDetailsService(ConsumerRepository consumerRepository) {
        this.consumerRepository = consumerRepository;
    }

    @Override
    public ConsumerDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Consumer> consumer = consumerRepository.findByUsername(username);

        if (consumer.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        return new ConsumerDetails(consumer.get());
    }
}