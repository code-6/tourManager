package com.stas.tourManager.backend.persistance.pojos;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;

public abstract class Participant extends AbstractEntity {
    @NotNull
    @NotEmpty
    protected String firstName;
    protected String middleName;
    @NotNull
    @NotEmpty
    protected String lastName;
    protected String fullName;

    public Participant() {
        super();
    }

    public Participant(@NotNull String firstName, @NotNull String lastName) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        fullName = String.format("%s %s", firstName, lastName);
    }

    public Participant(@NotNull String firstName, String middleName, @NotNull String lastName) {
        super();
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;

        if (middleName != null && !middleName.isEmpty())
            fullName = String.format("%s %s %s", firstName, middleName, lastName);
        else
            fullName = String.format("%s %s", firstName, lastName);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Participant)) return false;
        if (!super.equals(o)) return false;
        Participant that = (Participant) o;
        return getFirstName().equals(that.getFirstName()) &&
                Objects.equals(getMiddleName(), that.getMiddleName()) &&
                getLastName().equals(that.getLastName()) &&
                getFullName().equals(that.getFullName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getFirstName(), getMiddleName(), getLastName(), getFullName());
    }

    @Override
    public String toString() {
        return "Participant{" +
                "firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", fullName='" + fullName + '\'' +
                ", id=" + id +
                '}';
    }
}
