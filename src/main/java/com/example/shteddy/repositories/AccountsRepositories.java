package com.example.shteddy.repositories;

import com.example.shteddy.account.Account;
import com.example.shteddy.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountsRepositories extends JpaRepository<Account, Integer> {
    List<Account> findByUser(User user);

    List<Account> findByUserId(Integer userId);
}
