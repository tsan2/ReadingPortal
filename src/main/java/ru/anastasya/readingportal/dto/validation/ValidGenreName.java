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
@NotBlank(message = "Название жанра не может быть пустым")
@Size(min= ValidationConstants.GENRE_NAME_MIN_SIZE, max=ValidationConstants.GENRE_NAME_MAX_SIZE, message = "Длина названия не корректна")
public @interface ValidGenreName {
    String message() default "Название жанра не соответствует требованиям";
    Class<?>[] group() default {};
    Class<? extends Payload>[] payload() default {};
}
