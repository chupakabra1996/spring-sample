package ru.kpfu.itis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;


    public void sendMail(InternetAddress from, InternetAddress to, String subject) throws MailException {

        MimeMessagePreparator mimeMessagePreparator = mimeMessage -> {

            mimeMessage.setRecipient(Message.RecipientType.TO, to);
            mimeMessage.setFrom(from);
            mimeMessage.setText(subject);
        };

        this.mailSender.send(mimeMessagePreparator);
    }

}
