package com.stas.tourManager.frontend.views.option1.cdn;

import com.github.appreciated.apexcharts.ApexCharts;
import com.github.appreciated.apexcharts.ApexChartsBuilder;
import com.github.appreciated.apexcharts.config.builder.*;
import com.github.appreciated.apexcharts.config.chart.Type;
import com.github.appreciated.apexcharts.config.plotoptions.builder.BarBuilder;
import com.github.appreciated.apexcharts.config.xaxis.XAxisType;
import com.github.appreciated.apexcharts.config.yaxis.AxisBorder;
import com.github.appreciated.apexcharts.config.yaxis.AxisTicks;
import com.github.appreciated.apexcharts.helper.DateCoordinate;
import com.github.appreciated.apexcharts.helper.Series;
import com.stas.tourManager.backend.persistance.pojos.Tour;
import com.stas.tourManager.backend.persistance.services.TourService;
import com.stas.tourManager.frontend.views.MainLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.joda.time.DateTime;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Route(value = "apex", layout = MainLayout.class)
public class TestApexCharts extends Div {
    DateCoordinate[] list;
    List<Tour> tours;


    private TourService tourService;

    public TestApexCharts(TourService tourService) {
        this.tourService = tourService;

        tours = tourService.getByMonthAndYear(DateTime.now().getMonthOfYear(), 2020);
        ApexCharts barChart = ApexChartsBuilder.get()
                .withChart(ChartBuilder.get()
                        .withType(Type.rangeBar)
                        .build())
                .withPlotOptions(PlotOptionsBuilder.get()
                        .withBar(BarBuilder.get()
                                .withHorizontal(true)
                                .build())
                        .build())
                .withDataLabels(DataLabelsBuilder.get()
                        .withEnabled(false)
                        .build())
                .withSeries(getSeries())
                .withYaxis(YAxisBuilder.get()
                        .withMin(LocalDate.of(2020, DateTime.now().getMonthOfYear(), 1))
                        .withMax(LocalDate.of(2020, DateTime.now().getMonthOfYear(), DateTime.now().dayOfMonth().getMaximumValue()))
                        .build())
                .withXaxis(XAxisBuilder.get()
                        .withType(XAxisType.datetime)
                        .build())
                .build();
        barChart.setWidth("1000px");
        barChart.setHeight("500px");
        add(barChart);
        setSizeFull();
    }

    private Series<DateCoordinate> getSeries(){
        list = new DateCoordinate[tours.size()];
        AtomicInteger i = new AtomicInteger();
        tours.forEach(t->{
            list[i.get()] = new DateCoordinate<>(t.getTitle(), joda2java(t.getFrom()), joda2java(t.getTo()));
            i.getAndIncrement();
        });
        Series<DateCoordinate> s = new Series<>();
        s.setData(list);
        return s;
    }

    private LocalDate joda2java(DateTime date){
        return LocalDate.of(date.getYear(), date.getMonthOfYear(), date.getDayOfMonth());
    }
}





