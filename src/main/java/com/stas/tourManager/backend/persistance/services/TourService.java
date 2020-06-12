package com.stas.tourManager.backend.persistance.services;

import com.github.javafaker.Faker;
import com.stas.tourManager.backend.persistance.pojos.Tour;
import org.joda.time.Interval;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class TourService {
    private Logger log = LoggerFactory.getLogger(TourService.class);
    private List<Tour> tours = new ArrayList<>();

    public void add(Tour tour) {
        tours.add(tour);
        log.info("create new tour: " + tour.toString());
    }

    public List<Tour> getAll(){
        return tours;
    }

    public Tour get(long id) {
        return tours.stream().filter(tour -> tour.getId() == id).findAny().get();
    }

    public boolean delete(Tour tour) {
        log.info("delete tour: " + tour.toString());
        return tours.remove(tour);
    }

    public boolean exist(Tour tour) {
        return tours.stream().anyMatch(t -> t.equals(tour));
    }

    public void update(Tour newTour) {
        for (int i = 0; i < tours.size(); i++) {
            if (tours.get(i).getId() == newTour.getId()) {
                tours.set(i, newTour);
                break;
            }
        }
    }

    @PostConstruct
    public void init() {
        var faker = new Faker();
        for (int i = 0; i < 100; i++) {
            var tour = new Tour();
            tour.setTitle(faker.book().title());
            tour.setDescription(faker.yoda().quote());
            tour.setDate(new Interval(LocalDateTime.now().minusDays(10).toDateTime(), LocalDateTime.now().toDateTime()));
            tour.addDriver(DriverService.getRandomDriver());
            tour.addGuide(GuideService.getRandomGuide());
            tour.setFile(faker.file().fileName());
            add(tour);
        }
        log.debug("init tours complete");
    }
}
