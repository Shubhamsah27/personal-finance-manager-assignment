package com.example.finance.transaction;
import com.example.finance.category.*;
import com.example.finance.common.*;
import com.example.finance.security.CurrentUser;
import com.example.finance.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.*;
@Service @Transactional(readOnly=true) public class TransactionService{
 private final TransactionRepository transactions;private final CategoryService categories;private final CurrentUser current;
 public TransactionService(TransactionRepository t,CategoryService c,CurrentUser u){transactions=t;categories=c;current=u;}
 @Transactional public Dtos.TransactionResponse create(Dtos.TransactionRequest r){User u=current.get();return dto(transactions.save(new FinanceTransaction(r.amount(),r.date(),r.description(),categories.accessible(r.category(),u),u)));}
 public List<Dtos.TransactionResponse> all(LocalDate start,LocalDate end,Long categoryId,String categoryName,CategoryType type){User u=current.get();if(start!=null&&end!=null&&start.isAfter(end))throw new ApiException(HttpStatus.BAD_REQUEST,"Start date must not be after end date");Category c=null;if(categoryId!=null){c=categories.all().stream().filter(x->x.id().equals(categoryId)).findFirst().map(x->categories.accessible(x.name(),u)).orElseThrow(()->new ApiException(HttpStatus.BAD_REQUEST,"Invalid category"));}else if(categoryName!=null&&!categoryName.isBlank()){c=categories.accessible(categoryName,u);}return transactions.filter(u,start,end,c).stream().filter(t->type==null||t.getCategory().getType()==type).map(this::dto).toList();}
 @Transactional public Dtos.TransactionResponse update(Long id,Dtos.TransactionUpdate r){User u=current.get();FinanceTransaction t=owned(id,u);Category c=r.category()==null?null:categories.accessible(r.category(),u);t.update(r.amount(),r.description(),c);return dto(t);}
 @Transactional public void delete(Long id){FinanceTransaction t=owned(id,current.get());t.delete();}
 private FinanceTransaction owned(Long id,User u){FinanceTransaction t=transactions.findByIdAndDeletedFalse(id).orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND,"Transaction not found"));if(!t.getUser().getId().equals(u.getId()))throw new ApiException(HttpStatus.FORBIDDEN,"Transaction belongs to another user");return t;}
 private Dtos.TransactionResponse dto(FinanceTransaction t){return new Dtos.TransactionResponse(t.getId(),t.getAmount(),t.getDate(),t.getCategory().getName(),t.getDescription(),t.getCategory().getType());}
}
