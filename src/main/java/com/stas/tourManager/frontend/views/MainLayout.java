package com.stas.tourManager.frontend.views;

import com.stas.tourManager.frontend.views.option1.cdn.TestView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.dom.ThemeList;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.lumo.Lumo;

@PWA(name = "Picasso", iconPath = "img/icons/android-chrome-192x192.png", shortName = "PCS")
public class MainLayout extends AppLayout {
    public MainLayout() {
        initHeader();
    }

    private void initHeader() {
        var main = new Tab();
        var toursList = new Tab();
        var driversList = new Tab();
        var guidesList = new Tab();
        var test = new Tab();

        main.add(new RouterLink("Main", MainView.class));
        toursList.add(new RouterLink("Tours", ListToursView.class));
        driversList.add(new RouterLink("Drivers", ListDriversView.class));
        guidesList.add(new RouterLink("Guides", ListGuidesView.class));
        test.add(new RouterLink("TEST", TestView.class));

        var tabs = new Tabs(main, toursList, driversList, guidesList, test);

        var hl = new HorizontalLayout(tabs);
        hl.setWidthFull();
        hl.setAlignItems(FlexComponent.Alignment.START);

        var button = new Button("Dark side");
        button.addThemeVariants(ButtonVariant.LUMO_SMALL);
        button.addClickListener(click -> {
            ThemeList themeList = UI.getCurrent().getElement().getThemeList(); //

            if (themeList.contains(Lumo.DARK)) { //
                themeList.remove(Lumo.DARK);
                button.setText("Dark side");
            } else {
                themeList.add(Lumo.DARK);
                button.setText("Light side");
            }
        });

        var search = new TextField();
        search.setPlaceholder("type to search");

        var hl2 = new HorizontalLayout(search, button);
        hl2.setAlignItems(FlexComponent.Alignment.BASELINE);
        addToNavbar(hl, hl2);
    }

}

