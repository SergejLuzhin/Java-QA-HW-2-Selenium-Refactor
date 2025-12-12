package pages;

import entity.Product;
import helpers.Driver;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
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
 * <p>
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

    protected FluentWait<WebDriver> fluentWait;

    /**
     * Конструктор инициализирует элементы страницы,
     * ожидая появления ключевых элементов поиска и каталога.
     *
     * @author Сергей Лужин
     */
    public YandexMarketPage() {
        this.driver = Driver.getWebDriver();
        this.wait = new WebDriverWait(driver, testProperties.defaultTimeout());

        this.fluentWait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(testProperties.defaultTimeout()))
                .ignoring(StaleElementReferenceException.class)
                .ignoring(TimeoutException.class);


        this.searchInput = driver.findElement(By.xpath(xpathProperties.ymSearchInputXpath()));

        this.searchButton = driver.findElement(By.xpath(xpathProperties.ymSearchButtonXpath()));

        this.catalogButton = driver.findElement(By.xpath(xpathProperties.ymCatalogButtonXpath()));

        this.productsOnPage = new ArrayList<>();
    }

    /**
     * Выполняет поиск товара по текстовому запросу.
     *
     * @param query строка запроса для поиска
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
     * @author Сергей Лужин
     */
    public void hoverOnCategoryInCatalog(String category) {
        String xpath = xpathProperties.ymCatalogCategoryXpath().replace("*category*", category);

        WebElement categoryElement = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath(xpath))
        );

        new Actions(driver).moveToElement(categoryElement).perform();
    }

    /**
     * Кликает по подкатегории каталога.
     *
     * @param subcategory название подкатегории
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
     * @author Сергей Лужин
     */
    public void clickBrandCheckbox(List<String> brands) {
        for (String brand : brands) {
            String xpath = xpathProperties.ymFilterBrandXpath().replace("*brand*", brand);

            WebElement brandFilterElement = wait.until(
                    visibilityOfElementLocated(By.xpath(xpath))
            );

            brandFilterElement.click();

            List<WebElement> productElements =
                    driver.findElements(By.xpath(xpathProperties.ymCardsOnAllPagesXpath()));

            fluentWait.until(ExpectedConditions.stalenessOf(productElements.get(0)));
        }
    }

    /**
     * Последовательно прокручивает страницу вниз и собирает все товары,
     * добавляя их в список {@code productsOnPage}, пока не будет достигнут конец страницы.
     *
     * @author Сергей Лужин
     */
    public void scrollToBottomAndCollectAllProducts() {
        JavascriptExecutor js = (JavascriptExecutor) driver;

        int unacceptedPositionsCount = 0;
        int trueCurrentIndex;
        int currentIndex;

        while (true) {
            //trueCurrentIndex = productsOnPage.size() + unacceptedPositionsCount;

            currentIndex = productsOnPage.size();

            List<WebElement> productElements =
                    driver.findElements(By.xpath(xpathProperties.ymCardsOnAllPagesXpath()));

            if (currentIndex < productElements.size()) {
                new Actions(driver)
                        .moveToElement(productElements.get(currentIndex))
                        .perform();

                Product.saveProductFromElement(productElements.get(currentIndex), this);

                System.out.println("На данный момент было добавлено: " + productsOnPage.size() + " товаров");

                //if (!isAdded) unacceptedPositionsCount++;
            } else {
                long end = System.currentTimeMillis() + testProperties.defaultTimeout();
                boolean hasUpdated = false;

                while (System.currentTimeMillis() < end) {
                    productElements =
                            driver.findElements(By.xpath(xpathProperties.ymCardsOnAllPagesXpath()));

                    if (productElements.size() > currentIndex) {
                        hasUpdated = true;
                        break;
                    }

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

                if (!hasUpdated) {
                    System.out.println("Финальное количество добавленных товаров: " + productsOnPage.size());
                    break;
                }
            }
        }
    }

    /**
     * Возвращает текст заголовка карточки товара.
     *
     * @param element веб-элемент карточки товара
     * @return название товара
     * @author Сергей Лужин
     */
    public String getProductCardTitle(WebElement element) {
        String title = "ДЕФЕКТНАЯ КАРТОЧКА ТОВАРА";

        WebElement hrefElement = element.findElement(By.xpath(xpathProperties.ymCardHrefAddonXpath()));

        if (hrefElement.getAttribute("href").isEmpty()) {
            return title;
        }

        WebElement titleElement =
                element.findElement(By.xpath(xpathProperties.ymCardTitleAddonXpath()));

        long end = System.currentTimeMillis() + testProperties.defaultTimeout();

        while (System.currentTimeMillis() < end) {
            fluentWait.until(d ->
                    ExpectedConditions.visibilityOf(titleElement)
            );

            String text = titleElement.getText().trim();

            if (!text.isEmpty()) {
                title = text;
                break;
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        return title;
    }

    /**
     * Возвращает числовое значение цены товара из карточки.
     * Очищает текст от пробелов и нецифровых символов перед преобразованием.
     *
     * @param element веб-элемент карточки товара
     * @return цена товара в виде целого числа
     * @author Сергей Лужин
     */
    public int getProductCardPrice(WebElement element) {
        int price = 0;

        WebElement hrefElement = element.findElement(By.xpath(xpathProperties.ymCardHrefAddonXpath()));

        if (hrefElement.getAttribute("href").isEmpty()) {
            return price;
        }

        WebElement priceElement =
                element.findElement(By.xpath(xpathProperties.ymCardPriceAddonXpath()));

        long end = System.currentTimeMillis() + testProperties.defaultTimeout();

        while (System.currentTimeMillis() < end) {
            fluentWait.until(d ->
                    ExpectedConditions.visibilityOf(priceElement)
            );

            String text = priceElement.getText().trim();

            if (!text.isEmpty()) {
                price = Integer.parseInt(text
                        .replaceAll("[\\s\\u00A0\\u2006\\u2007\\u2008\\u2009\\u200A]", "")
                        .replaceAll("[^\\d]", ""));
                break;
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        return price;
    }
}

