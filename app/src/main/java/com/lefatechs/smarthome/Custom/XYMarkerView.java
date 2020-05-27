package com.lefatechs.smarthome.Custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;

import com.lefatechs.lefachart.components.MarkerView;
import com.lefatechs.lefachart.data.Entry;
import com.lefatechs.lefachart.formatter.ValueFormatter;
import com.lefatechs.lefachart.highlight.Highlight;
import com.lefatechs.lefachart.utils.MPPointF;
import com.lefatechs.smarthome.R;

import java.text.DecimalFormat;


@SuppressLint("ViewConstructor")
public class XYMarkerView extends MarkerView {

    private final TextView tvContent;
    private final ValueFormatter xAxisValueFormatter;

    private final DecimalFormat format;

    public XYMarkerView(Context context, ValueFormatter xAxisValueFormatter) {
        super(context, R.layout.custom_marker_view);

        this.xAxisValueFormatter = xAxisValueFormatter;
        tvContent = findViewById(R.id.tvContent);
        format = new DecimalFormat("###.0");
    }

    // runs every time the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {

        //tvContent.setText(String.format("Time: %s, Value: %s", xAxisValueFormatter.getFormattedValue(e.getX()), format.format(e.getY())));
        tvContent.setText(String.format("Value: %s", format.format(e.getY())));

        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}
