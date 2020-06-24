package com.stas.tourManager.frontend.components;


import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import org.joda.time.DateTime;

import java.util.Locale;

import static com.stas.tourManager.frontend.views.ListToursView.DATE_TIME_FORMAT;

@StyleSheet("https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.css")
@JavaScript("https://cdnjs.cloudflare.com/ajax/libs/jquery/3.4.1/jquery.min.js")
@JavaScript("https://cdn.jsdelivr.net/momentjs/latest/moment.min.js")
@JavaScript("https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.min.js")
public class DateRangePickerField extends TextField {
    public DateRangePickerField() {
        this("");
    }

    public DateRangePickerField(String label) {
        super(label);
        setValueChangeMode(ValueChangeMode.EAGER);
    }

    public void init(DateTime start, DateTime end){
        var script = "$($0).daterangepicker({\n" +
                "        timePicker: true,\n" +
                "        timePicker24Hour: true,\n" +
                "        timePickerIncrement: 5,\n" +
                "        autoApply: true,\n" +
                "        cancelLabel: 'Clear',\n" +
                "        opens: 'left',\n" +
                "        drops: 'auto',\n" +
                "        startDate: '%s',\n" +
                "        endDate: '%s',\n" +
                "        locale: {\n" +
                "           format: 'DD.MMM.YYYY HH:mm',\n" +
                "           cancelLabel: 'Clear'\n" +
                "        }\n" +
                "    }, function (start, end, label) {\n" +
                "        const pattern = \"DD.MMM.YYYY HH:mm\";\n" +
                "        $($0).val(start.format(pattern)+'-'+end.format(pattern));\n" +
                "    });\n" +
                "$($0).on('cancel.daterangepicker', function(ev, picker) {\n" +
                "      $(this).val('');\n" +
                "  });";
        getElement().executeJs(String.format(script, start.toString(DATE_TIME_FORMAT, Locale.US),
                end.toString(DATE_TIME_FORMAT, Locale.US)), getElement());
    }

    protected void onAttach(AttachEvent event) {
        super.onAttach(event);
        // executing JS should be avoided in constructor
        getElement().executeJs("$($0).daterangepicker({\n" +
                        "        timePicker: true,\n" +
                        "        timePicker24Hour: true,\n" +
                        "        timePickerIncrement: 5,\n" +
                        "        autoApply: true,\n" +
                        "        cancelLabel: 'Clear',\n" +
                        "        opens: 'left',\n" +
                        "        drops: 'auto',\n" +
                        "        locale: {\n" +
                        "           format: 'DD.MMM.YYYY HH:mm',\n" +
                        "           cancelLabel: 'Clear'\n" +
                        "        }\n" +
                        "    }, function (start, end, label) {\n" +
                        "        const pattern = \"DD.MMM.YYYY HH:mm\";\n" +
                        "        $($0).val(start.format(pattern)+'-'+end.format(pattern));\n" +
                        "    });\n" +
                        "$($0).on('cancel.daterangepicker', function(ev, picker) {\n" +
                        "      $(this).val('');\n" +
                        "  });", getElement());
    }
}
