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

                // ==========================================
                // RUTAS PÚBLICAS
                // ==========================================

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


                // ==========================================
                // SOLO ADMINISTRADOR
                // ==========================================

                .requestMatchers(
                        "/categoria/**",
                        "/cliente/**",
                        "/admin/**",

                        // Administración de técnicos

                        "/tecnico/nuevo",
                        "/tecnico/guardar",
                        "/tecnico/modificar/**",
                        "/tecnico/eliminar/**"
                )
                .hasRole("ADMIN")


                // ==========================================
                // ADMINISTRADOR Y USUARIO
                // ==========================================

                .requestMatchers(
                        "/inicio",

                        "/producto/**",

                        // Carrito

                        "/carrito/**",

                        // Facturación

                        "/facturar/**",

                        "/problemas",

                        "/quienesSomos",

                        // ==================================
                        // TÉCNICOS
                        // ==================================

                        "/tecnico/listado",

                        "/tecnico/contactar/**",

                        "/tecnico/enviarCorreo"
                )
                .hasAnyRole(
                        "ADMIN",
                        "USER"
                )


                // ==========================================
                // CUALQUIER OTRA RUTA
                // ==========================================

                .anyRequest()
                .authenticated()
        )


        // ==========================================
        // LOGIN
        // ==========================================

        .formLogin(form -> form

                .loginPage(
                        "/login"
                )

                .loginProcessingUrl(
                        "/login"
                )

                .defaultSuccessUrl(
                        "/inicio",
                        true
                )

                .failureUrl(
                        "/login?error=true"
                )

                .permitAll()
        )


        // ==========================================
        // LOGOUT
        // ==========================================

        .logout(logout -> logout

                .logoutUrl(
                        "/logout"
                )

                .logoutSuccessUrl(
                        "/login"
                )

                .invalidateHttpSession(
                        true
                )

                .clearAuthentication(
                        true
                )

                .deleteCookies(
                        "JSESSIONID"
                )

                .permitAll()
        )


        // ==========================================
        // ACCESO DENEGADO
        // ==========================================

        .exceptionHandling(exception -> exception

                .accessDeniedPage(
                        "/accesoDenegado"
                )

        );

        return http.build();
    }


    // ==========================================
    // PASSWORD ENCODER
    // ==========================================

    @Bean
    public PasswordEncoder passwordEncoder() {

        return NoOpPasswordEncoder.getInstance();
    }


    // ==========================================
    // CONFIGURACIÓN DE AUTENTICACIÓN
    // ==========================================

    @Autowired
    public void configurarGlobal(

            AuthenticationManagerBuilder auth,

            @Lazy
            UserDetailsService userDetailsService,

            @Lazy
            PasswordEncoder passwordEncoder)

            throws Exception {

        auth
                .userDetailsService(
                        userDetailsService
                )
                .passwordEncoder(
                        passwordEncoder
                );
    }

}