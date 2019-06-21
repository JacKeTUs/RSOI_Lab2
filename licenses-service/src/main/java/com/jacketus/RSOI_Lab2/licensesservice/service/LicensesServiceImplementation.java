package com.jacketus.RSOI_Lab2.licensesservice.service;

import com.jacketus.RSOI_Lab2.licensesservice.entity.License;
import com.jacketus.RSOI_Lab2.licensesservice.exception.LicenseNotFoundException;
import com.jacketus.RSOI_Lab2.licensesservice.repository.LicensesRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LicensesServiceImplementation implements LicensesService {
    private final LicensesRepository licensesRepository;

    @Autowired
    public LicensesServiceImplementation(LicensesRepository licensesRepository) {
        this.licensesRepository = licensesRepository;
    }

    @Override
    public List<License> getAllLicenses() {
        return licensesRepository.findAll();
    }

    @Override
    public License findLicenseById(Long id) throws LicenseNotFoundException {
        return licensesRepository.findById(id)
                .orElseThrow(() -> new LicenseNotFoundException(id));
    }

    @Override
    public List<License> findLicenseByUserID(Long userID) throws LicenseNotFoundException{
        List<License> license = licensesRepository.getAllByUserID(userID);
        if (license == null)
            throw new LicenseNotFoundException(userID);
        return license;
    }

    @Override
    public List<License> findLicenseByBookID(Long bookID) throws LicenseNotFoundException {
        List<License> license = licensesRepository.getAllByBookID(bookID);
        if (license == null)
            throw new LicenseNotFoundException(bookID);
        return license;
    }

    @Override
    public License checkLicenseByBookForUser(Long userID, Long bookID) throws LicenseNotFoundException {
        License license = licensesRepository.findByBookIDAndUserID(bookID, userID);
        return license;
    }

    @Override
    public License createLicense(License license) {
        System.out.println("CREATE: " + license.toString());
        return licensesRepository.save(license);
    }

    @Override
    public void rate(Long id, int rating) throws LicenseNotFoundException {
        License license = licensesRepository.findById(id)
                .orElseThrow(() -> new LicenseNotFoundException(id));
        license.setRating(rating);
        licensesRepository.save(license);
    }

    @Override
    public ResponseEntity check_health() {
        JSONObject json1 = new JSONObject();
        try {
            json1.put("info", "Licenses MService");
            json1.put("version", 0.4);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok(json1.toString());
    }


    @Override
    public License putLicense(License p) throws LicenseNotFoundException {
        return licensesRepository.findById(p.getId()).map(License -> {
            License.setUserID(p.getUserID());
            License.setBookID(p.getBookID());
            License.setRating(p.getRating());
            return licensesRepository.save(License);
        }).orElseThrow(() -> new LicenseNotFoundException(p.getId()));
    }
}
