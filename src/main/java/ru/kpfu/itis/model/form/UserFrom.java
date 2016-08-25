package ru.kpfu.itis.model.form;

import org.hibernate.validator.constraints.NotBlank;
import ru.kpfu.itis.validator.annotation.PasswordMatches;
import ru.kpfu.itis.validator.annotation.ValidEmail;

import javax.validation.constraints.Size;

@PasswordMatches
public class UserFrom {

    @NotBlank
    @ValidEmail
    private String email;

    @NotBlank
    @Size(min = 6, max = 20, message = "{Size.user.password}")
    private String password;
    private String matchingPassword;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getMatchingPassword() {
        return matchingPassword;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setMatchingPassword(String matchingPassword) {
        this.matchingPassword = matchingPassword;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
