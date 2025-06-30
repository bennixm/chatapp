package com.example.chatapp.Config;

import com.example.chatapp.Service.MyUserDetailsService;
import com.example.chatapp.Util.JwtAuthFilter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final MyUserDetailsService myUserDetailsService;
    private final CorsFilter corsFilter;
    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(MyUserDetailsService myUserDetailsService,
                          CorsFilter corsFilter,
                          JwtAuthFilter jwtAuthFilter) {
        this.myUserDetailsService = myUserDetailsService;
        this.corsFilter = corsFilter;
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authBuilder
                .userDetailsService(myUserDetailsService)
                .passwordEncoder(passwordEncoder());
        return authBuilder.build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf().disable()
                .cors().and()
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(
                                "/api/v1/user/login",
                                "/api/v1/user/save",
                                "/api/v1/user/logout"
                        ).permitAll()

                        .anyRequest().authenticated()
                );

        return http.build();
    }
}
