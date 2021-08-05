package com.creditsuisse.task.service;

import com.creditsuisse.task.domain.Rental;
import com.creditsuisse.task.repository.RentalRepository;
import com.creditsuisse.task.service.exception.ItemAlreadyRentException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class InventoryValidator {

    private final RentalRepository rentalRepository;

    public InventoryValidator(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    public void assertIsReturned(Long inventoryId, Long rentalId) {
        final List<Rental> rentalsOfInventory = rentalRepository.findAllByInventory_Id(inventoryId);
        if (rentalsOfInventory.isEmpty() || isRentalUpdate(rentalsOfInventory, rentalId) || isReturned(rentalsOfInventory)) {
            return;
        }
        throw new ItemAlreadyRentException();
    }

    private boolean isRentalUpdate(List<Rental> rentalsOfInventory, Long rentalId) {
        final long underRentCounter = rentalsOfInventory.stream().filter(rental -> rental.getReturnDate() == null).count();

        return underRentCounter == 1 && getRentalId(rentalsOfInventory).equals(rentalId);
    }

    private Long getRentalId(List<Rental> rentalsOfInventory) {
        return rentalsOfInventory.stream().filter(rental -> rental.getReturnDate() == null).findFirst().get().getId();
    }

    private boolean isReturned(List<Rental> rentalsOfInventory) {
        return rentalsOfInventory.stream().allMatch(rental -> rental.getReturnDate() != null);
    }
}
