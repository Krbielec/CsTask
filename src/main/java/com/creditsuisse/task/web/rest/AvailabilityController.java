package com.creditsuisse.task.web.rest;

import com.creditsuisse.task.service.AvailabilityQueryService;
import com.creditsuisse.task.service.criteria.AvailabilityQueryCriteria;
import com.creditsuisse.task.service.criteria.RentalCriteria;
import com.creditsuisse.task.service.dto.RentalDTO;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;

@RestController
@RequestMapping("/api")
public class AvailabilityController {

    private final Logger log = LoggerFactory.getLogger(AvailabilityController.class);

    private final AvailabilityQueryService availabilityQueryService;

    public AvailabilityController(AvailabilityQueryService availabilityQueryService) {
        this.availabilityQueryService = availabilityQueryService;
    }

    @GetMapping("/availability")
    public ResponseEntity<Long> getAvailability(AvailabilityQueryCriteria criteria) {
        log.debug("REST request to get availability by criteria: {}", criteria);
        return ResponseEntity.ok().body(availabilityQueryService.countAvailability(criteria.getBookId()));
    }
}
