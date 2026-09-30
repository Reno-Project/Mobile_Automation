package com.mobile.automation.flows;

import com.mobile.automation.config.CapabilitiesConfig;
import com.mobile.automation.db.DbHelper;
import com.mobile.automation.pages.SignupPage;

import java.util.concurrent.ThreadLocalRandom;

/**
 * End-to-end RenoHome signup sequence.
 */
public class SignupFlow {

    private final SignupPage signupPage;

    public SignupFlow() {
        this.signupPage = new SignupPage();
    }

    /**
     * Runs the full signup journey through Welcome to Reno.
     */
    public void completeSignup() {
        String name = CapabilitiesConfig.getSignupName();
        String emailLocal = CapabilitiesConfig.getSignupEmailLocal();
        String password = CapabilitiesConfig.getSignupPassword();
        String phone = generatePhoneStartingWithFive();

        signupPage.handleUpdateAppCtaIfPresent();
        signupPage.waitSeconds(1);

        signupPage.clickContinueWithEmail();
        signupPage.clickSignupLink();
        // Country screen appears only after Signup click + short wait inside clickSignupLink
        signupPage.validateSelectCountryScreen();
        signupPage.selectUnitedArabEmirates();

        signupPage.validateTellUsAboutYourselfScreen();
        signupPage.selectRenovateHome();

        signupPage.validateWhatIsYourNameScreen();
        signupPage.enterName(name);
        signupPage.clickContinueIfEnabled();

        signupPage.waitSeconds(2);
        signupPage.validateWhatIsYourEmailScreen();
        signupPage.enterEmailLocalPart(emailLocal);
        signupPage.selectGmailDomain();
        signupPage.clickEmailContinueIfEnabled();

        signupPage.validateSetupPasswordScreen();
        signupPage.enterPassword(password);
        signupPage.clickPasswordContinueIfEnabled();

        signupPage.validatePhoneScreen();
        System.out.println("Using phone number: " + phone);
        signupPage.enterPhoneNumber(phone);
        signupPage.clickPhoneContinueIfEnabled();

        // Fetch OTP only after the OTP screen is visible, so the query returns the new code.
        signupPage.waitForOtpEntryScreen();
        String otp = DbHelper.fetchLatestOtp();
        signupPage.enterOtp(otp);

        signupPage.skipFaceIdIfPresent();
        signupPage.handleNotificationsIfPresent();
        signupPage.assertWelcomeScreen();
    }

    private static String generatePhoneStartingWithFive() {
        int rest = ThreadLocalRandom.current().nextInt(100_000_00, 1_000_000_00);
        return "5" + rest;
    }
}
