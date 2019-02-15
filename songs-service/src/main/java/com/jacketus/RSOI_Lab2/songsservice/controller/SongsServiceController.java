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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
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
    public Page<Song> getAllSongs(@RequestParam(value = "page") int page, @RequestParam(value = "size") int size){
        logger.info("[GET] /songs page=" + page + " size=" + size);
        PageRequest p = PageRequest.of(page, size);
        return songsService.getAllSongs(p);
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
    @PostMapping(value = "/songs/{id}/rate")
    public void setRating(@PathVariable Long id, @RequestParam(value = "rating") int rate) throws SongNotFoundException {
        logger.info("[POST] /songs/" + id + "/rate, rating:" + rate);
        songsService.setRating(id, rate);
    }

    // Покупка песни
    @PostMapping(value = "/songs/{id}/buy")
    public void buySong(@PathVariable Long id) throws SongNotFoundException {
        logger.info("[POST] /songs/" + id + "/buy");
        songsService.incBuyNum(id);
    }

    @PutMapping(value="/songs/edit")
    public void putUser(@RequestBody Song p) throws SongNotFoundException {
        logger.info("[PUT] /songs/edit");
        songsService.putSong(p);
    }

    @GetMapping(value = "/check_health")
    public ResponseEntity checkHealth(){
        logger.info("[GET] /check_health");

        return songsService.check_health();
    }

}
