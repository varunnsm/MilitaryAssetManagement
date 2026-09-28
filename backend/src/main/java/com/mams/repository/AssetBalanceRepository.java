package com.mams.repository;
import com.mams.entity.AssetBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface AssetBalanceRepository extends JpaRepository<AssetBalance,Long> {
 Optional<AssetBalance> findByBaseIdAndEquipmentTypeId(Long baseId, Long equipmentTypeId);
 List<AssetBalance> findByBaseId(Long baseId);
}
