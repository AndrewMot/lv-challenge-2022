package com.livevox.challenge.app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        // disable X-Frame-Options for H2 console
        httpSecurity.headers().frameOptions().disable();

        // allow all requests including H2 console
        httpSecurity.authorizeRequests()
                .anyRequest().permitAll();

        // disable Cross-Site-Request-Forgery checking to allow requests from frontend webapp
        httpSecurity.csrf().disable();
    }
}
