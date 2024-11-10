package store.model.domain;

import java.util.ArrayList;
import java.util.List;

public class PromotionList {
    private final List<Promotion> promotions;

    public PromotionList() {
        this.promotions = new ArrayList<Promotion>();
    }

    public void addPromotion(Promotion promotion) {
        promotions.add(promotion);
    }

    public Promotion findPromotionByName(String name) {
        for (Promotion promotion : promotions) {
            if (promotion.getPromotionName().equals(name)) {
                return promotion;
            }
        }
        return null;
    }
}