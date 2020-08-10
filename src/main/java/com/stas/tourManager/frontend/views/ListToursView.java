package com.stas.tourManager.frontend.views;

import com.stas.tourManager.backend.persistance.pojos.Tour;
import com.stas.tourManager.backend.persistance.services.DriverService;
import com.stas.tourManager.backend.persistance.services.GuideService;
import com.stas.tourManager.backend.persistance.services.TourService;
import com.stas.tourManager.util.DateTimeRenderer;
import com.stas.tourManager.util.DesktopAPI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.IOException;
import java.util.Comparator;
import java.util.Locale;

//@CssImport("./styles/list-tours-view.css")
//@JavaScript("https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js")
//@JavaScript("./scripts/tableFilter.js")
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

    private ListDataProvider<Tour> dataProvider;

    private final Button createButton = new Button(VaadinIcon.PLUS.create());

    private final AddTourForm form;

    public ListToursView(TourService tourService, GuideService guideService, DriverService driverService) {
        this.tourService = tourService;
        this.guideService = guideService;
        this.driverService = driverService;

        dataProvider = DataProvider.ofCollection(this.tourService.getAll());

        tour = new Tour();

        form = new AddTourForm(guideService.getGuides(), driverService.getDrivers());
        // hide add form
        form.setVisible(false);
        form.addListener(AddTourForm.SaveEvent.class, this::saveTour);
        form.addListener(AddTourForm.DeleteEvent.class, this::deleteTour);
        form.addListener(AddTourForm.CancelEvent.class, e -> closeAddForm());

        initButtons();
        initGrid();
        setPadding(false);
        setSizeFull();
        setClassName("tours-list-view");

        add(form);
    }

    private void initGrid() {
        grid.addClassNames("grid", "grid-tours");
        //var toursList = tourService.getAll();
        //grid.setItems(toursList);
        grid.setDataProvider(dataProvider);
        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_COMPACT, GridVariant.LUMO_NO_BORDER);

        grid.setColumns("title");

        var titleTextField = new TextField("Title");
        titleTextField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        titleTextField.setValueChangeMode(ValueChangeMode.LAZY);
        titleTextField.addValueChangeListener(s -> {
            var value = s.getValue();
            // do tours filter by title
            if (value != null && !value.isEmpty()) {
                //dataProvider.setFilterByValue(t -> t.getTitle().toLowerCase(), value.toLowerCase());
                dataProvider.setFilter(t -> t.getTitle().toLowerCase().contains(value.toLowerCase()));
            } else dataProvider.clearFilters();
        });
        grid.getColumnByKey("title").setHeader(titleTextField);

        //grid.removeColumnByKey("from");
        // LocalDateTimeRenderer for date and time
        var fromTextField = new TextField("From");
        fromTextField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        fromTextField.setValueChangeMode(ValueChangeMode.LAZY);
        fromTextField.addValueChangeListener(s->{
            var value = s.getValue();
            // do tours filter by title
            if (value != null && !value.isEmpty()) {
                //dataProvider.setFilterByValue(t -> t.getTitle().toLowerCase(), value.toLowerCase());
                dataProvider.setFilter(t -> t.getFrom().toString(DATE_TIME_FORMAT, Locale.US).toLowerCase().contains(value));
            } else dataProvider.clearFilters();
        });
        grid.addColumn(new DateTimeRenderer<>(Tour::getFrom,
                DATE_TIME_FORMATTER.withLocale(Locale.US)))
                .setComparator(Comparator.comparing(Tour::getFrom))
                .setTextAlign(ColumnTextAlign.START)
                .setHeader(fromTextField).setSortable(true).setVisible(true);

        //grid.removeColumnByKey("to");
        var toTextField = new TextField("To");
        toTextField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        toTextField.setValueChangeMode(ValueChangeMode.LAZY);
        toTextField.addValueChangeListener(s->{
            var value = s.getValue();
            // do tours filter by title
            if (value != null && !value.isEmpty()) {
                //dataProvider.setFilterByValue(t -> t.getTitle().toLowerCase(), value.toLowerCase());
                dataProvider.setFilter(t -> t.getFrom().toString(DATE_TIME_FORMAT, Locale.US).toLowerCase().contains(value));
            } else dataProvider.clearFilters();
        });
        grid.addColumn(new DateTimeRenderer<>(Tour::getTo,
                DATE_TIME_FORMATTER.withLocale(Locale.US)))
                .setComparator(Comparator.comparing(Tour::getTo))
                .setTextAlign(ColumnTextAlign.START)
                .setHeader(toTextField).setSortable(true).setVisible(true);

        grid.getColumns().forEach(c -> c.setAutoWidth(true));

        var fileTextField = new TextField("File");
        fileTextField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        fileTextField.setValueChangeMode(ValueChangeMode.LAZY);
        fileTextField.addValueChangeListener(s->{
            var value = s.getValue();
            // do tours filter by title
            if (value != null && !value.isEmpty()) {
                //dataProvider.setFilterByValue(t -> t.getTitle().toLowerCase(), value.toLowerCase());
                dataProvider.setFilter(t->t.getFile().getName().toLowerCase().contains(value.toLowerCase()));
            } else dataProvider.clearFilters();
        });
        grid.addComponentColumn(tour -> {
            var anchor = new Anchor();
            var tourFile = tour.getFile();
            if (tourFile != null)
                anchor.setText(tourFile.getName());

            anchor.getElement().addEventListener("click", e -> {
                try {
                    if (Desktop.isDesktopSupported())
                        Desktop.getDesktop().open(tour.getFile());
                    else
                        DesktopAPI.open(tour.getFile());
// FIXME: 7/26/20 why catch not works?
                } catch (IOException exception) {
                    Notification.show("File:" + tour.getFile().getName() + " was removed or not exist", 3000, Notification.Position.MIDDLE);
                    exception.printStackTrace();
                }
            });
            return anchor;
        }).setHeader(fileTextField).setSortable(true).setWidth("150px").setAutoWidth(false);

//        // truncate description column
//        // FIXME: 6/17/20 why this column at first position?
//        grid.getColumnByKey("description")
//                .setSortable(false)
//                .setAutoWidth(false)
//                .setWidth("200px");

        var driversTextField = new TextField("Drivers");
        driversTextField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        driversTextField.setValueChangeMode(ValueChangeMode.LAZY);
        driversTextField.addValueChangeListener(s->{
            var value = s.getValue();
            // do tours filter by title
            if (value != null && !value.isEmpty()) {
                dataProvider.setFilter(t->t.getDrivers().stream().anyMatch(d->d.getFullName().toLowerCase().contains(value.toLowerCase())));
            } else dataProvider.clearFilters();
        });
        grid.addComponentColumn(tour -> {
            var hl = new VerticalLayout();
            hl.setPadding(false);
            hl.setSpacing(false);
            hl.setAlignItems(Alignment.START);
            tour.getDrivers().forEach(driver -> {
                var a = new Anchor();
                a.addClassName("driver-anchor");
                a.setText(driver.getFullName());
                a.getElement().addEventListener("click", click -> {
                    Notification.show("Driver \"" + driver.getFullName() + "\" selected", 3000, Notification.Position.MIDDLE);
                });
                hl.add(a);
            });
            return hl;
        }).setTextAlign(ColumnTextAlign.START).setHeader(driversTextField).setAutoWidth(true);

        var guidesTextField = new TextField("Guides");
        guidesTextField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        guidesTextField.setValueChangeMode(ValueChangeMode.LAZY);
        guidesTextField.addValueChangeListener(s->{
            var value = s.getValue();
            // do tours filter by title
            if (value != null && !value.isEmpty()) {
                dataProvider.setFilter(t->t.getGuides().stream().anyMatch(g->g.getFullName().toLowerCase().contains(value.toLowerCase())));
            } else dataProvider.clearFilters();
        });
        grid.addComponentColumn(tour -> {
            var hl = new VerticalLayout();
            hl.setPadding(false);
            hl.setSpacing(false);
            hl.setAlignItems(Alignment.START);
            tour.getGuides().forEach(guide -> {

                var a = new Anchor();
                a.addClassName("guide-anchor");
                a.setText(guide.getFullName());
                a.getElement().addEventListener("click", click -> {
                    Notification.show("Guide \"" + guide.getFullName() + "\" selected", 3000, Notification.Position.MIDDLE);
                });
                hl.add(a);
            });
            return hl;
        }).setTextAlign(ColumnTextAlign.START).setHeader(guidesTextField).setAutoWidth(true);

        // add edit button to each row and create button as header of the column.
        grid.addComponentColumn(tour -> {
            var editButton = new Button(VaadinIcon.EDIT.create());
            editButton.addClassName("button-edit");
            editButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
            editButton.addClickListener(e -> {
                log.debug("tour data before edit: " + tour.toString());
                // TODO: 6/17/20 change form header
                //form.initPicker(tour.getFrom(), tour.getTo());
                //form.getDate().init(tour.getFrom(), tour.getTo());
                editTour(tour);
                // shall be called after set tour to the form
                form.forEdit();
            });
            return editButton;
        }).setTextAlign(ColumnTextAlign.END).setHeader(createButton);

        grid.setSizeFull();

        add(grid);
    }

    private void initButtons() {
        createButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_SMALL);
        createButton.addClassName("button-create");
        createButton.addClickListener(e -> {
            // TODO: 6/17/20 hide delete button
            //form.initPicker(null, null);
            //form.getDate().init(DateTime.now().withHourOfDay(0).withSecondOfMinute(0),DateTime.now().withHourOfDay(0).withSecondOfMinute(0));
            form.forCreate();
            addTour();
        });
    }

    private void deleteTour(AddTourForm.DeleteEvent evt) {
        var tour = evt.getTour();
        tourService.delete(tour);
        // FIXME: 7/24/20 why refresh item does not updates grid?
        //dataProvider.refreshItem(tour);
        // note: this is only temporal solution.
        dataProvider.refreshAll();
        //updateGrid();
        closeAddForm();
    }

    private void saveTour(AddTourForm.SaveEvent evt) {
        var tour = evt.getTour();
        tourService.saveOrUpdate(tour);
        dataProvider.refreshAll();

        //updateGrid();
        closeAddForm();
    }

    private void addTour() {
        editTour(new Tour());
    }

    private void editTour(Tour tour) {
        if (tour == null) {
            closeAddForm();
        } else {
            form.setTour(tour);
            form.setVisible(true);
        }
    }

    private void closeAddForm() {
        form.setTour(null);
        form.setVisible(false);
    }

    /**
     * @implNote how this method will affect on load if there will be more than thousands rows?
     * @since used data provider and in memory
     * todo : make cacheble
     */
    private void updateGrid() {
        grid.setItems(tourService.getAll());
    }
}
