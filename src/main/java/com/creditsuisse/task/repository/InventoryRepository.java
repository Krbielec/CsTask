package com.creditsuisse.task.repository;

import com.creditsuisse.task.domain.Inventory;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Inventory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long>, JpaSpecificationExecutor<Inventory> {
    List<Inventory> findAllByBook_Id(Long bookId);
}
