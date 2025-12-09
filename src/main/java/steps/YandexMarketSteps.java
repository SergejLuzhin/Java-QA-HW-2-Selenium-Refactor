package steps;

import entity.Product;
import helpers.Driver;
import helpers.Scroller;
import helpers.SoftChecker;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebElement;
import pages.YandexMarketPage;


import java.util.ArrayList;
import java.util.List;

/**
 * Класс с шагами для работы с интерфейсом Яндекс Маркета.
 * Используется в тестах как набор переиспользуемых действий:
 * открытие сайта, выбор категории, установка фильтров, работа с карточками товаров, проверки.
 *
 * @author Сергей Лужин
 */
public class YandexMarketSteps {

    /**
     * Открывает указанный URL в переданном экземпляре WebDriver
     * и сохраняет этот драйвер во внутреннее статическое поле.
     *
     * @param url           адрес сайта, который нужно открыть
     *
     * @author Сергей Лужин
     */
    @Step("Переходим на сайт: {url}")
    public static void openSite(String url){
        Driver.getWebDriver().get(url);
    }

    /**
     * Переходит в каталог Яндекс Маркета, выбирает указанную категорию
     * и подкатегорию.
     *
     * @param category    название категории каталога
     * @param subcategory название подкатегории каталога
     * @param ymPage      объект страницы Яндекс Маркета
     *
     * @author Сергей Лужин
     */
    @Step("Выбираем категорию '{category}' и подкатегорию {subcategory} в каталоге")
    public static void chooseCategory(String category, String subcategory, YandexMarketPage ymPage) {
        ymPage.clickOnCatalogButton();
        ymPage.hoverOnCategoryInCatalog(category);
        ymPage.clickOnSubcategoryInCatalog(subcategory);
    }

    /**
     * Устанавливает фильтры поиска по цене и брендам на странице каталога.
     *
     * @param minPrice минимальная цена фильтра
     * @param maxPrice максимальная цена фильтра
     * @param brands   список брендов, по которым нужно отфильтровать товары
     * @param ymPage   объект страницы Яндекс Маркета
     *
     * @author Сергей Лужин
     */
    @Step("Устанавливаем фильтры поиска: Минимальная цена - {minPrice}, Максимальная цена - {maxPrice}, Бренды - {brands}")
    public static void setFilters(int minPrice, int maxPrice, List<String> brands, YandexMarketPage ymPage) {
        ymPage.setFilterPriceMin(minPrice);
        ymPage.setFilterPriceMax(maxPrice);
        ymPage.clickBrandCheckbox(brands);
    }

    /**
     * Получает все карточки товаров на странице,
     * скроллит страницу вниз и вверх, для прогрузки всех карточек,
     * а затем сохраняет каждый товар в сущность Product.
     *
     * @param ymPage объект страницы Яндекс Маркета
     *
     * @author Сергей Лужин
     */
    @Step("Получаем список всех карточек товаров на странице")
    public static void getAllProductCards(YandexMarketPage ymPage) {
        //Scroller.scrollToBottomOfPage();
        //Scroller.scrollToTopOfPage();
        //List<WebElement> productCards = ymPage.scrollToBottomAndCollectAllProducts();
        ymPage.scrollToBottomAndCollectAllProducts();

       /* for (WebElement productCard : productCards) {
            //System.out.println(productCard.toString());
            Product.saveProductFromElement(productCard, ymPage);
        }*/
    }

    /**
     * Выполняет комплекс мягких проверок (soft assertions) по товарам:
     * – соответствие товара поисковому запросу
     * – попадание в ценовой диапазон
     * – соответствие брендам
     * – достаточное количество товаров после фильтрации
     *
     * @param yandexMarketBeforeSearch объект страницы до поиска
     * @param yandexMarketAfterSearch  объект страницы после поиска
     * @param checkedAmount            минимальное количество товаров
     * @param minPrice                 минимальная цена
     * @param maxPrice                 максимальная цена
     * @param brands                   список брендов
     * @param indexOfCheckedElement    индекс проверяемого товара
     *
     * @author Сергей Лужин
     */
    @Step("Проводим софт проверки по окончании теста. Проверяем количество товаров, их соответствие фильтрам, а также, что сохраненный товар был найден через поиск")
    public static void runChecksSoftly(YandexMarketPage yandexMarketBeforeSearch, YandexMarketPage yandexMarketAfterSearch, int checkedAmount, int minPrice, int maxPrice, List<String> brands, int indexOfCheckedElement) {
        List<String> wrongPriceProducts = new ArrayList<>();
        List<String> wrongTitleProducts = new ArrayList<>();
        for (Product product : yandexMarketBeforeSearch.productsOnPage) {

            if (product.getPrice() < minPrice || product.getPrice() > maxPrice) {
                wrongPriceProducts.add(product.toString());
            }

            if (brands.stream().noneMatch(brand -> product.getTitle().toLowerCase().contains(brand.toLowerCase())) && !product.getTitle().isEmpty()) {
                wrongTitleProducts.add(product.toString());
            }
        }

        Product checkedProduct = yandexMarketBeforeSearch.productsOnPage.get(indexOfCheckedElement);

        boolean productIsFoundOnPage = yandexMarketAfterSearch.productsOnPage.stream()
                .anyMatch(p -> p.equals(checkedProduct));

        Assertions.assertAll(
                () -> SoftChecker.check(
                        yandexMarketBeforeSearch.productsOnPage.size() > checkedAmount,
                        "Проверяем, что после заданных фильтров было найдено как минимум " + checkedAmount + " товаров",
                        "Было найдено меньше товаров, чем " + checkedAmount + ". Было найдено только " + yandexMarketBeforeSearch.productsOnPage.size() + " товаров"
                ),
                () -> SoftChecker.check(
                        productIsFoundOnPage,
                        "Проверяем: что " + checkedProduct.getTitle() + " был найден на странице после поиска",
                        "Товар " + checkedProduct.getTitle() + " не был найден на странице после поиска"
                ),
                () -> SoftChecker.check(
                        wrongPriceProducts.isEmpty(),
                        "Проверяем, что товары на странице, после ввода фильтров соответствовали ценовому диапазону " + minPrice + " - " + maxPrice + " рублей",
                        "Были найдены товары, которые не соответсвуют ценовому дипазону от " + minPrice + " до " + maxPrice + " рублей: " + wrongPriceProducts
                ),
                () -> SoftChecker.check(
                        wrongTitleProducts.isEmpty(),
                        "Проверяем, что товары на странице, после ввода фильтров соответствовали брендам " + brands,
                        "Были найдены товары, которые не соответствуют брендам " + brands + ": " + wrongTitleProducts
                )
        );
    }

    /**
     * Выполняет поиск товара на Яндекс Маркете по переданному текстовому запросу.
     *
     * @param query поисковый запрос, по которому нужно найти товары
     * @param ymPage      объект страницы Яндекс Маркета
     *
     * @author Сергей Лужин
     */
    @Step("Делаем поиск на Яндекс Маркете по запросу: {query}")
    public static void goBySearchQuery(String query, YandexMarketPage ymPage) {
        ymPage.findViaSearchInput(query);
    }

    /**
     * Проверяет, что заголовок (title) текущей страницы
     * соответствует выбранной подкатегории каталога.
     * Валидация выполняется через assertTrue с сообщением об ошибке.
     *
     * @param subcategory ожидаемое название подкатегории в заголовке страницы
     *
     * @author Сергей Лужин
     */
    @Step("Проверяем, что открытая страница сответсвует категории {subcategory}")
    public static void checkPageTitle(String subcategory) {
        Assertions.assertTrue(Driver.getWebDriver().getTitle().contains(subcategory), "Тайтл " + Driver.getWebDriver().getTitle() + " на сайте не соответствует категории " + subcategory);
    }

}
