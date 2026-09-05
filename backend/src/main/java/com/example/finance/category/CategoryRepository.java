package com.example.finance.category;
import com.example.finance.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface CategoryRepository extends JpaRepository<Category,Long>{
 List<Category> findByIsDefaultTrueOrUserOrderByNameAsc(User user);
 Optional<Category> findByNormalizedNameAndIsDefaultTrue(String name);
 Optional<Category> findByNormalizedNameAndUser(String name,User user);
 boolean existsByNormalizedNameAndUser(String name,User user);
}
