package com.stas.tourManager.frontend.views;

import com.stas.tourManager.frontend.views.option1.cdn.TestView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.dom.ThemeList;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.lumo.Lumo;
import com.vaadin.server.Page;

import java.util.ArrayList;
import java.util.List;

@PWA(name = "Tour Manager", iconPath = "img/icons/android-chrome-512x512.png", shortName = "TM")
public class MainLayout extends AppLayout {
    // how to initialize current dynamicaly on tab change?
    IFilter current;
    static List<IFilter> views = new ArrayList<>();

    public static void registerView(IFilter view) {
        views.add(view);
    }

    public static void removeView(IFilter view) {
        views.removeIf(v -> v.equals(view));
    }

    public MainLayout() {
        initHeader();
    }

    private void initHeader() {

        var main = new Tab();
        main.setId(MainView.class.getName());
        var toursList = new Tab();
        toursList.setId(ListToursView.class.getName());
        var driversList = new Tab();
        driversList.setId(ListDriversView.class.getName());
        var guidesList = new Tab();
        guidesList.setId(ListGuidesView.class.getName());
        var test = new Tab();

        main.add(new RouterLink("Main", MainView.class));
        toursList.add(new RouterLink("Tours", ListToursView.class));
        driversList.add(new RouterLink("Drivers", ListDriversView.class));
        guidesList.add(new RouterLink("Guides", ListGuidesView.class));
        test.add(new RouterLink("TEST", TestView.class));

        var tabs = new Tabs(main, toursList, driversList, guidesList, test);

        tabs.addSelectedChangeListener(e -> {
            var currentTab = e.getSelectedTab();
            for (IFilter v : views) {
                var tabId = currentTab.getId();
                if (tabId.isPresent()) {
                    if (v.getClass().getName().equalsIgnoreCase(tabId.get())) {
                        current = v;
                        break;
                    }

                }
            }
        });

        var hl = new HorizontalLayout(tabs);
        hl.setWidthFull();
        hl.setAlignItems(FlexComponent.Alignment.START);

        var changeTheme = new Button("Dark side");
        changeTheme.addThemeVariants(ButtonVariant.LUMO_SMALL);
        changeTheme.addClickListener(click -> {
            ThemeList themeList = UI.getCurrent().getElement().getThemeList(); //

            if (themeList.contains(Lumo.DARK)) { //
                themeList.remove(Lumo.DARK);
                changeTheme.setText("Dark side");
            } else {
                themeList.add(Lumo.DARK);
                changeTheme.setText("Light side");
            }
        });

        var logout = new Button("logout");
        logout.addThemeVariants(ButtonVariant.LUMO_SMALL);
        logout.addClickListener(click -> {
            Notification.show("Not implemented yet", 3000, Notification.Position.MIDDLE);
        });

        var filterField = new TextField();
        filterField.setPlaceholder("type to filter table");
        filterField.setValueChangeMode(ValueChangeMode.EAGER);
        filterField.addValueChangeListener(text -> {
            if (current != null)
                if (text.getValue() == null || text.getValue().isEmpty())
                    current.updateGrid();
                else
                    current.filter(text.getValue());
        });

        var hl2 = new HorizontalLayout(filterField, changeTheme, logout);
        hl2.setAlignItems(FlexComponent.Alignment.BASELINE);
        addToNavbar(hl, hl2);
    }

}

