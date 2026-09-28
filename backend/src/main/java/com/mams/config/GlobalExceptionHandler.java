package com.mams.config;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    ResponseEntity<?> runtime(RuntimeException e){return ResponseEntity.badRequest().body(Map.of("message",e.getMessage()==null?"Request failed":e.getMessage()));}
    @ExceptionHandler(Exception.class)
    ResponseEntity<?> general(Exception e){return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message","Server error")); }
}
