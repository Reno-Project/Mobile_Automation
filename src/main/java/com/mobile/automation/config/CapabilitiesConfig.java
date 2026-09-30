package com.mobile.automation.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Loads config.properties and builds Appium capabilities in one place.
 */
public final class CapabilitiesConfig {

    private static final Properties PROPERTIES = loadProperties();

    private CapabilitiesConfig() {
    }

    public static String get(String key) {
        String value = PROPERTIES.getProperty(key);
        return value == null ? "" : value.trim();
    }

    public static String get(String key, String defaultValue) {
        String value = get(key);
        return value.isEmpty() ? defaultValue : value;
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        String value = get(key);
        if (value.isEmpty()) {
            return defaultValue;
        }
        return Boolean.parseBoolean(value);
    }

    public static int getInt(String key, int defaultValue) {
        String value = get(key);
        if (value.isEmpty()) {
            return defaultValue;
        }
        return Integer.parseInt(value);
    }

    public static String getAppiumServerUrl() {
        return get("appium.server.url", "http://127.0.0.1:4723/wd/hub");
    }

    public static String getUsername() {
        return get("login.username");
    }

    public static String getPassword() {
        return get("login.password");
    }

    public static String getSignupNamePrefix() {
        return get("signup.name.prefix", "sarthak Reno");
    }

    public static String getSignupEmailPrefix() {
        return get("signup.email.prefix", "sarthakreno");
    }

    public static String getSignupPassword() {
        return get("signup.password", "Sarthak@123");
    }

    public static String getDbUrl() {
        return get("db.url");
    }

    public static String getDbUsername() {
        return get("db.username");
    }

    public static String getDbPassword() {
        return get("db.password");
    }

    /**
     * Builds UiAutomator2 capabilities for Android.
     * Prefer app.path when set; otherwise use app.package + app.activity.
     */
    public static io.appium.java_client.android.options.UiAutomator2Options buildAndroidOptions() {
        io.appium.java_client.android.options.UiAutomator2Options options =
                new io.appium.java_client.android.options.UiAutomator2Options();

        options.setPlatformName(get("platform.name", "Android"));
        options.setAutomationName(get("automation.name", "UiAutomator2"));
        options.setDeviceName(get("device.name", "Android Emulator"));

        String udid = get("udid");
        if (!udid.isEmpty()) {
            options.setUdid(udid);
        }

        String appPath = get("app.path");
        if (!appPath.isEmpty()) {
            options.setApp(appPath);
        } else {
            String appPackage = get("app.package");
            String appActivity = get("app.activity");
            if (!appPackage.isEmpty()) {
                options.setAppPackage(appPackage);
            }
            if (!appActivity.isEmpty()) {
                options.setAppActivity(appActivity);
            }
        }

        options.setNoReset(getBoolean("no.reset", true));
        options.setFullReset(getBoolean("full.reset", false));
        options.setNewCommandTimeout(java.time.Duration.ofSeconds(
                getInt("new.command.timeout", 300)));

        return options;
    }

    private static Properties loadProperties() {
        Properties properties = new Properties();
        try (InputStream input = CapabilitiesConfig.class
                .getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new IllegalStateException(
                        "config.properties not found on classpath (expected under src/test/resources)");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load config.properties", e);
        }
        return properties;
    }
}
