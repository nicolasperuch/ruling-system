package com.github.nicolasperuch.api.exception.handler;

import com.github.nicolasperuch.api.dto.RulingDto;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public abstract class ExceptionHandlerApi {

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleValidationExceptions(MethodArgumentNotValidException ex) throws NoSuchMethodException,
                                                                                        NoSuchFieldException {
        RulingDto rulingDto = new RulingDto();
        Class targetClass = rulingDto.getClass();
        Field targetField = targetClass.getDeclaredField(ex.getBindingResult().getFieldError().getField());
        NotNull notNullAnnotation = targetField.getAnnotation(NotNull.class);
        return notNullAnnotation.message();
    }
}
