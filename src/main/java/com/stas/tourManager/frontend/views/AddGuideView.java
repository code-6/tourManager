package com.stas.tourManager.frontend.views;

import com.stas.tourManager.backend.persistance.pojos.Guide;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;

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

    public AddGuideView(boolean withDelete, Guide guide) {
        if(!withDelete)
            deleteButton.setVisible(false);

        guideBinder.bindInstanceFields(this);

        saveButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        firstName.setValue(guide.getFirstName());
        middleName.setValue(guide.getMiddleName());
        lastName.setValue(guide.getLastName());
        language.setValue(guide.getLanguage());

        var hl = new HorizontalLayout(saveButton, cancelButton, deleteButton);
        var vl = new VerticalLayout(firstName, middleName, lastName, language, hl);
        add(vl);

    }

    public void setGuide(Guide guide){
        guideBinder.setBean(guide);
    }
}
