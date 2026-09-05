package com.example.finance.goal;
import com.example.finance.common.Dtos;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;
@RestController @RequestMapping("/api/goals") public class GoalController{
 private final GoalService service;public GoalController(GoalService s){service=s;}
 @PostMapping ResponseEntity<Dtos.GoalResponse> create(@Valid @RequestBody Dtos.GoalRequest r){return ResponseEntity.status(201).body(service.create(r));}
 @GetMapping Map<String,Object> all(){return Map.of("goals",service.all());}
 @GetMapping("/{id}") Dtos.GoalResponse one(@PathVariable Long id){return service.one(id);}
 @PutMapping("/{id}") Dtos.GoalResponse update(@PathVariable Long id,@Valid @RequestBody Dtos.GoalUpdate r){return service.update(id,r);}
 @DeleteMapping("/{id}") Map<String,String> delete(@PathVariable Long id){service.delete(id);return Map.of("message","Goal deleted successfully");}
}
