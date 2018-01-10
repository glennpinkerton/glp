/*
 *  This class has a simple use of the Parameters stuff in junit.
 *  I must admit that this framework seems quite complicatged 
 *  relative to what I see as its usefulness.  
 */

package csw.jutils.junit_tests;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.*;

import java.util.Arrays;
import java.util.Collection;

import csw.jutils.src.Bounds2D;


@RunWith(Parameterized.class)
public class TestParam {

// Public parameter fields

    @Parameter(0)
    public double epx;
    @Parameter(1)
    public double epy;


    @Parameters   
    public static Collection<Object[]> data() {
        Object[][] data = new Object[][] {{10.,10.}, {20., 10.}, {10., 20.}};
        return Arrays.asList(data);
    } 

public static int   nrun = 1;

    @Test
    public void testExpand() {
        Bounds2D  b2d = new Bounds2D (0.0, 0.0, 200.0, 100.0);
        b2d.expandByPercentage (epx, epy);
        double  w, h;
        w = b2d.getWidth();
        h = b2d.getHeight();
        double  w2, h2;
        w2 = 200.0 * (1 + epx / 100.0);
        h2 = 100.0 * (1 + epy / 100.0);
        assertEquals(w2, w, 0.001);
        assertEquals(h2, h, 0.001);
System.out.println ("Run through parameterized stuff number " + nrun);
nrun++;
    }

}
