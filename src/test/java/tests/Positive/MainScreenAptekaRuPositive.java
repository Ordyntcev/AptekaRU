package tests.Positive;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import tests.AptekaRuLocators.AptekaRuLocators;
import tests.CardGoodsLocators.CardGoodsLocators;
import tests.HeadLocators.HeadLocators;
import tests.ScrollFeatures;
import tests.SideMenuLocators.SideMenuLocators;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.junit.Assert;
import org.junit.Before;
import org.testng.annotations.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * Created by Дима on 28.01.2017.
 */
public class MainScreenAptekaRuPositive {


    AndroidDriver driver;
    AptekaRuLocators aptekaRuLocators = new AptekaRuLocators();
    HeadLocators headLocators = new HeadLocators();
    SideMenuLocators sideMenuLocators = new SideMenuLocators();
    CardGoodsLocators cardGoodsLocators = new CardGoodsLocators();

    public void setDriver(AndroidDriver driver) {
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
    public void searchField() throws InterruptedException, MalformedURLException {

        AuthorizationPositive authorizationPositive = new AuthorizationPositive();
        authorizationPositive.setDriver(driver);

        //Войти в приложение как авторизованный пользователь
        authorizationPositive.authorizationWithCorrectTelephoneNumber();

        //Нажать на поле с поиском
        driver.findElement(By.id(aptekaRuLocators.SEARCH_FIELD)).click();

        //Ввести первые 3 символа которые присутствуют в названии препарата
        driver.findElement(By.name(aptekaRuLocators.SEARCH_FIELD_AFTER_OPENED)).sendKeys("pro");

        //Убедится что препарат найден
        ScrollFeatures.isElementPresentWithScroll(driver, "PRO ACTIVE УВЛАЖНЯЮЩИЙ Р-Р Д/МЯГКИХ КОНТ ЛИНЗ 10МЛ ИНД/УП");
    }

    @Test(suiteName = "smoke")
    public void addGoodsInFavourite() throws MalformedURLException, InterruptedException {

        //Найти товар, который будет добавлен в избранное
        searchField();

        //Добавить в изрбранное
        driver.findElement(By.id(aptekaRuLocators.FAVOURITE_BUTTON)).isDisplayed();
        driver.findElement(By.id(aptekaRuLocators.FAVOURITE_BUTTON)).click();
        driver.findElement(By.name(aptekaRuLocators.BACK_BUTTON)).click();

        //Перейти в раздел "Избранное" в боковом меню
        driver.findElement(By.xpath(headLocators.SIDE_MENU)).click();
        driver.findElement(By.id(sideMenuLocators.FAVOURITIES)).click();

        //Убедиться что раздел "Избранное" открыт
        String resultFind = driver.findElement(By.id(headLocators.FAVOURITIES_TITLE)).getText();
        Assert.assertTrue(resultFind.equals("Избранное"));

        //Убедиться, что в разделе "Избранное" присутствует товар который был добавлен
        ScrollFeatures.isElementPresentWithScroll(driver, "PRO ACTIVE УВЛАЖНЯЮЩИЙ Р-Р Д/МЯГКИХ КОНТ ЛИНЗ 10МЛ ИНД/УП");

        //Перейти в раздел "карточка с товаром" который был найден в избранном, и убрать товар из избранного
        driver.findElement(By.name("PRO ACTIVE УВЛАЖНЯЮЩИЙ Р-Р Д/МЯГКИХ КОНТ ЛИНЗ 10МЛ ИНД/УП")).click();
        driver.manage().timeouts().implicitlyWait(15,TimeUnit.SECONDS);
        driver.findElement(By.id(aptekaRuLocators.FAVOURITE_BUTTON)).isDisplayed();
        driver.findElement(By.id(aptekaRuLocators.FAVOURITE_BUTTON)).click();
        driver.findElement(By.name(aptekaRuLocators.BACK_BUTTON)).click();

        //Убедиться, что товар убран из раздела "Избранное"
        ScrollFeatures.isElementNotPresentWithScroll(driver,"PRO ACTIVE УВЛАЖНЯЮЩИЙ Р-Р Д/МЯГКИХ КОНТ ЛИНЗ 10МЛ ИНД/УП");

    }

    @Test(suiteName = "smoke")
    public void verifyIsAddGoodsInShoppingCart() throws MalformedURLException, InterruptedException {

        searchField();

        //Перейти в раздел "карточка с товаром" который был найден в поиске
        driver.findElement(By.name("PRO ACTIVE УВЛАЖНЯЮЩИЙ Р-Р Д/МЯГКИХ КОНТ ЛИНЗ 10МЛ ИНД/УП")).click();

        //Нажать на кнопку +
        driver.findElement(By.id(cardGoodsLocators.PLUS_BTN)).click();

        //После нажатия на кнопку +, убедиться, что на красном кружке, который находится на иконке "Корзина", отображается 1
        String s = driver.findElement(By.id(cardGoodsLocators.DIGIT_ON_SHOPING_CART)).getText();
        Assert.assertTrue(s.equals("1"));

        //После нажатия на кнопку -, убедиться, что красный кружок, который находится на иконке "Корзина" - исчез
        driver.findElement(By.id(cardGoodsLocators.MINUS_BTN)).click();
        ScrollFeatures.isElementNotPresent(driver, By.id(cardGoodsLocators.RED_CIRCLE_ON_SHOPING_CART));

        //Добавить товар в корзину при нажатии на кнопку "Добавить в корзину"
        driver.findElement(By.id(cardGoodsLocators.ADD_IN_SHOPING_CART_BTN)).click();

        //Убедиться, что на красном кружке, который находится на иконке "Корзина", отображается 1
        s = driver.findElement(By.id(cardGoodsLocators.DIGIT_ON_SHOPING_CART)).getText();
        Assert.assertTrue(s.equals("1"));

        //Убедиться, что текст на кнопку "Добавить в коризну" изменяется на"Перейти в корзину"
        String resultFind = driver.findElement(By.id(cardGoodsLocators.ADD_IN_SHOPING_CART_BTN)).getText();
        Assert.assertTrue(resultFind.equals("Перейти в корзину"));


    }
    @AfterMethod(alwaysRun = true)
    public void tearDown(){
        driver.quit();
    }
}
