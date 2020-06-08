package com.stas.tourManager;

import com.stas.tourManager.backend.persistance.pojos.Car;
import com.stas.tourManager.backend.persistance.pojos.LanguageService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class TourManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TourManagerApplication.class, args);
	}
	@PostConstruct
	private void init(){
		try {
			LanguageService.createLang("english");
			LanguageService.createLang("german");
			System.out.println("Languages created");

			Car.createCar("mercedes");
			Car.createCar("nissan");
			Car.createCar("audi");
			Car.createCar("toyota");
			System.out.println("Cars created");
		} catch (LanguageService.InvalidLanguageException e) {
			e.printStackTrace();
		}
	}



}
