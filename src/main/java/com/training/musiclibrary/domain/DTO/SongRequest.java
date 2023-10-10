package com.training.musiclibrary.domain.DTO;

import com.training.musiclibrary.domain.models.enums.Genre;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class SongRequest {
    private String title;
    private Genre genre;
    private MultipartFile audioFile;

    // user is determined by connected user
}
