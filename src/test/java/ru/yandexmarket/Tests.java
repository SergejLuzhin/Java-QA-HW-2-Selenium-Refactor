package ru.yandexmarket;

import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import pages.YandexMarketPage;
import java.util.List;

import static steps.YandexMarketSteps.*;
import static helpers.Properties.testProperties;

/**
 * Набор UI-тестов для проверки работы каталога Яндекс Маркета.
 * Использует шаги из {@link steps.YandexMarketSteps} и страницу {@link YandexMarketPage}
 * для проверки перехода по категориям, применения фильтров и поиска товаров.
 *
 * @author Сергей Лужин
 */
public class Tests extends BaseTests {

    /**
     * Параметризованный тест проверки каталога Яндекс Маркета по заданным параметрам.
     *
     * Последовательно:
     * открывает сайт,
     * выбирает категорию и подкатегорию,
     * проверяет заголовок страницы,
     * применяет фильтры по цене и брендам,
     * запоминает один из товаров,
     * выполняет поиск по его названию
     * и выполняет набор софт проверок по результатам.
     *
     * @param category              категория каталога
     * @param subcategory           подкатегория каталога
     * @param minPrice              минимальная цена фильтра
     * @param maxPrice              максимальная цена фильтра
     * @param brands                список брендов для фильтрации
     * @param indexOfCheckedElement индекс товара, который будет сохранён и проверен по поиску
     * @param checkedProductsAmount минимальное ожидаемое количество товаров после фильтрации
     *
     * @author Сергей Лужин
     */
    @Feature("Проверка каталога яндекс маркета")
    @DisplayName("Проверка каталога по заданным параметрам: ")
    @ParameterizedTest(name = "{displayName}: {arguments}")
    @MethodSource("helpers.DataProvider#providerYMtestCatalog")
    public void testYandexMarketCatalog(String category, String subcategory, int minPrice, int maxPrice, List<String> brands, int indexOfCheckedElement, int checkedProductsAmount){
        openSite(testProperties.yandexMarketUrl());
        YandexMarketPage yandexMarketBeforeSearch = new YandexMarketPage();
        chooseCategory(category, subcategory, yandexMarketBeforeSearch);
        checkPageTitle(subcategory);
        setFilters(minPrice, maxPrice, brands, yandexMarketBeforeSearch);
        getAllProductCards(yandexMarketBeforeSearch);
        goBySearchQuery(yandexMarketBeforeSearch.productsOnPage.get(indexOfCheckedElement).getTitle(), yandexMarketBeforeSearch);
        YandexMarketPage yandexMarketAfterSearch = new YandexMarketPage();
        getAllProductCards(yandexMarketAfterSearch);
        runChecksSoftly(yandexMarketBeforeSearch, yandexMarketAfterSearch, checkedProductsAmount,minPrice, maxPrice, brands, indexOfCheckedElement);
    }

}
