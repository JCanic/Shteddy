package com.example.shteddy.user;

import com.example.shteddy.account.Account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "USERS")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userID;

    private String username;
    private String password;
    private String email;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Account> accounts;
}
