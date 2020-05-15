package com.stas.tourManager.frontend.views.test;


import java.text.DateFormat;
import java.text.Format;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.annotation.WebServlet;

import org.vaadin.addons.datetimerangepicker.DateTimeRangeField;
import org.vaadin.addons.datetimerangepicker.type.DateTimeRange;
import org.vaadin.addons.datetimerangepicker.type.DateTimeRangeEnums;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.data.Binder;
import com.vaadin.data.HasValue.ValueChangeListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme("valo")
@Title("MyComponent Add-on Demo")
@Widgetset("org.vaadin.addons.datetimerangepicker.demo.DemoWidgetSet")
@SuppressWarnings("serial")
public class DemoUI extends UI {

    @WebServlet(
            value = "/demo/*",
            asyncSupported = true)
    @VaadinServletConfiguration(
            productionMode = false,
            ui = DemoUI.class)
    public static class Servlet extends VaadinServlet {
    }

    private static final Locale LOCALE_DEFAULT = Locale.GERMAN;

    private static final String APPLY_LABEL = "Apply";
    private static final String CANCEL_LABEL = "Cancel";

    // DateLimit Constants
    private static final String DATE_LIMIT_SPAN_MOMENT = "days";
    private static final int DATE_LIMIT_SPAN_VALUE = 7;

    // Range Constants
    private static final String RANGE_LIMIT_TODAY = "Today";
    private static final String RANGE_LIMIT_YESTERDAY = "Yesterday";

    // UI Components

    // Initialize our new UI component
    private DateTimeRangeField dateRangeFieldPrefilled;

    private DateTimeRangeField dateRangeFieldEmpty;

    private DateField minDateField;
    private DateField maxDateField;

    private Date startDate = Date.from(LocalDate.now()
            .minusDays(6)
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant());

    private Date endDate = Date.from(LocalDate.now()
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant());

    private final Date today = Date.from(LocalDate.now()
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant());

    private final Date yesterday = Date.from(LocalDate.now()
            .minusDays(1)
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant());

    private ComboBox comboOpens;
    private ComboBox comboDrops;

    private CheckBox checkShowDropDowns;
    private CheckBox checkShowWeekNumbers;
    private CheckBox checkShowISOWeekNumbers;

    private CheckBox checkSingleDatePicker;
    private CheckBox checkTimePicker;
    private CheckBox checkTimePicker24Hour;

    private TextField textParentEl;
    private TextField textTimePickerIncrement;

    private CheckBox checkTimePickerSeconds;
    private CheckBox checkDateLimit;
    private CheckBox checkAutoApply;
    private CheckBox checkLinkedCalendars;
    private CheckBox checkEnabled;

    private TextField textButtonClasses;
    private TextField textApplyClass;
    private TextField textCancelClass;

    private CheckBox checkRanges;
    private CheckBox checkAlwaysShowCalendars;
    private CheckBox checkShowCustomRangeLabel;

    private TextField textApplyLabel;
    private TextField textCancelLabel;

    private ComboBox cbLanguage;

    @Override
    protected void init(final VaadinRequest request) {

        // Initialize our new UI component
        final boolean linkedCalendars = true;
        setLocale(DemoUI.LOCALE_DEFAULT);
        final Format dateFormatter = DateFormat.getDateInstance(DateFormat.SHORT, DemoUI.LOCALE_DEFAULT);
        this.dateRangeFieldPrefilled = new DateTimeRangeField(dateFormatter, linkedCalendars, true).language(getLocale().toString());
        this.dateRangeFieldPrefilled.setWidth("300px");

        final Binder<DateTimeRangeBean> binderPrefilled = new Binder<>(DateTimeRangeBean.class);
        binderPrefilled.bind(this.dateRangeFieldPrefilled, DateTimeRangeBean::getDateTimeRange, DateTimeRangeBean::setDateTimeRange);

        final DateTimeRangeBean beanPrefilled = new DateTimeRangeBean(new DateTimeRange(this.startDate, this.endDate));
        binderPrefilled.setBean(beanPrefilled);

        this.dateRangeFieldEmpty = new DateTimeRangeField(dateFormatter, linkedCalendars, false).language(getLocale().toString());
        this.dateRangeFieldEmpty.setWidth("300px");
        final Binder<DateTimeRangeBean> binderEmpty = new Binder<>(DateTimeRangeBean.class);
        binderEmpty.bind(this.dateRangeFieldEmpty, DateTimeRangeBean::getDateTimeRange, DateTimeRangeBean::setDateTimeRange);
        final DateTimeRangeBean beanEmpty = new DateTimeRangeBean(null);
        binderEmpty.setBean(beanEmpty);

        final Button buttonEmpty = new Button("Show value");
        buttonEmpty.addClickListener(event -> {
            if (this.dateRangeFieldEmpty.getValue() != null) {
                beanEmpty.setDateTimeRange(this.dateRangeFieldEmpty.getValue());
                Notification.show(binderEmpty.getBean()
                        .getDateTimeRange()
                        .toString());
            }
            else {
                Notification.show("No DateRage is selected!");
            }

        });

        this.dateRangeFieldPrefilled.addValueChangeListener(e -> {
            final DateTimeRangeField dateTimeRangeField = (DateTimeRangeField) e.getSource();
            if (dateTimeRangeField != null && dateTimeRangeField.getValue() != null) {
                this.startDate = dateTimeRangeField.getValue()
                        .getFrom();
                this.endDate = dateTimeRangeField.getValue()
                        .getTo();
                beanEmpty.setDateTimeRange(new DateTimeRange(this.startDate, this.endDate));
            }
        });

        final ValueChangeListener valueChangeListener = event -> {
            binderPrefilled.removeBinding(DemoUI.this.dateRangeFieldPrefilled);
            setLocale(new Locale(this.cbLanguage.getValue()
                    .toString()));

            this.minDateField.setLocale(getLocale());
            this.maxDateField.setLocale(getLocale());
            this.dateRangeFieldPrefilled.setLocale(getLocale());

            DateTimeRangeField.DateLimit dateLimit = null;
            if (DemoUI.this.checkDateLimit.getValue()) {
                dateLimit = DemoUI.this.buildDateLimit(DemoUI.DATE_LIMIT_SPAN_MOMENT, DemoUI.DATE_LIMIT_SPAN_VALUE);
            }

            // Ranges
            final Map<String, DateTimeRangeField.Range> ranges = new HashMap<>();
            if (DemoUI.this.checkRanges.getValue()) {
                final DateTimeRangeField.Range rangeToday = DemoUI.this.buildRange(DemoUI.this.today, DemoUI.this.today);
                ranges.put(DemoUI.RANGE_LIMIT_TODAY, rangeToday);
                final DateTimeRangeField.Range rangeYesterday = DemoUI.this.buildRange(DemoUI.this.yesterday, DemoUI.this.yesterday);
                ranges.put(DemoUI.RANGE_LIMIT_YESTERDAY, rangeYesterday);
            }

            // Others
            DemoUI.this.dateRangeFieldPrefilled.parentEl(DemoUI.this.textParentEl.getValue())
                    .minDate(asDate(DemoUI.this.minDateField.getValue()))
                    .maxDate(asDate(DemoUI.this.maxDateField.getValue()))
                    .applyLabel(DemoUI.this.textApplyLabel.getValue())
                    .cancelLabel(DemoUI.this.textCancelLabel.getValue())
                    .opens(DateTimeRangeEnums.OPENS.valueOf(DemoUI.this.comboOpens.getValue()
                            .toString()
                            .toUpperCase()))
                    .language(DemoUI.this.cbLanguage.getValue()
                            .toString())
                    .drops(DateTimeRangeEnums.DROPS.valueOf(DemoUI.this.comboDrops.getValue()
                            .toString()
                            .toUpperCase()))
                    .showDropdowns(DemoUI.this.checkShowDropDowns.getValue())
                    .showWeekNumbers(DemoUI.this.checkShowWeekNumbers.getValue())
                    .showISOWeekNumbers(DemoUI.this.checkShowISOWeekNumbers.getValue())
                    .singleDatePicker(DemoUI.this.checkSingleDatePicker.getValue())
                    .timePicker(DemoUI.this.checkTimePicker.getValue())
                    .timePicker24Hour(DemoUI.this.checkTimePicker24Hour.getValue())
                    .timePickerIncrement(Integer.valueOf(DemoUI.this.textTimePickerIncrement.getValue()))
                    .timePickerSeconds(DemoUI.this.checkTimePickerSeconds.getValue())
                    .dateLimit(dateLimit)
                    .autoApply(DemoUI.this.checkAutoApply.getValue())
                    .linkedCalendars(DemoUI.this.checkLinkedCalendars.getValue())
                    .buttonClasses(DemoUI.this.textButtonClasses.getValue())
                    .applyClass(DemoUI.this.textApplyClass.getValue())
                    .cancelClass(DemoUI.this.textCancelClass.getValue())
                    .ranges(ranges)
                    .workable(DemoUI.this.checkEnabled.getValue())
                    .alwaysShowCalendars(DemoUI.this.checkAlwaysShowCalendars.getValue())
                    .showCustomRangeLabel(DemoUI.this.checkShowCustomRangeLabel.getValue());

            beanEmpty.setDateTimeRange(new DateTimeRange(this.startDate, this.endDate));
            binderPrefilled.bind(this.dateRangeFieldPrefilled, DateTimeRangeBean::getDateTimeRange, DateTimeRangeBean::setDateTimeRange);
        };

        // ParentEl
        this.textParentEl = new TextField("parentEl");
        this.textParentEl.addValueChangeListener(valueChangeListener);

        // MinDate
        this.minDateField = new DateField("minDate");
        this.minDateField.setLocale(getLocale());
        this.minDateField.addValueChangeListener(valueChangeListener);

        // MaxDate
        this.maxDateField = new DateField("maxDate");
        this.maxDateField.setLocale(getLocale());
        this.maxDateField.addValueChangeListener(valueChangeListener);

        // Combobox opens
        this.comboOpens = new ComboBox("opens");
        this.comboOpens.setItems(DateTimeRangeEnums.OPENS.values());
        this.comboOpens.setEmptySelectionAllowed(false);
        this.comboOpens.setValue(DateTimeRangeEnums.OPENS.RIGHT);
        this.comboOpens.addValueChangeListener(valueChangeListener);

        // Combobox drops
        this.comboDrops = new ComboBox("drops");
        this.comboDrops.setItems(DateTimeRangeEnums.DROPS.values());
        this.comboDrops.setEmptySelectionAllowed(false);
        this.comboDrops.setValue(DateTimeRangeEnums.DROPS.DOWN);
        this.comboDrops.addValueChangeListener(valueChangeListener);

        // Checkbox showDropdowns
        this.checkShowDropDowns = new CheckBox("showDropdowns");
        this.checkShowDropDowns.setValue(false);
        this.checkShowDropDowns.addValueChangeListener(valueChangeListener);

        // Checkbox showWeekNumbers
        this.checkShowWeekNumbers = new CheckBox("showWeekNumbers");
        this.checkShowWeekNumbers.setValue(false);
        this.checkShowWeekNumbers.addValueChangeListener(valueChangeListener);

        // Checkbox showISOWeekNumbers
        this.checkShowISOWeekNumbers = new CheckBox("showISOWeekNumbers");
        this.checkShowISOWeekNumbers.setValue(false);
        this.checkShowISOWeekNumbers.addValueChangeListener(valueChangeListener);

        // Checkbox singleDatePicker
        this.checkSingleDatePicker = new CheckBox("singleDatePicker");
        this.checkSingleDatePicker.setValue(false);
        this.checkSingleDatePicker.addValueChangeListener(valueChangeListener);

        // Checkbox timePicker
        this.checkTimePicker = new CheckBox("timePicker");
        this.checkTimePicker.setValue(false);
        this.checkTimePicker.addValueChangeListener(valueChangeListener);

        // Checkbox timePicker24Hour
        this.checkTimePicker24Hour = new CheckBox("timePicker24Hour");
        this.checkTimePicker24Hour.setValue(false);
        this.checkTimePicker24Hour.addValueChangeListener(valueChangeListener);

        this.textTimePickerIncrement = new TextField("timePickerIncrement (in minutes)");
        this.textTimePickerIncrement.setValue(String.valueOf(1));
        this.textTimePickerIncrement.addValueChangeListener(valueChangeListener);

        // Checkbox timePickerSeconds
        this.checkTimePickerSeconds = new CheckBox("timePickerSeconds");
        this.checkTimePickerSeconds.setValue(false);
        this.checkTimePickerSeconds.addValueChangeListener(valueChangeListener);

        // Checkbox dateRange
        this.checkDateLimit = new CheckBox("dateLimit (with example date range span)");
        this.checkDateLimit.setValue(false);
        this.checkDateLimit.addValueChangeListener(valueChangeListener);

        // Checkbox autoApply
        this.checkAutoApply = new CheckBox("autoApply");
        this.checkAutoApply.setValue(false);
        this.checkAutoApply.addValueChangeListener(valueChangeListener);

        // Checkbox linkedCalendars
        this.checkLinkedCalendars = new CheckBox("linkedCalendars");
        this.checkLinkedCalendars.setValue(true);
        this.checkLinkedCalendars.addValueChangeListener(valueChangeListener);

        // Checkbox enabled
        this.checkEnabled = new CheckBox("enabled");
        this.checkEnabled.setValue(true);
        this.checkEnabled.addValueChangeListener(valueChangeListener);

        // Textfield buttonClasses
        this.textButtonClasses = new TextField("buttonClasses");
        this.textButtonClasses.setValue("btn btn-sm");
        this.textButtonClasses.addValueChangeListener(valueChangeListener);

        // Textfield applyClass
        this.textApplyClass = new TextField("applyClass");
        this.textApplyClass.setValue("btn-success");
        this.textApplyClass.addValueChangeListener(valueChangeListener);

        // Textfield cancelClass
        this.textCancelClass = new TextField("cancelClass");
        this.textCancelClass.setValue("btn-default");
        this.textCancelClass.addValueChangeListener(valueChangeListener);

        // Checkbox checkRanges
        this.checkRanges = new CheckBox("ranges (with example predefined ranges)");
        this.checkRanges.setValue(false);
        this.checkRanges.addValueChangeListener(valueChangeListener);

        // Checkbox alwaysShowCalendars
        this.checkAlwaysShowCalendars = new CheckBox("alwaysShowCalendars");
        this.checkAlwaysShowCalendars.setValue(false);
        this.checkAlwaysShowCalendars.addValueChangeListener(valueChangeListener);

        // Checkbox showCustomRangeLabel
        this.checkShowCustomRangeLabel = new CheckBox("showCustomRangeLabel");
        this.checkShowCustomRangeLabel.setValue(false);
        this.checkShowCustomRangeLabel.addValueChangeListener(valueChangeListener);

        // Textfield applyLabel
        this.textApplyLabel = new TextField("applyLabel");
        this.textApplyLabel.setValue(DemoUI.APPLY_LABEL);
        this.textApplyLabel.addValueChangeListener(valueChangeListener);

        // Textfield cancelLabel
        this.textCancelLabel = new TextField("cancelLabel");
        this.textCancelLabel.setValue(DemoUI.CANCEL_LABEL);
        this.textCancelLabel.addValueChangeListener(valueChangeListener);

        // ComboBox language
        this.cbLanguage = new ComboBox("language");
        this.cbLanguage.setItems(Arrays.asList(DemoUI.LOCALE_DEFAULT.toString(), Locale.ENGLISH.toString(), Locale.FRENCH, Locale.ITALIAN));
        this.cbLanguage.setEmptySelectionAllowed(false);
        this.cbLanguage.setLocale(DemoUI.LOCALE_DEFAULT);
        this.cbLanguage.setValue(DemoUI.LOCALE_DEFAULT);
        this.cbLanguage.addValueChangeListener(valueChangeListener);

        // Button show
        final Button buttonPrefilled = new Button("Show value");
        buttonPrefilled.addClickListener(event -> {
            if (this.dateRangeFieldPrefilled.getValue() != null) {
                beanPrefilled.setDateTimeRange(this.dateRangeFieldPrefilled.getValue());
                Notification.show(binderPrefilled.getBean()
                        .getDateTimeRange()
                        .toString());
            }
            else {
                Notification.show("No DateRage is selected!");
            }

        });

        final VerticalLayout leftLayout = new VerticalLayout();
        leftLayout.setSpacing(true);
        leftLayout.addComponent(this.textParentEl);
        leftLayout.addComponent(this.minDateField);
        leftLayout.addComponent(this.maxDateField);
        leftLayout.addComponent(this.comboOpens);
        leftLayout.addComponent(this.comboDrops);
        leftLayout.addComponent(this.cbLanguage);

        final VerticalLayout middleLayout = new VerticalLayout();
        middleLayout.setSpacing(true);
        middleLayout.addComponent(this.checkShowDropDowns);
        middleLayout.addComponent(this.checkShowWeekNumbers);
        middleLayout.addComponent(this.checkShowISOWeekNumbers);
        middleLayout.addComponent(this.checkSingleDatePicker);
        middleLayout.addComponent(this.checkTimePicker);
        middleLayout.addComponent(this.checkTimePicker24Hour);
        middleLayout.addComponent(this.textTimePickerIncrement);
        middleLayout.addComponent(this.checkTimePickerSeconds);
        middleLayout.addComponent(this.checkDateLimit);
        middleLayout.addComponent(this.checkAutoApply);
        middleLayout.addComponent(this.checkLinkedCalendars);
        middleLayout.addComponent(this.checkEnabled);

        final VerticalLayout rightLayout = new VerticalLayout();
        rightLayout.setSpacing(true);
        rightLayout.addComponent(this.textButtonClasses);
        rightLayout.addComponent(this.textApplyClass);
        rightLayout.addComponent(this.textCancelClass);
        rightLayout.addComponent(this.checkRanges);
        rightLayout.addComponent(this.checkAlwaysShowCalendars);
        rightLayout.addComponent(this.checkShowCustomRangeLabel);
        rightLayout.addComponent(this.textApplyLabel);
        rightLayout.addComponent(this.textCancelLabel);

        final HorizontalLayout settingsLayout = new HorizontalLayout();
        settingsLayout.setCaption("Settings");
        settingsLayout.setSpacing(true);
        settingsLayout.setMargin(true);
        settingsLayout.addComponent(leftLayout);
        settingsLayout.addComponent(middleLayout);
        settingsLayout.addComponent(rightLayout);

        final VerticalLayout componentLayoutPrefilled = new VerticalLayout();
        componentLayoutPrefilled.setCaption("DateTimeRangeField with prefilled initial value");
        componentLayoutPrefilled.setSpacing(true);
        componentLayoutPrefilled.setMargin(true);
        componentLayoutPrefilled.addComponent(this.dateRangeFieldPrefilled);
        componentLayoutPrefilled.addComponent(buttonPrefilled);
        componentLayoutPrefilled.addComponent(settingsLayout);

        final VerticalLayout componentLayoutEmpty = new VerticalLayout();
        componentLayoutEmpty.setCaption("DateTimeRangeField with empty initial value");
        componentLayoutEmpty.setSpacing(true);
        componentLayoutEmpty.setMargin(true);
        componentLayoutEmpty.addComponent(this.dateRangeFieldEmpty);
        componentLayoutEmpty.addComponent(buttonEmpty);

        final HorizontalLayout mainLayout = new HorizontalLayout();
        mainLayout.setStyleName("demoContentLayout");
        mainLayout.setSizeFull();
        mainLayout.setMargin(true);
        mainLayout.addComponent(componentLayoutPrefilled);
        mainLayout.addComponent(componentLayoutEmpty);
        mainLayout.setExpandRatio(componentLayoutPrefilled, 0.8f);
        mainLayout.setExpandRatio(componentLayoutEmpty, 0.2f);

        setContent(mainLayout);
    }

    /**
     * Builds dateLimit with given date range span.
     *
     * @param dateLimitSpanMoment
     * @param dateLimitSpanValue
     * @return {@link DateTimeRangeField.DateLimit}
     */
    private DateTimeRangeField.DateLimit buildDateLimit(final String dateLimitSpanMoment, final int dateLimitSpanValue) {
        final DateTimeRangeField.DateLimit dateLimit = DemoUI.this.dateRangeFieldPrefilled.new DateLimit(dateLimitSpanMoment, dateLimitSpanValue);
        return dateLimit;
    }

    /**
     * Builds ranges with given range.
     *
     * @param from
     * @param to
     * @return {@link DateTimeRangeField.Range}
     */
    private DateTimeRangeField.Range buildRange(final Date from, final Date to) {
        final DateTimeRangeField.Range range = DemoUI.this.dateRangeFieldPrefilled.new Range(from, to);
        return range;
    }

    // Transform LocalDate to Date
    private Date asDate(final LocalDate localDate) {
        return (localDate != null) ? Date.from(localDate.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant()) : null;
    }

    // Transform Date to LocalDate
    private LocalDate asLocalDate(final Date date) {
        return Instant.ofEpochMilli(date.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
}
