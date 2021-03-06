package com.stas.tourManager.frontend.views;

import com.stas.tourManager.backend.persistance.pojos.Guide;
import com.stas.tourManager.backend.persistance.services.LanguageService;
import com.stas.tourManager.backend.persistance.services.GuideService;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class AddGuideForm extends AddParticipantForm {
    private ComboBox<String> language = new ComboBox<>();
    private Binder<Guide> binder = new BeanValidationBinder<>(Guide.class);
    private final Logger log = LoggerFactory.getLogger(AddGuideForm.class);

    @Autowired
    private final GuideService guideService;

    public AddGuideForm(boolean withDelete, String title, Guide guide, GuideService guideService) {
        super(withDelete, title);
        setupComboBox();
        binder.setBean(guide);
        fieldsLayout2.add(language);
        this.guideService = guideService;
        setupButtons();

        init();

        binder.bindInstanceFields(this);

    }

    private void setupButtons() {
        binder.addStatusChangeListener(evt -> saveButton.setEnabled(binder.isValid() && binder.hasChanges()));

        saveButton.addClickListener(event -> {
            save();
            ListGuidesView.updateTable();
            close();
        });

        deleteButton.addClickListener(event -> {
            new MyConfirmDialog("Confirm dialog", "r u sure?", e -> {
                System.out.println("YAHOOO! Confirmed");
                close();
            }).open();
        });
    }

    @Override
    protected void init() {
        super.init();
    }

    private void save() {
        if (binder.isValid()) {
            Guide g = binder.getBean();
            guideService.saveOrUpdate(g);
        } else {
            log.debug("Binder invalid");
        }
    }

    private void setupComboBox() {
        language.setItems(LanguageService.getLangList());
        // use if value is not primitive datatype
//        language.setItemLabelGenerator(LanguageService::getLang);
        language.setAllowCustomValue(true);
        language.addCustomValueSetListener(event -> {
            var source = event.getDetail();

            try {
                var lang = LanguageService.createLang(source);
                // FIX bug #001. When save language that not exist yet.
                language.setValue(lang);
                language.setInvalid(false);
                language.setErrorMessage(null);
            } catch (LanguageService.InvalidLanguageException e) {
                log.debug("Language: "+source+" is invalid!");
                language.setInvalid(true);
                language.setErrorMessage("Enter ISO 639 language name");
            }
        });
//        binder.forField(language)
//                .withValidator(l -> LanguageService.isValid(language.getValue()), "Invalid language!")
//                .bind(Guide::getLanguage, Guide::setLanguage);

        language.setClearButtonVisible(true);
        language.setAutofocus(false);
        language.setLabel("Language");

    }
}
