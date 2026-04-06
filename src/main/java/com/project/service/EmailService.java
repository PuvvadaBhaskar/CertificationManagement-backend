package com.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Async
    public void sendEmail(String to, String subject, String body) {

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("mr.bhaskar090@gmail.com");  // ✅ IMPORTANT
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);

            mailSender.send(message);

            System.out.println("✅ Mail sent to: " + to);

        } catch (Exception e) {
            System.out.println("❌ Mail failed");
            e.printStackTrace();
        }
    }
}