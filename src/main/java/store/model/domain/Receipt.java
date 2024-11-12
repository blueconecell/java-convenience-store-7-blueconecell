package store.model.domain;

import java.util.ArrayList;
import java.util.List;

public class Receipt {

    private final List<Product> products;

    public Receipt() {
        this.products = new ArrayList<>();
    }

    public int getProductSizeByName(String name) {
        int size = 0;
        for (Product product : products) {
            if (product.getName().equals(name)) {
                size++;
            }
        }
        return size;
    }

    public boolean isContainProductByName(String name) {
        for (Product product : products) {
            if (product.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public Product getProductByNameAndPromotion(String name, String promotionName) {
        for (Product product : products) {
            if (product.getName().equals(name) && product.getPromotionName().equals(promotionName)) {
                return product;
            }
        }
        return null;
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