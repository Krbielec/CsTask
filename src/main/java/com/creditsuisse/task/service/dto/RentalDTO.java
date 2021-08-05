package com.creditsuisse.task.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.creditsuisse.task.domain.Rental} entity.
 */
public class RentalDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate rentalDate;

    private LocalDate returnDate;

    @NotNull
    private PatronDTO patron;

    @NotNull
    private InventoryDTO inventory;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getRentalDate() {
        return rentalDate;
    }

    public void setRentalDate(LocalDate rentalDate) {
        this.rentalDate = rentalDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public PatronDTO getPatron() {
        return patron;
    }

    public void setPatron(PatronDTO patron) {
        this.patron = patron;
    }

    public InventoryDTO getInventory() {
        return inventory;
    }

    public void setInventory(InventoryDTO inventory) {
        this.inventory = inventory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RentalDTO)) {
            return false;
        }

        RentalDTO rentalDTO = (RentalDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, rentalDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RentalDTO{" +
            "id=" + getId() +
            ", rentalDate='" + getRentalDate() + "'" +
            ", returnDate='" + getReturnDate() + "'" +
            ", patron=" + getPatron() +
            ", inventory=" + getInventory() +
            "}";
    }
}
