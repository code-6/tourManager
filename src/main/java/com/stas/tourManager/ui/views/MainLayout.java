package com.stas.tourManager.ui.views;

import com.stas.tourManager.ui.views.option1.cdn.TestView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.PWA;

@PWA(name = "Picasso", iconPath = "img/icons/android-chrome-192x192.png", shortName = "PCS")
public class MainLayout extends AppLayout {
    public MainLayout() {
        initHeader();
    }

    private void initHeader() {
        var picasso = new Tab();
        var toursList = new Tab();
        var driversList = new Tab();
        var guidesList = new Tab();
        var test = new Tab();

        picasso.add(new RouterLink("Picasso", PicassoView.class));
        toursList.add(new RouterLink("Tours", ListToursView.class));
        driversList.add(new RouterLink("Drivers", ListDriversView.class));
        guidesList.add(new RouterLink("Guides", ListGuidesView.class));
        test.add(new RouterLink("TEST", TestView.class));

        var tabs = new Tabs(picasso, toursList, driversList, guidesList, test);
        addToNavbar(tabs);

    }

}

