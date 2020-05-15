package com.stas.tourManager.backend.persistance.services;

import com.github.javafaker.Faker;
import com.stas.tourManager.backend.persistance.pojos.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
public class DriverService {
    protected static List<Driver> drivers = new ArrayList<>();
    private Logger logger = LoggerFactory.getLogger(DriverService.class);

    public void addDriver(String firstName, String lastName, Car car) {
        var driver = new Driver(firstName, lastName).withCar(car);
        drivers.add(driver);
        logger.info("created new driver: " + driver.toString());
    }

    public void addDriver(String firstName, String lastName) {
        var driver = new Driver(firstName, lastName);
        drivers.add(driver);
        logger.info("created new driver: " + driver.toString());
    }

    public void saveOrUpdate(Driver driver){
        if(existDriver(driver.getId()))
            updateDriver(driver.getId(), driver.getFirstName(), driver.getMiddleName(), driver.getLastName(), driver.getCar());
        else
            addDriver(driver);
    }

    public void addDriver(Driver driver) {
        drivers.add(driver);
        logger.info("created new driver: " + driver.toString());
    }

    public void addDriver(Driver... drivers) {
        this.drivers.addAll(Arrays.asList(drivers));
        for (Driver driver : drivers) {
            logger.info("created new driver: " + driver.toString());
        }
    }

    /**
     * Delete driver by id.
     *
     * @param id unique driver identifier.
     * @return true if deleted successfully.
     */
    public boolean deleteDriver(long id) {
        var deleted = drivers.removeIf(g -> g.getId() == id);
        if (deleted)
            logger.info("deleted driver: " + id);
        return deleted;
    }

    /**
     * Delete driver by full name.
     *
     * @param fullName full name of driver. Example: Stanislav Wong. Case insensitive.
     * @return true if deleted successfully.
     * @implNote use only if exist only 1 driver with given name. Otherwise will be deleted first in list.
     */
    public boolean deleteDriver(String fullName) {
        var deleted = drivers.removeIf(g -> g.getFullName().equalsIgnoreCase(fullName));
        if (deleted)
            logger.info("deleted driver: " + fullName);
        return deleted;
    }

    public boolean deleteDriver(Driver driver) {
        var deleted = drivers.remove(driver);
        if (deleted)
            logger.info("deleted driver: " + driver.toString());
        return deleted;
    }

    public Optional<Driver> getDriver(long id) {
        return drivers.stream().filter(g -> g.getId() == id).findAny();
    }

    public Optional<Driver> getDriver(String fullName) {
        return drivers.stream().filter(g -> g.getFullName().equalsIgnoreCase(fullName)).findAny();
    }

    /**
     * @// FIXME: 4/28/20 logging doesn't shows old data.
     */
    public void updateDriver(long id, String firstName, String middleName, String lastName, Car car) {
        var logMessage = "update driver. Old data: ";
        var oldDriver = getDriver(id);
        if (oldDriver != null && oldDriver.isPresent() && !oldDriver.isEmpty()) {
            var og = oldDriver.get();
            logMessage += oldDriver.get().toStringFull() + ". New data: ";
            deleteDriver(id);

            if (firstName != null && !firstName.isEmpty() && !firstName.isBlank())
                og.setFirstName(firstName);
            if (middleName != null && !middleName.isEmpty() && !middleName.isBlank())
                og.setMiddleName(middleName);
            if (lastName != null && !lastName.isEmpty() && !lastName.isBlank())
                og.setLastName(lastName);
            if (car != null)
                og.setCar(car);

            if (middleName != null && !middleName.isEmpty())
                og.setFullName(String.format("%s %s %s", firstName, middleName, lastName));
            else
                og.setFullName(String.format("%s %s", firstName, lastName));

            addDriver(og);
            logMessage += og.toStringFull();
            logger.info(logMessage);
        }
    }

    public boolean existDriver(long id) {
        return drivers.stream().anyMatch(g -> g.getId() == id);
    }

    public boolean existDriver(String fullName) {
        return drivers.stream().anyMatch(g -> g.getFullName().equalsIgnoreCase(fullName));
    }

    @PostConstruct
    public void init() {
        Faker faker = new Faker();
        for (int i = 0; i < 10; i++) {
            addDriver(faker.name().firstName(), faker.name().lastName());
        }
        logger.debug("init driver complete");
    }

    /**
     * @return ascending sorted list by driver full name.
     */
    public List<Driver> getDrivers() {
        Comparator<Driver> compareByFullName = new Comparator<Driver>() {
            @Override
            public int compare(Driver o1, Driver o2) {
                return o1.getFullName().compareTo(o2.getFullName());
            }
        };
        drivers.sort(compareByFullName);
        return drivers;
    }

    public static Driver getRandomDriver(){
        return drivers.get(new Random().nextInt(drivers.size()));
    }
}
