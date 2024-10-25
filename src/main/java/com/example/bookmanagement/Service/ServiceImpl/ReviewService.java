package com.example.bookmanagement.Service.ServiceImpl;

import com.example.bookmanagement.Model.Review;
import com.example.bookmanagement.Repository.BookRecordRepository;
import com.example.bookmanagement.Repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final BookRecordRepository bookRecordRepository;

    public Review addReview(Review review) {

        // Validate rating
        if (review.getRating() < 1 || review.getRating() > 5) {
            throw new RuntimeException("Rating phải từ 1-5 sao!");
        }

        return reviewRepository.save(review);
    }

    public List<Review> getBookReviews(String bookId) {
        return reviewRepository.findByBookIdOrderByIdDesc(bookId);
    }
    public void deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow();
        reviewRepository.delete(review);
    }
}