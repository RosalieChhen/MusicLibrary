package com.training.musiclibrary.authentication.services;

import com.training.musiclibrary.authentication.DTO.AuthenticationRequest;
import com.training.musiclibrary.authentication.DTO.AuthenticationResponse;
import com.training.musiclibrary.authentication.DTO.RegisterRequest;
import com.training.musiclibrary.authentication.models.User;
import com.training.musiclibrary.authentication.models.enums.Role;
import com.training.musiclibrary.authentication.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RevokedTokenService revokedTokenService;

    public AuthenticationResponse registerUser(RegisterRequest registerRequest) {
        User newUser = new User(
                registerRequest.getUsername(),
                passwordEncoder.encode(registerRequest.getPassword()),
                registerRequest.getEmail(),
                Role.ARTIST
        );

        userRepository.save(newUser);
        var jwtToken = jwtService.generateToken(newUser);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticateUser(AuthenticationRequest authenticationRequest) {
        // manage all authentication, if can not authenticate => throw exception
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsername(),
                        authenticationRequest.getPassword()
                )
        );

        var user = userRepository.findByUsername(authenticationRequest.getUsername())
                .orElseThrow();

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public String logoutUser(String authorizationHeader){
        final var token = jwtService.getToken(authorizationHeader);
        revokedTokenService.revokeToken(token);
        return "Logged out successfully"; // TODO ??
    }
}
