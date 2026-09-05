package com.example.finance.security;
import com.example.finance.common.ApiException;
import com.example.finance.user.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
@Component public class CurrentUser{
 private final UserRepository users; public CurrentUser(UserRepository users){this.users=users;}
 public User get(){String name=SecurityContextHolder.getContext().getAuthentication().getName();return users.findByUsernameIgnoreCase(name).orElseThrow(()->new ApiException(HttpStatus.UNAUTHORIZED,"Authentication required"));}
}
