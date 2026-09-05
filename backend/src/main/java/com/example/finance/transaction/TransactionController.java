package com.example.finance.transaction;
import com.example.finance.category.CategoryType;
import com.example.finance.common.Dtos;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.*;
@RestController @RequestMapping("/api/transactions") public class TransactionController{
 private final TransactionService service;public TransactionController(TransactionService s){service=s;}
 @PostMapping ResponseEntity<Dtos.TransactionResponse> create(@Valid @RequestBody Dtos.TransactionRequest r){return ResponseEntity.status(201).body(service.create(r));}
 @GetMapping Map<String,Object> all(@RequestParam(required=false)LocalDate startDate,@RequestParam(required=false)LocalDate endDate,@RequestParam(required=false)Long categoryId,@RequestParam(required=false)String category,@RequestParam(required=false)CategoryType type){return Map.of("transactions",service.all(startDate,endDate,categoryId,category,type));}
 @PutMapping("/{id}") Dtos.TransactionResponse update(@PathVariable Long id,@Valid @RequestBody Dtos.TransactionUpdate r){return service.update(id,r);}
 @DeleteMapping("/{id}") Map<String,String> delete(@PathVariable Long id){service.delete(id);return Map.of("message","Transaction deleted successfully");}
}
