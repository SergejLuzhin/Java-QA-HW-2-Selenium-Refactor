package helpers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import java.util.concurrent.TimeUnit;

import static helpers.Properties.testProperties;

/**
 * Класс для централизованного управления экземпляром WebDriver.
 * Создаёт и настраивает драйвер браузера, а также предоставляет к нему доступ
 * через статический метод {@link #getWebDriver()}.
 *
 * @author Сергей Лужин
 */
public class Driver {

    /**
     * Общий экземпляр WebDriver, используемый во всём тестовом проекте.
     * Инициализируется методом {@link #create()}.
     */
    private static WebDriver webDriver;


    /**
     * Возвращает текущий экземпляр WebDriver.
     *
     * @return активный экземпляр WebDriver или null, если {@link #create()} ещё не вызывался
     *
     * @author Сергей Лужин
     */
    public static WebDriver getWebDriver () {
        return webDriver;
    }

    /**
     * Создаёт и настраивает экземпляр WebDriver для использования в тестах.
     * Устанавливает путь к ChromeDriver, оборачивает драйвер в EventFiringWebDriver
     * и регистрирует слушатель скриншотов для Allure.
     * Также настраивает размер окна и неявное ожидание.
     *
     * @author Сергей Лужин
     */
    public static void create() {
        System.setProperty("webdriver.chrome.driver", testProperties.driverChrome());

        WebDriver base = new ChromeDriver();

        EventFiringWebDriver driver = new EventFiringWebDriver(base);
        driver.register(new AllureScreenshotListener());

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(testProperties.defaultTimeout(), TimeUnit.SECONDS);

        webDriver = driver;
    }
}

