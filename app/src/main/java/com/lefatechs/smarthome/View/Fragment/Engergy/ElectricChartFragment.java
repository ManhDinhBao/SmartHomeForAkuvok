package com.lefatechs.smarthome.View.Fragment.Engergy;

import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.lefatechs.lefachart.charts.BarChart;
import com.lefatechs.lefachart.components.Legend;
import com.lefatechs.lefachart.components.XAxis;
import com.lefatechs.lefachart.components.YAxis;
import com.lefatechs.lefachart.data.BarData;
import com.lefatechs.lefachart.data.BarDataSet;
import com.lefatechs.lefachart.data.BarEntry;
import com.lefatechs.lefachart.data.Entry;
import com.lefatechs.lefachart.formatter.ValueFormatter;
import com.lefatechs.lefachart.highlight.Highlight;
import com.lefatechs.lefachart.interfaces.datasets.IBarDataSet;
import com.lefatechs.lefachart.listener.OnChartValueSelectedListener;
import com.lefatechs.lefachart.utils.Fill;
import com.lefatechs.lefachart.utils.MPPointF;
import com.lefatechs.smarthome.Custom.HourAxisValueFormatter;
import com.lefatechs.smarthome.Custom.MyValueFormatter;
import com.lefatechs.smarthome.Custom.XYMarkerView;
import com.lefatechs.smarthome.R;

import java.util.ArrayList;
import java.util.List;

public class ElectricChartFragment extends Fragment implements OnChartValueSelectedListener {
    public static BarChart chart;
    protected Typeface tfRegular;
    protected static Typeface tfLight;
    private int chartColor;

    private final RectF onValueSelectedRectF = new RectF();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tfRegular = ResourcesCompat.getFont(getActivity().getBaseContext(), R.font.sf_ui_display_regular);
        tfLight = ResourcesCompat.getFont(getActivity().getBaseContext(), R.font.sf_ui_display_light);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_chart, container, false);

        chartColor = ContextCompat.getColor(getActivity().getBaseContext(),R.color.white);

        chart = view.findViewById(R.id.chart_fragmentChart);
        chart.setOnChartValueSelectedListener(this);

        chart.setDrawBarShadow(false);
        chart.setDrawValueAboveBar(false);

        chart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        chart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        chart.setPinchZoom(false);
        chart.setDoubleTapToZoomEnabled(false);
        chart.setScaleYEnabled(false);

        chart.setDrawGridBackground(false);
        // chart.setDrawYLabels(false);

        //ValueFormatter xAxisFormatter = new DayAxisValueFormatter(chart);
        //ValueFormatter xAxisFormatter = new MonthAxisValueFormatter(chart);
        ValueFormatter xAxisFormatter = new HourAxisValueFormatter(chart);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(tfRegular);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(2f); // only intervals of 1 day
        xAxis.setLabelCount(24);
        xAxis.setValueFormatter(xAxisFormatter);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setTextSize(getActivity().getResources().getDimension(R.dimen.chart_xaxis_textsize));

        ValueFormatter custom = new MyValueFormatter("kwh");

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setTypeface(tfLight);
        //leftAxis.setDrawGridLines(false);
        leftAxis.setLabelCount(8, false);
        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setTextSize(getActivity().getResources().getDimension(R.dimen.chart_xaxis_textsize));

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);
        rightAxis.setDrawLabels(false);

        Legend l = chart.getLegend();
        l.setEnabled(false);
//        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
//        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
//        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
//        l.setDrawInside(false);
//        l.setForm(Legend.LegendForm.SQUARE);
//        l.setFormSize(9f);
//        l.setTextSize(11f);
//        l.setXEntrySpace(4f);

        XYMarkerView mv = new XYMarkerView(getActivity().getApplicationContext(), xAxisFormatter);
        mv.setChartView(chart); // For bounds control
        chart.setMarker(mv); // Set the marker to the chart
        setData(24,50, chart);

        return  view;
    }

    public void setData(int count, float range, BarChart chart) {

        float start = 1f;

        ArrayList<BarEntry> values = new ArrayList<>();

        for (int i = (int) start; i < start + count; i++) {
            float val = (float) (Math.random() * (range + 1));
            if (Math.random() * 100 < 25) {
                values.add(new BarEntry(i, val, getResources().getDrawable(R.drawable.star)));
            } else {
                values.add(new BarEntry(i, val));
            }
        }

        BarDataSet set1;

        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();

        } else {
            set1 = new BarDataSet(values, "Electric energy 2019");

            set1.setDrawIcons(false);

            List<Fill> gradientFills = new ArrayList<>();
            gradientFills.add(new Fill(chartColor, chartColor));

            set1.setFills(gradientFills);

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextColor(Color.WHITE);
            data.setValueTextSize(getActivity().getResources().getDimension(R.dimen.chart_values_textsize));
            data.setValueTypeface(tfLight);
            data.setBarWidth(0.9f);
            data.setDrawValues(false);

            chart.setData(data);
        }
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        if (e == null)
            return;

        RectF bounds = onValueSelectedRectF;
        chart.getBarBounds((BarEntry) e, bounds);
        MPPointF position = chart.getPosition(e, YAxis.AxisDependency.LEFT);

        Log.i("bounds", bounds.toString());
        Log.i("position", position.toString());

        Log.i("x-index",
                "low: " + chart.getLowestVisibleX() + ", high: "
                        + chart.getHighestVisibleX());

        MPPointF.recycleInstance(position);
    }

    @Override
    public void onNothingSelected() {

    }
}
