package com.jacketus.RSOI_Lab2.songsservice.service;

import com.jacketus.RSOI_Lab2.songsservice.entity.Song;
import com.jacketus.RSOI_Lab2.songsservice.exception.SongNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;

public interface SongsService {
    Page<Song> getAllSongs(PageRequest p);
    Song createSong(Song song);
    Song getSongByID(Long id) throws SongNotFoundException;
    void setRating(Long id, double rating) throws SongNotFoundException;
    double getRating(Long id) throws SongNotFoundException;
    int getRateNum(Long id) throws SongNotFoundException;
    void incBuyNum(Long id) throws SongNotFoundException;
    int getBuyNum(Long id) throws SongNotFoundException;
    Song putSong(Song p) throws SongNotFoundException;

    ResponseEntity check_health();
}