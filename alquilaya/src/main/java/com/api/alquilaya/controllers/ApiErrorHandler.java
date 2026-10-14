package com.api.alquilaya.controllers;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
@RestControllerAdvice
public class ApiErrorHandler {
 @ExceptionHandler(IllegalArgumentException.class)
 @ResponseStatus(HttpStatus.BAD_REQUEST)
 public Map<String,String> bad(IllegalArgumentException e){return Map.of("error",e.getMessage());}
}
