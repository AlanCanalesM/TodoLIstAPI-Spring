package com.spring.todo.api.todolistapi.Exceptions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.spring.todo.api.todolistapi.entity.ErrorResponse;

@RestControllerAdvice
public class ApiResponseEntityExceptionHandler extends ResponseEntityExceptionHandler{

    

    @Override
protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
        HttpStatusCode status, WebRequest request) {
           
                 //to extract the default error message from a diagnostic
                 // information about the errors held in MethodArgumentNotValidException
                 Exception exception = new Exception(ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
                 return this.createResponseEntity(HttpStatus.BAD_REQUEST, exception, request);
}
    

private ResponseEntity<Object> createResponseEntity(
            HttpStatus httpStatus, Exception ex, WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(httpStatus.value())
                .error(httpStatus.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getDescription(true))
                .build();
        return handleExceptionInternal(ex, errorResponse,
                new HttpHeaders(), httpStatus, request);
    }


    
}
