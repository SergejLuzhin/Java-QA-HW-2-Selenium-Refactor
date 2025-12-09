package helpers;

import org.openqa.selenium.*;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;

import static helpers.Screenshoter.attachScreenshot;

/**
 * Слушатель событий WebDriver для автоматического снятия скриншотов
 * и прикрепления их к отчёту Allure при ключевых действиях:
 * клике по элементу, вводе текста, навигации и возникновении исключений.
 *
 * @author Сергей Лужин
 */
public class AllureScreenshotListener extends AbstractWebDriverEventListener {

    /**
     * Вызывается после клика по элементу.
     * Делает скриншот страницы и прикрепляет его к отчёту Allure.
     *
     * @param element элемент, по которому был выполнен клик
     *
     * @author Сергей Лужин
     */
    @Override
    public void afterClickOn(WebElement element, WebDriver driver) {
        attachScreenshot("Клик");
    }

    /**
     * Вызывается после изменения значения элемента (ввода текста через sendKeys).
     * Делает скриншот страницы и прикрепляет его к отчёту Allure.
     *
     * @param element элемент, значение которого изменилось
     * @param keys    последовательность символов, переданная в элемент
     *
     * @author Сергей Лужин
     */
    @Override
    public void afterChangeValueOf(WebElement element, WebDriver driver, CharSequence[] keys) {
        attachScreenshot(String.format("Набор текста: %s", (Object) keys));
    }

    // Поиск элемента
   /* @Override
    public void afterFindBy(By by, WebElement element, WebDriver driver) {
        attachScreenshot("После поиска по: " + by.toString(), driver);
    }*/

    /**
     * Вызывается после перехода по указанному URL.
     * Делает скриншот сразу после завершения навигации.
     *
     * @param url    адрес, на который был выполнен переход
     *
     * @author Сергей Лужин
     */
    @Override
    public void afterNavigateTo(String url, WebDriver driver) {
        attachScreenshot("Перереход по: " + url);
    }

    /**
     * Вызывается при возникновении исключения во время работы WebDriver.
     * Делает скриншот страницы на момент ошибки и прикрепляет его к отчёту Allure.
     *
     * @param throwable возникшее исключение
     *
     * @author Сергей Лужин
     */
    @Override
    public void onException(Throwable throwable, WebDriver driver) {
        attachScreenshot("Ошибка: " + throwable);
    }
}
