package helpers;

import org.junit.jupiter.params.provider.Arguments;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Класс-поставщик тестовых данных для параметризованных тестов.
 * Содержит набор функций, возвращающих поток аргументов для тестов.
 *
 * @author Сергей Лужин
 */
public class DataProvider {
    public static Stream<Arguments> providerYMtestCatalog(){
        return Stream.of(
                Arguments.of(
                        "Электроника",                       // String category
                        "Ноутбуки",                                     // String subcategory
                        10000,                                          // int minPrice
                        20000,                                          // int maxPrice
                        Arrays.asList("Lenovo", "HP"),                  // List<String> brands
                        0,                                              // int indexOfCheckedElement
                        12                                              // int checkedProductAmount
                )
        );
    }
}
