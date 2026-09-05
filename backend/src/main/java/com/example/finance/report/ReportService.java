package com.example.finance.report;
import com.example.finance.category.CategoryType;
import com.example.finance.common.*;
import com.example.finance.security.CurrentUser;
import com.example.finance.transaction.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
@Service @Transactional(readOnly=true) public class ReportService{
 private final TransactionRepository transactions;private final CurrentUser current;
 public ReportService(TransactionRepository t,CurrentUser c){transactions=t;current=c;}
 public Dtos.ReportResponse monthly(int year,int month){if(month<1||month>12)throw new ApiException(HttpStatus.BAD_REQUEST,"Month must be between 1 and 12");YearMonth ym=YearMonth.of(year,month);return report(month,year,ym.atDay(1),ym.atEndOfMonth());}
 public Dtos.ReportResponse yearly(int year){return report(null,year,LocalDate.of(year,1,1),LocalDate.of(year,12,31));}
 private Dtos.ReportResponse report(Integer month,int year,LocalDate start,LocalDate end){Map<String,BigDecimal> income=new TreeMap<>(),expenses=new TreeMap<>();for(FinanceTransaction t:transactions.inPeriod(current.get(),start,end)){Map<String,BigDecimal> map=t.getCategory().getType()==CategoryType.INCOME?income:expenses;map.merge(t.getCategory().getName(),t.getAmount(),BigDecimal::add);}BigDecimal i=income.values().stream().reduce(BigDecimal.ZERO,BigDecimal::add),e=expenses.values().stream().reduce(BigDecimal.ZERO,BigDecimal::add);return new Dtos.ReportResponse(month,year,income,expenses,i.subtract(e));}
}
