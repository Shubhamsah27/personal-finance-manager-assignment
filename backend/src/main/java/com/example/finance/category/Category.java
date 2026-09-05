package com.example.finance.category;

import com.example.finance.user.User;
import jakarta.persistence.*;

@Entity
@Table(uniqueConstraints=@UniqueConstraint(columnNames={"normalized_name","user_id"}))
public class Category {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false) private String name;
    @Column(name="normalized_name",nullable=false) private String normalizedName;
    @Enumerated(EnumType.STRING) @Column(nullable=false) private CategoryType type;
    @Column(nullable=false) private boolean isDefault;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="user_id") private User user;
    protected Category() {}
    public Category(String name, CategoryType type, boolean isDefault, User user){this.name=name.trim();this.normalizedName=name.trim().toLowerCase();this.type=type;this.isDefault=isDefault;this.user=user;}
    public Long getId(){return id;} public String getName(){return name;} public String getNormalizedName(){return normalizedName;} public CategoryType getType(){return type;} public boolean isDefault(){return isDefault;} public User getUser(){return user;}
}
