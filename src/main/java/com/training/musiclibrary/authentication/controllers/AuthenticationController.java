package com.training.musiclibrary.authentication.controllers;

import com.training.musiclibrary.authentication.DTO.AuthenticationRequest;
import com.training.musiclibrary.authentication.DTO.AuthenticationResponse;
import com.training.musiclibrary.authentication.DTO.RegisterRequest;
import com.training.musiclibrary.authentication.models.User;
import com.training.musiclibrary.authentication.services.AuthenticationService;
import com.training.musiclibrary.authentication.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> registerUser(@RequestBody RegisterRequest registerRequest) {
        return new ResponseEntity<>(authenticationService.registerUser(registerRequest), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticateUser(@RequestBody AuthenticationRequest authenticationRequest) {
        return new ResponseEntity<>(authenticationService.authenticateUser(authenticationRequest), HttpStatus.OK);
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logoutUser(@RequestHeader("Authorization") String authorizationHeader){


        return new ResponseEntity<>(authenticationService.logoutUser(authorizationHeader), HttpStatus.OK);
    }

}
