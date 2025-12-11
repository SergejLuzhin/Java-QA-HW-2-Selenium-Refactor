package helpers;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import static helpers.Properties.testProperties;

/**
 * Утилитный класс для определения текущего положения страницы
 *
 *
 * @author Сергей Лужин
 */
public class PageOffsetLocator {
    /**
     * Проверяет, достигнут ли нижний край страницы при текущем положении скролла.
     *
     * @param js экземпляр {@link JavascriptExecutor}, через который выполняются JS-выражения в браузере
     *
     * @return {true}, если нижняя граница видимой области приблизилась к концу страницы, иначе {false}
     *
     *
     * @author Сергей Лужин
     */
    public static boolean hasReachedBottomOfPage(JavascriptExecutor js) {
        Number offset = (Number) js.executeScript("return window.pageYOffset;");
        Number window = (Number) js.executeScript("return window.innerHeight;");
        Number height = (Number) js.executeScript("return document.body.scrollHeight;");

        double currentBottom = offset.doubleValue() + window.doubleValue();
        double pageHeight = height.doubleValue();

        return  (currentBottom >= pageHeight - 50);
    }

    /**
     * Проверяет, что страница все еще находится вниз, после ожидания прогрузки
     *
     * @param js экземпляр {@link JavascriptExecutor}, через который выполняются JS-выражения в браузере
     * @param driver экземпляр {@link WebDriver}, на котором выставляется ожидание
     *
     * @return {true}, если нижняя граница видимой области приблизилась к концу страницы, иначе {false}
     *
     *
     * @author Сергей Лужин
     */
    public static boolean isStillAtBottomAfterWait_OLD(JavascriptExecutor js, WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(
                driver,
                testProperties.defaultTimeout()
        );

        try {
            wait.until(d -> !hasReachedBottomOfPage(js));
            return false; // страница прогрузилась еще
        } catch (TimeoutException e) {
            return true; // не прогрузилась, до сих пор внизу
        }
    }

    public static boolean isStillAtBottomAfterWait(JavascriptExecutor js, FluentWait<WebDriver> fluentWait) {
        boolean isStillAtBottom = true;

        isStillAtBottom = fluentWait.until(d -> {
            if (!hasReachedBottomOfPage(js)) {
                return false; // страница прогрузилась → выходим
            }
            return hasReachedBottomOfPage(js) ? null : false;
        });

        return isStillAtBottom;
    }
}
