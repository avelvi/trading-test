package aleksey.ivlev.service;

import aleksey.ivlev.model.Order;
import aleksey.ivlev.model.OrderType;

public interface CommandExecutorService {

    void placeOrder(Order originalOrder);

    String executeQuery(OrderType orderType);

    Integer executeTotalQuery(Integer price);

    void cancelOrder(Integer id);


}
