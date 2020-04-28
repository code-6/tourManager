package com.stas.tourManager.frontend.views;

import com.stas.tourManager.backend.persistance.pojos.Guide;
import com.stas.tourManager.backend.persistance.services.GuideService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Route(value = "guides", layout = MainLayout.class)
public class GuidesListView extends VerticalLayout {
    private Logger logger = LoggerFactory.getLogger(GuidesListView.class);
    private static GuideService guideService;
    private static Grid<Guide> grid = new Grid<>(Guide.class);
    public GuidesListView(GuideService guideService) {
        this.guideService = guideService;
        initTable();
        setSizeFull();
    }

    /**
     * @// FIXME: 4/28/20 bad solution. Used in add view to update list after apply changes in row
     * */
    public static void updateTable(){
        grid.setItems(guideService.getGuides());
    }

    /**
     * @// TODO: 4/27/20 add lazy data loading.
     */
    private void initTable() {

        var guidesList = guideService.getGuides();
        grid.addThemeVariants(GridVariant.LUMO_COMPACT);

//        grid.asSingleSelect().addValueChangeListener(e -> {
//            addClickShortcut(Key.KEY_E);
//            new AddGuideView(true).open();
//        });

        grid.setSizeFull();
        grid.setColumns("fullName", "language");
        // add edit button to each row. todo: implement edit logic.
        grid.addComponentColumn(g -> {
            var editButton = new Button("edit");
            editButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
            editButton.addClickListener(e -> new AddGuideView(true, g, guideService).open());
            return editButton;
        });

        // set content fit to columns
        grid.getColumns().forEach(c -> c.setAutoWidth(true));
        grid.setItems(guidesList);
        add(grid);
    }
}
