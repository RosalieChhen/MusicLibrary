package com.training.musiclibrary.domain.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class AudioFile {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;
    private String type;
    @Lob
    private byte[] content;

    public AudioFile(String name, String type, byte[] content) {
        this.name = name;
        this.type = type;
        this.content = content;
    }
}
