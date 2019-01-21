package com.jacketus.RSOI_Lab2.purchasesservice.repository;

import com.jacketus.RSOI_Lab2.purchasesservice.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PurchasesRepository extends JpaRepository<Purchase, Long> {
    Purchase findBySongID(Long song_id);
    Purchase findByUserID(Long user_id);
    Purchase findBySongIDAndUserID(Long song_id, Long User_id);
}
