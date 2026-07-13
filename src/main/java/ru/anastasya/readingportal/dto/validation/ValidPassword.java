package ru.anastasya.readingportal.dto.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import ru.anastasya.readingportal.utils.ValidationConstants;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@NotBlank(message = "Пароль не может быть пустым")
@Size(min = ValidationConstants.USER_PASSWORD_MIN_SIZE,
        message = "Длина пароля не соответствует требованиям. Минимальная длина пароля - " + ValidationConstants.USER_PASSWORD_MIN_SIZE)
public @interface ValidPassword {
    String message() default "Пароль не соответствует требованиям";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
