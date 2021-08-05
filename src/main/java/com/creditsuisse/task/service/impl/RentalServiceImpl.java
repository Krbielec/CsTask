package com.creditsuisse.task.service.impl;

import com.creditsuisse.task.domain.Rental;
import com.creditsuisse.task.repository.RentalRepository;
import com.creditsuisse.task.service.InventoryValidator;
import com.creditsuisse.task.service.RentalService;
import com.creditsuisse.task.service.dto.RentalDTO;
import com.creditsuisse.task.service.mapper.RentalMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Rental}.
 */
@Service
@Transactional
public class RentalServiceImpl implements RentalService {

    private final Logger log = LoggerFactory.getLogger(RentalServiceImpl.class);

    private final RentalRepository rentalRepository;

    private final RentalMapper rentalMapper;

    private final InventoryValidator inventoryValidator;

    public RentalServiceImpl(RentalRepository rentalRepository, RentalMapper rentalMapper, InventoryValidator inventoryValidator) {
        this.rentalRepository = rentalRepository;
        this.rentalMapper = rentalMapper;
        this.inventoryValidator = inventoryValidator;
    }

    @Override
    public RentalDTO save(RentalDTO rentalDTO) {
        log.debug("Request to save Rental : {}", rentalDTO);
        Rental rental = rentalMapper.toEntity(rentalDTO);
        inventoryValidator.assertIsReturned(rentalDTO.getInventory().getId(), rentalDTO.getId());
        rental = rentalRepository.save(rental);
        return rentalMapper.toDto(rental);
    }

    @Override
    public Optional<RentalDTO> partialUpdate(RentalDTO rentalDTO) {
        log.debug("Request to partially update Rental : {}", rentalDTO);

        return rentalRepository
            .findById(rentalDTO.getId())
            .map(
                existingRental -> {
                    rentalMapper.partialUpdate(existingRental, rentalDTO);
                    return existingRental;
                }
            )
            .map(rentalRepository::save)
            .map(rentalMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RentalDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Rentals");
        return rentalRepository.findAll(pageable).map(rentalMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RentalDTO> findOne(Long id) {
        log.debug("Request to get Rental : {}", id);
        return rentalRepository.findById(id).map(rentalMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Rental : {}", id);
        rentalRepository.deleteById(id);
    }
}
