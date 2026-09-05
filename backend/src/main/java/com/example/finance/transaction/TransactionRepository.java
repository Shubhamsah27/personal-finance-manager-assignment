package com.example.finance.transaction;
import com.example.finance.category.Category;
import com.example.finance.user.User;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
public interface TransactionRepository extends JpaRepository<FinanceTransaction,Long>{
 List<FinanceTransaction> findByUserAndDeletedFalseOrderByDateDescCreatedAtDesc(User user);
 Optional<FinanceTransaction> findByIdAndDeletedFalse(Long id);
 boolean existsByCategory(Category category);
 @Query("select t from FinanceTransaction t where t.user=:user and t.deleted=false and (:start is null or t.date>=:start) and (:end is null or t.date<=:end) and (:category is null or t.category=:category) order by t.date desc,t.createdAt desc")
 List<FinanceTransaction> filter(@Param("user") User user,@Param("start") LocalDate start,@Param("end") LocalDate end,@Param("category") Category category);
 @Query("select coalesce(sum(case when t.category.type=com.example.finance.category.CategoryType.INCOME then t.amount else -t.amount end),0) from FinanceTransaction t where t.user=:user and t.deleted=false and t.date>=:start")
 BigDecimal netSince(@Param("user") User user,@Param("start") LocalDate start);
 @Query("select t from FinanceTransaction t where t.user=:user and t.deleted=false and t.date between :start and :end")
 List<FinanceTransaction> inPeriod(@Param("user") User user,@Param("start") LocalDate start,@Param("end") LocalDate end);
}
