package com.stas.tourManager;

import com.stas.tourManager.backend.persistance.pojos.Car;
import com.stas.tourManager.backend.persistance.pojos.Language;
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
			Language.createLang("ENGLISH");
			Language.createLang("german");
			Language.createLang("russian");
			Language.createLang("chinese");
			Language.createLang("hindi");
			Language.createLang("arabic");
			System.out.println("Languages created");

			Car.createCar("mercedes");
			Car.createCar("nissan");
			Car.createCar("audi");
			Car.createCar("toyota");
			System.out.println("Cars created");
		} catch (Language.InvalidLanguageException e) {
			e.printStackTrace();
		}
	}



}
