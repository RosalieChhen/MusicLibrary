package com.training.musiclibrary.authentication.services;

import com.training.musiclibrary.authentication.models.RevokedToken;
import com.training.musiclibrary.authentication.repositories.RevokedTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RevokedTokenService {

    private final RevokedTokenRepository revokedTokenRepository;

    public RevokedToken revokeToken(String tokenId) {
        return revokedTokenRepository.save(new RevokedToken(tokenId));
    }

    public boolean isTokenRevoked(String tokenId) {
        return !revokedTokenRepository.findById(tokenId).isEmpty();
    }
}
