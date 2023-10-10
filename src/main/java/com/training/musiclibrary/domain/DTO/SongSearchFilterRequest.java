package com.training.musiclibrary.domain.DTO;

import com.training.musiclibrary.domain.models.enums.Genre;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SongSearchFilterRequest {
    private String title;
    private Genre genre;
    private String username;
}
