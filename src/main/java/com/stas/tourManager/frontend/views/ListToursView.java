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
import com.vaadin.flow.router.Route;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

@Route(value = "tours", layout = MainLayout.class)
public class ListToursView extends HorizontalLayout {
    private static final Logger log = LoggerFactory.getLogger(ListToursView.class);
    public static final String DATE_TIME_FORMAT = "dd.MMM.yyyy HH:mm";
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormat.forPattern(DATE_TIME_FORMAT);

    private TourService tourService;
    private GuideService guideService;
    private DriverService driverService;

    private Tour tour;

    private Grid<Tour> grid = new Grid<>(Tour.class);

    private final Button createButton = new Button("add", VaadinIcon.PLUS.create());

    private AddTourForm form;

    public ListToursView(TourService tourService, GuideService guideService, DriverService driverService) {
        this.tourService = tourService;
        this.guideService = guideService;
        this.driverService = driverService;

        tour = new Tour();

        form = new AddTourForm(guideService.getGuides(), driverService.getDrivers());

        initButtons();
        initGrid();

        setSizeFull();

        add(form);
    }

    private void initGrid() {
        var toursList = tourService.getAll();
        grid.addThemeVariants(GridVariant.LUMO_COMPACT);
        grid.setSizeFull();
        // temporary hide columns
        grid.getColumns().forEach(c -> c.setVisible(false));
        grid.getColumns().forEach(c -> c.setAutoWidth(true));

        grid.getColumnByKey("id").setVisible(true);
        grid.getColumnByKey("title").setVisible(true);

        grid.removeColumnByKey("from");
        // LocalDateTimeRenderer for date and time
        grid.addColumn(new DateTimeRenderer<>(Tour::getFrom,
                DATE_TIME_FORMATTER.withLocale(Locale.US)))
                .setHeader("From").setSortable(true).setVisible(true);

        grid.removeColumnByKey("to");
        grid.addColumn(new DateTimeRenderer<>(Tour::getTo,
                DATE_TIME_FORMATTER.withLocale(Locale.US)))
                .setHeader("To").setSortable(true).setVisible(true);

        // truncate description column
        grid.getColumnByKey("description")
                .setSortable(false)
                .setAutoWidth(false)
                .setWidth("200px")
                .setVisible(true);

        grid.setItems(toursList);

        // add edit button to each row and create button as header of the column.
        grid.addComponentColumn(tour -> {
            var editButton = new Button("edit", VaadinIcon.EDIT.create());
            editButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
            editButton.addClickListener(e -> {
                log.debug("tour data before edit: " + tour.toString());
                System.out.println("edit pressed");
            });
            return editButton;
        }).setHeader(createButton);

        add(grid);
    }

    private void initButtons() {
        createButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_SMALL);

        createButton.addClickListener(e -> {
            System.out.println("add pressed");
        });
    }
}
