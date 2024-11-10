package store.model.domain;

import java.util.ArrayList;
import java.util.List;

public class Receipt {

    private final List<Product> products;

    public Receipt() {
        this.products = new ArrayList<>();

    }

    public List<Product> getProducts() {
        return products;
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public void clearReceipt() {
        products.clear();
    }

}