package com.example.shopgiayonepoly.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {
    @Autowired
    private JavaMailSender javaMailSender;
    public void sendEmail(String toEmail,String subject,String body) {
        try {

            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setTo(toEmail);
            simpleMailMessage.setFrom("onepolysneaker1307@gmail.com");
            simpleMailMessage.setSubject(subject);
            simpleMailMessage.setText(body);

            javaMailSender.send(simpleMailMessage);
            System.out.println("gui mail thanh cong");
        }catch (Exception e) {
            System.out.println("gui mail that bai");
            e.printStackTrace();
        }
    }
}
