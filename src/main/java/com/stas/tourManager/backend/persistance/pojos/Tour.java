package com.stas.tourManager.backend.persistance.pojos;

import com.stas.tourManager.backend.persistance.services.GuideService;
import com.vaadin.flow.router.OptionalParameter;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Tour extends AbstractEntity {
    private Logger logger = LoggerFactory.getLogger(Tour.class);
    @NotEmpty
    @NotNull
    private String title;
    private String description;
    private Interval date;
    private List<Guide> guides = new ArrayList<>();
    private List<Driver> drivers = new ArrayList<>();
    private String file;

    private String from, to;

    public Tour() {

    }

    public void addGuide(Guide guide) {
        guides.add(guide);
        logger.info("add guide: " + guide.toString() + " to tour: " + title);
    }

    public boolean excludeGuide(Guide guide) {
        logger.info("exclude guide: " + guide.toString() + " from tour: " + title);
        return guides.remove(guide);
    }

    public void addDriver(Driver driver) {
        drivers.add(driver);
        logger.info("add driver: " + driver.toString() + " to tour: " + title);
    }

    public boolean excludeDriver(Driver driver) {
        logger.info("exclude driver: " + driver.toString() + " from tour: " + title);
        return drivers.remove(driver);
    }

    //region equals/hascode/tostring
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tour)) return false;
        if (!super.equals(o)) return false;
        Tour tour = (Tour) o;
        return getTitle().equals(tour.getTitle()) &&
                Objects.equals(getDescription(), tour.getDescription()) &&
                Objects.equals(getDate(), tour.getDate()) &&
                Objects.equals(getGuides(), tour.getGuides()) &&
                Objects.equals(getDrivers(), tour.getDrivers()) &&
                Objects.equals(getFile(), tour.getFile());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getTitle(), getDescription(), getDate(), getGuides(), getDrivers(), getFile());
    }

    @Override
    public String toString() {
        return "Tour{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", date=" + date.toString() +
                ", guides=" + guides.toString() +
                ", drivers=" + drivers.toString() +
                ", file='" + file + '\'' +
                '}';
    }
    //endregion

    //region getters/setters


    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Interval getDate() {
        return date;
    }

    public void setDate(Interval date) {
        this.date = date;
        from = date.getStart().toString("dd-MM-yyyy hh:mm");
        to = date.getEnd().toString("dd-MM-yyyy hh:mm");
    }

    public List<Guide> getGuides() {
        return guides;
    }

    public void setGuides(List<Guide> guides) {
        this.guides = guides;
    }

    public List<Driver> getDrivers() {
        return drivers;
    }

    public void setDrivers(List<Driver> drivers) {
        this.drivers = drivers;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
    //endregion
}
