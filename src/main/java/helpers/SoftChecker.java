package helpers;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;


/**
 * Утилитный класс для выполнения "мягких" проверок.
 * При провале условия добавляет отдельный шаг в Allure с сообщением,
 * а затем падает обычным JUnit-ассертом.
 *
 * Используется совместно с {@link org.junit.jupiter.api.Assertions}
 * и Allure-шагами для наглядного отображения результатов проверок.
 *
 * @author Сергей Лужин
 */
public class SoftChecker {

    /**
     * Выполняет проверку условия с описанием шага для Allure.
     * Если условие ложно, добавляется шаг с сообщением об ошибке,
     * после чего выполняется {@link Assertions#assertTrue(boolean)}.
     *
     * @param condition   проверяемое логическое условие
     * @param displayName текст шага, отображаемый в отчёте Allure
     * @param failMessage сообщение, которое будет добавлено в Allure при провале проверки
     *
     * @author Сергей Лужин
     */
    @Step("{displayName}")
    public static void check(boolean condition, String displayName, String failMessage) {
        if (!condition) {
            Allure.step(failMessage);
        }

        Assertions.assertTrue(
                condition
        );
    }
}

