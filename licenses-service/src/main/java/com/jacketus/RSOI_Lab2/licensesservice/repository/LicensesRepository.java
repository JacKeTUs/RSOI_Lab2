package com.jacketus.RSOI_Lab2.licensesservice.repository;

import com.jacketus.RSOI_Lab2.licensesservice.entity.License;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LicensesRepository extends JpaRepository<License, Long> {
    List<License> getAllByBookID(Long bookID);
    List<License> getAllByUserID(Long userID);
    License findByBookIDAndUserID(Long bookID, Long userID);
}
