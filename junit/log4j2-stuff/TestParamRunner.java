
package csw.jutils.junit_tests;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;


public class TestParamRunner {

    public static void main (String[] args) {
        Result result = JUnitCore.runClasses (TestParam.class);
        for (Failure failure : result.getFailures()) {
            System.out.println (failure.toString());
        }
    }

}
