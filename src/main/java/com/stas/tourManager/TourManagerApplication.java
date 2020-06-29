package com.stas.tourManager;

import com.stas.tourManager.backend.persistance.services.CarService;
import com.stas.tourManager.backend.persistance.services.LanguageService;
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



}
