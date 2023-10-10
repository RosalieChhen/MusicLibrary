package com.training.musiclibrary.authentication.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class RevokedToken {
    @Id
    private String id;

    public RevokedToken(String id) {
        this.id = id;
    }
}
