package com.SpringSecurityTest.SpringSecurityTest.config;

import com.SpringSecurityTest.SpringSecurityTest.repositories.ConsumerRepository;
import com.SpringSecurityTest.SpringSecurityTest.sucurity.ConsumerDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableJpaRepositories(basePackageClasses = ConsumerRepository.class)
public class SecurityConfig {
    private final ConsumerDetailsService consumerDetailsService;
    private final JWTFilter jwtFilter;


    @Autowired
    public SecurityConfig(ConsumerDetailsService consumerDetailsService, JWTFilter jwtFilter) {
        this.consumerDetailsService = consumerDetailsService;
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public AuthenticationManager authManager() {

        var authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(consumerDetailsService);
        authProvider.setPasswordEncoder(getPasswordEncoder());
        authProvider.setPasswordEncoder(getPasswordEncoder());
        return new ProviderManager(authProvider);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf((csrf) -> csrf
                        .disable()
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/consumers/{id}").hasRole("ADMIN")
                        .requestMatchers("/login", "/registration", "/findAll", "/error", "/consumers").permitAll()
                        .anyRequest().hasAnyRole("USER", "ADMIN")
                )
//                .formLogin((form) -> form
//                                .loginPage("/login")
//                                .defaultSuccessUrl("/hello")
//                                .failureUrl("/login?error")
////                        .failureForwardUrl("/error")
//                                //возврат полноценного представлени
//                                .usernameParameter("username").passwordParameter("password")
//                                .permitAll()
//                )
//                .logout((logout) -> logout.logoutUrl("/logout")
//                        .logoutSuccessUrl("/login").permitAll())
                .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS));
//                ).sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }
}
