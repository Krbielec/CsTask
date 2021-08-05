package com.creditsuisse.task.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.creditsuisse.task.domain.Rental} entity. This class is used
 * in {@link com.creditsuisse.task.web.rest.RentalResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /rentals?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class RentalCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter rentalDate;

    private LocalDateFilter returnDate;

    private LongFilter patronId;

    private LongFilter inventoryId;

    public RentalCriteria() {}

    public RentalCriteria(RentalCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.rentalDate = other.rentalDate == null ? null : other.rentalDate.copy();
        this.returnDate = other.returnDate == null ? null : other.returnDate.copy();
        this.patronId = other.patronId == null ? null : other.patronId.copy();
        this.inventoryId = other.inventoryId == null ? null : other.inventoryId.copy();
    }

    @Override
    public RentalCriteria copy() {
        return new RentalCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LocalDateFilter getRentalDate() {
        return rentalDate;
    }

    public LocalDateFilter rentalDate() {
        if (rentalDate == null) {
            rentalDate = new LocalDateFilter();
        }
        return rentalDate;
    }

    public void setRentalDate(LocalDateFilter rentalDate) {
        this.rentalDate = rentalDate;
    }

    public LocalDateFilter getReturnDate() {
        return returnDate;
    }

    public LocalDateFilter returnDate() {
        if (returnDate == null) {
            returnDate = new LocalDateFilter();
        }
        return returnDate;
    }

    public void setReturnDate(LocalDateFilter returnDate) {
        this.returnDate = returnDate;
    }

    public LongFilter getPatronId() {
        return patronId;
    }

    public LongFilter patronId() {
        if (patronId == null) {
            patronId = new LongFilter();
        }
        return patronId;
    }

    public void setPatronId(LongFilter patronId) {
        this.patronId = patronId;
    }

    public LongFilter getInventoryId() {
        return inventoryId;
    }

    public LongFilter inventoryId() {
        if (inventoryId == null) {
            inventoryId = new LongFilter();
        }
        return inventoryId;
    }

    public void setInventoryId(LongFilter inventoryId) {
        this.inventoryId = inventoryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final RentalCriteria that = (RentalCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(rentalDate, that.rentalDate) &&
            Objects.equals(returnDate, that.returnDate) &&
            Objects.equals(patronId, that.patronId) &&
            Objects.equals(inventoryId, that.inventoryId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, rentalDate, returnDate, patronId, inventoryId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RentalCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (rentalDate != null ? "rentalDate=" + rentalDate + ", " : "") +
            (returnDate != null ? "returnDate=" + returnDate + ", " : "") +
            (patronId != null ? "patronId=" + patronId + ", " : "") +
            (inventoryId != null ? "inventoryId=" + inventoryId + ", " : "") +
            "}";
    }
}
