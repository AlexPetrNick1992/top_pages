package com.example.top.pages.repository;

import com.example.top.pages.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    @Query(value = "select * from users where username = :username", nativeQuery = true)
    Optional<User> findUserByUsername(String username);

    @Query(value = "select * from users where email = :email", nativeQuery = true)
    Optional<User> findUserByEmail(String email);


}
