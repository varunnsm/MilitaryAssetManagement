package com.mams.repository;
import com.mams.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
public interface PurchaseRepository extends JpaRepository<Purchase,Long> {
 List<Purchase> findByPurchaseDateBetween(LocalDate from, LocalDate to);
 List<Purchase> findByBaseIdAndPurchaseDateBetween(Long baseId, LocalDate from, LocalDate to);
}
