package com.example.bookmanagement.Repository;

import com.example.bookmanagement.Model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT ct FROM Category ct where ct.name = :name")
    Category findByName(String name);
}
