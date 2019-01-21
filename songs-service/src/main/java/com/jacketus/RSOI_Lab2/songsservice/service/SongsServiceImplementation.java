package com.jacketus.RSOI_Lab2.songsservice.service;

import com.jacketus.RSOI_Lab2.songsservice.entity.Song;
import com.jacketus.RSOI_Lab2.songsservice.exception.SongNotFoundException;
import com.jacketus.RSOI_Lab2.songsservice.repository.SongsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import org.springframework.stereotype.Service;


@Service
public class SongsServiceImplementation implements SongsService {
    private final SongsRepository songsRepository;
    @Autowired
    public SongsServiceImplementation(SongsRepository songsRepository){
        this.songsRepository = songsRepository;
    }

    @Override
    public Page<Song> getAllSongs(PageRequest p) {
        return songsRepository.findAll(p);
    }

    @Override
    public Song getSongByID (Long id) throws SongNotFoundException {
        Song song =  songsRepository.findById(id)
                .orElseThrow(() -> new SongNotFoundException(id));
        return song;
    }

    @Override
    public Song createSong(Song song) {
        return songsRepository.save(song);
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
        return song.getRating();
    }

    @Override
    public void incBuyNum(Long id) throws SongNotFoundException {
        Song song = songsRepository.findById(id)
                .orElseThrow(() -> new SongNotFoundException(id));

        song.setBuy_nums(song.getBuy_nums() + 1);
        songsRepository.save(song);
    }

    @Override
    public int getBuyNum(Long id) throws SongNotFoundException {
        Song song = songsRepository.findById(id)
                .orElseThrow(() -> new SongNotFoundException(id));

        return song.getBuy_nums();
    }


}
