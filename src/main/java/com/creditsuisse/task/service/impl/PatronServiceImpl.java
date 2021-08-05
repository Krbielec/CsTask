package com.creditsuisse.task.service.impl;

import com.creditsuisse.task.domain.Patron;
import com.creditsuisse.task.repository.PatronRepository;
import com.creditsuisse.task.service.PatronService;
import com.creditsuisse.task.service.dto.PatronDTO;
import com.creditsuisse.task.service.mapper.PatronMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Patron}.
 */
@Service
@Transactional
public class PatronServiceImpl implements PatronService {

    private final Logger log = LoggerFactory.getLogger(PatronServiceImpl.class);

    private final PatronRepository patronRepository;

    private final PatronMapper patronMapper;

    public PatronServiceImpl(PatronRepository patronRepository, PatronMapper patronMapper) {
        this.patronRepository = patronRepository;
        this.patronMapper = patronMapper;
    }

    @Override
    public PatronDTO save(PatronDTO patronDTO) {
        log.debug("Request to save Patron : {}", patronDTO);
        Patron patron = patronMapper.toEntity(patronDTO);
        patron = patronRepository.save(patron);
        return patronMapper.toDto(patron);
    }

    @Override
    public Optional<PatronDTO> partialUpdate(PatronDTO patronDTO) {
        log.debug("Request to partially update Patron : {}", patronDTO);

        return patronRepository
            .findById(patronDTO.getId())
            .map(
                existingPatron -> {
                    patronMapper.partialUpdate(existingPatron, patronDTO);
                    return existingPatron;
                }
            )
            .map(patronRepository::save)
            .map(patronMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PatronDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Patrons");
        return patronRepository.findAll(pageable).map(patronMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PatronDTO> findOne(Long id) {
        log.debug("Request to get Patron : {}", id);
        return patronRepository.findById(id).map(patronMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Patron : {}", id);
        patronRepository.deleteById(id);
    }
}
