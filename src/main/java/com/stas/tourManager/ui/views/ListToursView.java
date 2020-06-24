package com.stas.tourManager.ui.views;

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
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.Locale;

@Route(value = "tours", layout = MainLayout.class)
public class ListToursView extends HorizontalLayout {
    private static final Logger log = LoggerFactory.getLogger(ListToursView.class);
    public static final String DATE_TIME_FORMAT = "dd.MMM.yyyy HH:mm";
    public static final DateTimeFormatter DATE_TIME_FORMATTER;

    static {
        DATE_TIME_FORMATTER = DateTimeFormat.forPattern(DATE_TIME_FORMAT).withLocale(Locale.US);
    }

    private TourService tourService;
    private GuideService guideService;
    private DriverService driverService;

    private Tour tour;

    private Grid<Tour> grid = new Grid<>(Tour.class);

    private final Button createButton = new Button("add", VaadinIcon.PLUS.create());

    public ListToursView(TourService tourService, GuideService guideService, DriverService driverService) {
        this.tourService = tourService;
        this.guideService = guideService;
        this.driverService = driverService;

        tour = new Tour();

        initButtons();
        initGrid();

        setSizeFull();

        //add(form);
    }

    private void initGrid() {
        var toursList = tourService.getAll();
        grid.setItems(toursList);
        grid.setSizeFull();
        grid.addThemeVariants(GridVariant.LUMO_COMPACT);
        grid.setColumns("id", "title", "from", "to");

        // set auto width to columns
        grid.getColumns().forEach(c -> c.setAutoWidth(true));

        grid.getColumnByKey("id")
                .setAutoWidth(false)
                .setWidth("35px");

        grid.removeColumnByKey("from");
        // LocalDateTimeRenderer for date and time
        grid.addColumn(new DateTimeRenderer<>(Tour::getFrom,
                DATE_TIME_FORMATTER.withLocale(Locale.US)))
                .setComparator(Comparator.comparing(Tour::getFrom))
                .setHeader("From").setSortable(true).setVisible(true);

        grid.removeColumnByKey("to");
        grid.addColumn(new DateTimeRenderer<>(Tour::getTo,
                DATE_TIME_FORMATTER.withLocale(Locale.US)))
                .setComparator(Comparator.comparing(Tour::getTo))
                .setHeader("To").setSortable(true).setVisible(true);

//        // truncate description column
//        // FIXME: 6/17/20 why this column at first position?
//        grid.getColumnByKey("description")
//                .setSortable(false)
//                .setAutoWidth(false)
//                .setWidth("200px");


        // add edit button to each row and create button as header of the column.
        grid.addComponentColumn(tour -> {
            var editButton = new Button("edit", VaadinIcon.EDIT.create());
            editButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
            editButton.addClickListener(e -> {
                log.debug("tour data before edit: " + tour.toString());
                // TODO: 6/17/20 change form header
                //form.getDate().initPicker(tour.getFrom(), tour.getTo());

                    add(getAddTourFormForEdit(tour));
            });
            return editButton;
        }).setHeader(createButton);

        add(grid);
    }

    private void initButtons() {
        createButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_SMALL);

        createButton.addClickListener(e -> {
            // TODO: 6/17/20 hide delete button
            //form.getDate().initPicker(null, null);

                add(getAddTourFormForCreate());
        });
    }


    private void deleteTour(AddTourForm.DeleteEvent evt) {
        tourService.delete(evt.getTour());
        updateGrid();
        closeAddForm();
    }

    private void saveTour(AddTourForm.SaveEvent evt) {
        tourService.saveOrUpdate(evt.getTour());
        updateGrid();
        closeAddForm();
    }

    private AddTourForm getAddTourFormForCreate() {
        var form = new AddTourForm(guideService.getGuides(), driverService.getDrivers());
        form.setId("addFormCreate");
        form.setTour(new Tour());
        form.getDeleteButton().setVisible(false);
        form.addListener(AddTourForm.SaveEvent.class, this::saveTour);
        form.addListener(AddTourForm.DeleteEvent.class, this::deleteTour);
        form.addListener(AddTourForm.CancelEvent.class, e -> closeAddForm());

        return form;
    }

    private AddTourForm getAddTourFormForEdit(Tour tour) {
        var form = new AddTourForm(guideService.getGuides(), driverService.getDrivers());
        form.setId("addFormEdit");
        form.setTour(tour);
        form.getHeaderLabel().setText("Edit tour: " + tour.getTitle());
        form.addListener(AddTourForm.SaveEvent.class, this::saveTour);
        form.addListener(AddTourForm.DeleteEvent.class, this::deleteTour);
        form.addListener(AddTourForm.CancelEvent.class, e -> closeAddForm());

        return form;
    }

    private void closeAddForm() {
        this.getChildren().forEach(c -> {
            try {
                var a = c.getId().get();
                if (a.equals("addFormEdit") || a.equals("addFormCreate"))
                    remove(c);
            } catch (Exception e) {
                // ignored
            }
        });
    }

    /**
     * @implNote how this method will affect on load if there will be more than thousands rows?
     * todo : make cacheble
     */
    private void updateGrid() {
        grid.setItems(tourService.getAll());
    }
}
