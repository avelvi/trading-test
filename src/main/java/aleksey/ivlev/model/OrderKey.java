package aleksey.ivlev.model;

import java.util.Objects;

public class OrderKey {

    private Integer price;
    private String type;

    public OrderKey(Integer price, String type) {
        this.price = price;
        this.type = type;
    }

    public Integer getPrice() {
        return price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderKey orderKey = (OrderKey) o;

        return Objects.equals(this.price, orderKey.getPrice())
                && Objects.equals(this.type, orderKey.getType());
    }

    @Override
    public int hashCode() {
        int result = price.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }
}
