package com.example.bookmanagement.Repository;

import com.example.bookmanagement.Model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("SELECT b FROM Book b WHERE b.volumeId = :volumeId")
    Book findByVoulmeId(@Param("volumeId") String volumeId);

}
