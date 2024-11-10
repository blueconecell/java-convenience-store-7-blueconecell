package store.model.service;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.chrono.ChronoLocalDate;
import store.model.domain.Cart;
import store.model.domain.Product;
import store.model.domain.Promotion;
import store.model.domain.PromotionList;
import store.model.domain.Receipt;
import store.model.domain.Stock;
import store.view.InputCallback;

public class PurchaseProcessing {
    private final PromotionList promotionList;
    private final InputCallback inputCallback;
    private final Receipt receipt;
    private final Stock stock;
    private final Cart cart;

    public PurchaseProcessing(PromotionList promotionList, InputCallback inputCallback, Receipt receipt, Stock stock,
                              Cart cart) {
        this.promotionList = promotionList;
        this.inputCallback = inputCallback;
        this.receipt = receipt;
        this.stock = stock;
        this.cart = cart;
    }

    public void applyCart() {
        for (Product product : cart.getProducts()) {
            if (isApplicableTo(product.getName())) {
                apply(product);
            } else {
                receipt.addProduct(product);
                stock.purchase(product.getName(), product.getQuantity());
            }
        }
    }

    private void apply(Product product) {
        String productName = product.getName();
        int cartProductQuantity = product.getQuantity();
        int stockPromotionProductQuantity = stock.findPromotionProductByName(productName).getQuantity();
        Promotion promotion = promotionList.findPromotionByName(
                stock.findPromotionProductByName(productName).getPromotionName());
        int promotionUnit = promotion.getPromotionUnit(); // 프로모션 단위 (2+1 상품 -> 3, 1+1 상품 -> 2)
        String promotionName = promotion.getPromotionName();
        int remaining = cartProductQuantity % promotionUnit; // 나머지 (구매량을 프로모션 단위로 나눈 나머지)
        int cleanCartProductQuantity = cartProductQuantity + promotionUnit - remaining; // 구매량 + 나머지
        int purchasePromotionBundle = 0; // 프로모션 단위로 구매한 묶음
        int priceOfProduct = product.getPrice();

        while (cleanCartProductQuantity > 0 && stockPromotionProductQuantity > 0) {
            cleanCartProductQuantity -= promotionUnit;
            stockPromotionProductQuantity -= promotionUnit;
            purchasePromotionBundle++;
        }

        if (stockPromotionProductQuantity > 0 && remaining > 0) { // 상품 추가 구매 제안 상황
            boolean progress = inputCallback.requestPromotionAddProduct(productName, promotionUnit - remaining);
            addPromotionProductApply(progress, purchasePromotionBundle, promotionUnit, productName, priceOfProduct,
                    promotionName,
                    cartProductQuantity);
        } else if (stockPromotionProductQuantity > 0 && remaining == 0) { // 프로모션 즉시 적용 상황
            receipt.addProduct(new Product(productName, priceOfProduct, cartProductQuantity, promotionName));
            stock.purchase(product.getName(), product.getQuantity());
        } else if (stockPromotionProductQuantity < 0) { // 프로모션 미적용 상품 갯수 안내 상황
            int nonPromotionProductQuantity = cartProductQuantity - (purchasePromotionBundle - 1) * promotionUnit;
            boolean progress = inputCallback.requestPromotionNotApply(productName, nonPromotionProductQuantity);
            promotionNotApply(progress, purchasePromotionBundle, promotionUnit, cartProductQuantity, productName,
                    priceOfProduct,
                    promotionName);
        }

    }

    private void promotionNotApply(boolean progress, int purchasePromotionBundle, int promotionUnit,
                                   int cartProductQuantity,
                                   String productName, int priceOfProduct, String promotionName) {
        if (progress) {
            int purchasedPromotionProductQuantity = (purchasePromotionBundle - 1) * promotionUnit;
            int purchasedDefaultProductQuantity = cartProductQuantity - purchasedPromotionProductQuantity;
            receipt.addProduct(new Product(productName, priceOfProduct, purchasedPromotionProductQuantity,
                    promotionName));
            receipt.addProduct(new Product(productName, priceOfProduct, purchasedDefaultProductQuantity, "null"));

            stock.purchase(productName, cartProductQuantity);

        } else {
            int purchasedPromotionProductQuantity = (purchasePromotionBundle - 1) * promotionUnit;
            receipt.addProduct(new Product(productName, priceOfProduct, purchasedPromotionProductQuantity,
                    promotionName));
            stock.purchase(productName, purchasedPromotionProductQuantity);
        }
    }

    private void addPromotionProductApply(boolean progress, int purchasePromotionBundle, int promotionUnit,
                                          String productName,
                                          int priceOfProduct, String promotionName, int cartProductQuantity) {
        if (progress) {
            int purchasedPromotionProductQuantity = purchasePromotionBundle * promotionUnit;
            receipt.addProduct(
                    new Product(productName, priceOfProduct, purchasedPromotionProductQuantity, promotionName));
            stock.purchase(productName, purchasedPromotionProductQuantity);
        } else {
            int purchasedPromotionProductQuantity = (purchasePromotionBundle - 1) * promotionUnit;
            int purchasedDefaultProductQuantity = cartProductQuantity - purchasedPromotionProductQuantity;
            receipt.addProduct(
                    new Product(productName, priceOfProduct, purchasedPromotionProductQuantity, promotionName));
            receipt.addProduct(new Product(productName, priceOfProduct, purchasedDefaultProductQuantity, "null"));
            stock.purchase(productName, cartProductQuantity);
        }
    }


    private boolean isApplicableTo(String cartProductName) {
        Promotion promotion = findPromotionByNameFromStock(cartProductName);
        if (isPromotionExist(cartProductName) && isEnoughPromotionPeriod(promotion)) {
            return true;
        }
        return false;
    }

    private boolean isPromotionExist(String productName) {
        return stock.findPromotionProductByName(productName) != null;
    }

    private boolean isEnoughPromotionPeriod(Promotion promotion) {
        return promotion.getPromotionStartDate().isBefore(ChronoLocalDate.from(DateTimes.now()))
                && promotion.getPromotionEndDate().isAfter(ChronoLocalDate.from(DateTimes.now()));
    }

    private Promotion findPromotionByNameFromStock(String productName) {
        for (Product product : stock.findAllProductsByName(productName)) {
            if (product.getPromotionName() != null) {
                return promotionList.findPromotionByName(product.getPromotionName());
            }
        }
        return null;
    }

}
