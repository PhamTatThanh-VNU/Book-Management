package com.example.bookmanagement.Repository;

import com.example.bookmanagement.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT us FROM User us WHERE us.username = :username")
    User findByUserName(String username);
}

