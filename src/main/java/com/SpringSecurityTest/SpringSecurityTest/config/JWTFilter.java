package com.SpringSecurityTest.SpringSecurityTest.config;

import com.SpringSecurityTest.SpringSecurityTest.services.ConsumerService;
import com.SpringSecurityTest.SpringSecurityTest.sucurity.ConsumerDetails;
import com.SpringSecurityTest.SpringSecurityTest.sucurity.ConsumerDetailsService;
import com.SpringSecurityTest.SpringSecurityTest.sucurity.JwtUtil;
import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.configurers.SecurityContextConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final ConsumerDetailsService
            consumerDetailsService;


    @Autowired
    public JWTFilter(JwtUtil jwtUtil, ConsumerDetailsService consumerDetailsService) {
        this.jwtUtil = jwtUtil;
        this.consumerDetailsService = consumerDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String autnHandler = request.getHeader("Authorization");
        if (autnHandler != null && !autnHandler.isBlank() && autnHandler.startsWith("Bearer ")) {
            String token = autnHandler.substring(7);
            if (token.isBlank()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "НЕТ ТОКЕНА");
            } else {
                try {
                    String username = jwtUtil.validateToken(token);
                    ConsumerDetails consumerDetails = consumerDetailsService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(consumerDetails, consumerDetails.getPassword(), consumerDetails.getAuthorities());
                    if (SecurityContextHolder.getContext().getAuthentication() == null) {
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                } catch (JWTVerificationException e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Токен не прошел аутентификацию");
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
