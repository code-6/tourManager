package com.stas.tourManager;

import com.stas.tourManager.backend.persistance.services.CarService;
import com.stas.tourManager.backend.persistance.services.LanguageService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

@SpringBootApplication
public class TourManagerApplication {
    public static String ROOT_PATH;

    static{
        ROOT_PATH = System.getenv("TOUR_DOCS_ROOT_PATH");
        initDocsRootFolder();
    }

    public static void main(String[] args) {
        SpringApplication.run(TourManagerApplication.class, args);
    }

    @PostConstruct
    private void init() {
        System.out.println("asd asd asd ");
        try {
            LanguageService.createLang("english");
            LanguageService.createLang("german");
            System.out.println("Languages created");

            CarService.createCar("mercedes");
            CarService.createCar("nissan");
            CarService.createCar("audi");
            CarService.createCar("toyota");
            System.out.println("Cars created");
        } catch (LanguageService.InvalidLanguageException e) {
            e.printStackTrace();
        }
    }

    private static void initDocsRootFolder() {
        final char sep = File.separatorChar;

        // if environment variable not setup set default path
        if (ROOT_PATH == null || ROOT_PATH.isEmpty())
            ROOT_PATH = '.' + sep + "tourManagerDocsRoot";
        if (!Files.exists(Paths.get(ROOT_PATH))) {
            new File(ROOT_PATH).mkdir();
        }
    }

}
