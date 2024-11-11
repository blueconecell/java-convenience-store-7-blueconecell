package store.controller;

import store.dto.ReceiptDto;
import store.model.domain.Cart;
import store.model.domain.PromotionList;
import store.model.domain.Receipt;
import store.model.domain.Stock;
import store.model.service.OrderService;
import store.model.service.PromotionBuilder;
import store.model.service.PurchaseProcessing;
import store.model.service.ReceiptCalculator;
import store.model.service.StockBuilder;
import store.view.InputView;
import store.view.OutputView;

public class StoreController {
    private final StockBuilder stockBuilder;
    private final PromotionList promotions;
    private final PromotionBuilder promotionBuilder;
    private final InputView inputView;
    private final OutputView outputView;
    private final Receipt receipt;
    private final OrderService orderService;

    public StoreController(StockBuilder stockBuilder, PromotionList promotions,
                           PromotionBuilder promotionBuilder, InputView inputView, OutputView outputView,
                           Receipt receipt, OrderService orderService) {
        this.stockBuilder = stockBuilder;
        this.promotions = promotions;
        this.promotionBuilder = promotionBuilder;
        this.inputView = inputView;
        this.outputView = outputView;
        this.receipt = receipt;
        this.orderService = orderService;
    }

    public void start() {
        Stock stock = setStore();
        storeLoop(stock);
    }

    private Stock setStore() {
        Stock stock = new Stock();
        stockBuilder.build(stock);
        promotionBuilder.build(promotions);
        return stock;
    }

    private void storeLoop(Stock stock) {
        while (true) {
            outputView.responseAllStock(stock);
            Cart cart = getValidCart(stock);

            PurchaseProcessing purchaseProcessing = new PurchaseProcessing(promotions, inputView, receipt, stock, cart);
            purchaseProcessing.applyCart();

            boolean doMembership = inputView.requestMembership();
            setReceipt(doMembership);
            clearCartAndReceipt(cart);
            String requestContinuePurchase = inputView.requestContinuePurchase();
            if (requestContinuePurchase.equalsIgnoreCase("N")) {
                break;
            }
        }
    }

    private void clearCartAndReceipt(Cart cart) {
        cart.clearCart();
        receipt.clearReceipt();
    }

    private void setReceipt(boolean doMembership) {
        ReceiptCalculator receiptCalculator = new ReceiptCalculator(receipt, promotions);
        ReceiptDto receiptDto = new ReceiptDto(receipt, receiptCalculator.getPromotionDiscount(),
                receiptCalculator.getMembershipDiscount(doMembership), receiptCalculator.getTotalMoney());

        outputView.responseReceipt(receiptDto, promotions);
    }

    private Cart getValidCart(Stock stock) {
        while (true) {
            try {
                String requestProducts = inputView.requestPurchaseProducts();
                return orderService.order(stock, requestProducts);
            } catch (IllegalArgumentException e) {
                outputView.responseError(e.getMessage());
            }
        }

    }
}
