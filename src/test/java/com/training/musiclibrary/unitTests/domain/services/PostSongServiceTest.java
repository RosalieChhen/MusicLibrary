package com.training.musiclibrary.unitTests.domain.services;

import com.training.musiclibrary.authentication.models.User;
import com.training.musiclibrary.authentication.models.enums.Role;
import com.training.musiclibrary.authentication.repositories.UserRepository;
import com.training.musiclibrary.domain.DTO.SongRequest;
import com.training.musiclibrary.domain.models.AudioFile;
import com.training.musiclibrary.domain.models.Song;
import com.training.musiclibrary.domain.models.enums.Genre;
import com.training.musiclibrary.domain.repositories.AudioFileRepository;
import com.training.musiclibrary.domain.repositories.SongRepository;
import com.training.musiclibrary.domain.services.AudioFileService;
import com.training.musiclibrary.domain.services.SongService;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class PostSongServiceTest {

    @Mock
    private SongRepository songRepository;

    @Mock
    private AudioFileService audioFileService;

    @Mock
    private AudioFileRepository audioFileRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    SongService songService;


    @Test
    public void testSaveSong() throws FileUploadException {

        AudioFile expectedAudioFile = new AudioFile("audioFilename", ".mp3", new byte[] {1,2,3});
        MultipartFile mockMultipartFile = new MockMultipartFile(
                expectedAudioFile.getName(),
                expectedAudioFile.getName(),
                expectedAudioFile.getType(),
                expectedAudioFile.getContent()
        );
        lenient().when(audioFileRepository.save(expectedAudioFile)).thenReturn(expectedAudioFile);
        lenient().when(audioFileService.saveAudioFile(mockMultipartFile)).thenReturn(expectedAudioFile);

        Authentication mockAuthentication = mock(Authentication.class);
        SecurityContextHolder.setContext(new SecurityContextImpl(mockAuthentication));
        lenient().when(mockAuthentication.getName()).thenReturn("Anne");

        User expectedUser = new User("Anne", "password", "anne@gmail.com", Role.ARTIST);
        lenient().when(userRepository.findByUsername("Anne")).thenReturn(Optional.of(expectedUser));

        Song expectedSong = new Song("songTitle", Genre.POP, expectedUser, expectedAudioFile);
        lenient().when(songRepository.save(expectedSong)).thenReturn(expectedSong);

        SongRequest songRequest = new SongRequest();
        songRequest.setTitle(expectedSong.getTitle());
        songRequest.setGenre(expectedSong.getGenre());
        songRequest.setAudioFile(mockMultipartFile);

        Song actualSong = songService.saveSong(songRequest);

        assertEquals(expectedSong, actualSong);
    }

}
