package com.stas.tourManager.backend.persistance.services;

import java.util.ArrayList;
import java.util.List;

public abstract class CarService {
    private static List<String> carsList = new ArrayList<>();

    public static String createCar(String car) {
        carsList.add(car);
        return car;
    }

    public static List<String> getCarsList() {
        return carsList;
    }
}
