package com.jacketus.RSOI_Lab2.songsservice.service;

import com.jacketus.RSOI_Lab2.songsservice.*;

import com.jacketus.RSOI_Lab2.songsservice.entity.Song;
import com.jacketus.RSOI_Lab2.songsservice.exception.SongNotFoundException;
import com.jacketus.RSOI_Lab2.songsservice.repository.SongsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class SongsServiceImplementation implements SongsService {
    private final SongsRepository songsRepository;
    @Autowired
    public SongsServiceImplementation(SongsRepository songsRepository){
        this.songsRepository = songsRepository;
    }

    @Override
    public List<Song> getAllSongs () {
        return songsRepository.findAll();
    }

    @Override
    public void createSong(Song song) {
        songsRepository.save(song);
    }

    @Override
    public int getRateNum(Long id) throws SongNotFoundException{
        Song song = songsRepository.findById(id)
                .orElseThrow(() -> new SongNotFoundException(id));
        return song.getRateNum();
    }

    @Override
    public void setRating(Long id, double rating) throws SongNotFoundException {
        Song song = songsRepository.findById(id)
                .orElseThrow(() -> new SongNotFoundException(id));
        song.setRating(rating);
        songsRepository.save(song);
    }

    @Override
    public double getRating(Long id) throws SongNotFoundException {
        Song song = songsRepository.findById(id)
                .orElseThrow(() -> new SongNotFoundException(id));
        double res = song.getRating();
        songsRepository.save(song);
        return res;
    }

}
