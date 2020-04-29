package com.stas.tourManager.frontend.views;

import com.stas.tourManager.backend.persistance.pojos.Guide;
import com.stas.tourManager.backend.persistance.pojos.Language;
import com.stas.tourManager.backend.persistance.services.GuideService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
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
    private ComboBox<Language> language = new ComboBox<>();

    private final Button saveButton = new Button("save");
    private final Button cancelButton = new Button("cancel");
    private final Button deleteButton = new Button("delete");
    // used to fill form with selected object and bind properties
    private Binder<Guide> guideBinder = new BeanValidationBinder<>(Guide.class);
    // todo: replace to free component
    private ConfirmDialog dialog = new ConfirmDialog("Confirm delete",
            "Are you sure you want to delete the item? This action can't be undone!",
            "Delete", this::delete, "Cancel", this::close);

    @Autowired
    private GuideService guideService;

    public AddGuideView(boolean withDelete, Guide guide, GuideService guideService) {
        dialog.setConfirmButtonTheme("error primary");
        this.guideService = guideService;
        // show form with or without delete button. If create new guide then no need delete button.
        if (!withDelete)
            deleteButton.setVisible(false);

        guideBinder.bindInstanceFields(this);
        // fill form with guide data by using data binder.
        //guideBinder.forField(language).bind("language");

        configureButtons();
        configureInputFields();
        guideBinder.setBean(guide);
//        // old form fill
//        firstName.setValue(guide.getFirstName());
//        // fix null exception
//        middleName.setValue((guide.getMiddleName() == null) ? "" : guide.getMiddleName());
//        lastName.setValue(guide.getLastName());
//        language.setValue((guide.getLanguage() == null) ? "" : guide.getLanguage());
        var hl = new HorizontalLayout(saveButton, cancelButton, deleteButton);
        var hl2 = new HorizontalLayout(firstName, lastName);
        var hl3 = new HorizontalLayout(middleName, language);
        var vl = new VerticalLayout(hl2, hl3, hl);
        // set buttons to center.
        vl.setAlignItems(FlexComponent.Alignment.CENTER);
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
        saveButton.addClickShortcut(Key.ENTER);

        deleteButton.addClickListener(event -> {
            dialog.open();
        });
    }

    private void close(ConfirmDialog.CancelEvent cancelEvent) {
        dialog.close();
    }

    // todo: implement method properly, don't forget to update list after delete.
    private void delete(ConfirmDialog.ConfirmEvent confirmEvent) {
        System.out.println("GUIDE: "+guideBinder.getBean().getFullName()+" deleted!");
        close();
    }

    /**
     *
     */
    private void save() {
        if (guideBinder.isValid()) {
            var g = guideBinder.getBean();
            guideService.saveOrUpdate(g);
        }
    }

    private void configureInputFields() {
        // configure input fields
        firstName.setSizeFull();
        middleName.setSizeFull();
        lastName.setSizeFull();

        language.setItems(Language.getLangList());
        language.setItemLabelGenerator(Language::getLang);
        language.setAllowCustomValue(true);
        language.addCustomValueSetListener(event -> {
            var source = event.getDetail();
            try {
                var lang = Language.createLang(source);
                // FIX bug #001. When save language that not exist yet.
                language.setValue(lang);
            } catch (Language.InvalidLanguageException e) {
                // todo: display error notification to user.
            }
        });
        language.setClearButtonVisible(true);
        language.setAutofocus(false);
        language.setLabel("Language");
    }
}
