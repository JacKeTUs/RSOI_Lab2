package com.jacketus.RSOI_Lab2.purchasesservice.repository;

import com.jacketus.RSOI_Lab2.purchasesservice.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PurchasesRepository extends JpaRepository<Purchase, Long> {
    Purchase findBySongID(Long songID);
    Purchase findByUserID(Long userID);
    Purchase findBySongIDAndUserID(Long songID, Long userID);
}
