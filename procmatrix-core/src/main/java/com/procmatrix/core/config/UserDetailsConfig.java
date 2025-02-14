package com.procmatrix.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UserDetailsConfig {

    @Value("${user.details.creator.username}")
    private String creatorUsername;

    @Value("${user.details.creator.password}")
    private String creatorPassword;

    @Value("${user.details.creator.roles}")
    private String[] creatorRoles;

    @Value("${user.details.reader.username}")
    private String readerUsername;

    @Value("${user.details.reader.password}")
    private String readerPassword;

    @Value("${user.details.reader.roles}")
    private String[] readerRoles;

    @Value("${user.details.operator.username}")
    private String operatorUsername;

    @Value("${user.details.operator.password}")
    private String operatorPassword;

    @Value("${user.details.operator.roles}")
    private String[] operatorRoles;

    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername(creatorUsername)
                .password(passwordEncoder().encode(creatorPassword))
                .roles(creatorRoles)
                .build());
        manager.createUser(User.withUsername(readerUsername)
                .password(passwordEncoder().encode(readerPassword))
                .roles(readerRoles)
                .build());
        manager.createUser(User.withUsername(operatorUsername)
                .password(passwordEncoder().encode(operatorPassword))
                .roles(operatorRoles)
                .build());
        return manager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
