package com.example.demo.annotation;

import com.example.demo.component.OffsetDateTimeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = OffsetDateTimeValidator.class) // Link to the validator class
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidOffsetDateTime {
    String message() default "Invalid date & time offset";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
