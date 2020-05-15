package com.stas.tourManager.frontend.views;

import com.stas.tourManager.backend.persistance.pojos.Guide;
import com.stas.tourManager.backend.persistance.services.GuideService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
// FIXME: 5/2/20 make this class reusable for all kind of list views
@Route(value = "guides", layout = MainLayout.class)
public class ListGuidesView extends VerticalLayout {
    private Logger logger = LoggerFactory.getLogger(ListGuidesView.class);
    private static GuideService guideService;
    private static Grid<Guide> grid = new Grid<>(Guide.class);
    private final Button createButton = new Button("add");

    public ListGuidesView(GuideService guideService) {
        this.guideService = guideService;
        initTable();
        setSizeFull();
    }

    /**
     * @// FIXME: 4/28/20 bad solution. Used in add view to update list after apply changes in row
     */
    public static void updateTable() {
        grid.setItems(guideService.getGuides());
    }

    private void initCreateButton() {
        createButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_SMALL);
        var plusIcon = VaadinIcon.PLUS.create();
        createButton.setIcon(plusIcon);
        createButton.addClickListener(e -> new AddGuideForm(false, new Guide(), guideService).open());
    }

    /**
     * @// TODO: 4/27/20 add lazy data loading.
     */
    private void initTable() {
        initCreateButton();
        var guidesList = guideService.getGuides();
        grid.addThemeVariants(GridVariant.LUMO_COMPACT);

//        grid.asSingleSelect().addValueChangeListener(e -> {
//            addClickShortcut(Key.KEY_E);
//            new AddGuideView(true).open();
//        });

        grid.setSizeFull();
        try {
            grid.removeColumnByKey("language");
        } catch (IllegalArgumentException e) {
            logger.error("unable to remove column 'language'.\n" + e.getMessage());
        }
        /* note that set columns shall follow right after remove column. otherwise possible illegalStateException.
        * FIX: 4/29/20 resolve IllegalArgumentException when deleting column 'language'
        * */
        grid.setColumns("fullName");
        grid.addColumn(g -> {
            var lang = g.getLanguage();
            return lang == null ? "" : lang.getLang();
        }).setHeader("Language").setSortable(true);
        // add edit button to each row.
        grid.addComponentColumn(g -> {
            var editButton = new Button("edit", VaadinIcon.EDIT.create());
            editButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
            editButton.addClickListener(e -> new AddGuideForm(true, g, guideService).open());
            return editButton;
        }).setHeader(createButton);

        // set content fit to columns
        grid.getColumns().forEach(c -> c.setAutoWidth(true));
        grid.setItems(guidesList);
        add(grid);
    }
}
