package com.stas.tourManager.frontend.views.test;

import java.io.Serializable;

import org.vaadin.addons.datetimerangepicker.type.DateTimeRange;

public class DateTimeRangeBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private DateTimeRange dateTimeRange = new DateTimeRange();

    public DateTimeRangeBean(DateTimeRange dateTimeRange) {
        setDateTimeRange(dateTimeRange);
    }

    public DateTimeRange getDateTimeRange() {
        return this.dateTimeRange;
    }

    public void setDateTimeRange(final DateTimeRange dateTimeRange) {
        this.dateTimeRange = dateTimeRange;
    }

}
