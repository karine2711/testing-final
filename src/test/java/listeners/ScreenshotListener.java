package listeners;

import base.BaseTest;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class ScreenshotListener extends BaseTest implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {
        try {
            takeScreenshot(result.getInstanceName() + result.getName());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
