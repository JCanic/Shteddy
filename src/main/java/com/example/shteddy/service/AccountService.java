package com.example.shteddy.service;

import com.example.shteddy.account.Account;
import com.example.shteddy.repositories.AccountsRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountService {

    private final AccountsRepositories accountsRepositories;

    @Autowired
    public AccountService(AccountsRepositories accountsRepositories) {
        this.accountsRepositories = accountsRepositories;
    }

    public BigDecimal getTotalBalanceByUserId(Integer userId) {
        List<Account> accounts = accountsRepositories.findByUserId(userId);
        return accounts.stream()
                .map(Account::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
