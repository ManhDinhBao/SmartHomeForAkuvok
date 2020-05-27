package com.lefatechs.lefachart.renderer.scatter;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.lefatechs.lefachart.interfaces.datasets.IScatterDataSet;
import com.lefatechs.lefachart.utils.ViewPortHandler;

/**
 * Created by
 */
public interface IShapeRenderer
{

    /**
     * Renders the provided ScatterDataSet with a shape.
     *
     * @param c               Canvas object for drawing the shape
     * @param dataSet         The DataSet to be drawn
     * @param viewPortHandler Contains information about the current state of the view
     * @param posX            Position to draw the shape at
     * @param posY            Position to draw the shape at
     * @param renderPaint     Paint object used for styling and drawing
     */
    void renderShape(Canvas c, IScatterDataSet dataSet, ViewPortHandler viewPortHandler,
                     float posX, float posY, Paint renderPaint);
}
