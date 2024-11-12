package store.model.domain;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private final List<Product> products;

    public Cart() {
        this.products = new ArrayList<>();
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public int size() {
        return products.size();
    }

    public Product getProductByName(String name) {
        for (Product product : products) {
            if (product.getName().equals(name)) {
                return product;
            }
        }
        return null;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void clearCart() {
        products.clear();
    }
}