package com.example.bookmanagement.Repository;

import com.example.bookmanagement.Model.Book;
import com.example.bookmanagement.Model.BookRecord;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRecordRepository extends JpaRepository<BookRecord, Long> {
    @Query("SELECT CASE WHEN COUNT(br) > 0 THEN true ELSE false END FROM BookRecord br WHERE br.user.id = :userId and br.book.id= :bookId")
    Boolean isBookRecordExist(@Param("userId") Long userId, @Param("bookId") Long bookId);

    @Query("SELECT br.book FROM BookRecord br WHERE br.user.id = :userId")
    List<Book> findBooksByUserId(@Param("userId") Long userId);

    @Query("SELECT br.book FROM BookRecord br WHERE LOWER(br.book.title) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Book> searchBooksByTitleOrIntroduction(@Param("query") String query);

    @Modifying
    @Transactional
    @Query("DELETE FROM BookRecord br WHERE br.book.id = :bookId")
    void deleteBookRecordByBookId(@Param("bookId") Long bookId);
}
