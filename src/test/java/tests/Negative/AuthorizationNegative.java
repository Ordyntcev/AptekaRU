package tests.Negative;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.junit.Assert;
import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.*;
import tests.AuthorizationLocators.AuthorizationLocators;
import tests.SnapShot;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * Created by Дима on 16.01.2017.
 */
public class AuthorizationNegative {
   public AndroidDriver driver;
    AuthorizationLocators authorizationLocators = new AuthorizationLocators();

    public void setDriver(AndroidDriver driver){
        this.driver = driver;
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp () throws MalformedURLException{

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
        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);

    }
    @Test(groups = "smoke")
    public void lengthTelephoneNumberIs12() throws InterruptedException {

        driver.findElement(By.id(authorizationLocators.TELEPHONE_FIELD)).isDisplayed();//is Displayed field 'MOBILE PHONE'???
        driver.findElement(By.id(authorizationLocators.TELEPHONE_FIELD)).sendKeys("+7000000000000");//Enter the number in field 'MOBILE PHONE'
        driver.hideKeyboard();
        driver.findElement(By.id(authorizationLocators.NEXT_BUTTON)).click();//Click on 'Далее'
        System.out.println("Кнопка далее нажата");
        String resultAfterOCR = SnapShot.OCR(SnapShot.image(driver));
        Assert.assertTrue("Введите корректный номер телефона", resultAfterOCR.contains("Введите корректный номер телефона"));
    }

    @Test(groups = "smoke")
    public void emptyFieldTelephoneNumber(){

        driver.findElement(By.id(authorizationLocators.TELEPHONE_FIELD)).isDisplayed();//
        driver.findElement(By.id(authorizationLocators.TELEPHONE_FIELD)).clear();//
        driver.hideKeyboard();
        driver.findElement(By.id(authorizationLocators.NEXT_BUTTON)).click();
        System.out.println("Кнопка далее нажата");
        String resultAfterOCR = SnapShot.OCR(SnapShot.image(driver));
        Assert.assertTrue("Введите корректный номер телефона", resultAfterOCR.contains("Введите корректный номер телефона"));
    }
    @Test(groups = "smoke")
    public void enterTheSymbolsInTelephoneField(){

        driver.findElement(By.id(authorizationLocators.TELEPHONE_FIELD)).isDisplayed();
        driver.findElement(By.id(authorizationLocators.TELEPHONE_FIELD)).clear();
        driver.findElement(By.id(authorizationLocators.TELEPHONE_FIELD)).sendKeys("+7+######");
        driver.hideKeyboard();
        driver.findElement(By.id(authorizationLocators.NEXT_BUTTON)).click();
        System.out.println("Кнопка далее нажата");
        String resultAfterOCR = SnapShot.OCR(SnapShot.image(driver));
        Assert.assertTrue("Введите корректный номер телефона", resultAfterOCR.contains("Введите корректный номер телефона"));
    }

    @Test(groups = "smoke")
    public void tryEnterWithWrongCode() throws InterruptedException {

        //Ввести в поле "Телефонный номер" следующий номер +7 000 000 00 00
        driver.findElement(By.id(authorizationLocators.TELEPHONE_FIELD)).isDisplayed();
        driver.findElement(By.id(authorizationLocators.TELEPHONE_FIELD)).sendKeys("+70000000000");
        driver.hideKeyboard();

        //Нажать на кнопку "Далее"
        driver.findElement(By.id(authorizationLocators.NEXT_BUTTON)).click();

        //Ничего не вводить в поле "Код" и нажать на кнопку "Войти"
        driver.findElement(By.id(authorizationLocators.CODE_FIELD)).isDisplayed();
        driver.findElement(By.id(authorizationLocators.ENTER_BUTTON)).click();
        Thread.sleep(2700);

        //Убедиться, что появляется тост с неправильно введенным кодом
        String resultAfterOCR = SnapShot.OCR(SnapShot.image(driver));
        System.out.println(resultAfterOCR);
        Assert.assertTrue("Введен неверный пароль", resultAfterOCR.contains("Введен неверный пароль"));

        //Ввести в поле "Код" некорректный код и нажать на кнопку "Войти"
        driver.findElement(By.id(authorizationLocators.CODE_FIELD)).sendKeys("12");
        driver.hideKeyboard();
        driver.findElement(By.id(authorizationLocators.ENTER_BUTTON)).click();
        Thread.sleep(2700);

        //Убедиться, что появляется тост с неправильно введенным кодом
        resultAfterOCR = SnapShot.OCR(SnapShot.image(driver));
        System.out.println(resultAfterOCR);
        Assert.assertTrue("Введен неверный пароль", resultAfterOCR.contains("Введен неверный пароль"));
    }

@AfterMethod(alwaysRun = true)
    public void tearDown(){
    driver.quit();
}

}
