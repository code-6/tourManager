package com.stas.tourManager.frontend.views;

import com.stas.tourManager.backend.persistance.pojos.Driver;
import com.stas.tourManager.backend.persistance.pojos.Guide;
import com.stas.tourManager.backend.persistance.pojos.Tour;
import com.vaadin.flow.component.AttachEvent;
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
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.gatanaso.MultiselectComboBox;

import java.util.List;
import java.util.Locale;

import static com.stas.tourManager.frontend.views.ListToursView.DATE_TIME_FORMAT;
import static com.stas.tourManager.frontend.views.ListToursView.DATE_TIME_FORMATTER;

// FIXME: 6/17/20 picker value and date field value is not reset to default after press on add button
@StyleSheet("https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.css")
@JavaScript("https://cdnjs.cloudflare.com/ajax/libs/jquery/3.4.1/jquery.min.js")
@JavaScript("https://cdn.jsdelivr.net/momentjs/latest/moment.min.js")
@JavaScript("https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.min.js")
public class AddTourForm extends FormLayout {
    private static String script = "$(function () {\n" +
            "    $('#daterange').daterangepicker({\n" +
            "        timePicker: true,\n" +
            "        timePicker24Hour: true,\n" +
            "        timePickerIncrement: 5,\n" +
            "        autoApply: true,\n" +
            "        startDate: '%s',\n" +
            "        endDate: '%s',\n" +
            "        cancelLabel: 'Clear',\n" +
            "        opens: 'left',\n" +
            "        drops: 'auto',\n" +
            "        locale: {\n" +
            "           format: 'DD.MMM.YYYY HH:mm',\n" +
            "           cancelLabel: 'Clear'\n" +
            "        }\n" +
            "    }, function (start, end, label) {\n" +
            "        const pattern = \"DD.MMM.YYYY HH:mm\";\n" +
            "        $('#daterange').val(start.format(pattern)+'-'+end.format(pattern));\n" +
            "    });\n" +
            "$('#daterange').on('cancel.daterangepicker', function(ev, picker) {\n" +
            "      $(this).val('');\n" +
            "  });" +
            "});";

    private static final Logger log = LoggerFactory.getLogger(AddTourForm.class);
    // default current date and time for picker
    private static DateTime start, end;

    static {
        start = DateTime.now().withHourOfDay(0).withMinuteOfHour(0);
        end = DateTime.now().withHourOfDay(0).withMinuteOfHour(0);
    }

    // text label for identify current action, create or edit.
    private Label headerLabel = new Label();

    private TextField title = new TextField();
    private TextArea description = new TextArea();
    // text field for date-picker
    private TextField date = new TextField("Date-time");
    private MultiselectComboBox<Guide> guides = new MultiselectComboBox<>();
    private MultiselectComboBox<Driver> drivers = new MultiselectComboBox<>();
    private FileBuffer buffer = new FileBuffer();
    private Upload file = new Upload(buffer);

    protected final Button saveButton = new Button("save");
    protected final Button cancelButton = new Button("cancel");
    protected final Button deleteButton = new Button("delete");

    protected HorizontalLayout header = new HorizontalLayout(headerLabel);
    protected HorizontalLayout fieldsLayout1 = new HorizontalLayout(title, date);
    protected HorizontalLayout fieldsLayout2 = new HorizontalLayout(guides, drivers);
    protected HorizontalLayout fieldsLayout3 = new HorizontalLayout(description, file);
    protected HorizontalLayout buttonsLayout = new HorizontalLayout(saveButton, cancelButton, deleteButton);
    protected VerticalLayout mainLayout = new VerticalLayout(header, fieldsLayout1, fieldsLayout2,
            fieldsLayout3, buttonsLayout);

    Binder<Tour> binder = new BeanValidationBinder<>(Tour.class);

    private Tour tour;

    public AddTourForm(List<Guide> guidesList, List<Driver> driversList) {

        binder.forField(date)
                .withNullRepresentation("")
                .withConverter(new IntervalConverter())
                .bind(Tour::getDate, Tour::setDate);

        binder.bindInstanceFields(this);

        configHeader();
        configFields();
        configComboBoxes(guidesList, driversList);
        configButtons();
        configLayouts();

        add(mainLayout);
    }

    //region form configuration helper methods
    public void configHeader() {
        // todo: make dynamic
        headerLabel.setText("Create new tour");
        header.setFlexGrow(1, this.title);
        header.setAlignItems(FlexComponent.Alignment.CENTER);
    }

    private void configFields() {
        title.setLabel("Title");
        title.setSizeFull();

        description.setLabel("Description");
        description.setSizeFull();

        file.addSucceededListener(event -> {
            System.out.println("FILE NAME: " + event.getFileName());
        });
//        upload.setSizeFull();
        file.setWidth("87%");

        date.setId("daterange");
        date.setSizeFull();
        date.setReadOnly(true);
    }

    private void configComboBoxes(List<Guide> guidesList, List<Driver> driversList) {
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

    private void configButtons() {
        cancelButton.addClickListener(e -> {
            fireEvent(new CancelEvent(this));
            Notification.show("Cancel pressed", 2000, Notification.Position.TOP_END);
        });

        saveButton.addClickShortcut(Key.ENTER);
        saveButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        saveButton.addClickListener(e -> {
            if(validateAndSave())
                Notification.show("Saved successfully", 2000, Notification.Position.TOP_END);
            else
                Notification.show("Save failed", 2000, Notification.Position.TOP_END);

        });

        // enable or disable save button depends on validation status of binder
        binder.addStatusChangeListener(evt -> saveButton.setEnabled(binder.isValid()));

        deleteButton.addClickShortcut(Key.DELETE);
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        deleteButton.addClickListener(event -> {
            new MyConfirmDialog("Confirm dialog", "r u sure?", e -> {
                fireEvent(new DeleteEvent(this, binder.getBean()));
                Notification.show("Deleted", 2000, Notification.Position.TOP_END);
            }).open();
        });
    }

    private void configLayouts() {
        fieldsLayout1.setSizeFull();
        fieldsLayout2.setSizeFull();
        fieldsLayout3.setSizeFull();

        mainLayout.setAlignItems(FlexComponent.Alignment.CENTER);
    }
    //endregion

    private boolean validateAndSave() {
        try {
            binder.writeBean(tour);
            fireEvent(new SaveEvent(this, tour));
            return true;
        } catch (ValidationException e) {
            log.error(ExceptionUtils.getStackTrace(e));
            return false;
        }
    }

    @Override
    protected void onAttach(AttachEvent event) {
        super.onAttach(event);
        // FIXME: 13.06.2020 possibly no need to use format in value setting because used locale in js script
        var s = String.format(script, start.toString(DATE_TIME_FORMAT, Locale.US), end.toString(DATE_TIME_FORMAT, Locale.US));
        // executing JS should be avoided in constructor
        getElement().executeJs(s);
        System.out.println("Executed JS: " + s);
    }

    public void setTour(Tour tour) {
        this.tour = tour;
        binder.readBean(tour);
    }

    //region interval converter
    /**
     * Converts String into joda Interval and vice versa.
     * */
    static class IntervalConverter implements Converter<String, Interval> {
        /**
         * Converts string value to interval
         * @param value string representation of interval, example: 02.Jun.2020 00:00-03.Jun.2020 00:00
         * */
        @Override
        public Result<Interval> convertToModel(String value, ValueContext context) {
            try {
                var arr = value.split("-");
                var from = DateTime.parse(arr[0], DATE_TIME_FORMATTER);
                var to = DateTime.parse(arr[1], DATE_TIME_FORMATTER);
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
            var result = String.format("%s-%s", start.toString(DATE_TIME_FORMAT, Locale.US),
                    end.toString(DATE_TIME_FORMAT, Locale.US));
            System.out.println("convert from interval to string result: " + result);
            return result;
        }
    }
    //endregion

    //region save delete and cancel events
    public static abstract class AddTourFormEvent extends ComponentEvent<AddTourForm> {
        private Tour tour;

        protected AddTourFormEvent(AddTourForm source, Tour tour) {
            super(source, false);
            this.tour = tour;
        }

        public Tour getTour() {
            return tour;
        }
    }

    public static class SaveEvent extends AddTourFormEvent {
        SaveEvent(AddTourForm source, Tour tour) {
            super(source, tour);
        }
    }

    public static class DeleteEvent extends AddTourFormEvent {
        DeleteEvent(AddTourForm source, Tour tour) {
            super(source, tour);
        }

    }

    public static class CancelEvent extends AddTourFormEvent {
        CancelEvent(AddTourForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
    //endregion
}
