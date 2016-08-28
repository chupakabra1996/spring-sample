package ru.kpfu.itis.service;

import freemarker.template.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import ru.kpfu.itis.model.entity.User;

import javax.mail.Message;
import java.util.Map;

@Service
public class MailService {

    @Autowired
    private Environment env;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;


    public void sendConfirmAccountMessage(Map<String, Object> model) {

        Configuration configuration = freeMarkerConfigurer.getConfiguration();

        MimeMessagePreparator preparator = mimeMessage -> {

            User user = (User) model.get("user");

            mimeMessage.setRecipients(Message.RecipientType.TO, user.getEmail());

            mimeMessage.setFrom(env.getRequiredProperty("smtp.username"));

            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);


            String content = FreeMarkerTemplateUtils.processTemplateIntoString(configuration
                    .getTemplate("accountConfirmationTempl.ftl"), model);

            helper.setText(content, true);
        };

        mailSender.send(preparator);

    }


}
