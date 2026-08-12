package com.mycompany.ProyectoAplicacionesWeb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http)
            throws Exception {

        http.authorizeHttpRequests(auth -> auth

                
                .requestMatchers(
                        "/",
                        "/login",
                        "/registro",
                        "/guardarUsuario",

                        "/css/**",
                        "/js/**",
                        "/img/**",
                        "/images/**",
                        "/webjars/**"
                )
                .permitAll()


                .requestMatchers(
                        "/categoria/**",
                        "/cliente/**"
                )
                .hasRole("ADMIN")


                .requestMatchers(
                        "/inicio",
                        "/producto/**",

                        // Carrito
                        "/carrito/**",

                        // Facturación del carrito
                        "/facturar/**",

                        "/problemas",
                        "/quienesSomos"
                )
                .hasAnyRole(
                        "ADMIN",
                        "USER"
                )

                
                .anyRequest()
                .authenticated()
        )


        .formLogin(form -> form

                .loginPage("/login")

                .loginProcessingUrl("/login")

                .defaultSuccessUrl(
                        "/inicio",
                        true
                )

                .failureUrl(
                        "/login?error=true"
                )

                .permitAll()
        )


        .logout(logout -> logout

                .logoutUrl("/logout")

                .logoutSuccessUrl("/login")

                .invalidateHttpSession(true)

                .clearAuthentication(true)

                .deleteCookies("JSESSIONID")

                .permitAll()
        )

        .exceptionHandling(exception -> exception
                .accessDeniedPage(
                        "/accesoDenegado"
                )
        );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return NoOpPasswordEncoder.getInstance();
    }

    @Autowired
    public void configurarGlobal(
            AuthenticationManagerBuilder auth,

            @Lazy
            UserDetailsService userDetailsService,

            @Lazy
            PasswordEncoder passwordEncoder)

            throws Exception {

        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }
}