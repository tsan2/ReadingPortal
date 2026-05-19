package ru.anastasya.readingportal.dto.validation;

import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import ru.anastasya.readingportal.utils.ValidationConstants;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@NotBlank(message = "Пароль не может быть пустым")
@Size(min = ValidationConstants.USER_PASSWORD_MIN_SIZE, message = "Длина пароля не соответствует требованиям")
public @interface ValidPassword {
    String message() default "Пароль не соответствует требованиям";
    Class<?>[] group() default {};
    Class<? extends Payload>[] payload() default {};

}
