package com.creditsuisse.task.web.rest;

import com.creditsuisse.task.repository.PatronRepository;
import com.creditsuisse.task.service.PatronQueryService;
import com.creditsuisse.task.service.PatronService;
import com.creditsuisse.task.service.UserBooksQueryService;
import com.creditsuisse.task.service.criteria.PatronCriteria;
import com.creditsuisse.task.service.dto.PatronDTO;
import com.creditsuisse.task.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.creditsuisse.task.domain.Patron}.
 */
@RestController
@RequestMapping("/api")
public class PatronResource {

    private final Logger log = LoggerFactory.getLogger(PatronResource.class);

    private static final String ENTITY_NAME = "patron";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PatronService patronService;

    private final PatronRepository patronRepository;

    private final PatronQueryService patronQueryService;

    private final UserBooksQueryService userBooksQueryService;

    public PatronResource(
        PatronService patronService,
        PatronRepository patronRepository,
        PatronQueryService patronQueryService,
        UserBooksQueryService userBooksQueryService
    ) {
        this.patronService = patronService;
        this.patronRepository = patronRepository;
        this.patronQueryService = patronQueryService;
        this.userBooksQueryService = userBooksQueryService;
    }

    /**
     * {@code POST  /patrons} : Create a new patron.
     *
     * @param patronDTO the patronDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new patronDTO, or with status {@code 400 (Bad Request)} if the patron has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/patrons")
    public ResponseEntity<PatronDTO> createPatron(@Valid @RequestBody PatronDTO patronDTO) throws URISyntaxException {
        log.debug("REST request to save Patron : {}", patronDTO);
        if (patronDTO.getId() != null) {
            throw new BadRequestAlertException("A new patron cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PatronDTO result = patronService.save(patronDTO);
        return ResponseEntity
            .created(new URI("/api/patrons/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /patrons/:id} : Updates an existing patron.
     *
     * @param id the id of the patronDTO to save.
     * @param patronDTO the patronDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated patronDTO,
     * or with status {@code 400 (Bad Request)} if the patronDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the patronDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/patrons/{id}")
    public ResponseEntity<PatronDTO> updatePatron(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PatronDTO patronDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Patron : {}, {}", id, patronDTO);
        if (patronDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, patronDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!patronRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PatronDTO result = patronService.save(patronDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, patronDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /patrons/:id} : Partial updates given fields of an existing patron, field will ignore if it is null
     *
     * @param id the id of the patronDTO to save.
     * @param patronDTO the patronDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated patronDTO,
     * or with status {@code 400 (Bad Request)} if the patronDTO is not valid,
     * or with status {@code 404 (Not Found)} if the patronDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the patronDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/patrons/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<PatronDTO> partialUpdatePatron(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PatronDTO patronDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Patron partially : {}, {}", id, patronDTO);
        if (patronDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, patronDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!patronRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PatronDTO> result = patronService.partialUpdate(patronDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, patronDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /patrons} : get all the patrons.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of patrons in body.
     */
    @GetMapping("/patrons")
    public ResponseEntity<List<PatronDTO>> getAllPatrons(PatronCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Patrons by criteria: {}", criteria);
        Page<PatronDTO> page = patronQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /patrons/count} : count all the patrons.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/patrons/count")
    public ResponseEntity<Long> countPatrons(PatronCriteria criteria) {
        log.debug("REST request to count Patrons by criteria: {}", criteria);
        return ResponseEntity.ok().body(patronQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /patrons/:id} : get the "id" patron.
     *
     * @param id the id of the patronDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the patronDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/patrons/{id}")
    public ResponseEntity<PatronDTO> getPatron(@PathVariable Long id) {
        log.debug("REST request to get Patron : {}", id);
        Optional<PatronDTO> patronDTO = patronService.findOne(id);
        return ResponseUtil.wrapOrNotFound(patronDTO);
    }

    @GetMapping("/patrons/{id}/books")
    public ResponseEntity<Long> countPatronsBooks(@PathVariable Long id) {
        log.debug("REST request to count books rent by an user: {}", id);
        return ResponseEntity.ok().body(userBooksQueryService.countRentals(id));
    }

    /**
     * {@code DELETE  /patrons/:id} : delete the "id" patron.
     *
     * @param id the id of the patronDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/patrons/{id}")
    public ResponseEntity<Void> deletePatron(@PathVariable Long id) {
        log.debug("REST request to delete Patron : {}", id);
        patronService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
