package com.stas.tourManager.frontend.views.option1.cdn;


import com.stas.tourManager.backend.persistance.pojos.Tour;
import com.stas.tourManager.backend.persistance.services.TourService;
import com.stas.tourManager.frontend.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

@Route(value = "test/cdn", layout = MainLayout.class)
//@JavaScript("./resources/static/js/test-jquery.js")
public class TestView extends VerticalLayout {

    @Autowired
    private TourService tourService;

    private Grid<Tour> grid = new Grid<>(Tour.class);

    @PostConstruct
    public void init() {
        grid.setItems(tourService.getAll());

        grid.setColumns("title", "drivers", "guides");

        var currentMonth = DateTime.now().dayOfMonth();

//        for (int i = 0; i < currentMonth.getMaximumValue(); i++) {
//            grid.addComponentColumn(tour -> {
//                return new Label("*");
//            }).setHeader(i < 9 ? "0" + (i + 1) : i + 1 + "")
//                    .setTextAlign(ColumnTextAlign.CENTER)
//                    .setAutoWidth(true);
//        }

        var createButton = new Button("+");
        createButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_SMALL);

        grid.addComponentColumn(t -> {
            var editAnchor = new Anchor();
            editAnchor.setText("e");
            return editAnchor;
        }).setHeader(createButton).setTextAlign(ColumnTextAlign.CENTER);

        grid.setSizeFull();
        grid.addThemeVariants(GridVariant.LUMO_COMPACT, GridVariant.LUMO_COLUMN_BORDERS, GridVariant.LUMO_ROW_STRIPES);

        grid.getColumns().forEach(c->{
            var vl = new VerticalLayout(new Label(c.getKey()), new TextField());
            vl.setPadding(false);
            vl.setSpacing(false);
            vl.setSizeFull();
            c.setHeader(vl);
        });
        System.out.println();
        add(grid);
        setSizeFull();

    }
}
