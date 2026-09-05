package com.example.finance.goal;
import com.example.finance.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface GoalRepository extends JpaRepository<SavingsGoal,Long>{ List<SavingsGoal> findByUserOrderByCreatedAtDesc(User user); }
