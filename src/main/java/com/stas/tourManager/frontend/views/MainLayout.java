package com.stas.tourManager.frontend.views;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.RouterLink;

public class MainLayout extends AppLayout {
    public MainLayout() {
        initHeader();
    }

    private void initHeader() {
        var picasso = new Tab();
        var toursList = new Tab();
        var driversList = new Tab();
        var guidesList = new Tab();

        picasso.add(new RouterLink("Picasso", PicassoView.class));
        toursList.add(new RouterLink("Tours", ListToursView.class));
        driversList.add(new RouterLink("Drivers", ListDriversView.class));
        guidesList.add(new RouterLink("Guides", ListGuidesView.class));

        var tabs = new Tabs(picasso, toursList, driversList, guidesList);
        addToNavbar(tabs);

    }
}

