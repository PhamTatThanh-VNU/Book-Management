package com.example.bookmanagement.Controller;

import com.example.bookmanagement.Model.Book;
import com.example.bookmanagement.Model.User;
import com.example.bookmanagement.Service.ServiceImpl.BookRecordService;
import com.example.bookmanagement.Service.ServiceImpl.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Data
public class OAuthController {
    private final UserService userService;
    private final BookRecordService bookRecordService;

    @RequestMapping("/login")
    public String login(Model model) {
        return "login";
    }


    @GetMapping("/user/profile")
    public String showProfile(Model model, @AuthenticationPrincipal OAuth2User oAuth2User) {
        //get  user
        String email = oAuth2User.getAttribute("email");
        User user = userService.findByUserName(email);
        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping("/user/profile/save")
    public String saveProfile(User user, RedirectAttributes redirectAttributes) throws IOException {
        try {
            userService.save(user);
            redirectAttributes.addFlashAttribute("success", "Sửa đổi thành công");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error: " + e);
        }
        return "redirect:/user/profile";
    }

    @GetMapping("/user/bookshelf")
    public String getUserBookshelf(@RequestParam Long userId, @AuthenticationPrincipal OAuth2User oAuth2User, Model model) {
        //get  user
        String email = oAuth2User.getAttribute("email");
        User user = userService.findByUserName(email);
        model.addAttribute("user", user);
        //get Book of user
        List<Book> userBooks = bookRecordService.findBookByUserId(userId);
        model.addAttribute("books", userBooks);
        return "userBookshelf";
    }

    @GetMapping("/search")
    public String searchBooks(@RequestParam String query,@AuthenticationPrincipal OAuth2User oAuth2User, Model model) {
        //get  user
        String email = oAuth2User.getAttribute("email");
        User user = userService.findByUserName(email);
        model.addAttribute("user", user);
        //get Books by query

        List<Book> books = bookRecordService.searchBooksByTitleOrIntroduction(query);
        model.addAttribute("books", books);
        return "userBookshelf";
    }

}
