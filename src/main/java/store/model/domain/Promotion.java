package store.model.domain;

import java.time.LocalDate;

public class Promotion {

    private final String promotionName;
    private final Integer buyUnit;
    private final Integer getUnit;
    private final LocalDate promotionStartDate;
    private final LocalDate promotionEndDate;

    public Promotion(String promotionType, Integer buyUnit, Integer getUnit, LocalDate promotionStartDate,
                     LocalDate promotionEndDate) {
        this.promotionName = promotionType;
        this.buyUnit = buyUnit;
        this.getUnit = getUnit;
        this.promotionStartDate = promotionStartDate;
        this.promotionEndDate = promotionEndDate;
    }

    public String getPromotionName() {
        return promotionName;
    }

    public LocalDate getPromotionStartDate() {
        return promotionStartDate;
    }

    public LocalDate getPromotionEndDate() {
        return promotionEndDate;
    }

    public Integer getPromotionUnit() {
        return buyUnit + getUnit;
    }
}
