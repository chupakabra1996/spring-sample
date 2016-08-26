package ru.kpfu.itis.security.registration;

import org.springframework.context.ApplicationEvent;
import org.springframework.web.context.request.WebRequest;
import ru.kpfu.itis.model.entity.User;

/**
 * /TODO documentation
 */
public class OnRegistrationCompleteEvent extends ApplicationEvent {

    private WebRequest request;

    public OnRegistrationCompleteEvent(User user, WebRequest request) {
        super(user); //user is a source
        this.request = request;
    }

    public WebRequest getRequest() {
        return request;
    }

    public void setRequest(WebRequest request) {
        this.request = request;
    }
}
