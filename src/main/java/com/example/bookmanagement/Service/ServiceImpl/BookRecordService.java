package com.example.bookmanagement.Service.ServiceImpl;

import com.example.bookmanagement.Model.Book;
import com.example.bookmanagement.Model.BookRecord;
import com.example.bookmanagement.Repository.BookRecordRepository;
import com.example.bookmanagement.Repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookRecordService {
    @Autowired
    private BookRecordRepository bookRecordRepository;

    public void save(BookRecord bookRecord) {
        bookRecordRepository.save(bookRecord);
    }

    public Boolean isExistingBookRecord(Long userId, Long bookId) {
        return bookRecordRepository.isBookRecordExist(userId, bookId);
    }

    public List<Book> findBookByUserId(Long userId) {
        return bookRecordRepository.findBooksByUserId(userId);
    }
    public List<Book> searchBooksByTitleOrIntroduction(String query){
        return bookRecordRepository.searchBooksByTitleOrIntroduction(query);
    }
    public void deteteBookRecordByBookId(Long bookId) {
        bookRecordRepository.deleteBookRecordByBookId(bookId);
    }
}