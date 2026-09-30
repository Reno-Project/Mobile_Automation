package com.mobile.automation.tests;

import com.mobile.automation.base.BaseTest;
import com.mobile.automation.base.DriverManager;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Manual smoke: only launches the app and confirms the session is alive.
 */
public class LaunchAppTest extends BaseTest {

    @Test(description = "Launch RenoHome on the connected device")
    public void shouldLaunchApp() {
        String packageName = DriverManager.getDriver().getCurrentPackage();
        Assert.assertEquals(packageName, "com.renohome", "App package should be com.renohome");
    }
}
