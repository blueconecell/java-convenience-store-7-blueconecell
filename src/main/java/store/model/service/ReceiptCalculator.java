package store.model.service;

import store.model.domain.Product;
import store.model.domain.Promotion;
import store.model.domain.PromotionList;
import store.model.domain.Receipt;

public class ReceiptCalculator {
    private final Receipt receipt;
    private final PromotionList promotionList;

    public ReceiptCalculator(Receipt receipt, PromotionList promotionList) {
        this.receipt = receipt;
        this.promotionList = promotionList;
    }

    public int getPromotionDiscount() {
        int promotionDiscount = 0;
        for (Product product : receipt.getProducts()) {
            if (!product.getPromotionName().equals("null") ) {
                Promotion promotion = promotionList.findPromotionByName(product.getPromotionName());
                promotionDiscount += product.getPrice() * product.getQuantity() / promotion.getPromotionUnit();
            }
        }
        return promotionDiscount;
    }
    private int getAllPromotionProductMoney() {
        int promotionDiscount = 0;
        for (Product product : receipt.getProducts()) {
            if (!product.getPromotionName().equals("null") ) {
                promotionDiscount += product.getPrice() * product.getQuantity();
            }
        }
        return promotionDiscount;
    }

    public int getMembershipDiscount(boolean doMembership) {
        if (!doMembership) {
            return 0;
        }
        int totalMoney = getTotalMoney();
        int allPromotionProductMoney = getAllPromotionProductMoney();
        int membershipDiscount = (int) ((totalMoney - allPromotionProductMoney) * 0.3);
        if (membershipDiscount > 8000) {
            membershipDiscount = 8000;
        }
        return membershipDiscount;
    }

    public int getTotalMoney() {
        int totalMoney = 0;
        for (Product product : receipt.getProducts()) {
            totalMoney += product.getPrice() * product.getQuantity();
        }
        return totalMoney;
    }
}