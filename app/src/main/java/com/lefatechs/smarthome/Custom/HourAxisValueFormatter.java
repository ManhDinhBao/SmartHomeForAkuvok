package com.lefatechs.smarthome.Custom;

import com.lefatechs.lefachart.charts.BarLineChartBase;
import com.lefatechs.lefachart.formatter.ValueFormatter;

public class HourAxisValueFormatter extends ValueFormatter {
    private String amSuffix="AM";
    private String pmSuffix="PM";

    private final BarLineChartBase<?> chart;

    public HourAxisValueFormatter(BarLineChartBase<?> chart) {
        this.chart = chart;
    }

    @Override
    public String getFormattedValue(float value) {

        value-=1;
        String hourValue="";
        if(value>24){
            return "";
        }
        else{
            if (value>=0 && value<12){
                String sValue = String.valueOf((int)value);
                hourValue = sValue+amSuffix;
            }
            else if (value>12 && value<24){
                String sValue = String.valueOf((int)value-12);
                hourValue = sValue+pmSuffix;
            }
            else if (value==12) {
                String sValue = String.valueOf((int)value);
              hourValue = sValue+pmSuffix;
            }
            return hourValue;
        }
    }
}
