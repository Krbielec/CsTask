package com.creditsuisse.task.service;

import com.creditsuisse.task.domain.*; // for static metamodels
import com.creditsuisse.task.domain.Patron;
import com.creditsuisse.task.repository.PatronRepository;
import com.creditsuisse.task.service.criteria.PatronCriteria;
import com.creditsuisse.task.service.dto.PatronDTO;
import com.creditsuisse.task.service.mapper.PatronMapper;
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
 * Service for executing complex queries for {@link Patron} entities in the database.
 * The main input is a {@link PatronCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PatronDTO} or a {@link Page} of {@link PatronDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PatronQueryService extends QueryService<Patron> {

    private final Logger log = LoggerFactory.getLogger(PatronQueryService.class);

    private final PatronRepository patronRepository;

    private final PatronMapper patronMapper;

    public PatronQueryService(PatronRepository patronRepository, PatronMapper patronMapper) {
        this.patronRepository = patronRepository;
        this.patronMapper = patronMapper;
    }

    /**
     * Return a {@link List} of {@link PatronDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PatronDTO> findByCriteria(PatronCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Patron> specification = createSpecification(criteria);
        return patronMapper.toDto(patronRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PatronDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PatronDTO> findByCriteria(PatronCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Patron> specification = createSpecification(criteria);
        return patronRepository.findAll(specification, page).map(patronMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PatronCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Patron> specification = createSpecification(criteria);
        return patronRepository.count(specification);
    }

    /**
     * Function to convert {@link PatronCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Patron> createSpecification(PatronCriteria criteria) {
        Specification<Patron> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Patron_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Patron_.name));
            }
            if (criteria.getDateOfBirth() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateOfBirth(), Patron_.dateOfBirth));
            }
            if (criteria.getPhoneNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNumber(), Patron_.phoneNumber));
            }
        }
        return specification;
    }
}
