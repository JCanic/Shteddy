package com.example.shteddy.repositories;

import com.example.shteddy.account.Account;
import com.example.shteddy.category.Category;
import com.example.shteddy.transaction.Transaction;
import com.example.shteddy.user.User;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Repository
public class MockAccountRepo implements AccountsRepositories {

    private final Set<Account> mockedAccounts;

    public MockAccountRepo() {
        User user1 = new User(1, "john_doe", "password", "john.doe@example.com", new ArrayList<>());
        User user2 = new User(2, "jane_doe", "password", "jane.doe@example.com", new ArrayList<>());

        Category category1 = new Category(1L, "Groceries", "Expenses for groceries", new ArrayList<>());
        Category category2 = new Category(2L, "Bills", "Monthly utility bills", new ArrayList<>());

        Account account1 = new Account(1, user1, "1234567890", new BigDecimal("1000.00"), new ArrayList<>());
        Account account2 = new Account(2, user2, "0987654321", new BigDecimal("2000.00"), new ArrayList<>());

        user1.getAccounts().add(account1);
        user2.getAccounts().add(account2);

        Transaction transaction1 = new Transaction(1L, account1, LocalDate.now(), new BigDecimal("100.00"), "debit", "Grocery shopping", category1);
        Transaction transaction2 = new Transaction(2L, account2, LocalDate.now(), new BigDecimal("200.00"), "debit", "Electricity bill", category2);

        account1.getTransactions().add(transaction1);
        account2.getTransactions().add(transaction2);

        category1.getTransactions().add(transaction1);
        category2.getTransactions().add(transaction2);

        mockedAccounts = new HashSet<>(Arrays.asList(account1, account2));
    }

    @Override
    public Set<Account> findAll() {
        return mockedAccounts;
    }

    @Override
    public List<Account> findByUser(Integer userID) {
        List<Account> userAccounts = new ArrayList<>();
        for (Account account : mockedAccounts) {
            if (Objects.equals(account.getUser().getUserID(), userID)) {
                userAccounts.add(account);
            }
        }
        return userAccounts;
    }

    @Override
    public Optional<Account> save(Account account) {
        mockedAccounts.add(account);
        return Optional.of(account);
    }

    @Override
    public Optional<Account> update(Integer accountID, Account updatedAccount) {
        for (Account account : mockedAccounts) {
            if (account.getAccountID().equals(accountID)) {
                mockedAccounts.remove(account);
                mockedAccounts.add(updatedAccount);
                return Optional.of(updatedAccount);
            }
        }
        return Optional.empty();
    }

    @Override
    public void deleteByUser(Integer userID) {
        mockedAccounts.removeIf(account -> Objects.equals(account.getUser().getUserID(), userID));
    }
}

