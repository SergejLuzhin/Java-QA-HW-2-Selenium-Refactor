package pages;

import entity.Product;
import helpers.Driver;
import helpers.PageOffsetLocator;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

import static org.openqa.selenium.Keys.ENTER;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

import static helpers.Properties.testProperties;
import static helpers.Properties.xpathProperties;
import static helpers.PageOffsetLocator.*;

/**
 * Page Object для работы со страницами Яндекс Маркета.
 * Инкапсулирует общие элементы и действия: поиск, работа с каталогом,
 * фильтрами и карточками товаров.
 *
 * Использует WebDriver, получаемый из {@link Driver#getWebDriver()}.
 *
 * @author Сергей Лужин
 */
public class YandexMarketPage {

    /**
     * Коллекция товаров, отображённых на текущей странице.
     * Заполняется на основе найденных карточек товаров.
     */
    public List<Product> productsOnPage;

    /**
     * Экземпляр WebDriver, используемый для взаимодействия со страницей.
     */
    protected WebDriver driver;

    /**
     * Веб-элемент поля ввода поискового запроса на странице Яндекс Маркета.
     */
    protected WebElement searchInput;

    /**
     * Веб-элемент кнопки запуска поиска по введённому запросу.
     */
    protected WebElement searchButton;

    /**
     * Веб-элемент кнопки запуска поиска по введённому запросу.
     */
    protected WebElement catalogButton;

    protected WebDriverWait wait;

    /**
     * Конструктор инициализирует элементы страницы,
     * ожидая появления ключевых элементов поиска и каталога.
     *
     *
     * @author Сергей Лужин
     */
    public YandexMarketPage() {
        this.driver = Driver.getWebDriver();
        this.wait = new WebDriverWait(driver, testProperties.defaultTimeout());

        this.searchInput = driver.findElement(By.xpath(xpathProperties.ymSearchInputXpath()));

        this.searchButton = driver.findElement(By.xpath(xpathProperties.ymSearchButtonXpath()));

        this.catalogButton = driver.findElement(By.xpath(xpathProperties.ymCatalogButtonXpath()));

        this.productsOnPage = new ArrayList<>();
    }

    /**
     * Выполняет поиск товара по текстовому запросу.
     *
     * @param query строка запроса для поиска
     *
     * @author Сергей Лужин
     */
    public void findViaSearchInput(String query) {
        searchInput = Driver.getWebDriver().findElement(By.xpath(xpathProperties.ymSearchInputXpath()));
        searchInput.sendKeys(query);
        searchInput.sendKeys(ENTER);
    }

    /**
     * Нажимает кнопку каталога.
     *
     * @author Сергей Лужин
     */
    public void clickOnCatalogButton() {
        wait.until(visibilityOfElementLocated(By.xpath(xpathProperties.ymCatalogButtonXpath())));
        catalogButton.click();
    }

    /**
     * Наводит курсор на категорию каталога.
     *
     * @param category название категории
     *
     * @author Сергей Лужин
     */
    public void hoverOnCategoryInCatalog(String category) {
        String xpath = xpathProperties.ymCatalogCategoryXpath().replace("*category*", category);

        WebElement categoryElement = wait.until(
                visibilityOfElementLocated(By.xpath(xpath))
        );

        Actions actions = new Actions(driver);
        actions.moveToElement(categoryElement).perform();
    }

    /**
     * Кликает по подкатегории каталога.
     *
     * @param subcategory название подкатегории
     *
     * @author Сергей Лужин
     */
    public void clickOnSubcategoryInCatalog(String subcategory) {
        String xpath = xpathProperties.ymCatalogSubcategoryXpath().replace("*subcategory*", subcategory);

        WebElement subcategoryElement = wait.until(
                visibilityOfElementLocated(By.xpath(xpath))
        );

        subcategoryElement.click();
    }

    /**
     * Устанавливает минимальную цену в фильтре товаров.
     *
     * @param price минимальная цена
     * @author Сергей Лужин
     */
    public void setFilterPriceMin(int price) {
        String xpath = xpathProperties.ymFilterPriceMinXpath();

        WebElement inputFilterPriceMin = wait.until(
                visibilityOfElementLocated(By.xpath(xpath))
        );

        inputFilterPriceMin.sendKeys(Integer.toString(price));
    }

    /**
     * Устанавливает максимальную цену в фильтре товаров.
     *
     * @param price максимальная цена
     * @author Сергей Лужин
     */
    public void setFilterPriceMax(int price) {
        String xpath = xpathProperties.ymFilterPriceMaxXpath();

        WebElement inputFilterPriceMax = wait.until(
                visibilityOfElementLocated(By.xpath(xpath))
        );

        inputFilterPriceMax.sendKeys(Integer.toString(price));
    }

    /**
     * Устанавливает фильтры брендов, кликая по каждому бренду в списке.
     *
     * @param brands список брендов для фильтрации
     *
     * @author Сергей Лужин
     */
    public void clickBrandCheckbox(List<String> brands) {
        for (String brand : brands) {
            String xpath = xpathProperties.ymFilterBrandXpath().replace("*brand*", brand);

            WebElement brandFilterElement = wait.until(
                    visibilityOfElementLocated(By.xpath(xpath))
            );

            brandFilterElement.click();

            //Ожидаем прогрузки новых товаров
            try {
                Thread.sleep(testProperties.explicitWaitTimeoutMs());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Последовательно прокручивает страницу вниз и собирает все товары,
     * добавляя их в список {@code productsOnPage}, пока не будет достигнут конец страницы.
     *
     *
     * @author Сергей Лужин
     */
    public void scrollToBottomAndCollectAllProducts() {
        JavascriptExecutor js = (JavascriptExecutor) driver;

        int doubledPositionsCount = 0;
        int trueCurrentIndex = 0;

        while (true) {
            List<WebElement> productElemnets =
                    driver.findElements(By.xpath(xpathProperties.ymCardsOnAllPagesXpath()));

            trueCurrentIndex = productsOnPage.size() + doubledPositionsCount;

            if (trueCurrentIndex < productElemnets.size()) {
                new Actions(driver)
                        .moveToElement(productElemnets.get(trueCurrentIndex))
                        .perform();

                System.out.println("Пробуем добавить товар под индексом: " + trueCurrentIndex);
                boolean isAdded = Product.saveProductFromElement(productElemnets.get(trueCurrentIndex), this);
                System.out.println("На данный момент было добавлено: " + productsOnPage.size() + " товаров");

                if (!isAdded) {
                    doubledPositionsCount++;
                    System.out.println("НАЙДЕНА ДУБЛИРОВАННА ПОЗИЦИЯ." +
                            "Общее количество дублированных позиций: " + doubledPositionsCount);
                }
            }
            else {
                js.executeScript("window.scrollBy(0, arguments[0]);", 500);
            }

            if (hasReachedBottomOfPage(js)) {
                System.out.println("Пытаемся завершить скроллинг, так как был достигнут конец страницы");

                boolean stillAtBottom = isStillAtBottomAfterWait(js, driver);

                if (stillAtBottom) {
                    System.out.println("Подождали, страница больше не прогрузилась. ЗАВЕРШАЕМ");
                    System.out.println("Финальное количество добавленных товаров: " + productsOnPage.size());
                    break;
                }
                else {
                    System.out.println("Подождали, страница прогрузилась еще. ПРОДОЛЖАЕМ");
                }
            }
        }
    }

    /**
     * Возвращает текст заголовка карточки товара.
     *
     * @param element веб-элемент карточки товара
     * @return название товара
     *
     * @author Сергей Лужин
     */
    public String getProductCardTitle(WebElement element){
        WebDriverWait wait = new WebDriverWait(
                driver,
                testProperties.defaultTimeout()
        );

        try {
            return wait.until(d -> {
                WebElement titleElement =
                        element.findElement(By.xpath(xpathProperties.ymCardTitleAddonXpath()));

                String text = titleElement.getText().trim();
                // если текст пустой — возвращаем null, WebDriverWait продолжит ждать
                return text.isEmpty() ? null : text;
            });
        } catch (TimeoutException e) {
            System.out.println("[WAIT] Заголовок товара не стал непустым за отведённое время");
            return "";
        }
    }

    /**
     * Возвращает числовое значение цены товара из карточки.
     * Очищает текст от пробелов и нецифровых символов перед преобразованием.
     *
     * @param element веб-элемент карточки товара
     * @return цена товара в виде целого числа
     *
     * @author Сергей Лужин
     */
    public int getProductCardPrice(WebElement element) {
        WebDriverWait wait = new WebDriverWait(
                driver,
                testProperties.defaultTimeout()
        );

        try {
            return wait.until(d -> {
                WebElement titleElement =
                        element.findElement(By.xpath(xpathProperties.ymCardPriceAddonXpath()));

                String text = titleElement.getText();

                if (text.isEmpty()){
                    return null;
                }
                else {
                    return Integer.parseInt(text
                            .replaceAll("[\\s\\u00A0\\u2006\\u2007\\u2008\\u2009\\u200A]", "")
                            .replaceAll("[^\\d]", ""));
                }
            });
        } catch (TimeoutException e) {
            System.out.println("[WAIT] Цена товара не стала непустой за отведённое время");
            return 0;
        }

    }
}

