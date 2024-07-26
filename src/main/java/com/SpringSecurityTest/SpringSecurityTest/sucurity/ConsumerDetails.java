package com.SpringSecurityTest.SpringSecurityTest.sucurity;

import com.SpringSecurityTest.SpringSecurityTest.models.Consumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

public class ConsumerDetails  implements UserDetails {
private final Consumer consumer;

    public ConsumerDetails(Consumer consumer) {
        this.consumer = consumer;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(consumer.getRole().toString()));
    }

    @Override
    public String getPassword() {
        return consumer.getPassword();
    }

    @Override
    public String getUsername() {
        return consumer.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
public Consumer getConsumer(){
    return this.consumer;
}
}
