package com.stas.tourManager.frontend.views;

import com.stas.tourManager.backend.persistance.pojos.Driver;
import com.stas.tourManager.backend.persistance.pojos.Guide;
import com.stas.tourManager.backend.persistance.pojos.Tour;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.FileBuffer;
import com.vaadin.flow.data.binder.*;
import com.vaadin.flow.data.converter.Converter;
import com.vaadin.flow.shared.Registration;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.gatanaso.MultiselectComboBox;

import java.util.List;
import java.util.Locale;

@StyleSheet("https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.css")
@JavaScript("https://cdnjs.cloudflare.com/ajax/libs/jquery/3.4.1/jquery.min.js")
@JavaScript("https://cdn.jsdelivr.net/momentjs/latest/moment.min.js")
@JavaScript("https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.min.js")
public class AddTourForm2 extends FormLayout {
    private static final Logger log = LoggerFactory.getLogger(AddTourForm2.class);

    public static final String DATE_TIME_PATTERN = "dd.MMM.yyyy HH:mm";
    public static final DateTimeFormatter dtf = DateTimeFormat.forPattern(DATE_TIME_PATTERN);

    // used to initialize default date in picker.
    private static DateTime start, end;

    static {
        start = DateTime.now().withHourOfDay(0).withMinuteOfHour(0);
        end = DateTime.now().withHourOfDay(0).withMinuteOfHour(0);
    }
    // data binder
    private Binder<Tour> binder = new BeanValidationBinder<>(Tour.class);
    // data provider
    private Tour tour;
    // used for identify current action edit or create
    private Label headerLabel = new Label();
    // input fields
    private TextField title = new TextField();
    private TextArea description = new TextArea();
    private TextField date = new TextField("Date-time");
    private MultiselectComboBox<Guide> guides = new MultiselectComboBox<>();
    private MultiselectComboBox<Driver> drivers = new MultiselectComboBox<>();
    private FileBuffer buffer = new FileBuffer();
    private Upload file = new Upload(buffer);
    // buttons
    protected final Button saveButton = new Button("save");
    protected final Button cancelButton = new Button("cancel");
    protected final Button deleteButton = new Button("delete");
    // layouts
    protected HorizontalLayout header = new HorizontalLayout(headerLabel);
    protected HorizontalLayout fieldsLayout1 = new HorizontalLayout(title, date);
    protected HorizontalLayout fieldsLayout2 = new HorizontalLayout(guides, drivers);
    protected HorizontalLayout fieldsLayout3 = new HorizontalLayout(description, file);
    protected HorizontalLayout buttonsLayout = new HorizontalLayout(saveButton, cancelButton, deleteButton);

    protected VerticalLayout mainLayout = new VerticalLayout(header, fieldsLayout1, fieldsLayout2,
            fieldsLayout3, buttonsLayout);

    public AddTourForm2() {
        binder.bindInstanceFields(this);
    }

    public AddTourForm2(List<Driver> driversList, List<Guide> guidesList) {
        binder.forField(date)
                .withNullRepresentation("")
                .withConverter(new AddTourForm2.String2IntervalConverter())
                .bind(Tour::getDate, Tour::setDate);

        binder.bindInstanceFields(this);

        configLayouts();
        configButtons();
        configFields();
        configComboBoxes(driversList, guidesList);

        setWidth("600px");
        setMinWidth("600px");
    }

    private void configComboBoxes(List<Driver> driversList, List<Guide> guidesList) {
        guides.setItems(guidesList);
        guides.setItemLabelGenerator(Guide::getFullName);
        guides.setClearButtonVisible(true);
        //guides.setCompactMode(true);
        guides.setPlaceholder("Choose guides");
        guides.setLabel("Appoint guides");
        guides.setSizeFull();

        drivers.setItems(driversList);
        drivers.setItemLabelGenerator(Driver::getFullName);
        drivers.setClearButtonVisible(true);
        drivers.setPlaceholder("Choose drivers");
        drivers.setLabel("Appoint drivers");
        drivers.setSizeFull();
    }

    private void configLayouts() {
        fieldsLayout1.setSizeFull();
        fieldsLayout2.setSizeFull();
        fieldsLayout3.setSizeFull();

        mainLayout.setAlignItems(FlexComponent.Alignment.CENTER);
    }

    private void configFields() {
        title.setLabel("Title");
        title.setSizeFull();

        description.setLabel("Description");
        description.setSizeFull();

        file.addSucceededListener(event -> {
            System.out.println("FILE NAME: " + event.getFileName());
            binder.getBean().setFile(event.getFileName());
        });
//        upload.setSizeFull();
        file.setWidth("87%");

        date.setId("daterange");
        date.setSizeFull();
        date.setReadOnly(true);
    }

    private void configHeader(){
        //headerLabel.setText(title);
        header.setFlexGrow(1, this.title);
        header.setAlignItems(FlexComponent.Alignment.CENTER);
    }

    private void configButtons() {
        cancelButton.addClickListener(e -> fireEvent(new CancelEvent(this)));

        saveButton.addClickShortcut(Key.ENTER);
        saveButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        saveButton.addClickListener(e -> {
                validateAndSave();
                Notification.show("Changes saved", 2000, Notification.Position.TOP_END);
        });

        deleteButton.addClickShortcut(Key.DELETE);
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        deleteButton.addClickListener(event -> {
            new MyConfirmDialog("Confirm dialog", "r u sure?", e -> {
                fireEvent(new DeleteEvent(this, binder.getBean()));
                Notification.show("Deleted !", 2000, Notification.Position.TOP_END);

            }).open();
            setVisible(false);
        });
    }

    private void validateAndSave() {
        try {
            binder.writeBean(tour);
            fireEvent(new SaveEvent(this, tour));
        } catch (ValidationException e) {
            e.printStackTrace();
            Notification.show(e.getMessage(), 2000, Notification.Position.TOP_END);
        }
    }

    public void setTour(Tour tour) {
        this.tour = tour;
        binder.readBean(tour);
    }

    public static abstract class TourFormEvent extends ComponentEvent<AddTourForm2> {
        private Tour tour;

        protected TourFormEvent(AddTourForm2 source, Tour tour) {
            super(source, false);
            this.tour = tour;
        }

        public Tour getTour() {
            return tour;
        }
    }

    public static class SaveEvent extends TourFormEvent {
        SaveEvent(AddTourForm2 source, Tour tour) {
            super(source, tour);
        }
    }

    public static class DeleteEvent extends TourFormEvent {
        DeleteEvent(AddTourForm2 source, Tour tour) {
            super(source, tour);
        }

    }

    public static class CancelEvent extends TourFormEvent {
        CancelEvent(AddTourForm2 source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

    static class String2IntervalConverter implements Converter<String, Interval> {

        @Override
        public Result<Interval> convertToModel(String value, ValueContext context) {
            try {
                var arr = value.split("-");
                var from = DateTime.parse(arr[0], dtf);
                var to = DateTime.parse(arr[1], dtf);
                var interval = new Interval(from, to);
                // TODO: 12.06.2020 test Interval.parse method
                return Result.ok(interval);
            } catch (NullPointerException e) {
                return Result.ok(null);
            }
        }

        @Override
        public String convertToPresentation(Interval value, ValueContext context) {
            try {
                start = value.getStart();
                end = value.getEnd();
                // fix error when press on add tour button.
            } catch (NullPointerException e) {
                // ignored, return current date with zero time by default.
                return "";
            }
            var result = String.format("%s-%s", start.toString(DATE_TIME_PATTERN, Locale.US), end.toString(DATE_TIME_PATTERN, Locale.US));
            log.debug("Date-time range to return: " + result);
            return result;
        }
    }
}
