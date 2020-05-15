package com.stas.tourManager.frontend.views;

import com.stas.tourManager.backend.persistance.pojos.Tour;
import com.stas.tourManager.backend.persistance.services.TourService;
import com.stas.tourManager.frontend.views.test.DemoUI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Route(value = "tours", layout = MainLayout.class)
public class ListToursView extends VerticalLayout {
    private Logger logger = LoggerFactory.getLogger(ListToursView.class);
    private static TourService tourService;
    private static Grid<Tour> grid = new Grid<>(Tour.class);
    private final Button createButton = new Button("add");

    public ListToursView(TourService tourService) {
        this.tourService = tourService;
        initTable();
        setSizeFull();
    }

    public static void updateTable() {
        grid.setItems(tourService.getAll());
    }

    private void initTable() {
        initCreateButton();
        var toursList = tourService.getAll();
        grid.addThemeVariants(GridVariant.LUMO_COMPACT);
        grid.setSizeFull();
        // add edit button to each row.
        grid.setColumns("title", "date", "description");
        grid.addComponentColumn(driver -> {
            var editButton = new Button("edit", VaadinIcon.EDIT.create());
            editButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
            //editButton.addClickListener(e -> new AddDriverForm(true, driver, driverService).open());
            return editButton;
        }).setHeader(createButton);

        // set content fit to columns
        grid.getColumns().forEach(c -> c.setAutoWidth(true));
        grid.setItems(toursList);
        add(grid);
    }

    private void initCreateButton() {
        createButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_SMALL);
        var plusIcon = VaadinIcon.PLUS.create();
        createButton.setIcon(plusIcon);
        //createButton.addClickListener(e -> new AddTourForm().open());
    }

}
