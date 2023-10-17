package com.training.musiclibrary.domain.repositories;

import com.training.musiclibrary.domain.models.Song;
import com.training.musiclibrary.domain.models.enums.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    public List<Song> findByGenre(Genre genre);
}
