
package csw.jutils.junit_tests;

import org.junit.Test;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.rules.TestWatcher;


import csw.jutils.src.Bounds2D;



public class TestLogger {


    @Rule
    public TestWatcher watchman = new Log4jTestWatcher();

    @Test
    public void testExpand() {

        for (int i=0; i<4; i++) {
            Bounds2D  b2d = new Bounds2D (0.0, 0.0, 200.0, 100.0);
            double  epx = (double)(i + 1) * 5.0;
            double  epy = (double)(i + 1) * 10.0;
            b2d.expandByPercentage (epx, epy);
            epx /= 100.0;
            epy /= 100.0;
            double  w, h, w2, h2;
            w2 = 200.0 * (1. + epx);
            h2 = 100.0 * (1. + epy);
            w = b2d.getWidth();
            h = b2d.getHeight();
            assertEquals(w2, w, 0.001);
            assertEquals(h2, h, 0.001);
        }

        for (int i=0; i<4; i++) {
            Bounds2D  b2d = new Bounds2D (0.0, 0.0, 200.0, 100.0);
            double  epx = (double)(i + 1) * -5.0;
            double  epy = (double)(i + 1) * -10.0;
            b2d.expandByPercentage (epx, epy);
            epx /= 100.0;
            epy /= 100.0;
            double  w, h, w2, h2;
            w2 = 200.0 * (1. + epx);
            h2 = 100.0 * (1. + epy);
            w = b2d.getWidth();
            h = b2d.getHeight();
            assertEquals(w2, w, 0.001);
            assertEquals(h2, h, 0.001);
        }

    }

}
