package com.example.finance.transaction;

import com.example.finance.category.Category;
import com.example.finance.user.User;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.*;

@Entity
@Table(name="finance_transactions")
public class FinanceTransaction {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false,precision=19,scale=2) private BigDecimal amount;
    @Column(nullable=false,updatable=false) private LocalDate date;
    private String description;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) private Category category;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) private User user;
    @Column(nullable=false) private boolean deleted=false;
    @Column(nullable=false,updatable=false) private Instant createdAt=Instant.now();
    protected FinanceTransaction(){}
    public FinanceTransaction(BigDecimal amount,LocalDate date,String description,Category category,User user){this.amount=amount;this.date=date;this.description=description;this.category=category;this.user=user;}
    public void update(BigDecimal amount,String description,Category category){if(amount!=null)this.amount=amount;if(description!=null)this.description=description;if(category!=null)this.category=category;}
    public void delete(){deleted=true;}
    public Long getId(){return id;} public BigDecimal getAmount(){return amount;} public LocalDate getDate(){return date;} public String getDescription(){return description;} public Category getCategory(){return category;} public User getUser(){return user;} public boolean isDeleted(){return deleted;} public Instant getCreatedAt(){return createdAt;}
}
