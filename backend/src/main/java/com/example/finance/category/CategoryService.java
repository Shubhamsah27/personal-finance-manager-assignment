package com.example.finance.category;
import com.example.finance.common.*;
import com.example.finance.security.CurrentUser;
import com.example.finance.transaction.TransactionRepository;
import com.example.finance.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service public class CategoryService{
 private final CategoryRepository categories;private final TransactionRepository transactions;private final CurrentUser current;
 public CategoryService(CategoryRepository c,TransactionRepository t,CurrentUser u){categories=c;transactions=t;current=u;}
 public List<Dtos.CategoryResponse> all(){return categories.findByIsDefaultTrueOrUserOrderByNameAsc(current.get()).stream().map(this::dto).toList();}
 @Transactional public Dtos.CategoryResponse create(Dtos.CategoryRequest r){User u=current.get();String n=r.name().trim().toLowerCase();if(categories.findByNormalizedNameAndIsDefaultTrue(n).isPresent()||categories.existsByNormalizedNameAndUser(n,u))throw new ApiException(HttpStatus.CONFLICT,"Category name already exists");return dto(categories.save(new Category(r.name(),r.type(),false,u)));}
 @Transactional public void delete(String name){User u=current.get();String n=name.trim().toLowerCase();if(categories.findByNormalizedNameAndIsDefaultTrue(n).isPresent())throw new ApiException(HttpStatus.FORBIDDEN,"Default categories cannot be deleted");Category c=categories.findByNormalizedNameAndUser(n,u).orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND,"Category not found"));if(transactions.existsByCategory(c))throw new ApiException(HttpStatus.BAD_REQUEST,"Category is referenced by a transaction");categories.delete(c);}
 public Category accessible(String name,User u){String n=name.trim().toLowerCase();return categories.findByNormalizedNameAndIsDefaultTrue(n).or(()->categories.findByNormalizedNameAndUser(n,u)).orElseThrow(()->new ApiException(HttpStatus.BAD_REQUEST,"Invalid category"));}
 private Dtos.CategoryResponse dto(Category c){boolean custom=!c.isDefault();return new Dtos.CategoryResponse(c.getId(),c.getName(),c.getType(),custom,custom);}
}
