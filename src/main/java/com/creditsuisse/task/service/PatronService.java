package com.creditsuisse.task.service;

import com.creditsuisse.task.service.dto.PatronDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.creditsuisse.task.domain.Patron}.
 */
public interface PatronService {
    /**
     * Save a patron.
     *
     * @param patronDTO the entity to save.
     * @return the persisted entity.
     */
    PatronDTO save(PatronDTO patronDTO);

    /**
     * Partially updates a patron.
     *
     * @param patronDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PatronDTO> partialUpdate(PatronDTO patronDTO);

    /**
     * Get all the patrons.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PatronDTO> findAll(Pageable pageable);

    /**
     * Get the "id" patron.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PatronDTO> findOne(Long id);

    /**
     * Delete the "id" patron.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
