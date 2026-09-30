package com.mobile.automation.base;

import com.mobile.automation.config.CapabilitiesConfig;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.time.Duration;

/**
 * Creates and holds the Appium AndroidDriver session.
 */
public final class DriverManager {

    private static AndroidDriver driver;

    private DriverManager() {
    }

    public static void startDriver() {
        if (driver != null) {
            return;
        }

        UiAutomator2Options options = CapabilitiesConfig.buildAndroidOptions();
        URL serverUrl = toUrl(CapabilitiesConfig.getAppiumServerUrl());
        driver = new AndroidDriver(serverUrl, options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    public static AndroidDriver getDriver() {
        if (driver == null) {
            throw new IllegalStateException("Driver is not started. Call startDriver() first.");
        }
        return driver;
    }

    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    private static URL toUrl(String url) {
        try {
            return URI.create(url).toURL();
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid Appium server URL: " + url, e);
        }
    }
}
