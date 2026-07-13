package ru.anastasya.readingportal.dto.validation;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import ru.anastasya.readingportal.utils.ValidationConstants;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@NotBlank(message = "Никнейм не может быть пустым")
@Size(min = ValidationConstants.USER_NICKNAME_MIN_SIZE, max = ValidationConstants.USER_NICKNAME_MAX_SIZE,
        message = "Длина никнейма не соответствует требованиям")
@Schema(description = "никнейм",
minLength = ValidationConstants.USER_NICKNAME_MIN_SIZE,
maxLength = ValidationConstants.USER_NICKNAME_MAX_SIZE,
requiredMode = Schema.RequiredMode.REQUIRED,
type = "string")
public @interface ValidUserNickname {
    String message() default "Никнейм не соответствует требованиям";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
