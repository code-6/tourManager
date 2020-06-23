package com.stas.tourManager.frontend.views.option1.cdn;


import com.stas.tourManager.frontend.views.MainLayout;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Input;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.dom.ElementFactory;
import com.vaadin.flow.router.Route;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutionException;

@Route(value = "test/cdn", layout = MainLayout.class)
@StyleSheet("https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.css")
@JavaScript("https://cdnjs.cloudflare.com/ajax/libs/jquery/3.4.1/jquery.min.js")
@JavaScript("https://cdn.jsdelivr.net/momentjs/latest/moment.min.js")
@JavaScript("https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.min.js")
//@JavaScript("./resources/static/js/test-jquery.js")
public class JqueryTestView extends VerticalLayout {
    private Button button = new Button("Open Modal");
    private H1 title = new H1("Test Page");
    private Input textField = new Input();

    @PostConstruct
    public void init() {
        textField.setPlaceholder("click here");
        textField.setMinWidth("285px");
        textField.setId("daterange");

        button.addClickListener(click -> {
            var textFieldDialog = new TextField("Date-time");
            textFieldDialog.setPlaceholder("click here");
            textFieldDialog.setMinWidth("285px");
            textFieldDialog.setId("daterange2");

            var window = new Dialog();
            window.add(textFieldDialog);
            window.setCloseOnOutsideClick(false);

            window.open();

            window.getElement().executeJs("$(function () {\n" +
                    "    $('#daterange2').daterangepicker({\n" +
                    "        timePicker: true,\n" +
                    "        timePicker24Hour: true,\n" +
                    "        timePickerIncrement: 5,\n" +
                    "        autoApply: true,\n" +
                    "        cancelLabel: 'Clear',\n" +
                    "        drops: 'auto'\n" +
                    "    }, function (start, end, label) {\n" +
                    "        const pattern = \"DD.MM.YYYY HH:mm\";\n" +
                    "        $('#daterange').val(start.format(pattern)+'-'+end.format(pattern));\n" +
                    "    });\n" +
                    "});").then(event -> {
                Notification.show("AFTER EXECUTE JS : " + event.asString(), 3000, Notification.Position.BOTTOM_END);
            });
        });
        var button2 = new Button("GetValue");
        button2.addClickListener(click -> {

            getElement().executeJs("return $('#daterange').val()").then(e -> {
                System.out.println(e.toJson());
                textField.setValue(e.toJson().replaceAll("\"", ""));
                Notification.show("VALUE IN FIELD: " + textField.getValue(), 3000, Notification.Position.BOTTOM_START);
            });
        });
        add(title, button, textField, button2);
    }

    protected void onAttach(AttachEvent event) {
        super.onAttach(event);
        // executing JS should be avoided in constructor
        getElement().executeJs("$(function () {\n" +
                "    $('#daterange').daterangepicker({\n" +
                "        timePicker: true,\n" +
                "        timePicker24Hour: true,\n" +
                "        timePickerIncrement: 5,\n" +
                "        autoApply: true,\n" +
                "        cancelLabel: 'Clear',\n" +
                "        drops: 'auto'\n" +
                "    }, function (start, end, label) {\n" +
                "        const pattern = \"DD.MM.YYYY HH:mm\";\n" +
                "        $('#daterange').val(start.format(pattern)+'-'+end.format(pattern));\n" +
                "    });\n" +
                "});").then(e -> {
            Notification.show(" onAttach AFTER EXECUTE JS : " + e.asString(), 3000, Notification.Position.BOTTOM_END);
        });
    }
    //"$('#daterange').find( \"input\" ).val(start.format('YYYY-MM-DD')+ ' ' +end.format('YYYY-MM-DD'))"+
}
