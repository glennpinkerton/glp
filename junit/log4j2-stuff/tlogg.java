	
package  csw.jutils.junit_tests;
 
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
 
public class tlogg {
 
    @Rule
    public TestWatcher watchman = new Log4jTestWatcher();
 
    @Test
    public void testFails() {
        // test
        Assert.fail();
    }
 
    @Test
    public void testSucceeds() {
        // test
    }

    @Test
    public void testSucceeds2() {
        // test
    }

    @Test
    public void testSucceeds3() {
        // test
    }

    @Test
    public void testSucceeds4() {
        // test
    }

    @Test
    public void testFails2() {
        // test
        Assert.fail();
    }
 
    @Test
    public void testFails3() {
        // test
        Assert.fail();
    }
 
    @Test
    public void testSucceeds5() {
        // test
    }

}
