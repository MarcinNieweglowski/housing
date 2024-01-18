package com.marcin.housing.service;

import com.marcin.housing.configuration.MailConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmailService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    private final JavaMailSender mailSender;
    private final MailConfig mailConfig;

    public void sendErrorEmail() {
        log.info("Sending email message - failed to download and store housing data");

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(mailConfig.to());
        mailMessage.setText(mailConfig.text().formatted(LocalDateTime.now().format(FORMATTER)));
        mailMessage.setSubject(mailConfig.subject());

        mailSender.send(mailMessage);
    }
}
