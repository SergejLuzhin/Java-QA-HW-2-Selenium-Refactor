package helpers;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
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

    public static boolean isStillAtBottomAfterWait(JavascriptExecutor js, WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(
                driver,
                testProperties.defaultTimeout()
        );

        System.out.println("[WAIT] Ждём, что низ страницы сместится (появится новый контент)");

        try {
            // Ждём, пока условие "достигнут низ страницы" перестанет быть верным
            wait.until(d -> !hasReachedBottomOfPage(js));
            System.out.println("[WAIT] Страница удлинилась, низ сместился");
            return false; // уже НЕ внизу
        } catch (TimeoutException e) {
            System.out.println("[WAIT] Низ страницы не сместился за отведённое время");
            return true; // всё ещё внизу
        }
    }
}
