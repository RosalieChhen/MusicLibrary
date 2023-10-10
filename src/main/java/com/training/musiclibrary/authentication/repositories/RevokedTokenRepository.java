package com.training.musiclibrary.authentication.repositories;

import com.training.musiclibrary.authentication.models.RevokedToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RevokedTokenRepository extends JpaRepository<RevokedToken, String> {
}
