package com.creditsuisse.task.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.creditsuisse.task.domain.Inventory} entity. This class is used
 * in {@link com.creditsuisse.task.web.rest.InventoryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /inventories?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class InventoryCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter bookId;

    public InventoryCriteria() {}

    public InventoryCriteria(InventoryCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.bookId = other.bookId == null ? null : other.bookId.copy();
    }

    @Override
    public InventoryCriteria copy() {
        return new InventoryCriteria(this);
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

    public LongFilter getBookId() {
        return bookId;
    }

    public LongFilter bookId() {
        if (bookId == null) {
            bookId = new LongFilter();
        }
        return bookId;
    }

    public void setBookId(LongFilter bookId) {
        this.bookId = bookId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final InventoryCriteria that = (InventoryCriteria) o;
        return Objects.equals(id, that.id) && Objects.equals(bookId, that.bookId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, bookId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InventoryCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (bookId != null ? "bookId=" + bookId + ", " : "") +
            "}";
    }
}
