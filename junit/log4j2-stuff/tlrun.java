
package csw.jutils.junit_tests;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;


public class tlrun {

    public static void main (String[] args) {
        Result result = JUnitCore.runClasses (tlogg.class);
        for (Failure failure : result.getFailures()) {
            System.out.println (failure.toString());
        }
    }

}
