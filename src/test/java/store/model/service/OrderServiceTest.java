package store.model.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import store.config.AppConfig;
import store.controller.StoreController;
import store.model.domain.Cart;
import store.model.domain.Product;
import store.model.domain.PromotionList;
import store.model.domain.Stock;

import static org.assertj.core.api.Assertions.assertThat;

class OrderServiceTest {
    AppConfig appConfig = new AppConfig();
    OrderService orderService = appConfig.orderService();
    StockBuilder stockBuilder = appConfig.stockBuilder();
    PromotionBuilder promotionBuilder = appConfig.promotionBuilder();
    PromotionList promotions = appConfig.promotions();

    @Test
    public void 정상작동_테스트() throws Exception {
        //given
        Stock stock = new Stock();
        stockBuilder.build(stock);
        promotionBuilder.build(promotions);

        //when
        String requestProducts = "[콜라-3]";
        Cart cart = orderService.order(stock, requestProducts);

        //then
        Cart expectedCart = new Cart();
        expectedCart.addProduct(new Product("콜라", 1000, 3, "null"));
        assertThat(cart.size()).isEqualTo(expectedCart.size());
        assertThat(cart.getProductByName("콜라").getName()).isEqualTo(expectedCart.getProductByName("콜라").getName());
        assertThat(cart.getProductByName("콜라").getPromotionName()).isEqualTo(
                expectedCart.getProductByName("콜라").getPromotionName());
        assertThat(cart.getProductByName("콜라").getQuantity()).isEqualTo(
                expectedCart.getProductByName("콜라").getQuantity());
        assertThat(cart.getProductByName("콜라").getPrice()).isEqualTo(expectedCart.getProductByName("콜라").getPrice());
    }

    @Test
    public void 재고보다_더_많은_양의_상품_주문() throws Exception {
        //given
        Stock stock = new Stock();
        stockBuilder.build(stock);
        promotionBuilder.build(promotions);

        //when
        String requestProducts = "[콜라-23]";

        //then
        assertThrows(IllegalArgumentException.class, () -> orderService.order(stock, requestProducts));
    }

    @Test
    public void 존재하지_않는_상품_주문() throws Exception {
        //given
        Stock stock = new Stock();
        stockBuilder.build(stock);
        promotionBuilder.build(promotions);

        //when
        String requestProducts = "[펩시-3]";

        //then
        assertThrows(IllegalArgumentException.class, () -> orderService.order(stock, requestProducts));
    }
}