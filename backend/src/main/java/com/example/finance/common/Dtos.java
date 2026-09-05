package com.example.finance.common;
import com.example.finance.category.CategoryType;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
public final class Dtos{
 private Dtos(){}
 public record RegisterRequest(@NotBlank @Email String username,@NotBlank @Size(min=8,message="Password must contain at least 8 characters") String password,@NotBlank String fullName,@NotBlank @Pattern(regexp="^\\+?[0-9]{7,15}$",message="Phone number is invalid") String phoneNumber){}
 public record LoginRequest(@NotBlank @Email String username,@NotBlank String password){}
 public record TransactionRequest(@NotNull @DecimalMin(value="0.01",message="Amount must be positive") BigDecimal amount,@NotNull @PastOrPresent(message="Transaction date cannot be in the future") LocalDate date,@NotBlank String category,String description){}
 public record TransactionUpdate(@DecimalMin(value="0.01",message="Amount must be positive") BigDecimal amount,String description,String category,LocalDate date){}
 public record TransactionResponse(Long id,BigDecimal amount,LocalDate date,String category,String description,CategoryType type){}
 public record CategoryRequest(@NotBlank String name,@NotNull CategoryType type){}
 public record CategoryResponse(Long id,String name,CategoryType type,boolean isCustom,boolean custom){}
 public record GoalRequest(@NotBlank String goalName,@NotNull @DecimalMin(value="0.01",message="Target amount must be positive") BigDecimal targetAmount,@NotNull @Future(message="Target date must be in the future") LocalDate targetDate,LocalDate startDate){}
 public record GoalUpdate(@DecimalMin(value="0.01",message="Target amount must be positive") BigDecimal targetAmount,@Future(message="Target date must be in the future") LocalDate targetDate){}
 public record GoalResponse(Long id,String goalName,BigDecimal targetAmount,LocalDate targetDate,LocalDate startDate,BigDecimal currentProgress,BigDecimal progressPercentage,BigDecimal remainingAmount){}
 public record ReportResponse(Integer month,int year,Map<String,BigDecimal> totalIncome,Map<String,BigDecimal> totalExpenses,BigDecimal netSavings){}
}
