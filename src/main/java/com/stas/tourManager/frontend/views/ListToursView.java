package com.stas.tourManager.frontend.views;

import com.stas.tourManager.backend.persistance.pojos.Tour;
import com.stas.tourManager.backend.persistance.services.DriverService;
import com.stas.tourManager.backend.persistance.services.GuideService;
import com.stas.tourManager.backend.persistance.services.TourService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.router.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.format.DateTimeFormatter;

@Route(value = "tours", layout = MainLayout.class)
public class ListToursView extends HorizontalLayout {
    private final Logger log = LoggerFactory.getLogger(ListToursView.class);

    private static TourService tourService;

    private GuideService guideService;

    private DriverService driverService;

    private AddTourForm form;

    private static Grid<Tour> grid = new Grid<>(Tour.class);

    private final Button createButton = new Button("add");

    public ListToursView(TourService tourService, GuideService guideService, DriverService driverService) {
        this.tourService = tourService;
        this.guideService = guideService;
        this.driverService = driverService;

        form = new AddTourForm(guideService, driverService, tourService);

        init();
    }

    private void init() {

        setSizeFull();
        var toursList = tourService.getAll();
        grid.addThemeVariants(GridVariant.LUMO_COMPACT);
        grid.setSizeFull();

//        try {
//            // FIXME: 6/8/20 when accessing in the same time from another device=>org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'com.stas.tourManager.frontend.views.ListToursView': Bean instantiation via constructor failed; nested exception is org.springframework.beans.BeanInstantiationException: Failed to instantiate [com.stas.tourManager.frontend.views.ListToursView]: Constructor threw exception; nested exception is java.lang.IllegalStateException: Cannot access state in VaadinSession or UI without locking the session.
//            grid.setColumns("title", "description");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        grid.removeColumns(grid.getColumnByKey("title"),
                grid.getColumnByKey("from"),
                grid.getColumnByKey("to"),
                grid.getColumnByKey("description"),
                grid.getColumnByKey("guides"),
                grid.getColumnByKey("drivers"),
                grid.getColumnByKey("file"),
                grid.getColumnByKey("date"));

        grid.addColumn("title").setHeader("Title").setSortable(true);
        // LocalDateTimeRenderer for date and time
        grid.addColumn(new DateTimeRenderer<>(Tour::getFrom,
                AddTourForm.dtf))
                .setHeader("From").setSortable(true);

        grid.addColumn(new DateTimeRenderer<>(Tour::getTo,
                AddTourForm.dtf))
                .setHeader("To").setSortable(true);

        grid.addColumn("description").setHeader("Description");

        grid.getColumns().forEach(c -> c.setAutoWidth(true));
        // truncate description column
        grid.getColumnByKey("description").setAutoWidth(false).setWidth("200px");
        grid.setItems(toursList);

        initButtons();

        add(grid);
        add(form);
    }

    /**
     * @// FIXME: 4/28/20 bad solution. Used in add view to update list after apply changes in row
     */
    public static void updateTable() {
        grid.setItems(tourService.getAll());
    }

    private void initButtons() {
        createButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_SMALL);
        var plusIcon = VaadinIcon.PLUS.create();
        createButton.setIcon(plusIcon);

        createButton.addClickListener(e -> {
            form.init(false, "Create new tour", new Tour());
        });
        // add edit button to each row.
        grid.addComponentColumn(tour -> {
            var editButton = new Button("edit", VaadinIcon.EDIT.create());
            editButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
            editButton.addClickListener(e -> {
                log.debug("tour data before edit: " + tour.toString());
                form.init(true, "Edit tour: " + tour.getTitle(), tour);
            });
            return editButton;
        }).setHeader(createButton);
    }
}
