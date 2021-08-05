package com.creditsuisse.task.service;

import com.creditsuisse.task.domain.Inventory;
import com.creditsuisse.task.domain.Rental;
import com.creditsuisse.task.repository.InventoryRepository;
import com.creditsuisse.task.repository.RentalRepository;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AvailabilityQueryService {

    private final RentalRepository rentalRepository;
    private final InventoryRepository inventoryRepository;

    public AvailabilityQueryService(RentalRepository rentalRepository, InventoryRepository inventoryRepository) {
        this.rentalRepository = rentalRepository;
        this.inventoryRepository = inventoryRepository;
    }

    public Long countAvailability(Long bookId) {
        final List<Inventory> inventoryItems = getInventoryItems(bookId);
        if (inventoryItems.isEmpty()) {
            return 0L;
        }
        final Set<Long> inventoryIds = inventoryItems.stream().map(Inventory::getId).collect(Collectors.toSet());

        final List<Rental> currentlyRentList = rentalRepository.findAllByReturnDateIsNullAndInventory_IdIn(inventoryIds);
        if (currentlyRentList.isEmpty()) {
            return Integer.toUnsignedLong(inventoryIds.size());
        }
        final Set<Long> rentInventory = currentlyRentList.stream().map(rental -> rental.getInventory().getId()).collect(Collectors.toSet());

        return inventoryIds.stream().filter(inventoryId -> !rentInventory.contains(inventoryId)).count();
    }

    private List<Inventory> getInventoryItems(Long bookId) {
        return bookId == null ? inventoryRepository.findAll() : inventoryRepository.findAllByBook_Id(bookId);
    }
}
