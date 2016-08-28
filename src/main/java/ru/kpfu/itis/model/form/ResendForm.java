package ru.kpfu.itis.model.form;


import ru.kpfu.itis.validator.annotation.ValidEmail;

public class ResendForm {

    @ValidEmail
    private String email;

    public ResendForm() { }

    public ResendForm(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResendForm that = (ResendForm) o;

        return email.equals(that.email);

    }

    @Override
    public int hashCode() {
        return email.hashCode();
    }
}
