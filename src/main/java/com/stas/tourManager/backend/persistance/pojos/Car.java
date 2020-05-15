package com.stas.tourManager.backend.persistance.pojos;

import java.util.ArrayList;
import java.util.List;

public class Car {
    private static List<Car> cars = new ArrayList<>();
    private String car;

    private Car(String car) {
        this.car = car;
    }

    public static Car createCar(String car){
        var c = new Car(car);
        cars.add(c);
        return c;
    }

    public String getCar() {
        return car;
    }

    public static List<Car> getCars() {
        return cars;
    }


}
