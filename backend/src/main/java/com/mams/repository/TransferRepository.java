package com.mams.repository;
import com.mams.entity.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
public interface TransferRepository extends JpaRepository<Transfer,Long> {
 List<Transfer> findByTransferDateBetween(LocalDate from, LocalDate to);
 List<Transfer> findByFromBaseIdOrToBaseIdOrderByTransferDateDesc(Long fromBaseId, Long toBaseId);
}
