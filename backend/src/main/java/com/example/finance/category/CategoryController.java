package com.example.finance.category;
import com.example.finance.common.Dtos;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;
@RestController @RequestMapping("/api/categories") public class CategoryController{
 private final CategoryService service;public CategoryController(CategoryService s){service=s;}
 @GetMapping Map<String,Object> all(){return Map.of("categories",service.all());}
 @PostMapping ResponseEntity<Dtos.CategoryResponse> create(@Valid @RequestBody Dtos.CategoryRequest r){return ResponseEntity.status(201).body(service.create(r));}
 @DeleteMapping("/{name}") Map<String,String> delete(@PathVariable String name){service.delete(name);return Map.of("message","Category deleted successfully");}
}
