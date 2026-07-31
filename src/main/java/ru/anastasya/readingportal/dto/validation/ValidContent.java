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

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@NotBlank(message = "Текст не может быть пустым")
@Size(max = ValidationConstants.CONTENT_MAX_SIZE, message = "Слишком длинный текст. Максимальная длина - 2 миллиона символов")
public @interface ValidContent {
    String message() default "Текст не соответствует требованиям";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
