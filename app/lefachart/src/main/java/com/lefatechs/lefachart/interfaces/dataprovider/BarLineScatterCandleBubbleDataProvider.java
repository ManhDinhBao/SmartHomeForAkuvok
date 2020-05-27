package com.lefatechs.lefachart.interfaces.dataprovider;

import com.lefatechs.lefachart.components.YAxis.AxisDependency;
import com.lefatechs.lefachart.data.BarLineScatterCandleBubbleData;
import com.lefatechs.lefachart.utils.Transformer;

public interface BarLineScatterCandleBubbleDataProvider extends ChartInterface {

    Transformer getTransformer(AxisDependency axis);
    boolean isInverted(AxisDependency axis);
    
    float getLowestVisibleX();
    float getHighestVisibleX();

    BarLineScatterCandleBubbleData getData();
}
