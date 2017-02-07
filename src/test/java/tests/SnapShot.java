package tests;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by Дима on 21.01.2017.
 */
public class SnapShot {


    public static String image(AppiumDriver<MobileElement> driver) {
        File targetfile = null;
        try {

            File scrFile = driver.getScreenshotAs(OutputType.FILE);
            System.out.println("Сделан скриншот экрана");
            String fileName = UUID.randomUUID().toString();
            targetfile = new File("C:\\SCREENS\\" + fileName + ".png");//Путь где будет сохранен скрин
            FileUtils.copyFile(scrFile, targetfile);
            System.out.println(targetfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return targetfile.toString();
    }

    public static String OCR(String ImagePath){
        String result = null;
        File imageFile = new File(ImagePath);
        ITesseract instance = new Tesseract();
        instance.setDatapath("C:/Program Files (x86)/Tesseract-OCR");
        instance.setLanguage("rus");


        try{
            result = instance.doOCR(imageFile);
        }
        catch (TesseractException e){
            System.err.println(e.getMessage());
        }
        return result;
    }
}
