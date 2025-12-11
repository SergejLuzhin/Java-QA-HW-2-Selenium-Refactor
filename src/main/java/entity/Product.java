package entity;

import org.openqa.selenium.WebElement;
import pages.YandexMarketPage;
import java.util.Objects;

import static helpers.Screenshoter.attachScreenshot;

/**
 * Сущность товара.
 * Содержит название и цену, а также служебные методы
 * для сравнения и текстового представления товара.
 *
 * @author Сергей Лужин
 */
public class Product {
    /**
     * Название товара.
     */
    private final String title;

    /**
     * Цена товара.
     */
    private final int price;

    /**
     * Создаёт экземпляр товара с указанными названием и ценой.
     *
     * @param title название товара
     * @param price цена товара
     * @author Сергей Лужин
     */
    public Product(String title, int price) {
        this.title = title;
        this.price = price;
    }

    /**
     * Возвращает цену товара.
     *
     * @return цена товара
     * @author Сергей Лужин
     */
    public int getPrice() {
        return price;
    }

    /**
     * Возвращает название товара.
     *
     * @return название товара
     * @author Сергей Лужин
     */
    public String getTitle() {
        return title;
    }

    /**
     * Создаёт объект товара из веб-элемента карточки
     * и добавляет его в список товаров страницы,
     * если его копии еще нет в данном списке
     *
     * @param productElement веб-элемент карточки товара
     * @param ymPage         страница Яндекс Маркета, содержащая методы для чтения данных товара и хранящая список товаров на ней
     * @author Сергей Лужин
     */
    public static boolean saveProductFromElement(WebElement productElement, YandexMarketPage ymPage) {
        String productTitle = ymPage.getProductCardTitle(productElement);
        int productPrice = ymPage.getProductCardPrice(productElement);

        Product candidate = new Product(productTitle, productPrice);

        attachScreenshot(candidate.toString());

        if (!ymPage.productsOnPage.contains(candidate) && !productTitle.isEmpty() && productPrice != 0) {
            ymPage.productsOnPage.add(candidate);
            System.out.println("Добавили: " + candidate.getTitle());
            return true;
        } else {
            System.out.println("Не добавили: " + candidate.getTitle());
            return false;
        }
    }

    /**
     * Сравнивает два товара по названию и цене.
     *
     * @param obj объект для сравнения
     * @return true, если товары равны по полям title и price, иначе false
     * @author Сергей Лужин
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Product product = (Product) obj;
        return price == product.price &&
                Objects.equals(title, product.title);
    }

    /**
     * Возвращает хэш-код товара на основе названия и цены.
     *
     * @return хэш-код товара
     * @author Сергей Лужин
     */
    @Override
    public int hashCode() {
        return Objects.hash(title, price);
    }

    /**
     * Возвращает строковое представление товара
     * в формате "Название стоимостью N рублей."
     *
     * @return человекочитаемое описание товара
     * @author Сергей Лужин
     */
    @Override
    public String toString() {
        return (this.getTitle() + " стоимостью " + this.getPrice() + " рублей. ");
    }
}
