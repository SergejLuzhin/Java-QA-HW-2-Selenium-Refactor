package helpers;

import org.aeonbits.owner.Config;

/**
 * Конфигурационный интерфейс для хранения XPath-локаторов элементов Яндекс Маркета.
 * Описывает ключи, по которым из файла xpath.properties будут загружаться значения XPath.
 *
 * Файл конфигурации:
 * src/main/resources/xpath.properties
 *
 * @author Сергей Лужин
 */
@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "file:src/main/resources/xpath.properties"
})
public interface XpathProperties extends Config {

    /**
     * Возвращает XPath локатор поля ввода текста в поиске Яндекс Маркета.
     *
     * @return XPath для поля ввода поискового запроса
     * @author Сергей Лужин
     */
    @Config.Key("ym.search.text.input")
    String ymSearchInputXpath();

    /**
     * Возвращает XPath локатор кнопки поиска на Яндекс Маркете.
     *
     * @return XPath для кнопки запуска поиска
     * @author Сергей Лужин
     */
    @Config.Key("ym.search.button")
    String ymSearchButtonXpath();

    /**
     * Возвращает XPath локатор кнопки открытия каталога.
     *
     * @return XPath для кнопки каталога Яндекс Маркета
     * @author Сергей Лужин
     */
    @Config.Key("ym.catalog.button")
    String ymCatalogButtonXpath();

    /**
     * Возвращает XPath локатор элемента категории в каталоге.
     * Используется с подстановкой названия категории.
     *
     * @return XPath-шаблон для выбора категории каталога
     * @author Сергей Лужин
     */
    @Config.Key("ym.catalog.category")
    String ymCatalogCategoryXpath();

    /**
     * Возвращает XPath локатор элемента подкатегории в каталоге.
     * Используется с подстановкой названия подкатегории.
     *
     * @return XPath-шаблон для выбора подкатегории каталога
     * @author Сергей Лужин
     */
    @Config.Key("ym.catalog.subcategory")
    String ymCatalogSubcategoryXpath();

    /**
     * Возвращает XPath локатор поля минимальной цены в фильтре.
     *
     * @return XPath для поля ввода минимальной цены
     * @author Сергей Лужин
     */
    @Config.Key("ym.filter.priceMin")
    String ymFilterPriceMinXpath();

    /**
     * Возвращает XPath локатор поля максимальной цены в фильтре.
     *
     * @return XPath для поля ввода максимальной цены
     * @author Сергей Лужин
     */
    @Config.Key("ym.filter.priceMax")
    String ymFilterPriceMaxXpath();

    /**
     * Возвращает XPath локатор чекбоксов брендов в фильтре.
     * Используется с подстановкой названия бренда.
     *
     * @return XPath-шаблон для чекбокса бренда
     * @author Сергей Лужин
     */
    @Config.Key("ym.filter.brand")
    String ymFilterBrandXpath();

    /**
     * Возвращает XPath локатор всех карточек товаров на странице.
     *
     * @return XPath для найденных карточек товаров
     * @author Сергей Лужин
     */
    @Config.Key("ym.card.on.all.pages")
    String ymCardsOnAllPagesXpath();

    /**
     * Возвращает XPath локатор заголовков карточек товаров на странице.
     *
     * @return XPath для элементов, содержащих названия товаров
     * @author Сергей Лужин
     */
    @Config.Key("ym.card.titles")
    String ymCardTitlesXpath();

    /**
     * Возвращает дополнительный XPath для поиска заголовка карточки товара.
     * Использоваться для поиска вложенного элемента внутри карточки.
     *
     * @return дополнительный XPath для элемента заголовка товара
     * @author Сергей Лужин
     */
    @Config.Key("ym.card.title.addon")
    String ymCardTitleAddonXpath();

    /**
     * Возвращает дополнительный XPath для поиска числового значения цены карточки товара.
     * Использоваться для поиска вложенного элемента внутри карточки.
     *
     * @return дополнительный XPath для элемента числового значения цены товара
     * @author Сергей Лужин
     */
    @Config.Key("ym.card.price.addon")
    String ymCardPriceAddonXpath();
}
