package com.example.finance.goal;

import com.example.finance.user.User;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.*;

@Entity
public class SavingsGoal {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false) private String goalName;
    @Column(nullable=false,precision=19,scale=2) private BigDecimal targetAmount;
    @Column(nullable=false) private LocalDate targetDate;
    @Column(nullable=false,updatable=false) private LocalDate startDate;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) private User user;
    @Column(nullable=false,updatable=false) private Instant createdAt=Instant.now();
    protected SavingsGoal(){}
    public SavingsGoal(String goalName,BigDecimal targetAmount,LocalDate targetDate,LocalDate startDate,User user){this.goalName=goalName;this.targetAmount=targetAmount;this.targetDate=targetDate;this.startDate=startDate;this.user=user;}
    public void update(BigDecimal amount,LocalDate date){if(amount!=null)targetAmount=amount;if(date!=null)targetDate=date;}
    public Long getId(){return id;} public String getGoalName(){return goalName;} public BigDecimal getTargetAmount(){return targetAmount;} public LocalDate getTargetDate(){return targetDate;} public LocalDate getStartDate(){return startDate;} public User getUser(){return user;} public Instant getCreatedAt(){return createdAt;}
}
