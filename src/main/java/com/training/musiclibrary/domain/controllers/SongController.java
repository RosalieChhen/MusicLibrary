package com.training.musiclibrary.domain.controllers;

import com.training.musiclibrary.domain.DTO.SongRequest;
import com.training.musiclibrary.domain.DTO.SongSearchFilterRequest;
import com.training.musiclibrary.domain.models.Song;
import com.training.musiclibrary.domain.services.SongService;
import com.training.musiclibrary.utils.constants.Endpoints;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Endpoints.SONGS)
@RequiredArgsConstructor
public class SongController {

    private final SongService songService;

    @Operation(summary = "Get the list of all songs")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of all songs")
    @GetMapping()
    public ResponseEntity<List<Song>> getAllSongs() {
        return new ResponseEntity<>(songService.findAll(), HttpStatus.OK);
    }

    @Operation(summary = "Get a song by id")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of the song")
    @GetMapping("/{id}")
    public ResponseEntity<Song> getSongById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(songService.findById(id), HttpStatus.OK);
    }

    @Operation(summary = "Download the audio file of a song by id")
    @ApiResponse(responseCode = "200", description = "Successful download of the audio file")
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

    @Operation(summary = "Search for songs by partial titles, genre and/or by partial artist's username")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of search results")
    @GetMapping("/search")
    public ResponseEntity<List<Song>> searchSongs(@RequestBody SongSearchFilterRequest searchFilterRequest) {
        List<Song> songs = songService.searchSongs(searchFilterRequest);

        return new ResponseEntity<>(songs, HttpStatus.OK);
    }

    @Operation(summary = "Create and upload a new Song (requires ARTIST role)")
    @ApiResponse(responseCode = "200", description = "Successful creation of the new Song")
    @PostMapping()
    public ResponseEntity<Song> uploadSong(@ModelAttribute SongRequest songRequest) throws FileUploadException {
        return new ResponseEntity<>(songService.saveSong(songRequest), HttpStatus.OK);
    }

}
