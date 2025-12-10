package helpers;

import org.openqa.selenium.JavascriptExecutor;

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
}
