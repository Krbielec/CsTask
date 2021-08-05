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
 * Criteria class for the {@link com.creditsuisse.task.domain.Patron} entity. This class is used
 * in {@link com.creditsuisse.task.web.rest.PatronResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /patrons?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PatronCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private LocalDateFilter dateOfBirth;

    private StringFilter phoneNumber;

    public PatronCriteria() {}

    public PatronCriteria(PatronCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.dateOfBirth = other.dateOfBirth == null ? null : other.dateOfBirth.copy();
        this.phoneNumber = other.phoneNumber == null ? null : other.phoneNumber.copy();
    }

    @Override
    public PatronCriteria copy() {
        return new PatronCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public LocalDateFilter getDateOfBirth() {
        return dateOfBirth;
    }

    public LocalDateFilter dateOfBirth() {
        if (dateOfBirth == null) {
            dateOfBirth = new LocalDateFilter();
        }
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDateFilter dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public StringFilter getPhoneNumber() {
        return phoneNumber;
    }

    public StringFilter phoneNumber() {
        if (phoneNumber == null) {
            phoneNumber = new StringFilter();
        }
        return phoneNumber;
    }

    public void setPhoneNumber(StringFilter phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PatronCriteria that = (PatronCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(dateOfBirth, that.dateOfBirth) &&
            Objects.equals(phoneNumber, that.phoneNumber)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, dateOfBirth, phoneNumber);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PatronCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (dateOfBirth != null ? "dateOfBirth=" + dateOfBirth + ", " : "") +
            (phoneNumber != null ? "phoneNumber=" + phoneNumber + ", " : "") +
            "}";
    }
}
