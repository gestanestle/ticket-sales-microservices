package com.krimo.notification.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

public interface MessageSenderService {
    void sendMessage(String recipient, String subject, String text);
}

@Service
@RequiredArgsConstructor
class EmailMessageSenderServiceImpl implements MessageSenderService {

    private final JavaMailSender mailSender;

    @Override
    public void sendMessage(String recipient, String subject, String text) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(recipient);
        mailMessage.setSubject(subject);
        mailMessage.setText(text);

        mailSender.send(mailMessage);
    }




}
