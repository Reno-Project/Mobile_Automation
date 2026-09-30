package com.mobile.automation.tests;

import com.mobile.automation.base.BaseTest;
import com.mobile.automation.flows.SignupFlow;
import org.testng.annotations.Test;

/**
 * End-to-end signup: launch app through Welcome to Reno.
 */
public class SampleSignupTest extends BaseTest {

    @Test(description = "Complete RenoHome signup flow with DB OTP")
    public void shouldCompleteSignupSuccessfully() {
        new SignupFlow().completeSignup();
    }
}
