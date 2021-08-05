package com.creditsuisse.task.repository;

import com.creditsuisse.task.domain.Patron;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Patron entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PatronRepository extends JpaRepository<Patron, Long>, JpaSpecificationExecutor<Patron> {}
