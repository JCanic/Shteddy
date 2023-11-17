package com.example.shteddy.account;

import com.example.shteddy.transaction.Transaction;
import com.example.shteddy.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Integer accountID;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String accountNumber;
    private BigDecimal balance;

    @OneToMany(mappedBy = "account")
    @JsonIgnore
    private List<Transaction> transactions;

    @Override
    public int hashCode() {
        return Objects.hash(accountID, accountNumber, balance);
    }
}
