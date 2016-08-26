package ru.kpfu.itis.util;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class RequestUtils {

    private HttpServletRequest request;


    public RequestUtils(HttpServletRequest request) {
        this.request = request;
    }


}
