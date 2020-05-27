package com.lefatechs.lefachart.interfaces.dataprovider;

import com.lefatechs.lefachart.components.YAxis;
import com.lefatechs.lefachart.data.LineData;

public interface LineDataProvider extends BarLineScatterCandleBubbleDataProvider {

    LineData getLineData();

    YAxis getAxis(YAxis.AxisDependency dependency);
}
