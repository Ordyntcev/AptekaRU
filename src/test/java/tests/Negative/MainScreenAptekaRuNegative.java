package tests.Negative;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.junit.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;
import tests.AptekaRuLocators.AptekaRuLocators;
import tests.Positive.AuthorizationPositive;
import tests.SnapShot;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * Created by Дима on 28.01.2017.
 */
public class MainScreenAptekaRuNegative {
    AndroidDriver driver;
    AptekaRuLocators aptekaRuLocators = new AptekaRuLocators();

    public void setDriver(AndroidDriver driver){
        this.driver = driver;
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp()throws MalformedURLException {

        setDriver(driver);

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(MobileCapabilityType.PLATFORM, Platform.ANDROID);
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME,"Android");
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME,"SAMSUNG GRAND PRIME");
        capabilities.setCapability(MobileCapabilityType.VERSION,"5.1.1");
        capabilities.setCapability("appPackage", "ru.apteka");
        capabilities.setCapability("appActivity", "ru.apteka.activities.SplashActivity");


        URL url = new URL("http://127.0.0.1:4723/wd/hub");
        driver = new AndroidDriver(url,capabilities);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    @Test(suiteName = "smoke")
    public void enterIncorrectDataInSearchField() throws InterruptedException {

        //Авторизация с корректным телефонным номером
        AuthorizationPositive authorizationPositive = new AuthorizationPositive();
        authorizationPositive.setDriver(driver);
        authorizationPositive.authorizationWithCorrectTelephoneNumber();

        //Нажать на поле с поиском
        driver.findElement(By.id(aptekaRuLocators.SEARCH_FIELD)).click();

        //Ввести первые символы при которых поиск не даст результатов
        driver.findElement(By.name(aptekaRuLocators.SEARCH_FIELD_AFTER_OPENED)).sendKeys("1234567");
        driver.hideKeyboard();

        //Убедиться, что после поиска, присутствует текст "Совпадений не найдено"
        Thread.sleep(2000);
        String resultAfterOCR = SnapShot.OCR(SnapShot.image(driver));
        System.out.println(resultAfterOCR);
        Assert.assertTrue("Совпадений не найдено", resultAfterOCR.contains("Совпадений не найдено"));

    }
    @AfterMethod(alwaysRun = true)
    public void tearDown(){
        driver.quit();
    }
}
