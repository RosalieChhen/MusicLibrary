package com.training.musiclibrary.integrationTests.restApi;

import com.training.musiclibrary.authentication.models.User;
import com.training.musiclibrary.authentication.models.enums.Role;
import com.training.musiclibrary.authentication.repositories.UserRepository;
import com.training.musiclibrary.domain.models.AudioFile;
import com.training.musiclibrary.domain.models.Song;
import com.training.musiclibrary.domain.models.enums.Genre;
import com.training.musiclibrary.domain.repositories.AudioFileRepository;
import com.training.musiclibrary.domain.repositories.SongRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test") // defined in config
public class GetSongIT {

    //@Autowired
    //private DataSource testDataSource;

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AudioFileRepository audioFileRepository;

    // Provides way to send HTTP request and validate responses without running a web server
    // Provides a simulated environment to testing controllers methods
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetAllSongs() throws Exception {
        // given
        User user1 = new User("Anne", "password", "anne@gmail.com", Role.ARTIST);
        User user2 = new User("John", "password", "doe@gmail.com", Role.ARTIST);

        userRepository.save(user1);
        userRepository.save(user2);

        AudioFile audio1 = new AudioFile("audio1", ".mp3", new byte[] {1,2,3,4});
        AudioFile audio2 = new AudioFile("audio1", ".mp3", new byte[] {5, 6, 7});
        AudioFile audio3 = new AudioFile("audio1", ".mp3", new byte[] {12, 17});

        audioFileRepository.save(audio1);
        audioFileRepository.save(audio2);
        audioFileRepository.save(audio3);

        Song song1 = new Song("song1", Genre.POP, user1, audio1);
        Song song2 = new Song("song2", Genre.COUNTRY, user1, audio2);
        Song song3 = new Song("song3", Genre.POP, user2, audio3);

        songRepository.save(song1);
        songRepository.save(song2);
        songRepository.save(song3);

        // when
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/songs"));

        // then
        response.andExpectAll(
                status().isOk(),
                content().contentType("application/json")
        );

    }

}
