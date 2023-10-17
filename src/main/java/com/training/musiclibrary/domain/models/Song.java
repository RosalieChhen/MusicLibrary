package com.training.musiclibrary.domain.models;

import com.training.musiclibrary.authentication.models.User;
import com.training.musiclibrary.domain.models.enums.Genre;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Song implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String title;
    private Genre genre;
    @ManyToOne
    private User user;
    @OneToOne
    private AudioFile audioFile;

    public Song(String title, Genre genre, User user, AudioFile audioFile) {
        this.title = title;
        this.genre = genre;
        this.user = user;
        this.audioFile = audioFile;
    }

}
