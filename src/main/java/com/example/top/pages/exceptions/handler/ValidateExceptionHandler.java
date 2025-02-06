package com.example.top.pages.exceptions.handler;

import com.example.top.pages.payload.response.ResponseEntityAppResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@RequiredArgsConstructor
@ControllerAdvice
public class ValidateExceptionHandler {
    private final ResponseEntityAppResponse responseEntityAppResponse;

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object handleRequestValidationException(Exception ex, HttpServletRequest request) {
        return responseEntityAppResponse.getAppResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
    }
}
