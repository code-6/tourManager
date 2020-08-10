package com.stas.tourManager.frontend.views.option1.cdn;


import com.stas.tourManager.backend.persistance.pojos.Tour;
import com.stas.tourManager.backend.persistance.services.TourService;
import com.stas.tourManager.frontend.views.ListToursView;
import com.stas.tourManager.frontend.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.Months;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.time.Month;
import java.time.Year;
import java.util.Locale;

@Route(value = "test/cdn", layout = MainLayout.class)
@CssImport("./styles/grid-no-cell-padding.css")
//@JavaScript("./resources/static/js/test-jquery.js")
public class TestView extends VerticalLayout {

    @Autowired
    private TourService tourService;

    private Grid<Tour> grid;
    private ListDataProvider<Tour> dataProvider;
    private int defaultMonth = DateTime.now().getMonthOfYear();
    private int defaultYear = DateTime.now().getYear();

    @PostConstruct
    public void init() {
        initMenu();
        initGrid(defaultMonth, defaultYear);

        setAlignItems(Alignment.CENTER);
        setSizeFull();
        setPadding(false);
    }

    private void initMenu() {
        var hl = new HorizontalLayout();
        hl.setWidthFull();

        var prevMonthButton = new Button("prev");
        //prevMonthButton.addThemeVariants(ButtonVariant.LUMO_SMALL);

        var nextMonthButton = new Button("next");
        //nextMonthButton.addThemeVariants(ButtonVariant.LUMO_SMALL);

        var monthSelector = new ComboBox<Month>();
        monthSelector.setWidth("145px");
        monthSelector.setItemLabelGenerator(month -> month.name());
        monthSelector.setItems(Month.values());
        monthSelector.setValue(Month.of(DateTime.now().getMonthOfYear()));
        monthSelector.setAllowCustomValue(false);

        var yearInputFiled = new TextField();
        yearInputFiled.setWidth("60px");
        yearInputFiled.setMaxLength(4);
        yearInputFiled.setMinLength(4);
        yearInputFiled.setValueChangeMode(ValueChangeMode.EAGER);
        yearInputFiled.addValueChangeListener(event -> {
            yearInputFiled.setInvalid(validateYear(event.getValue()));
        });
        yearInputFiled.setValue(String.valueOf(DateTime.now().getYear()));

        monthSelector.addValueChangeListener(event -> {
            if (!yearInputFiled.isInvalid() && !monthSelector.isInvalid()) {
                remove(grid);
                //System.out.println("VALID");
                var month = event.getValue();
                var year = yearInputFiled.getValue();
                initGrid(month.getValue(), Integer.parseInt(year));

            }

        });

        prevMonthButton.addClickListener(click -> monthSelector.setValue(monthSelector.getValue().minus(1)));
        nextMonthButton.addClickListener(click -> monthSelector.setValue(monthSelector.getValue().plus(1)));

        hl.add(prevMonthButton, monthSelector, yearInputFiled, nextMonthButton);
        hl.setAlignItems(Alignment.CENTER);
        add(hl);
    }

    private boolean validateYear(String year) {
        try {
            if (year == null || year.isEmpty())
                return true;

            int value = Integer.valueOf(year);

            if (value < 2015 || value > 2120)
                return true;
        } catch (NumberFormatException e) {
            return true;
        }
        return false;
    }

    private void initGrid(int month, int year) {
        if(grid != null)
            grid = null;
        grid = new Grid<>(Tour.class);
        dataProvider = DataProvider.ofCollection(tourService.getByMonthAndYear(month, year));
        //grid.setItems(tourService.getAll());
        grid.setDataProvider(dataProvider);
        grid.setColumns("title");

        var titleTextField = new TextField("Title");
        titleTextField.addClassName("filter-text-field");
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

        var driversTextField = new TextField("Drivers");
        driversTextField.addClassName("filter-text-field");
        driversTextField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        driversTextField.setValueChangeMode(ValueChangeMode.LAZY);
        driversTextField.addValueChangeListener(s -> {
            var value = s.getValue();
            // do tours filter by title
            if (value != null && !value.isEmpty()) {
                dataProvider.setFilter(t -> t.getDrivers().stream().anyMatch(d -> d.getFullName().toLowerCase().contains(value.toLowerCase())));
            } else dataProvider.clearFilters();
        });
        grid.addComponentColumn(t -> {
            var vl = new VerticalLayout();
            vl.setPadding(false);
            vl.setSpacing(false);
            vl.setWidthFull();
            t.getDrivers().forEach(d -> {
                var label = new Label(d.getFullName());
                label.getElement().addEventListener("click", c -> {
                    Notification.show(d.getFullName(), 2000, Notification.Position.MIDDLE);
                });
                vl.add(label);
            });
            return vl;
        }).setHeader(driversTextField);

        var guidesTextField = new TextField("Guides");
        guidesTextField.addClassName("filter-text-field");
        guidesTextField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        guidesTextField.setValueChangeMode(ValueChangeMode.LAZY);
        guidesTextField.addValueChangeListener(s -> {
            var value = s.getValue();
            // do tours filter by title
            if (value != null && !value.isEmpty()) {
                dataProvider.setFilter(t -> t.getGuides().stream().anyMatch(g -> g.getFullName().toLowerCase().contains(value.toLowerCase())));
            } else dataProvider.clearFilters();
        });
        grid.addComponentColumn(t -> {
            var vl = new VerticalLayout();
            vl.setPadding(false);
            vl.setSpacing(false);
            vl.setWidthFull();
            t.getGuides().forEach(g -> {
                var label = new Label(g.getFullName());
                label.getElement().addEventListener("click", c -> {
                    Notification.show(g.getFullName(), 2000, Notification.Position.MIDDLE);
                });
                vl.add(label);
            });
            return vl;
        }).setHeader(guidesTextField);
        // default current month
        fillCalendar(month, year);

        var addIcon = new Icon(VaadinIcon.PLUS_SQUARE_O);
        addIcon.addClickListener(c -> Notification.show("Not implemented yet", 2000, Notification.Position.MIDDLE));

        grid.addComponentColumn(t -> {
            var editIcon = new Icon(VaadinIcon.EDIT);
            editIcon.addClickListener(c -> Notification.show(t.getDateAsString(ListToursView.DATE_TIME_FORMAT, Locale.US), 3000, Notification.Position.MIDDLE));
            return editIcon;
        }).setHeader(addIcon).setTextAlign(ColumnTextAlign.CENTER).setAutoWidth(false).setWidth("1%");

        grid.setSizeFull();
        grid.addThemeVariants(GridVariant.LUMO_COMPACT, GridVariant.LUMO_COLUMN_BORDERS, GridVariant.LUMO_ROW_STRIPES);
        add(grid);
    }

    private void fillCalendar(int month, int year) {
        var m = Month.of(month);
        var y = Year.of(year);
        var dt = new DateTime();
        for (int i = 0; i < m.maxLength(); i++) {
            dt = dt.withDate(year, month, i + 1).withTime(0, 0, 0, 0);
            // configure header
            var dayOfWeek = dt.dayOfWeek();
            var dayOfWeekLabel = dayOfWeek.getAsShortText().substring(0, 2);

            var vl = new VerticalLayout(new Label(dayOfWeekLabel), new Label(i < 9 ? "0" + (i + 1) : i + 1 + ""));
            vl.setPadding(false);
            vl.setSpacing(false);
            vl.setAlignItems(Alignment.CENTER);
            vl.addClassName("day-of-month");
            vl.setWidthFull();
            // configure content
            DateTime finalDateTime = dt;

            grid.addColumn(tour -> {
                var interval = new Interval(tour.getFrom().withTime(0, 0, 0, 0)
                        , tour.getTo().withTime(0, 0, 0, 0));

                if (tour.getFrom().withTime(0, 0, 0, 0).isEqual(finalDateTime)) {
                    return "|_";
                }
                if(tour.getTo().withTime(0, 0, 0, 0).isEqual(finalDateTime)){
                    return "_|";
                }
                if (interval.overlaps(new Interval(finalDateTime, finalDateTime))) {
                    return "__";
                }

                return null;
            }).setTextAlign(ColumnTextAlign.CENTER)
                    .setHeader(vl)
                    .setAutoWidth(false)
                    .setWidth("1%");
//            grid.addComponentColumn(tour -> {
//                var interval = new Interval(tour.getFrom().withTime(0, 0, 0, 0)
//                        , tour.getTo().withTime(0, 0, 0, 0));
//
//                if (tour.getFrom().withTime(0, 0, 0, 0).isEqual(finalDateTime)
//                        || tour.getTo().withTime(0, 0, 0, 0).isEqual(finalDateTime)) {
//
////                    var icon = new Icon(VaadinIcon.LINE_V);
////                    icon.addClassName("line-horizontal");
////                    return icon;
//                    return new Label("__");
//                }
//
//                if (interval.overlaps(new Interval(finalDateTime, finalDateTime))) {
////                    var icon = new Icon(VaadinIcon.LINE_H);
////                    icon.addClassName("line-vertical");
////                    return icon;
//                    return new Label("|");
//                }
//
//                return new Label();
//            }).setTextAlign(ColumnTextAlign.CENTER)
//                    .setHeader(vl)
//                    .setAutoWidth(false)
//                    .setWidth("1%");


            //dateTime = dateTime.plusDays(1);

        }
    }
}
