package store.model.service;

import java.time.LocalDate;
import java.util.List;
import store.constant.Resources;
import store.model.domain.Promotion;
import store.model.domain.PromotionList;
import store.util.MarkdownReader;

public class PromotionBuilder {

    public void build(PromotionList promotions) {
        String mdPromotion = MarkdownReader.mdReader(Resources.PROMOTIONS_URL.getUrl());
        List<String> splitPromotions = List.of(mdPromotion.split("[,\\n]"));
        for (int i = 1; i < splitPromotions.size() / 5; i++) {
            addPromotionToPromotions(promotions, splitPromotions, i);
        }
    }

    private static void addPromotionToPromotions(PromotionList promotions, List<String> splitPromotions, int i) {
        promotions.addPromotion(new Promotion(
                splitPromotions.get(i * 5).trim(),
                Integer.parseInt(splitPromotions.get(i * 5 + 1).trim()),
                Integer.parseInt(splitPromotions.get(i * 5 + 2).trim()),
                LocalDate.parse(splitPromotions.get(i * 5 + 3).trim()),
                LocalDate.parse(splitPromotions.get(i * 5 + 4).trim()))
        );
    }
}