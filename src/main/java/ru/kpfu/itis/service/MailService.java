package ru.kpfu.itis.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;

@Service
public class MailService {

    private static final Logger logger = LoggerFactory.getLogger(MailService.class);

    @Autowired
    private JavaMailSender mailSender;


    public void sendMail(String subject) {

        MimeMessagePreparator preparator = mimeMessage -> {

            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress("ramilsafnab1996@gmail.com"));
            mimeMessage.setFrom(new InternetAddress("ramilsafnab1996@gmail.com"));
            mimeMessage.setText(subject);
        };

        this.mailSender.send(preparator);
    }

}
