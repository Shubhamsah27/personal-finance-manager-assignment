package com.example.finance.security;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import java.time.Instant;
import java.util.Map;

@Configuration
public class SecurityConfig{
 @Bean PasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder();}
 @Bean SecurityFilterChain security(HttpSecurity http,ObjectMapper mapper)throws Exception{
  http.csrf(c->c.disable()).cors(c->{}).authorizeHttpRequests(a->a
   .requestMatchers("/", "/index.html", "/assets/**", "/favicon.ico", "/api/auth/register", "/api/auth/login", "/api/health").permitAll()
   .anyRequest().authenticated())
   .exceptionHandling(e->e.authenticationEntryPoint((req,res,x)->write(mapper,res,401,"Authentication required",req.getRequestURI())))
   .logout(l->l.disable());
  return http.build();
 }
 private static void write(ObjectMapper mapper,HttpServletResponse res,int status,String message,String path)throws java.io.IOException{res.setStatus(status);res.setContentType(MediaType.APPLICATION_JSON_VALUE);mapper.writeValue(res.getOutputStream(),Map.of("status",status,"message",message,"timestamp",Instant.now().toString(),"path",path));}
}
