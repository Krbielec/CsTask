package com.creditsuisse.task.service;

import com.creditsuisse.task.domain.*; // for static metamodels
import com.creditsuisse.task.domain.Rental;
import com.creditsuisse.task.repository.RentalRepository;
import com.creditsuisse.task.service.criteria.RentalCriteria;
import com.creditsuisse.task.service.dto.RentalDTO;
import com.creditsuisse.task.service.mapper.RentalMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Rental} entities in the database.
 * The main input is a {@link RentalCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RentalDTO} or a {@link Page} of {@link RentalDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RentalQueryService extends QueryService<Rental> {

    private final Logger log = LoggerFactory.getLogger(RentalQueryService.class);

    private final RentalRepository rentalRepository;

    private final RentalMapper rentalMapper;

    public RentalQueryService(RentalRepository rentalRepository, RentalMapper rentalMapper) {
        this.rentalRepository = rentalRepository;
        this.rentalMapper = rentalMapper;
    }

    /**
     * Return a {@link List} of {@link RentalDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RentalDTO> findByCriteria(RentalCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Rental> specification = createSpecification(criteria);
        return rentalMapper.toDto(rentalRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link RentalDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RentalDTO> findByCriteria(RentalCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Rental> specification = createSpecification(criteria);
        return rentalRepository.findAll(specification, page).map(rentalMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RentalCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Rental> specification = createSpecification(criteria);
        return rentalRepository.count(specification);
    }

    /**
     * Function to convert {@link RentalCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Rental> createSpecification(RentalCriteria criteria) {
        Specification<Rental> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Rental_.id));
            }
            if (criteria.getRentalDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRentalDate(), Rental_.rentalDate));
            }
            if (criteria.getReturnDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReturnDate(), Rental_.returnDate));
            }
            if (criteria.getPatronId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getPatronId(), root -> root.join(Rental_.patron, JoinType.LEFT).get(Patron_.id))
                    );
            }
            if (criteria.getInventoryId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getInventoryId(),
                            root -> root.join(Rental_.inventory, JoinType.LEFT).get(Inventory_.id)
                        )
                    );
            }
        }
        return specification;
    }

    @Transactional(readOnly = true)
    public Long countAvailability() {
        log.debug("count by criteria : {}");
        return rentalRepository.countAllByReturnDateIsNull();
    }
}
