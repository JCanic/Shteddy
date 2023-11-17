package com.example.shteddy.repositories;

import com.example.shteddy.account.Account;
import com.example.shteddy.transaction.Transaction;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccount_AccountID(Integer accountId);

    @Query("SELECT t FROM Transaction t WHERE t.account.accountID = :accountId  AND (t.date BETWEEN :startDate AND :endDate)")
    List<Transaction> findByAccountAndCategoryAndDateRange(
            @Param("accountId") Long accountId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

}

