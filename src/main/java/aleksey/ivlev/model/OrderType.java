package aleksey.ivlev.model;

public enum OrderType {
    B("buyers"),
    S("sellers");

    private final String value;

    OrderType(final String value) {
        this.value = value;
    }

    public static OrderType fromValue(String value) {
        for (OrderType orderType : OrderType.values()) {
            if (orderType.value.equals(value)) {
                return orderType;
            }
        }
        return null;
    }

    public static OrderType fromName(String name) {
        try {
            return OrderType.valueOf(OrderType.class, name.trim().toUpperCase());
        } catch(IllegalArgumentException ex) {

        }
        return null;
    }
}
