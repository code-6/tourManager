package com.stas.tourManager.frontend.views;

import com.stas.tourManager.backend.persistance.pojos.Driver;
import com.stas.tourManager.backend.persistance.services.CarService;
import com.stas.tourManager.backend.persistance.services.DriverService;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import org.springframework.beans.factory.annotation.Autowired;

public class AddDriverForm extends AddParticipantForm {
    // note that field name shall be equal to pojos property name. Otherwise binder will not map data.
    private ComboBox<String> car = new ComboBox<>();
    private Binder<Driver> binder = new BeanValidationBinder<>(Driver.class);

    @Autowired
    private final DriverService driverService;

    public AddDriverForm(boolean withDelete, String title, Driver driver, DriverService driverService) {
        super(withDelete, title);

        setupComboBox();
        binder.setBean(driver);

        fieldsLayout2.add(car);

        this.driverService = driverService;

        setupButtons();

        init();
        binder.bindInstanceFields(this);
    }

    private void setupButtons() {
        binder.addStatusChangeListener(evt -> saveButton.setEnabled(binder.isValid() && binder.hasChanges()));

        saveButton.addClickListener(event -> {
            save();
            ListDriversView.updateTable();
            close();
        });

        deleteButton.addClickListener(event -> {
            new MyConfirmDialog("Confirm dialog", "r u sure?", e -> {
                System.out.println("YAHOOO! Confirmed");
                close();
            }).open();
        });
    }

    private void save() {
        if (binder.isValid()) {
            Driver driver = binder.getBean();
            driverService.saveOrUpdate(driver);
        }
    }

    private void setupComboBox() {
        car.setItems(CarService.getCarsList());
        //car.setItemLabelGenerator(Car::getCar);
        car.setAllowCustomValue(true);
        car.addCustomValueSetListener(event -> {
            var source = event.getDetail();
            var c = CarService.createCar(source);
            // FIX bug #001. When save language that not exist yet.
            car.setValue(c);
        });
        car.setClearButtonVisible(true);
        car.setAutofocus(false);
        car.setLabel("Car");
    }
}
