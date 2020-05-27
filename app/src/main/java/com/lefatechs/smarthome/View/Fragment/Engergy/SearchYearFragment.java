package com.lefatechs.smarthome.View.Fragment.Engergy;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.lefatechs.lefachart.charts.BarChart;
import com.lefatechs.lefachart.components.XAxis;
import com.lefatechs.lefachart.data.BarData;
import com.lefatechs.lefachart.data.BarDataSet;
import com.lefatechs.lefachart.data.BarEntry;
import com.lefatechs.lefachart.formatter.ValueFormatter;
import com.lefatechs.lefachart.interfaces.datasets.IBarDataSet;
import com.lefatechs.lefachart.utils.Fill;
import com.lefatechs.smarthome.Custom.MonthAxisValueFormatter;
import com.lefatechs.smarthome.Custom.XYMarkerView;
import com.lefatechs.smarthome.R;

import java.util.ArrayList;
import java.util.List;

public class SearchYearFragment extends Fragment {
    private int chartColor;
    private Button searchButton;
    private Spinner spYear;
    protected static Typeface tfLight;

    String[] mYears = new String[]{
            "2019", "2020", "2021", "2022"
    };
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.item_energy_year, container, false);
        searchButton=view.findViewById(R.id.button_itemEnergyYear_GetData);
        spYear = view.findViewById(R.id.spinner_itemEnergyYear_Year);

        chartColor = ContextCompat.getColor(getActivity().getBaseContext(),R.color.white);
        tfLight = ResourcesCompat.getFont(getActivity().getBaseContext(), R.font.sf_ui_display_light);
        searchButton.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.amber));

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateEnergyChart();
                UpdateWaterChart();
            }
        });

        ArrayAdapter<String> spAdapterYear = new ArrayAdapter<String>(
                getActivity(),R.layout.item_spinner,mYears
        );

        spAdapterYear.setDropDownViewResource(R.layout.item_spinner);
        spYear.setAdapter(spAdapterYear);
        return view;
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
            data.setValueTextSize(getActivity().getResources().getDimension(R.dimen.chart_xaxis_textsize));
            data.setValueTypeface(tfLight);
            data.setBarWidth(0.9f);

            chart.setData(data);
        }
    }

    public void UpdateEnergyChart(){
        ValueFormatter xAxisFormatter = new MonthAxisValueFormatter(ElectricChartFragment.chart);
        XAxis xAxis = ElectricChartFragment.chart.getXAxis();
        xAxis.setLabelCount(12);
        xAxis.setValueFormatter(xAxisFormatter);
        XYMarkerView mv = new XYMarkerView(getActivity().getApplicationContext(), xAxisFormatter);
        mv.setChartView(ElectricChartFragment.chart);
        ElectricChartFragment.chart.setMarker(mv);
        setData(12,3000, ElectricChartFragment.chart);
        ElectricChartFragment.chart.invalidate();
    }

    public void UpdateWaterChart(){
        ValueFormatter xAxisFormatter = new MonthAxisValueFormatter(WaterChartFragment.chart);
        XAxis xAxis = WaterChartFragment.chart.getXAxis();
        xAxis.setLabelCount(12);
        xAxis.setValueFormatter(xAxisFormatter);
        XYMarkerView mv = new XYMarkerView(getActivity().getApplicationContext(), xAxisFormatter);
        mv.setChartView(WaterChartFragment.chart);
        WaterChartFragment.chart.setMarker(mv);
        setData(12,2400, WaterChartFragment.chart);
        WaterChartFragment.chart.invalidate();
    }
}
