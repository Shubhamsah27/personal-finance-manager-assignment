package com.example.finance.config;
import com.example.finance.category.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.*;
import org.springframework.web.cors.*;
import java.util.List;
@Configuration public class ApplicationConfig{
 @Bean CommandLineRunner defaults(CategoryRepository repo){return args->{seed(repo,"Salary",CategoryType.INCOME);for(String n:List.of("Food","Rent","Transportation","Entertainment","Healthcare","Utilities"))seed(repo,n,CategoryType.EXPENSE);};}
 private void seed(CategoryRepository r,String n,CategoryType t){if(r.findByNormalizedNameAndIsDefaultTrue(n.toLowerCase()).isEmpty())r.save(new Category(n,t,true,null));}
 @Bean CorsConfigurationSource cors(){CorsConfiguration c=new CorsConfiguration();c.setAllowedOrigins(List.of("http://localhost:5173","http://localhost:4173"));c.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));c.setAllowedHeaders(List.of("Content-Type"));c.setAllowCredentials(true);UrlBasedCorsConfigurationSource s=new UrlBasedCorsConfigurationSource();s.registerCorsConfiguration("/api/**",c);return s;}
}
