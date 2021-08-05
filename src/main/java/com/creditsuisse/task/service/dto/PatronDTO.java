package com.creditsuisse.task.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.creditsuisse.task.domain.Patron} entity.
 */
public class PatronDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private LocalDate dateOfBirth;

    @NotNull
    private String phoneNumber;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PatronDTO)) {
            return false;
        }

        PatronDTO patronDTO = (PatronDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, patronDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PatronDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", dateOfBirth='" + getDateOfBirth() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            "}";
    }
}
