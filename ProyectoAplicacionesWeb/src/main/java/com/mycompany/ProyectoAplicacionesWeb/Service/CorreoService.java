package com.mycompany.ProyectoAplicacionesWeb.Service;


import jakarta.mail.MessagingException;

import jakarta.mail.internet.MimeMessage;


import org.springframework.beans.factory.annotation.Value;

import org.springframework.mail.javamail.JavaMailSender;

import org.springframework.mail.javamail.MimeMessageHelper;

import org.springframework.stereotype.Service;


@Service
public class CorreoService {


    private final JavaMailSender mailSender;


    @Value("${spring.mail.username}")
    private String correoRemitente;


    public CorreoService(
            JavaMailSender mailSender) {

        this.mailSender =
                mailSender;

    }


    

    public void enviarCorreoHtml(

            String destinatario,

            String asunto,

            String contenido)

            throws MessagingException {


        MimeMessage mensaje =
                mailSender.createMimeMessage();


        MimeMessageHelper helper =
                new MimeMessageHelper(

                        mensaje,

                        true,

                        "UTF-8"

                );


        

        helper.setFrom(
                correoRemitente
        );




        helper.setTo(
                destinatario
        );


        

        helper.setSubject(
                asunto
        );


       

        helper.setText(
                contenido,
                true
        );




        mailSender.send(
                mensaje
        );

    }

}