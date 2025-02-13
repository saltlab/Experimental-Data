package org.jfree.chart.renderer.xy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import java.awt.Color;
import org.jfree.chart.TestUtilities;
import org.jfree.chart.renderer.LookupPaintScale;
import org.jfree.data.Range;
import org.jfree.data.xy.DefaultXYZDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.junit.Test;

/**
 * Tests for the {@link XYShapeRenderer} class.
 */
public class XYShapeRendererTest {

    /**
     * Some checks for the equals() method.
     */
    public void testEquals() {
        XYShapeRenderer r1 = new XYShapeRenderer();
        XYShapeRenderer r2 = new XYShapeRenderer();
        r1.setPaintScale(new LookupPaintScale(1.0, 2.0, Color.white));
        r2.setPaintScale(new LookupPaintScale(1.0, 2.0, Color.white));
        r1.setDrawOutlines(true);
        r2.setDrawOutlines(true);
        r1.setUseOutlinePaint(false);
        r2.setUseOutlinePaint(false);
        r1.setUseFillPaint(true);
        r2.setUseFillPaint(true);
        r1.setGuideLinesVisible(true);
        r2.setGuideLinesVisible(true);
        r1.setGuideLinePaint(Color.red);
        r2.setGuideLinePaint(Color.red);
    }

    /**
     * Confirm that cloning works.
     */
    public void testCloning() throws CloneNotSupportedException {
        XYShapeRenderer r1 = new XYShapeRenderer();
        XYShapeRenderer r2 = (XYShapeRenderer) r1.clone();
        assertTrue(r1 != r2);
        assertTrue(r1.getClass() == r2.getClass());
        assertTrue(r1.equals(r2));
    }

    /**
     * Serialize an instance, restore it, and check for equality.
     */
    public void testSerialization() {
        XYShapeRenderer r1 = new XYShapeRenderer();
        XYShapeRenderer r2 = (XYShapeRenderer) TestUtilities.serialised(r1);
        assertEquals(r1, r2);
    }

    private static final double EPSILON = 0.0000000001;

    /**
     * Check if finding the bounds in z-dimension of an XYZDataset works. 
     */
    public void testFindZBounds() {
        XYShapeRenderer r = new XYShapeRenderer();
        assertNull(r.findZBounds(null));
        DefaultXYZDataset dataset = new DefaultXYZDataset();
        Range range;
        double data1[][] = { { 1, 1, 1 }, { 1, 1, 1 }, { 1, 2, 3 } };
        dataset.addSeries("series1", data1);
        range = r.findZBounds(dataset);
        assertNotNull(range);
        assertEquals(1d, range.getLowerBound(), EPSILON);
        assertEquals(3d, range.getUpperBound(), EPSILON);
        double data2[][] = { { 1, 1, 1 }, { 1, 1, 1 }, { -1, -2, -3 } };
        dataset.removeSeries("series1");
        dataset.addSeries("series2", data2);
        range = r.findZBounds(dataset);
        assertNotNull(range);
        assertEquals(-3d, range.getLowerBound(), EPSILON);
        assertEquals(-1d, range.getUpperBound(), EPSILON);
        double data3[][] = { { 1, 1, 1 }, { 1, 1, 1 }, { -1.2, 2.9, 3.8 } };
        dataset.removeSeries("series2");
        dataset.addSeries("series3", data3);
        range = r.findZBounds(dataset);
        assertNotNull(range);
        assertEquals(-1.2d, range.getLowerBound(), EPSILON);
        assertEquals(3.8d, range.getUpperBound(), EPSILON);
    }

    /**
     * Test for bug 3026341.
     */
    public void test3026341() {
        XYShapeRenderer renderer = new XYShapeRenderer();
        assertNull(renderer.findRangeBounds(null));
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries series = new XYSeries("S1");
        series.add(1.0, null);
        dataset.addSeries(series);
        Range r = renderer.findRangeBounds(dataset);
        assertNull(r);
        r = renderer.findDomainBounds(dataset);
        assertEquals(r.getLowerBound(), 1.0, EPSILON);
        assertEquals(r.getUpperBound(), 1.0, EPSILON);
        dataset.removeAllSeries();
        r = renderer.findDomainBounds(dataset);
        assertNull(r);
    }
}
