package com.stas.tourManager.backend.persistance.pojos;

import java.util.ArrayList;
import java.util.List;

public class Car {
    private static List<String> cars = new ArrayList<>();
    private String car;

    private Car(String car) {
        this.car = car;
    }

    public static String createCar(String car){
        cars.add(car);
        return car;
    }

    public String getCar() {
        return car;
    }

    public static List<String> getCars() {
        return cars;
    }


}
