package store.model.service;

import java.util.List;
import store.constant.Resources;
import store.model.domain.Product;
import store.model.domain.Stock;
import store.util.MarkdownReader;

public class StockBuilder {

    public StockBuilder() {
    }

    public void build(Stock stock) {
        String mdProduct = MarkdownReader.mdReader(Resources.PRODUCTS_URL.getUrl());
        List<String> splitMdProducts = List.of(mdProduct.trim().split("[,\\n]"));
        addAllProductsToStock(splitMdProducts, stock);
    }

    private void addAllProductsToStock(List<String> splitMdProducts, Stock stock) {
        String beforePromotion = splitMdProducts.get(4 + 3).trim();
        String beforeName = splitMdProducts.get(4).trim();
        for (int i = 1; i < splitMdProducts.size() / 4; i++) {
            addMissingDefaultProduct(splitMdProducts.get(i * 4).trim(), beforeName, beforePromotion, splitMdProducts,
                    i, stock);
            addProductToStock(splitMdProducts, i, stock);
            beforePromotion = splitMdProducts.get(i * 4 + 3).trim();
            beforeName = splitMdProducts.get(i * 4).trim();
        }
    }

    private void addMissingDefaultProduct(String name, String beforeName, String beforePromotion,
                                          List<String> splitMdProducts, int i, Stock stock) {
        if (!name.equals(beforeName) && !beforePromotion.equals("null")) {
            stock.addProduct(new Product(
                    splitMdProducts.get((i - 1) * 4).trim(),
                    Integer.parseInt(splitMdProducts.get((i - 1) * 4 + 1).trim()),
                    0, "null")
            );
        }
    }

    private void addProductToStock(List<String> splitMdProducts, int i, Stock stock) {
        stock.addProduct(new Product(
                splitMdProducts.get(i * 4).trim(),
                Integer.parseInt(splitMdProducts.get(i * 4 + 1).trim()),
                Integer.parseInt(splitMdProducts.get(i * 4 + 2).trim()),
                splitMdProducts.get(i * 4 + 3).trim())
        );
    }
}
