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


    // =========================================================
    // CONFIGURACIÓN DE SEGURIDAD Y RUTAS
    // =========================================================

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http)
            throws Exception {


        http.authorizeHttpRequests(

                auth -> auth


                        // =================================================
                        // RUTAS PÚBLICAS
                        // =================================================

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


                        // =================================================
                        // RUTAS EXCLUSIVAS DEL ADMINISTRADOR
                        // =================================================

                        .requestMatchers(

                                // CATEGORÍAS

                                "/categoria/**",

                                // CLIENTES

                                "/cliente/**",

                                // ADMINISTRACIÓN DE PRODUCTOS

                                "/admin/**",

                                // SOLICITUDES DE SOPORTE

                                "/solicitud/**",

                                // ADMINISTRACIÓN DE TÉCNICOS

                                "/tecnico/nuevo",

                                "/tecnico/guardar",

                                "/tecnico/modificar/**",

                                "/tecnico/eliminar/**"

                        )

                        .hasRole(
                                "ADMIN"
                        )


                        // =================================================
                        // RUTAS PARA ADMIN Y USER
                        // =================================================

                        .requestMatchers(

                                // INICIO

                                "/inicio",

                                // PRODUCTOS

                                "/producto/**",

                                // CARRITO

                                "/carrito/**",

                                // FACTURACIÓN

                                "/facturar/**",

                                // PROBLEMAS COMUNES

                                "/problemas",

                                // QUIÉNES SOMOS

                                "/quienesSomos",

                                // TÉCNICOS

                                "/tecnico/listado",

                                "/tecnico/contactar/**",

                                "/tecnico/enviarCorreo"

                        )

                        .hasAnyRole(

                                "ADMIN",

                                "USER"

                        )


                        // =================================================
                        // CUALQUIER OTRA RUTA REQUIERE AUTENTICACIÓN
                        // =================================================

                        .anyRequest()

                        .authenticated()

        );


        // =========================================================
        // CONFIGURACIÓN DEL LOGIN
        // =========================================================

        http.formLogin(

                form -> form


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

        );


        // =========================================================
        // CONFIGURACIÓN DEL LOGOUT
        // =========================================================

        http.logout(

                logout -> logout


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

        );


        // =========================================================
        // ACCESO DENEGADO
        // =========================================================

        http.exceptionHandling(

                exception -> exception

                        .accessDeniedPage(
                                "/accesoDenegado"
                        )

        );


        return http.build();

    }


    // =========================================================
    // PASSWORD ENCODER
    // =========================================================
    //
    // Se mantiene NoOpPasswordEncoder por ahora porque
    // los usuarios actuales tienen las contraseñas
    // almacenadas en texto plano.
    //
    // =========================================================

    @Bean
    public PasswordEncoder passwordEncoder() {

        return NoOpPasswordEncoder
                .getInstance();

    }


    // =========================================================
    // CONFIGURACIÓN GLOBAL DE AUTENTICACIÓN
    // =========================================================

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