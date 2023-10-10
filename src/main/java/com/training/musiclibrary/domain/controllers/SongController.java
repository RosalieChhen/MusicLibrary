package com.training.musiclibrary.domain.controllers;

import com.training.musiclibrary.domain.DTO.SongRequest;
import com.training.musiclibrary.domain.DTO.SongSearchFilterRequest;
import com.training.musiclibrary.domain.models.Song;
import com.training.musiclibrary.domain.services.SongService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/songs")
@RequiredArgsConstructor
public class SongController {

    private final SongService songService;

    @GetMapping()
    public ResponseEntity<List<Song>> getAllSongs() {
        return new ResponseEntity<>(songService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Song> getSongById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(songService.findById(id), HttpStatus.OK);
    }

    @GetMapping("{id}/download")
    public ResponseEntity<byte[]> downloadSong(@PathVariable("id") Long id) {

        byte[] audioFileContent = songService.getAudioFileContentBySongId(id);

        if(audioFileContent == null) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "song.mp3");

        return new ResponseEntity<>(audioFileContent, headers, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Song>> searchSongs(@RequestBody SongSearchFilterRequest searchFilterRequest) {
        List<Song> songs = songService.searchSongs(searchFilterRequest);

        return new ResponseEntity<>(songs, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Song> uploadSong(@ModelAttribute SongRequest songRequest) throws FileUploadException {
        return new ResponseEntity<>(songService.saveSong(songRequest), HttpStatus.OK);
    }

}
