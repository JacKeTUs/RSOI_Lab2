package com.jacketus.RSOI_Lab2.licensesservice.controller;

import com.jacketus.RSOI_Lab2.licensesservice.entity.License;
import com.jacketus.RSOI_Lab2.licensesservice.exception.LicenseNotFoundException;
import com.jacketus.RSOI_Lab2.licensesservice.service.LicensesService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class LicensesServiceController {
    private LicensesService licensesService;
    private Logger logger;

    @Autowired
    public LicensesServiceController(LicensesService licensesService){
        this.licensesService = licensesService;
        logger = LoggerFactory.getLogger(LicensesServiceController.class);
    }

    // Покупка.
    @PostMapping(value = "/licenses")
    public void addLicense(@RequestBody License license) {
        logger.info("[POST] /licenses");
        licensesService.createLicense(license);
    }

    // Получение информации об оплате
    @GetMapping(value = "/licenses/{id}")
    public License getLicenseById(@PathVariable Long id) throws LicenseNotFoundException {
        logger.info("[GET] /licenses/" + id);
        return licensesService.findLicenseById(id);
    }

    // Все купленные песни пользователем
    @GetMapping(value = "/licenses/find")
    public List<License> getLicenseByUserID(@RequestParam(value = "user_id") Long userID) throws LicenseNotFoundException {
        logger.info("[GET] /licenses/find ", userID);
        return licensesService.findLicenseByUserID(userID);
    }

    // Поиск лицензии на песню на пользователя.
    @GetMapping(value = "/licenses/check")
    public License checkLicenseByUserID(@RequestParam(value = "user_id") Long userID, @RequestParam(value = "book_id") Long bookID ) throws LicenseNotFoundException {
        logger.info("[GET] /licenses/check ", userID);
        License p = null;
        try {
            p = licensesService.checkLicenseByBookForUser(userID, bookID);
        } catch (LicenseNotFoundException e) {
            e.printStackTrace();
        }
        return p;
    }

    // Оценка.
    @PostMapping(value = "/licenses/{id}/rate")
    public void addLicense(@PathVariable Long id, @RequestParam(value = "rating") int rating) throws LicenseNotFoundException {
        logger.info("[POST] /licenses/" + id + "/rate, rating: " + rating);
        licensesService.rate(id, rating);
    }


    @PutMapping(value="/licenses/edit")
    public void putUser(@RequestBody License p) throws LicenseNotFoundException {
        logger.info("[PUT] /licenses/edit");
        licensesService.putLicense(p);
    }

    @GetMapping(value = "/check_health")
    public ResponseEntity checkHealth(){
        logger.info("[GET] /check_health");

        return licensesService.check_health();
    }

}
