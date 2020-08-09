package com.stas.tourManager.backend.persistance.services;

import com.github.javafaker.Faker;
import com.stas.tourManager.backend.persistance.pojos.Tour;
import com.stas.tourManager.frontend.views.ListToursView;
import org.joda.time.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class TourService {
    private Logger log = LoggerFactory.getLogger(TourService.class);
    private List<Tour> tours = new ArrayList<>();

    public void saveOrUpdate(Tour tour) {
        if(!exist(tour)){
            tours.add(tour);
            createFolder(tour);
            log.info("create new tour: " + tour.toString());
        }else update(tour);

    }

    public List<Tour> getAll(){
        return tours;
    }

    public List<Tour> getByMonthAndYear(int month, int year){
        var list = tours.stream().filter(t -> (t.getFrom().getMonthOfYear() == month && t.getFrom().getYear() == year)
                || (t.getTo().getMonthOfYear() == month && t.getTo().getYear() == year)).sorted(new SortByStartDate())
                .collect(Collectors.toList());
        list.forEach(t-> System.out.println(t.getDateAsString(ListToursView.DATE_TIME_FORMAT, Locale.US)));
        return list;
    }

    public Tour get(long id) {
        return tours.stream().filter(tour -> tour.getId() == id).findAny().get();
    }

    public boolean delete(Tour tour) {
        log.info("delete tour: " + tour.toString());
        return tours.remove(tour);
    }

    private void createFolder(Tour tour) {
        final char sep = File.separatorChar;
        String rootPath = System.getenv("TOUR_DOCS_ROOT_PATH");
        if (rootPath == null || rootPath.isEmpty())
            rootPath = '.' + sep + "tourManagerDocsRoot";
        if (!Files.exists(Paths.get(rootPath + sep + tour.getTitle()))) {
            new File( rootPath + sep + tour.getId()).mkdir();
        }
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
        for (int i = 0; i < 1000; i++) {
            var tour = new Tour();
            tour.setTitle(faker.book().title());
            tour.setDescription(faker.yoda().quote());
            tour.setDate(getRandomInterval());
            tour.addDriver(DriverService.getRandomDriver());
            tour.addGuide(GuideService.getRandomGuide());
            tour.setFile(new File(faker.file().fileName()));
            saveOrUpdate(tour);
        }
        log.debug("init tours complete");
    }

    private Interval getRandomInterval(){
        var tl = ThreadLocalRandom.current();
        var year = 2020;
        var month = tl.nextInt(1,13); // get random int between 1 and 12
        // find out which month was generated, to get know how many days has this month, can be 28, 29, 30 or 31
        var dateTime = new DateTime();
        dateTime = dateTime.withYear(year).withMonthOfYear(month);
        var day = tl.nextInt(1, dateTime.dayOfMonth().getMaximumValue()+1);
        var hour = tl.nextInt(0, 24);
        var minute = getRandomMinuteWithStep(5);

        var startDate = new DateTime()
                .withDate(year, month, day)
                .withTime(hour, minute, 0,0);

        var plusDays = tl.nextInt(1, 15);

        var endDate = startDate.plusDays(plusDays)
                .withTime(tl.nextInt(0, 24), getRandomMinuteWithStep(5), 0, 0);
        return new Interval(startDate, endDate);
    }

    private int getRandomMinuteWithStep(int step){
        int minute = 60;
        List<Integer> list = new ArrayList<>();
        while (minute > 0){
            minute-=step;
            list.add(minute);
        }

        return list.get(ThreadLocalRandom.current().nextInt(0, list.size() - 1));
    }

    static class SortByStartDate implements Comparator<Tour> {
        @Override
        public int compare(Tour tour, Tour t1) {
            return tour.getFrom().compareTo(t1.getFrom());
        }
    }
}
