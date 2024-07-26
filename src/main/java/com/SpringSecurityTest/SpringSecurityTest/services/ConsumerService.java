package com.SpringSecurityTest.SpringSecurityTest.services;

import com.SpringSecurityTest.SpringSecurityTest.models.Consumer;
import com.SpringSecurityTest.SpringSecurityTest.models.EnumRole;
import com.SpringSecurityTest.SpringSecurityTest.repositories.ConsumerRepository;
import com.SpringSecurityTest.SpringSecurityTest.response.ConsumerNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
public class ConsumerService {

    private final ConsumerRepository consumerRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ConsumerService(ConsumerRepository consumerRepository, PasswordEncoder passwordEncoder) {
        this.consumerRepository = consumerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //    //возвращение всех пассажиров
//    @Cacheable("consumers")
    public List<Consumer> findAll() {

        return consumerRepository.findAll();
    }

    public Optional<Consumer> getConsumerLogin(String l) {
        return consumerRepository.findByUsername(l);
    }

    public Optional<Consumer> getConsumerEmail(String e) {
        return consumerRepository.findByEmail(e);
    }

    //
//
//    //возвращает пассажира по id
//    @CachePut(value = "consumers", key = "#id")
    public Consumer findById(Long id) {
        Optional<Consumer> foundPerson = consumerRepository.findById(id);
        return foundPerson.get();
    }

    //
//    //изменение пассажира
//    @Transactional
//    @CachePut(value = "consumers", key = "#id")
//    public void update(Long id, Consumer updatedConsumer) {
//        updatedConsumer.setId(id);
//        passengerRepository.save(updatedConsumer);
//    }
//
//
//    //сохраниние пассажира
    @Transactional
//    @CachePut(value = "consumers", key = "#consumer")
    public void save(Consumer consumer) {
        consumer.setPassword(passwordEncoder.encode(consumer.getPassword()));
        if (consumer.getRole().equals(null)) {
            consumer.setRole(EnumRole.ROLE_USER);
        }
        consumerRepository.save(consumer);
    }

    public Consumer findByUsername(String u) {
        return consumerRepository.findByUsername(u).orElseThrow(ConsumerNotFoundException::new);

    }
//
//    //удаление пассажира
//    @Transactional
//    @CacheEvict(value = "passengers", key = "#id")
//    public void delete(Long id) {
//        passengerRepository.deleteById(id);
//    }
//
//

}
