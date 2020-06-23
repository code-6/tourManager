package com.stas.tourManager.backend.persistance.pojos;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Tour extends AbstractEntity {
    private Logger logger = LoggerFactory.getLogger(Tour.class);
    @NotEmpty
    @NotNull
    private String title;
    private String description;
    private Interval date;
    private Set<Guide> guides = new HashSet<>();
    private Set<Driver> drivers = new HashSet<>();
    private String file;

    private DateTime from;
    private DateTime to;

    public Tour() {

    }

    public void addGuide(Guide guide) {
        guides.add(guide);
        logger.info("add guide: " + guide.toString() + " to tour: " + this.toString());
    }

    public boolean excludeGuide(Guide guide) {
        logger.info("exclude guide: " + guide.toString() + " from tour: " + this.toString());
        return guides.remove(guide);
    }

    public void addDriver(Driver driver) {
        drivers.add(driver);
        logger.info("add driver: " + driver.toString() + " to tour: " + this.toString());
    }

    public boolean excludeDriver(Driver driver) {
        logger.info("exclude driver: " + driver.toString() + " from tour: " + this.toString());
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
                ", id='" + id + '\'' +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", guides=" + guides.toString() +
                ", drivers=" + drivers.toString() +
                ", file='" + file + '\'' +
                '}';
    }
    //endregion

    //region getters/setters


    public DateTime getFrom() {
        return from;
    }

    public DateTime getTo() {
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
        try{
            from = date.getStart();
            to = date.getEnd();
        }catch (NullPointerException e){
            from = null;
            to = null;
            logger.warn("Set from and to failed because of empty date value");
        }
    }

    public Set<Guide> getGuides() {
        return guides;
    }

    public void setGuides(Set<Guide> guides) {
        this.guides = guides;
    }

    public Set<Driver> getDrivers() {
        return drivers;
    }

    public void setDrivers(Set<Driver> drivers) {
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
