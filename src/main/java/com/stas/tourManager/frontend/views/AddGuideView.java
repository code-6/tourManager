package com.stas.tourManager.frontend.views;

import com.stas.tourManager.backend.persistance.pojos.Guide;
import com.stas.tourManager.backend.persistance.services.GuideService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import org.springframework.beans.factory.annotation.Autowired;

public class AddGuideView extends Dialog {
    private final TextField firstName = new TextField("First name");
    private final TextField middleName = new TextField("Middle name");
    private final TextField lastName = new TextField("Last name");
    private final TextField language = new TextField("Language");

    private final Button saveButton = new Button("save");
    private final Button cancelButton = new Button("cancel");
    private final Button deleteButton = new Button("delete");
    // used to fill form with selected object and bind properties
    private Binder<Guide> guideBinder = new BeanValidationBinder<>(Guide.class);

    @Autowired
    private GuideService guideService;

    public AddGuideView(boolean withDelete, Guide guide, GuideService guideService) {
        this.guideService = guideService;
        // show form with or without delete button. If create new guide then no need delete button.
        if (!withDelete)
            deleteButton.setVisible(false);

        guideBinder.bindInstanceFields(this);
        // fill form with guide data by using data binder.
        guideBinder.setBean(guide);

        configureButtons();
        configureInputFields();
//        // old form fill
//        firstName.setValue(guide.getFirstName());
//        // fix null exception
//        middleName.setValue((guide.getMiddleName() == null) ? "" : guide.getMiddleName());
//        lastName.setValue(guide.getLastName());
//        language.setValue((guide.getLanguage() == null) ? "" : guide.getLanguage());
        var hl = new HorizontalLayout(saveButton, cancelButton, deleteButton);
        var vl = new VerticalLayout(firstName, middleName, lastName, language, hl);
        add(vl);
        setCloseOnOutsideClick(false);
        setCloseOnEsc(true);
    }

    private void configureButtons() {
        // configure buttons
        saveButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        // close dialog windows on cancel button press
        cancelButton.addClickListener(event -> close());
        saveButton.addClickListener(event -> {
            save();
            GuidesListView.updateTable();
            close();
        });
    }

    private void save() {
        if (guideBinder.isValid()) {
            var g = guideBinder.getBean();
            guideService.updateGuide(g.getId(), g.getFirstName(), g.getMiddleName(), g.getLastName(), g.getLanguage());
        }
    }

    private void configureInputFields() {
        // configure input fields
        firstName.setSizeFull();
        middleName.setSizeFull();
        lastName.setSizeFull();
        language.setSizeFull();
    }
}
