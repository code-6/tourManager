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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

@Route(value = "tours", layout = MainLayout.class)
public class ListToursView extends HorizontalLayout {
    private Logger logger = LoggerFactory.getLogger(ListToursView.class);

    private static TourService tourService;

    private GuideService guideService;

    private DriverService driverService;

    private static Grid<Tour> grid = new Grid<>(Tour.class);
    private final Button createButton = new Button("add");

//    public static void updateTable() {
//        grid.setItems(tourService.getAll());
//    }

    public ListToursView(TourService tourService, GuideService guideService, DriverService driverService) {
        this.tourService = tourService;
        this.guideService = guideService;
        this.driverService = driverService;

        init();
    }

    private void init() {

        setSizeFull();
        var toursList = tourService.getAll();
        grid.addThemeVariants(GridVariant.LUMO_COMPACT);
        grid.setSizeFull();

        try {
            // FIXME: 6/8/20 when accessing in the same time from another device=>org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'com.stas.tourManager.frontend.views.ListToursView': Bean instantiation via constructor failed; nested exception is org.springframework.beans.BeanInstantiationException: Failed to instantiate [com.stas.tourManager.frontend.views.ListToursView]: Constructor threw exception; nested exception is java.lang.IllegalStateException: Cannot access state in VaadinSession or UI without locking the session.
            grid.setColumns("title", "from", "to", "description");
        } catch (Exception e) {
            e.printStackTrace();
        }

        grid.getColumns().forEach(c -> c.setAutoWidth(true));
        // truncate description column
        grid.getColumnByKey("description").setAutoWidth(false).setWidth("200px");
        grid.setItems(toursList);

        initButtons();

        add(grid);
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
            var form = new AddTourForm(false, "Create new tour", new Tour(), guideService, driverService, tourService);
            form.setVisible(true);
            add(form);
        });
        // add edit button to each row.
        grid.addComponentColumn(tour -> {
            var editButton = new Button("edit", VaadinIcon.EDIT.create());
            editButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
            editButton.addClickListener(e -> {
                var form = new AddTourForm(false, "Create new tour", tour, guideService, driverService, tourService);
                form.configHeader("Edit tour: " + tour.getTitle());
                form.setVisible(true);
                add(form);
            });
            return editButton;
        }).setHeader(createButton);
    }


}
