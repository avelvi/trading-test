package aleksey.ivlev.model;


import java.util.Objects;

public class Order implements Cloneable{

    private Integer id;
    private OrderType type;
    private Integer price;
    private Integer size;

    public Order(Integer id, OrderType type, Integer price, Integer size) {
        this.id = id;
        this.type = type;
        this.price = price;
        this.size = size;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public OrderType getType() {
        return type;
    }

    public void setType(OrderType type) {
        this.type = type;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(this.id, order.getId()) &&
                Objects.equals(this.size, order.getSize()) &&
                this.type == order.getType() &&
                Objects.equals(this.price, order.getPrice());
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, type, price, size);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Order{");
        sb.append("id=").append(id);
        sb.append(", type=").append(type);
        sb.append(", price=").append(price);
        sb.append(", size=").append(size);
        sb.append('}');
        return sb.toString();
    }
}