package com.example.bookmanagement.Service.ServiceImpl;

import com.example.bookmanagement.Model.Book;
import com.example.bookmanagement.Repository.BookRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    public void save(Book book) {
        Book existedBook = bookRepository.findByVoulmeId(book.getVolumeId());
        if (existedBook == null) {
            bookRepository.save(book);
        }
    }

    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    public Book findBookByVolumeId(String volumeId) {
        return bookRepository.findByVoulmeId(volumeId);
    }
}
