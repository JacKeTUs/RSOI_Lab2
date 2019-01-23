package com.jacketus.RSOI_Lab2.songsservice;

import com.jacketus.RSOI_Lab2.songsservice.controller.SongsServiceController;
import com.jacketus.RSOI_Lab2.songsservice.entity.Song;
import com.jacketus.RSOI_Lab2.songsservice.repository.SongsRepository;
import com.jacketus.RSOI_Lab2.songsservice.service.SongsService;
import com.jacketus.RSOI_Lab2.songsservice.service.SongsServiceImplementation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SongsServiceControllerTests {

	//@Autowired
    private MockMvc mvc;

	@MockBean
    private SongsServiceController songsServiceController;

	@Mock
	SongsService songsService;

	@Mock
    SongsRepository songsRepository;

    @Before
    public void setUp(){
        initMocks(this);

        songsService = new SongsServiceImplementation(songsRepository);
        songsServiceController = new SongsServiceController(songsService);
        mvc = MockMvcBuilders.standaloneSetup(songsServiceController).build();
    }

	@Test
	public void getAllSongs() throws Exception {
        List<Song> songs = new ArrayList<>();

        Song song = new Song();
        song.setArtist("Artist");
        song.setName("Song");
        song.setLink("Link");
        songs.add(song);
        songs.add(song);
        songs.add(song);

        Page<Song> songs_p = new PageImpl<Song>(songs);

        given(songsServiceController.getAllSongs(1,5)).willReturn(songs_p);
        mvc.perform(get("/songs?page=1&size=5")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("content[0].name", is(song.getName())));
	}

    @Test
    public void getSongByID() throws Exception {
        List<Song> songs = new ArrayList<>();

        Song song = new Song();
        song.setArtist("Artist");
        song.setName("Song");
        song.setLink("Link");
        songs.add(song);

        given(songsRepository.findById(1L)).willReturn(Optional.of(song));
        mvc.perform(get("/songs/1")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name", is(song.getName())));
    }

    @Test
    public void addSong() throws Exception {
        List<Song> songs = new ArrayList<>();

        Song song = new Song();
        song.setArtist("Artist");
        song.setName("Song");
        song.setLink("Link");
        songs.add(song);

        mvc.perform(post("/songs")
                .contentType("application/json")
                .content("{\"artist\":\"Sample Artist\", \"name\":\"Sample name\", \"link\":\"Sample Link\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void rateSong() throws Exception {
        List<Song> songs = new ArrayList<>();

        Song song = new Song();
        song.setArtist("Artist");
        song.setName("Song");
        song.setLink("Link");
        songs.add(song);

        given(songsRepository.findById(1L)).willReturn(Optional.of(song));
        mvc.perform(post("/songs/1/rate")
                .contentType("application/json")
                .param("rating", "4"))
                .andExpect(status().isOk());
    }

    @Test
    public void buySong() throws Exception {
        List<Song> songs = new ArrayList<>();

        Song song = new Song();
        song.setArtist("Artist");
        song.setName("Song");
        song.setLink("Link");
        songs.add(song);

        given(songsRepository.findById(1L)).willReturn(Optional.of(song));
        mvc.perform(post("/songs/1/buy")
                .contentType("application/json"))
                .andExpect(status().isOk());
    }
}
