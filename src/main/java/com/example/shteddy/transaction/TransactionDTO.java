package com.example.shteddy.transaction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {
    private Long transactionID;
    private LocalDate date;
    private BigDecimal amount;
    private String transactionType;
    private String description;
    private String categoryName; // Add only the category name

    // Constructor, Getters and Setters
}

