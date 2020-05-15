package com.stas.tourManager.frontend.views;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

public abstract class AddParticipantForm extends Dialog {
    protected final TextField firstName = new TextField("First name");
    protected final TextField middleName = new TextField("Middle name");
    protected final TextField lastName = new TextField("Last name");

    protected final Button saveButton = new Button("save");
    protected final Button cancelButton = new Button("cancel");
    protected final Button deleteButton = new Button("delete");

    protected HorizontalLayout buttonsLayout = new HorizontalLayout(saveButton, cancelButton, deleteButton);
    protected HorizontalLayout fieldsLayout = new HorizontalLayout(firstName, lastName);
    protected HorizontalLayout fieldsLayout2 = new HorizontalLayout(middleName);
    protected VerticalLayout mainLayout = new VerticalLayout(fieldsLayout, fieldsLayout2, buttonsLayout);

    public AddParticipantForm(boolean withDelete) {
        if(!withDelete)
            deleteButton.setVisible(false);

        cancelButton.addClickListener(e -> close());
        // setup key shortcuts
        saveButton.addClickShortcut(Key.ENTER);
        deleteButton.addClickShortcut(Key.DELETE);

        saveButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        firstName.setSizeFull();
        middleName.setSizeFull();
        lastName.setSizeFull();

        mainLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        setCloseOnOutsideClick(false);
        setCloseOnEsc(true);
    }

    protected void init(){
        add(mainLayout);
    }
}
