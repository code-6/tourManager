package com.stas.tourManager.frontend.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "drivers", layout = MainLayout.class)
public class DriversListView extends VerticalLayout {
    public DriversListView() {
        add(new H1("this is drivers list view"));
    }
}
