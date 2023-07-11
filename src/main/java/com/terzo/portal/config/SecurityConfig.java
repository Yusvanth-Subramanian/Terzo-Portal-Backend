package com.terzo.portal.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@SuppressWarnings("removal")
public class SecurityConfig {

    RequestFilter requestFilter;

    @Autowired
    public SecurityConfig(RequestFilter requestFilter){
        this.requestFilter = requestFilter;
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
        return builder.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors();
        http.csrf().disable()
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/approve-leave").hasAnyAuthority("MANAGER","HR")
                        .requestMatchers("/delete-user").hasAnyAuthority("MANAGER","HR")
                        .requestMatchers("get-unapproved-leaves").hasAnyAuthority("MANAGER","HR")
                        .requestMatchers("/save-user").hasAnyAuthority("MANAGER","HR")
                        .requestMatchers("/get-employee/details").hasAuthority("MANAGER")
                        .requestMatchers("/update-user").hasAnyAuthority("MANAGER","HR")
                        .requestMatchers("/generate-otp").permitAll()
                        .anyRequest().authenticated()
                )
                .logout().disable()
        ;
        http.addFilterBefore(requestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}
