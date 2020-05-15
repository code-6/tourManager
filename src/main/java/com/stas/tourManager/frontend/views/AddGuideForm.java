package com.stas.tourManager.frontend.views;

import com.stas.tourManager.backend.persistance.pojos.Guide;
import com.stas.tourManager.backend.persistance.pojos.Language;
import com.stas.tourManager.backend.persistance.services.GuideService;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import org.springframework.beans.factory.annotation.Autowired;

public class AddGuideForm extends AddParticipantForm {
    private ComboBox<Language> language = new ComboBox<>();
    private Binder<Guide> binder = new BeanValidationBinder<>(Guide.class);

    @Autowired
    private final GuideService guideService;

    public AddGuideForm(boolean withDelete, Guide guide, GuideService guideService) {
        super(withDelete);

        binder.bindInstanceFields(this);
        binder.setBean(guide);

        fieldsLayout2.add(language);

        this.guideService = guideService;

        setupButtons();
        setupComboBox();

        init();
    }

    private void setupButtons() {
        saveButton.addClickListener(event -> {
            save();
            ListGuidesView.updateTable();
            close();
        });

        deleteButton.addClickListener(event -> {
            new MyConfirmDialog("Confirm dialog", "r u sure?", e->{
                System.out.println("YAHOOO! Confirmed");
                close();
            }).open();
        });
    }

    private void save() {
        if (binder.isValid()) {
            Guide g = binder.getBean();
            guideService.saveOrUpdate(g);
        }
    }

    private void setupComboBox() {
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
