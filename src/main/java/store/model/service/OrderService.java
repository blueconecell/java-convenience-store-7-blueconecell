package store.model.service;

import java.util.ArrayList;
import java.util.List;
import store.model.domain.Cart;
import store.model.domain.Product;
import store.model.domain.Stock;

public class OrderService {
    private final Cart cart;
    private String name;
    private int quantity;

    public OrderService() {
        this.cart = new Cart();
    }

    public Cart order(Stock stock, String requestProducts) {
        List<String> products = new ArrayList<>(List.of(requestProducts.trim().split(",")));

        for (String product : products) {
            orderParser(product);
            productExistValidator(stock);
            enoughQuantityValidator(stock);
            quantityZeroValidator();
            addCart(stock);
        }
        return cart;
    }

    private void addCart(Stock stock) {
        Product product = cart.getProductByName(name);

        if (product != null) {
            int beforeQuantity = cart.getProductByName(name).getQuantity();
            cart.getProductByName(name).setQuantity(beforeQuantity + quantity);
        }
        if (product == null) {
            cart.addProduct(new Product(name, stock.findPriceByName(name), quantity, "null"));
        }

    }

    private void orderParser(String productText) {
        try {
            productText = productText.replace("[", "").replace("]", "");
            name = productText.split("-")[0].trim();
            quantity = Integer.parseInt(productText.split("-")[1].trim());
        } catch (Exception e) {
            throw new IllegalArgumentException("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
        }
    }

    private void quantityZeroValidator() {
        if (quantity <= 0) {
            throw new IllegalArgumentException("[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.");
        }
    }

    private void productExistValidator(Stock stock) {
        if (stock.findAllProductsByName(name).isEmpty()) {
            throw new IllegalArgumentException("[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.");
        }
    }

    private void enoughQuantityValidator(Stock stock) {
        if (stock.findAllQuantityByName(name) < quantity) {
            throw new IllegalArgumentException("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
        }

    }
}
