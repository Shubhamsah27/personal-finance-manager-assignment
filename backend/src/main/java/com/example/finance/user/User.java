package com.example.finance.user;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "app_users")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable=false, unique=true) private String username;
    @Column(nullable=false) private String passwordHash;
    @Column(nullable=false) private String fullName;
    @Column(nullable=false) private String phoneNumber;
    @Column(nullable=false, updatable=false) private Instant createdAt = Instant.now();
    protected User() {}
    public User(String username, String passwordHash, String fullName, String phoneNumber) { this.username=username.toLowerCase(); this.passwordHash=passwordHash; this.fullName=fullName; this.phoneNumber=phoneNumber; }
    public Long getId(){return id;} public String getUsername(){return username;} public String getPasswordHash(){return passwordHash;} public String getFullName(){return fullName;} public String getPhoneNumber(){return phoneNumber;} public Instant getCreatedAt(){return createdAt;}
}
