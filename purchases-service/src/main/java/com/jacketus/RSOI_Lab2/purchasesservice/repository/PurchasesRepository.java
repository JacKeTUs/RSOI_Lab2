package com.jacketus.RSOI_Lab2.purchasesservice.repository;

import com.jacketus.RSOI_Lab2.purchasesservice.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchasesRepository extends JpaRepository<Purchase, Long> {
    //Purchase findById(Long Id);
    Purchase findBySongID(Long songID);
    Purchase findByUserID(Long userID);
    Purchase findBySongIDAndUserID(Long songID, Long UserID);
}
