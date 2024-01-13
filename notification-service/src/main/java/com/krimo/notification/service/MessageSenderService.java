package com.krimo.notification.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

public interface MessageSenderService {
    void sendMessage(String recipient, String args);
}

@Service
@RequiredArgsConstructor
class EmailMessageSenderServiceImpl implements MessageSenderService {

    private final JavaMailSender mailSender;

    @Override
    public void sendMessage(String recipient, String arg) {
        final String sub = "Confirmation: Successful Ticket Purchase";

        final String msg =
                """
                        Congratulations! Your ticket purchase for %s is confirmed. 
                   
                        
                        Thank you for choosing to be a part of this memorable experience. We can't wait to see you there!
                        
                        """;

        send(recipient, sub, String.format(msg, arg));
    }

    protected void send(String to, String subject, String body) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(to);
        mailMessage.setSubject(subject);
        mailMessage.setText(body);

        mailSender.send(mailMessage);
    }

}
