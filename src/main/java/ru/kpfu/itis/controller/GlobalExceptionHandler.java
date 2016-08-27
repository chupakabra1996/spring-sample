package ru.kpfu.itis.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import ru.kpfu.itis.exception.EmailVerifyingException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {EmailVerifyingException.class})
    public ModelAndView emailVerificationExceptionHandler(Exception ex) {

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("error/serverError");

        modelAndView.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);

        modelAndView.addObject("message", ex.getMessage());

        return modelAndView;
    }



}
