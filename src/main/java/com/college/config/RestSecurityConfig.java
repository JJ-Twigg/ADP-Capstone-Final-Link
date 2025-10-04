package com.college.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class RestSecurityConfig {






    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")     //  ADMIN access only
                        .requestMatchers("/api/manager/**").hasAnyRole("MANAGER", "ADMIN") // Manager + Admin access
                        .requestMatchers("/api/user/**").hasAnyRole("USER", "MANAGER", "ADMIN") // all roles
                        .anyRequest().permitAll()
                )
                .httpBasic(basic -> {});

        return http.build();
    }




}
