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

    // Получение списка песен
    @GetMapping(value = "/songs")
    public List<Song> getAllSongs(){
        logger.info("[GET] /songs ");
        return songsService.getAllSongs();
    }

    // Получение информации об одной песне
    @GetMapping(value = "/songs/{id}")
    public Song getSongByID(@PathVariable Long id) throws SongNotFoundException {
        logger.info("[GET] /songs/ " + id);
        return songsService.getSongByID(id);
    }

    // Добавление песни на сайт
    @PostMapping(value = "/songs")
    public void createSong(@RequestBody Song song){
        logger.info("[POST] /songs ", song);
        songsService.createSong(song);
    }

    // Оценка песни
    @PostMapping(value = "songs/{id}/rate/{rating}")
    public void setRating(@PathVariable Long id, @PathVariable String rating) throws SongNotFoundException {

        double rate = Double.parseDouble(rating);
        songsService.setRating(id, rate);
        logger.info("[POST] /songs/" + id + "/rate/" + rate);
    }

    // Покупка песни
    @PostMapping(value = "/songs/{id}/buy")
    public void buySong(@PathVariable Long id) throws SongNotFoundException {
        songsService.incBuyNum(id);
        logger.info("[POST] /songs/" + id + "/buy");
    }
}
