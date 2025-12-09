package ru.yandexmarket;

import helpers.Driver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;

/**
 * Базовый класс для UI-тестов Яндекс Маркета.
 *
 * Отвечает за инициализацию и завершение работы WebDriver
 * перед и после выполнения каждого теста.
 * Все тестовые классы должны наследоваться от этого класса,
 * чтобы использовать единый механизм создания и закрытия браузера.
 *
 * @author Сергей Лужин
 */
public class BaseTests {

    /**
     * Экземпляр WebDriver, используемый во всех тестах, наследующих данный класс.
     *
     * @author Сергей Лужин
     */
    protected WebDriver driver;

    /**
     * Метод, выполняемый перед каждым тестом.
     *
     * Инициализирует WebDriver через {@link Driver#create()}
     * и сохраняет его в поле {@link #driver} для использования в тестах.
     *
     * @author Сергей Лужин
     */
    @BeforeEach
    public void before() {
        Driver.create();
        driver = Driver.getWebDriver();
    }

    /**
     * Метод, выполняемый после каждого теста.
     *
     * Завершает работу WebDriver, закрывая браузер.
     *
     * @author Сергей Лужин
     */
    @AfterEach
    public void after() {
        driver.quit();
    }
}
