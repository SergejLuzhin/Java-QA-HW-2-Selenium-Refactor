package helpers;

import org.aeonbits.owner.ConfigFactory;

/**
 * Вспомогательный класс упрощающий доступ к конфигурационным свойствам проекта.
 *
 * @author Сергей Лужин
 */
public class Properties {

    /**
     * Конфигурация тестовых параметров (таймауты, URL, путь к драйверу и т.д.).
     * Загружается на основе интерфейса {@link TestProperties}.
     *
     * @author Сергей Лужин
     */
    public static TestProperties testProperties = ConfigFactory.create(TestProperties.class);

    /**
     * Конфигурация XPath-локаторов элементов страницы.
     * Загружается на основе интерфейса {@link XpathProperties}.
     *
     * @author Сергей Лужин
     */
    public static XpathProperties xpathProperties = ConfigFactory.create(XpathProperties.class);
}
