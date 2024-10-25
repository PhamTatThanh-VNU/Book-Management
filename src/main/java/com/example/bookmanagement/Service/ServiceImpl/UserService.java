package com.example.bookmanagement.Service.ServiceImpl;

import com.example.bookmanagement.Model.User;
import com.example.bookmanagement.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public void save(User user) {
        User newUser = userRepository.findByUserName(user.getUsername());
        if (newUser == null) {
            newUser = new User();
        }
        updateUserDetails(newUser, user);
        userRepository.save(newUser);
    }

    public User findByUserName(String username) {
        return userRepository.findByUserName(username);
    }
    private void updateUserDetails(User existingUser, User user) {
        existingUser.setUsername(user.getUsername());
        existingUser.setPassword(user.getPassword());
        existingUser.setUrlImage(user.getUrlImage());
        existingUser.setFullName(user.getFullName());
        if (user.getBirthDate() != null) {
            existingUser.setBirthDate(user.getBirthDate());
        }
    }
}
