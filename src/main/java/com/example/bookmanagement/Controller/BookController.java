package com.example.bookmanagement.Controller;

import com.example.bookmanagement.DTO.BookDTO;
import com.example.bookmanagement.Model.*;
import com.example.bookmanagement.Service.ServiceImpl.*;
import lombok.Data;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@Data
public class BookController {
    private final GoogleResponseService googleResponseService;
    private final BookRecordService bookRecordService;
    private final BookService bookService;
    private final UserService userService;
    private final CategoryService categoryService;
    private final ReviewService reviewService;

    @GetMapping("/")
    public String getBooks(@RequestParam(value = "query", required = false, defaultValue = "best seller") String query,
                           @RequestParam(value = "category", required = false) String category, Model model,
                           @AuthenticationPrincipal OAuth2User userOauth2) {
        //get  user
        String email = userOauth2.getAttribute("email");
        User user = userService.findByUserName(email);
        model.addAttribute("user", user);
        //get books
        List<BookDTO> books = googleResponseService.getBooks(query, category); // Call service to get books
        model.addAttribute("books", books);
        model.addAttribute("query", query);
        return "index";
    }

    @GetMapping("/book/details")
    public String getBookDetails(@RequestParam("id") String bookId, Model model, @AuthenticationPrincipal OAuth2User userOauth2) {
        //get  user
        String email = userOauth2.getAttribute("email");
        model.addAttribute("email", email);
        User user = userService.findByUserName(email);
        model.addAttribute("user", user);
        //Check review
        Book existingBook = bookService.findBookByVolumeId(bookId);
        if (existingBook != null) {
            Boolean canReview = bookRecordService.isExistingBookRecord(user.getId(), existingBook.getId());
            model.addAttribute("canReview", canReview);
        } else {
            model.addAttribute("canReview", false);
        }
        //Review
        List<Review> reviews = reviewService.getBookReviews(bookId);
        model.addAttribute("reviews", reviews);

        //get book details
        BookDTO bookDetails = googleResponseService.getBookDetails(bookId);
//        System.out.println(bookDetails.getVolumeInfo().getCategories());
        model.addAttribute("book", bookDetails);
        return "bookdetails";
    }

    @PostMapping("/book/add")
    public String addBookToShelf(@RequestParam("bookId") String bookId,
                                 @AuthenticationPrincipal OAuth2User userOauth2,
                                 RedirectAttributes redirectAttributes
    ) {
        String bookAuthors;
        BookDTO bookDTO = googleResponseService.getBookDetails(bookId);

        if (bookDTO.getVolumeInfo().getAuthors() != null) {
            bookAuthors = bookDTO.getVolumeInfo().getAuthors().get(0);
        } else {
            bookAuthors = null;
        }
        //Find user by email
        String email = userOauth2.getAttribute("email");
        User user = userService.findByUserName(email);

        Book existingBook = bookService.findBookByVolumeId(bookId);
        if (existingBook == null) {
            //Save book to local
            existingBook = new Book();
            existingBook.setTitle(bookDTO.getVolumeInfo().getTitle() != null ? bookDTO.getVolumeInfo().getTitle() : "Không có tiêu đề");
            existingBook.setAuthor(bookAuthors);
            existingBook.setIntroduction(bookDTO.getVolumeInfo().getDescription() != null ? bookDTO.getVolumeInfo().getDescription() : "Không có mô tả");
            existingBook.setUrl(bookDTO.getVolumeInfo().getImageLinks() != null && bookDTO.getVolumeInfo().getImageLinks().getThumbnail() != null
                    ? bookDTO.getVolumeInfo().getImageLinks().getThumbnail()
                    : "/img/default.png");
            existingBook.setPreviewLink(bookDTO.getVolumeInfo().getPreviewLink() != null ? bookDTO.getVolumeInfo().getPreviewLink() + "&printsec=frontcover" : "Không có link xem trước");
            existingBook.setVolumeId(bookId);
            //Set Category
            if (bookDTO.getVolumeInfo().getCategories() != null) {
                for (String categoryName : bookDTO.getVolumeInfo().getCategories()) {
                    Category category = categoryService.findByName(categoryName);
                    if (category == null) {
                        category = new Category();
                        category.setName(categoryName);
                        categoryService.save(category);
                    }

                    existingBook.setCategory(category);
                }
            }
            bookService.save(existingBook);
        }

        Boolean isExistingBookRecord = bookRecordService.isExistingBookRecord(user.getId(), existingBook.getId());
//        System.out.println(isExistingBookRecord);
        if (!isExistingBookRecord) {
            //Save book Record
            BookRecord bookRecord = new BookRecord();
            bookRecord.setUser(user);
            bookRecord.setBook(existingBook);
            bookRecordService.save(bookRecord); // Lưu BookRecord
            redirectAttributes.addFlashAttribute("successMessage", "Cuốn sách đã được thêm vào kệ sách của bạn");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Cuốn sách này đã được thêm, bạn chỉ được thêm 1 lần ");
        }

        redirectAttributes.addAttribute("id", bookId);
        return "redirect:/book/details";
    }

    @PostMapping("/delete/{bookId}")
    public String deleteBook(@AuthenticationPrincipal OAuth2User oAuth2User, @PathVariable Long bookId) {
        User user = userService.findByUserName(oAuth2User.getAttribute("email"));
        Long userId = user.getId();
        bookRecordService.deteteBookRecordByBookId(bookId);
        return "redirect:/user/bookshelf?userId=" + userId;
    }

}
