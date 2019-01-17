package com.jacketus.RSOI_Lab2.songsservice.controller;

import com.jacketus.RSOI_Lab2.songsservice.*;
import com.jacketus.RSOI_Lab2.songsservice.entity.Song;
import com.jacketus.RSOI_Lab2.songsservice.exception.SongNotFoundException;
import com.jacketus.RSOI_Lab2.songsservice.service.SongsService;
import com.jacketus.RSOI_Lab2.songsservice.service.SongsServiceImplementation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class SongsServiceController {
    private final SongsService songsService;
    Logger logger;

    @Autowired
    public SongsServiceController(SongsService _songsService){
        this.songsService = _songsService;
        logger  = LoggerFactory.getLogger(SongsServiceImplementation.class);
    }

    @PostMapping(value = "/songs")
    public void createSong(@RequestBody Song song){
        songsService.createSong(song);
        logger.info("[POST] /songs ", song);
    }

    @GetMapping(value = "/songs")
    public List<Song> getAllSongs(){
        logger.info("[GET] /songs ");
        return songsService.getAllSongs();
    }

    @PostMapping(value = "songs/{id}/setRating/{rating}")
    public void setRating(@PathVariable Long id, @PathVariable double rating) throws SongNotFoundException {
        songsService.setRating(id, rating);
        logger.info("[POST] /songs/" + id + "/setRating/" + rating);
    }
}
