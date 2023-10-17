package com.training.musiclibrary.authentication.controllers;

import com.training.musiclibrary.authentication.DTO.AuthenticationRequest;
import com.training.musiclibrary.authentication.DTO.AuthenticationResponse;
import com.training.musiclibrary.authentication.DTO.RegisterRequest;
import com.training.musiclibrary.authentication.services.AuthenticationService;
import com.training.musiclibrary.utils.constants.Endpoints;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Endpoints.AUTH_PREFIX)
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Operation(summary = "Register a new user")
    @ApiResponse(responseCode = "200", description = "Successful registration of new user")
    @PostMapping(Endpoints.SIGNUP)
    public ResponseEntity<AuthenticationResponse> registerUser(@RequestBody RegisterRequest registerRequest) {
        return new ResponseEntity<>(authenticationService.registerUser(registerRequest), HttpStatus.CREATED);
    }

    @Operation(summary = "User Login")
    @ApiResponse(responseCode = "200", description = "Successful login of existing user")
    @PostMapping(Endpoints.LOGIN)
    public ResponseEntity<AuthenticationResponse> authenticateUser(@RequestBody AuthenticationRequest authenticationRequest) {
        return new ResponseEntity<>(authenticationService.authenticateUser(authenticationRequest), HttpStatus.OK);
    }

    @Operation(summary = "User Logout")
    @ApiResponse(responseCode = "200", description = "Successful logout of current user")
    @GetMapping(Endpoints.LOGOUT) // admin or artist
    public ResponseEntity<String> logoutUser(@RequestHeader("Authorization") String authorizationHeader){
        return new ResponseEntity<>(authenticationService.logoutUser(authorizationHeader), HttpStatus.OK);
    }

}
