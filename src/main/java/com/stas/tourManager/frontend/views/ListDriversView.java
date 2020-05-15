package com.stas.tourManager.frontend.views;

import com.stas.tourManager.backend.persistance.pojos.Driver;
import com.stas.tourManager.backend.persistance.services.DriverService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Route(value = "drivers", layout = MainLayout.class)
public class ListDriversView extends VerticalLayout {
    private Logger logger = LoggerFactory.getLogger(ListDriversView.class);
    private static DriverService driverService;
    private static Grid<Driver> grid = new Grid<>(Driver.class);
    private final Button createButton = new Button("add");

    public ListDriversView(DriverService driverService) {
        this.driverService = driverService;
        initTable();
        setSizeFull();
    }

    /**
     * @// FIXME: 4/28/20 bad solution. Used in add view to update list after apply changes in row
     */
    public static void updateTable() {
        grid.setItems(driverService.getDrivers());
    }

    private void initCreateButton() {
        createButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_SMALL);
        var plusIcon = VaadinIcon.PLUS.create();
        createButton.setIcon(plusIcon);
        createButton.addClickListener(e -> new AddDriverForm(false, new Driver(), driverService).open());
    }

    /**
     * @// TODO: 4/27/20 add lazy data loading.
     */
    private void initTable() {
        initCreateButton();
        var driversList = driverService.getDrivers();
        grid.addThemeVariants(GridVariant.LUMO_COMPACT);

//        grid.asSingleSelect().addValueChangeListener(e -> {
//            addClickShortcut(Key.KEY_E);
//            new AddGuideView(true).open();
//        });

        grid.setSizeFull();
        try {
            grid.removeColumnByKey("car");
        } catch (IllegalArgumentException e) {
            logger.error("unable to remove column 'car'.\n" + e.getMessage());
        }
        /* note that set columns shall follow right after remove column. otherwise possible illegalStateException.
         * FIX: 4/29/20 resolve IllegalArgumentException when deleting column 'language'
         * */
        grid.setColumns("fullName");
        grid.addColumn(g -> {
            var car = g.getCar();
            return car == null ? "" : car.getCar();
        }).setHeader("Car").setSortable(true);
        // add edit button to each row.
        grid.addComponentColumn(driver -> {
            var editButton = new Button("edit", VaadinIcon.EDIT.create());
            editButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
            editButton.addClickListener(e -> new AddDriverForm(true, driver, driverService).open());
            return editButton;
        }).setHeader(createButton);

        // set content fit to columns
        grid.getColumns().forEach(c -> c.setAutoWidth(true));
        grid.setItems(driversList);
        add(grid);
    }

}
