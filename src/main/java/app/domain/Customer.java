package app.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Customer {
    private int id;
    private String name;
    private boolean active;
    private final List<Product> products = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<Product> getProducts() {
        return products;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Customer customer = (Customer) o;
        return id == customer.id && active == customer.active && Objects.equals(name, customer.name) && products.equals(customer.products);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + Objects.hashCode(name);
        result = 31 * result + Boolean.hashCode(active);
        result = 31 * result + products.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return String.format("Покупатель: id - %d, имя - %s, активен - %b, лист покупок - %s.",
                id,name,active,products);
    }
}
