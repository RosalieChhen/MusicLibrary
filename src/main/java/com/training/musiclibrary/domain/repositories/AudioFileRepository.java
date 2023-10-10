package com.training.musiclibrary.domain.repositories;

import com.training.musiclibrary.domain.models.AudioFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AudioFileRepository extends JpaRepository<AudioFile, Long> {
}
