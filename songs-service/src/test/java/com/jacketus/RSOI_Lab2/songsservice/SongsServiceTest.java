package com.jacketus.RSOI_Lab2.songsservice;

import com.jacketus.RSOI_Lab2.songsservice.entity.Song;
import com.jacketus.RSOI_Lab2.songsservice.exception.SongNotFoundException;
import com.jacketus.RSOI_Lab2.songsservice.repository.SongsRepository;
import com.jacketus.RSOI_Lab2.songsservice.service.SongsService;
import com.jacketus.RSOI_Lab2.songsservice.service.SongsServiceImplementation;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
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

        try {
            given(songsRepository.save(song)).willReturn(song);
            Song songsaved = songsRepository.save(song);
            given(songsRepository.findById(songsaved.getId())).willReturn(Optional.of(songsaved));

            songsService.createSong(song);

            assertEquals("Artist", songsaved.getArtist());
            assertEquals("Song", songsaved.getName());
            assertEquals("Link", songsaved.getLink());
        }
        catch (Exception ex){
            fail();
        }
    }

    @Test
    public void shouldReturnSongsList(){
        List<Song> songs = new ArrayList<>();

        Song song = new Song();
        song.setArtist("Artist");
        song.setName("Song");
        song.setLink("Link");
        songs.add(song);
        songs.add(song);
        songs.add(song);

        Page<Song> songs_p = new PageImpl<Song>(songs);

        given(songsRepository.findAll(PageRequest.of(1, 1))).willReturn(songs_p);
        Page<Song> songsReturned = songsService.getAllSongs(PageRequest.of(1, 1));

        assertThat(songsReturned, is(songs_p));
    }

    @Test
    public void shouldNotFindSong(){
        Song song = new Song();
        song.setArtist("Artist");
        song.setName("Song");
        song.setLink("Link");


        given(songsRepository.findById(song.getId())).willReturn(Optional.of(song));
        Song songsaved = songsRepository.save(song);

        boolean founded = true;
        try {
            songsService.getRateNum(song.getId() + 1);
        } catch (Exception ex) {
            founded = false;
        }

        assertEquals(false, founded);

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
    public void shouldAddRate(){
        Song song = new Song();
        song.setArtist("Artist");
        song.setName("Song");
        song.setLink("Link");
        song.setRating(5);

        try {
            given(songsRepository.save(song)).willReturn(song);
            given(songsRepository.findById(song.getId())).willReturn(Optional.of(song));
            Song songsaved = songsRepository.save(song);

            songsService.setRating(songsaved.getId(), 3);
            double r = songsService.getRating(songsaved.getId());
            int eq = 0;
            if (r >= 0) eq = 1;
            assertEquals(1, eq);
        }
        catch (SongNotFoundException ex){
            fail();
        }
    }

    @Test
    public void shouldAddBuyNum(){
        Song song = new Song();
        song.setArtist("Artist");
        song.setName("Song");
        song.setLink("Link");

        try {
            given(songsRepository.save(song)).willReturn(song);
            given(songsRepository.findById(song.getId())).willReturn(Optional.of(song));
            Song songsaved = songsRepository.save(song);

            songsService.incBuyNum(songsaved.getId());
            int r = songsService.getBuyNum(songsaved.getId());
            assertEquals(1, r);
        }
        catch (SongNotFoundException ex){
            fail();
        }
    }

    @Test
    public void shouldHash(){
        Song song = new Song();
        song.setArtist("Artist");
        song.setName("Song");
        song.setLink("Link");
        song.setRating(5);

        try {
            given(songsRepository.save(song)).willReturn(song);
            given(songsRepository.findById(song.getId())).willReturn(Optional.of(song));
            Song songsaved = songsRepository.save(song);

            assertNotNull(songsaved.hashCode());
        }
        catch (Exception ex){
            fail();
        }
    }

    @Test
    public void shouldToString(){
        Song song = new Song();
        song.setArtist("Artist");
        song.setName("Song");
        song.setLink("Link");
        song.setRating(5);
        String s = "Song {id=null, artist=Artist, name='Song', link=Link, rating=5.0, rate_nums=1, buy_nums=0}";

        try {
            given(songsRepository.save(song)).willReturn(song);
            given(songsRepository.findById(song.getId())).willReturn(Optional.of(song));
            Song songsaved = songsRepository.save(song);

            System.out.println(songsaved.toString());
            assertEquals(s, songsaved.toString());
        }
        catch (Exception ex){
            fail();
        }
    }

    @Test
    public void shouldEquals(){
        Song song = new Song();
        song.setArtist("Artist");
        song.setName("Song");
        song.setLink("Link");
        song.setRating(5);

        try {
            given(songsRepository.save(song)).willReturn(song);
            given(songsRepository.findById(song.getId())).willReturn(Optional.of(song));
            Song songsaved = songsRepository.save(song);

            boolean eq = song.equals(songsaved);
            assertEquals(true, eq);
        }
        catch (Exception ex){
            fail();
        }
    }

}
