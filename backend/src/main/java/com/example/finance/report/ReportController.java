package com.example.finance.report;
import com.example.finance.common.Dtos;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/reports") public class ReportController{
 private final ReportService service;public ReportController(ReportService s){service=s;}
 @GetMapping("/monthly/{year}/{month}") Dtos.ReportResponse monthly(@PathVariable int year,@PathVariable int month){return service.monthly(year,month);}
 @GetMapping("/yearly/{year}") Dtos.ReportResponse yearly(@PathVariable int year){return service.yearly(year);}
}
