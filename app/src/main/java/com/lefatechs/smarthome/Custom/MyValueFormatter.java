package com.lefatechs.smarthome.Custom;

import com.lefatechs.lefachart.components.AxisBase;
import com.lefatechs.lefachart.components.XAxis;
import com.lefatechs.lefachart.formatter.ValueFormatter;

import java.text.DecimalFormat;

public class MyValueFormatter extends ValueFormatter
{

    private final DecimalFormat mFormat;
    private String suffix;

    public MyValueFormatter(String suffix) {
        mFormat = new DecimalFormat("###,###,###,##0.0");
        this.suffix = suffix;
    }

    @Override
    public String getFormattedValue(float value) {
        return mFormat.format(value) + suffix;
    }

    @Override
    public String getAxisLabel(float value, AxisBase axis) {
        if (axis instanceof XAxis) {
            return mFormat.format(value);
        } else if (value > 0) {
            return mFormat.format(value) + suffix;
        } else {
            return mFormat.format(value);
        }
    }
}
