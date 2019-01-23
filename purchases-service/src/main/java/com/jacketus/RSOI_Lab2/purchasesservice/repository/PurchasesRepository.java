package com.jacketus.RSOI_Lab2.purchasesservice.repository;

import com.jacketus.RSOI_Lab2.purchasesservice.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PurchasesRepository extends JpaRepository<Purchase, Long> {
    List<Purchase> getAllBySongID(Long songID);
    List<Purchase> getAllByUserID(Long userID);
    Purchase findBySongIDAndUserID(Long songID, Long userID);
}
