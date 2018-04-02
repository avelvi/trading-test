package aleksey.ivlev.service.impl;

import aleksey.ivlev.model.Order;
import aleksey.ivlev.model.OrderKey;
import aleksey.ivlev.model.OrderType;
import aleksey.ivlev.service.CommandExecutorService;
import aleksey.ivlev.storage.OrderBookStorage;

import java.util.*;

import static java.util.Objects.nonNull;

public class CommandExecutorServiceImpl implements CommandExecutorService {

    private OrderBookStorage orderBookStorage;

    public CommandExecutorServiceImpl(OrderBookStorage orderBookStorage) {
        this.orderBookStorage = orderBookStorage;
    }

    @Override
    public void placeOrder(Order originalOrder) {
        Optional<Order> remaining = placeInnerOrder(originalOrder);
//        System.out.println(orderBookStorage.getOrderBook().getBuyData());
//        System.out.println(orderBookStorage.getOrderBook().getSellData());
        remaining.ifPresent(this::placeRemaining);
    }

    private Optional<Order> placeInnerOrder(Order order) {
        if (!validateIfOrderExists(order)) {
            System.exit(1);
        }
        Map<Integer, Order> orders = orderBookStorage.getOrderBook().getOrders();
        orders.put(order.getId(), order);

        boolean searchInBuy = !order.getType().equals(OrderType.B);
        Integer orderSize = order.getSize();
        OrderKey orderKey = new OrderKey(order.getPrice(), searchInBuy ? OrderType.B.name() : OrderType.S.name());
        NavigableMap<OrderKey, LinkedList<Order>> data = getDataStorageByFlag(searchInBuy);

        while (true) {
            Optional<Map.Entry<OrderKey, LinkedList<Order>>> result;
            if (searchInBuy) {
                result = Optional.ofNullable(data.ceilingEntry(orderKey));
            } else {
                result = Optional.ofNullable(data.floorEntry(orderKey));
            }

            if (result.isPresent()) {
                Map.Entry<OrderKey, LinkedList<Order>> entry = result.get();
                ListIterator<Order> it = entry.getValue().listIterator();
                while (it.hasNext()) {
                    Order ord = it.next();
                    if (ord.getSize() <= orderSize) {
                        orders.remove(ord.getId());
                        it.remove();
                        orderSize -= ord.getSize();
                    } else {
                        ord.setSize(ord.getSize() - orderSize);
                        it.set(ord);
                        orderSize = 0;
                    }

                    if (orderSize == 0) {
                        break;
                    }
                }
                if (entry.getValue().isEmpty()) {
                    data.remove(entry.getKey());
                    if (orderSize == 0) {
                        break;
                    }
                }
            } else {
                break;
            }

            if (orderSize == 0) {
                break;
            }

        }

        if (orderSize > 0) {
            order.setSize(orderSize);
            return Optional.of(order);
        }

        return Optional.empty();
    }

    private boolean validateIfOrderExists(Order order) {
        if (orderBookStorage.getOrderBook().getOrders().containsKey(order.getId())) {
            System.out.println(String.format("Order with id = %d already exists.", order.getId()));
            return false;
        }
        return true;
    }

    private void placeRemaining(Order order) {
        OrderKey orderKey = new OrderKey(order.getPrice(), order.getType().name());
        NavigableMap<OrderKey, LinkedList<Order>> data = getDataStorageByOrderType(order.getType());

        if (data.containsKey(orderKey)) {
            List<Order> orders = data.get(orderKey);
            orders.add(order);
            orders.sort(Comparator.comparing(Order::getSize));
        } else {
            LinkedList<Order> orders = new LinkedList<>();
            orders.add(order);
            data.put(orderKey, orders);
        }

    }

    @Override
    public String executeQuery(OrderType orderType) {
        Map.Entry<OrderKey, LinkedList<Order>> entry;
        NavigableMap<OrderKey, LinkedList<Order>> data = getDataStorageByOrderType(orderType);
        if (orderType.equals(OrderType.B)) {
            entry = data.lastEntry();
        } else {
            entry = data.firstEntry();
        }

        if (nonNull(entry)) {
            StringBuffer sb = new StringBuffer();
            sb.append(entry.getKey().getPrice());
            sb.append(",");
            sb.append(entry.getValue().stream().map(Order::getSize).mapToInt(Integer::intValue).sum());
            return sb.toString();
        }

        return "";
    }

    @Override
    public Integer executeTotalQuery(Integer price) {
        OrderKey orderKey = new OrderKey(price, OrderType.B.name());
        Optional<LinkedList<Order>> buySize = Optional.ofNullable(orderBookStorage.getOrderBook().getBuyData().get(orderKey));

        orderKey.setType(OrderType.S.name());
        Optional<LinkedList<Order>> sellSize = Optional.ofNullable(orderBookStorage.getOrderBook().getSellData().get(orderKey));

        final Integer[] res = {0};

        buySize.ifPresent(bsize -> res[0] += bsize.stream().map(Order::getSize).mapToInt(Integer::intValue).sum());
        sellSize.ifPresent(ssize -> res[0] += ssize.stream().map(Order::getSize).mapToInt(Integer::intValue).sum());

        return res[0];
    }

    @Override
    public void cancelOrder(Integer id) {
        Map<Integer, Order> orders = orderBookStorage.getOrderBook().getOrders();
        Optional<Order> orderOptional = Optional.ofNullable(orders.get(id));
        orderOptional.ifPresent(order -> {
            OrderKey orderKey = new OrderKey(order.getPrice(), order.getType().name());
            NavigableMap<OrderKey, LinkedList<Order>> data = getDataStorageByOrderType(order.getType());
            cancelInnerOrder(id, order, orderKey, data);
        });

    }

    private void cancelInnerOrder(Integer id, Order order, OrderKey orderKey, NavigableMap<OrderKey, LinkedList<Order>> data) {
        Optional<LinkedList<Order>> ordersOpt = Optional.ofNullable(data.get(orderKey));
        ordersOpt.ifPresent(ord -> {
            Optional<Order> orderFromSearch = ord.stream()
                    .filter(o -> o.getId().equals(id))
//                    .filter(o -> o.getId().equals(id) && o.getSize().equals(order.getSize()))
                    .findFirst();

            orderFromSearch.ifPresent(order1 -> {
                orderBookStorage.getOrderBook().getOrders().remove(id);
                ord.removeIf(order2 -> order2.equals(order));
                if (ord.isEmpty()) {
                    data.remove(orderKey);
                }
            });
        });
    }

    private NavigableMap<OrderKey, LinkedList<Order>> getDataStorageByOrderType(OrderType orderType) {
        return orderType.equals(OrderType.B) ? orderBookStorage.getOrderBook().getBuyData() : orderBookStorage.getOrderBook().getSellData();
    }

    private NavigableMap<OrderKey, LinkedList<Order>> getDataStorageByFlag(boolean searchInBuyStorage) {
        return searchInBuyStorage ? orderBookStorage.getOrderBook().getBuyData() : orderBookStorage.getOrderBook().getSellData();
    }

}
