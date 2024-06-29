package com.example.demo.exception;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@Data
public class ErrorResponse {
    private int status;
    private String message;
    private Object data;
    private ErrorResponseInfo additionalInfo;

}