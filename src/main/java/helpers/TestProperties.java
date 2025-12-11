package helpers;

import org.aeonbits.owner.Config;

/**
 * Конфигурационный интерфейс для загрузки основных тестовых параметров проекта.
 * Значения подгружаются из файла:
 * src/main/resources/test.properties
 *
 * Хранит настройки таймаутов, URL Яндекс Маркета и путь к ChromeDriver.
 *
 * @author Сергей Лужин
 */
@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "file:src/main/resources/test.properties"
})
public interface TestProperties extends Config {
    /**
     * Возвращает значение стандартного таймаута,
     * используемого в тестах (в секундах).
     *
     * @return таймаут по умолчанию
     * @author Сергей Лужин
     */
    @Config.Key("default.timeout")
    int defaultTimeout();

    /**
     * Возвращает URL главной страницы Яндекс Маркета.
     *
     * @return строка с URL Яндекс Маркета
     * @author Сергей Лужин
     */
    @Config.Key("yandex-market.url")
    String yandexMarketUrl();

    /**
     * Возвращает путь к исполняемому файлу ChromeDriver.
     *
     * @return строка с путем к ChromeDriver
     * @author Сергей Лужин
     */
    @Config.Key("driver.chrome")
    String driverChrome();
}
