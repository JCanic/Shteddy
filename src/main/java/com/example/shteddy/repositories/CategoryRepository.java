package com.example.shteddy.repositories;

import com.example.shteddy.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    // You can define custom methods here if needed
}
