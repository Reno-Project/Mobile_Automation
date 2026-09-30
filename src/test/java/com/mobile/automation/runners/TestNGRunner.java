package com.mobile.automation.runners;

import org.testng.TestNG;

import java.nio.file.Paths;
import java.util.Collections;

/**
 * Run this class (main method) from the IDE to execute testng.xml manually.
 */
public final class TestNGRunner {

    private TestNGRunner() {
    }

    public static void main(String[] args) {
        TestNG testng = new TestNG();
        String suitePath = Paths.get("testng.xml").toAbsolutePath().toString();
        testng.setTestSuites(Collections.singletonList(suitePath));
        testng.run();
    }
}
