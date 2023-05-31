package com.krimo.email.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

public interface MailerService {
    void sendMaiL(String to, String subject, String body);
}
@Service
@RequiredArgsConstructor
class MailerServiceImpl implements MailerService {

    private final JavaMailSender mailSender;

    @Override
    public void sendMaiL(String to, String subject, String body) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(to);
        mailMessage.setSubject(subject);
        mailMessage.setText(body);

        mailSender.send(mailMessage);
    }


}