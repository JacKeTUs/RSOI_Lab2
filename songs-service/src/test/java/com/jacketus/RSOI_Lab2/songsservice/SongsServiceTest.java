package com.jacketus.RSOI_Lab2.songsservice;

import com.jacketus.RSOI_Lab2.songsservice.entity.Song;
import com.jacketus.RSOI_Lab2.songsservice.exception.SongNotFoundException;
import com.jacketus.RSOI_Lab2.songsservice.repository.SongsRepository;
import com.jacketus.RSOI_Lab2.songsservice.service.SongsService;
import com.jacketus.RSOI_Lab2.songsservice.service.SongsServiceImplementation;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.initMocks;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class SongsServiceTest {
    private SongsService songsService;

    @Mock
    SongsRepository songsRepository;

    @Before
    public void setUp(){
        initMocks(this);
        songsService = new SongsServiceImplementation(songsRepository);
    }

    @Test
    public void shouldCreateNewSong(){
        Song song = new Song();
        song.setArtist("Artist");
        song.setName("Song");
        song.setLink("Link");

        songsRepository.save(song);
    }

    @Test
    public void shouldReturnSongsList(){
        List<Song> songs = new ArrayList<>();
        Song song = new Song();
        song.setArtist("Artist");
        song.setName("Song");
        song.setLink("Link");
        songs.add(song);

        given(songsRepository.findAll()).willReturn(songs);
        List<Song> songsReturned = songsService.getAllSongs();
        assertThat(songsReturned, is(songs));
    }

    @Test
    public void shouldSetReviewsNum(){
        Song song = new Song();
        song.setArtist("Artist");
        song.setName("Song");
        song.setLink("Link");
        song.setRating(5);

        try {
            given(songsRepository.save(song)).willReturn(song);
            Song songsaved = songsRepository.save(song);
            given(songsRepository.findById(songsaved.getId())).willReturn(Optional.of(songsaved));

            int revs_num = songsService.getRateNum(songsaved.getId());
            assertEquals(1, revs_num);
    }
        catch (SongNotFoundException ex){
        fail();
    }
    }

    @Test
    public void shouldAddReview(){
        Song song = new Song();
        song.setArtist("Artist");
        song.setName("Song");
        song.setLink("Link");
        song.setRating(5);

        try {
            given(songsRepository.save(song)).willReturn(song);
            given(songsRepository.findById(song.getId())).willReturn(Optional.of(song));
            Song songsaved = songsRepository.save(song);

            songsaved.setRating(1);
            double r = songsService.getRating(songsaved.getId());
            int eq = 0;
            if (r == 3) eq = 1;
            assertEquals(1, eq);
        }
        catch (SongNotFoundException ex){
            fail();
        }
    }

}
