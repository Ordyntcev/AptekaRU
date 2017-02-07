package tests;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;

import java.util.List;

import static org.junit.Assert.fail;

/**
 * Created by Дима on 28.01.2017.
 */
public class ScrollFeatures {
    public static void scrollDown(AndroidDriver driver) {
        Dimension dimens = driver.manage().window().getSize();
        int x = (int) (dimens.getWidth() * 0.5);
        int startY = (int) (dimens.getHeight() * 0.5);
        int endY = (int) (dimens.getHeight() * 0.2);
// lest say if above code calculated correct scroll area as per your device then just add static loop to scroll to no of times you want to scroll
        for (int i=0;i<10;i++)
            driver.swipe(x, startY, x, endY, 800);
    }

    public static void isElementPresentWithScroll(AndroidDriver driver, String locator) {
        driver.findElementByAndroidUIAutomator("new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView(new UiSelector().textContains(\""+locator+"\").instance(0))");
    }

    public static void isElementNotPresentWithScroll(AndroidDriver driver, String locator){
        try{
            driver.findElementByAndroidUIAutomator("new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView(new UiSelector().textContains(\""+locator+"\").instance(0))");
            fail("Link with text <" + locator + "> is present");
        }
        catch (NoSuchElementException ex){

        }
    }

    public static void isElementNotPresent(AndroidDriver driver, By locator) {

        List<AndroidElement> deleteLinks =  driver.findElements(locator);
        Assert.assertTrue(deleteLinks.isEmpty());

    }
}
