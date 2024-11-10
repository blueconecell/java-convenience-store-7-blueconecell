package store.dto;

import store.model.domain.Receipt;

public class ReceiptDto {
    private final Receipt receipt;
    private final int promotionDiscount;
    private final int membershipDiscount;
    private final int totalMoney;

    public ReceiptDto(Receipt receipt, int promotionDiscount, int membershipDiscount, int totalMoney) {
        this.receipt = receipt;
        this.promotionDiscount = promotionDiscount;
        this.membershipDiscount = membershipDiscount;
        this.totalMoney = totalMoney;
    }

    public Receipt getReceipt() {
        return receipt;
    }

    public int getPromotionDiscount() {
        return promotionDiscount;
    }

    public int getMembershipDiscount() {
        return membershipDiscount;
    }

    public int getTotalMoney() {
        return totalMoney;
    }
}
