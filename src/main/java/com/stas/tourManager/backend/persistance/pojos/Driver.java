package com.stas.tourManager.backend.persistance.pojos;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class Driver extends Participant {

    private String car;

    public Driver() {
        super();
    }

    public Driver(@NotNull String firstName, @NotNull String lastName) {
        super(firstName, lastName);
    }

    public Driver(@NotNull String firstName, String middleName, @NotNull String lastName) {
        super(firstName, middleName, lastName);
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Driver)) return false;
        if (!super.equals(o)) return false;
        Driver driver = (Driver) o;
        return Objects.equals(getCar(), driver.getCar());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getCar());
    }

    @Override
    public String toString() {
        return "Driver{" +
                "car='" + car + '\'' +
                ", fullName='" + fullName + '\'' +
                ", id=" + id +
                '}';
    }

    public String toStringFull() {
        return "Driver{" +
                "car='" + car + '\'' +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", fullName='" + fullName + '\'' +
                ", id=" + id +
                '}';
    }

    public Driver withCar(String car) {
        this.setCar(car);
        return this;
    }
}
