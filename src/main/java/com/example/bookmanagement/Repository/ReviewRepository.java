package com.example.bookmanagement.Repository;

import com.example.bookmanagement.Model.Book;
import com.example.bookmanagement.Model.Review;
import com.example.bookmanagement.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Long> {
    @Query("SELECT r FROM Review r WHERE r.book.volumeId = :bookId ORDER BY r.id DESC")
    List<Review> findByBookIdOrderByIdDesc(String bookId);
    @Query("SELECT r FROM Review r WHERE r.user.id = :userId AND r.book.volumeId = :bookId")
    List<Review> findByUserIdAndBookId(Long userId, String bookId);
}
