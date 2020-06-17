package com.stas.tourManager.backend.persistance.services;

import com.github.javafaker.Faker;
import com.stas.tourManager.backend.persistance.pojos.Tour;
import org.joda.time.Interval;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class TourService {
    private Logger log = LoggerFactory.getLogger(TourService.class);
    private List<Tour> tours = new ArrayList<>();

    public void saveOrUpdate(Tour tour) {
        if(!exist(tour)){
            tours.add(tour);
            log.info("create new tour: " + tour.toString());
        }else{
            update(tour);
        }
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
            var oldTour = tours.get(i);
            if (oldTour.getId() == newTour.getId()) {
                log.info("update tour: "+oldTour.getTitle()+"\nold data: "+oldTour.toString()+"\nnew data: " + newTour.toString());
                tours.set(i, newTour);
                break;
            }
        }
    }

    @PostConstruct
    public void init() {
        var faker = new Faker();
        for (int i = 0; i < 10; i++) {
            var tour = new Tour();
            tour.setTitle(faker.book().title());
            tour.setDescription(faker.yoda().quote());
            tour.setDate(new Interval(LocalDateTime.now().minusDays(ThreadLocalRandom.current().nextInt(1, 15)).toDateTime(),
                    LocalDateTime.now().plusDays(ThreadLocalRandom.current().nextInt(1, 15)).toDateTime()));
            tour.addDriver(DriverService.getRandomDriver());
            tour.addGuide(GuideService.getRandomGuide());
            tour.setFile(faker.file().fileName());
            saveOrUpdate(tour);
        }
        log.debug("init tours complete");
    }
}
