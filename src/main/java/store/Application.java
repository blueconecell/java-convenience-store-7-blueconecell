package store;

import store.config.AppConfig;
import store.controller.StoreController;

public class Application {
    public static void main(String[] args) {

        AppConfig appConfig = new AppConfig();
        StoreController convenienceStoreController = appConfig.convenienceStoreController();
        
        convenienceStoreController.start();
    }
}
