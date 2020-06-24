package com.stas.tourManager.ui.components;


import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Locale;

import static com.stas.tourManager.ui.views.ListToursView.DATE_TIME_FORMAT;

@StyleSheet("https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.css")
@JavaScript("https://cdnjs.cloudflare.com/ajax/libs/jquery/3.4.1/jquery.min.js")
@JavaScript("https://cdn.jsdelivr.net/momentjs/latest/moment.min.js")
@JavaScript("https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.min.js")
public class DateRangePickerField extends TextField {

    private static DateTime startDate, endDate;

    public DateRangePickerField() {
        this("");
    }

    public DateRangePickerField(String label) {
        super(label);
        setValueChangeMode(ValueChangeMode.EAGER);
    }

    public static DateTime getStartDate() {
        return startDate;
    }

    public static void setStartDate(DateTime startDate) {
        DateRangePickerField.startDate = startDate;
    }

    public static DateTime getEndDate() {
        return endDate;
    }

    public static void setEndDate(DateTime endDate) {
        DateRangePickerField.endDate = endDate;
    }

    protected void onAttach(AttachEvent event) {
        super.onAttach(event);
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
        // executing JS should be avoided in constructor
        getElement().executeJs(String.format(script, startDate.toString(DATE_TIME_FORMAT, Locale.US), endDate.toString(DATE_TIME_FORMAT, Locale.US)), getElement());

    }



//    public void initPicker(DateTime startDate, DateTime endDate) {
//        var script = "$($0).daterangepicker({\n" +
//                "        timePicker: true,\n" +
//                "        timePicker24Hour: true,\n" +
//                "        timePickerIncrement: 5,\n" +
//                "        autoApply: true,\n" +
//                "        cancelLabel: 'Clear',\n" +
//                "        opens: 'left',\n" +
//                "        drops: 'auto',\n" +
//                "        startDate: '%s',\n" +
//                "        endDate: '%s',\n" +
//                "        locale: {\n" +
//                "           format: 'DD.MMM.YYYY HH:mm',\n" +
//                "           cancelLabel: 'Clear'\n" +
//                "        }\n" +
//                "    }, function (start, end, label) {\n" +
//                "        const pattern = \"DD.MMM.YYYY HH:mm\";\n" +
//                "        $($0).val(start.format(pattern)+'-'+end.format(pattern));\n" +
//                "    });\n" +
//                "$($0).on('cancel.daterangepicker', function(ev, picker) {\n" +
//                "      $(this).val('');\n" +
//                "  });";
//
//        if (startDate == null && endDate == null) {
//            var s = String.format(script, DateTime.now().withHourOfDay(0).withMinuteOfHour(0).toString(DATE_TIME_FORMAT, Locale.US),
//                    DateTime.now().withHourOfDay(0).withMinuteOfHour(0).toString(DATE_TIME_FORMAT, Locale.US));
//            UI.getCurrent().getPage().executeJs(s);
//
//            UI.getCurrent().getPage().executeJs("$($0).val('')");
//
//        } else {
//            var s = String.format(script, startDate.toString(DATE_TIME_FORMAT, Locale.US),
//                    endDate.toString(DATE_TIME_FORMAT, Locale.US));
//            UI.getCurrent().getPage().executeJs(s);
//
//            UI.getCurrent().getPage()
//                    .executeJs("$('$0').val('" + startDate.toString(DATE_TIME_FORMAT, Locale.US) + "-" + endDate.toString(DATE_TIME_FORMAT, Locale.US) + "')");
//        }
//    }
}
