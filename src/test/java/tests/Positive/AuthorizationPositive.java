package tests.Positive;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import tests.AptekaRuLocators.AptekaRuLocators;
import tests.AuthorizationLocators.AuthorizationLocators;
import tests.HeadLocators.HeadLocators;
import tests.ScrollFeatures;
import tests.SnapShot;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.junit.Assert;
import org.junit.Before;
import org.testng.annotations.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;


/**
 * Created by Дима on 12.01.2017.
 */
public class AuthorizationPositive {
    public AuthorizationPositive() {
    }

    public void setDriver(AndroidDriver driver) {
        this.driver = driver;
    }


    AndroidDriver driver;
    AptekaRuLocators aptekaRuLocators = new AptekaRuLocators();
    AuthorizationLocators authorizationLocators = new AuthorizationLocators();
    HeadLocators headLocators = new HeadLocators();
    String title = null;

    @BeforeMethod(alwaysRun = true)
    public void setUp() throws MalformedURLException {

        setDriver(driver);

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(MobileCapabilityType.PLATFORM, Platform.ANDROID);
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "SAMSUNG GRAND PRIME");
        capabilities.setCapability(MobileCapabilityType.VERSION, "5.1.1");
        capabilities.setCapability("appPackage", "ru.apteka");
        capabilities.setCapability("appActivity", "ru.apteka.activities.SplashActivity");


        URL url = new URL("http://127.0.0.1:4723/wd/hub");
        driver = new AndroidDriver(url, capabilities);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    @Test(suiteName = "smoke")
    public void enterWithoutRegistration() {


        //Убедиться, что на экране присутствует кнопка "Войти без регистрации"
        driver.findElement(By.id(authorizationLocators.ENTER_WITHOUT_REGISTRATION_BUTTON)).isDisplayed();

        //Нажать на кнопку "Войти без регистрации"
        driver.findElement(By.id(authorizationLocators.ENTER_WITHOUT_REGISTRATION_BUTTON)).click();
        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);

        //Убедиться, что пользователь попадает на главный экран Apteka.RU
        driver.findElement(By.name(aptekaRuLocators.APTEKA_RU_TITLE)).isDisplayed();
        ScrollFeatures.isElementPresentWithScroll(driver, "Apteka.RU");

        //Открыть боковое меню
        driver.findElement(By.xpath(headLocators.SIDE_MENU)).click();

        //Убедиться, что кнопка "Войти" присутствует
        driver.findElement(By.xpath(headLocators.ENTER_BTN_IN_SIDE_MENU)).isDisplayed();

        //Нажать на кнопку "Войти"
        driver.findElement(By.xpath(headLocators.ENTER_BTN_IN_SIDE_MENU)).click();

        //Убедиться, что происходит переход на экран авторизации
        driver.findElement(By.id(authorizationLocators.ENTER_WITHOUT_REGISTRATION_BUTTON)).isDisplayed();

    }

    @Test(suiteName = "smoke")
    public void authorizationWithCorrectTelephoneNumber() throws InterruptedException {

        //Ввести в поле "Телефонный номер" номер +7 000 000 00 00
        driver.findElement(By.id(authorizationLocators.TELEPHONE_FIELD)).isDisplayed();
        driver.findElement(By.id(authorizationLocators.TELEPHONE_FIELD)).sendKeys("+70000000000");
        driver.hideKeyboard();

        //Нажать на кнопку "Далее"
        driver.findElement(By.id(authorizationLocators.NEXT_BUTTON)).click();

        //Ввести в поле "Код" следующий код 0000
        driver.findElement(By.id(authorizationLocators.CODE_FIELD)).isDisplayed();
        driver.findElement(By.id(authorizationLocators.CODE_FIELD)).sendKeys("0000");
        driver.hideKeyboard();

        //Нажать на кнопку "Войти"
        driver.findElement(By.id(authorizationLocators.ENTER_BUTTON)).click();

        //Убедиться, что происходит переход на главный экран Apteka.RU
        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
        driver.findElement(By.name(aptekaRuLocators.APTEKA_RU_TITLE)).isDisplayed();
        ScrollFeatures.isElementPresentWithScroll(driver, "Apteka.RU");
    }

    @Test(suiteName = "smoke")
    public void authorizationWithCode() throws InterruptedException {

        //Ввести в поле "Телефонный номер" следующий номер +7 905 433 86 02
        driver.findElement(By.id(authorizationLocators.TELEPHONE_FIELD)).isDisplayed();
        driver.findElement(By.id(authorizationLocators.TELEPHONE_FIELD)).sendKeys("+79054338602");
        driver.hideKeyboard();

        //Нажать на кнопку "Далее"
        driver.findElement(By.id(authorizationLocators.NEXT_BUTTON)).click();

        //Нажать на кнопку "Отправить код"
        driver.findElement(By.id(authorizationLocators.CODE_FIELD)).isDisplayed();
        driver.findElement(By.id(authorizationLocators.SEND_CODE_BUTTON)).click();
        Thread.sleep(2700);

        //Убедиться, что появляется тост с успешным отправлением кода на телефонный номер

        String resultAfterOCR = SnapShot.OCR(SnapShot.image(driver));
        System.out.println(resultAfterOCR);
        Assert.assertTrue("Пароль отправлен на указанный Вами", resultAfterOCR.contains("Пароль отправлен на указанный Вами"));

        //Убедиться, что текст на кнопку "Выслать код" изменился на "Выслать код повторно"
        ScrollFeatures.isElementPresentWithScroll(driver, "Выслать код повторно");

        //Ожидать пока код который выслан по СМС подставится в поле ввода "КОД"
        WebDriverWait wait = new WebDriverWait(driver, 60);
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id(authorizationLocators.CODE_FIELD), "5394"));

        //После того как код который был выслан по СМС подставился в поле "КОД", нажать на кнопку "Войти"
        driver.findElement(By.id(authorizationLocators.ENTER_BUTTON)).click();

        //Убедиться, что происходит переход на главный экран Apteka.RU
        driver.findElement(By.name(aptekaRuLocators.APTEKA_RU_TITLE)).isDisplayed();
        ScrollFeatures.isElementPresentWithScroll(driver, "Apteka.RU");
    }
    @AfterMethod(alwaysRun = true)
    public void tearDown(){
        driver.quit();
    }
}


