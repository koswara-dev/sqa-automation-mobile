package com.juaracoding.slappium.page;

import org.openqa.selenium.By;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;

public class HomePage {

    private final By greetingLabel = By.id("com.maxsoftlk.loginapplication:id/tvGreeting");
    private final By welcomeLabel = By.id("com.maxsoftlk.loginapplication:id/tvWelcome");

    private final AndroidDriver<MobileElement> driver;

    public HomePage(AndroidDriver<MobileElement> driver) {
        this.driver = driver;
    }

    public String getGreetingLabelText() {
        return driver.findElement(greetingLabel).getText();
    }

    public String getWelcomeLabelText() {
        return driver.findElement(welcomeLabel).getText();
    }
}
