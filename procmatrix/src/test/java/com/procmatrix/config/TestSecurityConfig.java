
package com.procmatrix.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@TestConfiguration
@Configuration
@EnableWebSecurity
public class TestSecurityConfig {

    /*@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/**", "/{id}").permitAll() // Allow unauthenticated access to /{id}
                        .anyRequest().authenticated()
                )
                .httpBasic(withDefaults()); // Use httpBasic with defaults
        return http.build();
    }
    @Bean
    public UserDetailsService testUserDetailsService() {
        return username -> null;
    }*/
}