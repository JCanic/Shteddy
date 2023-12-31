package com.example.shteddy.category;

import com.example.shteddy.transaction.Transaction;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @Column(name = "category_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryID;

    private String name;
    private String description;

    public Category(String name, String description, List<Transaction> transactions) {
        this.name = name;
        this.description = description;
        this.transactions = transactions;
    }

    @OneToMany(mappedBy = "category") //
    private List<Transaction> transactions;
}
