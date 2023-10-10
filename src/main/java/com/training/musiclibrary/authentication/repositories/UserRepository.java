package com.training.musiclibrary.authentication.repositories;

import com.training.musiclibrary.authentication.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.deleted = FALSE")
    List<User> findAllActiveUsers();
}
