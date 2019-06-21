package com.jacketus.RSOI_Lab2.licensesservice;

import com.jacketus.RSOI_Lab2.licensesservice.entity.License;
import com.jacketus.RSOI_Lab2.licensesservice.exception.LicenseNotFoundException;
import com.jacketus.RSOI_Lab2.licensesservice.repository.LicensesRepository;
import com.jacketus.RSOI_Lab2.licensesservice.service.LicensesService;
import com.jacketus.RSOI_Lab2.licensesservice.service.LicensesServiceImplementation;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.initMocks;

public class LicensesServiceTest {
    private LicensesService licensesService;

    @Mock
    LicensesRepository licensesRepository;

    @Before
    public void setUp(){
        initMocks(this);
        licensesService = new LicensesServiceImplementation(licensesRepository);
    }

    @Test
    public void shouldReturnAllLicenses(){
        List<License> licenses= new ArrayList<>();
        License license= new License();
        license.setBookID(5L);
        license.setUserID(1L);

        given(licensesRepository.findAll()).willReturn(licenses);
        List<License> licensesReturned = licensesService.getAllLicenses();
        assertThat(licensesReturned, is(licenses));
    }

    @Test
    public void shouldCreateLicense(){
        License license = new License();
        license.setBookID(5L);
        license.setUserID(1L);

        given(licensesRepository.save(license)).willReturn(license);
        assertThat(licensesRepository.save(license), is(license));
        License saved = licensesRepository.save(license);

        Long s = saved.getBookID();
        assertEquals(Optional.of(5L), Optional.ofNullable(s));
        assertEquals(Optional.of(1L), Optional.ofNullable(saved.getUserID()));
    }

    @Test
    public void shouldFindLicenseByBookID() throws LicenseNotFoundException {
        List<License> licenses= new ArrayList<>();
        License license = new License();
        license.setBookID(5L);
        license.setUserID(1L);
        licenses.add(license);

        given(licensesRepository.save(license)).willReturn(license);
        given(licensesRepository.getAllByBookID(5L)).willReturn(licenses);

        licensesService.createLicense(license);
        assertThat(licensesService.findLicenseByBookID(5L).get(0), is(license));
    }

    @Test
    public void shouldFindLicenseByUserID() throws LicenseNotFoundException {
        List<License> licenses= new ArrayList<>();
        License license = new License();
        license.setBookID(5L);
        license.setUserID(1L);
        licenses.add(license);

        given(licensesRepository.save(license)).willReturn(license);
        given(licensesRepository.getAllByUserID(1L)).willReturn(licenses);

        licensesService.createLicense(license);
        assertThat(licensesService.findLicenseByUserID(1L).get(0), is(license));
    }

    @Test
    public void shouldFindLicenseByUserIDAndBookID() throws LicenseNotFoundException {
        License license = new License();
        license.setBookID(5L);
        license.setUserID(1L);

        given(licensesRepository.save(license)).willReturn(license);
        given(licensesRepository.findByBookIDAndUserID(5L,1L)).willReturn(license);

        licensesService.createLicense(license);

        if (licensesService.checkLicenseByBookForUser(1L, 5L) == null)
            fail();
        if (licensesService.checkLicenseByBookForUser(1L, 6L) != null)
            fail();

    }

    @Test
    public void shouldRateLicense() throws LicenseNotFoundException {
        License license = new License();
        license.setBookID(5L);
        license.setUserID(1L);

        given(licensesRepository.save(license)).willReturn(license);
        given(licensesRepository.findById(license.getId())).willReturn(Optional.of(license));

        License saved = licensesService.createLicense(license);
        licensesService.rate(saved.getId(), 4);
        int rate = licensesService.findLicenseById(saved.getId()).getRating();

        assertEquals(4, rate);
    }

    @Test
    public void shouldHash(){
        License license = new License();
        license.setBookID(5L);
        license.setUserID(1L);

        try {
            given(licensesRepository.save(license)).willReturn(license);
            given(licensesRepository.findById(license.getId())).willReturn(Optional.of(license));
            License saved = licensesService.createLicense(license);

            assertNotNull(saved.hashCode());
        }
        catch (Exception ex){
            fail();
        }
    }

    @Test
    public void shouldToString(){
        License license = new License();
        license.setBookID(5L);
        license.setUserID(1L);
        String s = "License{id=null, userID='1', bookID='5', rating='0'}";

        try {
            given(licensesRepository.save(license)).willReturn(license);
            given(licensesRepository.findById(license.getId())).willReturn(Optional.of(license));
            License saved = licensesService.createLicense(license);

            System.out.println(saved.toString());
            assertEquals(s, saved.toString());
        }
        catch (Exception ex){
            fail();
        }
    }

    @Test
    public void shouldEquals(){
        License license = new License();
        license.setBookID(5L);
        license.setUserID(1L);

        try {
            given(licensesRepository.save(license)).willReturn(license);
            given(licensesRepository.findById(license.getId())).willReturn(Optional.of(license));
            License saved = licensesService.createLicense(license);

            boolean eq = saved.equals(license);
            assertEquals(true, eq);
        }
        catch (Exception ex){
            fail();
        }
    }

}
