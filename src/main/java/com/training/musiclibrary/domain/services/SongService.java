package com.training.musiclibrary.domain.services;

import com.training.musiclibrary.authentication.models.User;
import com.training.musiclibrary.authentication.repositories.UserRepository;
import com.training.musiclibrary.domain.DTO.SongRequest;
import com.training.musiclibrary.domain.DTO.SongSearchFilterRequest;
import com.training.musiclibrary.domain.models.AudioFile;
import com.training.musiclibrary.domain.models.Song;
import com.training.musiclibrary.domain.repositories.SongRepository;
import com.training.musiclibrary.utils.EnumUtils;
import com.training.musiclibrary.utils.StringUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SongService {

    private final SongRepository songRepository;
    private final AudioFileService audioFileService;
    private final UserRepository userRepository;

    public List<Song> findAll() {
        return songRepository.findAll();
    }

    public Song findById(Long id) {
        return songRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    // QUESTION : What to return ? songId, song entity, ... ?
    public Song saveSong(SongRequest songRequest ) throws FileUploadException {

        // Create an AudioFile entity and save it
        AudioFile audioFile = audioFileService.saveAudioFile(songRequest.getAudioFile());

        // Get current logged in user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName()).orElse(null); // Front have to manage, question ?

        // Create a Song entity, associate it to the audioFile and user and save the new song
        Song newSong = new Song(
                songRequest.getTitle(),
                songRequest.getGenre(),
                user,
                audioFile
        );
        return songRepository.save(newSong);
    }

    public byte[] getAudioFileContentBySongId(Long id) {

        Song song = this.findById(id);

        if(song == null || song.getAudioFile() == null) {
            return null;
        }

        return song.getAudioFile().getContent();
    }

    public List<Song> searchSongs(SongSearchFilterRequest searchFilter) {

        List<Song> songs = songRepository.findAll();

        //if(searchFilter.getGenre() != null && searchFilter.getGenre().name() != "" ) {
        if(!EnumUtils.isNullOrEmpty(searchFilter.getGenre())) { // is useless to check empty string for enum type ?
            songs = songs.stream()
                    .filter(song -> song.getGenre().equals(searchFilter.getGenre()))
                    .collect(Collectors.toList());
        }

        if(!StringUtils.isNullOrBlank(searchFilter.getTitle())) {
            // Implement NOT case sensitive filter
            final String titleFilter = searchFilter.getTitle().toLowerCase();
            songs = songs.stream()
                    .filter(song -> song.getTitle().toLowerCase().contains(titleFilter))
                    .collect(Collectors.toList());
        }

        if(!StringUtils.isNullOrBlank(searchFilter.getUsername())) {
            // Implement NOT case sensitive filter
            final String usernameFilter = searchFilter.getUsername().toLowerCase();
            songs = songs.stream()
                    .filter(song -> song.getUser() != null) // preventive
                    .filter(song -> song.getUser().getUsername().toLowerCase().contains(usernameFilter))
                    .collect(Collectors.toList());
        }

        return songs;
    }
}
