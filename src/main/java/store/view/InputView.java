package store.view;

import camp.nextstep.edu.missionutils.Console;

public class InputView implements InputCallback {
    private static final String ERROR_HEADER = "[ERROR] ";
    private static final String PURCHASE_PRODUCTS = "구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])";
    private static final String PROMOTION_ADD_PRODUCT_TEMPLATE = "현재 %s은(는) %d개를 추가하면 무료로 1개를 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)";
    private static final String PROMOTION_NOT_APPLY_TEMPLATE = "%s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)";
    private static final String MEMBERSHIP = "멤버십 할인을 받으시겠습니까? (Y/N)";
    private static final String CONTINUE_PURCHASE = "감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)";
    private static final String YES_OR_NO = "Y 또는 N만 입력해 주세요.";

    public String requestPurchaseProducts() {
        System.out.println(PURCHASE_PRODUCTS);
        return Console.readLine();
    }

    public boolean requestPromotionAddProduct(String productName, int quantity) {
        String message = String.format(PROMOTION_ADD_PRODUCT_TEMPLATE, productName, quantity);
        return getYesOrNoResponse(message);
    }

    public boolean requestPromotionNotApply(String productName, int quantity) {
        String message = String.format(PROMOTION_NOT_APPLY_TEMPLATE, productName, quantity);
        return getYesOrNoResponse(message);
    }

    @Override
    public boolean requestMembership() {
        return getYesOrNoResponse(MEMBERSHIP);
    }

    public String requestContinuePurchase() {
        System.out.println(CONTINUE_PURCHASE);
        return Console.readLine();
    }

    private boolean getYesOrNoResponse(String message) {
        while (true) {
            try {
                System.out.println(message);
                String input = Console.readLine();
                isValidYesOrNo(input);
                return isTrueOfFalse(input);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void isValidYesOrNo(String input) {
        if (!input.equalsIgnoreCase("Y") && !input.equalsIgnoreCase("N")) {
            throw new IllegalArgumentException(ERROR_HEADER + YES_OR_NO);
        }
    }

    private boolean isTrueOfFalse(String input) {
        return input.equalsIgnoreCase("Y");
    }
}