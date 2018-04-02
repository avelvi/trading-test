package aleksey.ivlev.model;

import java.util.*;

public class OrderBook {

    private NavigableMap<OrderKey, LinkedList<Order>> buyData;
    private NavigableMap<OrderKey, LinkedList<Order>> sellData;
    private Map<Integer, Order> orders;

    private static OrderBook instance;

    private OrderBook() {
        this.buyData = new TreeMap<>(Comparator.comparing(OrderKey::getPrice));
        this.sellData = new TreeMap<>(Comparator.comparing(OrderKey::getPrice));
        this.orders = new HashMap<>();

    }

    public static OrderBook getInstance(){
        if(instance == null){
            instance = new OrderBook();
        }
        return instance;
    }

    public NavigableMap<OrderKey, LinkedList<Order>> getBuyData() {
        return buyData;
    }

    public NavigableMap<OrderKey, LinkedList<Order>> getSellData() {
        return sellData;
    }

    public Map<Integer, Order> getOrders() {
        return orders;
    }
}
