package aleksey.ivlev.storage;

import aleksey.ivlev.model.OrderBook;

public class OrderBookStorage {

    private OrderBook orderBook;

    public OrderBookStorage(OrderBook orderBook) {
        this.orderBook = orderBook;
    }

    public OrderBook getOrderBook() {
        return orderBook;
    }
}
