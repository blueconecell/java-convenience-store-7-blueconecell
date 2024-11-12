package store.view;

public interface InputCallback {
    boolean requestPromotionAddProduct(String productName, int quantity);

    boolean requestPromotionNotApply(String productName, int quantity);

    boolean requestMembership();
}
