package com.example.demo.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Data
public class ResourceNotFoundException extends RuntimeException{

    private int status;
    private String message;
    private String data;

    public ResourceNotFoundException(int status, String message, String data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

}
