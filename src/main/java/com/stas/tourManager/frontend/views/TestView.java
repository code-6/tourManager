package com.stas.tourManager.frontend.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("")
public class TestView extends VerticalLayout {

    public TestView() {
        add(new H1("Hello world from tour manager!"));
    }
}
