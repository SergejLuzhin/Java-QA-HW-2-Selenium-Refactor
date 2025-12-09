package helpers;

import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.ByteArrayInputStream;

/**
 * Утилитный класс для создания и прикрепления скриншотов к отчёту Allure.
 * Используется для фиксации состояния страницы в момент выполнения теста.
 *
 * @author Сергей Лужин
 */
public class Screenshoter {

    /**
     * Делает скриншот текущего состояния браузера и прикрепляет его к отчёту Allure.
     *
     * @param name   название скриншота в отчёте Allure
     *
     * @author Сергей Лужин
     */
    public static void attachScreenshot(String name) {
        WebDriver driver = Driver.getWebDriver();
        try {
            if (driver instanceof TakesScreenshot) {
                byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                Allure.addAttachment(name, new ByteArrayInputStream(screenshot));
            }
        } catch (Exception ignored) {}
    }

}
