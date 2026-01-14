package com.example.demo.component;

import com.example.demo.annotation.ValidOffsetDateTime;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.OffsetDateTime;

public class OffsetDateTimeValidator implements ConstraintValidator<ValidOffsetDateTime, String> {
    @Override
    public boolean isValid(
            String offsetDateTimeString,
            ConstraintValidatorContext constraintValidatorContext
    ) {
        boolean isValid = true;

        try {
             OffsetDateTime parsedDate = OffsetDateTime.parse(offsetDateTimeString);

             if (parsedDate.isBefore(OffsetDateTime.now())) {
                 isValid = false;
             }
        } catch (Exception e) {
            isValid = false;
        }
        return isValid;
    }
}
