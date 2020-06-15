package com.stas.tourManager.frontend.views;

import com.vaadin.flow.data.renderer.BasicRenderer;
import com.vaadin.flow.function.ValueProvider;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;

import java.time.LocalDateTime;
import java.util.Locale;

public class DateTimeRenderer<SOURCE> extends BasicRenderer<SOURCE, DateTime> {

    private DateTimeFormatter formatter;
    private String nullRepresentation;

    /**
     * Builds a new template renderer using the value provider as the source of
     * values to be rendered.
     *
     * @param valueProvider the callback to provide a objects to the renderer, not
     *                      <code>null</code>
     */
    protected DateTimeRenderer(ValueProvider<SOURCE, DateTime> valueProvider) {
        super(valueProvider);
    }

    public DateTimeRenderer(
            ValueProvider<SOURCE, DateTime> valueProvider, DateTimeFormatter formatter) {
        this(valueProvider, formatter, "");
    }

    public DateTimeRenderer(
            ValueProvider<SOURCE, DateTime> valueProvider, DateTimeFormatter formatter, String nullRepresentation) {
        super(valueProvider);

        if (formatter == null) {
            throw new IllegalArgumentException("formatter may not be null");
        }

        this.formatter = formatter;
        this.nullRepresentation = nullRepresentation;
    }

    @Override
    protected String getFormattedValue(DateTime dateTime) {
        return dateTime == null ? nullRepresentation
                : formatter.print(dateTime);
    }
}
