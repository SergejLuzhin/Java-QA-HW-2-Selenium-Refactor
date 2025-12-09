package helpers;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.FluentWait;

import java.time.Duration;

/**
 * Утилитный класс для прокрутки веб-страниц.
 * Содержит методы для плавного скролла вверх и вниз страницы
 *
 * Использует WebDriver, полученный через {@link Driver#getWebDriver()}.
 *
 * @author Сергей Лужин
 */
public class Scroller {

    /**
     * Плавно прокручивает страницу до самого низа,
     * или до максимального количества шагов,
     * фиксируя страницу скриншотами.
     *
     * @author Сергей Лужин
     */
    public static void scrollToBottomOfPage() {
        WebDriver driver = Driver.getWebDriver();
        JavascriptExecutor js = (JavascriptExecutor) driver;

        int scrollStep = 600;          // на сколько пикселей скроллим за шаг
        long pauseMs = 400;            // «плавная» пауза между шагами
        int maxSteps = 20;             // защита от бесконечного цикла

        for (int i = 0; i < maxSteps; i++) {
            Screenshoter.attachScreenshot("Прокрутка страницы вниз");

            // скроллим вниз на scrollStep пикселей
            js.executeScript("window.scrollBy(0, arguments[0]);", scrollStep);

            long targetTime = System.currentTimeMillis() + pauseMs;
            new FluentWait<>(driver)
                    .withTimeout(Duration.ofMillis(pauseMs + 50))
                    .pollingEvery(Duration.ofMillis(50))
                    .ignoring(Exception.class)
                    .until(d -> System.currentTimeMillis() >= targetTime);

            // проверяем, дошли ли до низа страницы
            long offset = ((Number) js.executeScript("return window.pageYOffset;")).longValue();      // текущая вертикальная позиция
            long windowHeight = ((Number) js.executeScript("return window.innerHeight;")).longValue(); // видимая высота окна
            long docHeight = ((Number) js.executeScript("return document.body.scrollHeight;")).longValue(); // общая высота страницы

            if (offset + windowHeight >= docHeight) {
                break;
            }
        }
    }

    /**
     * Плавно прокручивает страницу до самого верха,
     * или до максимального количества шагов,
     * (как правило скролла вверх должно всегда хватать,
     * так как он быстрее, чем скролл вниз)
     * фиксируя страницу скриншотами.
     *
     * @author Сергей Лужин
     */
    public static void scrollToTopOfPage() {
        WebDriver driver = Driver.getWebDriver();
        JavascriptExecutor js = (JavascriptExecutor) driver;

        int scrollStep = 1000;          // на сколько пикселей скроллим за шаг
        long pauseMs = 200;            // «плавная» пауза между шагами
        int maxSteps = 50;             // защита от бесконечного цикла

        for (int i = 0; i < maxSteps; i++) {
            Screenshoter.attachScreenshot("Прокрутка страницы вверх");

            // скроллим вверх на scrollStep пикселей (отрицательное значение)
            js.executeScript("window.scrollBy(0, arguments[0]);", -scrollStep);

            long targetTime = System.currentTimeMillis() + pauseMs;
            new FluentWait<>(driver)
                    .withTimeout(Duration.ofMillis(pauseMs + 50))
                    .pollingEvery(Duration.ofMillis(50))
                    .ignoring(Exception.class)
                    .until(d -> System.currentTimeMillis() >= targetTime);

            // проверяем, дошли ли до верха страницы
            long offset = ((Number) js.executeScript("return window.pageYOffset;")).longValue(); // текущая вертикальная позиция скролла

            if (offset <= 0) {
                break; // уже в самом верху — выходим
            }
        }
    }
}
