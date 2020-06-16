package com.stas.tourManager.frontend.views;

import com.stas.tourManager.backend.persistance.pojos.Driver;
import com.stas.tourManager.backend.persistance.pojos.Guide;
import com.stas.tourManager.backend.persistance.pojos.Tour;
import com.stas.tourManager.backend.persistance.services.DriverService;
import com.stas.tourManager.backend.persistance.services.GuideService;
import com.stas.tourManager.backend.persistance.services.TourService;
import com.vaadin.flow.component.AttachEvent;
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
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.gatanaso.MultiselectComboBox;

// FIXME: 15.06.2020 refactor code! Add responsive.
@StyleSheet("https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.css")
@JavaScript("https://cdnjs.cloudflare.com/ajax/libs/jquery/3.4.1/jquery.min.js")
@JavaScript("https://cdn.jsdelivr.net/momentjs/latest/moment.min.js")
@JavaScript("https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.min.js")
public class AddTourForm extends FormLayout {
    private static final Logger log = LoggerFactory.getLogger(AddTourForm.class);

    public static final String DATE_TIME_PATTERN = "dd.MMM.yyyy HH:mm";
    public static final DateTimeFormatter dtf = DateTimeFormat.forPattern(DATE_TIME_PATTERN);
    private static DateTime start, end;

    static {
        start = DateTime.now().withHourOfDay(0).withMinuteOfHour(0);
        end = DateTime.now().withHourOfDay(0).withMinuteOfHour(0);
    }

    private Tour tour;

    private GuideService guideService;
    private DriverService driverService;
    private TourService tourService;

    private Label headerLabel = new Label();

    private TextField title = new TextField();
    private TextArea description = new TextArea();
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

    private Binder<Tour> binder = new BeanValidationBinder<>(Tour.class);

    /**
     * Required to call init method after create an instance.
     * fixme: bad solution. Init methods shall not be executed manually. Code is not reusable!
     */
    public AddTourForm(GuideService guideService, DriverService driverService, TourService tourService) {
        this.guideService = guideService;
        this.driverService = driverService;
        this.tourService = tourService;

        configLayouts();
        configButtons();
        configFields();
        configComboBoxes();
        configBinder(tour);

        add(mainLayout);
        setWidth("600px");
        setMinWidth("600px");
        setVisible(false);
    }

    public void init(boolean withDelete, String title, Tour tour) {

        if (!withDelete)
            deleteButton.setVisible(false);
        else
            deleteButton.setVisible(true);

        setTour(tour);

        configHeader(title);
        setVisible(true);
    }

    public void configHeader(String title) {
        headerLabel.setText(title);
        header.setFlexGrow(1, this.title);
        header.setAlignItems(FlexComponent.Alignment.CENTER);
    }

    private void configLayouts() {
        fieldsLayout1.setSizeFull();
        fieldsLayout2.setSizeFull();
        fieldsLayout3.setSizeFull();

        mainLayout.setAlignItems(FlexComponent.Alignment.CENTER);
    }

    private void configButtons() {
        cancelButton.addClickListener(e -> setVisible(false));

        saveButton.addClickShortcut(Key.ENTER);
        saveButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        saveButton.addClickListener(e -> {
            save();
            ListToursView.updateTable();
            setVisible(false);
            Notification.show("Changes saved", 2000, Notification.Position.TOP_END);
        });

        deleteButton.addClickShortcut(Key.DELETE);
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        deleteButton.addClickListener(event -> {
            new MyConfirmDialog("Confirm dialog", "r u sure?", e -> {
                Notification.show("Deleted !", 2000, Notification.Position.TOP_END);
            }).open();
            setVisible(false);
        });
    }

    private void save() {
        if (binder.isValid()) {
            Tour tour = binder.getBean();
            tourService.saveOrUpdate(tour);
        } else {
            log.error("Unable to create tour! ");
        }
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
    }

    private void configComboBoxes() {
        guides.setItems(guideService.getGuides());
        guides.setItemLabelGenerator(Guide::getFullName);
        guides.setClearButtonVisible(true);
        //guides.setCompactMode(true);
        guides.setPlaceholder("Choose guides");
        guides.setLabel("Appoint guides");
        guides.setSizeFull();

        drivers.setItems(driverService.getDrivers());
        drivers.setItemLabelGenerator(Driver::getFullName);
        drivers.setClearButtonVisible(true);
        drivers.setPlaceholder("Choose drivers");
        drivers.setLabel("Appoint drivers");
        drivers.setSizeFull();
    }

    private void configBinder(Tour tour) {
        binder.forField(date)
                .withNullRepresentation("")
                .withConverter(new String2IntervalConverter())
                .bind(Tour::getDate, Tour::setDate);

        binder.bindInstanceFields(this);

        binder.setBean(tour);
    }

    @Override
    protected void onAttach(AttachEvent event) {
        super.onAttach(event);
        // FIXME: 13.06.2020 possibly no need to use format in value setting because used locale in js script
        var script = String.format("$(function () {\n" +
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
                "           format: 'DD.MMM.YYYY HH:mm'\n" +
                "        }\n" +
                "    }, function (start, end, label) {\n" +
                "        const pattern = \"DD.MMM.YYYY HH:mm\";\n" +
                "        $('#daterange').val(start.format(pattern)+'-'+end.format(pattern));\n" +
                "    });\n" +
                "});", start.toString(DATE_TIME_PATTERN), end.toString(DATE_TIME_PATTERN));
        // executing JS should be avoided in constructor
        getElement().executeJs(script);
        //log.debug("Executed JS: "+script);
    }

    //region getters/setters
    public Tour getTour() {
        return tour;
    }

    public void setTour(Tour tour) {
        this.tour = tour;
        binder.readBean(tour);
    }
    //endregion

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
            var result = String.format("%s-%s", start.toString(DATE_TIME_PATTERN), end.toString(DATE_TIME_PATTERN));
            log.debug("Date-time range to return: " + result);
            return result;
        }
    }


}
