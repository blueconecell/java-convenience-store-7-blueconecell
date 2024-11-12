package store.config;

import store.controller.StoreController;
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

public class AppConfig {


    public StoreController convenienceStoreController() {
        return new StoreController(stockBuilder(), promotions(), promotionBuilder(), inputView(),
                outputView(),
                receipt(), orderService());
    }

    public Stock stock() {
        return new Stock();
    }

    public StockBuilder stockBuilder() {
        return new StockBuilder();
    }

    public PromotionList promotions() {
        return new PromotionList();
    }

    public PromotionBuilder promotionBuilder() {
        return new PromotionBuilder();
    }

    public InputView inputView() {
        return new InputView();
    }

    public OutputView outputView() {
        return new OutputView();
    }

    public Cart cart() {
        return new Cart();
    }

    public Receipt receipt() {
        return new Receipt();
    }

    public OrderService orderService() {
        return new OrderService();
    }

    public PurchaseProcessing purchaseProcessing() {
        return new PurchaseProcessing(promotions(), inputView(), receipt(), stock(), cart());
    }

    public ReceiptCalculator receiptCalculator() {
        return new ReceiptCalculator(receipt(), promotions());
    }
}
