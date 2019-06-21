package com.jacketus.RSOI_Lab2.licensesservice;

import com.jacketus.RSOI_Lab2.licensesservice.controller.LicensesServiceController;
import com.jacketus.RSOI_Lab2.licensesservice.entity.License;
import com.jacketus.RSOI_Lab2.licensesservice.repository.LicensesRepository;
import com.jacketus.RSOI_Lab2.licensesservice.service.LicensesService;
import com.jacketus.RSOI_Lab2.licensesservice.service.LicensesServiceImplementation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
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
public class LicensesServiceControllerTests {

	//@Autowired
	private MockMvc mvc;

	@MockBean
	private LicensesServiceController licensesServiceController;

	@Mock
    LicensesService licensesService;

	@Mock
	LicensesRepository licensesRepository;

	@Before
	public void setUp(){
		initMocks(this);

        licensesService = new LicensesServiceImplementation(licensesRepository);
        licensesServiceController = new LicensesServiceController(licensesService);
		mvc = MockMvcBuilders.standaloneSetup(licensesServiceController).build();
	}

    @Test
    public void addLicense() throws Exception {

        mvc.perform(post("/licenses")
                .contentType("application/json")
                .content("{\"userID\":1, \"bookID\":1}"))
                .andExpect(status().isOk());
    }

    @Test
    public void getLicense() throws Exception {

        License license = new License();
        license.setBookID(1L);
        license.setUserID(1L);

        given(licensesRepository.findById(1L)).willReturn(Optional.of(license));
        mvc.perform(get("/licenses/1")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("bookID", is(Integer.valueOf(license.getBookID().toString()))));
    }

    @Test
    public void findLicense() throws Exception {

	    List<License> p = new ArrayList<>();
        License license = new License();
        license.setBookID(1L);
        license.setUserID(1L);
        p.add(license);

        given(licensesRepository.getAllByBookID(1L)).willReturn(p);
        mvc.perform(get("/licenses/find")
                .contentType("application/json")
                .param("user_id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("bookID", is(Integer.valueOf(license.getBookID().toString()))));
    }

    @Test
    public void checkLicense() throws Exception {

        License license = new License();
        license.setUserID(1L);
        license.setBookID(2L);

        given(licensesRepository.findByBookIDAndUserID(2L, 1L)).willReturn(license);
        mvc.perform(get("/licenses/check")
                .contentType("application/json")
                .param("user_id", "1")
                .param("book_id", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("bookID", is(Integer.valueOf(license.getBookID().toString()))));
    }

    @Test
    public void rateLicense() throws Exception {

        License license = new License();
        license.setUserID(1L);
        license.setBookID(2L);

        given(licensesRepository.findById(1L)).willReturn(Optional.of(license));
        mvc.perform(post("/licenses/1/rate")
                .contentType("application/json")
                .param("rating", "4"))
                .andExpect(status().isOk());

        mvc.perform(get("/licenses/1")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("rating", is(4)));
    }
}
