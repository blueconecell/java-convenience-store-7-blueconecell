package store.model.domain;

import java.util.ArrayList;
import java.util.List;

public class Stock {
    private final List<Product> products;

    public Stock() {
        this.products = new ArrayList<>();
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public List<Product> getAllProducts() {
        return products;
    }

    public Integer findPriceByName(String name) {
        for (Product product : products) {
            if (product.getName().equals(name)) {
                return product.getPrice();
            }
        }
        return null;
    }

    public List<Product> findAllProductsByName(String name) {
        List<Product> productsByName = new ArrayList<>();
        for (Product product : products) {
            if (product.getName().equals(name)) {
                productsByName.add(product);
            }
        }
        return productsByName;
    }

    public Integer findAllQuantityByName(String name) {
        int totalQuantity = 0;
        for (Product product : products) {
            if (product.getName().equals(name)) {
                totalQuantity += product.getQuantity();
            }
        }
        return totalQuantity;
    }

    public Product findPromotionProductByName(String name) {
        for (Product product : products) {
            if (product.getName().equals(name) && !product.getPromotionName().equals("null")) {
                return product;
            }
        }
        return null;
    }

    public Product findDefaultProductByName(String name) {
        for (Product product : products) {
            if (product.getName().equals(name) && product.getPromotionName().equals("null")) {
                return product;
            }
        }
        return null;
    }


    public void purchase(String productName, int quantity) {
        Product promotionProduct = findPromotionProductByName(productName);
        Product defaultProduct = findDefaultProductByName(productName);
        int remainingQuantity = quantity;
        remainingQuantity = sellPromotionProductFirst(quantity, promotionProduct, remainingQuantity);
        sellDefaultProductLast(remainingQuantity, defaultProduct);
    }

    private static void sellDefaultProductLast(int remainingQuantity, Product defaultProduct) {
        if (remainingQuantity > 0) {
            defaultProduct.setQuantity(defaultProduct.getQuantity() - remainingQuantity);
        }
    }

    private static int sellPromotionProductFirst(int quantity, Product promotionProduct, int remainingQuantity) {
        if (promotionProduct != null) {
            int promoQuantityToUse = Math.min(promotionProduct.getQuantity(), quantity);
            promotionProduct.setQuantity(promotionProduct.getQuantity() - promoQuantityToUse);
            remainingQuantity = quantity - promoQuantityToUse;
        }
        return remainingQuantity;
    }
}