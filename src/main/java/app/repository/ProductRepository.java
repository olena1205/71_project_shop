package app.repository;

import app.domain.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
Репозитории - это второй слой приложения
Задача репозитория - обеспечить простейший доступ к данным
посредством реализации CRUD операций
CRUD - Create(Создать), Read(прочитать), Update(обновить), Delete(удалить)

 */
public class ProductRepository {
    // Файл, который является базой данных
    private final File database;

    // Маппер для чтения и записи объектов в файл
    private final ObjectMapper mapper;

    // Поле, которое хранит максимальный идентификатор, сохраненный в БД
    private int maxId;

    // Конструктор
    // В этом конструкторе мы инициализируем все поля репозитория
    public ProductRepository() throws IOException {
        database = new File("database/product.txt");
        mapper = new ObjectMapper();

        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        // Выясняем, какой идентификатор БД на данный момент максимальный
        List<Product> products = findAll();
//      Получаем последний элемент списка с условием, что перед тем, как забрать максимальный
//      идентификатор, мы должны убедиться, что он не пустой
        if (!products.isEmpty()) {
//      Находим последний элемент списка, он и будет иметь максимальный идентификатор ->
//      создаем переменную типа Product - lastProduct. Дальше обращаемся к
//      products, т.е. к нашему списку и вызываем метод get, который позволит нам
//      получить последний элемент по индексу. Если мы обратимся к нашему списку, то метод
//      size() вернет нам его размер, но последний идентификатор будет на 1 меньше,
//      потому что начинается с нуля, а значит мы просто делаем -1.
//      Таким образом, мы получим индекс последнего элемента, а метод get() нам этот элемент
//      вернет и соответственно мы сможем его спокойно сохранить в lastProduct
            Product lastProduct = products.get(products.size() - 1);
            // обращаемся к lastProduct и вызываем геттер, который вернет идентификатор
            maxId = lastProduct.getId();

        }
    }

    // Сохранение нового продукта в БД
//    Общепринятая логика - после сохранения вернуть то, что сохранили, поэтому прописываем
//    в качестве возвращаемого значения тип Product. Ну и конечно же, чтобы что-то сохранить,
//    нужно понимать, что мы сохраняем, поэтому этот метод принимает данные продукта, которые
//    мы прописываем в качестве соответствующих параметров.
    public Product save(Product product) throws IOException {
        product.setId(++maxId); // сначала увеличиваем и потом возвращаем уже maxId(префиксный инкремент)
        List<Product> products = findAll();// получаем переменную, которая будет хранить список старых продуктов
        products.add(product); // добавляем новый объект в конец списка
        mapper.writeValue(database, products); // чтобы БД узнала о дополненом списке,
        // вызываем mapper и полностью перезаписываем БД для продукта, т.е. используем
        // writeValue, указываем куда мы сохраняем и что мы сохраняем
        return product;
    }

    // Чтение всех продуктов из БД
//    Этот метод возвращает нам список. Это значит, что мы можем в других методах
//    получать уже просто готовый список Java, который мы уже хорошо знаем, знаем
//    какие у него есть методы и делать дальше действия, которые нам понадобятся
    public List<Product> findAll() throws IOException {
        try {
            Product[] products = mapper.readValue(database, Product[].class);
//      для того, чтобы передать все значения из нашего массива, мы обращаемся
//      к классу Arrays -> обращаемся к методу asList и просто в вызов метода
//      отправляем наш продукт
            return new ArrayList<>(Arrays.asList(products));
        } catch (MismatchedInputException e) {
            return new ArrayList<>();
        }
    }

    // Чтение одного продукта по id
    public Product findById(int id) throws IOException {
        return findAll().
                stream()
                .filter(x -> x.getId() == id)
                .findFirst()
                .orElse(null);
    }

    // Обновление существующего продукта
    // этот метод будет менять только цену продукта
    public void update(Product product) throws IOException {
        int id = product.getId();
        double newPrice = product.getPrice();
        boolean active = product.isActive();

        List<Product> products = findAll();
        products
                .stream()
                .filter(x -> x.getId() == id)
                .forEach(x -> {
                            x.setPrice(newPrice);
                            x.setActive(active);
                        }
                );

        mapper.writeValue(database, products);
    }

    // Удаление продукта
    public void deleteById(int id) throws IOException {
        List<Product> products = findAll();
        products.removeIf(x -> x.getId() == id);
        mapper.writeValue(database, products);
    }
}
