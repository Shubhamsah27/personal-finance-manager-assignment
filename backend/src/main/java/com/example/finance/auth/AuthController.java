package com.example.finance.auth;
import com.example.finance.common.Dtos;
import com.example.finance.user.User;
import jakarta.servlet.http.*;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.*;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController @RequestMapping("/api/auth") public class AuthController{
 private final AuthService auth; public AuthController(AuthService auth){this.auth=auth;}
 @PostMapping("/register") ResponseEntity<Map<String,Object>> register(@Valid @RequestBody Dtos.RegisterRequest r){User u=auth.register(r);return ResponseEntity.status(201).body(Map.of("message","User registered successfully","userId",u.getId()));}
 @PostMapping("/login") Map<String,String> login(@Valid @RequestBody Dtos.LoginRequest r,HttpServletRequest request){Authentication a=auth.authenticate(r);SecurityContext context=SecurityContextHolder.createEmptyContext();context.setAuthentication(a);SecurityContextHolder.setContext(context);request.getSession(true).setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,context);return Map.of("message","Login successful");}
 @PostMapping("/logout") Map<String,String> logout(HttpServletRequest request){HttpSession s=request.getSession(false);if(s!=null)s.invalidate();SecurityContextHolder.clearContext();return Map.of("message","Logout successful");}
}
