package com.creditsuisse.task.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.creditsuisse.task.domain.Inventory} entity.
 */
public class InventoryDTO implements Serializable {

    private Long id;

    private BookDTO book;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BookDTO getBook() {
        return book;
    }

    public void setBook(BookDTO book) {
        this.book = book;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InventoryDTO)) {
            return false;
        }

        InventoryDTO inventoryDTO = (InventoryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, inventoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InventoryDTO{" +
            "id=" + getId() +
            ", book=" + getBook() +
            "}";
    }
}
