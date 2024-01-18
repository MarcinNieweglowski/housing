package com.marcin.housing.service;

import com.marcin.housing.configuration.MailConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private MailConfig mailConfig;

    @InjectMocks
    private EmailService emailService;

    @Test
    void sendErrorEmail_shouldCallMailSenderSendMessage_toSendTheEmail() {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setText("some text");
        mailMessage.setSubject("subject");
        mailMessage.setTo("dummyToEmail");

        when(mailConfig.to()).thenReturn(mailMessage.getTo()[0]);
        when(mailConfig.text()).thenReturn(mailMessage.getText());
        when(mailConfig.subject()).thenReturn(mailMessage.getSubject());

        emailService.sendErrorEmail();

        verify(mailSender, times(1)).send(mailMessage);
    }
}