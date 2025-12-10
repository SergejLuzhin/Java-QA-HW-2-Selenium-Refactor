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
     * Возвращает значение явного ожидания,
     * используемого в тестах (в миллисекундах).
     *
     * @return время явного ожидания
     * @author Сергей Лужин
     */
    @Config.Key("explicit.wait.timeout.ms")
    int explicitWaitTimeoutMs();

    /**
     * Возвращает значение задержки между скроллами,
     * используемой в тестах (в миллисекундах).
     *
     * @return задержка между скроллами
     * @author Сергей Лужин
     */
    @Config.Key("scroll.timeout.ms")
    int scrollTimeoutMs();

    /**
     * Возвращает значение задержки на ожидание прогрузки страницы,
     * используемой в тестах (в миллисекундах).
     *
     * @return задержка на ожидание прогрузки страницы
     * @author Сергей Лужин
     */
    @Config.Key("page.update.timeout.ms")
    int pageUpdateTimeoutMs();

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
