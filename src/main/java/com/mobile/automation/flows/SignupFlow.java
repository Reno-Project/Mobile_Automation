package com.mobile.automation.flows;

import com.mobile.automation.config.CapabilitiesConfig;
import com.mobile.automation.db.DbHelper;
import com.mobile.automation.pages.SignupPage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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
        int runNumber = nextRunNumber();
        String name = CapabilitiesConfig.getSignupNamePrefix() + " " + runNumber;
        String emailLocal = CapabilitiesConfig.getSignupEmailPrefix() + runNumber;
        String password = CapabilitiesConfig.getSignupPassword();
        String phone = generatePhoneStartingWithFive();
        System.out.println("Using name: " + name);
        System.out.println("Using email: " + emailLocal + "@gmail.com");

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

    /**
     * Reads signup-counter.txt, uses that number for this run, then stores the next one.
     */
    private static int nextRunNumber() {
        Path counterFile = Path.of(System.getProperty("user.dir"), "signup-counter.txt");
        int current = 1;
        try {
            if (Files.exists(counterFile)) {
                String text = Files.readString(counterFile).trim();
                if (!text.isEmpty()) {
                    current = Integer.parseInt(text);
                }
            }
            Files.writeString(counterFile, Integer.toString(current + 1));
        } catch (IOException | NumberFormatException e) {
            System.out.println("Could not update signup counter, using " + current + ": " + e.getMessage());
        }
        return current;
    }

    private static String generatePhoneStartingWithFive() {
        int rest = ThreadLocalRandom.current().nextInt(100_000_00, 1_000_000_00);
        return "5" + rest;
    }
}
