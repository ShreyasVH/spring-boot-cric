package com.springboot.cric.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import com.springboot.cric.exceptions.MyException;
import com.springboot.cric.responses.Response;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Response> handleAllExceptions(Exception ex, WebRequest request, InputStream inputStream) throws IOException {
        System.out.println(ex.getMessage());
//        System.out.println(request.);
        return handleException("Internal Server Error occurred", 500);
    }

    @ExceptionHandler(MyException.class)
    public final ResponseEntity<Response> handleNotFoundExceptions(MyException ex, WebRequest request) {
        System.out.println(ex.getMessage());
        return handleException(ex.getDescription(), ex.getHttpStatusCode());
    }

    private ResponseEntity<Response> handleException(String message, int httpStatusCode) {
        Response errorResponse = new Response(message);
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(httpStatusCode));
    }
}
