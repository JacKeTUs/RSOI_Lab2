package com.jacketus.RSOI_Lab2.songsservice.service;

import com.jacketus.RSOI_Lab2.songsservice.entity.Song;
import com.jacketus.RSOI_Lab2.songsservice.exception.SongNotFoundException;

import java.util.List;

public interface SongsService {
    List<Song> getAllSongs();
    void createSong(Song song);
    Song getSongByID(Long id) throws SongNotFoundException;
    void setRating(Long id, double rating) throws SongNotFoundException;
    double getRating(Long id) throws SongNotFoundException;
    int getRateNum(Long id) throws SongNotFoundException;
    void incBuyNum(Long id) throws SongNotFoundException;
    int getBuyNum(Long id) throws SongNotFoundException;
}