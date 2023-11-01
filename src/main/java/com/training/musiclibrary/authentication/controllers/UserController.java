package com.training.musiclibrary.authentication.controllers;

import com.training.musiclibrary.authentication.DTO.UserSearchFilterRequest;
import com.training.musiclibrary.authentication.models.User;
import com.training.musiclibrary.authentication.services.UserService;
import com.training.musiclibrary.utils.constants.Endpoints;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(Endpoints.USERS)
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "Get the list of all users")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of all users")
    @GetMapping()
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @Operation(summary = "Get a user by username")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of the user")
    @GetMapping("/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        return new ResponseEntity<>(userService.findByUsername(username), HttpStatus.OK);
    }

    @Operation(summary = "Get a user by id")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of the user")
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return new ResponseEntity<>(userService.findById(id), HttpStatus.OK);
    }

    @Operation(summary = "Delete a user by id (requires ADMIN role)")
    @ApiResponse(responseCode = "200", description = "Successful deletion of the user")
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Search for users by partial username")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of search results")
    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsers(@RequestBody UserSearchFilterRequest userSearchFilterRequest) {
        return new ResponseEntity<>(userService.searchUser(userSearchFilterRequest), HttpStatus.OK);
    }
}
