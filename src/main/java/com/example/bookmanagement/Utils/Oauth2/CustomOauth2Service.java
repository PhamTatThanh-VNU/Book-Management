package com.example.bookmanagement.Utils.Oauth2;

import com.example.bookmanagement.Model.User;
import com.example.bookmanagement.Repository.UserRepository;
import com.example.bookmanagement.Service.ServiceImpl.UserService;
import lombok.Data;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Service
@Data
public class CustomOauth2Service implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserService userService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);

        String fullName = oAuth2User.getAttribute("name");
        String username = oAuth2User.getAttribute("email");
        String urlImage = oAuth2User.getAttribute("picture");


        User user = userService.findByUserName(username);
        if (user == null) {
            user = new User();
            user.setFullName(fullName);
            user.setUsername(username);
            user.setUrlImage(urlImage);
            userService.save(user);
        }
        return oAuth2User;
    }
}
