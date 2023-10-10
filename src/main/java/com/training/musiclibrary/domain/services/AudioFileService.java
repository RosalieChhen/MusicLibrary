package com.training.musiclibrary.domain.services;

import com.training.musiclibrary.domain.repositories.AudioFileRepository;
import com.training.musiclibrary.domain.models.AudioFile;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AudioFileService {

    private final AudioFileRepository audioFileRepository;

    public AudioFile saveAudioFile(MultipartFile audioFile) throws FileUploadException {

        AudioFile newAudioFile = new AudioFile();

        newAudioFile.setName(audioFile.getOriginalFilename());
        newAudioFile.setType(audioFile.getContentType());
        try {
            newAudioFile.setContent(audioFile.getBytes());
        } catch (IOException e) {
            throw new FileUploadException("Error while uploading audio file", e);
        }

        return audioFileRepository.save(newAudioFile);
    }
}
