package pages;

import entity.Product;
import helpers.Driver;
import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.json.JsonOutput;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

import static org.openqa.selenium.Keys.ENTER;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

import static helpers.Properties.testProperties;
import static helpers.Properties.xpathProperties;

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

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Возвращает список веб-элементов карточек товаров на текущей странице.
     *
     * @return список WebElement карточек товаров
     *
     * @author Сергей Лужин
     */
    public List<WebElement> getAllProductCardsOnPage() {
        return driver.findElements(By.xpath(xpathProperties.ymCardsOnAllPagesXpath()));
    }

    public void scrollToBottomAndCollectAllProducts() {
        JavascriptExecutor js = (JavascriptExecutor) driver;

        int currentLastElementIndex = 0;
        int doubledPositionsCount = 0;
        boolean isAdded = false;
        int trueIndex = 0;

        while (true) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            currentLastElementIndex = productsOnPage.size();
            isAdded = false;

            List<WebElement> productCards =
                    driver.findElements(By.xpath(xpathProperties.ymCardsOnAllPagesXpath()));

            trueIndex = currentLastElementIndex + doubledPositionsCount;

            if (trueIndex < productCards.size()) {
                new Actions(driver)
                        .moveToElement(productCards.get(trueIndex))
                        .perform();

                System.out.println("Пробуем добавить товар под индексом: " + trueIndex);
                isAdded = Product.saveProductFromElement(productCards.get(trueIndex), this);

                currentLastElementIndex++;

                System.out.println("Сейчас в productsOnPage: " + productsOnPage.size() + " товаров");
                if (!isAdded) {
                    doubledPositionsCount++;
                }
                System.out.println("Количество дублированных позиций: " + doubledPositionsCount);
            }
            else {
                System.out.println("СКРОЛЛИМ МАНУАЛЬНО");
                js.executeScript("window.scrollBy(0, arguments[0]);", 500);
            }

            Number offset = (Number) js.executeScript("return window.pageYOffset;");
            Number window = (Number) js.executeScript("return window.innerHeight;");
            Number height = (Number) js.executeScript("return document.body.scrollHeight;");

            double currentBottom = offset.doubleValue() + window.doubleValue();
            double pageHeight = height.doubleValue();

            if (currentBottom >= pageHeight - 50) {
                System.out.println("Пытаемся завершить скроллинг, так как был достигнут конец страницы");

                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                offset = (Number) js.executeScript("return window.pageYOffset;");
                window = (Number) js.executeScript("return window.innerHeight;");
                height = (Number) js.executeScript("return document.body.scrollHeight;");

                currentBottom = offset.doubleValue() + window.doubleValue();
                pageHeight = height.doubleValue();

                if (currentBottom >= pageHeight - 50) {
                    System.out.println("Подождали, но страница больше не прогрузилась. ЗАВЕРШАЕМ");
                    System.out.println("Финальное количество товаров в productsOnPage: " + productsOnPage.size());
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
     * @param productElement веб-элемент карточки товара
     * @return название товара
     *
     * @author Сергей Лужин
     */
    public String getProductCardTitle(WebElement productElement) {
        By titleLocator = By.xpath(xpathProperties.ymCardTitleAddonXpath());

        WebDriverWait wait = new WebDriverWait(driver, 10); // Selenium 3: timeout в секундах

        // ждём, пока текст в элементе станет НЕ пустым
        String title = wait.until(d -> {
            try {
                WebElement titleElement = productElement.findElement(titleLocator);
                String text = titleElement.getText();
                if (text != null && !text.trim().isEmpty()) {
                    return text;
                }
                return null; // продолжаем ждать
            } catch (StaleElementReferenceException e) {
                return null; // элемент переотрисовался, ждём ещё
            }
        });

        return title.trim();
    }

    /**
     * Возвращает числовое значение цены товара из карточки.
     * Очищает текст от пробелов и нецифровых символов перед преобразованием.
     *
     * @param productElement веб-элемент карточки товара
     * @return цена товара в виде целого числа
     *
     * @author Сергей Лужин
     */
    public int getProductCardPrice(WebElement productElement) {
        By priceLocator = By.xpath(xpathProperties.ymCardPriceAddonXpath());

        WebDriverWait wait = new WebDriverWait(driver, 10);

        // ждём, пока удастся вытащить непустую числовую строку
        String priceText = wait.until(d -> {
            try {
                WebElement priceElement = productElement.findElement(priceLocator);
                String text = priceElement.getText();
                if (text == null) {
                    return null;
                }

                String digits = text.replaceAll("[^0-9]", "");
                if (digits.isEmpty()) {
                    return null; // ждём дальше
                }

                return digits;
            } catch (StaleElementReferenceException e) {
                return null;
            }
        });

        return Integer.parseInt(priceText);
    }
}

