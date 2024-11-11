package store.constant;

public enum Resources {
    PRODUCTS_URL("src/main/resources/products.md"),
    PROMOTIONS_URL("src/main/resources/promotions.md");

    private final String url;

    Resources(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
