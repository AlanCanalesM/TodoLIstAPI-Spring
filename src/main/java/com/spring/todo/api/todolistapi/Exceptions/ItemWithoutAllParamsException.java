package com.spring.todo.api.todolistapi.Exceptions;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ItemWithoutAllParamsException extends RuntimeException {
    
    public ItemWithoutAllParamsException(String message) {
        super(message);
    }
}
