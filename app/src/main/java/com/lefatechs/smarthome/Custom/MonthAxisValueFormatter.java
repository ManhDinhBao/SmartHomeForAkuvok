package com.lefatechs.smarthome.Custom;

import com.lefatechs.lefachart.charts.BarLineChartBase;
import com.lefatechs.lefachart.formatter.ValueFormatter;

public class MonthAxisValueFormatter extends ValueFormatter {

    private final String[] mMonths = new String[]{
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    };

    private final BarLineChartBase<?> chart;

    public MonthAxisValueFormatter(BarLineChartBase<?> chart) {
        this.chart = chart;
    }

    @Override
    public String getFormattedValue(float value) {

        int position = (int) value;
        if (position>12){
            return "";
        }else{
            String monthName = mMonths[position-1];

            return monthName;
        }

        //return String.valueOf(value);
    }

}
