package ru.kpfu.itis.validator;

import ru.kpfu.itis.model.form.UserFrom;
import ru.kpfu.itis.validator.annotation.PasswordMatches;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, UserFrom> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {

    }

    @Override
    public boolean isValid(UserFrom value, ConstraintValidatorContext context) {

        return value.getPassword().equals(value.getMatchingPassword());
    }

}


