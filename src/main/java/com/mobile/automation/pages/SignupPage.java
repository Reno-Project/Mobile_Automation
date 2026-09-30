package com.mobile.automation.pages;

import com.mobile.automation.base.DriverManager;
import com.mobile.automation.config.CapabilitiesConfig;
import com.mobile.automation.utils.ScreenshotUtil;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

/**
 * RenoHome signup page — Android-only locators (UiAutomator2).
 * Uses @text / @content-desc / UiSelector. Never uses iOS @name / @label / @accessible.
 */
public class SignupPage {

    private final AndroidDriver driver;
    private final WebDriverWait wait;
    private final WebDriverWait shortWait;

    // ---- Exact text CTAs ----
    private static final By UPDATE_APP_CTA = androidExact("Update app");
    private static final By CONTINUE_WITH_EMAIL = androidExact("Continue with email");
    private static final By UAE_OPTION = androidExact("United Arab Emirates");
    private static final By CONTINUE_BUTTON = androidExact("Continue");
    private static final By GMAIL_DOMAIN = androidExact("@gmail.com");
    private static final By SKIP_FACE_ID = androidExact("Skip this for now");
    private static final By ENABLE_NOTIFICATIONS = androidExact("Enable notifications");
    private static final By ALLOW_BUTTON = androidExact("Allow");
    private static final By WELCOME_TO_RENO = androidExact("Welcome to Reno");

    // ---- Partial text screen markers ----
    private static final By SELECT_COUNTRY_SCREEN = androidContains("Select Your Country");

    /** Same screen markers — Android may split/alter the heading text. */
    private static final List<By> TELL_US_ABOUT_YOURSELF_LOCATORS = Arrays.asList(
            AppiumBy.androidUIAutomator("new UiSelector().textContains(\"Tell us about yourself\")"),
            AppiumBy.androidUIAutomator("new UiSelector().textContains(\"Tell us about Yourself\")"),
            AppiumBy.androidUIAutomator("new UiSelector().descriptionContains(\"Tell us about yourself\")"),
            AppiumBy.androidUIAutomator("new UiSelector().textContains(\"I want to renovate my home\")"),
            AppiumBy.androidUIAutomator("new UiSelector().descriptionContains(\"I want to renovate my home\")"),
            AppiumBy.androidUIAutomator("new UiSelector().textContains(\"interior designer\")"),
            androidContains("Tell us about yourself"),
            androidContains("Tell us about Yourself"),
            androidContains("I want to renovate my home"),
            androidContains("I own or rent a property")
    );

    private static final List<By> RENOVATE_HOME_LOCATORS = Arrays.asList(
            AppiumBy.androidUIAutomator("new UiSelector().textContains(\"I want to renovate my home\")"),
            AppiumBy.androidUIAutomator("new UiSelector().descriptionContains(\"I want to renovate my home\")"),
            AppiumBy.androidUIAutomator("new UiSelector().textContains(\"renovate my home\")"),
            androidContains("I want to renovate my home"),
            androidContains("I own or rent a property")
    );

    private static final By WHAT_IS_YOUR_NAME = androidContains("What is your name");
    private static final By WHAT_IS_YOUR_EMAIL = androidContains("What is your email");
    private static final By SETUP_PASSWORD_SCREEN = androidContains("Setup your password");
    private static final By NOTIFICATIONS_SCREEN = androidContains("Don't miss on important project updates");

    /** Phone number field only — not country code. From your Android xpath (bounds omitted). */
    private static final List<By> PHONE_INPUT_LOCATORS = Arrays.asList(
            AppiumBy.xpath("//android.widget.EditText[@text='Your phone' and @clickable='true' and @hint='Your phone']"),
            AppiumBy.xpath("//*[@class='android.widget.EditText' and @text='Your phone' and @clickable='true' and @hint='Your phone']"),
            AppiumBy.xpath("//android.widget.EditText[@hint='Your phone']"),
            AppiumBy.xpath("//android.widget.EditText[@text='Your phone']"),
            AppiumBy.androidUIAutomator("new UiSelector().className(\"android.widget.EditText\").text(\"Your phone\")"),
            AppiumBy.androidUIAutomator("new UiSelector().className(\"android.widget.EditText\").textContains(\"Your phone\")")
    );

    /** Phone screen markers — few locators so this step stays fast. */
    private static final List<By> PHONE_SCREEN_LOCATORS = Arrays.asList(
            AppiumBy.xpath("//android.widget.EditText[@hint='Your phone' or @text='Your phone']"),
            AppiumBy.androidUIAutomator("new UiSelector().textContains(\"phone number\")"),
            AppiumBy.androidUIAutomator("new UiSelector().textContains(\"Your phone\")")
    );

    /**
     * OTP entry screen only — do not match the phone screen text "OTP will be sent".
     */
    private static final List<By> OTP_SCREEN_LOCATORS = Arrays.asList(
            AppiumBy.androidUIAutomator("new UiSelector().textContains(\"Enter OTP\")"),
            AppiumBy.androidUIAutomator("new UiSelector().textContains(\"Enter the OTP\")"),
            AppiumBy.androidUIAutomator("new UiSelector().textContains(\"Enter the code\")"),
            AppiumBy.androidUIAutomator("new UiSelector().textContains(\"verification code\")"),
            AppiumBy.androidUIAutomator("new UiSelector().textContains(\"Verification code\")"),
            AppiumBy.androidUIAutomator("new UiSelector().textContains(\"Resend\")"),
            AppiumBy.androidUIAutomator("new UiSelector().descriptionContains(\"Enter OTP\")"),
            AppiumBy.androidUIAutomator("new UiSelector().descriptionContains(\"Resend\")"),
            AppiumBy.xpath("//*[@class='android.view.ViewGroup' and @bounds='[171, 1191][126, 144]' and @package='com.renohome']"),
            AppiumBy.xpath("//android.view.ViewGroup[@bounds='[171, 1191][126, 144]' and @package='com.renohome']"),
            androidContains("Enter OTP"),
            androidContains("Enter the code"),
            androidContains("Resend")
    );

    /**
     * OTP input section — ViewGroup (not always EditText). From your Android xpath + fallbacks.
     */
    private static final List<By> OTP_SECTION_LOCATORS = Arrays.asList(
            AppiumBy.xpath("//*[@class='android.view.ViewGroup' and @bounds='[171, 1191][126, 144]' and @package='com.renohome']"),
            AppiumBy.xpath("//android.view.ViewGroup[@bounds='[171, 1191][126, 144]' and @package='com.renohome']"),
            AppiumBy.androidUIAutomator("new UiSelector().textContains(\"OTP\")"),
            AppiumBy.androidUIAutomator("new UiSelector().descriptionContains(\"OTP\")"),
            AppiumBy.androidUIAutomator("new UiSelector().textContains(\"verification\")"),
            AppiumBy.androidUIAutomator("new UiSelector().textContains(\"Enter the code\")"),
            AppiumBy.androidUIAutomator("new UiSelector().className(\"android.widget.EditText\").focused(true)"),
            AppiumBy.androidUIAutomator("new UiSelector().className(\"android.widget.EditText\")"),
            AppiumBy.xpath("//android.widget.EditText"),
            AppiumBy.xpath("//android.view.ViewGroup[@clickable='true' and @package='com.renohome']")
    );

    /**
     * iOS had: //*[@name="Signup" and @label="Signup" ...]
     * Android: text / content-desc / UiSelector only.
     */
    private static final List<By> SIGNUP_LOCATORS = Arrays.asList(
            AppiumBy.androidUIAutomator("new UiSelector().text(\"Signup\")"),
            AppiumBy.androidUIAutomator("new UiSelector().description(\"Signup\")"),
            AppiumBy.androidUIAutomator("new UiSelector().textContains(\"Signup\")"),
            AppiumBy.xpath("//android.widget.TextView[@text='Signup']"),
            AppiumBy.xpath("//android.widget.TextView[@content-desc='Signup']"),
            AppiumBy.xpath("//*[@text='Signup' or @content-desc='Signup']"),
            AppiumBy.xpath("//android.widget.TextView[contains(@text,'Signup')]"),
            AppiumBy.xpath("//android.view.ViewGroup[contains(@content-desc,'Signup')]")
    );

    public SignupPage() {
        this.driver = DriverManager.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        this.shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    /**
     * Exact match on Android TextView / Button / ViewGroup / View via text or content-desc.
     */
    private static By androidExact(String value) {
        String q = quote(value);
        return AppiumBy.xpath(
                "//android.widget.TextView[@text=" + q + " or @content-desc=" + q + "]"
                        + " | //android.widget.Button[@text=" + q + " or @content-desc=" + q + "]"
                        + " | //android.widget.ImageButton[@content-desc=" + q + "]"
                        + " | //android.view.ViewGroup[@content-desc=" + q + "]"
                        + " | //android.view.View[@text=" + q + " or @content-desc=" + q + "]"
                        + " | //*[@text=" + q + " or @content-desc=" + q + "]");
    }

    private static By androidContains(String value) {
        String q = quote(value);
        return AppiumBy.xpath(
                "//android.widget.TextView[contains(@text," + q + ") or contains(@content-desc," + q + ")]"
                        + " | //android.widget.Button[contains(@text," + q + ") or contains(@content-desc," + q + ")]"
                        + " | //android.view.ViewGroup[contains(@content-desc," + q + ")]"
                        + " | //android.view.View[contains(@text," + q + ") or contains(@content-desc," + q + ")]"
                        + " | //*[contains(@text," + q + ") or contains(@content-desc," + q + ")]");
    }

    private static String quote(String value) {
        if (value.contains("'")) {
            return "\"" + value + "\"";
        }
        return "'" + value + "'";
    }

    public void handleUpdateAppCtaIfPresent() {
        try {
            WebElement cta = shortWait.until(ExpectedConditions.visibilityOfElementLocated(UPDATE_APP_CTA));
            cta.click();
            System.out.println("Update app CTA clicked");
        } catch (Exception e) {
            System.out.println("Update app CTA not visible");
        }
    }

    public void clickContinueWithEmail() {
        assertVisibleAndClick(CONTINUE_WITH_EMAIL, "Continue with email CTA is not visible");
        waitSeconds(1);
    }

    public void clickSignupLink() {
        System.out.println("Step 3: Click on the Signup label");
        WebElement element = findFirstDisplayed(SIGNUP_LOCATORS);
        if (element == null) {
            failHard("Signup text is not visible (Android text/content-desc)");
            return;
        }
        element.click();
        System.out.println("Clicked Signup label: " + safeText(element));
        waitSeconds(2);
    }

    public void validateSelectCountryScreen() {
        assertVisible(SELECT_COUNTRY_SCREEN, "Select Your Country screen is not visible");
    }

    public void selectUnitedArabEmirates() {
        assertVisibleAndClick(UAE_OPTION, "United Arab Emirates option is not visible");
        waitSeconds(3);
    }

    public void validateTellUsAboutYourselfScreen() {
        WebElement screen = findFirstDisplayed(TELL_US_ABOUT_YOURSELF_LOCATORS);
        if (screen == null) {
            failHard("Tell us about yourself screen is not visible");
            return;
        }
        System.out.println("Tell us about yourself screen visible: " + safeText(screen));
    }

    public void selectRenovateHome() {
        WebElement cta = findFirstDisplayed(RENOVATE_HOME_LOCATORS);
        if (cta == null) {
            failHard("I want to renovate my home CTA is not visible");
            return;
        }
        cta.click();
        System.out.println("Clicked renovate home CTA: " + safeText(cta));
        waitSeconds(2);
    }

    public void validateWhatIsYourNameScreen() {
        assertVisible(WHAT_IS_YOUR_NAME, "What is your name? screen is not visible");
    }

    public void enterName(String name) {
        typeIntoFirstEditText(name);
    }

    public void clickContinueIfEnabled() {
        clickContinueWhenEnabled("name screen");
    }

    public void validateWhatIsYourEmailScreen() {
        assertVisible(WHAT_IS_YOUR_EMAIL, "What is your email? screen is not visible");
    }

    public void enterEmailLocalPart(String localPart) {
        typeIntoFirstEditText(localPart);
    }

    public void selectGmailDomain() {
        assertVisibleAndClick(GMAIL_DOMAIN, "@gmail.com option is not visible");
    }

    public void clickEmailContinueIfEnabled() {
        clickContinueWhenEnabled("email screen");
    }

    public void validateSetupPasswordScreen() {
        assertVisible(SETUP_PASSWORD_SCREEN, "Setup your password screen is not visible");
    }

    public void enterPassword(String password) {
        typeIntoFirstEditText(password);
    }

    public void clickPasswordContinueIfEnabled() {
        clickContinueWhenEnabled("password screen");
        waitSeconds(1);
    }

    public void validatePhoneScreen() {
        System.out.println("Validating phone number screen...");
        WebElement screen = findFirstDisplayed(PHONE_SCREEN_LOCATORS, 8);
        if (screen == null) {
            failHard("What is your phone number screen is not visible");
            return;
        }
        System.out.println("Phone screen visible: " + safeText(screen));
    }

    public void enterPhoneNumber(String phone) {
        WebElement field = findFirstDisplayed(PHONE_INPUT_LOCATORS, 5);
        if (field == null) {
            failHard("Phone number EditText (Your phone) is not visible — avoid country code field");
            return;
        }
        field.click();
        field.clear();
        field.sendKeys(phone);
        System.out.println("Entered phone into Your phone field: " + phone);
    }

    public void clickPhoneContinueIfEnabled() {
        clickContinueWhenEnabled("phone screen");
    }

    /**
     * Blocks until the OTP entry screen is on screen. Query the DB only after this returns.
     */
    public void waitForOtpEntryScreen() {
        System.out.println("Waiting for OTP entry screen before fetching OTP from DB...");
        WebElement screen = findFirstDisplayed(OTP_SCREEN_LOCATORS, 25);
        if (screen == null) {
            failHard("OTP entry screen is not visible — OTP was not fetched");
            return;
        }
        System.out.println("OTP entry screen visible: " + safeText(screen));
        waitSeconds(2);
    }

    public void enterOtp(String otp) {
        System.out.println("Entering OTP: " + otp);
        waitSeconds(2);

        // 1) Prefer EditText if present after OTP screen loads
        if (tryTypeIntoAnyEditText(otp)) {
            System.out.println("OTP entered via EditText");
            waitSeconds(2);
            return;
        }

        // 2) Click OTP ViewGroup / related element, then type
        WebElement otpSection = findFirstDisplayed(OTP_SECTION_LOCATORS);
        if (otpSection != null) {
            try {
                otpSection.click();
                waitSeconds(1);
                if (tryTypeIntoAnyEditText(otp) || trySendKeysToElement(otpSection, otp) || tryKeyboardType(otp)) {
                    System.out.println("OTP entered after clicking OTP section");
                    waitSeconds(2);
                    return;
                }
            } catch (Exception e) {
                System.out.println("OTP section click failed: " + e.getMessage());
            }
        }

        // 3) Viewport-relative taps (bounds change by density) + keyboard
        if (tryTapOtpAreaAndType(otp)) {
            System.out.println("OTP entered via viewport tap + keyboard");
            waitSeconds(2);
            return;
        }

        // 4) Last resort: adb input text
        if (tryAdbInputText(otp)) {
            System.out.println("OTP entered via adb shell input text");
            waitSeconds(2);
            return;
        }

        failHard("OTP section is not visible / not clickable — could not enter OTP");
    }

    private boolean tryTypeIntoAnyEditText(String otp) {
        try {
            List<WebElement> editTexts = driver.findElements(AppiumBy.xpath("//android.widget.EditText"));
            for (WebElement edit : editTexts) {
                if (edit.isDisplayed()) {
                    edit.click();
                    try {
                        edit.clear();
                    } catch (Exception ignored) {
                        // some OTP fields don't support clear
                    }
                    edit.sendKeys(otp);
                    return true;
                }
            }
        } catch (Exception ignored) {
            // no EditText
        }
        return false;
    }

    private boolean trySendKeysToElement(WebElement element, String otp) {
        try {
            element.sendKeys(otp);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean tryKeyboardType(String otp) {
        try {
            new Actions(driver).sendKeys(otp).perform();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean tryTapOtpAreaAndType(String otp) {
        try {
            var size = driver.manage().window().getSize();
            // Your OTP box is mid-screen; try a few relative points
            int[][] points = {
                    {size.getWidth() * 50 / 100, size.getHeight() * 52 / 100},
                    {size.getWidth() * 40 / 100, size.getHeight() * 50 / 100},
                    {171 + 63, 1191 + 72}, // original bounds center
                    {size.getWidth() * 30 / 100, size.getHeight() * 55 / 100}
            };

            for (int[] point : points) {
                System.out.println("Tapping OTP area at x=" + point[0] + ", y=" + point[1]);
                w3cTap(point[0], point[1]);
                waitSeconds(1);
                if (tryTypeIntoAnyEditText(otp) || tryKeyboardType(otp) || tryAdbInputText(otp)) {
                    return true;
                }
            }
        } catch (Exception e) {
            System.out.println("Viewport OTP tap failed: " + e.getMessage());
        }
        return false;
    }

    private void w3cTap(int x, int y) {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence tap = new Sequence(finger, 1);
        tap.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), x, y));
        tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(java.util.Collections.singletonList(tap));
    }

    private boolean tryAdbInputText(String otp) {
        try {
            String udid = CapabilitiesConfig.get("udid");
            ProcessBuilder pb;
            if (udid != null && !udid.isBlank()) {
                pb = new ProcessBuilder("adb", "-s", udid, "shell", "input", "text", otp);
            } else {
                pb = new ProcessBuilder("adb", "shell", "input", "text", otp);
            }
            pb.redirectErrorStream(true);
            Process process = pb.start();
            int code = process.waitFor();
            return code == 0;
        } catch (Exception e) {
            System.out.println("adb input text failed: " + e.getMessage());
            return false;
        }
    }

    public void skipFaceIdIfPresent() {
        try {
            WebElement skip = shortWait.until(ExpectedConditions.elementToBeClickable(SKIP_FACE_ID));
            skip.click();
            System.out.println("Skip this for now clicked");
        } catch (Exception e) {
            System.out.println("Skip this for now not visible — continuing");
        }
    }

    public void handleNotificationsIfPresent() {
        try {
            shortWait.until(ExpectedConditions.visibilityOfElementLocated(NOTIFICATIONS_SCREEN));
            System.out.println("Notifications screen is visible");
        } catch (Exception e) {
            System.out.println("Notifications screen not visible — continuing");
            return;
        }

        try {
            shortWait.until(ExpectedConditions.elementToBeClickable(ENABLE_NOTIFICATIONS)).click();
            System.out.println("Enable notifications clicked");
        } catch (Exception e) {
            System.out.println("Enable notifications CTA not visible — continuing");
        }

        try {
            shortWait.until(ExpectedConditions.elementToBeClickable(ALLOW_BUTTON)).click();
            System.out.println("Allow popup clicked");
        } catch (Exception e) {
            System.out.println("Allow popup not visible — continuing");
        }
    }

    public void assertWelcomeScreen() {
        List<By> welcomeLocators = Arrays.asList(
                AppiumBy.androidUIAutomator("new UiSelector().text(\"Welcome to Reno\")"),
                AppiumBy.androidUIAutomator("new UiSelector().textContains(\"Welcome to Reno\")"),
                AppiumBy.androidUIAutomator("new UiSelector().descriptionContains(\"Welcome to Reno\")"),
                AppiumBy.androidUIAutomator("new UiSelector().textContains(\"Welcome\")"),
                WELCOME_TO_RENO,
                androidContains("Welcome to Reno"),
                androidContains("Welcome")
        );
        WebElement welcome = findFirstDisplayed(welcomeLocators);
        if (welcome == null) {
            failHard("Welcome to Reno screen is not visible");
            return;
        }
        System.out.println("Welcome to Reno screen validated: " + safeText(welcome));
    }

    public void waitSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Wait interrupted", e);
        }
    }

    private void typeIntoFirstEditText(String value) {
        WebElement field = wait.until(ExpectedConditions.elementToBeClickable(
                AppiumBy.xpath("//android.widget.EditText")));
        field.click();
        field.clear();
        field.sendKeys(value);
    }

    private WebElement findFirstDisplayed(List<By> locators) {
        return findFirstDisplayed(locators, 15);
    }

    private WebElement findFirstDisplayed(List<By> locators, int timeoutSeconds) {
        Duration previous = driver.manage().timeouts().getImplicitWaitTimeout();
        driver.manage().timeouts().implicitlyWait(Duration.ZERO);
        try {
            long deadline = System.currentTimeMillis() + timeoutSeconds * 1000L;
            while (System.currentTimeMillis() < deadline) {
                for (By locator : locators) {
                    try {
                        for (WebElement element : driver.findElements(locator)) {
                            if (element.isDisplayed()) {
                                System.out.println("Matched locator: " + locator);
                                return element;
                            }
                        }
                    } catch (Exception e) {
                        String msg = e.getMessage() == null ? "" : e.getMessage();
                        if (msg.contains("UnreachableBrowser") || msg.contains("ConnectException")
                                || msg.contains("disconnected")) {
                            System.out.println("Appium session lost while finding element: "
                                    + e.getClass().getSimpleName());
                            return null;
                        }
                    }
                }
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return null;
                }
            }
            return null;
        } finally {
            driver.manage().timeouts().implicitlyWait(previous);
        }
    }

    private void clickContinueWhenEnabled(String screenName) {
        try {
            WebElement continueBtn = wait.until(ExpectedConditions.elementToBeClickable(CONTINUE_BUTTON));
            if (!continueBtn.isEnabled()) {
                failHard("Continue button is not clickable on " + screenName);
            }
            continueBtn.click();
        } catch (TimeoutException e) {
            failHard("Continue button is not clickable on " + screenName);
        }
    }

    private void assertVisible(By locator, String failureMessage) {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (Exception e) {
            failHard(failureMessage);
        }
    }

    private void assertVisibleAndClick(By locator, String failureMessage) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
        } catch (Exception e) {
            failHard(failureMessage);
        }
    }

    private String safeText(WebElement element) {
        try {
            String text = element.getText();
            if (text != null && !text.isBlank()) {
                return text;
            }
            String desc = element.getAttribute("contentDescription");
            return desc == null ? "" : desc;
        } catch (Exception e) {
            return "";
        }
    }

    private void failHard(String message) {
        try {
            ScreenshotUtil.capture("signup_failure");
        } catch (Exception e) {
            System.out.println("Could not capture screenshot: " + e.getMessage());
        }
        throw new AssertionError(message);
    }
}
