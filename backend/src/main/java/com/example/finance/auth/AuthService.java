package com.example.finance.auth;
import com.example.finance.common.*;
import com.example.finance.user.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service public class AuthService{
 private final UserRepository users; private final PasswordEncoder passwords;
 public AuthService(UserRepository users,PasswordEncoder passwords){this.users=users;this.passwords=passwords;}
 @Transactional public User register(Dtos.RegisterRequest r){if(users.existsByUsernameIgnoreCase(r.username()))throw new ApiException(HttpStatus.CONFLICT,"Email is already registered");return users.save(new User(r.username(),passwords.encode(r.password()),r.fullName(),r.phoneNumber()));}
 public Authentication authenticate(Dtos.LoginRequest r){User u=users.findByUsernameIgnoreCase(r.username()).orElseThrow(()->new ApiException(HttpStatus.UNAUTHORIZED,"Invalid credentials"));if(!passwords.matches(r.password(),u.getPasswordHash()))throw new ApiException(HttpStatus.UNAUTHORIZED,"Invalid credentials");return new UsernamePasswordAuthenticationToken(u.getUsername(),null,java.util.List.of());}
}
