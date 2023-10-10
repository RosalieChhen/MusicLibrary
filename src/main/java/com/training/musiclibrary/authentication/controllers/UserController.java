package com.training.musiclibrary.authentication.controllers;

import com.training.musiclibrary.authentication.DTO.UserSearchFilterRequest;
import com.training.musiclibrary.authentication.models.User;
import com.training.musiclibrary.authentication.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping()
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return new ResponseEntity<>(userService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsers(@RequestBody UserSearchFilterRequest userSearchFilterRequest) {
        return new ResponseEntity<>(userService.searchUser(userSearchFilterRequest), HttpStatus.OK);
    }

    @DeleteMapping("{id}/delete")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
