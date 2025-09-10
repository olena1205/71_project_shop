package app.service;

import app.domain.Customer;
import app.exceptions.CustomerNotFoundException;
import app.exceptions.CustomerSaveException;
import app.repository.CustomerRepository;

import java.io.IOException;
import java.util.List;

public class CustomerService {

    private final CustomerRepository repository;
    private final ProductService productService;

    // Конструктор
    public CustomerService() throws IOException {
        repository = new CustomerRepository();
        productService = new ProductService();
    }

    //   Сохранить покупателя в базе данных (при сохранении покупатель автоматически считается активным).
    public Customer save(Customer customer) throws IOException, CustomerSaveException {
        if (customer == null) {
            throw new CustomerSaveException("Покупатель не может быть null");
        }

        String name = customer.getName();
        if (name == null || name.trim().isEmpty()) {
            throw new CustomerSaveException("Имя покупателя не может быть пустым");
        }

        customer.setActive(true);
        return repository.save(customer);
    }

    //   Вернуть всех покупателей из базы данных (активных).
    public List<Customer> getAllActiveCustomers() throws IOException {
        return repository.findAll()
                .stream()
                .filter(Customer::isActive)
                .toList();
    }
//    Вернуть одного покупателя из базы данных по его идентификатору (если он активен).
    public Customer getActiveCustomerById(int id) throws IOException, CustomerNotFoundException {
        Customer customer = repository.findById(id);

        if (customer == null || !customer.isActive()){
            throw new CustomerNotFoundException(id);
        }
        return customer;
    }
//    Изменить одного покупателя в базе данных по его идентификатору.
    public void update(Customer customer) throws CustomerSaveException, IOException {
        if (customer == null) {
            throw new CustomerSaveException("Покупатель не может быть null");
        }

        String name = customer.getName();
        if (name == null || name.trim().isEmpty()) {
            throw new CustomerSaveException("Имя покупателя не может быть пустым");
        }

        repository.update(customer);
    }
//    Удалить покупателя из базы данных по его идентификатору.
    public void deleteById(int id) throws IOException, CustomerNotFoundException {
        getActiveCustomerById(id).setActive(false);
    }
//    Удалить покупателя из базы данных по его имени.
//    Восстановить удалённого покупателя в базе данных по его идентификатору.
//    Вернуть общее количество покупателей в базе данных (активных).
//    Вернуть стоимость корзины покупателя по его идентификатору (если он активен). • Вернуть среднюю стоимость продукта в корзине покупателя по его идентификатору (если он активен)
//    Добавить товар в корзину покупателя по их идентификаторам (если оба активны)
//    Удалить товар из корзины покупателя по их идентификаторам
//    Полностью очистить корзину покупателя по его идентификатору (если он активен
}
