package com.example.bookmanagement.Controller;

import com.example.bookmanagement.Model.Book;
import com.example.bookmanagement.Model.Review;
import com.example.bookmanagement.Model.User;
import com.example.bookmanagement.Service.ServiceImpl.BookService;
import com.example.bookmanagement.Service.ServiceImpl.ReviewService;
import com.example.bookmanagement.Service.ServiceImpl.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    private final BookService bookService;
    private final UserService userService;

    @PostMapping("/review/addReview")
    public String addReview(
            @RequestParam String bookId,
            @RequestParam String content,
            @RequestParam int rating,
            @AuthenticationPrincipal OAuth2User oAuth2User,
            RedirectAttributes redirectAttributes
    ) {
        try {
            Review review = new Review();
            review.setContent(content);
            review.setRating(rating);
            //get User by Oauth2 User
            User user = userService.findByUserName(oAuth2User.getAttribute("email"));
            review.setUser(user);
            Book book = bookService.findBookByVolumeId(bookId);
            review.setBook(book);

            reviewService.addReview(review);
            redirectAttributes.addFlashAttribute("successMessage", "Đánh giá của bạn đã được ghi nhận!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/book/details?id=" + bookId;
    }

    @PostMapping("/review/{reviewId}/delete")
    public String deleteReview(
            @PathVariable Long reviewId,
            @RequestParam String bookId,
            RedirectAttributes redirectAttributes
    ) {
        try {
            reviewService.deleteReview(reviewId);
            redirectAttributes.addFlashAttribute("successMessage", "Đã xóa đánh giá!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/book/details?id=" + bookId;
    }
}