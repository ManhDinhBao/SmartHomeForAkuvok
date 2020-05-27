package com.lefatechs.lefachart.interfaces.datasets;

import com.lefatechs.lefachart.data.Entry;

/**
 * Created by ManhDB.
 */
public interface IBarLineScatterCandleBubbleDataSet<T extends Entry> extends IDataSet<T> {

    /**
     * Returns the color that is used for drawing the highlight indicators.
     *
     * @return
     */
    int getHighLightColor();
}
