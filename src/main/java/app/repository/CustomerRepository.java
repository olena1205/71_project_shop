package app.repository;

import app.domain.Customer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomerRepository {
    // Файл, который является базой данных
    private final File database;

    // Маппер для чтения и записи объектов в файл
    private final ObjectMapper mapper;

    // Поле, которое хранит максимальный идентификатор, сохраненный в БД
    private int maxId;


    public CustomerRepository() throws IOException {
        database = new File("database/customer.txt");
        mapper = new ObjectMapper();

        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        // Выясняем, какой идентификатор БД на данный момент максимальный
        List<Customer> customers = findAll();

        if (!customers.isEmpty()) {
            Customer lastCustomer = customers.get(customers.size() - 1);
            maxId = lastCustomer.getId();

        }
    }


    public Customer save(Customer customer) throws IOException {
        customer.setId(++maxId);
        List<Customer> customers = findAll();
        customers.add(customer);
        mapper.writeValue(database, customers);
        return customer;
    }


    public List<Customer> findAll() throws IOException {
        try {
            Customer[] customers = mapper.readValue(database, Customer[].class);
            return new ArrayList<>(Arrays.asList(customers));
        } catch (MismatchedInputException e) {
            return new ArrayList<>();
        }
    }

    public Customer findById(int id) throws IOException {
        return findAll().
                stream()
                .filter(x -> x.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public void update(Customer customer) throws IOException {
        int id = customer.getId();
        String newName = customer.getName();

        List<Customer> customers = findAll();
        customers
                .stream()
                .filter(x -> x.getId() == id)
                .forEach(x -> x.setName(newName));

        mapper.writeValue(database, customers);
    }

    public void deleteById(int id) throws IOException {
        List<Customer> customers = findAll();
        customers.removeIf(x -> x.getId() == id);
        mapper.writeValue(database, customers);
    }
}
