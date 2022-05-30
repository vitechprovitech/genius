package com.faguy.genius.service;

import javax.mail.MessagingException;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.faguy.genius.model.DTO.DataMailDTO;

@Service
public class EmailService {

    private final TemplateEngine templateEngine;

    private final JavaMailSender javaMailSender;

    public EmailService(TemplateEngine templateEngine, JavaMailSender javaMailSender) {
        this.templateEngine = templateEngine;
        this.javaMailSender = javaMailSender;
    }

    public String sendMail(DataMailDTO message, String to, String subject) throws MessagingException {
        Context context = new Context();
        context.setVariable("data", message);
        String process = templateEngine.process("mailTemplate", context);
        javax.mail.internet.MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setSubject("PRODUCT KEY - GENIUS HOME" );
        helper.setText(process, true);
        helper.setTo(to);
        javaMailSender.send(mimeMessage);
        return "Sent";
    }
}