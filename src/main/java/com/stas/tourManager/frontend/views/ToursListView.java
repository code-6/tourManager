package com.stas.tourManager.frontend.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "tours", layout = MainLayout.class)
public class ToursListView extends VerticalLayout {
    public ToursListView() {
        add(new H1("this is tours list view"));
    }
}
