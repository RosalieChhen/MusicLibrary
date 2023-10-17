package com.training.musiclibrary.unitTests.domain.services;

import com.training.musiclibrary.authentication.models.User;
import com.training.musiclibrary.authentication.models.enums.Role;
import com.training.musiclibrary.domain.DTO.SongSearchFilterRequest;
import com.training.musiclibrary.domain.models.AudioFile;
import com.training.musiclibrary.domain.models.Song;
import com.training.musiclibrary.domain.models.enums.Genre;
import com.training.musiclibrary.domain.repositories.SongRepository;
import com.training.musiclibrary.domain.services.SongService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetSongServiceTest {

    @Mock
    private SongRepository songRepository;

    @InjectMocks
    SongService songService;

    private List<Song> songsList = new ArrayList<>();

    @BeforeEach
    public void setup(){

        User user1 = new User("Anne", "password", "anne@gmail.com", Role.ARTIST);
        User user2 = new User("John", "password", "doe@gmail.com", Role.ARTIST);

        AudioFile audio1 = new AudioFile("audio1", ".mp3", new byte[] {1,2,3,4});
        AudioFile audio2 = new AudioFile("audio1", ".mp3", new byte[] {5, 6, 7});
        AudioFile audio3 = new AudioFile("audio1", ".mp3", new byte[] {12, 17});

        Song song1 = new Song("song1", Genre.POP, user1, audio1);
        Song song2 = new Song("song2", Genre.COUNTRY, user1, audio2);
        Song song3 = new Song("song3", Genre.POP, user2, audio3);

        songsList.add(song1);
        songsList.add(song2);
        songsList.add(song3);

    }

    @Test
    public void testFindAll() {

        when(songRepository.findAll()).thenReturn(songsList);

        List<Song> actualSongsList = songService.findAll();

        assertEquals(songsList, actualSongsList);
    }

    @Test
    public void testFindById(){

        Song expectedSong = songsList.get(0);
        Long expectedSongId = expectedSong.getId();

        when(songRepository.findById(expectedSongId))
                .thenReturn(Optional.of(expectedSong));

        Song actualSong = songService.findById(expectedSongId);

        assertEquals(expectedSong, actualSong);
    }

    @Test
    public void testSearchSongByGenre() {

        Genre genreFilter = Genre.POP;

        List<Song> expectedSongsList = songsList.stream()
                .filter(song -> song.getGenre().equals(genreFilter))
                .collect(Collectors.toList());

        when(songRepository.findAll()).thenReturn(songsList);

        SongSearchFilterRequest songSearchFilterRequest = new SongSearchFilterRequest();
        songSearchFilterRequest.setGenre(genreFilter);

        List<Song> actualSongsList = songService.searchSongs(songSearchFilterRequest);

        assertEquals(expectedSongsList, actualSongsList);
    }

    @Test
    public void testSearchSongByUsername() {

        String partialUsernameFilter = "nne";

        List<Song> expectedSongsList = songsList.stream()
                .filter(song -> song.getUser().getUsername().contains(partialUsernameFilter))
                .collect(Collectors.toList());

        when(songRepository.findAll()).thenReturn(songsList);

        SongSearchFilterRequest songSearchFilterRequest = new SongSearchFilterRequest();
        songSearchFilterRequest.setUsername(partialUsernameFilter);

        List<Song> actualSongsList = songService.searchSongs(songSearchFilterRequest);

        assertEquals(expectedSongsList, actualSongsList);
    }

    @Test
    public void testSearchSongByGenreAndTitle() {

        Genre genreFilter = Genre.POP;
        String partialTitleFilter = "g1";

        List<Song> expectedSongsList = songsList.stream()
                .filter(song -> song.getGenre().equals(genreFilter))
                .filter(song -> song.getTitle().contains(partialTitleFilter))
                .collect(Collectors.toList());

        when(songRepository.findAll()).thenReturn(songsList);

        SongSearchFilterRequest songSearchFilterRequest = new SongSearchFilterRequest();
        songSearchFilterRequest.setGenre(genreFilter);
        songSearchFilterRequest.setTitle(partialTitleFilter);

        List<Song> actualSongsList = songService.searchSongs(songSearchFilterRequest);

        assertEquals(expectedSongsList, actualSongsList);
    }

    @Test
    public void testGetAudioFileContentBySongId() {

        Song expectedSong = songsList.get(0);
        Long expectedSongId = expectedSong.getId();
        byte[] expectedAudioFileContent = expectedSong.getAudioFile().getContent();

        when(songRepository.findById(expectedSongId)).thenReturn(Optional.of(expectedSong));

        byte[] actualAudioFileContent = songService.getAudioFileContentBySongId(expectedSongId);

        assertEquals(expectedAudioFileContent, actualAudioFileContent);
    }



}
