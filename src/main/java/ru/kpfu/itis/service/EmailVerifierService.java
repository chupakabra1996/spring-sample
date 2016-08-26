package ru.kpfu.itis.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.exception.EmailVerifyingException;

import java.io.IOException;

@Service
public class EmailVerifierService {

    private static final Logger logger = LoggerFactory.getLogger(EmailVerifierService.class);

    private HttpClient httpClient;

    @Autowired
    private Environment env;


    public EmailVerifierService() {
        this.httpClient = HttpClientBuilder.create().build();
    }

    public boolean check(String emailAddress) {

        String json = getResponse(emailAddress);

        logger.error("[json response = `" + json + "`]");

        try {

            JsonNode rootNode = new ObjectMapper().readTree(json);

            JsonNode isValid = rootNode.get("smtp_check");

            return isValid.asBoolean();

        } catch (IOException e) {

            logger.error("[Cant't parse response json]", e);
            throw new EmailVerifyingException("Response json is not valid");
        }

    }


    private String getUrl(String emailAddress) {

        return new StringBuilder(env.getRequiredProperty("api.url"))
                .append("?").append(env.getRequiredProperty("api.access_param"))
                .append("=").append(env.getRequiredProperty("api.access_key"))
                .append("&").append(env.getRequiredProperty("api.email_param"))
                .append("=").append(emailAddress).toString();
    }


    private String getResponse(String emailAddress) {

        try {

            logger.error("[Try to verify email ...]");

            HttpGet get = new HttpGet(getUrl(emailAddress));

            HttpResponse response = httpClient.execute(get);

            return EntityUtils.toString(response.getEntity());

        } catch (IOException e) {

            logger.error("[Email verifying exception]", e);
            throw new EmailVerifyingException("Email api service is not available now!");
        }
    }

}
