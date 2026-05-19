package ru.anastasya.readingportal.dto.validation;

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
@NotBlank(message = "Название не может быть пустым")
@Size(min = ValidationConstants.TITLE_MIN_SIZE, max = ValidationConstants.TITLE_MAX_SIZE, message = "Некорректная длина названия")
public @interface ValidTitle {
    String message() default "Название не соответствует требованиям";
    Class<?>[] group() default {};
    Class<? extends Payload>[] payload() default {};
}
