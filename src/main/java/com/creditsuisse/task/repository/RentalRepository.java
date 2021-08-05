package com.creditsuisse.task.repository;

import com.creditsuisse.task.domain.Rental;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Rental entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RentalRepository extends JpaRepository<Rental, Long>, JpaSpecificationExecutor<Rental> {
    List<Rental> findAllByInventory_Id(Long inventoryId);
    Long countAllByReturnDateIsNullAndInventory_IdIn(Set<Long> inventoryIds);
    Long countAllByReturnDateIsNull();
    List<Rental> findAllByReturnDateIsNullAndInventory_IdIn(Set<Long> inventoryIds);
    List<Rental> findAllByInventory_IdIn(Set<Long> inventoryIds);
    Long countAllByPatron_Id(Long patronId);
}
