package com.jacketus.RSOI_Lab2.licensesservice.service;

import com.jacketus.RSOI_Lab2.licensesservice.entity.License;
import com.jacketus.RSOI_Lab2.licensesservice.exception.LicenseNotFoundException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface LicensesService {
    License findLicenseById(Long id) throws LicenseNotFoundException;
    List<License> findLicenseByUserID(Long userID) throws LicenseNotFoundException;
    List<License> findLicenseByBookID(Long bookID) throws LicenseNotFoundException;
    License checkLicenseByBookForUser(Long userID, Long bookID) throws LicenseNotFoundException;
    List<License> getAllLicenses();
    License createLicense(License license);
    void rate(Long id, int rating) throws LicenseNotFoundException;

    ResponseEntity check_health();

    License putLicense(License p) throws LicenseNotFoundException;
}

