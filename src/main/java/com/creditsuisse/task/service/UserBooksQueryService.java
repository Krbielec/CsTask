package com.creditsuisse.task.service;

import com.creditsuisse.task.domain.Patron;
import com.creditsuisse.task.repository.PatronRepository;
import com.creditsuisse.task.repository.RentalRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserBooksQueryService {

    private final PatronRepository patronRepository;
    private final RentalRepository rentalRepository;

    public UserBooksQueryService(PatronRepository patronRepository, RentalRepository rentalRepository) {
        this.patronRepository = patronRepository;
        this.rentalRepository = rentalRepository;
    }

    public Long countRentals(Long patronId) {
        if (patronId == null) {
            throw new RuntimeException();
        }

        final Optional<Patron> byId = patronRepository.findById(patronId);
        if (!byId.isPresent()) {
            return 0L;
        }

        return rentalRepository.countAllByPatron_Id(patronId);
    }
}
