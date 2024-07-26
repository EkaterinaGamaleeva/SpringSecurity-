package com.SpringSecurityTest.SpringSecurityTest.repositories;

import com.SpringSecurityTest.SpringSecurityTest.models.Consumer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConsumerRepository extends JpaRepository<Consumer,Long> {
    Optional<Consumer> findByUsername(String username);
    Optional<Consumer>  findByEmail(String email);
}
