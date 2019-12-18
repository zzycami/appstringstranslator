package run.app.translator.googleTranslate;

import org.junit.runner.*;
import org.junit.runner.notification.Failure;


public class TestRunner {
    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(TestGoogleTranslate.class);
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }
        System.out.println("测试结果："+result.wasSuccessful());
    }
}
