package com.example.shteddy.repositories;

import com.example.shteddy.account.Account;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface AccountsRepositories {

        Set<Account> findAll();

        List<Account> findByUser(Integer userID); // returns a List of Accounts

        Optional<Account> save(Account account);

        Optional<Account> update(Integer accountID, Account updatedAccount);

        void deleteByUser(Integer userID); // deletes all accounts associated with a User
}

