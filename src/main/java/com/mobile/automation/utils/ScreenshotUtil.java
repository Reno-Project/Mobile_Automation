package com.mobile.automation.utils;

import com.mobile.automation.base.DriverManager;
import org.openqa.selenium.OutputType;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Saves Appium screenshots under target/screenshots/.
 * Never throws — if Appium/session is dead, logs and returns empty.
 */
public final class ScreenshotUtil {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS");

    private ScreenshotUtil() {
    }

    public static String capture(String namePrefix) {
        try {
            Path dir = Paths.get("target", "screenshots");
            Files.createDirectories(dir);

            String safeName = namePrefix.replaceAll("[^a-zA-Z0-9-_]", "_");
            Path file = dir.resolve(safeName + "_" + LocalDateTime.now().format(FORMATTER) + ".png");

            byte[] bytes = DriverManager.getDriver().getScreenshotAs(OutputType.BYTES);
            Files.write(file, bytes);

            System.out.println("Screenshot saved: " + file.toAbsolutePath());
            return file.toAbsolutePath().toString();
        } catch (Exception e) {
            // Session may already be dead (UnreachableBrowserException / ConnectException)
            System.out.println("Screenshot skipped (session unreachable): " + e.getClass().getSimpleName()
                    + " - " + e.getMessage());
            return "";
        }
    }
}
