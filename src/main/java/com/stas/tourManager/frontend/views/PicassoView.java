package com.stas.tourManager.frontend.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "", layout = MainLayout.class)
public class PicassoView extends VerticalLayout {
    public PicassoView() {
        add(new H1("This is Picasso page"));
    }

}
