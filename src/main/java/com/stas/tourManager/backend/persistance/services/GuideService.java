package com.stas.tourManager.backend.persistance.services;

import com.stas.tourManager.backend.persistance.pojos.Guide;
import com.stas.tourManager.backend.persistance.pojos.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
public class GuideService {
    protected List<Guide> guides = new ArrayList<>();
    private Logger logger = LoggerFactory.getLogger(GuideService.class);

    public void addGuide(String firstName, String lastName, Language language) {
        var guide = new Guide(firstName, lastName).withLanguage(language);
        guides.add(guide);
        logger.info("created new guide: " + guide.toString());
    }

    public void addGuide(String firstName, String lastName) {
        var guide = new Guide(firstName, lastName);
        guides.add(guide);
        logger.info("created new guide: " + guide.toString());
    }

    public void addGuide(Guide guide) {
        guides.add(guide);
        logger.info("created new guide: " + guide.toString());
    }

    public void addGuide(Guide... guides) {
        this.guides.addAll(Arrays.asList(guides));
        for (Guide guide : guides) {
            logger.info("created new guide: " + guide.toString());
        }
    }

    public boolean deleteGuide(long id) {
        var deleted = guides.removeIf(g -> g.getId() == id);
        if (deleted)
            logger.info("deleted guide: " + id);
        return deleted;
    }

    public boolean deleteGuide(String fullName) {
        var deleted = guides.removeIf(g -> g.getFullName().equalsIgnoreCase(fullName));
        if (deleted)
            logger.info("deleted guide: " + fullName);
        return deleted;
    }

    public boolean deleteGuide(Guide guide) {
        var deleted = guides.remove(guide);
        if (deleted)
            logger.info("deleted guide: " + guide.toString());
        return deleted;
    }

    public Optional<Guide> getGuide(long id) {
        return guides.stream().filter(g -> g.getId() == id).findAny();
    }

    public Optional<Guide> getGuide(String fullName) {
        return guides.stream().filter(g -> g.getFullName().equalsIgnoreCase(fullName)).findAny();
    }

    /**
     * @// FIXME: 4/28/20 logging doesn't shows old data.
     * */
    public void updateGuide(long id, String firstName, String middleName, String lastName, Language language) {
        var logMessage = "update guide. Old data: ";
        var oldGuide = getGuide(id);
        if (oldGuide != null && oldGuide.isPresent() && !oldGuide.isEmpty()) {
            var og = oldGuide.get();
            logMessage += oldGuide.get().toStringFull() + ". New data: ";
            deleteGuide(id);

            if (firstName != null && !firstName.isEmpty() && !firstName.isBlank())
                og.setFirstName(firstName);
            if (middleName != null && !middleName.isEmpty() && !middleName.isBlank())
                og.setMiddleName(middleName);
            if (lastName != null && !lastName.isEmpty() && !lastName.isBlank())
                og.setLastName(lastName);
            if (language != null)
                og.setLanguage(language);

            if (middleName != null && !middleName.isEmpty())
                og.setFullName(String.format("%s %s %s", firstName, middleName, lastName));
            else
                og.setFullName(String.format("%s %s", firstName, lastName));

            addGuide(og);
            logMessage += og.toStringFull();
            logger.info(logMessage);
        }
    }

    public boolean existGuide(long id) {
        return guides.stream().anyMatch(g -> g.getId() == id);
    }

    public boolean existGuide(String fullName) {
        return guides.stream().anyMatch(g -> g.getFullName().equalsIgnoreCase(fullName));
    }

    @PostConstruct
    public void init() {
        for (int i = 0; i < 100; i++){
            addGuide("Guide "+i, "Guide", null);
        }
        logger.debug("init complete");
    }

    public List<Guide> getGuides() {
        Comparator<Guide> compareByFullName = new Comparator<Guide>() {
            @Override
            public int compare(Guide o1, Guide o2) {
                return o1.getFullName().compareTo(o2.getFullName());
            }
        };
        guides.sort(compareByFullName);
        return guides;
    }

}
