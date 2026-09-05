package com.example.finance.goal;
import com.example.finance.common.*;
import com.example.finance.security.CurrentUser;
import com.example.finance.transaction.TransactionRepository;
import com.example.finance.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.*;
import java.time.LocalDate;
import java.util.*;
@Service @Transactional(readOnly=true) public class GoalService{
 private final GoalRepository goals;private final TransactionRepository transactions;private final CurrentUser current;
 public GoalService(GoalRepository g,TransactionRepository t,CurrentUser c){goals=g;transactions=t;current=c;}
 @Transactional public Dtos.GoalResponse create(Dtos.GoalRequest r){User u=current.get();LocalDate start=r.startDate()==null?LocalDate.now():r.startDate();if(start.isAfter(LocalDate.now()))throw new ApiException(HttpStatus.BAD_REQUEST,"Start date cannot be in the future");return dto(goals.save(new SavingsGoal(r.goalName(),r.targetAmount(),r.targetDate(),start,u)));}
 public List<Dtos.GoalResponse> all(){return goals.findByUserOrderByCreatedAtDesc(current.get()).stream().map(this::dto).toList();}
 public Dtos.GoalResponse one(Long id){return dto(owned(id,current.get()));}
 @Transactional public Dtos.GoalResponse update(Long id,Dtos.GoalUpdate r){SavingsGoal g=owned(id,current.get());g.update(r.targetAmount(),r.targetDate());return dto(g);}
 @Transactional public void delete(Long id){goals.delete(owned(id,current.get()));}
 private SavingsGoal owned(Long id,User u){SavingsGoal g=goals.findById(id).orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND,"Goal not found"));if(!g.getUser().getId().equals(u.getId()))throw new ApiException(HttpStatus.FORBIDDEN,"Goal belongs to another user");return g;}
 private Dtos.GoalResponse dto(SavingsGoal g){BigDecimal p=transactions.netSince(g.getUser(),g.getStartDate());BigDecimal pct=p.multiply(BigDecimal.valueOf(100)).divide(g.getTargetAmount(),2,RoundingMode.HALF_UP).max(BigDecimal.ZERO).min(BigDecimal.valueOf(100));pct=pct.stripTrailingZeros();if(pct.scale()<1)pct=pct.setScale(1);BigDecimal remaining=g.getTargetAmount().subtract(p).max(BigDecimal.ZERO);return new Dtos.GoalResponse(g.getId(),g.getGoalName(),g.getTargetAmount(),g.getTargetDate(),g.getStartDate(),p,pct,remaining);}
}
