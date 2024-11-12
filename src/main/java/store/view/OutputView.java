package store.view;

import java.text.NumberFormat;
import store.dto.ReceiptDto;
import store.model.domain.Product;
import store.model.domain.Promotion;
import store.model.domain.PromotionList;
import store.model.domain.Receipt;
import store.model.domain.Stock;

public class OutputView {
    private static final String HELLO_MESSAGE = "안녕하세요. W편의점입니다.\n현재 보유하고 있는 상품입니다.\n";
    private static final String STOCK_DISPLAY_TEMPLATE = "- %s %s원 %s %s";
    private static final String RECEIPT_HEADER = "==============W 편의점================";
    private static final String RECEIPT_PROMOTION = "=============증      정===============";
    private static final String RECEIPT_SHOW_MONEY = "=====================================";

    public void responseAllStock(Stock stock) {
        NumberFormat numberFormat = NumberFormat.getInstance();
        System.out.println(HELLO_MESSAGE);
        printAllProductsInStock(stock, numberFormat);
    }

    public void responseReceipt(ReceiptDto receiptDto, PromotionList promotionList) {
        NumberFormat numberFormat = NumberFormat.getInstance();

        int totalQuantity = printHeaderAndAllProducts(receiptDto, numberFormat);
        printPromotion(receiptDto, promotionList);
        printCalculatedMoney(receiptDto, numberFormat, totalQuantity);
    }

    public void responseError(String errorMessage) {
        System.out.println(errorMessage);
    }

    private static void printAllProductsInStock(Stock stock, NumberFormat numberFormat) {
        for (Product product : stock.getAllProducts()) {
            String name = product.getName();
            String price = numberFormat.format(product.getPrice());
            String promotionName = "";
            String quantity = "재고 없음";
            promotionName = updatePromotionName(product, promotionName);
            quantity = updateQuantity(product, quantity, numberFormat);
            String message = String.format(STOCK_DISPLAY_TEMPLATE, name, price, quantity, promotionName);
            System.out.println(message);
        }
    }

    private static String updateQuantity(Product product, String quantity, NumberFormat numberFormat) {
        if (product.getQuantity() != 0) {
            quantity = numberFormat.format(product.getQuantity()) + "개";
        }
        return quantity;
    }

    private static String updatePromotionName(Product product, String promotionName) {
        if (!product.getPromotionName().equals("null")) {
            promotionName = product.getPromotionName();
        }
        return promotionName;
    }

    private void printCalculatedMoney(ReceiptDto receiptDto, NumberFormat numberFormat, int totalQuantity) {
        System.out.println(RECEIPT_SHOW_MONEY);
        String totalMoney = numberFormat.format(receiptDto.getTotalMoney());
        String promotionDiscount = numberFormat.format(receiptDto.getPromotionDiscount());
        String membershipDiscount = numberFormat.format(receiptDto.getMembershipDiscount());
        int purchaseMoney =
                receiptDto.getTotalMoney() - receiptDto.getPromotionDiscount() - receiptDto.getMembershipDiscount();
        String purchaseMoneyText = numberFormat.format(purchaseMoney);
        System.out.print(convertLeft("총구매액", 20));
        System.out.print(convertLeft(Integer.toString(totalQuantity), 10));
        System.out.println(convertLeft(totalMoney, 7));
        System.out.print(convertLeft("행사할인", 30));
        System.out.println(convertLeft("-" + promotionDiscount, 7));
        System.out.print(convertLeft("멤버십할인", 30));
        System.out.println(convertLeft("-" + membershipDiscount, 7));
        System.out.print(convertLeft("내실돈", 30));
        System.out.println(convertLeft(purchaseMoneyText, 7));
    }

    private void printPromotion(ReceiptDto receiptDto, PromotionList promotions) {
        System.out.println(RECEIPT_PROMOTION);
        for (Product product : receiptDto.getReceipt().getProducts()) {
            if (!product.getPromotionName().equals("null")) {
                Promotion promotion = promotions.findPromotionByName(product.getPromotionName());
                System.out.print(convertLeft(product.getName(), 20));
                System.out.println(
                        convertLeft(Integer.toString(product.getQuantity() / promotion.getPromotionUnit()), 2));
            }
        }
    }

    private int printHeaderAndAllProducts(ReceiptDto receiptDto, NumberFormat numberFormat) {
        System.out.println(RECEIPT_HEADER);
        printProducts("상품명", "수량", "금액");
        int totalQuantity = 0;

        Receipt receiptWithAllDefaultProducts = getReceiptWithAllDefaultProducts(receiptDto);

        for (Product product : receiptWithAllDefaultProducts.getProducts()) {
            int quantity = product.getQuantity();
            totalQuantity += quantity;
            String price = numberFormat.format(product.getPrice() * quantity);
            printProducts(product.getName(), Integer.toString(quantity), price);
        }
        return totalQuantity;
    }

    private static Receipt getReceiptWithAllDefaultProducts(ReceiptDto receiptDto) {
        Receipt receiptWithAllDefaultProducts = new Receipt();
        for (Product product : receiptDto.getReceipt().getProducts()) {
            if (receiptWithAllDefaultProducts.isContainProductByName(product.getName())) {
                Product defaultProduct = receiptWithAllDefaultProducts.getProductByNameAndPromotion(product.getName(),
                        "null");
                defaultProduct.setQuantity(defaultProduct.getQuantity() + product.getQuantity());
            } else {
                receiptWithAllDefaultProducts.addProduct(
                        new Product(product.getName(), product.getPrice(), product.getQuantity(), "null"));
            }
        }
        return receiptWithAllDefaultProducts;
    }

    private void printProducts(String name, String quantity, String price) {
        System.out.print(convertLeft(name, 20));
        System.out.print(convertLeft(quantity, 10));
        System.out.println(convertLeft(price, 7));
    }

    private int getKorCnt(String kor) {
        int cnt = 0;
        for (int i = 0; i < kor.length(); i++) {
            if (kor.charAt(i) >= '가' && kor.charAt(i) <= '힣') {
                cnt++;
            }
        }
        return cnt;
    }

    private String convertRight(String text, int size) {
        String formatter = String.format("%%%ds", size - Math.round(0.75 * getKorCnt(text)));
        return String.format(formatter, text);
    }

    private String convertLeft(String text, int size) {
        String formatter = String.format("%%-%ds", size - Math.round(0.75 * getKorCnt(text)));
        return String.format(formatter, text);
    }

}
