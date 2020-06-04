package com.stas.tourManager.frontend.views.option1.cdn;


import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Input;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.dom.ElementFactory;
import com.vaadin.flow.router.Route;


@Route("test/cdn")
@StyleSheet("https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.css")
@JavaScript("https://cdnjs.cloudflare.com/ajax/libs/jquery/3.4.1/jquery.min.js")
@JavaScript("https://cdn.jsdelivr.net/momentjs/latest/moment.min.js")
@JavaScript("https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.min.js")
//@JavaScript("./resources/static/js/test-jquery.js")
public class JqueryTestView extends VerticalLayout {
    private String value = "Test";
    public JqueryTestView() {
        H1 title = new H1("Test Page");
        // daterange picker works with html input, but not with vaadin textField, why?
//        Input input = new Input();
//        input.setType("text");
//        input.setClassName("daterange");
//        input.addClassName("daterange");
        TextField textField = new TextField("Date-time");
        textField.setMinWidth("285px");
        textField.setId("daterange");

        add(title, textField);
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
                "        cancelLabel: 'Clear'\n" +
                "    }, function (start, end, label) {\n" +
                "        const pattern = \"DD-MM-YYYY HH:mm\";\n" +
                "        $('#daterange').val(start.format(pattern)+'-'+end.format(pattern));\n" +
                "    });\n" +
                "});").then(e-> {

        });
    }
    //"$('#daterange').find( \"input\" ).val(start.format('YYYY-MM-DD')+ ' ' +end.format('YYYY-MM-DD'))"+
}
