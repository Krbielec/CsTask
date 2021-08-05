package com.creditsuisse.task.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Rental.
 */
@Entity
@Table(name = "rental")
public class Rental implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "rental_date", nullable = false)
    private LocalDate rentalDate;

    @Column(name = "return_date")
    private LocalDate returnDate;

    @ManyToOne
    @NotNull
    private Patron patron;

    @ManyToOne
    @NotNull
    @JsonIgnoreProperties(value = { "book" }, allowSetters = true)
    private Inventory inventory;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Rental id(Long id) {
        this.id = id;
        return this;
    }

    public LocalDate getRentalDate() {
        return this.rentalDate;
    }

    public Rental rentalDate(LocalDate rentalDate) {
        this.rentalDate = rentalDate;
        return this;
    }

    public void setRentalDate(LocalDate rentalDate) {
        this.rentalDate = rentalDate;
    }

    public LocalDate getReturnDate() {
        return this.returnDate;
    }

    public Rental returnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
        return this;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public Patron getPatron() {
        return this.patron;
    }

    public Rental patron(Patron patron) {
        this.setPatron(patron);
        return this;
    }

    public void setPatron(Patron patron) {
        this.patron = patron;
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    public Rental inventory(Inventory inventory) {
        this.setInventory(inventory);
        return this;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Rental)) {
            return false;
        }
        return id != null && id.equals(((Rental) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Rental{" +
            "id=" + getId() +
            ", rentalDate='" + getRentalDate() + "'" +
            ", returnDate='" + getReturnDate() + "'" +
            "}";
    }
}
