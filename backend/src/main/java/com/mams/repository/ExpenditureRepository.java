package com.mams.repository;
import com.mams.entity.Expenditure;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
public interface ExpenditureRepository extends JpaRepository<Expenditure,Long> {
 List<Expenditure> findByExpenditureDateBetween(LocalDate from, LocalDate to);
 List<Expenditure> findByBaseIdAndExpenditureDateBetween(Long baseId, LocalDate from, LocalDate to);
}
