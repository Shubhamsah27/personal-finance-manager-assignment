package com.example.finance.common;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler{
 @ExceptionHandler(ApiException.class) ResponseEntity<Map<String,Object>> api(ApiException e,HttpServletRequest r){return response(e.getStatus(),e.getMessage(),r);}
 @ExceptionHandler(MethodArgumentNotValidException.class) ResponseEntity<Map<String,Object>> validation(MethodArgumentNotValidException e,HttpServletRequest r){String m=e.getBindingResult().getFieldErrors().stream().findFirst().map(x->x.getDefaultMessage()).orElse("Invalid request");return response(HttpStatus.BAD_REQUEST,m,r);}
 @ExceptionHandler({IllegalArgumentException.class,org.springframework.http.converter.HttpMessageNotReadableException.class}) ResponseEntity<Map<String,Object>> bad(Exception e,HttpServletRequest r){return response(HttpStatus.BAD_REQUEST,"Malformed or invalid request",r);}
 private ResponseEntity<Map<String,Object>> response(HttpStatus s,String m,HttpServletRequest r){return ResponseEntity.status(s).body(Map.of("status",s.value(),"message",m,"timestamp",Instant.now().toString(),"path",r.getRequestURI()));}
}
