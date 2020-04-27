package com.stas.tourManager.frontend.views;

import com.stas.tourManager.backend.persistance.pojos.Guide;
import com.stas.tourManager.backend.persistance.services.GuideService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.selection.SingleSelect;
import com.vaadin.flow.router.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

@Route(value = "guides", layout = MainLayout.class)
public class GuidesListView extends VerticalLayout {
    private Logger logger = LoggerFactory.getLogger(GuidesListView.class);
    private GuideService guideService;

    public GuidesListView(GuideService guideService) {
        this.guideService = guideService;
        initTable();
        setSizeFull();
    }

    /**
     * @// TODO: 4/27/20 add lazy data loading.
     */
    private void initTable() {
        var grid = new Grid<>(Guide.class);
        grid.setColumns("id", "fullName", "language");
        var guidesList = guideService.getGuides();
        grid.setItems(guidesList);
        // add edit button to each row. todo: implement edit logic.
        grid.addComponentColumn(g -> {
            var editButton = new Button("edit");
            editButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
            // todo: why guide is null? Find out how to pass chosen guide on edit press.
            editButton.addClickListener(e -> new AddGuideView(true, g).open());
            return editButton;
        });

        // set content fit to columns
        grid.getColumns().forEach(c -> c.setAutoWidth(true));

        grid.addThemeVariants(GridVariant.LUMO_COMPACT);

//        grid.asSingleSelect().addValueChangeListener(e -> {
//            addClickShortcut(Key.KEY_E);
//            new AddGuideView(true).open();
//        });

        grid.setSizeFull();
        add(grid);
    }
}
